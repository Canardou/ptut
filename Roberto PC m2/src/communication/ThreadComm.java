package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import env.Case;




public class ThreadComm extends Thread{
	
	
	
	EntiteeBT recepteur;
	private boolean connected;
	private BluetoothCommPC2 com;
	

	
	//constructeur
	public ThreadComm(EntiteeBT robot) { 
		this.recepteur= robot;
		this.com= new BluetoothCommPC2(InfoEntitee.PCmarion, this.recepteur);
		try {
			this.com.openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	// méthode
	
	public void run(){
		
		while(true){
			
			//connexion PC -> robot
			
			this.com.connexion();
			this.connected=true;
			while(this.connected){
				System.out.println("robot connectÃ© : ");
				
				//
				//Initialisation du robot
				//
				
				//Angle de référence
				Trame2 sendAngleRef= new Trame2((byte)1,(byte)Order.SAVEREFANGLE);
				this.com.send (sendAngleRef);
				System.out.println("angleRef OK" );
				
				// Position initiale
				Case caseInit = new Case(1,2);														// à recupérer grace au moniteur, pour l'instant initialisation quelconque 		
				int orientation=0; 																	// à recupérer grace au moniteur, pour l'instant initialisation quelconque 	
				Trame2 sendPositionInit  = new Trame2((byte)1, caseInit,orientation);
				this.com.send (sendPositionInit);
				System.out.println("Position initiale OK" );
				
				// Regarde les murs autour de lui CHECkFIRSTCASE
				Trame2 sendCheckCase= new Trame2((byte)1,(byte)Order.CHECKFIRSTCASE);
				this.com.send (sendCheckCase);
				System.out.println("Detection murs case1 OK" );
				
				// Demande au robot s'il est occupé et reception
				Trame2 sendIsBusy= new Trame2((byte)1,(byte)Order.SENDBUSY);  // ajouter dans Ordre et gérer cette commande dans robot
				this.com.send (sendIsBusy);
				System.out.println("Demande isBusy OK" );
				Trame2 receiveIsBusy = this.com.receive();
				int Busy=receiveIsBusy.getBusy();
				
				
				if (Busy!=1){
					
					// demande et reception de la liste des cases explorées
					this.com.send (new Trame2((byte)1,(byte)Order.CASETOSEND));
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
					else{System.out.println(" Rien reÃ§u!");}
					
					
					
					//gestion de la cartographie et envoie d'ordres
					
						
					
				}
			}
			
				
			
		}
		
		
		
		
	}	
		
	}
	
	
	
	
	


