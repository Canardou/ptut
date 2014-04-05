package robot;

import communication.ComBluetooth;
import communication.EntiteeBT;
import communication.Trame2;

/**
 * Cette classe permet de gerer la tache de communication avec le superviseur
 * @author Thomas
 * @see Thread
 */
public class ThreadCom extends Thread {
	
	/**
	 * Attribut représentant le robot
	 * @see Robot
	 */
	private Robot			robot;
	
	/**
	 * Attribut représentant la communication
	 */
	private	ComBluetooth	com;
	
	/**
	 * Attribut représentant l'entitée de communication
	 */
	private EntiteeBT		entitee;	
	
	/**
	 * Attribut représentant une trame
	 */
	private	Trame2			trame;
	
	/**
	 * Constructeur de ThreadCom
	 * @param r
	 * 		objet robot
	 */
	public ThreadCom(Robot r) {
		super();
		this.robot=r;
		this.entitee= new EntiteeBT("Robot H",(byte)1,"00:16:53:06:DA:CF");
		this.com	= new ComBluetooth(entitee);
		start(); //appel à run() (lancement de la tache)
	}

	/**
	 * Tache de communication
	 */
	public void run() {
		
		// Connexion bluetooth
		this.com.connexion();
		
		while(true) {
			/*
			// On regarde d'abord si on a des données à envoyer
			if(!this.robot.getEnv().getListCase().isEmpty()) {
				//Envoi de la liste de cases
				//...
			}
			// Sinon on se met en attente de reception de données
			else {
			*/
				this.trame=this.com.receive();
				
				if(this.trame.getTypeTrame()==Param.ORDRE) {
					// Ordre a pour valeur {STOP,FORWARD,TURNL,TURNR,TURNB,CALCOMPASS,SAVEREFANGLE,CHECKFIRSTCASE}
					this.robot.getListOrder().addOrder(this.trame.getOrdre());
				}
				else if(this.trame.getTypeTrame()==Param.INITPOSITION) {
					this.robot.getEnv().setInitValues(this.trame.getPosX(), this.trame.getPosY(), this.trame.getDirection());
					this.robot.getListOrder().addOrder(Param.SETPOSITION);
				}
			//}
		}		
	}
}