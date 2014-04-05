package robot;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;

import java.lang.Math;

/**
 * Cette classe permet de gérer les déplacements du robot
 * @author Thomas
 */
public class Movement {

	/**
	 * Attribut représentant les 2 moteurs qui permettent de déplacer le robot
	 * @see DifferentialPilot
	 */
	private DifferentialPilot diffPilot;
	
	/**
	 * Attribut contenant l'objet robot
	 * @see Robot
	 */
	private Robot robot ;
	
	/**
	 * Attribut contenant le type de régulation necessaire pour la méthode moveForward()
	 * @see Movement#chooseReg(double, double)
	 * @see Movement#moveForward()
	 * @see Param#LEFTREG
	 * @see Param#MIDLEFTREG
	 * @see Param#NOREG
	 * @see Param#RIGHTREG
	 * @see Param#MIDRIGHTREG
	 */
	private int reg ;
	
	/**
	 * Attribut indiquant que le sonar gauche/avant est en position avant
	 */
	private boolean	frontWallReg;
	
	/**
	 * Attribut contenant la distance parcourue utilisé dans la méthode moveForward()
	 * @see DifferentialPilot#getMovement()
	 * @see Movement#moveForward()
	 */
	private	double distTraveled ;
	
	/**
	 * Attribut contenant l'angle de référence initial en degrés, celui-ci est mis à jour
	 * lors de l'execution de l'ordre correspondant. Sa valeur est comprise dans
	 * l'intervalle [0,360]
	 * @see Param#SAVEREFANGLE
	 * @see Movement#saveRefAngle()
	 */
	private double initAngle;   
	
	/**
	 * Attribut contenant l'angle de référence actuel en degrés, il est toujours de la forme
	 * initAngle + N*90° et est mis à jour lorsque le robot tourne. Sa valeur est comprise 
	 * dans l'intervalle [0,360]. il sert de référence angulaire pour la régulation des mouvements
	 * @see Movement#initAngle
	 */
	private double refAngle; 
	
	/**
	 * Attribut qui indique si l'angle initial de référence a été enregistré au moins une fois
	 */
	private boolean refAngleDone;
	
	/**
	 * Attribut utilisé dans la méthode moveForward() pour savoir quand arreter le mouvement
	 * @see Movement#moveForward()
	 */
	private boolean	finish;

	/**
	 * Constructeur de Movement
	 * @param wheelDiameter
	 * 		Diamètre des roues en cm
	 * @param trackWidth
	 * 		Distance inter-roues en cm
	 * @param leftMotor
	 * 		Objet représentant le moteur gauche
	 * @param rightMotor
	 * 		Objet représentant le moteur droit
	 * @param reverse
	 * 		Sens des moteurs
	 * @param bob
	 * 		Objet représentant le robot
	 * @see RegulatedMotor
	 * @see DifferentialPilot
	 * @see Robot
	 */
	public Movement(double wheelDiameter, double trackWidth, RegulatedMotor leftMotor, RegulatedMotor rightMotor, boolean reverse, Robot bob) {
		this.diffPilot 		= new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
		this.robot 			= bob ;
		this.refAngleDone	= false;
	}

