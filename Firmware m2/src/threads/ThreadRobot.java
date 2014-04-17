package threads;

import env.*;
import robot.*;
import sensors.*;
import orders.*;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;


/**
 * Classe principale du code embarqu� sur le robot. Elle contient tout les
 * �l�ments qui permettent de repr�senter le robot.
 * 
 * @author Thomas
 */
public class ThreadRobot extends Thread {
	
	/**
	 * Diam�tre des roues du robot en cm
	 */
 	public static final double DIAMROUE = 5.6;
 	
 	/**
 	 * Distance inter-roues du robot en cm
 	 */
    public static final double DISTROUE = 11.5; 

	/**
	 * Attribut repr�sentant les mouvements du robot
	 * 
	 * @see Movement
	 * @see Robot#getMov()
	 */
	private Movement mov;

	/**
	 * Attribut repr�sentant l'environnement du robot
	 * 
	 * @see Environment
	 * @see Robot#getEnv()
	 */
	private Environment env;

	/**
	 * Attribut repr�sentant le moteur du sonar
	 * 
	 * @see NXTRegulatedMotor
	 * @see Robot#getSonarMotor
	 */
	private NXTRegulatedMotor sonarMotor;

	/**
	 * Attribut repr�sentant la boussole
	 * 
	 * @see Compass
	 * @see Robot#getCompass()
	 */
	private Compass compass;

	/**
	 * Attribut repr�sentant le sonar droit
	 * 
	 * @see Sonar
	 * @see Robot#getRightSonar
	 */
	private Sonar rightSonar;

	/**
	 * Attribut repr�sentant le sonar gauche
	 * 
	 * @see Sonar
	 * @see Robot#getLeftFrontSonar
	 */
	private Sonar leftFrontSonar;

	/**
	 * Attribut repr�sentant la gestion des ordres
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
		this.leftFrontSonar = new Sonar(SensorPort.S2);
		this.rightSonar = new Sonar(SensorPort.S1);
		this.order = new Order(this);
		this.setPriority(NORM_PRIORITY);
	}

	/**
	 * Tache principale du robot
	 */
	public void run() {
		while (true) {
			this.order.chooseExploration();
			this.order.execute();
			if (this.order.getReadyToGo()) {
					this.env.refreshCoord();
					this.env.saveCurrentCase(false);
				
				
			}
			//this.env.afficher();
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

}
