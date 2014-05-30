package robot.environnement;

import lejos.nxt.Sound;
import robot.evenements.Ordre;
import robot.taches.TachePrincipale;

/**
 * Classe permettant de d�crire l'environnement du robot. Cette classe contient
 * les variables qui indiquent la pr�sence eventuelle des 4 murs autour du
 * robot. Elle contient aussi les coordon�es du robot, ainsi que sa direction,
 * et leurs valeurs initiales (qui doivent �tre recues du superviseur). On
 * trouve aussi une liste de cases explor�es qui doit �tre r�gulierement envoy�e
 * au superviseur.
 * 
 * @author Thomas
 */
public class Environnement {
	
	// ------------------------------------- CONSTANTES -------------------------------------------

	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur � gauche. En
	 * cm.
	 */
	public static final double DISTANCE_MUR_GAUCHE = 21;

	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur � droite. En
	 * cm.
	 */
	public static final double DISTANCE_MUR_DROIT = 21;
	
	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur en face. En
	 * cm.
	 */
	public static final double DISTANCE_MUR_AVANT = 21;
	
	/**
	 * Valeur renvoy� par le capteur de lumi�re a partir de laquelle on
	 * consid�re qu'il y a la cible.
	 */
	public static final double CIBLE_LUMIERE = 525;
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------

	/**
	 * Information sur la pr�sence d'un mur � l'avant du robot. Attention cette
	 * valeur peut-�tre incertaine durant l'execution d'un mouvement.
	 */
	private boolean murAvant;

	/**
	 * Information sur la pr�sence d'un mur � droite du robot.
	 */
	private boolean murDroit;

	/**
	 * Information sur la pr�sence d'un mur � gauche du robot. Attention cette
	 * valeur peut-�tre incertaine durant l'execution d'un mouvement.
	 */
	private boolean murGauche;

	/**
	 * Information sur la pr�sence d'un mur � l'arri�re du robot. Attention
	 * cette valeur peut-�tre incertaine durant l'execution d'un mouvement.
	 */
	private boolean murArriere;

	/**
	 * Coordon�e x du robot.
	 */
	private int x;

	/**
	 * Coordon�e y du robot.
	 */
	private int y;

	/**
	 * Direction du robot.
	 */
	private int dir;

	/**
	 * Coordon�e initiale x du robot.
	 */
	private int xinit;

	/**
	 * Coordon�e initiale y du robot.
	 */
	private int yinit;

	/**
	 * Direction initiale du robot.
	 */
	private int dirinit;

	/**
	 * Liste contenant les cases r�cemment explor�es, � envoyer r�gulierement au
	 * superviseur.
	 */
	private ListCase listCase;

	/**
	 * Savoir si le robot ce situe sur la case de la cible.
	 */
	private boolean cibleIci;
	
	/**
	 * Savoir si la cible a �t� detect�e au moins une fois par le robot.
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
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * tourne � gauche.
	 */
	public void majMurTourneAGauche() {
		this.murAvant = this.murGauche;
		this.murArriere = this.murDroit;
	}

	/**
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * tourne � droite.
	 */
	public void majMurTourneADroite() {
		this.murAvant = this.murDroit;
		this.murArriere = this.murGauche;
	}

	/**
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * fait demi-tour.
	 */
	public void majMurDemiTour() {
		boolean tempo;
		tempo = this.murAvant;
		this.murAvant = this.murArriere;
		this.murArriere = tempo;
	}

	/**
	 * Met � jour la variable de direction lorsque le robot tourne � droite.
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
	 * Met � jour la variable de direction lorsque le robot tourne � gauche.
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
	 * Met � jour la variable de direction lorsque le robot fait demi-tour.
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
	 * Affiche les coordon�es de la case actuelle ainsi que les murs pr�sents.
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
	 * Ajoute la case actuelle � la liste.
	 * 
	 * @param tPrincipale
	 */
	public synchronized void enregistrerCaseActuelle(TachePrincipale tPrincipale) {
		this.listCase.ajouterCase(tPrincipale, this.x, this.y, this.dir, murAvant, murGauche, murArriere, murDroit);
	}

	/**
	 * Met � jour les coordon�es du robot en fonction de sa direction.
	 * 
	 * @param tPrincipale
	 * @return 0 si les coordon�es ont �t� modifi�es.
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
	 * Met � jour la position � partir des valeurs contenues dans xinit, yinit,
	 * dirinit.
	 */
	public synchronized void setPosition() {
		this.x = this.xinit;
		this.y = this.yinit;
		this.dir = this.dirinit;
		System.out.println("SetPosition\nx=" + this.x + "y=" + this.y + "d=" + this.dir);
	}
	
	/**
	 * Met � jour les variables d'intialisation de la position � partir des
	 * valeurs pass�es en arguments.
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
	 * Met � jour la variable determinant la pr�sence d'un mur � l'arriere.
	 * 
	 * @param value
	 *            true si un mur est pr�sent
	 */
	public void setMurArriere(boolean value) {
		this.murArriere = value;
	}
	
	/**
	 * @param arg
	 * 		Met � jour la variable cibleIci.
	 */
	public void setCibleIci(boolean arg) {
		this.cibleIci=arg;
	}
	

	/**
	 * Met � jour la variable indiquant la pr�sence d'un mur � gauche.
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
	 * Met � jour la variable indiquant la pr�sence d'un mur � l'avant.
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
	 * Met � jour la variable indiquant la pr�sence d'un mur � droite.
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
	 * Met � jour la variable indiquant la pr�sence de la cible au sol � partir
	 * des donn�es issues des capteurs.
	 * @param lum lumi�re renvoy�e par le capteur.
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
	 * @return la pr�sence ou non d'un mur � l'avant du robot.
	 */
	public boolean getMurAvant() {
		return this.murAvant;
	}

	/**
	 * @return la pr�sence ou non d'un mur � droite du robot.
	 */
	public boolean getMurDroit() {
		return this.murDroit;
	}

	/**
	 * @return la pr�sence ou non d'un mur � gauche du robot.
	 */
	public boolean getMurGauche() {
		return this.murGauche;
	}

	/**
	 * @return la pr�sence ou non d'un mur � l'arri�re du robot.
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
	 * @return la pr�sence d'une cible.
	 */
	public boolean getCibleIci() {
		return this.cibleIci;
	}
}