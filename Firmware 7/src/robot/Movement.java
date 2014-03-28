package robot;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;

import java.lang.Math;

public class Movement {

	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	private 	DifferentialPilot 	diffPilot;
	private		Robot 				robot ;
	private 	int 				reg ;
	private 	boolean				frontWallReg;
	private		double 				distTraveled ;
	private 	double	 			initAngle;          // Angle initial du robot
	private 	double    			refAngle;           // Angle de référence ( = initAngle + N*90° )
	private 	boolean				finish;

	/**********************************************/
	/**                                          **/
	/**               CONSTRUCTEUR               **/
	/**                                          **/
	/**********************************************/	
	public Movement(double wheelDiameter, double trackWidth, RegulatedMotor leftMotor, RegulatedMotor rightMotor, boolean reverse, Robot bob) {
		this.diffPilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
		this.diffPilot.setRotateSpeed(Param.RSPEED_CRUISE);
		this.diffPilot.setTravelSpeed(Param.SPEED_CRUISE);
		this.robot = bob ;
		this.frontWallReg = false;
	}
	 
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/
	
	public DifferentialPilot getDiffPilot() {
		return this.diffPilot ;
	}
	
	/** Le robot avance de 30cm **/
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
	
	/** Fonction de fin de mouvement (en regardant devant)**/
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
	
	/** Fonction qui choisit le type de régulation à appliquer selon l'environnement du robot **/
	private void chooseReg(double errAngle, double errDist) {
		
		// Si on a un mur de chaque coté
		if ( this.robot.getEnv().getRightWallDetected() && this.robot.getEnv().getLeftWallDetected() ) {
			
			// Trop à droite ou on tourne dangeureusement à droite
			if ( errDist > Param.REG_ERRDIST || errAngle > Param.REG_ERRANGLEMAX ) {
				this.reg = Param.RIGHTREG ;
			}
			// Un peu à droite et on a tendance à partir plus vers la droite
			else if ( errDist <= Param.REG_ERRDIST && errDist > 0
					&& errAngle > Param.REG_ERR ) {
				this.reg = Param.MIDRIGHTREG ;
			}
			// Trop à gauche ou on tourne dangeureusement à gauche
			else if ( errDist < -Param.REG_ERRDIST || errAngle < -Param.REG_ERRANGLEMAX ) {
				this.reg = Param.LEFTREG ;
			}
			// Un peu à gauche et on a tendance à partir plus vers la gauche
			else if ( errDist >= -Param.REG_ERRDIST && errDist < 0
					&& errAngle < -Param.REG_ERR ) {
				this.reg = Param.MIDLEFTREG ;
			}
			else {
				this.reg = Param.NOREG ;
			}
		}
		
		// Sinon si on a que le mur droit
		else if ( this.robot.getEnv().getRightWallDetected() ) {
			
			// Trop à droite ou on tourne dangeureusement à droite
			if( (this.robot.getRightSonar().getMoyData() < Param.RIGHTWALL_MIN) 
					|| (errAngle > Param.REG_ERRANGLEMAX) ) {
				this.reg = Param.RIGHTREG ;
			}
			// Un peu à droite et on a tendance à partir plus vers la droite
			else if ( (this.robot.getRightSonar().getMoyData() < ((Param.RIGHTWALL_MAX+Param.RIGHTWALL_MIN)/2 - Param.RIGHTWALL_MID) )
					&& (errAngle > Param.REG_ERR) ) {
				this.reg = Param.MIDRIGHTREG ;
			}
			// Trop à gauche ou on tourne dangeureusement à gauche
			else if( this.robot.getRightSonar().getMoyData() > Param.RIGHTWALL_MAX 
					|| errAngle < -Param.REG_ERRANGLEMAX ) {
				this.reg = Param.LEFTREG ;
			}
			// Un peu à gauche et on a tendance à partir plus vers la gauche
			else if ( (this.robot.getRightSonar().getMoyData() > ((Param.RIGHTWALL_MAX+Param.RIGHTWALL_MIN)/2 + Param.RIGHTWALL_MID) )
					&& (errAngle < Param.REG_ERR) ) {
				this.reg = Param.MIDLEFTREG ;
			}
		}			
		
		// Sinon si on a que le mur gauche		
		else if ( this.robot.getEnv().getLeftWallDetected() ) {
			
			// Trop à gauche ou on tourne dangeureusement à gauche
			if( (this.robot.getLeftFrontSonar().getMoyData() < Param.RIGHTWALL_MIN) 
					|| (errAngle < -Param.REG_ERRANGLEMAX) ) {
				this.reg = Param.LEFTREG ;
			}
			// Un peu à gauche et on a tendance à partir plus vers la gauche
			else if ( (this.robot.getLeftFrontSonar().getMoyData() < ((Param.RIGHTWALL_MAX+Param.RIGHTWALL_MIN)/2 - Param.RIGHTWALL_MID) )
					&& (errAngle < Param.REG_ERR) ) {
				this.reg = Param.MIDLEFTREG ;
			}
			// Trop à droite ou on tourne dangeureusement à droite
			if( (this.robot.getLeftFrontSonar().getMoyData() > Param.RIGHTWALL_MAX) 
					|| (errAngle > Param.REG_ERRANGLEMAX) ) {
				this.reg = Param.RIGHTREG ;
			}
			// Un peu à droite et on a tendance à partir plus vers la droite
			else if ( (this.robot.getLeftFrontSonar().getMoyData() > ((Param.RIGHTWALL_MAX+Param.RIGHTWALL_MIN)/2 - Param.RIGHTWALL_MID) )
					&& (errAngle > Param.REG_ERR) ) {
				this.reg = Param.MIDRIGHTREG ;
			}
		}
		
		// Sinon si on a aucun mur à coté
		else {
			this.reg = Param.NOREG;
		}
	}
	
