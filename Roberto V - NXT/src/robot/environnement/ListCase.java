package robot.environnement;

import java.util.*;
import robot.taches.TachePrincipale;

/**
 * Cette classe contient une liste qui permet de gerer les cases qui sont
 * explor�es par le robot (en FIFO). Elle sert de buffer d'�mission, elle doit
 * �tre r�guli�rement vid�e (�mission des cases vers le superviseur).
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
	 * Attribut repr�sentant la tache principale.
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
	 * Compare les coordon�es en param�tre avec les coordon�es de la derni�re
	 * case ins�r�e dans la liste.
	 * 
	 * @param x
	 *            Coordon�e x de la case � tester.
	 * @param y
	 *            Coordon�e y de la case � tester.
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
	 * Ajoute une case en fin de liste. R�alise le changement necessaire pour
	 * passer du rep�re relatif au robot au r�p�re absolu du superviseur. Ainsi
	 * la case ajout�e est directement utilisable par le superviseur.
	 * 
	 * @param x
	 *            Coordon�e x de la case.
	 * @param y
	 *            Coordon�e y de la case.
	 * @param dir
	 *            Orientation du robot.
	 * @param murAvant
	 *            Mur eventuel � l'avant du robot (true si un mur est pr�sent).
	 * @param murGauche
	 *            Mur eventuel � gauche du robot (true si un mur est pr�sent).
	 * @param murArriere
	 *            Mur eventuel � droite du robot (true si un mur est pr�sent).
	 * @param murDroit
	 *            Mur eventuel � l'arri�re du robot (true si un mur est pr�sent).
	 * @return 0 si l'op�ration s'est bien d�roul�e.
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