package sensors;

import env.*;
import lejos.nxt.I2CPort;
import lejos.nxt.UltrasonicSensor;

/**
 * Cette classe repr�sente un sonar. Il poss�de un filtre passe-bas (moyenne).
 * 
 * @author Thomas
 * @see UltrasonicSensor
 */
public class Sonar extends UltrasonicSensor {

	/**
	 * Offset d'erreur des sonars en cm.
	 */
	public static final double SONAR_OFFSET = 2;

	/**
	 * Attribut contenant un tableau d'�chantillons de la boussole.
	 */
	private double data[];

	/**
	 * Attribut contenant l'index du tableau d'�chantillons.
	 */
	private int idxData;

	/**
	 * Attribut contenant la moyenne des valeurs dans le tableau d'�chantillons.
	 */
	private double moyData;

	/**
	 * Constructeur de Sonar.
	 * 
	 * @param port
	 *            Port auquel est branch� le capteur.
	 * @see I2CPort
	 */
	public Sonar(I2CPort port) {
		super(port);
		this.data = new double[Environment.TAB_NBDATA];
		this.idxData = 0;
		this.moyData = 0;
	}

	/**
	 * Fonction � utiliser pour lire la distance renvoy� par le sonar.
	 * 
	 * @return la valeur moyenne du tableau d'�chantillons.
	 */
	public double getMoyData() {
		return this.moyData;
	}

	/**
	 * Acquisition d'un �chantillon et mise � jour de la valeur moyenne.
	 */
	public void refresh() {
		this.acquisition();
		this.moyData = this.moyenne();
	}

	/**
	 * @return la moyenne du tableau d'�chantillons.
	 */
	private double moyenne() {
		int i;
		double sum = 0;
		for (i = 0; i < Environment.TAB_NBDATA; i++) {
			sum += this.data[i];
		}
		return (sum / Environment.TAB_NBDATA);
	}

	/**
	 * Acquisitionne une valeur et la place dans le tableau.
	 */
	private void acquisition() {
		this.data[this.idxData] = (this.getRange() - SONAR_OFFSET);
		this.idxData = (this.idxData + 1) % Environment.TAB_NBDATA;
	}
}