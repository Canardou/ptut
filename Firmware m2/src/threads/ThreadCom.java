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
	 * Attribut repr�sentant le robot
	 * 
	 * @see Robot
	 */
	private ThreadRobot robot;

	/**
	 * Attribut repr�sentant la communication
	 */
	private ComBluetooth com;

	/**
	 * Attribut repr�sentant l'entit�e de communication
	 */
	private EntiteeBT entitee;

	/**
	 * Attribut repr�sentant une trame
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
		this.setPriority(NORM_PRIORITY);
		
	}

	/**
	 * Tache de communication
	 */
	public void run() {

		while(true) {
			this.com.connexion();
			this.connected=true;
			while (this.connected) {	
				this.trame = this.com.receive();
				if (this.trame != null) {
					
					if (this.trame.getTypeTrame() == ORDRE) {
						if(this.trame.getOrdre()!= Order.CASETOSEND){
						System.out.println(this.trame.getTypeTrame());
						this.robot.getOrder().add(this.trame.getOrdre());
						System.out.println("tCom-R:"+this.robot.getOrder().print(this.trame.getOrdre()));}
						else{
							this.setPriority(MAX_PRIORITY);
							Trame2 cases = new Trame2((byte)0,this.robot.getEnv().getListCase());
							this.setPriority(NORM_PRIORITY);
							this.com.send(cases);
						System.out.println("tCom-E:clear");}
					} else if (this.trame.getTypeTrame() == INITPOSITION) {
						// this.robot.getEnv().setInitValues(this.trame.getPosX(),this.trame.getPosY(), this.trame.getDirection());
						this.robot.getOrder().add(Order.SETPOSITION);
						System.out.println("tCom-R:"+this.robot.getOrder().print(this.trame.getOrdre()));
					}/* else if (this.trame.getTypeTrame() == SENDCASE) {
						Trame2 cases = new Trame2((byte)0,this.robot.getEnv().getListCase());
						this.com.send(cases);
						try {
							cases.printTrame();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("tCom-E:clear");
					}*/
				}
				else {
					System.out.println("tCom:comperdu");
					this.connected=false;
					this.close();
				}
			}
		}
	}
public void close(){
		
		this.setPriority(MIN_PRIORITY);
	    try {
			this.com.fermer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}