package robot;

import lejos.nxt.Sound;
import tests.Tests;
import threads.*;

/**
 * Classe principale du robot, Elle initie les paramètres de démarrage, objets
 * necessaires, créée et démarre les tâches
 * 
 * @author Thomas
 */
public class Init {
	
	/**
	 * Volume sonore du robot. De 0 à 100
	 */
	public static final int VOLUME = 0; 

	/**
	 * Constructeur de Init
	 */
	public Init() {
		ThreadRobot tRobot = new ThreadRobot();
		ThreadCom tCom = new ThreadCom(tRobot);
		this.initTest(tRobot);
		tRobot.start();
		tCom.start();
	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		//new Init();
		new Tests(12);
	}

	/**
	 * Initialise divers paramètre
	 */
	public static void init(ThreadRobot tRobot) {
		Sound.setVolume(VOLUME);
		tRobot.getMov().getDiffPilot().setTravelSpeed(Movement.SPEED_CRUISE);
		tRobot.getMov().getDiffPilot().setRotateSpeed(Movement.RSPEED_CRUISE);
		tRobot.getSonarMotor().setSpeed(Movement.RSPEED_SONARMOTOR);

		tRobot.getEnv().setPosition(10, 10, 0); // A faire depuis la com
	}

	/**
	 * Fonction d'initialisation test
	 */
	public static void initTest(ThreadRobot tRobot) {
		Sound.setVolume(VOLUME);
		tRobot.getMov().getDiffPilot().setTravelSpeed(Movement.SPEED_CRUISE);
		tRobot.getMov().getDiffPilot().setRotateSpeed(Movement.RSPEED_CRUISE);
		tRobot.getSonarMotor().setSpeed(Movement.RSPEED_SONARMOTOR);

		tRobot.getEnv().setPosition(10, 10, 0);
		
		tRobot.getOrder().add(Order.WAITBUTTON);
		tRobot.getOrder().chooseInsecurely();
		tRobot.getOrder().execute();
		tRobot.getOrder().add(Order.WAIT1SEC);
		tRobot.getOrder().chooseInsecurely();
		tRobot.getOrder().execute();
		tRobot.getOrder().add(Order.SAVEREFANGLE);
		tRobot.getOrder().chooseInsecurely();
		tRobot.getOrder().execute();
		tRobot.getOrder().add(Order.CHECKFIRSTCASE);
		tRobot.getOrder().chooseInsecurely();
		tRobot.getOrder().execute();
	}

}
