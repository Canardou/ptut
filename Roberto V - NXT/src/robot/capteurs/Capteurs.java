package robot.capteurs;

import lejos.nxt.SensorPort;
import robot.taches.TachePrincipale;

/**
 * Cette classe permet de gerer les diff�rents capteurs pr�sent sur le robot.
 * 
 * @author Thomas
 */
public class Capteurs {
	
	// ------------------------------------- CONSTANTES -------------------------------------------
	
	/**
	 * Port auquel est connect� le sonar droit.
	 */
	public static final SensorPort PORT_SONARDROIT = SensorPort.S1;
	
	/**
	 * Port auquel est connect� le sonar avant/gauche.
	 */
	public static final SensorPort PORT_SONARAVANTGAUCHE = SensorPort.S2;
	
	/**
	 * Port auquel est connect� le capteur de lumi�re.
	 */
	public static final SensorPort PORT_LUMIERE = SensorPort.S3;
	
	/**
	 * Port auquel est connect� la boussole.
	 */
	public static final SensorPort PORT_BOUSSOLE = SensorPort.S4;
	
	/**
	 * Taille du tableau (nombre d'�chantillons dans le filtre) des capteurs en
	 * nombre d'�l�ments.
	 */
	public static final int TAB_NBDATA = 3;
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------

	/**
	 * Attribut repr�sentant la boussole.
	 */
	private Boussole boussole;

	/**
	 * Attribut repr�sentant le sonar droit.
	 */
	private Sonar sonarDroit;

	/**
	 * Attribut repr�sentant le sonar gauche.
	 */
	private Sonar sonarAvantGauche;
	
	/**
	 * Attribut repr�sentant le capteur de luminosit�.
	 */
	private Lumiere capteurLumiere;
	
	/**
	 * Attribut permettant de savoir dans quelle position est le sonar
	 * avant/gauche. True si le sonar est dirig� vers l'avant, False s'il est
	 * dirig� vers la gauche.
	 */
	private boolean sonarEstDevant;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------
	
	/**
	 * Constructeur de Capteurs.
	 */
	public Capteurs() {
		this.boussole = new Boussole(PORT_BOUSSOLE);
		this.capteurLumiere = new Lumiere(PORT_LUMIERE);
		this.sonarAvantGauche = new Sonar(PORT_SONARAVANTGAUCHE);
		this.sonarDroit = new Sonar(PORT_SONARDROIT);
		this.sonarEstDevant = false;
	}
	
	// ------------------------------------- METHODES ---------------------------------------------
	
	/**
	 * Met � jour les donn�es issues de tout les capteurs. Met aussi � jour la
	 * pr�sence ou non des murs scann�s.
	 * 
	 * @param tPrincipale
	 */
	public void miseAJour(TachePrincipale tPrincipale) {
		this.sonarAvantGauche.rafraichir();
		this.sonarDroit.rafraichir();
		this.boussole.rafraichir();
		this.capteurLumiere.rafraichir();
		if (this.sonarEstDevant) {
			tPrincipale.getEnv().setMurAvant(this.sonarAvantGauche.getMoyData());
		} else {
			tPrincipale.getEnv().setMurGauche(this.sonarAvantGauche.getMoyData());
		}
		tPrincipale.getEnv().setMurDroit(this.sonarDroit.getMoyData());
		tPrincipale.getEnv().setCibleIci(this.capteurLumiere.getMoyData());
	}

	/**
	 * Met � jour les donn�es issues de tout les capteurs en remplissant leur filtre.
	 * 
	 * @param tPrincipale
	 */
	public void miseAJourComplete(TachePrincipale tPrincipale) {
		for (int i = 0; i < TAB_NBDATA; i++) {
			this.sonarAvantGauche.rafraichir();
			this.sonarDroit.rafraichir();
			this.boussole.rafraichir();
			this.capteurLumiere.rafraichir();
		}
		if (this.sonarEstDevant) {
			tPrincipale.getEnv().setMurAvant(this.sonarAvantGauche.getMoyData());
		} else {
			tPrincipale.getEnv().setMurGauche(this.sonarAvantGauche.getMoyData());
		}
		tPrincipale.getEnv().setMurDroit(this.sonarDroit.getMoyData());
		tPrincipale.getEnv().setCibleIci(this.capteurLumiere.getMoyData());
	}

	/**
	 * Tourne le sonar rotatif en position avant.
	 * 
	 * @param tPrincipale
	 */
	public void tournerSonarDevant(TachePrincipale tPrincipale) {
		tPrincipale.getMouv().getMoteurSonar().rotateTo(-90);
		this.sonarEstDevant = true;
		this.miseAJourComplete(tPrincipale);
	}

	/**
	 * Tourne le sonar rotatif en position gauche.
	 * 
	 * @param tPrincipale
	 */
	public void tournerSonarAGauche(TachePrincipale tPrincipale) {
		tPrincipale.getMouv().getMoteurSonar().rotateTo(0);
		this.sonarEstDevant = false;
		this.miseAJourComplete(tPrincipale);
	}

	// ------------------------------------- GETTERS ----------------------------------------------
	
	/**
	 * @return l'attribut repr�sentant la boussole.
	 */
	public Boussole getBoussole() {
		return this.boussole;
	}

	/**
	 * @return l'attribut repr�sentant le sonar droit.
	 */
	public Sonar getSonarDroit() {
		return this.sonarDroit;
	}

	/**
	 * @return l'attribut repr�sentant le sonar rotatif avant/gauche.
	 */
	public Sonar getSonarAvantGauche() {
		return this.sonarAvantGauche;
	}

	/**
	 * @return l'attribut repr�sentant le capteur de lumi�re.
	 */
	public Lumiere getCapteurLumiere() {
		return this.capteurLumiere;
	}
	
	/**
	 * @return true si le sonar est en position avant.
	 */
	public boolean getSonarEstDevant() {
		return this.sonarEstDevant;
	}
}
