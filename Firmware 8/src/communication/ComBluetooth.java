package communication;
import java.io.*;

//import lejos.nxt.comm.NXTConnection;
//import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class ComBluetooth{
	
	public EntiteeBT emetteur;
	public EntiteeBT recepteur;
	public Envoie envoie ;
	public Reception reception;
	public BTConnection connection;
	
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
		
		byte[] trameRecue= new byte[tailleTrameRecue+2];
		trameRecue[0]=(byte)tailleTrameRecue;
		//System.out.println("trame[0]"+trameRecue[0]);

		int j;
		for (j=1; j < tailleTrameRecue+1; j++){
			trameRecue[j]= this.recepteur.getInput().readByte();
			//System.out.println("trame["+j+"]: "+trameRecue[j]);
		}
		
		
		Trame2 trameR = null;
		if (trameRecue[tailleTrameRecue]==1){
			trameR= new Trame2(trameRecue[0],trameRecue[1],trameRecue[2],trameRecue[3],Trame2.convertByteBool(trameRecue[4]),Trame2.convertByteBool(trameRecue[5]),Trame2.convertByteBool(trameRecue[6]),trameRecue[7]);
			//return trameR;
		}
		else if (trameRecue[tailleTrameRecue]==2){
			trameR= new Trame2(trameRecue[0],trameRecue[1],trameRecue[2],trameRecue[3],trameRecue[4]);
			//return trameR;
		}
		else if (trameRecue[tailleTrameRecue]==3){
			trameR= new Trame2(trameRecue[0],trameRecue[1],(int)trameRecue[2]);
			//return trameR;
		}
		else if (trameRecue[tailleTrameRecue]==4){
			trameR= new Trame2(trameRecue[0],trameRecue[1],(double)trameRecue[2]);
			//return trameR;
		}
		else if (trameRecue[tailleTrameRecue]==5){
			trameR= new Trame2(trameRecue[0],trameRecue[1],trameRecue[2]);
			//return trameR;
		}

		this.connection.closeStream();
		return trameR;
	}
	
	public void envoyer(Trame2 message) throws Exception{
		
		//try
		//{
			
			/*BTConnection connection = Bluetooth.connect(this.recepteur.getAdr(), NXTConnection.RAW);
			//if (connection == null)
				throw new IOException("Wow very Connect fail");
			System.out.println("so connected.");
			Thread.sleep(2000);
			lejos.nxt.LCD.scroll();
			
			this.emetteur.setInput(connection.openDataInputStream());
			this.emetteur.setOutput(connection.openDataOutputStream());
			
			this.emetteur.getOutput().writeByte((byte)1);
			this.emetteur.getOutput().flush();
			int i;
			
			int ack = 1;
			while(ack==1){
				ack=(byte)this.emetteur.getInput().read();
			}
			System.out.println("Wow so synchronisation.");
			Thread.sleep(2000);*/
			this.connection.openStream();
			int i;
			for(i=0;i<message.getTailleTrame();i++){
			this.emetteur.getOutput().writeByte(message.tableauTrame()[i]);}
			
			this.emetteur.getOutput().flush();
			this.connection.closeStream();
			/*
			Trame messageEnvoye=new Trame(message.getID(),message.getPosX(),message.getPosY(),message.getMurHaut(),message.getMurGauche(),message.getMurDroit(),message.getDirection());
			Trame messageRecu=new Trame(this.emetteur.getInput().read(),this.emetteur.getInput().read(),this.emetteur.getInput().read(),this.emetteur.getInput().readBoolean(),this.emetteur.getInput().readBoolean(),this.emetteur.getInput().readBoolean(),this.emetteur.getInput().read());
			if(messageRecu==messageEnvoye){
				this.emetteur.getOutput().write(0);
				this.emetteur.getOutput().flush();
				this.emetteur.getInput().close();
				this.emetteur.getOutput().close();
				connection.close();	
			}
			else{
				this.emetteur.getOutput().write(1);
				this.emetteur.getOutput().flush();
				this.emetteur.getInput().close();
				this.emetteur.getOutput().close();
				connection.close();	
			}

		}
		catch(Exception ioe)
		{
			LCD.scroll();
			System.out.println("ERROR");
			System.out.println(ioe.getMessage());
		}*/
	}
	public void fermer() throws Exception{
	
		this.recepteur.getInput().close();
		this.recepteur.getOutput().close();
		this.connection.close();
	}
	
}