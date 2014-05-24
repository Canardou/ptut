package robot.capteurs;

import lejos.nxt.I2CPort;
import lejos.nxt.UltrasonicSensor;

/**
 * Cette classe repr�sente un sonar.
 * 
 * @author Thomas
 */
public class Sonar extends UltrasonicSensor {
	
	// ------------------------------------- CONSTANTES -------------------------------------------

	/**
	 * Offset d'erreur des sonars en cm.
	 */
	public static final double SONAR_OFFSET = 2;

	// ------------------------------------- ATTRIBUTS --------------------------------------------
	
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
	 * Constructeur de Sonar.
	 * 
	 * @param port
	 *            Port auquel est branch� le capteur.
	 */
	public Sonar(I2CPort port) {
		super(port);
		this.data = new double[Capteurs.TAB_NBDATA];
		this.idxData = 0;
		this.moyData = 0;
	}

	// ------------------------------------- METHODES ---------------------------------------------
	
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
	 * Acquiert une valeur et la place dans le tableau.
	 */
	private void acquisition() {
		this.data[this.idxData] = (this.getRange() - SONAR_OFFSET);
		this.idxData = (this.idxData + 1) % Capteurs.TAB_NBDATA;
	}
	
	// ------------------------------------- GETTERS -----------------------------------------------

	/**
	 * Methode � utiliser pour lire la distance renvoy�e par le capteur.
	 * 
	 * @return la valeur moyenne du tableau d'�chantillons.
	 */
	public double getMoyData() {
		return this.moyData;
	}
}