	/** Execution de la regulation demandée **/
	private void executeReg(double errAngle) {
	    if ( this.reg == Param.RIGHTREG  ){
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(-Param.RIGHTWALL_ARC1);
	    }

	    else if ( this.reg == Param.LEFTREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(Param.RIGHTWALL_ARC1);
	    }

	    else if ( this.reg == Param.MIDRIGHTREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(-Param.RIGHTWALL_ARC2);
	    }
	    
	    else if ( this.reg == Param.MIDLEFTREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(Param.RIGHTWALL_ARC2);
	    }
	    
	    else if( this.reg == Param.NOREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.forward();
	    }
	    else if( this.reg == Param.ROTREG ) {
	    	this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.turn(-errAngle);
			this.diffPilot.forward();
	    }
	}	

	/** Fait tourner le robot à droite (environ 90°) **/
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
	
	/** Fait tourner le robot à gauche (environ 90°)**/
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
	
	/** Fait tourner le robot de l'angle voulu ("angle" sens horaire) **/
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

	/** Fait faire un demi-tour au robot (environ 180°) **/
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

	/** Fait reculer le robot **/
	public void moveBackward() {
		//this.diffPilot.backward();
	}

	/** Dude, stahp. **/
	public void stop() {
		this.diffPilot.stop();
	}
	
	/** Permet de se mettre à bonne distance du mur avant **/
	public void joinWall() {
		 this.getDiffPilot().setTravelSpeed(Param.SPEED_DANGER);
		 while(this.robot.getLeftFrontSonar().getMoyData()>Param.LIMFRONTWALL) {
			 this.getDiffPilot().forward();
			 this.robot.getLeftFrontSonar().refresh();
		 }
		 this.getDiffPilot().setTravelSpeed(Param.SPEED_CRUISE);
		 this.stop();
	}

	/** Enregistre l'angle initial du robot **/
	public void saveInitAngle() {
		this.initAngle =  this.robot.getCompass().getMoyData();
		this.refAngle  =  this.initAngle;	
	}
	
}