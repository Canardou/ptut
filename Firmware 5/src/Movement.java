import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;

import java.lang.Math;

public class Movement {

	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	protected 	DifferentialPilot 	diffPilot;
	private		Robot 				robot ;
	protected 	int 				reg ;
	private		double 				distTraveled ;
	protected   boolean 			frontWallReg ;

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
	}
	 
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/
	// Fait avancer le robot de 30cm (sauf si mur detecté à l'avant) en faisant en sorte que le robot soit bien
	// au milieu du couloir
	public void moveForward() {

		this.frontWallReg = false ;
		boolean frontWallChecked = false;
		double errAngle	  = 0 ;
		double errDist	  = 0 ;
		this.distTraveled = 0 ;
		this.reg		  = Param.NOREG ;	
		this.robot.checkLeftRightWalls();	
		errAngle = this.calcErr(errAngle);
		
		// tant qu'il n'y a pas de mur devant ou qu'on n'a pas fait 30cm
		while ( this.distTraveled < Param.UNIT_DIST && !this.frontWallReg ) {
			
			errDist	  = this.robot.leftFrontSonar.moyData - this.robot.rightSonar.moyData +1 ;
			errAngle  = this.calcErr(errAngle);
			
			if ( this.distTraveled > Param.REG_CHECK && !frontWallChecked ) {
				frontWallChecked = true ;
		    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
				this.stop();
				this.turn(-errAngle);
				this.robot.checkFullEnv();	
				if ( this.robot.frontWallDetected ) {
					this.frontWallReg = true ;
				}
			}
			else {			
				this.chooseReg(errAngle,errDist);
				this.executeReg(errAngle);
			}

			// Capter l'environnement du robot
			this.robot.checkLeftRightWalls();
		}
		this.stop();
		this.turn(-errAngle);
	}
	
	// Fonctions de régulation
	public void chooseReg(double errAngle, double errDist) {
		
		// Si on a un mur de chaque coté
		if ( this.robot.rightWallDetected && this.robot.leftWallDetected ) {
			
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
		else if ( this.robot.rightWallDetected ) {
			
			// Trop à droite ou on tourne dangeureusement à droite
			if( (this.robot.rightSonar.moyData < Param.RIGHTWALL_MIN) 
					|| (errAngle > Param.REG_ERRANGLEMAX) ) {
				this.reg = Param.RIGHTREG ;
			}
			// Un peu à droite et on a tendance à partir plus vers la droite
			else if ( (this.robot.rightSonar.moyData < ((Param.RIGHTWALL_MAX+Param.RIGHTWALL_MIN)/2 - Param.RIGHTWALL_MID) )
					&& (errAngle > Param.REG_ERR) ) {
				this.reg = Param.MIDRIGHTREG ;
			}
			// Trop à gauche ou on tourne dangeureusement à gauche
			else if( this.robot.rightSonar.moyData > Param.RIGHTWALL_MAX 
					|| errAngle < -Param.REG_ERRANGLEMAX ) {
				this.reg = Param.LEFTREG ;
			}
			// Un peu à gauche et on a tendance à partir plus vers la gauche
			else if ( (this.robot.rightSonar.moyData > ((Param.RIGHTWALL_MAX+Param.RIGHTWALL_MIN)/2 + Param.RIGHTWALL_MID) )
					&& (errAngle < Param.REG_ERR) ) {
				this.reg = Param.MIDLEFTREG ;
			}
		}			
		
		// Sinon si on a que le mur gauche		
		else if ( this.robot.leftWallDetected ) {
			
			// Trop à gauche ou on tourne dangeureusement à gauche
			if( (this.robot.leftFrontSonar.moyData < Param.RIGHTWALL_MIN) 
					|| (errAngle < -Param.REG_ERRANGLEMAX) ) {
				this.reg = Param.LEFTREG ;
			}
			// Un peu à gauche et on a tendance à partir plus vers la gauche
			else if ( (this.robot.leftFrontSonar.moyData < ((Param.RIGHTWALL_MAX+Param.RIGHTWALL_MIN)/2 - Param.RIGHTWALL_MID) )
					&& (errAngle < Param.REG_ERR) ) {
				this.reg = Param.MIDLEFTREG ;
			}
			// Trop à droite ou on tourne dangeureusement à droite
			if( (this.robot.leftFrontSonar.moyData > Param.RIGHTWALL_MAX) 
					|| (errAngle > Param.REG_ERRANGLEMAX) ) {
				this.reg = Param.RIGHTREG ;
			}
			// Un peu à droite et on a tendance à partir plus vers la droite
			else if ( (this.robot.leftFrontSonar.moyData > ((Param.RIGHTWALL_MAX+Param.RIGHTWALL_MIN)/2 - Param.RIGHTWALL_MID) )
					&& (errAngle > Param.REG_ERR) ) {
				this.reg = Param.MIDRIGHTREG ;
			}
		}
		
		// Sinon si on a aucun mur à coté
		else {
			this.reg = Param.NOREG;
		}
	}
	
	// Execute le type de régulation demandé
	public void executeReg(double errAngle) {
	    if ( this.reg == Param.RIGHTREG  ){
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(-Param.RIGHTWALL_ARC1);
	    }

	    else if ( this.reg == Param.LEFTREG ) {
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(Param.RIGHTWALL_ARC1);
	    }

	    else if ( this.reg == Param.MIDRIGHTREG ) {
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(-Param.RIGHTWALL_ARC2);
	    }
	    
	    else if ( this.reg == Param.MIDLEFTREG ) {
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.arcForward(Param.RIGHTWALL_ARC2);
	    }
	    
	    else if( this.reg == Param.NOREG ) {
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.diffPilot.forward();
	    }
	    else if( this.reg == Param.ROTREG ) {
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			this.turn(-errAngle);
			this.diffPilot.forward();
	    }
	}	

	// Calcul de l'erreur de l'angle	
	public double calcErr(double errAngle) {
		errAngle = this.robot.refAngle - this.robot.compass.moyData;
		if(errAngle<-180) {
			errAngle = errAngle+360;
		}
		else if (errAngle>180) {
			errAngle = errAngle-360;
		}
		return errAngle;
	}
	
	// Fait tourner le robot de 90° à droite
	public void turnRight() {
		double err ;
		
		if(this.robot.dir==Param.XP) {
			this.robot.dir = Param.YP ;
		}
		else if (this.robot.dir==Param.XN) {
			this.robot.dir = Param.YN ;
		}
		else if (this.robot.dir==Param.YP) {
			this.robot.dir = Param.XN ;
		}
		else if (this.robot.dir==Param.YN) {
			this.robot.dir = Param.XP ;
		}
		
	    this.robot.refAngle = (this.robot.refAngle - 90)%360 ;

		if(this.robot.refAngle<0) {
			this.robot.refAngle = this.robot.refAngle + 360 ;
		}
		
		err = this.robot.refAngle - this.robot.compass.moyData;
		if(err<-180) {
			err = err+360;
		}
		else if (err>180) {
			err = err-360;
		}
		this.turn(-err);
	}
	
	// Fait tourner le robot de 90° à gauche
	public void turnLeft() {
		double err ;
		
		if(this.robot.dir==Param.XP) {
			this.robot.dir = Param.YN ;
		}
		else if (this.robot.dir==Param.XN) {
			this.robot.dir = Param.YP ;
		}
		else if (this.robot.dir==Param.YP) {
			this.robot.dir = Param.XP ;
		}
		else if (this.robot.dir==Param.YN) {
			this.robot.dir = Param.XN ;
		}

	    this.robot.refAngle = (this.robot.refAngle + 90)%360 ;
	    
		if(this.robot.refAngle<0) {
			this.robot.refAngle = this.robot.refAngle + 360 ;
		}
		
		err = this.robot.refAngle - this.robot.compass.moyData;
		if(err<-180) {
			err = err+360;
		}
		else if (err>180) {
			err = err-360;
		}
		this.turn(-err);
	}
	
	// Fait tourner le robot de l'angle voulu (sens horaire)
	public void turn(double angle) {
		double err ;		
		double desiredAngle = (this.robot.compass.moyData - angle) % 360;	
		
		if(desiredAngle<0) {
			desiredAngle=desiredAngle+360;
		}
		else if(desiredAngle>360) {
			desiredAngle=desiredAngle-360;
		}
		
		err = desiredAngle - this.robot.compass.moyData;
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

			this.robot.refreshCompass();
			
			err = desiredAngle - this.robot.compass.moyData;
			if(err<-180) {
				err = err+360;
			}
			else if (err>180) {
				err = err-360;
			}
		}
		
		//On arrete les moteurs pour arreter le rotate lorsqu'on à atteind l'angle
		this.stop();
	}
	
	// Fait tourner le robot de 180°
	public void turnBack() {
		double err ;
		
		if(this.robot.dir==Param.XP) {
			this.robot.dir = Param.XN ;
		}
		else if (this.robot.dir==Param.XN) {
			this.robot.dir = Param.XP ;
		}
		else if (this.robot.dir==Param.YP) {
			this.robot.dir = Param.YN ;
		}
		else if (this.robot.dir==Param.YN) {
			this.robot.dir = Param.YP ;
		}
		
	    this.robot.refAngle = (this.robot.refAngle + 180)%360 ;
		if(this.robot.refAngle<0) {
			this.robot.refAngle = this.robot.refAngle + 360 ;
		}
		
		err = this.robot.refAngle - this.robot.compass.moyData;
		if(err<=-180) {
			err = err+360;
		}
		else if (err>180) {
			err = err-360;
		}
		this.turn(-err);
	}

	// Fait reculer le robot
	public void moveBackward() {
		//this.diffPilot.backward();
	}

	// Dude, stahp.
	public void stop() {
		this.diffPilot.stop();
	}

}