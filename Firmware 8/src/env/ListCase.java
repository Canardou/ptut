package env;

import robot.*;
import env.Case;
import java.util.*;

/**
 * Cette classe contient une pile qui permet de gerer les cases 
 * qui sont explor�es par le robot (en FIFO). Elle sert de buffer d'�mission,
 * elle doit �tre r�guli�rement vid�e (�mission des cases vers
 * le superviseur)
 * @author Thomas
 * @see Stack
 * @see Case
 */
public class ListCase {	
	
	/**
	 * Attribut contenant la pile de cases
	 * @see Stack
	 */
	private Stack<Case> pile;
	
	/**
	 * Constructeur de ListeCase
	 */
	public ListCase () {
		this.pile = new Stack<Case>() ;
	}

	/**
	 * Compare les coordon�es en param�tre avec les coordon�es de la derni�re case ins�r�e dans la pile (case en bas de la pile)
	 * @param x	  	
	 * 		Coordon�e x de la case � tester	
	 * @param y
	 * 		Coordon�e y de la case � tester
	 * @return true s'il ne s'agit pas de la meme case ou si la pile est vide
	 * @see Case
	 * @see ListCase#pile
	 */
	public boolean isDifferent(int x, int y) {
		if(!this.pile.isEmpty()) {
			return !(this.pile.elementAt(0).getX()==x && this.pile.elementAt(0).getY()==y); 
		}
		else {
			return true;
		}
	}
	
	/**
	 * Ajoute une case en bas de la pile. R�alise le changement necessaire pour passer du rep�re relatif au robot au r�p�re absolu du superviseur. Ainsi la case ajout�e est directement utilisable par le superviseur
	 * @param x
	 * 		Coordon�e x de la case
	 * @param y
	 * 		Coordon�e y de la case
	 * @param dir
	 * 		Orientation du robot 
	 * @param frontWall
	 * 		Mur eventuel � l'avant du robot (true si un mur est pr�sent)
	 * @param leftWall
	 * 		Mur eventuel � gauche du robot (true si un mur est pr�sent)
	 * @param backWall
	 * 		Mur eventuel � droite du robot (true si un mur est pr�sent)
	 * @param rightWall
	 * 		Mur eventuel � l'arri�re du robot (true si un mur est pr�sent)
	 * @see Environment#x
	 * @see Environment#y
	 * @see Environment#dir
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 * @see Case
	 */
	public void addCase(int x, int y, int dir, boolean frontWall, boolean leftWall, boolean backWall, boolean rightWall)
	{
		Case caseTemp = new Case(x,y);
		caseTemp.setReveal();
		
		if(dir==Param.XP) {			
			if(frontWall) {
				caseTemp.close(Case.RIGHT);
			}
			else if (leftWall) {
				caseTemp.close(Case.UP);
			}
			else if (backWall) {
				caseTemp.close(Case.LEFT);
			}
			else if (rightWall) {
				caseTemp.close(Case.DOWN);
			}
		}
		else if(dir==Param.YP){
			if(frontWall) {
				caseTemp.close(Case.UP);
			}
			else if (leftWall) {
				caseTemp.close(Case.LEFT);
			}
			else if (backWall) {
				caseTemp.close(Case.DOWN);
			}
			else if (rightWall) {
				caseTemp.close(Case.RIGHT);
			}	
		}
		else if(dir==Param.XN){
			if(frontWall) {
				caseTemp.close(Case.LEFT);
			}
			else if (leftWall) {
				caseTemp.close(Case.DOWN);
			}
			else if (backWall) {
				caseTemp.close(Case.RIGHT);
			}
			else if (rightWall) {
				caseTemp.close(Case.UP);
			}	
		}
		else if(dir==Param.YN){
			if(frontWall) {
				caseTemp.close(Case.DOWN);
			}
			else if (leftWall) {
				caseTemp.close(Case.RIGHT);
			}
			else if (backWall) {
				caseTemp.close(Case.UP);
			}
			else if (rightWall) {
				caseTemp.close(Case.LEFT);
			}	
		}		
		this.pile.insertElementAt(caseTemp, 0);
		System.out.println(caseTemp);
	}
	
	/**
	 * Retire la case en haut de la pile
	 * @return la case en haut de la pile ou null si la pile est vide
	 * @see Case
	 */
	public Case getCase() {
		if(this.pile.isEmpty()) {
			return null;
		}
		else {
			return this.pile.pop();
		}		
	}
	
	/**
	 * V�rifie si la pile est vide
	 * @return true si la pile est vide
	 */
	public boolean isEmpty() {
		if(this.pile.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
}