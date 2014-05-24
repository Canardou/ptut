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
	 * Attribut permettant de gerer les trames a envoyer et a recevoir.
	 */
	private Trame2 trame;

	/**
	 * Attribut représentant la communication.
	 */
	private ComBluetooth com;

	/**
	 * Attribut représentant l'entitée de communication.
	 */
	private EntiteeBT entitee;
	
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
		
		this.entitee = new EntiteeBT(Bluetooth.getFriendlyName(), (byte) this.idRobot, Bluetooth.getLocalAddress());
		
		this.com = new ComBluetooth(entitee);
		this.setPriority(5);		
	}
	
	// ------------------------------------- TACHE ------------------------------------------------

	/**
	 * Tache de communication.
	 */
	public void run() {

		while(true) {
			this.com.connexion();
			System.out.println("tCom:connexion");
			this.connected=true;
			
			while (this.connected) {	
				this.trame = this.com.receive();
				
				if (this.trame != null) {
					
					if (this.trame.getTypeTrame() == TYPE_ORDRE) {
						System.out.println("tCom-R:"+this.tPrincipale.getOrdre().printOrdre(this.trame.getOrdre()));
						if (this.trame.getOrdre() == Ordre.VIDER_ORDRES) {
							this.tPrincipale.getOrdre().vider();
						} else if(this.trame.getOrdre() == Ordre.ENVOYER_CASE) {
							System.out.println("tcom-E:cases");
							this.com.send(new Trame2((byte) this.idRobot,this.tPrincipale.getEnv().getListCase()));
							this.tPrincipale.getEnv().getListCase().vider();
						} else if(this.trame.getOrdre() == Ordre.ENVOYER_ISBUSY) {
							//System.out.println("tcom-E:isbusy");
							this.com.send(new Trame2((byte) this.idRobot,this.tPrincipale.getOrdre().getIsBusy()));
						} else {
							this.tPrincipale.getOrdre().ajouterOrdre(this.trame.getOrdre());							
						}
						
					} else if (this.trame.getTypeTrame() == TYPE_INITPOSITION) {
						this.tPrincipale.getEnv().setInitPos(this.trame.getPosX(),this.trame.getPosY(), this.trame.getDirection());
						this.tPrincipale.getOrdre().ajouterOrdre(Ordre.SETPOSITION);
						System.out.println("tCom-R:setPos");
					}
				}
				else {
					System.out.println("tCom:comperdu");
					this.connected=false;
				}
			}
		}
	}
}