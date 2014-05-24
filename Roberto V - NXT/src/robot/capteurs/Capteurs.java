package robot.capteurs;

import robot.taches.TachePrincipale;
import lejos.nxt.SensorPort;

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
	public static final int TAB_NBDATA = 4;
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------
	
	/**
	 * Attribut repr�sentant la tache principale.
	 */
	private TachePrincipale tPrincipale;

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
	 * 
	 * @param tPrincipaleInit
	 */
	public Capteurs(TachePrincipale tPrincipaleInit) {
		this.tPrincipale=tPrincipaleInit;
		this.boussole = new Boussole(PORT_BOUSSOLE, tPrincipaleInit);
		this.capteurLumiere = new Lumiere(PORT_LUMIERE);
		this.sonarAvantGauche = new Sonar(PORT_SONARAVANTGAUCHE);
		this.sonarDroit = new Sonar(PORT_SONARDROIT);
		this.sonarEstDevant = false;
	}
	
	// ------------------------------------- METHODES ---------------------------------------------
	
	/**
	 * Met � jour les donn�es issues de tout les capteurs.
	 */
	public void miseAJour() {
		this.sonarAvantGauche.rafraichir();
		this.sonarDroit.rafraichir();
		this.boussole.rafraichir();
		this.capteurLumiere.rafraichir();
		if (this.sonarEstDevant) {
			this.tPrincipale.getEnv().setMurAvant(this.sonarAvantGauche.getMoyData());
		} else {
			this.tPrincipale.getEnv().setMurGauche(this.sonarAvantGauche.getMoyData());
		}
		this.tPrincipale.getEnv().setMurDroit(this.sonarDroit.getMoyData());
		this.tPrincipale.getEnv().setCibleIci(this.capteurLumiere.getMoyData());
		
	}

	/**
	 * Met � jour les donn�es issues de tout les capteurs en remplissant leur filtre.
	 */
	public void miseAJourComplete() {
		for (int i = 0; i < TAB_NBDATA; i++) {
			this.sonarAvantGauche.rafraichir();
			this.sonarDroit.rafraichir();
			this.boussole.rafraichir();
			this.capteurLumiere.rafraichir();
		}
		if (this.sonarEstDevant) {
			this.tPrincipale.getEnv().setMurAvant(this.sonarAvantGauche.getMoyData());
		} else {
			this.tPrincipale.getEnv().setMurGauche(this.sonarAvantGauche.getMoyData());
		}
		this.tPrincipale.getEnv().setMurDroit(this.sonarDroit.getMoyData());
		this.tPrincipale.getEnv().setCibleIci(this.capteurLumiere.getMoyData());
	}

	/**
	 * Tourne le sonar rotatif en position avant.
	 */
	public void tournerSonarDevant() {
		this.tPrincipale.getMouv().getMoteurSonar().rotateTo(-90);
		this.sonarEstDevant = true;
		this.miseAJourComplete();
	}

	/**
	 * Tourne le sonar rotatif en position gauche.
	 */
	public void tournerSonarAGauche() {
		this.tPrincipale.getMouv().getMoteurSonar().rotateTo(0);
		this.sonarEstDevant = false;
		this.miseAJourComplete();
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
