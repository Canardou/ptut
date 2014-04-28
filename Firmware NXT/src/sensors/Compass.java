package sensors;

import robot.*;
import env.*;
import threads.ThreadRobot;
import lejos.nxt.I2CPort;
import lejos.nxt.addon.CompassHTSensor;

/**
 * Cette classe représente une boussole. Elle possède un filtre passe-bas
 * (moyenne).
 * 
 * @author Thomas
 * @see CompassHTSensor
 */
public class Compass extends CompassHTSensor {

	/**
	 * Angle à réaliser pour faire les 2 tours de calibration de la boussole en
	 * degrés.
	 */
	public static final int ANGLE_CAL = 760;

	/**
	 * Attribut représentant le robot.
	 * 
	 * @see Robot
	 */
	private ThreadRobot robot;

	/**
	 * Attribut contenant un tableau d'échantillons de la boussole.
	 * 
	 * @see Param#TAB_NBDATA
	 */
	private double data[];

	/**
	 * Attribut contenant l'index du tableau d'échantillons.
	 */
	private int idxData;

	/**
	 * Attribut contenant la moyenne des valeurs dans le tableau d'échantillons.
	 */
	private double moyData;

	/**
	 * Attribut indiquant si la calibration a été faite au moins une fois.
	 */
	private boolean calDone;

	/**
	 * Constructeur de Compass.
	 * 
	 * @param port
	 *            Port auquel le capteur est branché.
	 * @param botinit
	 *            Objet robot.
	 * @see Robot
	 * @see I2CPort
	 */
	public Compass(I2CPort port, ThreadRobot botinit) {
		super(port);
		this.robot = botinit;
		this.data = new double[Environment.TAB_NBDATA];
		this.idxData = 0;
		this.moyData = 0;
		this.calDone = false;
	}

	/**
	 * Fonction à utiliser pour lire l'angle renvoyé par la boussole.
	 * 
	 * @return la valeur moyenne du tableau d'échantillons.
	 */
	public double getMoyData() {
		return this.moyData;
	}

	/**
	 * Réalise la calibration de la boussole. Met à jour l'attribut calDone.
	 */
	public void calibrate() {
		System.out.println("Cal compass");
		this.robot.getMov().getDiffPilot().setRotateSpeed(Movement.RSPEED_CAL);
		this.startCalibration();
		this.robot.getMov().getDiffPilot().rotate(ANGLE_CAL);
		this.stopCalibration();
		this.robot.getMov().getDiffPilot()
				.setRotateSpeed(Movement.RSPEED_CRUISE);
		this.calDone = true;
	}

	/**
	 * Acquisition d'un échantillon et mise à jour de la valeur moyenne.
	 */
	public void refresh() {
		this.acquisition();
		this.moyData = this.moyenne();
	}

	/**
	 * @return la moyenne du tableau d'échantillons.
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
	 * 
	 * @see Compass#data
	 * @see Compass#idxData
	 */
	private void acquisition() {
		this.data[this.idxData] = this.getDegreesCartesian();
		this.idxData = (this.idxData + 1) % Environment.TAB_NBDATA;
	}

	/**
	 * @return la valeur de la variable calDone.
	 * @see Compass#calDone
	 */
	public boolean getCalDone() {
		return this.calDone;
	}
}
