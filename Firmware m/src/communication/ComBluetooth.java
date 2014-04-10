package communication;
import java.io.*;

import env.Case;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class ComBluetooth{
	
	public EntiteeBT emetteur;
	public EntiteeBT recepteur;
	public BTConnection connection;
//	public Envoie envoie ;
//	public Reception reception;
	
	public ComBluetooth( EntiteeBT recepteur){
		this.emetteur=emetteur;
		this.recepteur=recepteur;
	}
	
	public void connexion() {
		 try{
			 this.initialisation();
		 } catch (Exception e) {}
	}
	
	public Trame2 receive() {
		Trame2 trame=null; 
		 try{
			 trame=this.ecouter();
		 } catch (Exception e) {}
		 return trame;
	}
	
	public void send(Trame2 trame) {
		
		 try{
			 this.envoyer(trame);
		 } catch (Exception e) {}
		 
	}
	
	
	
	
	public void initialisation() throws Exception{
		//on se met en attende de connexion
		System.out.println("Wow much wait...");
		this.connection = Bluetooth.waitForConnection();
		if (connection == null)
			throw new IOException("Epic fail connexion");
		System.out.println("Wow very connexion !");
				
		//si pas d'échec, on active les I/O bluetooth
		this.recepteur.setInput(connection.openDataInputStream());
		this.recepteur.setOutput(connection.openDataOutputStream());
				
		try {
			this.recepteur.getOutput().writeByte((byte)0);
			this.recepteur.getOutput().flush();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
				
	}
	
	public Trame2 ecouter()throws Exception{
				
		this.connection.openStream();
		this.recepteur.getOutput().write(0);
		this.recepteur.getOutput().flush();		
		
		//on lit les données sur le flux d'entrée
		int tailleTrameRecue = (int)this.recepteur.getInput().read();
		//System.out.println("j'ai lu: "+ tailleTrameRecue);
		
		byte[] trameRecue= new byte[tailleTrameRecue];   //tailleTrameRecue+2 ?
		trameRecue[0]=(byte)tailleTrameRecue;
		//System.out.println("trame[0]"+trameRecue[0]);

		int j;
		for (j=1; j < tailleTrameRecue; j++){
			trameRecue[j]= this.recepteur.getInput().readByte();
			//System.out.println("trame["+j+"]: "+trameRecue[j]);
		}
		
		
		Trame2 trameR = null;
		if (trameRecue[tailleTrameRecue-1]==5){
			trameR= new Trame2(trameRecue[1],trameRecue[2]);
		}
		else if (trameRecue[tailleTrameRecue-1]==7){
			trameR= new Trame2(trameRecue[1],trameRecue[2],trameRecue[3]);
		}
		else if (trameRecue[tailleTrameRecue-1]==8){
			Case firstCase= new Case(trameRecue[2],trameRecue[3]);
			trameR= new Trame2(trameRecue[1],firstCase,trameRecue[4]);
		}
		this.connection.closeStream();
		return trameR;
	}
	
	public void envoyer(Trame2 message) throws Exception{
		this.connection.openStream();
		while(this.emetteur.getInput().read()!=0){
			
		}
		
		int i;
		for(i=0;i<message.tableauTrame().length;i++){
		this.emetteur.getOutput().writeByte(message.tableauTrame()[i]);}
			
		this.emetteur.getOutput().flush();
		this.connection.closeStream();
			
	}
	
	public void fermer() throws Exception{
	
		this.recepteur.getInput().close();
		this.recepteur.getOutput().close();
		this.connection.close();
	}
	
}