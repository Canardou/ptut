package robot.environnement;

import lejos.nxt.Sound;
import robot.evenements.Ordre;
import robot.taches.TachePrincipale;

/**
 * Classe permettant de décrire l'environnement du robot. Cette classe contient
 * les variables qui indiquent la présence eventuelle des 4 murs autour du
 * robot. Elle contient aussi les coordonées du robot, ainsi que sa direction,
 * et leurs valeurs initiales (qui doivent être recues du superviseur). On
 * trouve aussi une liste de cases explorées qui doit être régulierement envoyée
 * au superviseur.
 * 
 * @author Thomas
 */
public class Environnement {
	
	// ------------------------------------- CONSTANTES -------------------------------------------

	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur à gauche. En
	 * cm.
	 */
	public static final double DISTANCE_MUR_GAUCHE = 21;

	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur à droite. En
	 * cm.
	 */
	public static final double DISTANCE_MUR_DROIT = 21;
	
	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur en face. En
	 * cm.
	 */
	public static final double DISTANCE_MUR_AVANT = 21;
	
	/**
	 * Valeur renvoyé par le capteur de lumière a partir de laquelle on
	 * considère qu'il y a la cible.
	 */
	public static final double CIBLE_LUMIERE = 525;
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------

	/**
	 * Information sur la présence d'un mur à l'avant du robot. Attention cette
	 * valeur peut-être incertaine durant l'execution d'un mouvement.
	 */
	private boolean murAvant;

	/**
	 * Information sur la présence d'un mur à droite du robot.
	 */
	private boolean murDroit;

	/**
	 * Information sur la présence d'un mur à gauche du robot. Attention cette
	 * valeur peut-être incertaine durant l'execution d'un mouvement.
	 */
	private boolean murGauche;

	/**
	 * Information sur la présence d'un mur à l'arrière du robot. Attention
	 * cette valeur peut-être incertaine durant l'execution d'un mouvement.
	 */
	private boolean murArriere;

	/**
	 * Coordonée x du robot.
	 */
	private int x;

	/**
	 * Coordonée y du robot.
	 */
	private int y;

	/**
	 * Direction du robot.
	 */
	private int dir;

	/**
	 * Coordonée initiale x du robot.
	 */
	private int xinit;

	/**
	 * Coordonée initiale y du robot.
	 */
	private int yinit;

	/**
	 * Direction initiale du robot.
	 */
	private int dirinit;

	/**
	 * Liste contenant les cases récemment explorées, à envoyer régulierement au
	 * superviseur.
	 */
	private ListCase listCase;

	/**
	 * Savoir si le robot ce situe sur la case de la cible.
	 */
	private boolean cibleIci;
	
	/**
	 * Savoir si la cible a été detectée au moins une fois par le robot.
	 */
	private boolean cibleTrouvee;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur de Environment. 
	 */
	public Environnement() {
		this.listCase = new ListCase();
		this.cibleIci = false;
		this.cibleTrouvee = false;
		this.xinit=0;
		this.yinit=0;
		this.dirinit=0;
	}

	// ------------------------------------- METHODES ---------------------------------------------

	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot
	 * tourne à gauche.
	 */
	public void majMurTourneAGauche() {
		this.murAvant = this.murGauche;
		this.murArriere = this.murDroit;
	}

	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot
	 * tourne à droite.
	 */
	public void majMurTourneADroite() {
		this.murAvant = this.murDroit;
		this.murArriere = this.murGauche;
	}

	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot
	 * fait demi-tour.
	 */
	public void majMurDemiTour() {
		boolean tempo;
		tempo = this.murAvant;
		this.murAvant = this.murArriere;
		this.murArriere = tempo;
	}

	/**
	 * Met à jour la variable de direction lorsque le robot tourne à droite.
	 */
	public synchronized void majDirTourneADroite() {
		if (this.dir == Case.RIGHT) {
			this.dir = Case.DOWN;
		} else if (this.dir == Case.LEFT) {
			this.dir = Case.UP;
		} else if (this.dir == Case.UP) {
			this.dir = Case.RIGHT;
		} else if (this.dir == Case.DOWN) {
			this.dir = Case.LEFT;
		} else {
			System.out.println("majDirTourneADroite:\nerr dir");
		}
	}

	/**
	 * Met à jour la variable de direction lorsque le robot tourne à gauche.
	 */
	public synchronized void majDirTourneAGauche() {
		if (this.dir == Case.RIGHT) {
			this.dir = Case.UP;
		} else if (this.dir == Case.LEFT) {
			this.dir = Case.DOWN;
		} else if (this.dir == Case.UP) {
			this.dir = Case.LEFT;
		} else if (this.dir == Case.DOWN) {
			this.dir = Case.RIGHT;
		} else {
			System.out.println("majDirTourneAGauche:\nerr dir");
		}
	}

	/**
	 * Met à jour la variable de direction lorsque le robot fait demi-tour.
	 */
	public synchronized void majDirDemiTour() {
		if (this.dir == Case.RIGHT) {
			this.dir = Case.LEFT;
		} else if (this.dir == Case.LEFT) {
			this.dir = Case.RIGHT;
		} else if (this.dir == Case.UP) {
			this.dir = Case.DOWN;
		} else if (this.dir == Case.DOWN) {
			this.dir = Case.UP;
		} else {
			System.out.println("majDirDemiTour:\nerr dir");
		}
	}
	
