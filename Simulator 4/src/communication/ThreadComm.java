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
	private int Busy, compteur, typeOrdre;
	


	

	
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
		this.Busy=-2;
		this.compteur=0;

	}
	
	
	// methode
	
	public synchronized void setOrdres(Queue<Integer> ordres){
		
		this.queueOrdres.addAll(ordres);
	}
	
	@Override
	public synchronized void run(){
		
		// Pour toujours:
		while(true){
				try {
					
					this.wait(250);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//...tentative connexion...
				try{
					this.com.connexion();
					//...si ça réussit...
					//synchronized(this){
						//...connected <= true...
							this.connected=true;
					//}
					//...et on attend 1/2 seconde...
							this.wait(500);}
				
				//...sinon connected <= false
				catch(ProblemeConnexion e){
				//	synchronized(this){
						this.connected=false;//}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//Tant que connected = true...
				while(this.connected){
					//...on attend 100ms...
					try {
						this.wait(100);
					} 
					catch (InterruptedException e1) {
						
						e1.printStackTrace();
					}
					// ...on demande au robot s'il est occupé... et reception de sa réponse...
					
					if(this.recepteur.getID()==0) {
						Trame2 sendIsBusy= new Trame2((byte)0,(byte)Order.SENDBUSY);  
					} else if (this.recepteur.getID()==1) {
						Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.SENDBUSY);  
					} else if (this.recepteur.getID()==2) {
						Trame2 sendIsBusy= new Trame2((byte)2,(byte)Order.SENDBUSY);
						this.com.send (sendIsBusy);					
					}
					
					while(Busy==-2 && compteur < 10){
						try {
							this.wait(100);
						}catch (InterruptedException e1) {
						
						//	e1.printStackTrace();
						}
					
						try{
							Trame2 receiveIsBusy = this.com.receive();
							Busy=receiveIsBusy.getBusy();
							//synchronized(this){
								this.connected = true;//}
							}
						catch(Exception e){
							//synchronized(this){
							compteur ++;//}
						}
					}
					
					if(compteur>=10){
						this.connected = false;
						this.compteur=0;
						System.out.println("Robot " + this.recepteur.getID()+" Compteur explosé");
					}
						
					if (Busy!=1){
						
						//synchronized(this){
						typeOrdre = this.lireOrdre();//}
						if(typeOrdre !=-1){
							System.out.println("Robot " + this.recepteur.getID()+": je lis mes odres" );
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
						
						System.out.println("Robot " + this.recepteur.getID()+": angleRef OK" );
						break;
	
						case Order.SETPOSITION:
						//synchronized(this){
						if(this.recepteur.getID()==0) {
							Trame2 sendPositionInit  = new Trame2((byte)0, this.caseInit, this.orientation);
							System.out.println("Robot " +this.recepteur.getID()+ " init case, trame = "+sendPositionInit.toString());
							this.com.send (sendPositionInit);
						} else if (this.recepteur.getID()==1) {
							Trame2 sendPositionInit  = new Trame2((byte)1, this.caseInit, this.orientation);
							System.out.println("Robot " +this.recepteur.getID()+ " init case, trame = "+sendPositionInit.toString());
							this.com.send (sendPositionInit);
						} else if (this.recepteur.getID()==2) {
							Trame2 sendPositionInit  = new Trame2((byte)2, this.caseInit, this.orientation);
							System.out.println("Robot " +this.recepteur.getID()+ " init case, trame = "+sendPositionInit.toString());
							this.com.send (sendPositionInit);						
						}
						//}
						
						
						System.out.println("Robot " + this.recepteur.getID()+": Position initiale OK" );
						
						break;
						
						case Order.CHECKFIRSTCASE:
						// Regarde les murs autour de lui CHECkFIRSTCASE
						if(this.recepteur.getID()==0) {
							Trame2 sendCheckCase= new Trame2((byte)0,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);
							//synchronized(this){
								this.envoye =true;
							//}
						} else if (this.recepteur.getID()==1) {
							Trame2 sendCheckCase= new Trame2((byte)1,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);
							//synchronized(this){
								this.envoye =true;
							//}
						} else if (this.recepteur.getID()==2) {
							Trame2 sendCheckCase= new Trame2((byte)2,(byte)Order.CHECKFIRSTCASE);
							this.com.send (sendCheckCase);	
							//synchronized(this){
								this.envoye =true;
							//}
							Trame2 receiveListCase=this.com.receive();
							
							
							try {
								this.wait(200);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							if(receiveListCase != null){
								//synchronized(this){
								caseRecue = receiveListCase.toCase();//}
								this.reception = true ;
								System.out.println("Robot " + this.recepteur.getID()+ ": Detection murs case1 OK" );
								System.out.println("Robot " +this.recepteur.getID()+ " case checked, trame = "+receiveListCase.toString());
								System.out.println("Robot " +this.recepteur.getID()+ "case = "+receiveListCase.toCase().toString());
							}
							else{
								System.out.println("Robot " + this.recepteur.getID()+": Rien reçu!");
								//synchronized(this){
								caseRecue = null ;//}
							}
							
						}
						
						
						
						break;
						
						
						case Order.STOP:
							// envoie ordre STOP
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.STOP);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.STOP);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.STOP);  
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.FORWARD:
							// Demande au robot d'avancer
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.FORWARD);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.FORWARD); 
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.FORWARD); 
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.TURNL:
							// Demande au robot de tourner à gauche
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNL);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNL);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNL);  
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.TURNR:
							// Demande au robot de tourner à droite
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNR);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNR);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNR);  
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.TURNB:
							// Demande au robot de faire demi tour
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNB);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNB);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.TURNB);  
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.CALCOMPASS:
							// Demande au robot de calibrer la boussole
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CALCOMPASS);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CALCOMPASS);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CALCOMPASS);  
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.CLEARLISTORDER:
							// Demande au robot de vider le buffer d'ordres non executes
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CLEARLISTORDER);
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CLEARLISTORDER);
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.CLEARLISTORDER);
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.WAITBUTTON:
							// Demande au robot d'attendre un appuie du bouton
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAITBUTTON);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAITBUTTON);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAITBUTTON);  
								this.com.send (sendIsBusy);					
							}
							
							break;
							
						case Order.WAIT1SEC:
							// Demande au robot d'attendre 1 seconde
							if(this.recepteur.getID()==0) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAIT1SEC);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==1) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAIT1SEC);  
								this.com.send (sendIsBusy);
							} else if (this.recepteur.getID()==2) {
								Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.WAIT1SEC);  
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
							//synchronized(this){
							caseRecue = receiveListCase.toCase();//}
							this.reception = true ;
						}
						else{
							System.out.println("Robot " + this.recepteur.getID()+": Rien reçu!");
							//synchronized(this){
							caseRecue = null ;//}
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
	
	public synchronized int lireOrdre(){
		int i ;
		//synchronized(this){
		try{
		i = ((LinkedList<Integer>)this.queueOrdres).getLast();
		((LinkedList<Integer>)this.queueOrdres).removeLast();
		return i;
		}
		catch(NoSuchElementException e){
			return -1;
		}
		//}
	
	}
	
	public synchronized Case getCaseRecue(){
		//synchronized(this){
		return this.caseRecue;//}
	}
	
	public synchronized Queue<Integer> getQueue(){
		//synchronized(this){
		return this.queueOrdres;//}
	}

	public synchronized boolean getEnvoye(){
		//synchronized(this){
		boolean b =this.envoye;
		this.envoye = false ;
		return b;//}
	}
	public synchronized boolean getReception(){
		//synchronized(this){
		boolean b =this.reception;
		this.reception = false ;
		return b;//}
	}
	
	public synchronized boolean getConnected(){
		//synchronized(this){
		return this.connected;//}
		
	}
	
	public void setCaseInit(int x, int y, int dir){
		this.caseInit = new Case(x,y);
		this.orientation = dir ;
		
	}
}
	
	
	
	
	


