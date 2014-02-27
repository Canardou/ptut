package communication;
import java.io.*;

import lejos.nxt.comm.NXTConnection;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class ComBluetooth{
	
	private EntiteeBT emetteur;
	private EntiteeBT recepteur;
	
	public ComBluetooth(EntiteeBT emetteur, EntiteeBT recepteur){
		this.emetteur=emetteur;
		this.recepteur=recepteur;
	}
	
	public Trame ecouter()throws Exception{
		//on se met en attende de connexion
		BTConnection connection = Bluetooth.waitForConnection();
		if (connection == null)
			throw new IOException("Epic fail connexion");
		System.out.println("Wow very connexion.");
		
		//si pas d'échec, on active les I/O bluetooth
		this.recepteur.setInput(connection.openDataInputStream());
		this.recepteur.setOutput(connection.openDataOutputStream());
		
		//on envoie la valeur 0 au superviseur pour initier la communication
		this.recepteur.getOutput().write(0);
		this.recepteur.getOutput().flush();
		
		//on lit les données sur le flux d'entrée
		byte ID, x, y;
		int dir;
		ID= (byte)this.recepteur.getInput().read();
		x=(byte)this.recepteur.getInput().read();
		y=(byte)this.recepteur.getInput().read();
		dir=this.recepteur.getInput().readInt();
		Trame message=new Trame(ID,x,y,dir);
		
		//on les renvoie au superviseur pour vérification
		this.recepteur.getOutput().write(ID);
		this.recepteur.getOutput().write(x);
		this.recepteur.getOutput().write(y);
		this.recepteur.getOutput().writeInt(dir);
		this.recepteur.getOutput().flush();
		
		//il nous répond, si c'est OK on retourne le message, sinon on retourne null
		if(this.recepteur.getInput().read()==0){
			this.recepteur.getInput().close();
			this.recepteur.getOutput().close();
			connection.close();
			return message;
		}
		else{
			this.recepteur.getOutput().writeInt(1);
			this.recepteur.getOutput().flush();
			connection.close();
			return null;
		}
		
	}
	
	public void envoyer(byte ID, byte x, byte y, boolean h, boolean g, boolean d, int dir) throws Exception{
		
		try
		{

			BTConnection connection = Bluetooth.connect(this.recepteur.getAdr(), NXTConnection.RAW);
			if (connection == null)
				throw new IOException("Wow very Connect fail");
			System.out.println("so connected.");
			
			this.emetteur.setInput(connection.openDataInputStream());
			this.emetteur.setOutput(connection.openDataOutputStream());
			
			this.emetteur.getOutput().writeByte(ID);
			this.emetteur.getOutput().writeByte(x);
			this.emetteur.getOutput().writeByte(y);
			this.emetteur.getOutput().writeBoolean(h);
			this.emetteur.getOutput().writeBoolean(g);
			this.emetteur.getOutput().writeBoolean(d);
			this.emetteur.getOutput().writeInt(dir);
			this.emetteur.getOutput().flush();
			
			Trame messageEnvoye=new Trame(ID,x,y,h,g,d,dir);
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
		}
	}
	
	
	
	
}