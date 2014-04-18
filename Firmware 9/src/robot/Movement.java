package robot;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;

import java.lang.Math;

import env.Environment;
import threads.ThreadRobot;

/**
 * Cette classe permet de gérer les déplacements du robot.
 * 
 * @author Thomas
 */
public class Movement {

	/**
	 * Vitesse de croisière. De 0 à 36 cm/s.
	 */
	public static final double SPEED_CRUISE = 16;

	/**
	 * Vitesse à l'approche d'un mur avant. De 0 à 36 cm/s.
	 */
	public static final double SPEED_DANGER = 12;

	/**
	 * Vitesse de rotation de croisière. De 0 à 366 deg/s
	 */
	public static final double RSPEED_CRUISE = 70;

	/**
	 * Vitesse de rotation lorsqu'on approche de l'angle voulu. De 0 à 366 deg/s.
	 */
	public static final double RSPEED_DANGER = 25;

	/**
	 * Vitesse de rotation lors de la calibration. Elle doit rester faible pour
	 * une calibration efficace 20 deg/s maximum.
	 */
	public static final double RSPEED_CAL = 20;

	/**
	 * Vitesse de rotation du moteur qui guide le sonar. De 0 à 366 deg/s.
	 */
	public static final int RSPEED_SONARMOTOR = 270;

	/**
	 * Angle relatif à l'objectif à partir duquel on réduit la vitesse de
	 * rotation. Utilisé lorsque le robot fait des virages. En degrés.
	 */
	public static final double ANGLE_DANGER = 18;

	/**
	 * Angle relatif à l'objectif à partir duquel on considère que l'on a
	 * atteind l'objectif en angle. Utilisé lorsque le robot fait des virages.
	 * En Degrés.
	 */
	public static final double ANGLE_ERR = 2;

	/**
	 * Hystérésis sur la régulation de la vitesse de rotation pour éviter les
	 * oscillations en vitesse qui perturbe la boussole. Utilisé lorsque le
	 * robot fait des virages. En degrés.
	 */
	public static final double ANGLE_HYST = 1;

	/**
	 * Distance entre 2 cases en cm
	 */
	public static final double UNIT_DIST = 30;

	/**
	 * Distance à partir de laquelle le sonar rotatif va passer en position
	 * avant. Utilisé lorsque le robot avance. En cm.
	 */
	public static final double REG_FRONT = 25;

	/**
	 * Offset modélisant le fait que les sonars ne soient pas à la même distance
	 * du centre du robot. Utilisé lorsque le robot avance. En cm.
	 */
	public static final double OFFSETMID = 0;
	
	/**
	 * Distance souhaitée par rapport au mur droit. Utilisé uniquement lorsqu'il
	 * n'y a qu'un mur droit de disponible pour réguler. Utilisé lorsque le
	 * robot avance. En cm.
	 */
	public static final double DISTRIGHTWALL = 11;

	/**
	 * Distance souhaitée par rapport au mur gauche. Utilisée uniquement
	 * lorsqu'il n'y a que le mur gauche de disponible et que le sonar rotatif
	 * est en position gauche. Utilisé lorsque le robot avance. En cm.
	 */
	public static final double DISTLEFTWALL = 8;

	/**
	 * Gain de la régulation par retour d'état. Gain appliqué à l'erreur
	 * d'angle. Utilisé lorsque le robot avance.
	 */
	public static final double K_TETA = 0.0030;
	
	/**
	 * Gain de la régulation par retour d'état. Gain appliqué à l'erreur de
	 * distance. Utilisé lorsque le robot avance.
	 */
	public static final double K_DIST = 0.015;

	/**
	 * Attribut représentant les 2 moteurs qui permettent de déplacer le robot.
	 * 
	 * @see DifferentialPilot
	 */
	private DifferentialPilot diffPilot;

	/**
	 * Attribut de référence vers le thread robot.
	 * 
	 * @see ThreadRobot
	 */
	private ThreadRobot tRobot;

	/**
	 * Attribut contenant l'angle de référence initial en degrés, celui-ci est
	 * mis à jour lors de l'execution de l'ordre correspondant. Sa valeur est
	 * comprise dans l'intervalle [0,360].
	 */
	private double initRefAngle;

	/**
	 * Attribut contenant l'angle de référence actuel en degrés, il est toujours
	 * de la forme initAngle + N*90° et est mis à jour lorsque le robot tourne.
	 * Sa valeur est comprise dans l'intervalle [0,360]. il sert de référence
	 * angulaire pour la régulation de tout les mouvements.
	 */
	private double refAngle;

	/**
	 * Attribut qui indique si l'angle initial de référence a été enregistré au
	 * moins une fois.
	 */
	private boolean refAngleDone;
	
	/**
	 * Attribut définissant le mode de fonctionnement du robot.
	 */
	private boolean fastMode;

