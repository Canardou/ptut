package communication;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import labyrinth.Case;




public class ThreadComm extends Thread{
	
	
	
	EntiteeBT recepteur;
	private boolean connected;
	private BluetoothCommPC2 com;
	private Case caseInit;
	private Case caseRecue ;
	private int orientation;
	private Queue<Integer> queueOrdres ;
	private boolean busy;

	

	
	//constructeur
	public ThreadComm(EntiteeBT robot, Case caseinit, int orientation) { 
		this.recepteur= robot;
		InfoEntitee IE = new InfoEntitee() ;
		this.com= new BluetoothCommPC2(IE.PCkiwor, this.recepteur);
		this.caseInit=caseinit;
		this.orientation = orientation;
		this.queueOrdres =new LinkedList<Integer>();
		this.caseRecue = new Case(-1,-1);

	}
	
	
	// m�thode
	
	public void setOrdres(Queue<Integer> ordres){
		this.queueOrdres = ordres;
	}
	
	@Override
	public void run(){
		
		while(true){
			
			//connexion PC -> robot
			
			this.com.connexion();
			this.connected=true;
				System.out.println("robot connecté : ");
				
				//
				//Initialisation du robot
				//
				

				System.out.println("Demande isBusy OK" );
				Trame2 receiveIsBusy = this.com.receive();
				int Busy=receiveIsBusy.getBusy();
				int typeOrdre;
				
				while(this.connected){

					System.out.println("Demande isBusy OK" );
					receiveIsBusy = this.com.receive();
					Busy=receiveIsBusy.getBusy();
					
					if (Busy!=1){
						typeOrdre = this.lireOrdre();
						switch(typeOrdre){

						//Angle de r�f�rence
						case Order.SAVEREFANGLE: 
						
						if(this.recepteur.getID()==0) {
							Trame2 sendAngleRef= new Trame2((byte)0,(byte)Order.SAVEREFANGLE);
							this.com.send (sendAngleRef);
						} else if (this.recepteur.getID()==1) {
							Trame2 sendAngleRef= new Trame2((byte)1,(byte)Order.SAVEREFANGLE);
							this.com.send (sendAngleRef);
						} else if (this.recepteur.getID()==2) {
							Trame2 sendAngleRef= new Trame2((byte)2,(byte)Order.SAVEREFANGLE);
							this.com.send (sendAngleRef);						
						}
						
						System.out.println("angleRef OK" );
						break;
	
						case Order.SETPOSITION:
						if(this.recepteur.getID()==0) {
							Trame2 sendPositionInit  = new Trame2((byte)0, caseInit,orientation);
							this.com.send (sendPositionInit);
						} else if (this.recepteur.getID()==1) {
							Trame2 sendPositionInit  = new Trame2((byte)1, caseInit,orientation);
							this.com.send (sendPositionInit);
						} else if (this.recepteur.getID()==2) {
							Trame2 sendPositionInit  = new Trame2((byte)2, caseInit,orientation);
							this.com.send (sendPositionInit);						
						}
						
						
						System.out.println("Position initiale OK" );
						
						break;
						
						case Order.CHECKFIRSTCASE:
						// Regarde les murs autour de lui CHECkFIRSTCASE
						if(this.recepteur.getID()==0) {
							Trame2 sendCheckCase= new Trame2((byte)0,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);
						} else if (this.recepteur.getID()==1) {
							Trame2 sendCheckCase= new Trame2((byte)1,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);
						} else if (this.recepteur.getID()==2) {
							Trame2 sendCheckCase= new Trame2((byte)2,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);						
						}
						
						System.out.println("Detection murs case1 OK" );
						
						break;
						
						case Order.SENDBUSY:
						// Demande au robot s'il est occup� et reception
						if(this.recepteur.getID()==0) {
							Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
							this.com.send (sendIsBusy);
						} else if (this.recepteur.getID()==1) {
							Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
							this.com.send (sendIsBusy);
						} else if (this.recepteur.getID()==2) {
							Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
							this.com.send (sendIsBusy);					
						}
						
						break;
						// demande et reception de la liste des cases explor�es

						case Order.CASETOSEND:
						
						if(this.recepteur.getID()==0) {
							this.com.send (new Trame2((byte)0,(byte)Order.CASETOSEND));
						} else if (this.recepteur.getID()==1) {
							this.com.send (new Trame2((byte)1,(byte)Order.CASETOSEND));
						} else if (this.recepteur.getID()==2) {
							this.com.send (new Trame2((byte)2,(byte)Order.CASETOSEND));						
						}
						
						Trame2 receiveListCase=this.com.receive();
						if(receiveListCase != null){	
							caseRecue = receiveListCase.toCase();
						}
						else{
							System.out.println(" Rien reçu!");
							caseRecue = null ;
						}
						break;
						
						case -1:
							//LULZ NUTHIGN TU DO !!
						break;
					}
				}
			}
		}			
	}
	
	public int lireOrdre(){
		int i ;
		try{
		i = ((LinkedList<Integer>)this.queueOrdres).getLast();
		((LinkedList<Integer>)this.queueOrdres).removeLast();
		return i;
		}
		catch(NoSuchElementException e){
			return -1;
		}
	
	}
	
}
	
	
	
	
	


