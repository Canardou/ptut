package threads;

import robot.Order;
import communication.*;

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
	 * Attribut représentant le robot
	 * 
	 * @see Robot
	 */
	private ThreadRobot tRobot;
	
	private Trame2 trame;

	/**
	 * Attribut représentant la communication
	 */
	private ComBluetooth com;

	/**
	 * Attribut représentant l'entitée de communication
	 */
	private EntiteeBT entitee;
	
	private boolean connected;

	/**
	 * Constructeur de ThreadCom
	 * @param r
	 *            objet robot
	 */
	public ThreadCom(ThreadRobot r) {
		super();
		this.tRobot = r;
		this.entitee = new EntiteeBT("Robot H", (byte) 1, "00:16:53:06:DA:CF");
		this.com = new ComBluetooth(entitee);
		this.setPriority(5);
	}

	/**
	 * Tache de communication
	 */
	public void run() {

		while(true) {
			this.com.connexion();
			System.out.println("tCom:connexion");
			this.connected=true;
			
			while (this.connected) {	
				this.trame = this.com.receive();
				
				if (this.trame != null) {
					
					if (this.trame.getTypeTrame() == ORDRE) {
						System.out.println("tCom-R:"+this.tRobot.getOrder().print(this.trame.getOrdre()));
						if (this.trame.getOrdre() == Order.CLEARLISTORDER) {
							this.tRobot.getOrder().clear();
						} else if(this.trame.getOrdre() == Order.SENDCASE) {
							System.out.println("tcom-E:cases");
							// Ici il faut faire un send de la liste de case : this.tRobot.getEnv().getListCase();
							this.tRobot.getEnv().getListCase().clear();						
						} else if(this.trame.getOrdre() == Order.SENDBUSY) {
							System.out.println("tcom-E:isbusy");
							// ici faut faire un send de la valeur this.tRobot.getOrder().getIsBusy()
						} else {
							this.tRobot.getOrder().add(this.trame.getOrdre());							
						}
						
					} else if (this.trame.getTypeTrame() == INITPOSITION) {
						//this.robot.getEnv().setInitPos(this.trame.getPosX(),this.trame.getPosY(), this.trame.getDirection());
						this.tRobot.getOrder().add(Order.SETPOSITION);
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