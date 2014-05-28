package robot.evenements;

import java.util.ArrayList;
import java.util.Random;

import lejos.nxt.Button;
import robot.taches.*;

/**
 * Cette classe contient tout les éléments necessaires à la gestion des ordres
 * comme la liste d'ordres, le choix des ordres, et l'execution de l'ordre.
 * 
 * @author Thomas
 * 
 */
public class Ordre {
	
	// ------------------------------------- CONSTANTES -------------------------------------------
	
	/**
	 * Ordre : immobilise le robot.
	 */
	public static final int STOP = 0;

	/**
	 * Ordre : fait avancer le robot sur la case suivante.
	 */
	public static final int AVANCER = 1;

	/**
	 * Ordre : fait tourner le robot à gauche.
	 */
	public static final int TOURNER_A_GAUCHE = 2;

	/**
	 * Ordre : fait tourner le robot à droite.
	 */
	public static final int TOURNER_A_DROITE = 3;

	/**
	 * Ordre : fait faire un demi tour au robot.
	 */
	public static final int DEMITOUR = 4;

	/**
	 * Ordre : lancer un calibrage de la boussole.
	 */
	public static final int CALIBRER_BOUSSOLE = 5;

	/**
	 * Ordre : sauvegarde de l'angle de référence.
	 */
	public static final int ENREGISTRER_ANGLE_REF = 6;

	/**
	 * Ordre : verifier les 4 murs de la case et l'ajouter à la liste de cases.
	 */
	public static final int EXPLORER_PREMIERE_CASE = 7;
	
	/**
	 * Ordre : fixer les coordonnées du robot.
	 */
	public static final int SETPOSITION = 8;

	/**
	 * Ordre : attendre que l'utilisateur appuie sur le bouton ENTER du robot.
	 */
	public static final int ATTENDRE_BOUTON = 10;

	/**
	 * Ordre : le robot ne fait rien pendant 1 seconde.
	 */
	public static final int ATTENDRE_1SEC = 11;
	
	/**
	 * Ordre : met le robot en mode rapide.
	 */
	public static final int FASTMODE = 15;
	
	/**
	 * Ordre : met le robot en mode normal d'exploration (mode par défaut).
	 */
	public static final int NORMALMODE = 13;
	
	/**
	 * Ordre : Vider la liste d'ordres.</br> Attention cette ordre est executé
	 * immédiatement à sa reception.
	 */
	public static final int VIDER_ORDRES = 9;
	
	/**
	 * Ordre : Envoyer la liste de cases.</br> Attention cette ordre est executé
	 * immédiatement à sa reception.
	 */
	public static final int ENVOYER_CASE = 12;
		
	/**
	 * Ordre : Envoyer l'état du robot.</br> Attention cette ordre est executé
	 * immédiatement à sa reception.
	 */
	public static final int ENVOYER_ISBUSY = 16;
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------
	
	/**
	 * Ordre actuel du robot.
	 */
	private int ordreActuel;
	
	/**
	 * Indique si le robot est en train d'executer un ordre.
	 */
	private int isBusy;

	/**
	 * Attribut contenant la liste d'ordres.
	 */
	private ArrayList<Integer> list;
	
	/**
	 * Utiliser dans la méthode choisirOrdreRandom() pour les tests.
	 */
	private int ancienRandom=-1;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur de Ordre.
	 */
	public Ordre() {
		this.list = new ArrayList<Integer>();
		this.ordreActuel = STOP;
		this.isBusy=1;
	}
	
	// ------------------------------------- METHODES ---------------------------------------------

	/**
	 * Ajoute un ordre à la fin de la liste.
	 * 
	 * @param o
	 *            Ordre à insérer dans la liste.
	 * @return 0 si l'opération s'est bien déroulée.
	 */
	public synchronized int ajouterOrdre(int o) {
		if (o == STOP || o == AVANCER || o == TOURNER_A_GAUCHE
				|| o == TOURNER_A_DROITE || o == DEMITOUR
				|| o == CALIBRER_BOUSSOLE || o == ENREGISTRER_ANGLE_REF
				|| o == EXPLORER_PREMIERE_CASE || o == ATTENDRE_1SEC 
				|| o == ATTENDRE_BOUTON || o == FASTMODE 
				|| o == NORMALMODE || o == SETPOSITION) {
			this.isBusy=1;
			this.list.add(o);
			return 0;
		} else {
			System.out.println("ajouterOrdre\nerr ordre "+o);
			return 1;
		}
	}
	
	/**
	 * Renvoit l'ordre souhaité en argument s' il existe (sans le retirer de la liste).
	 * L'index 0 correspond au prochain ordre executé.
	 * @param idx
	 * 		index de l'ordre à récupérer.
	 * @return l'ordre désiré, -1 s'il n'y a pas d'ordre à cet index.
	 */
	public synchronized Integer voirOrdre(int idx) {
		if(this.list.size()>idx) {
			return this.list.get(idx);
		} else {
			return -1;
		}
	}
	
	/**
	 * Vide entierement la liste.
	 */
	public synchronized void vider() {
		this.list.clear();
	}

