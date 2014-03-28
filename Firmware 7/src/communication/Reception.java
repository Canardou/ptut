package communication;

import java.io.IOException;


public class Reception extends Thread{
	public Thread t;
	//private EntiteeBT emetteur;
	private EntiteeBT recepteur;
	public Reception(EntiteeBT recepteur) throws InterruptedException{
		//this.emetteur = emetteur;
		//System.out.println("Wow very constructeur in !");
		//Thread.sleep(2000);
		this.recepteur = recepteur;
		this.t = new Thread(this);
		t.start();
		//System.out.println("Wow very constructeur out !");
		//Thread.sleep(4000);

	}

	public void run(){
		while(true){
		//on lit les données sur le flux d'entrée
			System.out.println("Wow so in run !");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		int tailleTrameRecue = 0;
		 	try {
				this.recepteur.getOutput().writeByte((byte)0);
				this.recepteur.getOutput().flush();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			try {
				tailleTrameRecue = this.recepteur.getInput().read();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("Wow so in middle !");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("So taille=" + tailleTrameRecue);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		if (tailleTrameRecue>0){
			System.out.println("Wow so in the if !");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		byte[] trameRecue= new byte[tailleTrameRecue];
		trameRecue[0]=(byte)tailleTrameRecue;
		 
		for (int j=1; j <= tailleTrameRecue-1; j++){
			try {
				trameRecue[j]= this.recepteur.getInput().readByte();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Wow so construction !");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Trame2 trameR = null;
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
		
		System.out.println("Wow much out !");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		this.recepteur.trame=trameR;
		try {
			this.recepteur.trame.printTrame();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		else{ System.out.println("pas de trame reçue");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
		}

	}
	
}