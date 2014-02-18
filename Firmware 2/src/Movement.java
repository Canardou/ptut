import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;
import lejos.nxt.Sound;

public class Movement {

	/****************/
	/** CONSTANTES **/
	/****************/

	// Paramètres pour longer le mur droit
	public static final double RIGHTWALL_DIST 	= 10;  // Distance à laquelle le robot va longer le mur
	public static final double RIGHTWALL_ERR 	= 10;  // Erreur permise, défini une bande à RIGHTWALL_DIST +- RIGHTWALL_ERR du mur droit dans laquelle le robot doit rouler
	public static final double RIGHTWALL_GAIN 	= 10; // Gain du régulateur P pour longer le mur
	public static final double RIGHTWALL_REG	= 5;  // Régule la position par rapport au mur droit tout les RIGHTWALL_REG cm.

	// Paramètres de vitesse
	public static final double SPEED_CRUISE		= 30; // Vitesse de croisère (38 max)
	public static final double SPEED_DANGER		= 15; // Vitesse à l'approche d'un mur avant
	public static final double FRONTWALL_DANGER = 24; // Distance à partir de laquelle la vitesse est réduite (approche d'un mur avant)

	// Paramètres de la carte
	public static final double UNIT_DIST	 	= 30; // Longueur d'une case

	// Paramètres des virages
	public static final double RSPEED_CRUISE 	= 70;// Vitesse de rotation (360 max)
	public static final double RSPEED_DANGER	= 20; // Vitesse de rotation lorsqu'on approche de l'angle désiré
	public static final double ANGLE_DANGER		= 20; // Angle relatif à l'objectif à partir duquel la vitesse de rotation est réduite
	public static final double BACK_DIST 		= 1;  // Distance de recul lors de la décomposition de la rotation
	public static final double ANGLE_ERR		= 3;  // Erreur tolérée sur l'angle (angledésiré +- ANGLE_ERR)


	/***************/
	/** ATTRIBUTS **/
	/***************/
	protected DifferentialPilot diffPilot;
	private Robot robot ;

	/******************/
	/** CONSTRUCTEUR **/
	/******************/
	public Movement(double wheelDiameter, double trackWidth, RegulatedMotor leftMotor, RegulatedMotor rightMotor, boolean reverse, Robot bob) {
		this.diffPilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
		this.diffPilot.setRotateSpeed(RSPEED_CRUISE);
		this.diffPilot.setTravelSpeed(SPEED_CRUISE);
		this.robot = bob ;
	}

	/**************/
	/** METHODES **/
	/**************/

	// Fait avancer le robot de 30cm (sauf si mur detecté à l'avant) en faisant en sorte que le robot soit bien
	// au milieu du couloir : régulation tout les RIGHTWALL_REG cm au minimum (si régulation necessaire)
	public void moveForward() {		
		System.out.println("entree moveforward");	
		
		double distTraveled = 0.0 ;
		double distRegulated = (-RIGHTWALL_REG-1);
		
		this.diffPilot.reset(); // reset du tacho count pour le getDistanceTraveled()
		
		// tant qu'il n'y a pas de mur devant ou qu'on n'a pas fait 30cm
		while ( (!this.robot.frontWallDetected) /*|| (distTraveled < UNIT_DIST)*/ ) {
			
			this.diffPilot.forward(); //Let's go, dude.

			// si on approche d'un mur (devant) on réduit la vitesse
			if (this.robot.frontSonar.moyData < FRONTWALL_DANGER) { 
				this.diffPilot.setTravelSpeed(SPEED_DANGER);
			}			
			else {
				this.diffPilot.setTravelSpeed(SPEED_CRUISE);
			}
			// si on a parcouru au moins RIGHTWALL_REG cm depuis la derniere régulation
			if( (distTraveled-distRegulated) >= RIGHTWALL_REG ){
				// si on est trop proche du mur droit, OU s'il y a bien un mur et qu'on est trop loin
				// => on tourne un peu à gauche pour s'en éloigner
				if ( (this.robot.rightSonar.moyData < (RIGHTWALL_DIST - RIGHTWALL_ERR)) || (this.robot.rightWallDetected && (this.robot.rightSonar.moyData > (RIGHTWALL_DIST + RIGHTWALL_ERR))) ) { 
					this.turn( RIGHTWALL_GAIN*(this.robot.rightSonar.moyData - RIGHTWALL_DIST) );
					distRegulated = distTraveled ;
				} 
			}

			// Capter l'environnement du robot
			this.robot.refreshFrontSonar();
			this.robot.refreshRightSonar();
			this.robot.checkFrontWall();
			this.robot.checkRightWall();	
			distTraveled = this.diffPilot.getMovement().getDistanceTraveled();
		}
		System.out.println("sortie moveforward");	
		this.stop();		
	}

