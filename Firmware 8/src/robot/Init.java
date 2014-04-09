package robot;

import orders.*;
import lejos.nxt.Sound;
import threads.*;
import tests.*;

public class Init {
	
	/**
	 * Volume sonore du robot. De 0 � 100
	 */
	public static final int VOLUME = 10; 

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
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//new Init();
		new Tests(8);
	}

	/**
	 * Initialise divers param�tre
	 * 
	 * @see Param#VOLUME
	 * @see Param#SPEED_CRUISE
	 * @see Param#RSPEED_CRUISE
	 * @see Param#RSPEED_SONARMOTOR
	 */
	private void init(ThreadRobot tRobot) {
		Sound.setVolume(VOLUME);
		tRobot.getMov().getDiffPilot().setTravelSpeed(Movement.SPEED_CRUISE);
		tRobot.getMov().getDiffPilot().setRotateSpeed(Movement.RSPEED_CRUISE);
		tRobot.getSonarMotor().setSpeed(Movement.RSPEED_SONARMOTOR);

		tRobot.getEnv().setInitValues(10, 10, 0); // A faire depuis la com
	}

	/**
	 * Fonction d'initialisation test
	 */
	public static void initTest(ThreadRobot tRobot) {
		Sound.setVolume(VOLUME);
		tRobot.getMov().getDiffPilot().setTravelSpeed(Movement.SPEED_CRUISE);
		tRobot.getMov().getDiffPilot().setRotateSpeed(Movement.RSPEED_CRUISE);
		tRobot.getSonarMotor().setSpeed(Movement.RSPEED_SONARMOTOR);

		tRobot.getEnv().setInitValues(10, 10, 0);
		
		tRobot.getOrder().add(Order.WAITBUTTON);
		tRobot.getOrder().chooseInsecurely();
		tRobot.getOrder().execute();
		tRobot.getOrder().add(Order.WAIT1SEC);
		tRobot.getOrder().chooseInsecurely();
		tRobot.getOrder().execute();
		tRobot.getOrder().add(Order.SETPOSITION);
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
