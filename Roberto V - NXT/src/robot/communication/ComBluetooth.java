package robot.communication;
import java.io.*;

import robot.environnement.Case;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class ComBluetooth{
	
	public EntiteeBT entitee1;
	//public EntiteeBT entitee1;
//	public Envoie envoie ;
//	public Reception reception;
	
	public ComBluetooth( EntiteeBT entitee1){
		this.entitee1=entitee1;
	//	this.entitee1=entitee1;
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
		BTConnection connection = Bluetooth.waitForConnection();
		if (connection == null)
			throw new IOException("Epic fail connexion");
		System.out.println("Wow very connexion !");
				
		//si pas d'échec, on active les I/O bluetooth
		this.entitee1.setInput(connection.openDataInputStream());
		this.entitee1.setOutput(connection.openDataOutputStream());
				
		//try {
			//this.entitee1.getOutput().writeByte((byte)0);  //????????
			//this.entitee1.getOutput().flush();
		//} catch (IOException e3) {
			// TODO Auto-generated catch block
		//	e3.printStackTrace();
		//}
				
	}
	
	public Trame2 ecouter()throws Exception{
				
		
		this.entitee1.getOutput().write(0);
		this.entitee1.getOutput().flush();		
		
		//on lit les données sur le flux d'entrée
		int tailleTrameRecue = (int)this.entitee1.getInput().read();
		//System.out.println("j'ai lu: "+ tailleTrameRecue);
		
		byte[] trameRecue= new byte[tailleTrameRecue];   //tailleTrameRecue+2 ?
		trameRecue[0]=(byte)tailleTrameRecue;
		//System.out.println("trame[0]"+trameRecue[0]);

		int j;
		for (j=1; j < tailleTrameRecue; j++){
			trameRecue[j]= this.entitee1.getInput().readByte();
			//System.out.println("trame["+j+"]: "+trameRecue[j]);
		}
		
		
		Trame2 trameR = null;
		if (trameRecue[tailleTrameRecue-1]==5){
			trameR= new Trame2(trameRecue[1],trameRecue[2]);
		}
		else if (trameRecue[tailleTrameRecue-1]==7){
			trameR= new Trame2(trameRecue[1],trameRecue[2],trameRecue[3]);
		}
		else if (trameRecue[tailleTrameRecue-1]==2){
			Case firstCase= new Case(trameRecue[2],trameRecue[3]);
			trameR= new Trame2(trameRecue[1],firstCase,trameRecue[4]);
		}
		
		return trameR;
	}
	
	public void envoyer(Trame2 message) throws Exception{
		
		while(this.entitee1.getInput().read()!=0){
			
		}
		int i;
		for(i=0;i<message.tableauTrame().length;i++){
		this.entitee1.getOutput().writeByte(message.tableauTrame()[i]);}
			
		this.entitee1.getOutput().flush();
	
			
	}
	
	public void fermer() throws Exception{
	
		this.entitee1.getInput().close();
		this.entitee1.getOutput().close();
	}
	
}