package robot.communication;
import java.io.*;
import robot.environnement.Case;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * 
 * Classe qui traite la réception et l'envoi de trames du côté du robot
 *
 */

public class ComBluetooth{
	
	// ------------------------------------- ATTRIBUTS--------------------------------------------
	
	public EntiteeBT entitee;
	
	// ------------------------------------- CONSTRUCTEUR--------------------------------------------
	/**
	 * 
	 * @param entitee
	 */
	public ComBluetooth( EntiteeBT entitee){
		this.entitee=entitee;
	}
	
	// ------------------------------------- METHODES--------------------------------------------
	/**
	 * Appel à la méthode initialisation
	 * @throws Exception
	 */
	
	public void connexion() throws Exception {
		this.initialisation();
	}
	
	/**
	 * Appel à la méthode écoute
	 * @return trame recue
	 */
	public Trame2 receive() {
		Trame2 trame=null; 
		 try{
			 trame=this.ecouter();
		 } catch (Exception e) {
			 System.out.println("receive:except.");
		 }
		 return trame;
	}
	
	/**
	 * Appel à la méthode envoyer
	 * @param trame, envoie d'une trame
	 */
	public void send(Trame2 trame) {
		
		 try{
			 this.envoyer(trame);
		 } catch (Exception e) {
			 System.out.println("send:exception");
		 }
		 
	}
	
	/**
	 * Entitee se met en attente de connexion et établissement des flux.
	 * @throws Exception
	 */
	
	public void initialisation() throws Exception{
		
		BTConnection connection = Bluetooth.waitForConnection();
		if (connection == null)
			throw new IOException("connexion:echec");
		
		this.entitee.setInput(connection.openDataInputStream());
		this.entitee.setOutput(connection.openDataOutputStream());		
	}
	
	/**
	 * Reception d'une trame
	 * @return
	 * @throws Exception
	 */
	public Trame2 ecouter()throws Exception{
				
		this.entitee.getOutput().write(0);
		this.entitee.getOutput().flush();		
		
		//on lit les donnÃ©es sur le flux d'entrÃ©e
		int tailleTrameRecue = (int)this.entitee.getInput().read();

		byte[] trameRecue= new byte[tailleTrameRecue];   
		trameRecue[0]=(byte)tailleTrameRecue;
		

		int j;
		for (j=1; j < tailleTrameRecue; j++){
			trameRecue[j]= this.entitee.getInput().readByte();
		}
		
		
		Trame2 trameR = null;
		if (trameRecue[tailleTrameRecue-1]==trameR.typeOrdre){
			trameR= new Trame2(trameRecue[1],trameRecue[2]);
		}
		else if (trameRecue[tailleTrameRecue-1]==trameR.typeCaseInit){
			Case firstCase= new Case(trameRecue[2],trameRecue[3]);
			trameR= new Trame2(trameRecue[1],firstCase,trameRecue[4]);
		}
		else  {
			System.out.println("ecoute:Type="+trameRecue[tailleTrameRecue-1]);
		}
		return trameR;
	}
	
	/**
	 * Envoi d'une trame
	 * @param message
	 * @throws Exception
	 */
	public void envoyer(Trame2 message) throws Exception{
		
		while(this.entitee.getInput().read()!=0){
			
		}
		int i;
		for(i=0;i<message.tableauTrame().length;i++){
		this.entitee.getOutput().writeByte(message.tableauTrame()[i]);}
			
		this.entitee.getOutput().flush();	
	}
	
	/**
	 * Fermeture de la connexion
	 * @throws Exception
	 */
	public void fermer() throws Exception{
	
		this.entitee.getInput().close();
		this.entitee.getOutput().close();
	}
	
}