	// Fait tourner le robot de 90° à droite
	public void turnRight() {
		this.turn(90.0);
	}
	
	// Fait tourner le robot de 90° à gauche
	public void turnLeft() {
		this.turn(-90.0);
	}
	
	// Fait tourner le robot de l'angle voulu (sens horaire)
	public void turn(double angle) {
		System.out.println("entree turn");	
		double err ;
		
		double desiredAngle = (this.robot.compass.moyData - angle) % 360;		
		err = desiredAngle - this.robot.compass.moyData;
		
		System.out.println("Actu="+this.robot.compass.moyData);	
		System.out.println("Des="+desiredAngle);	
		System.out.println("err="+err);
		
		
		// Tant qu'on n'a pas atteind l'angle
		while ( err < -ANGLE_ERR || err > ANGLE_ERR ) {
			
			this.diffPilot.rotate(angle, true);//MODIFIER ICI SI SENS PAS BON
			
			// Lorsqu'on se rapproche de l'angle désiré on réduit la vitesse de rotation
			if ( err > -ANGLE_DANGER && err < ANGLE_DANGER) {
				//this.stop();//A VOIR SI NECESSAIRE POUR LIMITER LE DEPASSEMENT
				this.diffPilot.setRotateSpeed(RSPEED_DANGER);
			}
			else {
				this.diffPilot.setRotateSpeed(RSPEED_CRUISE);
			}

			this.robot.refreshCompass();
			err = desiredAngle - this.robot.compass.moyData;
			if(err>angle || err<-angle){
				System.out.println("err trop grand"+err);
			}
		}
		System.out.println("sortie turn");	
		
		//SI BESOIN GESTION DU DEPASSEMENT
		
		//On arrete les moteurs pour arreter le rotate lorsqu'on à atteind l'angle
		this.stop();
	}
	
	// Idem que turn(angle) mais avec une décomposition de la rotation pour
	// tourner dans une petite zone sans toucher les murs
	public void turn(double angle, int nbdiv) {
		double desiredAngle = (this.robot.compass.moyData - (angle / nbdiv)) % 360;

		// Tant qu'on n'a pas atteind l'angle
		for (int i = 1; i <= nbdiv; i++) {
			while ( (desiredAngle - this.robot.compass.moyData) < -ANGLE_ERR || (desiredAngle - this.robot.compass.moyData) > ANGLE_ERR ) {

				this.diffPilot.rotate(angle, true);//MODIFIER ICI SI SENS PAS BON

				// Lorsqu'on se rapproche de l'angle désiré on réduit la vitesse de rotation
				if ( (desiredAngle - this.robot.compass.moyData) > -ANGLE_DANGER && (desiredAngle - this.robot.compass.moyData) < ANGLE_DANGER ) {
					this.stop();//A VOIR SI NECESSAIRE POUR LIMITER LE DEPASSEMENT
					this.diffPilot.setRotateSpeed(RSPEED_DANGER);
				}
				this.robot.refreshCompass();
			}

			//SI BESOIN GESTION DU DEPASSEMENT
			
			this.stop();
			this.diffPilot.setRotateSpeed(RSPEED_CRUISE);

			// Si l'on a pas atteind l'angle final on recule un peu pour recentrer le robot
			if (i != nbdiv) {
				this.diffPilot.travel(-BACK_DIST);
				this.stop();
				desiredAngle = (desiredAngle - (angle / nbdiv)) % 360;
			}
		}
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