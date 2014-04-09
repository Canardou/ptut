package env;

import env.Case;
import java.util.*;

/**
 * Cette classe contient une liste qui permet de gerer les cases 
 * qui sont explorées par le robot (en FIFO). Elle sert de buffer d'émission,
 * elle doit être régulièrement vidée (émission des cases vers le superviseur).
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
	 * Compare les coordonées en paramètre avec les coordonées de la dernière case insérée dans la liste
	 * @param x	  	
	 * 		Coordonée x de la case à tester	
	 * @param y
	 * 		Coordonée y de la case à tester
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
	 * Ajoute une case en fin de liste. Réalise le changement necessaire pour passer du repère relatif au robot 
	 * au répère absolu du superviseur. Ainsi la case ajoutée est directement utilisable par le superviseur
	 * @param x
	 * 		Coordonée x de la case
	 * @param y
	 * 		Coordonée y de la case
	 * @param dir
	 * 		Orientation du robot 
	 * @param frontWall
	 * 		Mur eventuel à l'avant du robot (true si un mur est présent)
	 * @param leftWall
	 * 		Mur eventuel à gauche du robot (true si un mur est présent)
	 * @param backWall
	 * 		Mur eventuel à droite du robot (true si un mur est présent)
	 * @param rightWall
	 * 		Mur eventuel à l'arrière du robot (true si un mur est présent)
	 * @return 0  si l'opération s'est bien déroulée
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
	 * Vérifie si la liste est vide
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