	/**
	 * Affiche les coordonées de la case actuelle ainsi que les murs présents.
	 */
	public synchronized void printEnv() {
		System.out.print("X=" + this.x + " Y=" + this.y + "D=" + this.dir + "\n");
		if (this.murAvant) {
			System.out.print("F ");
		}
		if (this.murDroit) {
			System.out.print("R ");
		}
		if (this.murArriere) {
			System.out.print("B ");
		}
		if (this.murGauche) {
			System.out.print("L");
		}
		System.out.print("\n");
	}

	/**
	 * Ajoute la case actuelle à la liste.
	 * 
	 * @param tPrincipale
	 */
	public synchronized void enregistrerCaseActuelle(TachePrincipale tPrincipale) {
		this.listCase.ajouterCase(tPrincipale, this.x, this.y, this.dir, murAvant, murGauche, murArriere, murDroit);
	}

	/**
	 * Met à jour les coordonées du robot en fonction de sa direction.
	 * 
	 * @param tPrincipale
	 * @return 0 si les coordonées ont été modifiées.
	 */
	public synchronized int majCoord(TachePrincipale tPrincipale) {
		if (tPrincipale.getOrdre().getOrdreActuel() == Ordre.AVANCER) {
			if (this.dir == Case.RIGHT) {
				this.x++;
				return 0;
			} else if (this.dir == Case.LEFT) {
				this.x--;
				return 0;
			} else if (this.dir == Case.UP) {
				this.y--;
				return 0;
			} else if (this.dir == Case.DOWN) {
				this.y++;
				return 0;
			} else {
				System.out.println("majCoord:err dir");
				return 1;
			}
		} else {
			System.out.println("majCoord:err ordre");
			return 1;
		}
	}
	
	// ------------------------------------- SETTERS ----------------------------------------------
	
	/**
	 * Met à jour la position à partir des valeurs contenues dans xinit, yinit,
	 * dirinit.
	 */
	public synchronized void setPosition() {
		this.x = this.xinit;
		this.y = this.yinit;
		this.dir = this.dirinit;
		System.out.println("SetPosition\nx=" + this.x + "y=" + this.y + "d=" + this.dir);
	}
	
	/**
	 * Met à jour les variables d'intialisation de la position à partir des
	 * valeurs passées en arguments.
	 * 
	 * @param xi
	 * @param yi
	 * @param diri
	 */
	public synchronized void setInitPos(int xi, int yi, int diri) {
		this.xinit = xi;
		this.yinit = yi;
		this.dirinit = diri;
	}
	
	/**
	 * Met à jour la variable determinant la présence d'un mur à l'arriere.
	 * 
	 * @param value
	 *            true si un mur est présent
	 */
	public void setMurArriere(boolean value) {
		this.murArriere = value;
	}
	
	/**
	 * @param arg
	 * 		Met à jour la variable cibleIci.
	 */
	public void setCibleIci(boolean arg) {
		this.cibleIci=arg;
	}
	

	/**
	 * Met à jour la variable indiquant la présence d'un mur à gauche.
	 * @param dist distance du mur.
	 */
	public void setMurGauche(double dist) {
		if (dist <= DISTANCE_MUR_GAUCHE) {
			this.murGauche = true;
		} else {
			this.murGauche = false;
		}
	}

	/**
	 * Met à jour la variable indiquant la présence d'un mur à l'avant.
	 * @param dist distance du mur.
	 */
	public void setMurAvant(double dist) {
		if (dist <= DISTANCE_MUR_AVANT) {
			this.murAvant = true;
		} else {
			this.murAvant = false;
		}
	}

	/**
	 * Met à jour la variable indiquant la présence d'un mur à droite.
	 * @param dist distance du mur.
	 */
	public void setMurDroit(double dist) {
		if (dist <= DISTANCE_MUR_DROIT) {
			this.murDroit = true;
		} else {
			this.murDroit = false;
		}
	}
	
	/**
	 * Met à jour la variable indiquant la présence de la cible au sol à partir
	 * des données issues des capteurs.
	 * @param lum lumière renvoyée par le capteur.
	 */
	public void setCibleIci(double lum) {
		if (lum > CIBLE_LUMIERE) {
			if (!this.cibleTrouvee){ 
				this.cibleTrouvee = true;
				this.cibleIci = true;
				Sound.beepSequenceUp();
			}		
		}
	}	
	
	// ------------------------------------- GETTERS ----------------------------------------------
	
	/**
	 * @return la présence ou non d'un mur à l'avant du robot.
	 */
	public boolean getMurAvant() {
		return this.murAvant;
	}

	/**
	 * @return la présence ou non d'un mur à droite du robot.
	 */
	public boolean getMurDroit() {
		return this.murDroit;
	}

	/**
	 * @return la présence ou non d'un mur à gauche du robot.
	 */
	public boolean getMurGauche() {
		return this.murGauche;
	}

	/**
	 * @return la présence ou non d'un mur à l'arrière du robot.
	 */
	public boolean getMurArriere() {
		return this.murArriere;
	}

	/**
	 * @return la liste de cases.
	 */
	public ListCase getListCase() {
		return this.listCase;
	}
	
	/**
	 * @return la présence d'une cible.
	 */
	public boolean getCibleIci() {
		return this.cibleIci;
	}
}