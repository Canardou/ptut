package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import java.lang.Thread;

public class BluetoothCommPC2 extends Thread{
	
/*attributs */
	
	private EntiteeBT emetteur;
	private EntiteeBT recepteur;
	
	private InputStream in;
	private OutputStream out;
	private DataInputStream dis;
	private DataOutputStream dos;
	private NXTComm nxtComm;

	
	//private static boolean trameIdentique=false;
	
	
	/*constructeur*/
	
	public BluetoothCommPC2 (EntiteeBT emetteur, EntiteeBT recepteur) {
	
		this.emetteur=emetteur;
		this.recepteur=recepteur;
	}
	
	public void initialisationCommunication(){
		
		// creation d'une connexion bluetooth entre PC et NXT
		
				try {
					nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				} catch (NXTCommException e) {
					System.err.println("Could not create connection " +e.getMessage());
				}
				System.out.println("Attempting to connect to robot...");
				
				// ouverture connexion et activation des input et output streams 
				try {
					nxtComm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, recepteur.getNom() , recepteur.getAdr()),NXTComm.PACKET);  // pas sure
					System.out.println("connexion ok");
					
					
					this.in = this.nxtComm.getInputStream();  
					this.out = this.nxtComm.getOutputStream(); 
					
					this.dis = new DataInputStream(this.in);
					this.dos = new DataOutputStream(this.out);

					this.emetteur.setInput(this.dis);
					this.emetteur.setOutput(this.dos);
					
				//on vérifie que le PC recoie la valeur 0  pour initier la communication
					
				
				} catch (NXTCommException e) {
					//throw new IOException("Failed to connect " + e.toString());
					System.err.println("Failed to connect" +e.getMessage());
				}
					
		
		
	}
	
	
	//méthode : PC envoie trame à la brique NXT
	
	public void sendTrameToNXT(Trame2 trameEnvoyee) throws IOException {
		
		while(this.emetteur.getInput().read()!=0){
			
		}
	
			
		
		// si communication OK alors le superviseur envoie la trame à la brique NXT
		int tailleTrame=trameEnvoyee.tableauTrame().length;
		
		for (int i=0; i<tailleTrame;i++){
			this.emetteur.getOutput().writeByte(trameEnvoyee.tableauTrame()[i]);
			System.out.println("valeur de la trame envoyée");
			System.out.println(trameEnvoyee.tableauTrame()[i]);
		}
		this.emetteur.getOutput().flush();
		 
		//superviseur recoit la trame renvoyée par la brique pour verifier que c'est la bonne
		
		/*int tailleTrameRecue = this.emetteur.getInput().read();
		 
		for (int j=1; j <= tailleTrameRecue-1; j++){
			if(this.emetteur.getInput().read() != trameEnvoyee.tableauTrame()[j] ){
				trameIdentique=false;	
			}
			else{
				trameIdentique=true;
			}
		}     */
	
		
		// si les deux trames sont identiques alors le superviseur envoie 0 à la brique sinon 1
	/*	if(trameIdentique==true){
			this.emetteur.getOutput().write(0);
			this.emetteur.getOutput().flush();
			//on ferme les communications input output
			this.emetteur.getInput().close();
			this.emetteur.getOutput().close();
			nxtComm.close();			
			
		}
		else{
			this.emetteur.getOutput().writeInt(1);
			this.emetteur.getOutput().flush();
			this.emetteur.getInput().close();
			this.emetteur.getOutput().close();
			nxtComm.close();
			
		}     */
				
	}
	
	//méthode : PC recoie trame de la brique NXT
	
	public Trame2 receiveTrameNXT() throws IOException, NXTCommException{
		//superviseur reçoit une demande de la brique
		/*try {
			nxtComm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, emetteur.getNom() , emetteur.getAdr()),NXTComm.PACKET);  // pas sure
			System.out.println("connexion ok");
			
			//on active les I/O bluetooth
			
			this.in = this.nxtComm.getInputStream();  
			this.out = this.nxtComm.getOutputStream(); 
			
			this.dis = new DataInputStream(this.in);
			this.dos = new DataOutputStream(this.out);

			this.recepteur.setInput(this.dis);
			this.recepteur.setOutput(this.dos);
			
			//test attente communication
			if (this.recepteur.getInput().read()==1){
				this.recepteur.getOutput().write(0);
				this.recepteur.getOutput().flush();
			}
			
			
			
		
		} catch (NXTCommException e) {
			//throw new IOException("Failed to connect " + e.toString());
			System.err.println("Failed to connect" +e.getMessage());
		}
	
		*/
		//reception de la trame
		
		int tailleTrameRecue = this.recepteur.getInput().read();
		byte[] trameRecue= new byte[tailleTrameRecue];
		trameRecue[0]=(byte)tailleTrameRecue;
		 
		for (int j=1; j <= tailleTrameRecue-1; j++){
			trameRecue[j]= this.recepteur.getInput().readByte();
		}
		
		//reemission de la trame pour verifier que c'est la bonne
		
/*		for (int i=0; i <= trameRecue.length; i++){
			this.recepteur.getOutput().write(trameRecue[i]);
		}  
		
		this.recepteur.getOutput().flush();     */
		
		//Si NXT renvoie 0 alors la trame est bonne, la méthode retourne la trame, sinon elle retourne null
		/*		if(this.recepteur.getInput().read()==0){
					this.recepteur.getInput().close();
					this.recepteur.getOutput().close();
					nxtComm.close();  */
		
					Trame2 trameR=null;
					
					if (trameRecue[tailleTrameRecue-1]==1){
						trameR= new Trame2(trameRecue[0],trameRecue[1],trameRecue[2],trameRecue[3],Trame2.convertByteBool(trameRecue[4]),Trame2.convertByteBool(trameRecue[5]),Trame2.convertByteBool(trameRecue[6]),trameRecue[7]);
						//return trameR;
					}
					else if (trameRecue[tailleTrameRecue-1]==2){
						trameR= new Trame2(trameRecue[0],trameRecue[1],trameRecue[2],trameRecue[3],trameRecue[4]);
						//return trameR;
					}
					else if (trameRecue[tailleTrameRecue-1]==3){
						trameR= new Trame2(trameRecue[0],trameRecue[1],(int)trameRecue[2]);
						//return trameR;
					}
					else if (trameRecue[tailleTrameRecue-1]==4){
						trameR= new Trame2(trameRecue[0],trameRecue[1],(double)trameRecue[2]);
						//return trameR;
					}
					
					return trameR;
					
					
				//}
			/*	else{
					this.recepteur.getOutput().writeInt(1);
					this.recepteur.getOutput().flush();
					nxtComm.close();
					return null;
				}   */
	
	}



public void fermerCommunication() throws IOException{

		
		this.in.close();
		this.out.close();
		nxtComm.close();
	
	
}
}
