package robot.communication;

import java.util.ArrayList;

import robot.environnement.Case;
import robot.environnement.ListCase;

/**
 * 
 * Classe qui énumère toutes les trames qui peuvent être envoyées et reçues par la brique NXT.</br> 
 * - Le robot peut recevoir : une trame qui contient un ordre. </br>
 * - Le robot peut envoyer : une trame qui contient la case initiale.
 */




public class Trame2 {
	
	// ------------------------------------- CONSTANTES --------------------------------------------
	
	/**
	 * Constante permettant d'indientifer le type de la trame: </br>
	 * Trame contenant la case initiale. 
	 */
	public static final int typeCaseInit=2;
	/**
	 * Constante permettant d'indientifer le type de la trame: </br>
	 * Trame contenant un ordre  
	 */
	
	public static final int typeOrdre=5;
	/**
	 * Constante permettant d'indientifer le type de la trame: </br>
	 * Trame contenant une information sur l'état du robot : occupé ou non.
	 */
	
	public static final int typeBusy=9;
	
	/**
	 * Constante permettant d'indientifer le type de la trame: </br>
	 * Trame contenant la liste des cases explorées par le robot.
	 */
	
	public static final int typeListCase=1;
	
	
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------
	
	/**
	 * Identifiant du robot 0-1-2
	 */
	private byte ID;
	
	private byte ordre;
	
	/** Direction :
	 * 	UP=0;
	 *	LEFT=1;
	 *	DOWN=2;
	 *	RIGHT=3;
	 */
	
	private int direction;
	
	/**
	 * TypeTrame = typeCaseInit / typeOrdre / typeBusy / typeListCase
	 */
	private int typeTrame;
	
	private int isBusy;
	
	/**
	 * Coordonnées d'une case
	 */
	private int X;
	private int Y;

	/**
	 * Tableau qui contient toutes les informations sur une trame.
	 * Contenu typique de contenuT :
	 * contenuT[0]= taille du tableau
	 * contenuT[1]= identifiant du robot
	 * contenuT[2..(N-1)]= ordre / case(s)/Busy/ 
	 * contenuT[N]=typeTrame
	 * 
	 */
	private byte[] contenuT;
	
	// ------------------------------------- CONSTRUCTEURS --------------------------------------------
	/**
	 * Trame contenant un ordre
	 * @param ID
	 * @param ordre
	 */
	
	public Trame2(byte ID ,byte ordre){
			
			this.ID=ID;
			this.ordre= ordre;
			this.typeTrame=typeOrdre;
			
			this.contenuT= new byte[4];
			
			this.contenuT[0]=4;
			this.contenuT[1]=this.ID;
			this.contenuT[2]=this.ordre;
			this.contenuT[3]=(byte)this.typeTrame;
		}
	
	/**
	 * Trame la liste de cases explorées
	 * @param ID
	 * @param ordre
	 */
	
	
	public Trame2(byte ID, ListCase listCase ){

		ArrayList<Case> pile;
		int i=2;
		
		this.ID=ID;
		pile=listCase.getArrayList();
		
		this.typeTrame=typeListCase;
			
		this.contenuT= new byte[(byte)(pile.size()*3+3)];  //on rÃ©cupÃ¨re la taille du tab, chaque case contient 3 infos + taille trame, type et ID
	
		this.contenuT[0]=(byte)(pile.size()*3+3);
		this.contenuT[1]=this.ID;
		
		for (Case Case1 : pile){   
			
			this.contenuT[i]=(byte)Case1.getX();
			this.contenuT[i+1]=(byte)Case1.getY();
			this.contenuT[i+2]=Case1.getCompo();
			i=i+3;
		}
		
		this.contenuT[i]=(byte)this.typeTrame;
			
	}
	
	/**
	 * Trame qui contient la position initiale du robot : case initiale et orientation.
	 * @param ID
	 * @param message
	 * @param typeMessage
	 */
	
	public Trame2(byte ID, Case firstCase, int direction){
		this.ID=ID;
		this.direction=direction;
		this.typeTrame=2;
		this.X=firstCase.getX();
		this.Y=firstCase.getY();
		
		this.contenuT= new byte[6];
		
		this.contenuT[0]=6;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=(byte)this.X;
		this.contenuT[3]=(byte)this.Y;
		this.contenuT[4]=(byte)this.direction;
		this.contenuT[5]=(byte)this.typeTrame;
		
	}
	/**
	 * Trame qui contient l'état du robot : occupé ou non
	 * @param ID
	 * @param isBusy
	 */
	
		public Trame2(byte ID, int isBusy){
			this.ID=ID;
			this.isBusy=isBusy;
			this.typeTrame=9;
			
			this.contenuT= new byte[3];
			
			this.contenuT[0]=3;
			this.contenuT[1]=this.ID;
			this.contenuT[2]=(byte)this.isBusy;
			
		}
		
	
		// ------------------------------------- METHODES --------------------------------------------
	/**
	 * 
	 * @return un tableau qui contient les informations sur une trame
	 */
	public byte[] tableauTrame(){
		return this.contenuT;
	}
	
	/**
	 * 
	 * @return le type de la trame
	 */
	public int getTypeTrame(){
		return this.typeTrame;
	}
	
	/**
	 * 
	 * @return l'ordre
	 */
	
	public byte getOrdre(){
		return this.ordre;
	}
	/**
	 * 
	 * @return position en X
	 */
	public int getPosX(){
		return this.X;
	}
	/**
	 * 
	 * @return position en Y
	 */
	public int getPosY(){
		return this.Y;
	}
	
	/**
	 * 
	 * @return direction du robot
	 */
	public int getDirection(){
		return this.direction;
	}
	
	/**
	 * Affiche le contenu d'une trame
	 * @throws InterruptedException
	 */
	public void printTrame() throws InterruptedException{
		
		int i;
		String message = "";
		for(i=0;i<this.contenuT[0];i++){
			message = message + this.contenuT[i] +" ; ";
		}
		System.out.println(message);
		Thread.sleep(4000);
	}
}