	/**
	 * Vérifie si la liste est vide.
	 * 
	 * @return true si la liste est vide.
	 */
	public synchronized boolean estVide() {
		if (this.list.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Méthode de test, algorithme d'exploration basique.
	 * 
	 * @param tPrincipale
	 */
	public void choisirOrdreExploration(TachePrincipale tPrincipale) {
		if (!tPrincipale.getEnv().getMurAvant()) {
			this.ajouterOrdre(AVANCER);
		} else if (!tPrincipale.getEnv().getMurDroit()) {
			this.ajouterOrdre(TOURNER_A_DROITE);
		} else if (!tPrincipale.getEnv().getMurGauche()) {
			this.ajouterOrdre(TOURNER_A_GAUCHE);
		} else {
			this.ajouterOrdre(DEMITOUR);
		}
		this.ordreActuel = this.getOrdreSuivant(tPrincipale);
	}
	
	/**
	 * Méthode de test, choix de la direction random (a condition qu'il n'y ait
	 * pas de mur).
	 * 
	 * @param tPrincipale
	 */
	public void choisirOrdreRandom(TachePrincipale tPrincipale) {
		Random generateurRandom = new Random();
		int valeurRandom;
		boolean trouve=false;
		
		if(tPrincipale.getEnv().getMurAvant() && tPrincipale.getEnv().getMurGauche() && tPrincipale.getEnv().getMurDroit()) {
			this.ajouterOrdre(DEMITOUR);
			this.ancienRandom=DEMITOUR;
		} else if(this.ancienRandom==DEMITOUR || this.ancienRandom==TOURNER_A_DROITE || this.ancienRandom==TOURNER_A_GAUCHE ) {
			this.ajouterOrdre(AVANCER);
			this.ancienRandom=AVANCER;
		} else {
			while(!trouve) {
				valeurRandom = generateurRandom.nextInt(3);
				if(valeurRandom==0){
					if(!tPrincipale.getEnv().getMurAvant()) {
						this.ajouterOrdre(AVANCER);
						this.ancienRandom=AVANCER;
						trouve=true;
					}
				} else if(valeurRandom==1) {
					if(!tPrincipale.getEnv().getMurDroit()) {
						this.ajouterOrdre(TOURNER_A_DROITE);
						this.ancienRandom=TOURNER_A_DROITE;
						trouve=true;
					}
				} else if(valeurRandom==2) {
					if(!tPrincipale.getEnv().getMurGauche()) {
						this.ajouterOrdre(TOURNER_A_GAUCHE);
						this.ancienRandom=TOURNER_A_GAUCHE;
						trouve=true;
					}
				}	
			}
		}
		this.ordreActuel = this.getOrdreSuivant(tPrincipale);
	}

	/**
	 * Mettre à jour l'ordre à executer.
	 * 
	 * @param tPrincipale
	 */
	public void choisirOrdre(TachePrincipale tPrincipale) {
		this.ordreActuel = this.getOrdreSuivant(tPrincipale);
	}

	/**
	 * Execute l'ordre contenu dans ordreActuel.
	 * 
	 * @param tPrincipale
	 */
	public void executerOrdre(TachePrincipale tPrincipale) {
		if (this.ordreActuel == AVANCER) {
			tPrincipale.getMouv().avancer(tPrincipale);
		} else if (this.ordreActuel == TOURNER_A_DROITE) {
			tPrincipale.getMouv().tournerADroite(tPrincipale);
		} else if (this.ordreActuel == TOURNER_A_GAUCHE) {
			tPrincipale.getMouv().tournerAGauche(tPrincipale);
		} else if (this.ordreActuel == DEMITOUR) {
			tPrincipale.getMouv().faireDemiTour(tPrincipale);
		} else if (this.ordreActuel == STOP) {
			tPrincipale.getMouv().stop();
		} else if (this.ordreActuel == CALIBRER_BOUSSOLE) {
			tPrincipale.getCapteurs().getBoussole().calibrer(tPrincipale);
		} else if (this.ordreActuel == ENREGISTRER_ANGLE_REF) {
			tPrincipale.getMouv().setRefAngleInit(tPrincipale);
		} else if (this.ordreActuel == EXPLORER_PREMIERE_CASE) {
			tPrincipale.getMouv().tournerAGauche(tPrincipale);
			tPrincipale.getMouv().tournerADroite(tPrincipale);
			tPrincipale.getEnv().enregistrerCaseActuelle(tPrincipale);
		} else if (this.ordreActuel == ATTENDRE_BOUTON) {
			this.pauseBouton();
		} else if (this.ordreActuel == ATTENDRE_1SEC) {
			this.pauseTemps(1000);
		} else if (this.ordreActuel == FASTMODE) {
			tPrincipale.getMouv().setFastMode(true);
		} else if (this.ordreActuel == NORMALMODE) {
			tPrincipale.getMouv().setFastMode(false);
		} else if (this.ordreActuel == SETPOSITION) {
			tPrincipale.getEnv().setPosition();
		} else {
			System.out.println("executerOrdre:\nerr ordre "+this.ordreActuel);
		}
	}
	
	/**
	 * Execute l'ordre passé en argument.
	 * 
	 * @param tPrincipale
	 * @param ordre
	 */
	public void executerOrdre(TachePrincipale tPrincipale, int ordre) {
		if (ordre == AVANCER) {
			tPrincipale.getMouv().avancer(tPrincipale);
		} else if (ordre == TOURNER_A_DROITE) {
			tPrincipale.getMouv().tournerADroite(tPrincipale);
		} else if (ordre == TOURNER_A_GAUCHE) {
			tPrincipale.getMouv().tournerAGauche(tPrincipale);
		} else if (ordre == DEMITOUR) {
			tPrincipale.getMouv().faireDemiTour(tPrincipale);
		} else if (ordre == STOP) {
			tPrincipale.getMouv().stop();
		} else if (ordre == CALIBRER_BOUSSOLE) {
			tPrincipale.getCapteurs().getBoussole().calibrer(tPrincipale);
		} else if (ordre == ENREGISTRER_ANGLE_REF) {
			tPrincipale.getMouv().setRefAngleInit(tPrincipale);
		} else if (ordre == EXPLORER_PREMIERE_CASE) {
			tPrincipale.getMouv().tournerAGauche(tPrincipale);
			tPrincipale.getMouv().tournerADroite(tPrincipale);
			tPrincipale.getEnv().enregistrerCaseActuelle(tPrincipale);
		} else if (ordre == ATTENDRE_BOUTON) {
			this.pauseBouton();
		} else if (ordre == ATTENDRE_1SEC) {
			this.pauseTemps(1000);
		} else if (ordre == FASTMODE) {
			tPrincipale.getMouv().setFastMode(true);
		} else if (ordre == NORMALMODE) {
			tPrincipale.getMouv().setFastMode(false);
		} else if (ordre == SETPOSITION) {
			tPrincipale.getEnv().setPosition();
		} else {
			System.out.println("executerOrdre:\nerr ordre "+ordre);
		}
	}

	/**
	 * Renvoie une chaine de caractères correspondant à l'ordre en argument.
	 * @param o
	 * 		l'ordre à afficher.
	 * @return l'ordre sous la forme d'un String.
	 */
	public String printOrdre(int o) {
		if (o == STOP) {
			return "stop";
		} else if (o == AVANCER) {
			return "avancer";
		} else if (o == TOURNER_A_DROITE) {
			return "droite";
		} else if (o == TOURNER_A_GAUCHE) {
			return "gauche";
		} else if (o == DEMITOUR) {
			return "demitour";
		} else if (o == CALIBRER_BOUSSOLE) {
			return "cal boussole";
		} else if (o == ENREGISTRER_ANGLE_REF) {
			return "sauv. ref";
		} else if (o == EXPLORER_PREMIERE_CASE) {
			return "explo1case";
		} else if (o == VIDER_ORDRES) {
			return "vider ordres";
		} else if (o == ATTENDRE_BOUTON) {
			return "wait bouton";
		} else if (o == ATTENDRE_1SEC) {
			return "wait 1sec";
		} else if (o == FASTMODE) {
			return "fast mode";
		} else if (o == NORMALMODE) {
			return "normal mode";
		} else if (o == SETPOSITION) {
			return "setPos";
		} else if (o == ENVOYER_CASE) {
			return "sendcase";
		} else if (o == ENVOYER_ISBUSY) {
			return "sendbusy";
		} else {
			return String.valueOf(o);
		}
	}
	
	/**
	 * Attend que l'utilisateur appuie sur un bouton.
	 */
	public void pauseBouton() {
		System.out.println("pause:bouton?");
		Button.waitForAnyPress();
	}

	/**
	 * Pause en ms.
	 * 
	 * @param ms
	 */
	public void pauseTemps(int ms) {
		long initTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - initTime) < ms) {}
	}
	
