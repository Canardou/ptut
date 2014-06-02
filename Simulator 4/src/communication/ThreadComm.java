package communication;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import communication.ProblemeConnexion;
import labyrinth.Case;

/**
 * 
 * Cette classe est un thread qui g�re la communication c�t� PC
 *
 */
public class ThreadComm extends Thread {

	// ------------------------------------- ATTRIBUTS--------------------------------------------
	/**
	 * Entit�e de type robot.
	 */
	EntiteeBT recepteur;
	/**
	 *Etat de connexion.
	 *Etat du robot.
	 */
	private volatile boolean connected, enMouvement;
	
	/**
	 * Attribut repr�sentant la communication.
	 */
	private BluetoothCommPC2 com;
	
	/**
	 * Coordonn�es et direction initiales du robot.
	 */
	private Case caseInit;
	
	/**
	 * Cases explor�es par le robot.
	 */
	private volatile Case caseRecue;
	
	/**
	 * Condition � vrai quand on a fini d'envoyer tous les ordres d'initialisation .
	 */
	private volatile boolean envoye;
	
	/**
	 * Condition � vrai quand une trame doit �tre re�ue.
	 */
	private volatile boolean reception;
	/**
	 * Direction des robots.
	 */
	private volatile int orientation;
	
	/** 
	 * Queue �num�rant les ordres.
	 */
	private volatile Queue<Integer> queueOrdres;
	
	/**
	 * Etat robot.
	 * Orde.
	 */
	private int Busy, typeOrdre;

	// ------------------------------------- CONSTRUCTEURS--------------------------------------------
	/**
	 * Constructeur de ThreadComm.
	 * @param robot
	 * @param caseinit
	 * @param orientation
	 */
	public ThreadComm(EntiteeBT robot, Case caseinit, int orientation) {
		this.recepteur = robot;
		InfoEntitee IE = new InfoEntitee();
		this.com = new BluetoothCommPC2(IE.PCkiwor, this.recepteur);
		this.caseInit = caseinit;
		this.orientation = orientation;
		System.out.println("Robot " + robot.getID() + " direction = "+ this.orientation);
		this.queueOrdres = new LinkedList<Integer>();
		this.caseRecue = new Case(-1, -1);
		this.envoye = false;
		this.reception = false;
		this.Busy = 1;
		this.enMouvement=false;

	}

	// ------------------------------------- METHODES--------------------------------------------

	/**
	 * Mise � jour de la queue d'ordres.
	 * @param ordres
	 */
	public synchronized void setOrdres(Queue<Integer> ordres) {

		((LinkedList<Integer>) this.queueOrdres).addAll(0,ordres);
	}

