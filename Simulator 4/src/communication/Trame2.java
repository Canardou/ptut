package communication;

import java.util.ArrayList;




import labyrinth.Case;
import labyrinth.ListeCase;

/**
 * 
 * Classe qui énumère toutes les trames qui peuvent être envoyées et reçues par le PC.</br> 
 * - Le PC peut recevoir : une trame qui contient une liste de cases ou la position initiale du robot. </br>
 * - Le PC peut envoyer : une trame qui contient un ordre.
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
		 * Trame contenant la liste des cases explorées par le robot.
		 */
		
		public static final int typeListCase=1;
	
		// ------------------------------------- ATTRIBUTS --------------------------------------------
		
		/**
		 * Identifiant du robot 0-1-2
		 */
		private byte ID;
		
		/** Direction :
		 * 	UP=0;
		 *	LEFT=1;
		 *	DOWN=2;
		 *	RIGHT=3;
		 */
		private int direction;
		
		private byte ordre;
		
		/**
		 * TypeTrame = typeCaseInit / typeOrdre / typeBusy / typeListCase
		 */
		private int typeTrame;
		
		
		/**
		 * Coordonnée en X d'une case
		 */
		private int X;
		/**
		 * Coordonnée en Y d'une case
		 */
		private int Y;

		private ArrayList<Case> pile;

		/**
		 * Tableau qui contient toutes les informations sur une trame.</br>
		 * Contenu typique de contenuT :</br>
		 * contenuT[0]= taille du tableau</br>
		 * contenuT[1]= identifiant du robot</br>
		 * contenuT[2..(N-1)]= ordre / case(s)/Busy/ </br>
		 * contenuT[N]=typeTrame
		 * 
		 */
		private byte[] contenuT;

	
	
		// ------------------------------------- CONSTRUCTEUR --------------------------------------------
	
	
		/**
		 * Trame contenant un ordre.</br>
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
		 * Trame la liste de cases explorées.</br>
		 * @param ID
		 * @param ordre
		 */
		
		public Trame2(byte ID, ListeCase listCase ){
		
			int i=2;
			
			this.ID=ID;
			this.pile=listCase.getArrayList();
			
			this.typeTrame=typeListCase;
			
			this.contenuT= new byte[(byte)(this.pile.size()*3+3)];  //on rÃ©cupÃ¨re la taille du tab, chaque case contient 3 infos + taille trame, type et ID
		
			this.contenuT[0]=(byte)(this.pile.size()*3+3);
			this.contenuT[1]=this.ID;
			
			for (Case Case1 : this.pile){   
				
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
			this.typeTrame=typeCaseInit;
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
		 * @return identifiant
		 */
		public byte getID(){
			return this.ID;
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
		 * @return direction du robot
		 */
		public int getDirection(){
			return this.direction;
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
		
		
		public Case toCase(){
			if(this.typeTrame == 1){
				Case nouvelleCase = new Case((int)this.contenuT[this.contenuT.length-4],(int)this.contenuT[this.contenuT.length-3]);
				nouvelleCase.update(this.contenuT[this.contenuT.length-2]);
				return  nouvelleCase;
			}
			else{
				return null;
			}
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
