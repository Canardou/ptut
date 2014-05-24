package robot.actionneurs;

import java.lang.Math;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;
import robot.taches.*;

/**
 * Cette classe permet de gérer les déplacements du robot.
 * 
 * @author Thomas
 */
public class Mouvement {
	
	// ------------------------------------- CONSTANTES -------------------------------------------

	/**
	 * Diamètre des roues du robot en cm.
	 */
	private static final double DIAMETRE_ROUE = 5.6;
	
 	/**
 	 * Distance inter-roues du robot en cm.
 	 */
    public static final double DISTANCE_ENTRE_ROUE = 11.5; 
    
    /**
     * Port auquel est connecté le moteur gauche.
     */
    public static final RegulatedMotor PORT_MOTEUR_GAUCHE = Motor.A ;
    
    /**
     * Port auquel est connecté le moteur droit.
     */
    public static final RegulatedMotor PORT_MOTEUR_DROIT = Motor.B ;
    
    /**
     * Port auquel est connecté le moteur droit.
     */
    public static final NXTRegulatedMotor PORT_MOTEUR_SONAR = Motor.C ;
    
	/**
	 * Vitesse de croisière. De 0 à 36 cm/s.
	 */
	public static final double VITESSE_CROISIERE = 20;
	
	/**
	 * Vitesse à l'approche d'un mur avant. De 0 à 36 cm/s.
	 */
	public static final double VITESSE_DANGER = 12;

	/**
	 * Vitesse de rotation de croisière. De 0 à 366 deg/s
	 */
	public static final double VITESSE_ROTATION_CROISIERE = 160;
	
	/**
	 * Vitesse de rotation lorsqu'on approche de l'angle voulu. De 0 à 366 deg/s.
	 */
	public static final double VITESSE_ROTATION_DANGER = 30;

	/**
	 * Vitesse de rotation du moteur qui guide le sonar. De 0 à 366 deg/s.
	 */
	public static final int VITESSE_ROTATION_SONAR = 270;

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
	public static final double ANGLE_ERREUR = 3;

	/**
	 * Hystérésis sur la régulation de la vitesse de rotation pour éviter les
	 * oscillations en vitesse qui perturbe la boussole. Utilisé lorsque le
	 * robot fait des virages. En degrés.
	 */
	public static final double ANGLE_HYSTERESIS = 1;

	/**
	 * Distance entre 2 cases en cm.
	 */
	public static final double DISTANCE_UNITAIRE = 30;

	/**
	 * Offset modélisant le fait que les sonars ne soient pas à la même distance
	 * du centre du robot. Utilisé lorsque le robot avance. En cm.
	 */
	public static final double OFFSET_ENTRE_SONARS = 0;

	/**
	 * Distance à partir de laquelle le sonar rotatif va passer en position
	 * avant. Utilisé lorsque le robot avance. En cm.
	 */
	public static final double REGUL_DISTANCE_DANGER = 25;

	/**
	 * Limite en dessous de laquelle on considère qu'on est à proximité d'un mur
	 * avant. En cm.
	 */
	public static final double REGUL_DISTANCE_MUR_AVANT_DANGER = 22;
	
	/**
	 * Distance souhaitée par rapport au mur droit. Utilisé uniquement lorsqu'il
	 * n'y a qu'un mur droit de disponible pour réguler. Utilisé lorsque le
	 * robot avance. En cm.
	 */
	public static final double REGUL_DISTANCE_MUR_DROIT = 6.5;

	/**
	 * Distance souhaitée par rapport au mur gauche. Utilisée uniquement
	 * lorsqu'il n'y a que le mur gauche de disponible et que le sonar rotatif
	 * est en position gauche. Utilisé lorsque le robot avance. En cm.
	 */
	public static final double REGUL_DISTANCE_MUR_GAUCHE = 6.5;

	/**
	 * Distance d'arret par rapport au mur d'en face.
	 */
	public static final double REGUL_DISTANCE_MUR_AVANT = 9.5;

	/**
	 * Gain de la régulation par retour d'état. Gain appliqué à l'erreur
	 * d'angle. Utilisé lorsque le robot avance.
	 */
	public static final double K_TETA = 0.0025;
	
