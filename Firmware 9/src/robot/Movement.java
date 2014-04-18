package robot;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;

import java.lang.Math;

import env.Environment;
import threads.ThreadRobot;

/**
 * Cette classe permet de g�rer les d�placements du robot.
 * 
 * @author Thomas
 */
public class Movement {

	/**
	 * Vitesse de croisi�re. De 0 � 36 cm/s.
	 */
	public static final double SPEED_CRUISE = 16;

	/**
	 * Vitesse � l'approche d'un mur avant. De 0 � 36 cm/s.
	 */
	public static final double SPEED_DANGER = 12;

	/**
	 * Vitesse de rotation de croisi�re. De 0 � 366 deg/s
	 */
	public static final double RSPEED_CRUISE = 70;

	/**
	 * Vitesse de rotation lorsqu'on approche de l'angle voulu. De 0 � 366 deg/s.
	 */
	public static final double RSPEED_DANGER = 25;

	/**
	 * Vitesse de rotation lors de la calibration. Elle doit rester faible pour
	 * une calibration efficace 20 deg/s maximum.
	 */
	public static final double RSPEED_CAL = 20;

	/**
	 * Vitesse de rotation du moteur qui guide le sonar. De 0 � 366 deg/s.
	 */
	public static final int RSPEED_SONARMOTOR = 270;

	/**
	 * Angle relatif � l'objectif � partir duquel on r�duit la vitesse de
	 * rotation. Utilis� lorsque le robot fait des virages. En degr�s.
	 */
	public static final double ANGLE_DANGER = 18;

	/**
	 * Angle relatif � l'objectif � partir duquel on consid�re que l'on a
	 * atteind l'objectif en angle. Utilis� lorsque le robot fait des virages.
	 * En Degr�s.
	 */
	public static final double ANGLE_ERR = 2;

	/**
	 * Hyst�r�sis sur la r�gulation de la vitesse de rotation pour �viter les
	 * oscillations en vitesse qui perturbe la boussole. Utilis� lorsque le
	 * robot fait des virages. En degr�s.
	 */
	public static final double ANGLE_HYST = 1;

	/**
	 * Distance entre 2 cases en cm
	 */
	public static final double UNIT_DIST = 30;

	/**
	 * Distance � partir de laquelle le sonar rotatif va passer en position
	 * avant. Utilis� lorsque le robot avance. En cm.
	 */
	public static final double REG_FRONT = 25;

	/**
	 * Offset mod�lisant le fait que les sonars ne soient pas � la m�me distance
	 * du centre du robot. Utilis� lorsque le robot avance. En cm.
	 */
	public static final double OFFSETMID = 0;
	
	/**
	 * Distance souhait�e par rapport au mur droit. Utilis� uniquement lorsqu'il
	 * n'y a qu'un mur droit de disponible pour r�guler. Utilis� lorsque le
	 * robot avance. En cm.
	 */
	public static final double DISTRIGHTWALL = 11;

	/**
	 * Distance souhait�e par rapport au mur gauche. Utilis�e uniquement
	 * lorsqu'il n'y a que le mur gauche de disponible et que le sonar rotatif
	 * est en position gauche. Utilis� lorsque le robot avance. En cm.
	 */
	public static final double DISTLEFTWALL = 8;

	/**
	 * Gain de la r�gulation par retour d'�tat. Gain appliqu� � l'erreur
	 * d'angle. Utilis� lorsque le robot avance.
	 */
	public static final double K_TETA = 0.0030;
	
	/**
	 * Gain de la r�gulation par retour d'�tat. Gain appliqu� � l'erreur de
	 * distance. Utilis� lorsque le robot avance.
	 */
	public static final double K_DIST = 0.015;

	/**
	 * Attribut repr�sentant les 2 moteurs qui permettent de d�placer le robot.
	 * 
	 * @see DifferentialPilot
	 */
	private DifferentialPilot diffPilot;

	/**
	 * Attribut de r�f�rence vers le thread robot.
	 * 
	 * @see ThreadRobot
	 */
	private ThreadRobot tRobot;

