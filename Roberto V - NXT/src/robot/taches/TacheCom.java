package robot.taches;

import lejos.nxt.comm.Bluetooth;
import robot.communication.*;
import robot.evenements.Ordre;

/**
 * Cette classe permet de gerer la communication avec le superviseur.
 * 
 * @author Thomas
 */
public class TacheCom extends Thread {
	
	// ------------------------------------- CONSTANTES -------------------------------------------

	/**
	 * Type de trame : ordre
	 */
	public static final int TYPE_ORDRE = 5;

	/**
	 * Type de trame : initialisation de la position/direction
	 */
	public static final int TYPE_INITPOSITION = 2;
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------

	/**
	 * Tache principale du robot.
	 */
	private TachePrincipale tPrincipale;

	/**
	 * Attribut représentant la communication.
	 */
	private ComBluetooth com;
	
	/**
	 * Etat de la connexion.
	 */
	private boolean connected;
	
	/**
	 * Identifiant du robot.
	 */
	private int idRobot;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur de TacheCom.
	 * 
	 * @param tPrincipaleInit
	 */
	public TacheCom(TachePrincipale tPrincipaleInit) {
		super();
		this.tPrincipale = tPrincipaleInit;
		
		if(Bluetooth.getFriendlyName().equals("Robot F")) {
			this.idRobot=0;
		} else if(Bluetooth.getFriendlyName().equals("Robot H")) {
			this.idRobot=1;
		} else if(Bluetooth.getFriendlyName().equals("Robot J")) {
			this.idRobot=2;
		} else {
			System.out.println("tCom:errId");
		}	
		EntiteeBT entitee;
		entitee = new EntiteeBT(Bluetooth.getFriendlyName(), (byte) this.idRobot, Bluetooth.getLocalAddress());
		
		this.com = new ComBluetooth(entitee);
		this.setPriority(5);		
	}
	
	// ------------------------------------- TACHE ------------------------------------------------

	/**
	 * Tache de communication.
	 */
	public void run() {
		
		Trame2 trame;

		while(true) {
			System.out.println("tCom:Attente co.");
			try {
				this.com.connexion();
				System.out.println("tCom:Co. OK");
				this.connected=true;
			} catch (Exception e) {
				 System.out.println("tCom:Echec co.");
			}
			
			while (this.connected) {	
				trame = this.com.receive();
				
				if (trame != null) {
					
					if (trame.getTypeTrame() == TYPE_ORDRE) {
						if( trame.getOrdre() != Ordre.ENVOYER_ISBUSY ) {
							System.out.println("tCom-R:"+this.tPrincipale.getOrdre().printOrdre(trame.getOrdre()));
						}
						if (trame.getOrdre() == Ordre.VIDER_ORDRES) {
							this.tPrincipale.getOrdre().vider();
						} else if(trame.getOrdre() == Ordre.ENVOYER_CASE) {
							this.com.send(new Trame2((byte) this.idRobot,this.tPrincipale.getEnv().getListCase()));
							this.tPrincipale.getEnv().getListCase().vider();
						} else if(trame.getOrdre() == Ordre.ENVOYER_ISBUSY) {
							this.com.send(new Trame2((byte) this.idRobot,(byte)this.tPrincipale.getOrdre().getIsBusy()));
						} else {
							this.tPrincipale.getOrdre().ajouterOrdre(trame.getOrdre());							
						}
						
					} else if (trame.getTypeTrame() == TYPE_INITPOSITION) {
						System.out.println("tCom-R:setPos");
						this.tPrincipale.getEnv().setInitPos(trame.getPosX(),trame.getPosY(), trame.getDirection());
						this.tPrincipale.getOrdre().ajouterOrdre(Ordre.SETPOSITION);
					}
				}
				else {
					this.connected=false;
				}
			}
		}
	}
}