	/**
	 * Gain de la régulation par retour d'état. Gain appliqué à l'erreur de
	 * distance. Utilisé lorsque le robot avance.
	 */
	public static final double K_DIST = 0.015;
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------

	/**
	 * Les 2 moteurs qui permettent de déplacer le robot.
	 */
	private DifferentialPilot diffPilot;
	
	/**
	 * Le moteur sur lequel est fixé le sonar.
	 */	
	private NXTRegulatedMotor moteurSonar;

	/**
	 * Tache principale du robot.
	 */
	private TachePrincipale tPrincipale;

	/**
	 * Angle de référence initial en degrés, celui-ci est mis à jour lors de
	 * l'execution de l'ordre correspondant. Sa valeur est comprise dans
	 * l'intervalle [0,360].
	 */
	private double refAngleInit;

	/**
	 * Angle de référence actuel en degrés, il est toujours de la forme
	 * initAngle + N*90° et est mis à jour lorsque le robot tourne. Sa valeur
	 * est comprise dans l'intervalle [0,360]. il sert de référence angulaire
	 * pour la régulation de tout les mouvements.
	 */
	private double refAngleActuel;
	
	/**
	 * Attribut définissant le mode de fonctionnement du robot.
	 */
	private boolean fastMode;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur de Movement
	 * @param tPrincipaleInit
	 */
	public Mouvement(TachePrincipale tPrincipaleInit) {
		this.diffPilot = new DifferentialPilot(DIAMETRE_ROUE, DISTANCE_ENTRE_ROUE, PORT_MOTEUR_GAUCHE, PORT_MOTEUR_DROIT, false);
		this.moteurSonar = PORT_MOTEUR_SONAR;
		this.tPrincipale = tPrincipaleInit;
		this.fastMode = false;
	}
	
	// ------------------------------------- METHODES ---------------------------------------------

	/**
	 * Fait avancer le robot sur la case suivante. Le déplacement du robot est
	 * régulé de facon à rester au mieux au milieu du couloir. La régulation est
	 * basé sur un retour d'état sur les valeurs suivantes :</br> - Erreur
	 * d'angle : Différence entre l'angle de référence et l'angle du robot, à
	 * partir de la boussole.</br> - Erreur de distance : Distance du robot par
	 * rapport au milieu du couloir, à partir des sonars et de la présence ou
	 * non de murs lateraux.</br> A la fin du mouvement, une case décrivant
	 * l'environnement est créée.
	 */
	public void avancer() {
		double errAngle = 0;
		double errDist = 0;
		double commande = 0;
		double distance = 0;
		boolean termine = false;
		boolean murDevant = false ;

		// Initialisation du mouvement
		this.diffPilot.reset();
		this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);	
		this.tPrincipale.getCapteurs().miseAJourComplete();

