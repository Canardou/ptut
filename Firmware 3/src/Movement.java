import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;
import java.lang.Math;
import lejos.nxt.Sound;

public class Movement {

	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	protected DifferentialPilot diffPilot;
	private Robot robot ;
	private int reg ;
	private double distTraveled ;
	private double distRegulated ;
	private int countLeftReg ;
	private int countRightReg ;

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
	// au milieu du couloir : régulation tout les RIGHTWALL_REG cm au minimum (si régulation necessaire)
	public void moveForward() {	
		double errAngle=0 ;
		this.distTraveled = 0.0 ;
		this.distRegulated = 0.0;		
		this.reg = Param.NOREG ;
		this.countLeftReg = 1 ;
		this.countRightReg = 1;
				
		this.robot.checkEnv();
		this.robot.checkEnv();
		
		errAngle = this.calcErr(errAngle);
		this.turn(-errAngle);	// On commence par bien s'aligner à l'allée ..
		this.diffPilot.reset(); // reset du tacho count pour le getDistanceTraveled()
				
		// tant qu'il n'y a pas de mur devant ou qu'on n'a pas fait 30cm
		while ( (!this.robot.frontWallDetected) && (this.distTraveled < Param.UNIT_DIST) ) {
			
			this.distTraveled += this.diffPilot.getMovement().getDistanceTraveled();	
			this.diffPilot.forward(); //Let's go, dude.
			this.checkSpeed();
			
			// si on a parcouru au moins RIGHTWALL_REG cm depuis la derniere régulation
			if( (this.distTraveled - this.distRegulated) >= Param.RIGHTWALL_REG ) {				
				errAngle = this.calcErr(errAngle);				
				this.chooseReg(errAngle);
				this.executeReg(errAngle);
			}

			// Capter l'environnement du robot
			this.robot.checkEnv();
		}
		this.stop();		
	}
	
	// Adapte la vitesse selon la présence d'un mur devant ou non
	public void checkSpeed() {
		// si on approche d'un mur (devant) on réduit la vitesse
		if (this.robot.frontSonar.moyData < Param.FRONTWALL_DANGER-Param.FRONTWALL_HYST) { 
			this.diffPilot.setTravelSpeed(Param.SPEED_DANGER);
		}
		else if(this.robot.frontSonar.moyData > Param.FRONTWALL_DANGER+Param.FRONTWALL_HYST) {
			this.diffPilot.setTravelSpeed(Param.SPEED_CRUISE);
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
	
	// Fonctions de régulation
	public void chooseReg(double errAngle) {
		if( (this.robot.rightSonar.moyData < Param.RIGHTWALL_MIN) ) {
			this.reg = Param.RIGHTREG ;
		}
		else if(this.robot.rightWallDetected && ( this.robot.rightSonar.moyData > Param.RIGHTWALL_MAX ) ) {
			this.reg = Param.LEFTREG ;
		}
		else if ( Math.abs(errAngle) > Param.RIGHTWALL_ANGLE) {
			this.reg = Param.MIDREG ;
		}
		else {
			this.reg = Param.NOREG ;
		}
	}
	public void executeReg(double errAngle) {
	    if ( this.reg == Param.RIGHTREG  ){
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			distRegulated=distTraveled;
	    	this.turn(-errAngle-this.countRightReg*Param.RIGHTWALL_CORR);
	    	if(this.countRightReg<=2) {
	    		this.countRightReg++;
	    	}
	    }			    
	    else {
	    	this.countRightReg = 0 ;
	    }

	    if ( this.reg == Param.LEFTREG ) {
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			distRegulated=distTraveled;
	    	this.turn(-errAngle+this.countLeftReg*Param.RIGHTWALL_CORR);
	    	if(this.countLeftReg<=2) {
	    		this.countLeftReg++ ;
	    	}
	    } 
	    else {
	    	this.countLeftReg = 0 ;
	    }
	    
	    if( this.reg == Param.MIDREG ) {
	    	distTraveled += this.diffPilot.getMovement().getDistanceTraveled();
			distRegulated=distTraveled;
			this.turn(-errAngle);
	    }
	}	
	
	// Fait tourner le robot de 90° à droite
	public void turnRight() {
		double err ;
		
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

			this.robot.checkEnv();
			
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
		this.turn(180.0);
	}

	// Fait reculer le robot
	public void moveBackward() {
		this.diffPilot.backward();
	}

	// Dude, stahp.
	public void stop() {
		this.diffPilot.stop();
	}

}