package robot.actionneurs;

import java.lang.Math;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;
import robot.taches.TachePrincipale;

/**
 * Cette classe permet de gérer les déplacements du robot. Elle contient les
 * constantes liées aux caractéristiques physiques du robot (comment est-il
 * monté?) ainsi que les constantes en rapport avec le mouvement (vitesses,
 * erreurs admises, gain des regulations, distances souhaitées par rapport aux
 * murs, etc ...).
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
     * Port auquel est connecté le moteur portant le sonar.
     */
    public static final NXTRegulatedMotor PORT_MOTEUR_SONAR = Motor.C ;

	/**
	 * Vitesse de croisière. Utilisé lorsque le robot avance. De 0 à 36 cm/s.
	 */
	public static final double VITESSE_CROISIERE = 16;
	
	/**
	 * Vitesse du robot lorsqu'il est en fin de mouvement. Utilisé lorsque le
	 * robot avance. De 0 à 36 cm/s.
	 */
	public static final double VITESSE_DANGER = 12;

	/**
	 * Vitesse de rotation de croisière. De 0 à 366 deg/s.
	 */
	public static final double VITESSE_ROTATION_CROISIERE = 140;
	
	/**
	 * Vitesse de rotation lorsqu'il est en fin de mouvement, c'est à dire
	 * lorsqu'il est proche de l'angle désiré (voir ANGLE_DANGER). Utilisé
	 * lorsque le robot tourne. De 0 à 366 deg/s.
	 */
	public static final double VITESSE_ROTATION_DANGER = 30;

	/**
	 * Vitesse de rotation du moteur qui guide le sonar. De 0 à 366 deg/s.
	 */
	public static final int VITESSE_ROTATION_SONAR = 270;

	/**
	 * Angle relatif à l'objectif à partir duquel on réduit la vitesse de
	 * rotation. Utilisé lorsque le robot tourne. En degrés.
	 */
	public static final double ANGLE_DANGER = 18;
	
	/**
	 * Angle relatif à l'objectif à partir duquel on considère que l'on a
	 * atteind l'angle désiré. Utilisé lorsque le robot tourne. En Degrés.
	 */
	public static final double ANGLE_ERREUR = 3;

	/**
	 * Hystérésis sur la régulation de la vitesse de rotation pour éviter les
	 * oscillations en vitesse qui perturbe la boussole. Utilisé lorsque le
	 * robot tourne. En degrés.
	 */
	public static final double ANGLE_HYSTERESIS = 1;
	
	/**
	 * Valeur maximale de l'erreur pour determiner si elle est valide ou si on
	 * ne la prend pas en compte. On sait que le cap de reférence est a peu prés
	 * correct, ainsi si on trouve une erreur trop grande c'est qu'il y a eu un
	 * probleme dans son calcul ou sa mesure, ou que le robot a trop oscillé par
	 * exemple. On considère alors que l'erreur a mal été calculée et on ne la
	 * prend pas en compte au dela de cette valeur. Utilisé lorsque le robot
	 * avance. En degrés.
	 */
	public static final double ERR_REF_ANGLE_MAX = 30;
	
	/**
	 * Valeur en dessus de laquelle on considère qu'il n'y a pas d'erreur.
	 * Utilisé lorsque le robot avance. En degrés.
	 */
	public static final double ERR_REF_ANGLE_MIN = 6 ;

	/**
	 * Distance a partir de laquelle le robot prend des mesures pour calculer
	 * l'erreur du cap. Utilisé lorsque le robot avance. En cm.
	 */
	public static final double DISTANCE_MESURE_ERRCAP = 12;
	
	/**
	 * Distance entre 2 cases en cm.
	 */
	public static final double DISTANCE_UNITAIRE = 31;
	
	/**
	 * Erreur concernant la position du robot lorsqu'il s'arrete au milieu d'une
	 * nouvelle case. Utilisé lorsque le robot avance (fin du mouvement). En cm.
	 */
	public static final double ERREUR_POSITION_CENTRE_CASE = 2;
	
	/**
	 * Lorsqu'on detecte la perte d'un mur sur un coté, on utilise cette donnée
	 * comme une info en plus, ainsi, le robot va parcourir ce nombre de cm
	 * aprés le mur perdu, pour s'arreter bien au milieu de la case. Utilisé
	 * lorsque le robot avance, en fin de mouvement s'il n'y a pas de mur devant
	 * et des murs manquant sur le coté.
	 */
	public static final double REG_DISTANCE_APRES_MUR = 10 ;
	
	/**
	 * Distance maximale parcouru où il est admissible de detecter la perte d'un
	 * mur lateral. En cm.
	 */
	public static final double REG_DISTANCE_PERTE_MUR_MAX = 28;
	
	/**
	 * Distance minimale parcouru où il est admissible de detecter la perte d'un
	 * mur lateral. En cm.
	 */
	public static final double REG_DISTANCE_PERTE_MUR_MIN = 14;

	/**
	 * Offset modélisant le fait que les sonars ne soient pas à la même distance
	 * du centre du robot. En effet, lorsque le robot est au centre du couloir,
	 * il faut avoir Distance_Mesurée_Gauche-Distance_Mesurée_Droit-OFFSET=0.
	 * Utilisé lorsque le robot avance. En cm.
	 */
	public static final double OFFSET_ENTRE_SONARS = 0;

	/**
	 * Distance à partir de laquelle le sonar rotatif va passer en position
	 * avant. Utilisé lorsque le robot avance (fin de mouvement). En cm.
	 */
	public static final double REGUL_DISTANCE_DANGER = 25;

	/**
	 * Limite en dessous de laquelle on considère qu'on est à proximité d'un mur
	 * avant. Utilisé lorsque le robot avance (en fin de mouvement). En cm.
	 */
	public static final double REGUL_DISTANCE_MUR_AVANT_DANGER = 22;
	
	/**
	 * Distance souhaitée par rapport au mur droit. Utilisé uniquement lorsqu'il
	 * n'y a qu'un mur droit de disponible pour réguler. Utilisé lorsque le
	 * robot avance. En cm.
	 */
	public static final double REGUL_DISTANCE_MUR_DROIT = 7.5;

	/**
	 * Distance souhaitée par rapport au mur gauche. Utilisée uniquement
	 * lorsqu'il n'y a que le mur gauche de disponible et que le sonar rotatif
	 * est en position gauche. Utilisé lorsque le robot avance. En cm.
	 */
	public static final double REGUL_DISTANCE_MUR_GAUCHE = 7.5;

	/**
	 * Distance, par rapport au mur devant, à laquelle le robot va s'arreter
	 * (s'il y a un mur devant). Utilisé lorsque le robot avance (en fin de
	 * mouvement). En cm.
	 */
	public static final double REGUL_DISTANCE_MUR_AVANT = 9.5;

	/**
	 * Gain de la régulation par retour d'état. Gain appliqué à l'erreur
	 * d'angle (angle mesuré - cap). Utilisé lorsque le robot avance.
	 */
	public static final double K_TETA = 0.0025;

	/**
	 * Gain de la régulation par retour d'état. Gain appliqué à l'erreur de
	 * distance (distance par rapport au centre du couloir). Utilisé lorsque le
	 * robot avance.
	 */
	public static final double K_DIST = 0.02;
	
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
	 * Angle de référence initial en degrés, celui-ci est mis à jour lors de
	 * l'execution de l'ordre correspondant. Sa valeur est comprise dans
	 * l'intervalle [0,360].
	 */
	private double refAngleInit;

	/**
	 * Angle de référence actuel en degrés, il est toujours de la forme
	 * initAngle + N*90° et est mis à jour lorsque le robot tourne. Sa valeur
	 * est comprise dans l'intervalle [0,360]. il sert de référence angulaire
	 * pour la régulation de tout les mouvements (cap).
	 */
	private double refAngleActuel;
	
	/**
	 * Erreur mesurée sur le cap. Cela permet detecter la deviation de la
	 * boussole.
	 */
	private double errRefAngle;
	
	/**
	 * Attribut définissant le mode de fonctionnement du robot. En fastMode, le
	 * robot ne mettra plus le sonar rotatif en position avant. Ce mode n'est a
	 * utiliser que lorsque le chemin a emprunter est connu, pour se déplacer
	 * plus vite.
	 */
	private boolean fastMode;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur de Mouvement.
	 */
	public Mouvement() {
		this.diffPilot = new DifferentialPilot(DIAMETRE_ROUE, DISTANCE_ENTRE_ROUE, PORT_MOTEUR_GAUCHE, PORT_MOTEUR_DROIT, false);
		this.moteurSonar = PORT_MOTEUR_SONAR;
		this.fastMode = false;
		this.errRefAngle=0;
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
	 * @param tPrincipale
	 */
	public void avancer(TachePrincipale tPrincipale) {
		double errAngle = 0;
		double errDist = 0;
		
		double commande = 0;
		double distance = 0;
		boolean termine = false;
		boolean murDevant = false ;
		
		boolean ancienMurDroit = tPrincipale.getEnv().getMurDroit();
		boolean ancienMurGauche = tPrincipale.getEnv().getMurGauche();
		double distPerteMur = 0 ;
		
		double nbMesureCap = 0;
		double sommeErrCap = 0;

		// Initialisation du mouvement
		this.diffPilot.reset();
		this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);	
		tPrincipale.getCapteurs().miseAJourComplete(tPrincipale);

		while (!termine) {
			
			// Récuperation des données des capteurs
			tPrincipale.getCapteurs().miseAJour(tPrincipale);
			errAngle = this.refAngleActuel - tPrincipale.getCapteurs().getBoussole().getMoyData();
			
			// Calcul de l'erreur angulaire
			if (errAngle < -180) {
				errAngle = errAngle + 360;
			} else if (errAngle > 180) {
				errAngle = errAngle - 360;
			}
			
			// Calcul de l'erreur position par rapport au milieu du couloir, selon la présence des murs
			if (tPrincipale.getEnv().getMurDroit() && tPrincipale.getEnv().getMurGauche() && !tPrincipale.getCapteurs().getSonarEstDevant()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData() - tPrincipale.getCapteurs().getSonarDroit().getMoyData() - OFFSET_ENTRE_SONARS);
			} else if (tPrincipale.getEnv().getMurDroit()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (REGUL_DISTANCE_MUR_DROIT - tPrincipale.getCapteurs().getSonarDroit().getMoyData());
			} else if (tPrincipale.getEnv().getMurGauche() && !tPrincipale.getCapteurs().getSonarEstDevant()) {
				errDist = Math.cos(Math.toRadians(errAngle)) * (tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData() - REGUL_DISTANCE_MUR_GAUCHE);
			} else {
				errDist = 0;
			}
			
			// Calcul de la commande par retour d'etat
			commande = -(K_DIST * errDist + K_TETA * errAngle);

			// Application de la commande au système
			if (commande != 0) { // Attention à la division par 0
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
			
			// Si on arrive sur une nouvelle case , il se peut qu'il n'y ait pas
			// de mur à droite ou/et à gauche. On detecte lorsqu'on perd la
			// présence d'un mur à gauche ou/et à droite pour avoir une info
			// complémentaire sur la position du robot.
			if( ((ancienMurDroit && !tPrincipale.getEnv().getMurDroit())
					|| (ancienMurGauche && !tPrincipale.getEnv().getMurGauche())) 
					&& distance > REG_DISTANCE_PERTE_MUR_MIN && distance < REG_DISTANCE_PERTE_MUR_MAX ) {
				distPerteMur = distance ;
			}
			ancienMurDroit = tPrincipale.getEnv().getMurDroit() ;
			ancienMurGauche = tPrincipale.getEnv().getMurGauche() ;

			// On fait la vérification du cap en cumulant l'erreur angulaire en
			// vu d'en faire une moyenne. pour eventuellement corriger le cap de
			// référence.
			if( distance > DISTANCE_MESURE_ERRCAP && distance < REGUL_DISTANCE_DANGER) {
				if(this.refAngleActuel>270) { // On fait attention à la discontinuité 360->0
					if(tPrincipale.getCapteurs().getBoussole().getMoyData()<90) {
						sommeErrCap += (tPrincipale.getCapteurs().getBoussole().getMoyData()+360-this.refAngleActuel);
					} else {
						sommeErrCap += tPrincipale.getCapteurs().getBoussole().getMoyData()-this.refAngleActuel;
					}
				} else if (this.refAngleActuel<90) {
					if(tPrincipale.getCapteurs().getBoussole().getMoyData()>270) {
						sommeErrCap += (tPrincipale.getCapteurs().getBoussole().getMoyData()-360-this.refAngleActuel);
					} else {
						sommeErrCap += tPrincipale.getCapteurs().getBoussole().getMoyData()-this.refAngleActuel;
					}
				} else {
					sommeErrCap += tPrincipale.getCapteurs().getBoussole().getMoyData()-this.refAngleActuel;
				}
				nbMesureCap++;
			}

			//Vérification sur l'avancement du mouvement
			if ( !this.fastMode ) {
				
				// Si le sonar est devant c'est qu'on est en fin de mouvement :
				if (tPrincipale.getCapteurs().getSonarEstDevant()) {
					
					// S'il y a un mur devant on va se caller à bonne distance de celui-ci
					if (tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData() < REGUL_DISTANCE_MUR_AVANT_DANGER || murDevant) {
						if(!murDevant) {
							murDevant=true;
						}						
						if ((tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData() < REGUL_DISTANCE_MUR_AVANT) || (distance > (DISTANCE_UNITAIRE + 2*ERREUR_POSITION_CENTRE_CASE))) {
							termine = true;
							this.tourner(tPrincipale,-errAngle-this.errRefAngle);
							tPrincipale.getCapteurs().tournerSonarAGauche(tPrincipale);
							this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);
						}
						
					// Sinon on s'arrete lorsqu'on se considère au centre de la nouvelle case
					} else {
						if( distPerteMur==0 ) {
							if (distance > DISTANCE_UNITAIRE) {
								termine = true;
								this.tourner(tPrincipale,-errAngle-this.errRefAngle);
								tPrincipale.getCapteurs().tournerSonarAGauche(tPrincipale);
								this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);
							}
						}
						else {
							if ( distance > (distPerteMur+REG_DISTANCE_APRES_MUR) ) {
								if (distance > DISTANCE_UNITAIRE-ERREUR_POSITION_CENTRE_CASE) {
									termine = true;
									this.tourner(tPrincipale,-errAngle-this.errRefAngle);
									tPrincipale.getCapteurs().tournerSonarAGauche(tPrincipale);
									this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);
								}
							}
						}
					}
				
				// Sinon si on approche de la fin, on place le sonar devant
				} else {
					
					if (distance > REGUL_DISTANCE_DANGER) {
						// Calcul de l'erreur de cap
						if (nbMesureCap!=0) {
								if ( Math.abs(sommeErrCap/nbMesureCap)<ERR_REF_ANGLE_MAX && Math.abs(sommeErrCap/nbMesureCap)>ERR_REF_ANGLE_MIN ) {
									this.errRefAngle = sommeErrCap/nbMesureCap;
								} else {
									this.errRefAngle = 0;
								}
						} else {
							this.errRefAngle = 0;
						}
						
						this.diffPilot.setTravelSpeed(VITESSE_DANGER);
						this.diffPilot.stop();
						distance += Math.cos(Math.toRadians(errAngle)) * this.diffPilot.getMovement().getDistanceTraveled();
						this.tourner(tPrincipale,-errAngle-this.errRefAngle);
						tPrincipale.getCapteurs().tournerSonarDevant(tPrincipale);						
					}
				}
			
			// Si on est en fastMode, on se contente de s'arreter lorsqu'on se considère au centre de la nouvelle case
			} else {
				if( distPerteMur==0 ) {
					if (distance > DISTANCE_UNITAIRE) {
						termine = true;
						this.tourner(tPrincipale,-errAngle);
						tPrincipale.getCapteurs().tournerSonarAGauche(tPrincipale);
						this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);
					}
				}
				else {
					if ( distance > (distPerteMur+REG_DISTANCE_APRES_MUR) ) {
						if (distance > DISTANCE_UNITAIRE-ERREUR_POSITION_CENTRE_CASE) {
							termine = true;
							this.tourner(tPrincipale,-errAngle);
							tPrincipale.getCapteurs().tournerSonarAGauche(tPrincipale);
							this.diffPilot.setTravelSpeed(VITESSE_CROISIERE);
						}
					}
				}
			}
		}
		
		// Mise à jour des données
		tPrincipale.getEnv().setMurArriere(false);
		tPrincipale.getCapteurs().miseAJourComplete(tPrincipale);
		tPrincipale.getEnv().majCoord(tPrincipale);
		
		// Creation de la case décrivant l'environnement de la nouvelle case
		if ( !this.fastMode ) {
			tPrincipale.getEnv().enregistrerCaseActuelle(tPrincipale);
		}
	}

	/**
	 * Fait tourner le robot à droite (d'environ 90°) en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulation angulaire basée sur un relais à
	 * seuil pur. Met à jour la direction du robot et les variables qui
	 * indiquent la présence eventuelle des 4 murs.
	 * @param tPrincipale
	 */
	public void tournerADroite(TachePrincipale tPrincipale) {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		tPrincipale.getEnv().majDirTourneADroite();

		// Mise a jour de l'angle de référence
		this.refAngleActuel = (this.refAngleActuel - 90) % 360;
		if (this.refAngleActuel < 0) {
			this.refAngleActuel = this.refAngleActuel + 360;
		}

		// Calcul de l'angle a réaliser
		err = this.refAngleActuel - tPrincipale.getCapteurs().getBoussole().getMoyData();
		if (err < -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.tourner(tPrincipale,-err-this.errRefAngle);

		// Mise à jour des données
		tPrincipale.getEnv().majMurTourneADroite();
		tPrincipale.getCapteurs().miseAJourComplete(tPrincipale);
	}

	/**
	 * Fait tourner le robot à gauche (d'environ 90°) en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulation angulaire basée sur un relais à
	 * seuil pur. Met à jour la direction du robot et les variables qui
	 * indiquent la présence eventuelle des 4 murs.
	 * @param tPrincipale
	 */
	public void tournerAGauche(TachePrincipale tPrincipale) {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		tPrincipale.getEnv().majDirTourneAGauche();

		// Mise a jour de l'angle de référence
		this.refAngleActuel = (this.refAngleActuel + 90) % 360;
		if (this.refAngleActuel < 0) {
			this.refAngleActuel = this.refAngleActuel + 360;
		}

		// Calcul de l'angle a réaliser
		err = this.refAngleActuel - tPrincipale.getCapteurs().getBoussole().getMoyData();
		if (err < -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.tourner(tPrincipale,-err-this.errRefAngle);

		// Mise à jour des données
		tPrincipale.getEnv().majMurTourneAGauche();
		tPrincipale.getCapteurs().miseAJourComplete(tPrincipale);
	}

	/**
	 * Fait faire un demi tour au robot (environ 180°) en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Met à jour
	 * la direction du robot et les variables qui indiquent la présence
	 * eventuelle des 4 murs.
	 * @param tPrincipale
	 */
	public void faireDemiTour(TachePrincipale tPrincipale) {
		double err;

		// Mise à jour de la variable qui indique la direction du robot dans le labyrinthe
		tPrincipale.getEnv().majDirDemiTour();

		// Mise a jour de l'angle de référence
		this.refAngleActuel = (this.refAngleActuel + 180) % 360;
		if (this.refAngleActuel < 0) {
			this.refAngleActuel = this.refAngleActuel + 360;
		}

		// Calcul de l'angle a réaliser
		err = this.refAngleActuel - tPrincipale.getCapteurs().getBoussole().getMoyData();
		if (err <= -180) {
			err = err + 360;
		} else if (err > 180) {
			err = err - 360;
		}

		this.tourner(tPrincipale,-err-this.errRefAngle);

		// Mise à jour des données
		tPrincipale.getEnv().majMurDemiTour();
		tPrincipale.getCapteurs().miseAJourComplete(tPrincipale);
	}

	/**
	 * Fait tourner le robot de l'angle passé en argument en régulant l'angle à
	 * l'aide de la boussole (en se basant sur l'angle de référence). Cette
	 * régulation est analogue à une régulation basée sur un relais à seuil pur.
	 * 
	 * @param tPrincipale
	 * @param angle
	 *            Angle à faire, en degrés, sens horaire.
	 */
	private int tourner(TachePrincipale tPrincipale, double angle) {
		double err;

		if(angle<=360 && angle>=-360) {
			
			// Calcul de l'angle absolu à atteindre
			double desiredAngle = (tPrincipale.getCapteurs().getBoussole().getMoyData() - angle) % 360;
			if (desiredAngle < 0) {
				desiredAngle = desiredAngle + 360;
			} else if (desiredAngle > 360) {
				desiredAngle = desiredAngle - 360;
			}

			// Calcul de l'erreur angulaire
			err = desiredAngle - tPrincipale.getCapteurs().getBoussole().getMoyData();
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
				tPrincipale.getCapteurs().miseAJour(tPrincipale);
				err = desiredAngle - tPrincipale.getCapteurs().getBoussole().getMoyData();
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
	 * Force l'arret des moteurs.
	 */
	public void stop() {
		this.diffPilot.stop();
	}
	
	// ------------------------------------- SETTERS ----------------------------------------------
	
	/**
	 * @param b
	 * 		état de l'attribut fastMode.
	 */
	public void setFastMode(boolean b) {
		this.fastMode = b;
	}

	/**
	 * Enregistre l'angle initial qui servira de référence pour la régulation
	 * des mouvements.
	 * 
	 * @param tPrincipale
	 */
	public void setRefAngleInit(TachePrincipale tPrincipale) {
		tPrincipale.getCapteurs().miseAJourComplete(tPrincipale);
		this.refAngleInit = tPrincipale.getCapteurs().getBoussole().getMoyData();
		this.refAngleActuel = this.refAngleInit;
		this.errRefAngle = 0;
	}
	
	// ------------------------------------- GETTERS ----------------------------------------------

	/**
	 * @return la valeur de l'attribut refAngle.
	 */
	public double getRefAngleActuel() {
		return this.refAngleActuel;
	}

	/**
	 * @return l'objet représentation le pilote différentiel.
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
	 * @return l'état de l'attribut fastMode.
	 */
	public boolean getFastMode() {
		return this.fastMode;
	}
}