		while (!termine) {

			// Récuperation et calcul des sorties du système
			this.tPrincipale.getCapteurs().miseAJour();
			errAngle = this.refAngleActuel - this.tPrincipale.getCapteurs().getBoussole().getMoyData();
			if (errAngle < -180) {
				errAngle = errAngle + 360;
			} else if (errAngle > 180) {
				errAngle = errAngle - 360;
			}
			if (this.tPrincipale.getEnv().getMurDroit() && this.tPrincipale.getEnv().getMurGauche() && !this.tPrincipale.getCapteurs().getSonarEstDevant()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (this.tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData() - this.tPrincipale.getCapteurs().getSonarDroit().getMoyData() - OFFSET_ENTRE_SONARS);
			} else if (this.tPrincipale.getEnv().getMurDroit()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (REGUL_DISTANCE_MUR_DROIT - this.tPrincipale.getCapteurs().getSonarDroit().getMoyData());
			} else if (this.tPrincipale.getEnv().getMurGauche() && !this.tPrincipale.getCapteurs().getSonarEstDevant()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (this.tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData() - REGUL_DISTANCE_MUR_GAUCHE);
			} else {
				errDist = 0;
			}
			
			// Calcul de la commande
			commande = -(K_DIST * errDist + K_TETA * errAngle);

			// Application de la commande au système
			if (commande != 0) {
				distance += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
				if(commande > 0) {
					this.diffPilot.arc(1 / commande,10,true);
				}
				else {
					this.diffPilot.arc(1 / commande,-10,true);
				}
			} else {
				distance += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
				this.diffPilot.arc(10000000,10,true);
			}

			//Vérification sur l'avancement du mouvement
			if ( !this.fastMode ) {
				
				if (this.tPrincipale.getCapteurs().getSonarEstDevant()) {
					
					// S'il y a un mur devant on va se caller à bonne distance
					if (this.tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData() < REGUL_DISTANCE_MUR_AVANT_DANGER || murDevant) {
						if(!murDevant) {
							murDevant=true;
						}						
						if ((this.tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData() < REGUL_DISTANCE_MUR_AVANT) || (distance > (DISTANCE_UNITAIRE + 3))) {
							termine = true;
							this.tourner(-errAngle);
							this.tPrincipale.getCapteurs().tournerSonarAGauche();
							this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);
							
						}
					// Sinon on s'arrete au bout de 30cm
					} else {
						if (distance > DISTANCE_UNITAIRE) {
							termine = true;
							this.tourner(-errAngle);
							this.tPrincipale.getCapteurs().tournerSonarAGauche();
							this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);
						}
					}
					
				} else {
					// Si on approche de la fin, on place le sonar devant
					if (distance > REGUL_DISTANCE_DANGER) {
						this.diffPilot.setTravelSpeed(VITESSE_DANGER);
						this.diffPilot.stop();
						distance += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
						System.out.println(errAngle);
						this.tourner(-errAngle);
						this.tPrincipale.getCapteurs().tournerSonarDevant();						
					}
				}
				
			} else {
				if (distance > DISTANCE_UNITAIRE ) {
					termine = true;
					this.tourner(-errAngle);
				}
			}
		}
		
		// Mise à jour de l'environnement
		this.tPrincipale.getEnv().setMurArriere(false);
		this.tPrincipale.getCapteurs().miseAJourComplete();
		this.tPrincipale.getEnv().majCoord();
		
		if ( !this.fastMode ) {
			this.tPrincipale.getEnv().enregistrerCaseActuelle();
		}
	}

	/**
	 * Fait tourner le robot à droite (d'environ 90°) en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulation angulaire basée sur un relais à
	 * seuil pur. Met à jour la direction du robot et les variables qui
	 * indiquent la présence eventuelle des 4 murs.
	 */
	public void tournerADroite() {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tPrincipale.getEnv().majDirTourneADroite();

		// On change l'angle de référence
		this.refAngleActuel = (this.refAngleActuel - 90) % 360;
		if (this.refAngleActuel < 0) {
			this.refAngleActuel = this.refAngleActuel + 360;
		}

		// On calcule l'angle à faire
		err = this.refAngleActuel - this.tPrincipale.getCapteurs().getBoussole().getMoyData();
		if (err < -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.tourner(-err);

		// Mise à jour de l'environnement
		this.tPrincipale.getEnv().majMurTourneADroite();
		this.tPrincipale.getCapteurs().miseAJourComplete();
	}

	/**
	 * Fait tourner le robot à gauche (d'environ 90°) en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulation angulaire basée sur un relais à
	 * seuil pur. Met à jour la direction du robot et les variables qui
	 * indiquent la présence eventuelle des 4 murs.
	 */
	public void tournerAGauche() {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tPrincipale.getEnv().majDirTourneAGauche();

		// On change l'angle de référence
		this.refAngleActuel = (this.refAngleActuel + 90) % 360;
		if (this.refAngleActuel < 0) {
			this.refAngleActuel = this.refAngleActuel + 360;
		}

		// On calcule l'angle à faire
		err = this.refAngleActuel - this.tPrincipale.getCapteurs().getBoussole().getMoyData();
		if (err < -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.tourner(-err);

		// Mise à jour de l'environnement
		this.tPrincipale.getEnv().majMurTourneAGauche();
		this.tPrincipale.getCapteurs().miseAJourComplete();
	}

	/**
	 * Fait faire un demi tour au (environ 180°) en régulant l'angle à l'aide de
	 * la boussole (en se basant sur l'angle de référence). Met à jour la
	 * direction du robot et les variables qui indiquent la présence eventuelle
	 * des 4 murs.
	 */
	public void faireDemiTour() {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		this.tPrincipale.getEnv().majDirDemiTour();

		// On change l'angle de référence
		this.refAngleActuel = (this.refAngleActuel + 180) % 360;
		if (this.refAngleActuel < 0) {
			this.refAngleActuel = this.refAngleActuel + 360;
		}

		// On calcule l'angle à faire
		err = this.refAngleActuel - this.tPrincipale.getCapteurs().getBoussole().getMoyData();
		if (err <= -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.tourner(-err);

		// Mise à jour de l'environnement
		this.tPrincipale.getEnv().majMurDemiTour();
		this.tPrincipale.getCapteurs().miseAJourComplete();
	}

	/**
	 * Fait tourner le robot de l'angle passé en argument en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulation basée sur un relais à seuil pur.
	 * 
	 * @param angle
	 *            Angle à faire, en degrés, sens horaire.
	 */
	private int tourner(double angle) {
		double err;

		if(angle<=360 && angle>=-360) {
			// Calcul de l'angle absolu à atteindre
			double desiredAngle = (this.tPrincipale.getCapteurs().getBoussole().getMoyData() - angle) % 360;
			if (desiredAngle < 0) {
				desiredAngle = desiredAngle + 360;
			} else if (desiredAngle > 360) {
				desiredAngle = desiredAngle - 360;
			}

			// Calcul de l'erreur angulaire
			err = desiredAngle - this.tPrincipale.getCapteurs().getBoussole().getMoyData();
			if (err < -180) {
				err = err + 360;
			} else if (err > 180) {
				err = err - 360;
			}
			
			// Tant qu'on n'a pas atteint l'angle
			while ( err <= -ANGLE_ERREUR || err >= ANGLE_ERREUR ) {

				if (err < 0) {
					this.diffPilot.rotate(Math.abs(angle), true);
				} else {
					this.diffPilot.rotate(-Math.abs(angle), true);
				}

				// Lorsqu'on se rapproche de l'angle désiré on réduit la vitesse de rotation
				if (err > -ANGLE_DANGER + ANGLE_HYSTERESIS
						&& err < ANGLE_DANGER - ANGLE_HYSTERESIS) {
					this.diffPilot.setRotateSpeed(VITESSE_ROTATION_DANGER);
				} else if (err < -ANGLE_DANGER - ANGLE_HYSTERESIS
						|| err > ANGLE_DANGER + ANGLE_HYSTERESIS) {
					this.diffPilot.setRotateSpeed(VITESSE_ROTATION_CROISIERE);
				}

				// Calcul de l'erreur angulaire
				this.tPrincipale.getCapteurs().getBoussole().rafraichir();
				err = desiredAngle - this.tPrincipale.getCapteurs().getBoussole().getMoyData();
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
	
	// ------------------------------------- SETTERS ----------------------------------------------
	
	/**
	 * @param b
	 * 		état de l'attribut fastMode
	 */
	public void setFastMode(boolean b) {
		this.fastMode = b;
	}

	/**
	 * Enregistre l'angle initial qui servira de référence pour la régulation
	 * des mouvements.
	 */
	public void setRefAngleInit() {
		this.tPrincipale.getCapteurs().miseAJourComplete();
		this.refAngleInit = this.tPrincipale.getCapteurs().getBoussole().getMoyData();
		this.refAngleActuel = this.refAngleInit;
	}
	
	// ------------------------------------- GETTERS ----------------------------------------------

	/**
	 * @return la valeur de l'attribut refAngle
	 */
	public double getRefAngleActuel() {
		return this.refAngleActuel;
	}

	/**
	 * @return l'objet représentation le pilote différentiel
	 */
	public DifferentialPilot getDiffPilot() {
		return this.diffPilot;
	}
	
	/**
	 * @return la valeur de l'attribut sonarMotor.
	 */
	public NXTRegulatedMotor getMoteurSonar() {
		return this.moteurSonar;
	}

	/**
	 * @return l'état de l'attribut fastMode
	 */
	public boolean getFastMode() {
		return this.fastMode;
	}
}