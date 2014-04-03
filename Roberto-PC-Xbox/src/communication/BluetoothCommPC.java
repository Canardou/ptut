package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import lejos.pc.comm.*;



public class BluetoothCommPC{
	
	/*attributs */
	
	private EntiteeBT emetteur;
	private EntiteeBT recepteur;
	
	private InputStream in;
	private OutputStream out;
	private DataInputStream dis;
	private DataOutputStream dos;
	private NXTComm nxtComm;
	private NXTInfo nxtInfo;
	
	
	/*constructeur*/
	
	public BluetoothCommPC (EntiteeBT emetteur, EntiteeBT recepteur) {
	
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
		
		
	}
	
	//méthode : PC envoie trame à la brique NXT
	
	public void sendTrameToNXT(byte ID, byte x, byte y, int dir) throws IOException {
		
		
		
		
		// ouverture connexion et activation des input et output streams 
		try {
			nxtComm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, recepteur.getNom() , recepteur.getAdr()),NXTComm.PACKET);  // pas sure
			
			this.in = this.nxtComm.getInputStream();  
			this.out = this.nxtComm.getOutputStream(); 
			
			this.dis = new DataInputStream(this.in);
			this.dos = new DataOutputStream(this.out);

			this.emetteur.setInput(this.dis);
			this.emetteur.setOutput(this.dos);
			
		//on vérifie que le PC recoie la valeur 0  pour initier la communication
			
			while (this.emetteur.getInput().read() != 0) {
				; // wait for ready signal
			}

			System.out.println("Robot is ready!");
		
		} catch (NXTCommException e) {
			//throw new IOException("Failed to connect " + e.toString());
			System.err.println("Failed to connect" +e.getMessage());
		}
				
		
		// si communication OK alors le superviseur envoie la trame à la brique NXT
		
		Trame messageEnvoye=new Trame(ID,x,y,dir);
		
		this.emetteur.getOutput().writeByte(ID);
		this.emetteur.getOutput().writeByte(x);
		this.emetteur.getOutput().writeByte(y);
		this.emetteur.getOutput().writeInt(dir);
		this.emetteur.getOutput().flush();
		
		//superviseur recoit la trame renvoyée par la brique pour verifier que c'est la bonne
		
		byte IDc, xc, yc; //IDc =IDcheck
		int dirc;
		
		IDc=(byte)this.emetteur.getInput().read();
		xc=(byte)this.emetteur.getInput().read();
		yc=(byte)this.emetteur.getInput().read();
		dirc=this.emetteur.getInput().readInt();
		
		Trame messageVerification=new Trame(IDc,xc,yc,dirc);
		
		// si les deux trames sont identiques alors le superviseur envoir 0 à la brique sinon 1
		if(messageVerification==messageEnvoye){
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
			
		}
				
	}
	
	//méthode : PC recoie trame de la brique NXT
	
	public Trame receiveTrameNXT() throws IOException{
		//superviseur reçoit une demande de la brique
		try {
			nxtComm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, recepteur.getNom() , recepteur.getAdr()),NXTComm.PACKET);  // pas sure
			
			//on active les I/O bluetooth
			
			this.in = this.nxtComm.getInputStream();  
			this.out = this.nxtComm.getOutputStream(); 
			
			this.dis = new DataInputStream(this.in);
			this.dos = new DataOutputStream(this.out);

			this.recepteur.setInput(this.dis);
			this.recepteur.setOutput(this.dos);
			
		
		} catch (NXTCommException e) {
			//throw new IOException("Failed to connect " + e.toString());
			System.err.println("Failed to connect" +e.getMessage());
		}
		
		
		//on envoie la valeur 0 à la brique pour initier la communication   -- à rajouter dans la classe correspondante
		this.recepteur.getOutput().write(0);
		this.recepteur.getOutput().flush();
		
		//reception de la trame
		byte ID, x, y;
		boolean h,g,d;
		int dir;
		
		ID= (byte)this.recepteur.getInput().read();
		x=(byte)this.recepteur.getInput().read();
		y=(byte)this.recepteur.getInput().read();
		h=this.recepteur.getInput().readBoolean();
		g=this.recepteur.getInput().readBoolean();
		d=this.recepteur.getInput().readBoolean();
		dir=this.recepteur.getInput().readInt();
		
		Trame messageRecu=new Trame(ID,x,y,h,g,d,dir);
		
		//reemission de la trame pour verefier que c'est la bonne
		this.recepteur.getOutput().write(ID);
		this.recepteur.getOutput().write(x);
		this.recepteur.getOutput().write(y);
		this.recepteur.getOutput().writeBoolean(h);
		this.recepteur.getOutput().writeBoolean(g);
		this.recepteur.getOutput().writeBoolean(d);
		this.recepteur.getOutput().writeInt(dir);
		this.recepteur.getOutput().flush();
		
		//Si NXT renvoie 0 alors la trame est bonne, la méthode retourne la trame, sinon elle retourne null
				if(this.recepteur.getInput().read()==0){
					this.recepteur.getInput().close();
					this.recepteur.getOutput().close();
					nxtComm.close();
					return messageRecu;
				}
				else{
					this.recepteur.getOutput().writeInt(1);
					this.recepteur.getOutput().flush();
					nxtComm.close();
					return null;
				}
		
	}

	
}
