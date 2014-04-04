package communication;

import java.io.IOException;


public class Envoie extends Thread{
	public Thread t;
	private EntiteeBT emetteur;
	//private EntiteeBT recepteur;
	private Trame2 message;
	public Envoie(EntiteeBT emetteur, Trame2 message){
		this.emetteur = emetteur;
		//this.recepteur = recepteur;
		this.message = message;
		this.t=new Thread(this);
		t.start();
	}

	public void run(){
		
			int i;
			for(i=0;i<this.message.getTailleTrame();i++){
			try {
				this.emetteur.getOutput().writeByte(this.message.tableauTrame()[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}}
			
			try {
				this.emetteur.getOutput().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
		
	
	
	
}