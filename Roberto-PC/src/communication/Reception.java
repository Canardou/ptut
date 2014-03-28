package communication;

import java.io.IOException;


public class Reception extends Thread {
	
	private EntiteeBT recepteur;
	private EntiteeBT emetteur;
	private Trame2 trame;
	private Thread T;

	public Reception(){
		
	}
	
			
	public void run()  {

		int tailleTrameRecue = 0;
		
		try {
			tailleTrameRecue = this.recepteur.getInput().read();
		} catch (IOException e) {
		
			e.printStackTrace();
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
		
		Trame2 trameR=null;
		
		if (trameRecue[tailleTrameRecue-1]==1){
			trameR= new Trame2(trameRecue[0],trameRecue[1],trameRecue[2],trameRecue[3],Trame2.convertByteBool(trameRecue[4]),Trame2.convertByteBool(trameRecue[5]),Trame2.convertByteBool(trameRecue[6]),trameRecue[7]);
			
		}
		else if (trameRecue[tailleTrameRecue-1]==2){
			trameR= new Trame2(trameRecue[0],trameRecue[1],trameRecue[2],trameRecue[3],trameRecue[4]);
			
		}
		else if (trameRecue[tailleTrameRecue-1]==3){
			trameR= new Trame2(trameRecue[0],trameRecue[1],(int)trameRecue[2]);
			
		}
		else if (trameRecue[tailleTrameRecue-1]==4){
			trameR= new Trame2(trameRecue[0],trameRecue[1],(double)trameRecue[2]);
			
		}
		
		return trameR;
					
	}
	
	public void start(Trame2 trameEnvoyee, EntiteeBT recepteur, EntiteeBT emetteur ){
		this.emetteur=emetteur;
		this.recepteur=recepteur;
		this.trame=trameEnvoyee;
		
		Thread T=new Thread(this);
		System.out.println("start ok");
		T.start();
		
		
		
		
	}

}
