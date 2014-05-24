package robot.environnement;

import java.util.*;
import robot.taches.TachePrincipale;

/**
 * Cette classe contient une liste qui permet de gerer les cases qui sont
 * explorées par le robot (en FIFO). Elle sert de buffer d'émission, elle doit
 * être régulièrement vidée (émission des cases vers le superviseur).
 * 
 * @author Thomas
 */
public class ListCase {
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------
	
	/**
	 * ArrayList contenant les cases.
	 */
	private ArrayList<Case> list;
	
	/**
	 * Attribut représentant la tache principale.
	 */
	private TachePrincipale tPrincipale;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur de ListCase.
	 */
	public ListCase(TachePrincipale tPrincipaleInit) {
		this.list = new ArrayList<Case>();
		this.tPrincipale=tPrincipaleInit;
	}
	
	// ------------------------------------- METHODES ---------------------------------------------
	
	/**
	 * Compare les coordonées en paramètre avec les coordonées de la dernière
	 * case insérée dans la liste.
	 * 
	 * @param x
	 *            Coordonée x de la case à tester.
	 * @param y
	 *            Coordonée y de la case à tester.
	 * @return true s'il ne s'agit pas de la meme case ou si la pile est vide.
	 */
	public synchronized boolean estDifferente(int x, int y) {
		if (!this.list.isEmpty()) {
			return !(this.list.get(this.list.size() - 1).getX() == x && this.list.get(this.list.size() - 1).getY() == y);
		} else {
			return true;
		}
	}

	/**
	 * Ajoute une case en fin de liste. Réalise le changement necessaire pour
	 * passer du repère relatif au robot au répère absolu du superviseur. Ainsi
	 * la case ajoutée est directement utilisable par le superviseur.
	 * 
	 * @param x
	 *            Coordonée x de la case.
	 * @param y
	 *            Coordonée y de la case.
	 * @param dir
	 *            Orientation du robot.
	 * @param murAvant
	 *            Mur eventuel à l'avant du robot (true si un mur est présent).
	 * @param murGauche
	 *            Mur eventuel à gauche du robot (true si un mur est présent).
	 * @param murArriere
	 *            Mur eventuel à droite du robot (true si un mur est présent).
	 * @param murDroit
	 *            Mur eventuel à l'arrière du robot (true si un mur est présent).
	 * @return 0 si l'opération s'est bien déroulée.
	 */
	public synchronized int ajouterCase(int x, int y, int dir, boolean murAvant,
			boolean murGauche, boolean murArriere, boolean murDroit) {
		if (x >= 0
				&& y >= 0
				&& (dir == Case.UP || dir == Case.RIGHT || dir == Case.LEFT || dir == Case.DOWN)) {

			Case caseTemp = new Case(x, y);
			caseTemp.setReveal();
			
			if(this.tPrincipale.getEnv().getCibleIci()) {
				caseTemp.setMark();
				this.tPrincipale.getEnv().setCibleIci(false);
			}

			if (dir == Case.RIGHT) {
				if (murAvant) {
					caseTemp.close(Case.RIGHT);
				} else if (murGauche) {
					caseTemp.close(Case.UP);
				} else if (murArriere) {
					caseTemp.close(Case.LEFT);
				} else if (murDroit) {
					caseTemp.close(Case.DOWN);
				}
			} else if (dir == Case.UP) {
				if (murAvant) {
					caseTemp.close(Case.UP);
				} else if (murGauche) {
					caseTemp.close(Case.LEFT);
				} else if (murArriere) {
					caseTemp.close(Case.DOWN);
				} else if (murDroit) {
					caseTemp.close(Case.RIGHT);
				}
			} else if (dir == Case.LEFT) {
				if (murAvant) {
					caseTemp.close(Case.LEFT);
				} else if (murGauche) {
					caseTemp.close(Case.DOWN);
				} else if (murArriere) {
					caseTemp.close(Case.RIGHT);
				} else if (murDroit) {
					caseTemp.close(Case.UP);
				}
			} else if (dir == Case.DOWN) {
				if (murAvant) {
					caseTemp.close(Case.DOWN);
				} else if (murGauche) {
					caseTemp.close(Case.RIGHT);
				} else if (murArriere) {
					caseTemp.close(Case.UP);
				} else if (murDroit) {
					caseTemp.close(Case.LEFT);
				}
			}
			if (this.list.add(caseTemp)) {
				return 0;
			} else {
				System.out.println("ajouterCase:err add");
				return 1;
			}
		} else {
			System.out.println("ajouterCase:err param");
			return 1;
		}
	}
	
	/**
	 * Vide entierement la liste.
	 */
	public void vider() {
		this.list.clear();
	}

	/**
	 * @return true si la liste est vide.
	 */
	public boolean estVide() {
		if (this.list.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	// ------------------------------------- GETTERS ----------------------------------------------

	/**
	 * @return l'attribut contenant la liste de cases.
	 */
	public synchronized ArrayList<Case> getArrayList() {
		return this.list;
	}

	/**
	 * Retire la case de la liste (indice 0) et retourne l'objet.
	 * 
	 * @return la case la plus ancienne de la liste.
	 */
	public synchronized Case getCase() {
		if (this.list.isEmpty()) {
			return null;
		} else {
			return this.list.remove(0);
		}
	}
}