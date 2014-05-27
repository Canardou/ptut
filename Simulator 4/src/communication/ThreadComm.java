package communication;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import communication.ProblemeConnexion;
import labyrinth.Case;




public class ThreadComm extends Thread{
	
	
	
	EntiteeBT recepteur;
	private volatile boolean connected;
	private BluetoothCommPC2 com;
	private Case caseInit;
	private volatile Case caseRecue ;
	private volatile boolean envoye;
	private volatile boolean reception;
	private volatile int orientation;
	private volatile Queue<Integer> queueOrdres ;
	


	

	
	//constructeur
	public ThreadComm(EntiteeBT robot, Case caseinit, int orientation) { 
		this.recepteur= robot;
		InfoEntitee IE = new InfoEntitee() ;
		this.com= new BluetoothCommPC2(IE.PCkiwor, this.recepteur);
		this.caseInit=caseinit;
		this.orientation = orientation;
		System.out.println("Robot "+robot.getID()+" direction = "+this.orientation);
		this.queueOrdres =new LinkedList<Integer>();
		this.caseRecue = new Case(-1,-1);
		this.envoye = false;
		this.reception = false;

	}
	
	
	// m�thode
	
	public void setOrdres(Queue<Integer> ordres){
		synchronized(this){
		this.queueOrdres.addAll(ordres);}
	}
	
