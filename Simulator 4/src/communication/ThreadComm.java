package communication;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import communication.ProblemeConnexion;
import labyrinth.Case;

public class ThreadComm extends Thread {

	EntiteeBT recepteur;
	private volatile boolean connected, enMouvement;
	private BluetoothCommPC2 com;
	private Case caseInit;
	private volatile Case caseRecue;
	private volatile boolean envoye;
	private volatile boolean reception;
	private volatile int orientation;
	private volatile Queue<Integer> queueOrdres;
	private int Busy, typeOrdre;

	// constructeur
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

	// methode

	public synchronized void setOrdres(Queue<Integer> ordres) {

		this.queueOrdres.addAll(ordres);
	}

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

				// Récuperation du isBusy du robot
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
								
								// On indique que l'initialisation des robots est terminée
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
								|| typeOrdre == Order.CASETOSEND) {
							
							Trame2 orderToSend = new Trame2((byte)  this.recepteur.getID(), (byte) typeOrdre);
							System.out.println("Robot "+this.recepteur.getID()+" : Emission de l'ordre "+Order.printOrdre(typeOrdre));
							this.com.send(orderToSend);
							
							// Regarder si le PC doit recevoir des données ensuite
							if(typeOrdre == Order.CASETOSEND
								|| typeOrdre == Order.CHECKFIRSTCASE
								|| typeOrdre == Order.FORWARD) {
								waitReceive=true;
							}
							
							// Regarder si on a envoyé un ordre de mouvement
							if(typeOrdre == Order.STOP
									|| typeOrdre == Order.FORWARD
									|| typeOrdre == Order.TURNL
									|| typeOrdre == Order.TURNR
									|| typeOrdre == Order.TURNB
									|| typeOrdre == Order.CHECKFIRSTCASE) {
								this.enMouvement=true;
								System.out.println("Robot "+this.recepteur.getID()+" : Debut de mouvement");
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

	public synchronized int lireOrdre() {
		int i;
		// synchronized(this){
		try {
			i = ((LinkedList<Integer>) this.queueOrdres).getLast();
			((LinkedList<Integer>) this.queueOrdres).removeLast();
			return i;
		} catch (NoSuchElementException e) {
			return -1;
		}
		// }

	}

	public synchronized Case getCaseRecue() {
		// synchronized(this){
		return this.caseRecue;// }
	}

	public synchronized Queue<Integer> getQueue() {
		// synchronized(this){
		return this.queueOrdres;// }
	}

	public synchronized boolean getEnvoye() {
		// synchronized(this){
		boolean b = this.envoye;
		this.envoye = false;
		return b;// }
	}

	public synchronized boolean getReception() {
		// synchronized(this){
		boolean b = this.reception;
		this.reception = false;
		return b;// }
	}

	public synchronized boolean getConnected() {
		// synchronized(this){
		return this.connected;// }

	}
	
	public synchronized boolean getEnMouvement() {
		return this.enMouvement;
	}

	public void setCaseInit(int x, int y, int dir) {
		this.caseInit = new Case(x, y);
		this.orientation = dir;

	}
	
}