	/**
	 * Attribut contenant l'angle de r�f�rence initial en degr�s, celui-ci est
	 * mis � jour lors de l'execution de l'ordre correspondant. Sa valeur est
	 * comprise dans l'intervalle [0,360].
	 */
	private double initRefAngle;

	/**
	 * Attribut contenant l'angle de r�f�rence actuel en degr�s, il est toujours
	 * de la forme initAngle + N*90� et est mis � jour lorsque le robot tourne.
	 * Sa valeur est comprise dans l'intervalle [0,360]. il sert de r�f�rence
	 * angulaire pour la r�gulation de tout les mouvements.
	 */
	private double refAngle;

	/**
	 * Attribut qui indique si l'angle initial de r�f�rence a �t� enregistr� au
	 * moins une fois.
	 */
	private boolean refAngleDone;
	
	/**
	 * Attribut d�finissant le mode de fonctionnement du robot.
	 */
	private boolean fastMode;

	/**
	 * Constructeur de Movement
	 * 
	 * @param wheelDiameter
	 *            Diam�tre des roues en cm
	 * @param trackWidth
	 *            Distance inter-roues en cm
	 * @param leftMotor
	 *            Objet repr�sentant le moteur gauche
	 * @param rightMotor
	 *            Objet repr�sentant le moteur droit
	 * @param reverse
	 *            Sens des moteurs
	 * @param bob
	 *            Objet repr�sentant le robot
	 * @see RegulatedMotor
	 * @see DifferentialPilot
	 * @see ThreadRobot
	 */
	public Movement(double wheelDiameter, double trackWidth, RegulatedMotor leftMotor, RegulatedMotor rightMotor, boolean reverse, ThreadRobot bob) {
		this.diffPilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
		this.tRobot = bob;
		this.refAngleDone = false;
		this.fastMode = false;
	}

	/**
	 * Fait avancer le robot sur la case suivante (avancer d'environ UNIT_DIST
	 * cm). Le d�placement du robot est r�gul� de facon � rester au mieux au
	 * milieu du couloir. La r�gulation est bas� sur un retour d'�tat sur les
	 * valeurs suivantes :</br> - Erreur d'angle : Diff�rence entre l'angle de
	 * r�f�rence et l'angle du robot, � partir de la boussole</br> - Erreur de
	 * distance : Distance du robot par rapport au milieu du couloir, � partir
	 * des sonars et de la pr�sence ou non de murs lateraux</br>
	 */
	public void moveForward() {
		double errAngle = 0;
		double errDist = 0;
		double commande = 0;
		double distTraveled = 0;
		boolean finish = false;

		this.diffPilot.reset();
		this.diffPilot.setTravelSpeed(SPEED_CRUISE);	

		// Mise � jour de l'environnement
		this.tRobot.getEnv().checkSensorsFull();

		while (!finish) {

			// R�cuperation et calcul des sorties du syst�me
			this.tRobot.getEnv().checkSensors();
			
			errAngle = this.refAngle - this.tRobot.getCompass().getMoyData();
			if (errAngle < -180) {
				errAngle = errAngle + 360;
			} else if (errAngle > 180) {
				errAngle = errAngle - 360;
			}
			if (this.tRobot.getEnv().getRightWallDetected() && this.tRobot.getEnv().getLeftWallDetected() && !this.tRobot.getEnv().getSonarIsFront()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (this.tRobot.getLeftFrontSonar().getMoyData() - this.tRobot.getRightSonar().getMoyData() - OFFSETMID);
			} else if (this.tRobot.getEnv().getRightWallDetected()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (DISTRIGHTWALL - this.tRobot.getRightSonar().getMoyData());
			} else if (this.tRobot.getEnv().getLeftWallDetected() && !this.tRobot.getEnv().getSonarIsFront()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (this.tRobot.getLeftFrontSonar().getMoyData() - DISTLEFTWALL);
			} else {
				errDist = 0;
			}
			
			// Calcul de la commande
			commande = -(K_DIST * errDist + K_TETA * errAngle);

			// Application de la commande au syst�me
			if (commande != 0) {
				distTraveled += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
				this.diffPilot.arcForward(1 / commande);
			} else {
				distTraveled += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
				this.diffPilot.arcForward(10000000);
			}

			// Verification de la fin du mouvement
			if ( !this.fastMode ) {
				// Si on est en fin de mouvement (sonar tourn� vers l'avant)
				if (this.tRobot.getEnv().getSonarIsFront()) {
					// S'il y a un mur devant on va se caller � bonne distance
					if (this.tRobot.getLeftFrontSonar().getMoyData() < Environment.FRONTWALL_DANGER) {
						if (this.tRobot.getLeftFrontSonar().getMoyData() < Environment.LIMFRONTWALL) {
							finish = true;
							this.stop();
							this.turn(-errAngle);
							this.tRobot.getEnv().sonarLeft();
							this.diffPilot.setTravelSpeed(SPEED_CRUISE);		
						}
					// Sinon on s'arrete au bout de 30cm
					} else {
						if (distTraveled > UNIT_DIST) {
							finish = true;
							this.stop();
							this.turn(-errAngle);
							this.tRobot.getEnv().sonarLeft();
							this.diffPilot.setTravelSpeed(SPEED_CRUISE);
						}
					}
				} else {
					// Si on arrive en fin de mouvement
					if (distTraveled > REG_FRONT) {
						distTraveled += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
						this.stop();
						this.turn(-errAngle);
						this.tRobot.getEnv().sonarFront();
						this.diffPilot.setTravelSpeed(SPEED_DANGER);
					}
				}
			} else {
				if (distTraveled > UNIT_DIST ) {
					finish = true;
					this.stop();
					this.turn(-errAngle);
				}
			}
		}
		
		this.stop();
		
		// Mise � jour de l'environnement
		this.tRobot.getEnv().setBackWallDetected(false);
		this.tRobot.getEnv().checkSensorsFull();
		this.tRobot.getEnv().refreshCoord();
		
		if ( !this.fastMode ) {
			this.tRobot.getEnv().saveCurrentCase();
		}
	}

