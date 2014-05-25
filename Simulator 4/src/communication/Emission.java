package communication;

import java.io.IOException;


public class Emission extends Thread {
	
	private EntiteeBT recepteur;
	private EntiteeBT emetteur;
	private Trame2 trame;
	private Thread T;

	public Emission(){
		
	}
	
			
	@Override
	public void run()  {
		System.out.println("run ok");
		int tailleTrame=this.trame.tableauTrame().length;
		try {
			if (this.emetteur.getInput().read()==0){
				
			for (int i=0; i<tailleTrame;i++){
				try {
					
						this.emetteur.getOutput().writeByte(this.trame.tableauTrame()[i]);
						System.out.println("valeur de la trame envoy�e");
						System.out.println(this.trame.tableauTrame()[i]);
					
					} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		try {
			emetteur.getOutput().flush();
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
					
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