	/**
	 * Constructeur de Movement
	 * 
	 * @param wheelDiameter
	 *            Diamètre des roues en cm
	 * @param trackWidth
	 *            Distance inter-roues en cm
	 * @param leftMotor
	 *            Objet représentant le moteur gauche
	 * @param rightMotor
	 *            Objet représentant le moteur droit
	 * @param reverse
	 *            Sens des moteurs
	 * @param bob
	 *            Objet représentant le robot
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
	 * cm). Le déplacement du robot est régulé de facon à rester au mieux au
	 * milieu du couloir. La régulation est basé sur un retour d'état sur les
	 * valeurs suivantes :</br> - Erreur d'angle : Différence entre l'angle de
	 * référence et l'angle du robot, à partir de la boussole</br> - Erreur de
	 * distance : Distance du robot par rapport au milieu du couloir, à partir
	 * des sonars et de la présence ou non de murs lateraux</br>
	 */
	public void moveForward() {
		double errAngle = 0;
		double errDist = 0;
		double commande = 0;
		double distTraveled = 0;
		boolean finish = false;

		this.diffPilot.reset();
		this.diffPilot.setTravelSpeed(SPEED_CRUISE);	

		// Mise à jour de l'environnement
		this.tRobot.getEnv().checkSensorsFull();

		while (!finish) {

			// Récuperation et calcul des sorties du système
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

			// Application de la commande au système
			if (commande != 0) {
				distTraveled += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
				this.diffPilot.arcForward(1 / commande);
			} else {
				distTraveled += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
				this.diffPilot.arcForward(10000000);
			}

			// Verification de la fin du mouvement
			if ( !this.fastMode ) {
				// Si on est en fin de mouvement (sonar tourné vers l'avant)
				if (this.tRobot.getEnv().getSonarIsFront()) {
					// S'il y a un mur devant on va se caller à bonne distance
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
		
		// Mise à jour de l'environnement
		this.tRobot.getEnv().setBackWallDetected(false);
		this.tRobot.getEnv().checkSensorsFull();
		this.tRobot.getEnv().refreshCoord();
		
		if ( !this.fastMode ) {
			this.tRobot.getEnv().saveCurrentCase();
		}
	}

	/**
	 * Fait tourner le robot à droite (d'environ 90°) en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulation angulaire basée sur un relais à
	 * seuil pur. Met à jour la direction du robot et les variables qui
	 * indiquent la présence eventuelle des 4 murs.
	 */
	public void turnRight() {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tRobot.getEnv().dirRight();

		// On change l'angle de référence
		this.refAngle = (this.refAngle - 90) % 360;
		if (this.refAngle < 0) {
			this.refAngle = this.refAngle + 360;
		}

		// On calcule l'angle à faire
		err = this.refAngle - this.tRobot.getCompass().getMoyData();
		if (err < -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.turn(-err);

		// Mise à jour de l'environnement
		this.tRobot.getEnv().wallRight();
		this.tRobot.getEnv().checkSensorsFull();
	}

	/**
	 * Fait tourner le robot à gauche (d'environ 90°) en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulation angulaire basée sur un relais à
	 * seuil pur. Met à jour la direction du robot et les variables qui
	 * indiquent la présence eventuelle des 4 murs.
	 */
	public void turnLeft() {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tRobot.getEnv().dirLeft();

		// On change l'angle de référence
		this.refAngle = (this.refAngle + 90) % 360;
		if (this.refAngle < 0) {
			this.refAngle = this.refAngle + 360;
		}

		// On calcule l'angle à faire
		err = this.refAngle - this.tRobot.getCompass().getMoyData();
		if (err < -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.turn(-err);

		// Mise à jour de l'environnement
		this.tRobot.getEnv().wallLeft();
		this.tRobot.getEnv().checkSensorsFull();
	}

	/**
	 * Fait faire un demi tour au (environ 180°) en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Met à jour la direction du robot et les variables qui
	 * indiquent la présence eventuelle des 4 murs.
	 */
	public void turnBack() {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tRobot.getEnv().dirBack();

		// On change l'angle de référence
		this.refAngle = (this.refAngle + 180) % 360;
		if (this.refAngle < 0) {
			this.refAngle = this.refAngle + 360;
		}

		// On calcule l'angle à faire
		err = this.refAngle - this.tRobot.getCompass().getMoyData();
		if (err <= -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.turn(-err);

		// Mise à jour de l'environnement
		this.tRobot.getEnv().wallBack();
		this.tRobot.getEnv().checkSensorsFull();
	}

	/**
	 * Fait tourner le robot de l'angle passé en argument en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulatio basée sur un relais à seuil pur.
	 * 
	 * @param angle
	 *            Angle à faire, en degrés, sens horaire. (entre -360 et +360)
	 */
	private int turn(double angle) {
		double err;

		if(angle<=360 && angle>=-360) {
			// Calcul de l'angle absolu à atteindre
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

				// Lorsqu'on se rapproche de l'angle désiré on réduit la vitesse de rotation
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
			
			// On arrete les moteurs pour arreter le rotate lorsqu'on à atteint l'angle
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
	 * Enregistre l'angle initial qui servira de référence pour la régulation
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
	 * @return l'objet représentation le pilote différentiel
	 */
	public DifferentialPilot getDiffPilot() {
		return this.diffPilot;
	}
	
	/**
	 * @param b
	 * 		état de l'attribut fastMode
	 */
	public void setFastMode(boolean b) {
		this.fastMode = b;
	}
	
	/**
	 * @return l'état de l'attribut fastMode
	 */
	public boolean getFastMode() {
		return this.fastMode;
	}
}