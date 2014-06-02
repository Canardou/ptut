package communication;
import java.io.*;

/**
 * Classe qui contient des informations sur une entit�e : PC ou brique NXT.
 * 
 *
 */

public class EntiteeBT{
	
	// ------------------------------------- ATTRIBUTS--------------------------------------------
	
		/**
		 * Nom de l'entit�e.
		 */
		private String nom;
		/**
		 * Identifiant du robot ou du PC.
		 */
		private int ID;
		/**
		 * Adresse du PC ou de la brique.
		 */
		private String adr;
		/**
		 * flux d'entr�e.
		 */
		private DataInputStream input;
		/**
		 * flux de sortie.
		 */
		private DataOutputStream output;
	
		/**
		 * Construction d'une entit�e de type robot.
		 * @param nom
		 * @param ID
		 * @param adr
		 */
		
		// ------------------------------------- CONSTRUCTEURS--------------------------------------------
		public EntiteeBT(String nom, int ID, String adr){
			
			this.nom=nom;
			this.ID=ID;
			this.adr=adr;
		}
		/**
		 * Construction d'une entit�e de type PC.
		 * @param nom
		 * @param adr
		 */
		public EntiteeBT(String nom, String adr){
			
			this.nom=nom;
			this.adr=adr;
		}
		
		// ------------------------------------- METHODES--------------------------------------------
	
		/**
		 * 
		 * @return nom du robot ou du pc
		 */
		public String getNom(){
			return this.nom;
		}
		/**
		 * 
		 * @return adresse du PC ou du robot
		 */
		public String getAdr(){
			return this.adr;
		}
		/**
		 * 
		 * @return identifiant du robot
		 */
		public int getID(){
			return this.ID;
		}	
		/**
		 * 
		 * @return flux d'entr�e.
		 */
		public DataInputStream getInput(){
			return this.input;
		}
		/**
		 * 
		 * @return flux de sortie.
		 */
		public DataOutputStream getOutput(){
			return this.output;
		}	
		/**
		 * 
		 * @param in , set du flux d'entr�e.
		 */
		public void setInput(DataInputStream in){
			this.input=in;
		}
		/**
		 * 
		 * @param out, set du flux de sortie.
		 */
		public void setOutput(DataOutputStream out){
			this.output=out;
		}
	
}