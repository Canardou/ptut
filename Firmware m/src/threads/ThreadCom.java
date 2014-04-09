package threads;

import communication.*;
import orders.*;

/**
 * Cette classe permet de gerer la tache de communication avec le superviseur
 * @author Thomas
 * @see Thread
 */
public class ThreadCom extends Thread {

	/**
	 * Type de trame : ordre
	 */
	public static final int ORDRE = 5;

	/**
	 * Type de trame : initialisation de la position/direction
	 */
	public static final int INITPOSITION = 2;

	/**
	 * Type de trame : demande de transmission de la liste de cases
	 */
	public static final int SENDCASE = 1;

	/**
	 * Attribut représentant le robot
	 * 
	 * @see Robot
	 */
	private ThreadRobot robot;

	/**
	 * Attribut représentant la communication
	 */
	private ComBluetooth com;

	/**
	 * Attribut représentant l'entitée de communication
	 */
	private EntiteeBT entitee;

	/**
	 * Attribut représentant une trame
	 */
	private Trame2 trame;
	
	private boolean connected;

	/**
	 * Constructeur de ThreadCom
	 * @param r
	 *            objet robot
	 */
	public ThreadCom(ThreadRobot r) {
		super();
		this.robot = r;
		//this.entitee = new EntiteeBT("Robot H", (byte) 1, "00:16:53:06:DA:CF");
		this.entitee = new EntiteeBT("Robot J", (byte) 1, "00:16:53:06:F5:30");
		
		this.com = new ComBluetooth(entitee);
		this.setPriority(5);
	}

	/**
	 * Tache de communication
	 */
	public void run() {

		while(true) {
			this.com.connexion();
			this.connected=true;
			
			while (this.connected) {	
				/*this.trame = this.com.receive();
				if (this.trame != null) {
					if (this.trame.getTypeTrame() == ORDRE) {
						this.robot.getOrder().add(this.trame.getOrdre());
						System.out.println("tCom-R:"+this.robot.getOrder().print(this.trame.getOrdre()));
					} else if (this.trame.getTypeTrame() == INITPOSITION) {
						// this.robot.getEnv().setInitValues(this.trame.getPosX(),this.trame.getPosY(), this.trame.getDirection());
						this.robot.getOrder().add(Order.SETPOSITION);
						System.out.println("tCom-R:"+this.robot.getOrder().print(this.trame.getOrdre()));
					} else if (this.trame.getTypeTrame() == SENDCASE) {
						*/ this.com.send(new Trame2((byte)0,this.robot.getEnv().getListCase()));
						System.out.println("tCom-E:clear");
				/*	}
				}
				else {
					System.out.println("tCom:comperdu");
					this.connected=false;
				}*/
			}
		}
	}
}