	@Override
	public void run(){
		
		while(true){
			
				try{this.com.connexion();
				synchronized(this){
				this.connected=true;
				}}
				catch(ProblemeConnexion e){
					synchronized(this){
					this.connected=false;}
				}
				
				/*
				if(this.recepteur.getID()==0) {
					Trame2 sendIsBusy= new Trame2((byte)0,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
					this.com.send (sendIsBusy);
				} else if (this.recepteur.getID()==1) {
					Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
					this.com.send (sendIsBusy);
				} else if (this.recepteur.getID()==2) {
					Trame2 sendIsBusy= new Trame2((byte)2,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
					this.com.send (sendIsBusy);					
				}
				
				Trame2 receiveIsBusy = this.com.receive();
				
				int Busy=-2;
				while(Busy==-2){
				try{
				receiveIsBusy = this.com.receive();
				Busy=receiveIsBusy.getBusy();
				System.out.println("Demande isBusy success" );
				}
				catch(Exception e){
					//this.connected = false;
				}
				}
				int typeOrdre;
				//System.out.println("robot connecté : ");
				
				//
				//Initialisation du robot
				//
				

				//System.out.println("Demande isBusy" );
				*/
				
				while(this.connected){
					//System.out.println(this.connected);
					
					try {
						this.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// Demande au robot s'il est occup� et reception
					
					if(this.recepteur.getID()==0) {
						Trame2 sendIsBusy= new Trame2((byte)0,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
						this.com.send (sendIsBusy);
					} else if (this.recepteur.getID()==1) {
						Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
						this.com.send (sendIsBusy);
					} else if (this.recepteur.getID()==2) {
						Trame2 sendIsBusy= new Trame2((byte)2,(byte)Order.SENDBUSY);  // ajouter dans Ordre et g�rer cette commande dans robot
						this.com.send (sendIsBusy);					
					}
					
					int Busy=-2;
					while(Busy==-2){
					try{
					Trame2 receiveIsBusy = this.com.receive();
					Busy=receiveIsBusy.getBusy();
				//	System.out.println("Demande isBusy success bis" );
					synchronized(this){
					this.connected = true;}
					}
					catch(Exception e){
						synchronized(this){
						this.connected = false;}
						}
					}
					int typeOrdre;
					if (Busy!=1){
						
						synchronized(this){
						typeOrdre = this.lireOrdre();}
						if(typeOrdre !=-1){
							System.out.println("Je lis mes odres" );
							System.out.println("Ordre: "+typeOrdre );}
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
						synchronized(this){
						if(this.recepteur.getID()==0) {
							Trame2 sendPositionInit  = new Trame2((byte)0, this.caseInit, this.orientation);
							System.out.println("Robot 0 envoie case : "+caseInit.toString());
							this.com.send (sendPositionInit);
						} else if (this.recepteur.getID()==1) {
							Trame2 sendPositionInit  = new Trame2((byte)1, this.caseInit, this.orientation);
							System.out.println("Robot 1 envoie case : "+caseInit.toString());
							this.com.send (sendPositionInit);
						} else if (this.recepteur.getID()==2) {
							Trame2 sendPositionInit  = new Trame2((byte)2, this.caseInit, this.orientation);
							System.out.println("Robot 3 envoie case : "+caseInit.toString());
							this.com.send (sendPositionInit);						
						}
						}
						
						
						System.out.println("Position initiale OK" );
						
						break;
						
						case Order.CHECKFIRSTCASE:
						// Regarde les murs autour de lui CHECkFIRSTCASE
						if(this.recepteur.getID()==0) {
							Trame2 sendCheckCase= new Trame2((byte)0,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);
							synchronized(this){
								this.envoye =true;
							}
						} else if (this.recepteur.getID()==1) {
							Trame2 sendCheckCase= new Trame2((byte)1,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);
							synchronized(this){
								this.envoye =true;
							}
						} else if (this.recepteur.getID()==2) {
							Trame2 sendCheckCase= new Trame2((byte)2,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);	
							synchronized(this){
								this.envoye =true;
							}
							Trame2 receiveListCase=this.com.receive();
							if(receiveListCase != null){
								synchronized(this){
								caseRecue = receiveListCase.toCase();}
								this.reception = true ;
							}
							else{
								System.out.println(" Rien reçu!");
								synchronized(this){
								caseRecue = null ;}
							}
						}
						
						System.out.println("Detection murs case1 OK" );
						
						break;
						
						
						case Order.STOP:
							// envoie ordre STOP
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.STOP);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.STOP);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.STOP);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.FORWARD:
							// Demande au robot d'avancer
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.FORWARD);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.FORWARD);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.FORWARD);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.TURNL:
							// Demande au robot de tourner à gauche
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNL);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNL);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNL);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.TURNR:
							// Demande au robot de tourner à droite
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNR);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNR);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNR);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.TURNB:
							// Demande au robot de faire demi tour
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNB);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNB);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNB);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.CALCOMPASS:
							// Demande au robot de calibrer la boussole
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CALCOMPASS);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CALCOMPASS);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CALCOMPASS);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.CLEARLISTORDER:
							// Demande au robot de vider le buffer d'ordres non executes
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CLEARLISTORDER);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CLEARLISTORDER);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CLEARLISTORDER);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.WAITBUTTON:
							// Demande au robot d'attendre un appuie du bouton
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAITBUTTON);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAITBUTTON);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAITBUTTON);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.WAIT1SEC:
							// Demande au robot d'attendre 1 seconde
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAIT1SEC);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAIT1SEC);  // ajouter dans Ordre et g�rer cette commande dans robot
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAIT1SEC);  // ajouter dans Ordre et g�rer cette commande dans robot
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
							synchronized(this){
							caseRecue = receiveListCase.toCase();}
							this.reception = true ;
						}
						else{
							System.out.println(" Rien reçu!");
							synchronized(this){
							caseRecue = null ;}
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
		synchronized(this){
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
	
	public Case getCaseRecue(){
		synchronized(this){
		return this.caseRecue;}
	}
	
	public Queue<Integer> getQueue(){
		synchronized(this){
		return this.queueOrdres;}
	}

	public boolean getEnvoye(){
		synchronized(this){
		boolean b =this.envoye;
		this.envoye = false ;
		return b;}
	}
	public boolean getReception(){
		synchronized(this){
		boolean b =this.reception;
		this.reception = false ;
		return b;}
	}
	
	public boolean getConnected(){
		synchronized(this){
		return this.connected;}
		
	}
	
	public void setCaseInit(int x, int y, int dir){
		this.caseInit = new Case(x,y);
		this.orientation = dir ;
		
	}
}
	
	
	
	
	


