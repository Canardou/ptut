package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import labyrinth.Case;




public class ThreadComm extends Thread{
	
	
	
	EntiteeBT recepteur;
	private boolean connected;
	private BluetoothCommPC2 com;
	private Case caseInit;
	private int orientation;
	private Queue<Integer> queueOrdres ;
	

	
	//constructeur
	public ThreadComm(EntiteeBT robot, Case caseinit, int orientation) { 
		this.recepteur= robot;
		InfoEntitee IE = new InfoEntitee() ;
		this.com= new BluetoothCommPC2(IE.PCkiwor, this.recepteur);
		this.caseInit=caseinit;
		this.orientation = orientation;
		this.queueOrdres =new LinkedList<Integer>();
	
	}
	
	
	// m�thode
	
	public void setOrdres(Queue<Integer> ordres){
		this.queueOrdres = ordres;
	}
	
	public void run(){
		
		while(true){
			
			//connexion PC -> robot
			
			this.com.connexion();
			this.connected=true;
			
				System.out.println("robot connecté : ");
				
				//
				//Initialisation du robot
				//
				
				//Angle de r�f�rence
				if(this.recepteur.getID()==0) {
					Trame2 sendAngleRef= new Trame2((byte)1,(byte)Order.SAVEREFANGLE);
					this.com.send (sendAngleRef);
				} else if (this.recepteur.getID()==1) {
					Trame2 sendAngleRef= new Trame2((byte)1,(byte)Order.SAVEREFANGLE);
					this.com.send (sendAngleRef);
				} else if (this.recepteur.getID()==2) {
					Trame2 sendAngleRef= new Trame2((byte)1,(byte)Order.SAVEREFANGLE);
					this.com.send (sendAngleRef);						
				}
				
				System.out.println("angleRef OK" );
				
				// Position initiale
				//Case caseInit = new Case(1,2);														// � recup�rer grace au moniteur, pour l'instant initialisation quelconque 		
				//int orientation=0; 			
				// � recup�rer grace au moniteur, pour l'instant initialisation quelconque 	
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
				
				System.out.println("Demande isBusy OK" );
				Trame2 receiveIsBusy = this.com.receive();
				int Busy=receiveIsBusy.getBusy();
				

				// donne au robot sa position initiale
				
				if(this.recepteur.getID()==0) {
					this.com.send (new Trame2((byte)0,(byte)Order.SETPOSITION));
				} else if (this.recepteur.getID()==1) {
					this.com.send (new Trame2((byte)1,(byte)Order.SETPOSITION));
				} else if (this.recepteur.getID()==2) {
					this.com.send (new Trame2((byte)2,(byte)Order.SETPOSITION));						
				}
				
				while(this.connected){
				
					if (Busy!=1){
						
						
						// demande et reception de la liste des cases explor�es

						if(this.recepteur.getID()==0) {
							this.com.send (new Trame2((byte)0,(byte)Order.CASETOSEND));
						} else if (this.recepteur.getID()==1) {
							this.com.send (new Trame2((byte)1,(byte)Order.CASETOSEND));
						} else if (this.recepteur.getID()==2) {
							this.com.send (new Trame2((byte)2,(byte)Order.CASETOSEND));						
						}
						
						Trame2 receiveListCase=this.com.receive();
						System.out.println("trame recue :");
						if(receiveListCase != null){
						try {
							receiveListCase.printTrame();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
						else{System.out.println(" Rien reçu!");}
						
						// Pour tester 
						/*
						if(this.recepteur.getID()==0) {
							this.com.send (new Trame2((byte)0,(byte)Order.TURNR));
						} else if (this.recepteur.getID()==1) {
							this.com.send (new Trame2((byte)1,(byte)Order.FORWARD));
						} else if (this.recepteur.getID()==2) {
							this.com.send (new Trame2((byte)1,(byte)Order.TURNL));						
						}*/	
					}
				}
			}		
			
	}	
}
	
	
	
	
	


