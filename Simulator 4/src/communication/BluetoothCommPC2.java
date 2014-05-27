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
import communication.ProblemeConnexion;
import java.lang.Thread;

import labyrinth.ListeCase;

public class BluetoothCommPC2 extends Thread{
	
/*attributs */
	
	private EntiteeBT emetteur;
	private EntiteeBT recepteur;
	
	private InputStream in;
	private OutputStream out;
	private DataInputStream dis;
	private DataOutputStream dos;
	private NXTComm nxtComm; 
	
	
	
	/*constructeur*/
	
	public BluetoothCommPC2 (EntiteeBT emetteur, EntiteeBT recepteur) {
	
		this.emetteur=emetteur;
		this.recepteur=recepteur;
	}
	
	
	
	
	/*m�thodes*/
	
	
	public void connexion() throws ProblemeConnexion {
		 try{
			 this.initialisationCommunication();
		 } catch (Exception e) {
			 ProblemeConnexion probleme = new ProblemeConnexion();
			 throw probleme ;
		 }
	}
	
	public Trame2 receive() {
		Trame2 trame=null; 
		 try{
			 trame=this.receiveTrameNXT();
		 } catch (Exception e) {}
		 return trame;
	}
	
	public void send(Trame2 trame) {
		
		 try{
			 this.sendTrameToNXT(trame);
		 } catch (Exception e) {}
		 
	}
	
	
	
	
	
	public void initialisationCommunication() throws NXTCommException{
		
		// creation d'une connexion bluetooth entre PC et NXT
		
				
					nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				
				System.out.println("Attempting to connect to robot...");
				
				// ouverture connexion et activation des input et output streams 
				
					//System.out.println("print YOLO");
					System.out.println(recepteur.getNom());
					System.out.println( recepteur.getAdr());
					System.out.println(NXTComm.PACKET);
					nxtComm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, recepteur.getNom() , recepteur.getAdr()),NXTComm.PACKET);  // pas sure
					System.out.println("connexion ok");
					
					
					this.in = this.nxtComm.getInputStream();  
					System.out.println("creation in");
					this.out = this.nxtComm.getOutputStream(); 
					System.out.println("creation out");
					
					this.dis = new DataInputStream(this.in);
					System.out.println("creation dis");
					this.dos = new DataOutputStream(this.out);
					System.out.println("creation dos");

					this.emetteur.setInput(this.dis);
					System.out.println("set in");
					this.emetteur.setOutput(this.dos);
					System.out.println("set out");
					
				//on v�rifie que le PC recoie la valeur 0  pour initier la communication
					
				
				
					
		
		
	}
	
	
	//m�thode : PC envoie trame � la brique NXT
	
	public void sendTrameToNXT(Trame2 trameEnvoyee) throws IOException {
		
		while(this.emetteur.getInput().read()!=0){
			
		}
	
		
		// si communication OK alors le superviseur envoie la trame a la brique NXT
		int tailleTrame=trameEnvoyee.tableauTrame().length;
		
		for (int i=0; i<tailleTrame;i++){
			this.emetteur.getOutput().writeByte(trameEnvoyee.tableauTrame()[i]);
			//System.out.println("j'envoie: "+trameEnvoyee.tableauTrame()[i]);
		}
		this.emetteur.getOutput().flush();
		 
		
				
	}
	
	//m�thode : PC recoie trame de la brique NXT
	
	public Trame2 receiveTrameNXT() throws IOException, NXTCommException{
		
		
		this.emetteur.getOutput().write(0);
		this.emetteur.getOutput().flush();	
		
		//reception de la trame
		
		int tailleTrameRecue = this.emetteur.getInput().read();
		byte[] trameRecue= new byte[tailleTrameRecue];
		trameRecue[0]=(byte)tailleTrameRecue;
		 
		for (int j=1; j <= tailleTrameRecue-1; j++){
			trameRecue[j]= this.emetteur.getInput().readByte();
			}
		
	
		
		Trame2 trameR=null;
					
		if (trameRecue[tailleTrameRecue-1]==6){
			ListeCase listCase=new ListeCase();
			int k=2;
			
			for (int i=0; i<(tailleTrameRecue-3)/3 ; i++){
				listCase.addCase2(trameRecue[k], trameRecue[k+1], trameRecue[k+2]);
				k=k+3;
			}
			trameR= new Trame2(trameRecue[1],listCase);}
			
		else if (trameRecue[tailleTrameRecue-1]==7){
			
			trameR= new Trame2(trameRecue[1],trameRecue[2],trameRecue[3]);
			
		}
		else if (trameRecue[tailleTrameRecue-1]!=7 & trameRecue[tailleTrameRecue-1]!=6){
			ListeCase listCase=new ListeCase();
			int k=2;
			for (int i=0; i<(tailleTrameRecue-3)/3 ; i++){
				listCase.addCase2(trameRecue[k], trameRecue[k+1], trameRecue[k+2]);
				k=k+3;
				//System.out.println(trameRecue[k]+" ; "+trameRecue[k+1]+" ; "+trameRecue[k+2] + " ; ");
			}
			trameR= new Trame2(trameRecue[1],listCase);}
		
					
		return trameR;
					
			
	
	}

	public void openStream()throws IOException {
		
		this.in = this.nxtComm.getInputStream();  
		this.out = this.nxtComm.getOutputStream(); 
		
		this.dis = new DataInputStream(this.in);
		this.dos = new DataOutputStream(this.out);
		
		this.emetteur.setInput(this.dis);
		this.emetteur.setOutput(this.dos);
	}
	
	public void fermerStream()throws IOException {
		this.in.close();
		this.out.close();
	}

public void fermerCommunication() throws IOException{

		
		this.in.close();
		this.out.close();
		nxtComm.close();
	
	
}
}