	// ------------------------------------- GETTERS ----------------------------------------------

	/**
	 * Retire et renvoie l'ordre le plus ancien présent dans la liste.
	 * 
	 * @param tPrincipale
	 * @return l'ordre le plus ancien présent dans la liste ou l'ordre STOP si la liste est vide.
	 */
	private synchronized Integer getOrdreSuivant(TachePrincipale tPrincipale) {
		if (this.list.isEmpty()) {
			if(this.isBusy!=0) {
				this.isBusy=0;
				if(tPrincipale.getMouv().getFastMode()) {
					tPrincipale.getEnv().enregistrerCaseActuelle(tPrincipale);
				}
			}
			return Ordre.STOP;
		} else {
			return this.list.remove(0);
		}
	}
	
	/**
	 * @return le nombre d'ordre en attente.
	 */
	public synchronized int getNombreOrdre() {
		return this.list.size();
	}

	/**
	 * @return la valeur de l'attribut list.
	 */
	public synchronized ArrayList<Integer> getList() {
		return this.list;
	}

	/**
	 * @return la valeur de l'attribut currentOrder.
	 */
	public int getOrdreActuel() {
		return this.ordreActuel;
	}
	
	/**
	 * @return 0 si le robot ne fait rien.
	 */
	public synchronized int getIsBusy() {
		return this.isBusy;
	}
}
