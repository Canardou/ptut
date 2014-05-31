package robot;

import lejos.nxt.Sound;
import robot.actionneurs.Mouvement;
import robot.evenements.Ordre;
import robot.taches.*;
import tests.Tests;

/**
 * Cette classe permet de demarrer le programme du robot.
 * 
 * @author Thomas
 */
public class Init {
	
	// ------------------------------------- CONSTANTES -------------------------------------------
	
	/**
	 * Volume sonore du robot. De 0 à 100
	 */
	public static final int VOLUME = 80; 
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur de Init
	 */
	public Init() {
		TachePrincipale tPrincipale = new TachePrincipale();
		TacheCom tCom = new TacheCom(tPrincipale);
		this.init(tPrincipale);
		//this.initTest(tPrincipale); // a faire via la com
		tPrincipale.start();
		tCom.start();
	}
	
	// ------------------------------------- MAIN -------------------------------------------------

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		new Init();
	}
	
	// ------------------------------------- METHODES ---------------------------------------------

	/**
	 * Initialise divers paramètres. A appeller au lancement du programme.
	 */
	public void init(TachePrincipale tPrincipale) {
		Sound.setVolume(VOLUME);
		tPrincipale.getMouv().getDiffPilot().setTravelSpeed(Mouvement.VITESSE_CROISIERE);
		tPrincipale.getMouv().getDiffPilot().setRotateSpeed(Mouvement.VITESSE_ROTATION_CROISIERE);
		tPrincipale.getMouv().getMoteurSonar().setSpeed(Mouvement.VITESSE_ROTATION_SONAR);
	}

	/**
	 * Fonction d'initialisation test
	 */
	public void initTest(TachePrincipale tPrincipale) {
		Sound.setVolume(VOLUME);
		tPrincipale.getMouv().getDiffPilot().setTravelSpeed(Mouvement.VITESSE_CROISIERE);
		tPrincipale.getMouv().getDiffPilot().setRotateSpeed(Mouvement.VITESSE_ROTATION_CROISIERE);
		tPrincipale.getMouv().getMoteurSonar().setSpeed(Mouvement.VITESSE_ROTATION_SONAR);

		tPrincipale.getEnv().setInitPos(3, 3, 1);
		tPrincipale.getOrdre().executerOrdre(tPrincipale, Ordre.SETPOSITION);
		tPrincipale.getOrdre().executerOrdre(tPrincipale, Ordre.ATTENDRE_BOUTON);
		tPrincipale.getOrdre().executerOrdre(tPrincipale, Ordre.ATTENDRE_1SEC);
		tPrincipale.getOrdre().executerOrdre(tPrincipale, Ordre.ENREGISTRER_ANGLE_REF);
		tPrincipale.getOrdre().executerOrdre(tPrincipale, Ordre.EXPLORER_PREMIERE_CASE);
	}
}