	/**
	 * Fait avancer le robot sur la case suivante (avancer d'environ UNIT_DIST cm). Le déplacement du robot est régulé
	 * de facon à rester au mieux au milieu du couloir, il est constitué de 2 phases. Dans un premier temps, le robot 
	 * va avancer et reguler sa position en utilisant la boussole ainsi que ses 2 sonars, avec les murs latéraux eventuels. 
	 * Lorsque que le robot a presque atteint l'objectif (aprés avoir parcouru REG_CHECK cm). Le sonar rotatif va se 
	 * tourner vers l'avant pour vérifier s'il y a un mur. Si aucun mur n'est présent à l'avant, le robot va finir 
	 * d'avancer pour completer la distance désirée (UNIT_DIST). Si un mur est detecté, le robot va avancer jusqu'a se 
	 * placer a la bonne distance de celui ci (LIMFRONTWALL). La régulation se fait selon différents modes. Si l'on considère
	 * que l'on doit réguler fortement la position, le robot va avancer en faisant un arc de rayon ARC2 cm. Si l'on considère 
	 * que l'on doit réguler légèrement, le robot avancera en faisant un arc de rayon ARC1 cm. Sinon, le robot va simplement 
	 * tout droit. La méthode met à jour les variables indiquant la présence eventuelle des 4 murs.
	 * @see Movement#endOfMove(double)
	 * @see Movement#reg
	 * @see Movement#distTraveled
	 * @see Movement#refAngle
	 * @see Movement#frontWallReg
	 * @see Movement#finish
	 * @see Param#UNIT_DIST
	 * @see Param#LIMFRONTWALL
	 * @see Param#FRONTWALL_DANGER
	 * @see Param#ARC2
	 * @see Param#ARC1
	 * @see Param#REG_ERRDIST
	 * @see Param#REG_ERRANGLEMAX
	 * @see Param#REG_ERRANGLE
	 * @see Param#RIGHTWALL_MAX
	 * @see Param#LEFTWALL_MAX
	 * @see Param#RIGHTWALL_MIN
	 * @see Param#LEFTWALL_MIN
	 * @see Param#REG_CHECK
	 */
	public void moveForward() {
		double errAngle	  = 0 ;
		double errDist	  = 0 ;
		this.distTraveled = 0 ;
		this.reg		  = Param.NOREG ;
		this.frontWallReg = false;
		this.finish    	  = false;
		
		this.diffPilot.setTravelSpeed(Param.SPEED_CRUISE);
		
		// On remplit les filtres
		this.robot.getEnv().check2WallsCompassFull();
		
		this.diffPilot.reset();
		
		while (!this.finish) {
			
			// Calcul des erreurs pour la régulation (distance aux murs et angle)
			errDist	  = this.robot.getLeftFrontSonar().getMoyData() - this.robot.getRightSonar().getMoyData() + 1 ;
			errAngle  = this.refAngle - this.robot.getCompass().getMoyData();
			if(errAngle<-180) {
				errAngle = errAngle+360;
			}
			else if (errAngle>180) {
				errAngle = errAngle-360;
			}
			
			if ( this.distTraveled > Param.REG_CHECK ) {
				this.endOfMove(errAngle);			
			}
			else {			
				this.chooseReg(errAngle,errDist);
				this.executeReg(errAngle);
				this.robot.getEnv().check2WallsCompass();
			}			
		}
		
		this.turn(-errAngle);
		
		if(this.frontWallReg) {
			this.robot.getSonarMotor().rotateTo(0);
		}
		
		// Mise a jour de l'environnement
		this.robot.getEnv().setBackWallDetected(false);
		this.robot.getEnv().check2WallsCompassFull();
	}
	
