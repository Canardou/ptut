package communication;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import communication.ProblemeConnexion;
import labyrinth.Case;

public class ThreadComm extends Thread {

	EntiteeBT recepteur;
	private volatile boolean connected;
	private BluetoothCommPC2 com;
	private Case caseInit;
	private volatile Case caseRecue;
	private volatile boolean envoye;
	private volatile boolean reception;
	private volatile int orientation;
	private volatile Queue<Integer> queueOrdres;
	private int Busy, compteur, typeOrdre;

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
				
				// ...tentative connexion...
				try {
					this.com.connexion();
					this.connected = true;
					this.wait(500);
				} catch (ProblemeConnexion e) {
					this.connected = false;
				} catch (InterruptedException e) {}
			}
			
			// Tant que connected = true...
			while (this.connected) {

				// ...on attend 200ms...
				try {
					this.wait(200);
				} catch (InterruptedException e1) {	}

				// Récuperation du isBusy du robot
				try {
					Trame2 sendIsBusy = new Trame2((byte) this.recepteur.getID(), (byte) Order.SENDBUSY);
					this.com.send(sendIsBusy);
					Trame2 receiveIsBusy = this.com.receive();
					Busy = receiveIsBusy.getOrdre();
					this.connected = true;
					System.out.println("Robot "+this.recepteur.getID()+" : ReceiveBusy : "+Busy);
				} catch (Exception e) {
					System.out.println("Robot "+this.recepteur.getID()+" : EchecBusy");
				}

				if (Busy != 1) {

					// Reception eventuelle
					if(waitReceive) {
						Trame2 orderToSend = new Trame2((byte)  this.recepteur.getID(), (byte) Order.CASETOSEND);
						System.out.println("Robot "+this.recepteur.getID()+" : Emission de l'ordre "+Order.printOrdre(Order.CASETOSEND));
						this.com.send(orderToSend);
						
						Trame2 receiveListCase = null;
						while(receiveListCase==null) {
							receiveListCase = this.com.receive();
						}
						System.out.println("Robot "+this.recepteur.getID()+" : Reception case : "+receiveListCase.toCase());
						waitReceive=false;
						
					} else {
						
						typeOrdre = this.lireOrdre();
						
						if (typeOrdre != -1) {
							System.out.println("Robot "+this.recepteur.getID()+" : Lecture de l'ordre : "+typeOrdre);
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
							if(typeOrdre==Order.CHECKFIRSTCASE) {
								this.envoye = true;
							}
							if(typeOrdre == Order.CASETOSEND
								||typeOrdre == Order.CHECKFIRSTCASE) {
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

	public void setCaseInit(int x, int y, int dir) {
		this.caseInit = new Case(x, y);
		this.orientation = dir;

	}
}
