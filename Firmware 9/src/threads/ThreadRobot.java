package threads;

import env.*;
import robot.*;
import sensors.*;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

/**
 * Classe principale du code embarqué sur le robot. Elle contient tout les
 * éléments qui permettent de représenter le robot.
 * 
 * @author Thomas
 */
public class ThreadRobot extends Thread {
	
	/**
	 * Diamètre des roues du robot en cm
	 */
 	public static final double DIAMROUE = 5.6;
 	
 	/**
 	 * Distance inter-roues du robot en cm
 	 */
    public static final double DISTROUE = 11.5; 

	/**
	 * Attribut représentant les mouvements du robot
	 * 
	 * @see Movement
	 * @see Robot#getMov()
	 */
	private Movement mov;

	/**
	 * Attribut représentant l'environnement du robot
	 * 
	 * @see Environment
	 * @see Robot#getEnv()
	 */
	private Environment env;

	/**
	 * Attribut représentant le moteur du sonar
	 * 
	 * @see NXTRegulatedMotor
	 * @see Robot#getSonarMotor
	 */
	private NXTRegulatedMotor sonarMotor;

	/**
	 * Attribut représentant la boussole
	 * 
	 * @see Compass
	 * @see Robot#getCompass()
	 */
	private Compass compass;

	/**
	 * Attribut représentant le sonar droit
	 * 
	 * @see Sonar
	 * @see Robot#getRightSonar
	 */
	private Sonar rightSonar;

	/**
	 * Attribut représentant le sonar gauche
	 * 
	 * @see Sonar
	 * @see Robot#getLeftFrontSonar
	 */
	private Sonar leftFrontSonar;
	
	/**
	 * Attribut représentant le capteur de luminosité.
	 */
	private Light lightSensor;

	/**
	 * Attribut représentant la gestion des ordres
	 */
	private Order order;

	/**
	 * Constructeur de Robot
	 */
	public ThreadRobot() {
		this.mov = new Movement(DIAMROUE, DISTROUE, Motor.A, Motor.B, false, this);
		this.env = new Environment(this);
		this.sonarMotor = Motor.C;
		this.compass = new Compass(SensorPort.S4, this);
		//this.lightSensor =new Light(SensorPort.S3);
		this.leftFrontSonar = new Sonar(SensorPort.S2);
		this.rightSonar = new Sonar(SensorPort.S1);
		this.order = new Order(this);
		this.setPriority(5);
	}

	/**
	 * Tache principale du robot
	 */
	public void run() {
		while (true) {
			this.order.chooseExploration();
			this.order.execute();
		}
	} 

	/**
	 * @return la valeur de l'attribut mov
	 * @see Movement
	 * @see Robot#mov
	 */
	public Movement getMov() {
		return this.mov;
	}

	/**
	 * @return la valeur de l'attribut env
	 * @see Environment
	 * @see Robot#env
	 */
	public Environment getEnv() {
		return this.env;
	}

	/**
	 * @return la valeur de l'attribut sonarMotor
	 * @see NXTRegulatedMotor
	 * @see Robot#sonarMotor
	 */
	public NXTRegulatedMotor getSonarMotor() {
		return this.sonarMotor;
	}

	/**
	 * @return la valeur de l'attribut compass
	 * @see Compass
	 * @see Robot#compass
	 */
	public Compass getCompass() {
		return this.compass;
	}

	/**
	 * @return la valeur de l'attribut rightSonar
	 * @see Sonar
	 * @see Robot#rightSonar
	 */
	public Sonar getRightSonar() {
		return this.rightSonar;
	}

	/**
	 * @return la valeur de l'attribut leftFrontSonar
	 * @see Sonar
	 * @see Robot#leftFrontSonar
	 */
	public Sonar getLeftFrontSonar() {
		return this.leftFrontSonar;
	}
	
	/**
	 * @return l'objet ordre du robot
	 */
	public Order getOrder() {
		return this.order;
	}
	
	/**
	 * @return la valeur de l'attribut lightSensor
	 * @see Light
	 * @see Robot#lightSensor
	 */
	public Light getLightSensor() {
		return this.lightSensor;
	}

}