	/**
	 * Gère la fin du déplacement réalisé par la méthode moveForward()
	 * @param errAngle
	 * 		Différence entre l'angle de référence et l'angle du robot
	 * @see Movement#moveForward()
	 */
	private void endOfMove(double errAngle) {
		if(!this.frontWallReg) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.stop();
			this.turn(-errAngle);
			this.frontWallReg = true;
			this.robot.getSonarMotor().rotateTo(-90);
			this.robot.getEnv().checkFrontWallFull();
			this.diffPilot.setTravelSpeed(Param.SPEED_DANGER);
		} 
		else {
			this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.forward();	
			this.robot.getEnv().checkFrontWallCompass();
			
			if(this.robot.getLeftFrontSonar().getMoyData()<=Param.LIMFRONTWALL
					|| this.robot.getLeftFrontSonar().getMoyData()>Param.FRONTWALL_DANGER && this.distTraveled > Param.UNIT_DIST) {
				this.finish=true;
				this.stop();
			}
		}
	}
	
	/**
	 * Choisit le type de régulation necessaire en fonction de l'erreur d'angle, de l'erreur de distance.
	 * et de la présence ou non de mur latéraux. Met à jour l'attribut reg
	 * @param errAngle
	 * 		Différence entre l'angle de référence et l'angle du robot
	 * @param errDist
	 * 		Différence entre la distance au mur gauche et la distance au mur droit
	 * @see Movement#reg
	 */
	private void chooseReg(double errAngle, double errDist) {

		//Si on a un mur de chaque coté
		if ( this.robot.getEnv().getRightWallDetected() && this.robot.getEnv().getLeftWallDetected() ) {

			//Si on est trop à droite
			if ( errDist > Param.REG_ERRDIST ) {
				//Si le robot n'est pas déjà orienté vers le centre du couloir
				if( errAngle > -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				//Sinon on le laisse continuer vers le centre
				else {
					this.reg = Param.NOREG ;				
				}
			}
			//Sinon si on est légèrement à droite
			else if ( errDist > 0 ) {
				// Si le robot est trop orienté vers la droite
				if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				// Sinon si le robot est trop orienté vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.MIDLEFTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la droite
				else if ( errAngle > Param.REG_ERRANGLE ) {
					this.reg = Param.MIDRIGHTREG ;
				}
				else {
					this.reg = Param.NOREG ;
				}
			}			
			//Sinon si on est trop à gauche
			else if ( errDist < -Param.REG_ERRDIST ) {
				//Si le robot n'est pas déjà orienté vers le centre du couloir
				if( errAngle < Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				//Sinon on le laisse continuer vers le centre
				else {
					this.reg = Param.NOREG ;				
				}
			}
			//Sinon si on est légèrement à gauche
			else if ( errDist < 0 ) {
				// Si le robot est trop orienté vers la gauche
				if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				// Sinon si le robot est trop orienté vers la droite
				else if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.MIDRIGHTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLE ) {
					this.reg = Param.MIDLEFTREG ;
				}
				else {
					this.reg = Param.NOREG ;
				}				
			}
			//Sinon si on est au centre du couloir (d'un point de vu distance)
			else {
				// Si le robot est trop orienté vers la gauche
				if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				// Sinon si le robot est trop orienté vers la droite
				else if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLE ) {
					this.reg = Param.MIDLEFTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la droite
				else if ( errAngle > Param.REG_ERRANGLE ) {
					this.reg = Param.MIDRIGHTREG ;
				}		
				else {
					this.reg = Param.NOREG ;
				}
			}
		}
		
		// Sinon si on a que le mur droit
		else if ( this.robot.getEnv().getRightWallDetected() ) {
			
			//Si on est trop à droite
			if ( this.robot.getRightSonar().getMoyData() < Param.RIGHTWALL_MIN ) {
				//Si le robot n'est pas déjà orienté vers le centre du couloir
				if( errAngle > -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				//Sinon on le laisse continuer vers le centre
				else {
					this.reg = Param.NOREG ;				
				}
			}			
			//Sinon si on est légèrement à droite
			else if ( this.robot.getRightSonar().getMoyData() < (Param.RIGHTWALL_MIN+Param.RIGHTWALL_MAX)/2 ) {
				// Si le robot est trop orienté vers la droite
				if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				// Sinon si le robot est trop orienté vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.MIDLEFTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la droite
				else if ( errAngle > Param.REG_ERRANGLE ) {
					this.reg = Param.MIDRIGHTREG ;
				}
				else {
					this.reg = Param.NOREG ;
				}
			}			
			//Sinon si on est trop à gauche
			else if ( this.robot.getRightSonar().getMoyData() > Param.RIGHTWALL_MAX ) {
				//Si le robot n'est pas déjà orienté vers le centre du couloir
				if( errAngle < Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				//Sinon on le laisse continuer vers le centre
				else {
					this.reg = Param.NOREG ;				
				}
			}
			//Sinon si on est légèrement à gauche
			else if ( this.robot.getRightSonar().getMoyData() > (Param.RIGHTWALL_MIN+Param.RIGHTWALL_MAX)/2 ) {
				// Si le robot est trop orienté vers la gauche
				if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				// Sinon si le robot est trop orienté vers la droite
				else if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.MIDRIGHTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLE ) {
					this.reg = Param.MIDLEFTREG ;
				}
				else {
					this.reg = Param.NOREG ;
				}			
			}
			//Sinon si on est au centre du couloir (d'un point de vu distance)
			else {
				// Si le robot est trop orienté vers la gauche
				if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				// Sinon si le robot est trop orienté vers la droite
				else if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLE ) {
					this.reg = Param.MIDLEFTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la droite
				else if ( errAngle > Param.REG_ERRANGLE ) {
					this.reg = Param.MIDRIGHTREG ;
				}		
				else {
					this.reg = Param.NOREG ;
				}
			}
			
		}
		
		// Sinon si on a que le mur gauche		
		else if ( this.robot.getEnv().getLeftWallDetected() ) {
			
			//Si on est trop à droite
			if ( this.robot.getLeftFrontSonar().getMoyData() < Param.LEFTWALL_MIN ) {
				//Si le robot n'est pas déjà orienté vers le centre du couloir
				if( errAngle > -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				//Sinon on le laisse continuer vers le centre
				else {
					this.reg = Param.NOREG ;				
				}
			}			
			//Sinon si on est légèrement à droite
			else if ( this.robot.getLeftFrontSonar().getMoyData() < (Param.LEFTWALL_MIN+Param.LEFTWALL_MAX)/2 ) {
				// Si le robot est trop orienté vers la droite
				if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				// Sinon si le robot est trop orienté vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.MIDLEFTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la droite
				else if ( errAngle > Param.REG_ERRANGLE ) {
					this.reg = Param.MIDRIGHTREG ;
				}
				else {
					this.reg = Param.NOREG ;
				}
			}			
			//Sinon si on est trop à gauche
			else if ( this.robot.getLeftFrontSonar().getMoyData() > Param.LEFTWALL_MAX ) {
				//Si le robot n'est pas déjà orienté vers le centre du couloir
				if( errAngle < Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				//Sinon on le laisse continuer vers le centre
				else {
					this.reg = Param.NOREG ;				
				}
			}
			//Sinon si on est légèrement à gauche
			else if ( this.robot.getLeftFrontSonar().getMoyData() > (Param.LEFTWALL_MIN+Param.LEFTWALL_MAX)/2 ) {
				// Si le robot est trop orienté vers la gauche
				if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				// Sinon si le robot est trop orienté vers la droite
				else if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.MIDRIGHTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLE ) {
					this.reg = Param.MIDLEFTREG ;
				}
				else {
					this.reg = Param.NOREG ;
				}			
			}
			//Sinon si on est au centre du couloir (d'un point de vu distance)
			else {
				// Si le robot est trop orienté vers la gauche
				if ( errAngle < -Param.REG_ERRANGLEMAX ) {
					this.reg = Param.LEFTREG ;
				}
				// Sinon si le robot est trop orienté vers la droite
				else if ( errAngle > Param.REG_ERRANGLEMAX ) {
					this.reg = Param.RIGHTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la gauche
				else if ( errAngle < -Param.REG_ERRANGLE ) {
					this.reg = Param.MIDLEFTREG ;
				}
				// Sinon si le robot est orienté légèrement vers la droite
				else if ( errAngle > Param.REG_ERRANGLE ) {
					this.reg = Param.MIDRIGHTREG ;
				}		
				else {
					this.reg = Param.NOREG ;
				}
			}	
			
		}
		// Sinon si on a aucun mur à coté
		else {
			this.reg = Param.NOREG ;
		}
	}
	
	/**
	 * Execute la régulation demandé par l'intermediaire de l'attribut reg
	 * @param errAngle
	 * 		Différence entre l'angle de référence et l'angle du robot
	 * @see Movement#moveForward()
	 * @see Movement#reg
	 */
	private void executeReg(double errAngle) {
	    if ( this.reg == Param.RIGHTREG  ){
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(-Param.ARC1);
	    }

	    else if ( this.reg == Param.LEFTREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(Param.ARC1);
	    }

	    else if ( this.reg == Param.MIDRIGHTREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(-Param.ARC2);
	    }
	    
	    else if ( this.reg == Param.MIDLEFTREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(Param.ARC2);
	    }
	    
	    else if( this.reg == Param.NOREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.forward();
	    }
	}	

	/**
	 * Fait tourner le robot à droite (d'environ 90°) en régulant l'angle à l'aide de la boussole (en se basant sur
	 * l'angle de référence). Met à jour la direction du robot et les variables qui indiquent la présence eventuelle 
	 * des 4 murs
	 * @see env#Environment
	 * @see Movement#turn(double)
	 */
	public void turnRight() {
		double err ;
		
		// On met a jour la variable qui indique la direction du robot dans le labyrinthe
		this.robot.getEnv().dirRight();
	
		// On change l'angle de référence
	    this.refAngle = (this.refAngle - 90)%360 ;
		if(this.refAngle<0) {
			this.refAngle = this.refAngle + 360 ;
		}
		
		// On calcule l'angle à faire
		err = this.refAngle - this.robot.getCompass().getMoyData();
		if(err<-180) {
			err = err+360;
		}
		else if (err>180) {
			err = err-360;
		}
		
		this.turn(-err);
		
		// Mise a jour de l'environnement
		this.robot.getEnv().wallRight();
		this.robot.getEnv().check2WallsCompassFull();
	}
	
	/**
	 * Fait tourner le robot à gauche (d'environ 90°) en régulant l'angle à l'aide de la boussole (en se basant sur
	 * l'angle de référence). Met à jour la direction du robot et les variables qui indiquent la présence eventuelle 
	 * des 4 murs
	 * @see env#Environment
	 * @see Movement#turn(double)
	 */
	public void turnLeft() {
		double err ;
		
		// On met a jour la variable qui indique la direction du robot dans le labyrinthe
		this.robot.getEnv().dirLeft();

	    this.refAngle = (this.refAngle + 90)%360 ;
		if(this.refAngle<0) {
			this.refAngle = this.refAngle + 360 ;
		}
		
		err = this.refAngle - this.robot.getCompass().getMoyData();
		if(err<-180) {
			err = err+360;
		}
		else if (err>180) {
			err = err-360;
		}
		
		this.turn(-err);
		
		// Mise a jour de l'environnement
		this.robot.getEnv().wallLeft();
		this.robot.getEnv().check2WallsCompassFull();
	}

	/**
	 * Fait tourner le robot de l'angle passé en argument en régulant l'angle à l'aide de la boussole (en se basant
	 * sur l'angle de référence).
	 * @param angle
	 * 		Angle à faire, en degrés, sens horaire.
	 * @see Param#ANGLE_DANGER
	 * @see Param#ANGLE_ERR
	 * @see Param#ANGLE_HYST
	 */
	private void turn(double angle) {
		double err ;	
		
		double desiredAngle = (this.robot.getCompass().getMoyData() - angle) % 360;	
		if(desiredAngle<0) {
			desiredAngle=desiredAngle+360;
		}
		else if(desiredAngle>360) {
			desiredAngle=desiredAngle-360;
		}
		
		err = desiredAngle - this.robot.getCompass().getMoyData();
		if(err<-180) {
			err = err+360;
		}
		else if (err>180) {
			err = err-360;
		}
		
		// Tant qu'on n'a pas atteind l'angle
		while ( err <= -Param.ANGLE_ERR || err >= Param.ANGLE_ERR ) {
			
			if(err<0) {
				this.diffPilot.rotate(Math.abs(angle), true);
			}
			else {
				this.diffPilot.rotate(-Math.abs(angle), true);
			}
			
			// Lorsqu'on se rapproche de l'angle désiré on réduit la vitesse de rotation
			if ( err > -Param.ANGLE_DANGER+Param.ANGLE_HYST && err < Param.ANGLE_DANGER-Param.ANGLE_HYST ) {
				this.diffPilot.setRotateSpeed(Param.RSPEED_DANGER);
			}
			else if ( err < -Param.ANGLE_DANGER-Param.ANGLE_HYST || err > Param.ANGLE_DANGER+Param.ANGLE_HYST ) {
				this.diffPilot.setRotateSpeed(Param.RSPEED_CRUISE);
			}

			this.robot.getCompass().refresh();
			
			err = desiredAngle - this.robot.getCompass().getMoyData();
			if(err<-180) {
				err = err+360;
			}
			else if (err>180) {
				err = err-360;
			}
		}
		
		//On arrete les moteurs pour arreter le rotate lorsqu'on à atteint l'angle
		this.stop();
	}

	/**
	 * Fait faire un demi-tour au robot (d'environ 180°) en régulant l'angle à l'aide de la boussole (en se basant sur
	 * l'angle de référence). Met à jour la direction du robot et les variables qui indiquent la présence eventuelle 
	 * des 4 murs
	 * @see env#Environment
	 * @see Movement#turn(double)
	 */
	public void turnBack() {
		double err ;
		
		this.robot.getEnv().dirBack();

		
	    this.refAngle = (this.refAngle + 180)%360 ;
		if(this.refAngle<0) {
			this.refAngle = this.refAngle + 360 ;
		}
		
		err = this.refAngle - this.robot.getCompass().getMoyData();
		if(err<=-180) {
			err = err+360;
		}
		else if (err>180) {
			err = err-360;
		}
		this.turn(-err);
		
		// Mise a jour de l'environnement
		this.robot.getEnv().wallBack();
		this.robot.getEnv().check2WallsCompassFull();
	}

	/**
	 * Force l'arret des moteurs
	 */
	public void stop() {
		this.diffPilot.stop();
	}
	
	/**
	 * Enregistre l'angle initial qui servira de référence pour la régulation des mouvements.
	 * Met à jour l'attribut refAngleDone
	 * @see Movement#initAngle
	 * @see Movement#refAngle
	 * @see Movement#refAngleDone
	 */
	public void saveRefAngle() {
		System.out.println("Sauv. de l'angle de ref");
		this.initAngle =  this.robot.getCompass().getMoyData();
		this.refAngle  =  this.initAngle;	
		this.refAngleDone=true;
	}
	
	/**
	 * @return la valeur de l'attribut refAngleDone
	 * @see Movement#refAngleDone
	 */
	public boolean getRefAngleDone() {
		return this.refAngleDone;
	}
	
	/**
	 * @return l'objet représentation le pilote différentiel
	 * @see Movement#diffPilot
	 * @see DifferentialPilot
	 */
	public DifferentialPilot getDiffPilot() {
		return this.diffPilot ;
	}
}