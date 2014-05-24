package robot.capteurs;

import lejos.nxt.I2CPort;
import lejos.nxt.addon.CompassHTSensor;
import robot.actionneurs.Mouvement;
import robot.taches.TachePrincipale;

/**
 * Cette classe repr�sente le capteur boussole. 
 * 
 * @author Thomas
 */
public class Boussole extends CompassHTSensor {
	
	// ------------------------------------- CONSTANTES -------------------------------------------

	/**
	 * Angle � r�aliser pour faire la calibration de la boussole.
	 */
	public static final int ANGLE_CAL = 760;

	/**
	 * Vitesse de rotation lors de la calibration. Elle doit rester faible pour
	 * une calibration efficace 20 deg/s maximum.
	 */
	public static final double VITESSE_ROTATION_CALIBRATION = 20;

	// ------------------------------------- ATTRIBUTS --------------------------------------------
	
	/**
	 * Tache principale du robot.
	 */
	private TachePrincipale tPrincipale;

	/**
	 * Tableau d'�chantillons des donn�es issues de la boussole.
	 */
	private double data[];

	/**
	 * Index du tableau d'�chantillons.
	 */
	private int idxData;

	/**
	 * Moyenne des valeurs dans le tableau d'�chantillons.
	 */
	private double moyData;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur du capteur boussole.
	 * 
	 * @param port
	 *            Port auquel le capteur est branch�.
	 * @param tPrincipaleInit
	 */
	public Boussole(I2CPort port, TachePrincipale tPrincipaleInit) {
		super(port);
		this.tPrincipale = tPrincipaleInit;
		this.data = new double[Capteurs.TAB_NBDATA];
		this.idxData = 0;
		this.moyData = 0;
	}
	
	// ------------------------------------- METHODES ---------------------------------------------
	
	/**
	 * R�alise la calibration de la boussole.
	 */
	public void calibrer() {
		System.out.println("Cal. boussole");
		this.tPrincipale.getMouv().getDiffPilot().setRotateSpeed(VITESSE_ROTATION_CALIBRATION);
		this.startCalibration();
		this.tPrincipale.getMouv().getDiffPilot().rotate(ANGLE_CAL);
		this.stopCalibration();
		this.tPrincipale.getMouv().getDiffPilot().setRotateSpeed(Mouvement.VITESSE_ROTATION_CROISIERE);
	}
	
	/**
	 * Acquisition d'un �chantillon (dans le tableau) et mise � jour de la
	 * valeur moyenne.
	 */
	public void rafraichir() {
		this.acquisition();
		this.moyData = this.moyenne();
	}
	
	/**
	 * @return la moyenne du tableau d'�chantillons.
	 */
	private double moyenne() {
		int i;
		double sum = 0;
		for (i = 0; i < Capteurs.TAB_NBDATA; i++) {
			sum += this.data[i];
		}
		return (sum / Capteurs.TAB_NBDATA);
	}
	
	/**
	 * Acquiert une valeur du capteur et la place dans le tableau.
	 */
	private void acquisition() {
		this.data[this.idxData] = this.getDegreesCartesian();
		this.idxData = (this.idxData + 1) % Capteurs.TAB_NBDATA;
	}
	
	// ------------------------------------- GETTERS -----------------------------------------------

	/**
	 * Methode � utiliser pour lire la valeur renvoy�e par le capteur.
	 * 
	 * @return la valeur moyenne du tableau d'�chantillons.
	 */
	public double getMoyData() {
		return this.moyData;
	}
}
