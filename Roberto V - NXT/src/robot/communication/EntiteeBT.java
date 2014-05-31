package robot.communication;
import java.io.*;

/**
 * Classe qui contient des informations sur une entitée : PC ou brique NXT.
 * 
 *
 */

public class EntiteeBT{
	
	// ------------------------------------- ATTRIBUTS--------------------------------------------
	/**
	 * Nom de l'entitée.
	 */
	private String nom;
	/**
	 * Identifiant du robot ou du PC.
	 */
	private byte ID;
	
	/**
	 * Adresse du PC ou de la brique.
	 */
	private String adr;
	
	/**
	 * flux d'entrée.
	 */
	private DataInputStream input;
	
	/**
	 * flux de sortie.
	 */
	
	private DataOutputStream output;
	
	public Trame2 trame;
	
	// ------------------------------------- CONSTRUCTEURS--------------------------------------------
	
	/**
	 * Construction d'une entitée de type robot.
	 * @param nom
	 * @param ID
	 * @param adr
	 */
	
	public EntiteeBT(String nom, byte ID, String adr){
		
		this.nom=nom;
		this.ID=ID;
		this.adr=adr;
	}
	/**
	 * Construction d'une entitée de type PC.
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
	 * @return flux d'entrée.
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
	 * @param in , set du flux d'entrée.
	 */
	public void setInput(DataInputStream in){
		this.input=in;
	}
	
	/**
	 * 
	 * @param out set du flux de sortie.
	 */
	public void setOutput(DataOutputStream out){
		this.output=out;
	}	
	
}