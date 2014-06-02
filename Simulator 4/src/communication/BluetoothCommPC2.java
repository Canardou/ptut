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

/**
 * 
 * Classe qui traite la réception et l'envoi de trames du côté du PC.
 *
 */

public class BluetoothCommPC2 extends Thread{
	
	// ------------------------------------- ATTRIBUTS-----------------------------------
	
	private EntiteeBT emetteur;
	private EntiteeBT recepteur;
	
	private InputStream in;
	private OutputStream out;
	private DataInputStream dis;
	private DataOutputStream dos;
	private NXTComm nxtComm; 
	
	
	
	// ------------------------------------- CONSTRUCTEUR--------------------------------------------
	/**
	 * Constructeur de BluetoothCommPC2
	 * @param emetteur
	 * @param recepteur
	 */
	public BluetoothCommPC2 (EntiteeBT emetteur, EntiteeBT recepteur) {
	
		this.emetteur=emetteur;
		this.recepteur=recepteur;
	}
	
	
	
	// ------------------------------------- METHODES--------------------------------------------
	
	/**
	 * Appel à la méthode initialisationCommunication.
	 * @throws ProblemeConnexion
	 */
	public void connexion() throws ProblemeConnexion {
		 try{
			 this.initialisationCommunication();
		 } catch (Exception e) {
			 ProblemeConnexion probleme = new ProblemeConnexion();
			 throw probleme ;
		 }
	}
	/**
	 * Appel à la méthode receiveTrameNXT.
	 * @return trame recue
	 * @throws Exception
	 */
	public Trame2 receive() throws Exception {
		Trame2 trame=null; 
	    trame=this.receiveTrameNXT();
		return trame;
	}
	/**
	 * Appel à la méthode sendTrameToNXT.
	 * @param trame
	 */
	public void send(Trame2 trame) {
		
		 try{
			 this.sendTrameToNXT(trame);
		 } catch (Exception e) {}
		 
	}
	
	
	/**
	 * Création de la connexion bluetooth entre PC et NXT.
	 * @throws NXTCommException
	 */
	public void initialisationCommunication() throws NXTCommException{
		
		// creation d'une connexion bluetooth entre PC et NXT	
		nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		System.out.println("Attempting to connect to robot...");
				
		// ouverture connexion et activation des input et output streams 
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
		
	}
	
	
	/**
	 * Envoi d'une trame.
	 * @param trameEnvoyee
	 * @throws IOException
	 */
	public void sendTrameToNXT(Trame2 trameEnvoyee) throws IOException {
		
		while(this.emetteur.getInput().read()!=0){
			
		}
		// si communication OK alors le superviseur envoie la trame a la brique NXT
		int tailleTrame=trameEnvoyee.tableauTrame().length;
		
		for (int i=0; i<tailleTrame;i++){
			this.emetteur.getOutput().writeByte(trameEnvoyee.tableauTrame()[i]);
		}
		this.emetteur.getOutput().flush();
		 
		
				
	}
	
	/**
	 * Reception d'une trame.
	 * @return
	 * @throws IOException
	 * @throws NXTCommException
	 */
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
	
		if (trameRecue[tailleTrameRecue-1]==Trame2.typeOrdre){
			trameR= new Trame2(trameRecue[1],trameRecue[2]);
			
		}
		else if (trameRecue[tailleTrameRecue-1]!=Trame2.typeOrdre ){
			ListeCase listCase=new ListeCase();
			int k=2;
			//(tailleTrameRecue-3)/3 = nombre de cases
			for (int i=0; i<(tailleTrameRecue-3)/3 ; i++){
				listCase.addCase2(trameRecue[k], trameRecue[k+1], trameRecue[k+2]);
				k=k+3;
			}
			trameR= new Trame2(trameRecue[1],listCase);
		}
					
		return trameR;
					
	}

	/**
	 * Ouverture des flux.
	 * @throws IOException
	 */
	public void openStream()throws IOException {
		
		this.in = this.nxtComm.getInputStream();  
		this.out = this.nxtComm.getOutputStream(); 
		
		this.dis = new DataInputStream(this.in);
		this.dos = new DataOutputStream(this.out);
		
		this.emetteur.setInput(this.dis);
		this.emetteur.setOutput(this.dos);
	}
	
	/**
	 * Fermeture des flux.
	 * @throws IOException
	 */
	
	public void fermerStream()throws IOException {
		this.in.close();
		this.out.close();
	}

	/**
	 * Fermeture de la communication.
	 * @throws IOException
	 */
	public void fermerCommunication() throws IOException{

		
		this.in.close();
		this.out.close();
		nxtComm.close();	
	}
}
