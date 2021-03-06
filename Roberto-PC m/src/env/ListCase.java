package env;

import env.Case;
import java.util.*;

/**
 * Cette classe contient une liste qui permet de gerer les cases 
 * qui sont explor�es par le robot (en FIFO). Elle sert de buffer d'�mission,
 * elle doit �tre r�guli�rement vid�e (�mission des cases vers le superviseur).
 * @author Thomas
 * @see ArrayList
 * @see Case
 */
public class ListCase {	
	
	/**
	 * Attribut contenant la liste de cases
	 * @see ArrayList
	 */
	private ArrayList<Case> list;
	
	/**
	 * Constructeur de ListeCase
	 */
	public ListCase () {
		this.list = new ArrayList<Case>() ;
	}
	
	public ArrayList<Case> getArrayList() {
		return this.list;
	}
	
	/**
	 * Compare les coordon�es en param�tre avec les coordon�es de la derni�re case ins�r�e dans la liste
	 * @param x	  	
	 * 		Coordon�e x de la case � tester	
	 * @param y
	 * 		Coordon�e y de la case � tester
	 * @return true s'il ne s'agit pas de la meme case ou si la pile est vide
	 * @see Case
	 * @see ListCase#list
	 */
	public boolean isDifferent(int x, int y) {
		if(!this.list.isEmpty()) {
			return !(this.list.get(this.list.size()-1).getX()==x && this.list.get(this.list.size()-1).getY()==y); 
		}
		else {
			return true;
		}
	}
	
	/**
	 * Ajoute une case en fin de liste. R�alise le changement necessaire pour passer du rep�re relatif au robot 
	 * au r�p�re absolu du superviseur. Ainsi la case ajout�e est directement utilisable par le superviseur
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
	 * @return 0  si l'op�ration s'est bien d�roul�e
	 * @see Environment#x
	 * @see Environment#y
	 * @see Environment#dir
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 * @see Case
	 */
	public int addCase(int x, int y, int dir, boolean frontWall, boolean leftWall, boolean backWall, boolean rightWall)
	{		
		if( x>=0 && y>=0 && (dir==Case.UP || dir==Case.RIGHT || dir==Case.LEFT || dir==Case.DOWN) ) {
			
			Case caseTemp = new Case(x,y);
			caseTemp.setReveal();			
			
			if(dir==Case.RIGHT) {			
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
			else if(dir==Case.UP){
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
			else if(dir==Case.LEFT){
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
			else if(dir==Case.DOWN){
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
			System.out.println(caseTemp);
			if(this.list.add(caseTemp)) {
				return 0;
			}
			else {
				System.out.println("[ERR]addCase:add");
				return 1;
			}
		}
		else {
			System.out.println("[ERR]addCase:param");
			return 1;
		}
			
	}
	
	public void addCase2(int x, int y, int dir){
		Case caseTemp = new Case(x,y);
		this.list.add(caseTemp);
		
	}
	/**
	 * Retire la case de la liste (indice 0) et retourne l'objet
	 * @return la case la plus ancienne de la liste
	 * @see Case
	 */
	public Case getCase() {
		if(this.list.isEmpty()) {
			return null;
		}
		else {
			return this.list.remove(0);
		}		
	}
	
	/**
	 * Vide entierement la liste
	 */
	public void clear() {
		this.list.clear();
	}
	
	/**
	 * V�rifie si la liste est vide
	 * @return true si la liste est vide
	 */
	public boolean isEmpty() {
		if(this.list.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
}