	/**
	 * Fait tourner le robot � droite (d'environ 90�) en r�gulant l'angle �
	 * l'aide de la boussole (en se basant sur l'angle de r�f�rence). Cette
	 * r�gulation est analogue � une r�gulation angulaire bas�e sur un relais �
	 * seuil pur. Met � jour la direction du robot et les variables qui
	 * indiquent la pr�sence eventuelle des 4 murs.
	 */
	public void turnRight() {
		double err;

		// Mise � jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tRobot.getEnv().dirRight();

		// On change l'angle de r�f�rence
		this.refAngle = (this.refAngle - 90) % 360;
		if (this.refAngle < 0) {
			this.refAngle = this.refAngle + 360;
		}

		// On calcule l'angle � faire
		err = this.refAngle - this.tRobot.getCompass().getMoyData();
		if (err < -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.turn(-err);

		// Mise � jour de l'environnement
		this.tRobot.getEnv().wallRight();
		this.tRobot.getEnv().checkSensorsFull();
	}

	/**
	 * Fait tourner le robot � gauche (d'environ 90�) en r�gulant l'angle �
	 * l'aide de la boussole (en se basant sur l'angle de r�f�rence). Cette
	 * r�gulation est analogue � une r�gulation angulaire bas�e sur un relais �
	 * seuil pur. Met � jour la direction du robot et les variables qui
	 * indiquent la pr�sence eventuelle des 4 murs.
	 */
	public void turnLeft() {
		double err;

		// Mise � jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tRobot.getEnv().dirLeft();

		// On change l'angle de r�f�rence
		this.refAngle = (this.refAngle + 90) % 360;
		if (this.refAngle < 0) {
			this.refAngle = this.refAngle + 360;
		}

		// On calcule l'angle � faire
		err = this.refAngle - this.tRobot.getCompass().getMoyData();
		if (err < -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.turn(-err);

		// Mise � jour de l'environnement
		this.tRobot.getEnv().wallLeft();
		this.tRobot.getEnv().checkSensorsFull();
	}

	/**
	 * Fait faire un demi tour au (environ 180�) en r�gulant l'angle �
	 * l'aide de la boussole (en se basant sur l'angle de r�f�rence). Met � jour la direction du robot et les variables qui
	 * indiquent la pr�sence eventuelle des 4 murs.
	 */
	public void turnBack() {
		double err;

		// Mise � jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tRobot.getEnv().dirBack();

		// On change l'angle de r�f�rence
		this.refAngle = (this.refAngle + 180) % 360;
		if (this.refAngle < 0) {
			this.refAngle = this.refAngle + 360;
		}

		// On calcule l'angle � faire
		err = this.refAngle - this.tRobot.getCompass().getMoyData();
		if (err <= -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.turn(-err);

		// Mise � jour de l'environnement
		this.tRobot.getEnv().wallBack();
		this.tRobot.getEnv().checkSensorsFull();
	}

	/**
	 * Fait tourner le robot de l'angle pass� en argument en r�gulant l'angle �
	 * l'aide de la boussole (en se basant sur l'angle de r�f�rence). Cette
	 * r�gulation est analogue � une r�gulatio bas�e sur un relais � seuil pur.
	 * 
	 * @param angle
	 *            Angle � faire, en degr�s, sens horaire. (entre -360 et +360)
	 */
	private int turn(double angle) {
		double err;

		if(angle<=360 && angle>=-360) {
			// Calcul de l'angle absolu � atteindre
			double desiredAngle = (this.tRobot.getCompass().getMoyData() - angle) % 360;
			if (desiredAngle < 0) {
				desiredAngle = desiredAngle + 360;
			} else if (desiredAngle > 360) {
				desiredAngle = desiredAngle - 360;
			}

			// Calcul de l'erreur angulaire
			err = desiredAngle - this.tRobot.getCompass().getMoyData();
			if (err < -180) {
				err = err + 360;
			} else if (err > 180) {
				err = err - 360;
			}
			
			// Tant qu'on n'a pas atteint l'angle
			while ( err <= -ANGLE_ERR || err >= ANGLE_ERR ) {

				if (err < 0) {
					this.diffPilot.rotate(Math.abs(angle), true);
				} else {
					this.diffPilot.rotate(-Math.abs(angle), true);
				}

				// Lorsqu'on se rapproche de l'angle d�sir� on r�duit la vitesse de rotation
				if (err > -ANGLE_DANGER + ANGLE_HYST
						&& err < ANGLE_DANGER - ANGLE_HYST) {
					this.diffPilot.setRotateSpeed(RSPEED_DANGER);
				} else if (err < -ANGLE_DANGER - ANGLE_HYST
						|| err > ANGLE_DANGER + ANGLE_HYST) {
					this.diffPilot.setRotateSpeed(RSPEED_CRUISE);
				}

				// Calcul de l'erreur angulaire
				this.tRobot.getCompass().refresh();
				err = desiredAngle - this.tRobot.getCompass().getMoyData();
				if (err < -180) {
					err = err + 360;
				} else if (err > 180) {
					err = err - 360;
				}
			}
			
			// On arrete les moteurs pour arreter le rotate lorsqu'on � atteint l'angle
			this.stop();

			return 0;
		}
		else {
			System.out.println("turn:err param");
			return 1;
		}
	}

	/**
	 * Force l'arret des moteurs
	 */
	public void stop() {
		this.diffPilot.stop();
	}

	/**
	 * Enregistre l'angle initial qui servira de r�f�rence pour la r�gulation
	 * des mouvements.
	 */
	public void saveRefAngle() {
		this.tRobot.getEnv().checkSensorsFull();
		this.initRefAngle = this.tRobot.getCompass().getMoyData();
		this.refAngle = this.initRefAngle;
		this.refAngleDone = true;
	}

	/**
	 * @return la valeur de l'attribut refAngleDone
	 */
	public boolean getRefAngleDone() {
		return this.refAngleDone;
	}

	/**
	 * @return la valeur de l'attribut refAngle
	 */
	public double getRefAngle() {
		return this.refAngle;
	}

	/**
	 * @return l'objet repr�sentation le pilote diff�rentiel
	 */
	public DifferentialPilot getDiffPilot() {
		return this.diffPilot;
	}
	
	/**
	 * @param b
	 * 		�tat de l'attribut fastMode
	 */
	public void setFastMode(boolean b) {
		this.fastMode = b;
	}
	
	/**
	 * @return l'�tat de l'attribut fastMode
	 */
	public boolean getFastMode() {
		return this.fastMode;
	}
}