package robot;

import communication.ComBluetooth;
import communication.EntiteeBT;
import communication.Trame2;

public class ThreadCom extends Thread {
	
	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/
	private Robot			robot;
	private	ComBluetooth	com;
	private EntiteeBT		entitee;	 
	private	Trame2			trame;
	 
	/**********************************************/
	/**                                          **/
	/**               CONSTRUCTEUR               **/
	/**                                          **/
	/**********************************************/
	public ThreadCom(Robot r) {
		super();
		this.robot=r;
		this.entitee= new EntiteeBT("Robot H",(byte)1,"00:16:53:06:DA:CF");
		this.com	= new ComBluetooth(entitee);
		start(); //appel à run() (lancement de la tache)
	}
	
	/**********************************************/
	/**                                          **/
	/**                   TACHE                  **/
	/**                                          **/
	/**********************************************/
	public void run() {
		// Connexion bluetooth
		this.com.connexion();
		
		while(true) {
			 this.trame=this.com.receive();
			 this.robot.getListOrder().addOrder(this.trame.getOrdre());
		}
	}
	
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/
	
	
		
	
}