	/**
	 * Tache de communication.
	 */
	@Override
	public synchronized void run() {
		
		boolean waitReceive = false;
		
		// Pour toujours:
		while (true) {

			if (this.connected != true) {
				// Connexion
				try {
					System.out.println("Robot "+this.recepteur.getID()+" : Connexion tentative");
					this.com.connexion();
					System.out.println("Robot "+this.recepteur.getID()+" : Connexion reussi");
					this.connected = true;
				} catch (ProblemeConnexion e1) {
					System.out.println("Robot "+this.recepteur.getID()+" : Connexion echec");
					this.connected = false;
					try {
						this.wait(500);
					} catch (InterruptedException e2) {}
				}
			}
			

			while (this.connected) {
				
				// Periode des emissions/receptions
				try {
					this.wait(200);
				} catch (InterruptedException e1) {}

				// R�cuperation du isBusy du robot
				try {
					Trame2 sendIsBusy = new Trame2((byte) this.recepteur.getID(), (byte) Order.SENDBUSY);
					this.com.send(sendIsBusy);
					Trame2 receiveIsBusy = this.com.receive();
					Busy = receiveIsBusy.getOrdre();
					//System.out.println("Robot "+this.recepteur.getID()+" : Reception isBusy "+Busy);
				} catch (Exception e) {
					System.out.println("Robot "+this.recepteur.getID()+" : Probleme de reception isBusy");
					this.connected=false;					
				}
				
				// Si le robot est libre
				if (Busy != 1 && this.connected) {

					if(this.enMouvement){ 
						this.enMouvement=false;
						System.out.println("Robot "+this.recepteur.getID()+" : Fin de mouvement");
					}
					
					// Reception eventuelle
					if(waitReceive) {
						Trame2 orderToSend = new Trame2((byte)  this.recepteur.getID(), (byte) Order.CASETOSEND);
						System.out.println("Robot "+this.recepteur.getID()+" : Emission de l'ordre "+Order.printOrdre(Order.CASETOSEND));
						this.com.send(orderToSend);
						
						Trame2 receiveListCase = null;
						while(receiveListCase==null) {
							try {
								receiveListCase = this.com.receive();
								System.out.println("Robot "+this.recepteur.getID()+" : Reception case : "+receiveListCase.toCase());
								this.caseRecue = receiveListCase.toCase();
								
								// On indique que l'initialisation des robots est termin�e
								if(this.envoye==false) {
									this.envoye = true;
								}
								waitReceive=false;
							} catch (Exception e) {
								System.out.println("Robot "+this.recepteur.getID()+" : Probleme de reception case");
							}
						}
					}
					
					else {
						typeOrdre = this.lireOrdre();
						
						if (typeOrdre != -1) {
							System.out.println("Robot "+this.recepteur.getID()+" : Lecture de l'ordre a envoyer : "+Order.printOrdre(typeOrdre));
						}
						
						// Regarder si on a envoy� un ordre de mouvement
						if(typeOrdre == Order.STOP
								|| typeOrdre == Order.FORWARD
								|| typeOrdre == Order.TURNL
								|| typeOrdre == Order.TURNR
								|| typeOrdre == Order.TURNB
								|| typeOrdre == Order.CHECKFIRSTCASE) {
							this.enMouvement=true;
							System.out.println("Robot "+this.recepteur.getID()+" : Debut de mouvement");
						}
						
						// Emission de l'ordre
						if(typeOrdre == Order.STOP
								|| typeOrdre == Order.FORWARD
								|| typeOrdre == Order.TURNL
								|| typeOrdre == Order.TURNR
								|| typeOrdre == Order.TURNB
								|| typeOrdre == Order.CALCOMPASS
								|| typeOrdre == Order.SAVEREFANGLE
								|| typeOrdre == Order.CHECKFIRSTCASE
								|| typeOrdre == Order.CLEARLISTORDER
								|| typeOrdre == Order.WAITBUTTON
								|| typeOrdre == Order.WAIT1SEC
								|| typeOrdre == Order.CASETOSEND
								|| typeOrdre == Order.MISSION_TERMINATE) {
							
							Trame2 orderToSend = new Trame2((byte)  this.recepteur.getID(), (byte) typeOrdre);
							System.out.println("Robot "+this.recepteur.getID()+" : Emission de l'ordre "+Order.printOrdre(typeOrdre));
							this.com.send(orderToSend);
							
							// Regarder si le PC doit recevoir des donn�es ensuite
							if(typeOrdre == Order.CASETOSEND
								|| typeOrdre == Order.CHECKFIRSTCASE
								|| typeOrdre == Order.FORWARD) {
								waitReceive=true;
							}
							
						
							
						} else if(typeOrdre == Order.SETPOSITION) {
							Trame2 orderToSend = new Trame2((byte) this.recepteur.getID(), this.caseInit, this.orientation);
							System.out.println("Robot "+this.recepteur.getID()+" : Emission de l'ordre SETPOSITION : "+ this.caseInit + ", " + this.orientation);
							this.com.send(orderToSend);
						}
						
					}
					
					
	
				}
			}
		}
	}
	/**
	 * R�cup�rer le dernier ordre dans la queue et le supprimer.
	 * @return int
	 */

	public synchronized int lireOrdre() {
		int i;
		try {
			i = ((LinkedList<Integer>) this.queueOrdres).getLast();
			((LinkedList<Integer>) this.queueOrdres).removeLast();
			return i;
		} catch (NoSuchElementException e) {
			return -1;
		}

	}

	/**
	 * R�cup�rer case.
	 * @return Case
	 */
	public synchronized Case getCaseRecue() {
		return this.caseRecue;
	}

	/**
	 * R�cup�rer la queue d'ordres.
	 * @return queue<Integer>
	 */
	public synchronized Queue<Integer> getQueue() {
		return this.queueOrdres;
	}

	/**
	 * R�cup�rer la vairiable envoye.
	 * @return boolean
	 */
	public synchronized boolean getEnvoye() {
		boolean b = this.envoye;
		this.envoye = false;
		return b;
	}
	/**
	 * R�cup�rer la vairiable reception.
	 * @return boolean
	 */
	public synchronized boolean getReception() {
		// synchronized(this){
		boolean b = this.reception;
		this.reception = false;
		return b;// }
	}
	/**
	 * R�cup�rer l'�tat de la connexion.
	 * @return boolean
	 */
	public synchronized boolean getConnected() {
		
		return this.connected;
	}
	
	/**
	 * R�cup�rer l'�tat du robot.
	 * @return boolean
	 */
	public synchronized boolean getEnMouvement() {
		return this.enMouvement;
	}

	/**
	 * R�cup�rer la position initiale du robot.
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void setCaseInit(int x, int y, int dir) {
		this.caseInit = new Case(x, y);
		this.orientation = dir;

	}
	
}
