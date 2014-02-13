import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot {	
		
	
	/****************/
	/** CONSTANTES **/
	/****************/
	public static final int ARRET = 0;
	public static final int AVANCE = 1;
	public static final int TOURNE = 2;
	
	 /***************/
	 /** ATTRIBUTS **/
	 /***************/
	 protected DifferentialPilot motor;	
	 protected Compass compass;	
	 protected Sonar leftSonar;	
	 protected Sonar frontSonar;	
	 protected Sonar rightSonar;
	 protected int etat;
	
	 /******************/
	 /** CONSTRUCTEUR **/
	 /******************/
	 public Robot() {
		this.motor      = new DifferentialPilot(5.6, 11.5, Motor.A, Motor.B, false);
		this.compass    = new Compass(SensorPort.S4);
		this.leftSonar  = new Sonar(SensorPort.S3);
		this.frontSonar = new Sonar(SensorPort.S2);
		this.rightSonar = new Sonar(SensorPort.S1);
		this.etat = AVANCE ;
	 }
	
	 
	 /**************/
	 /** METHODES **/
	 /**************/
	public static void main(String[] args) {
		System.out.println("Demarrage");	
		Robot derp = new Robot();
		derp.motor.setRotateSpeed(150);
		Button.waitForAnyPress();
		while(true) {
			
			derp.frontSonar.acquisition();
		    derp.frontSonar.moyData = derp.frontSonar.moyenne();		    
			derp.leftSonar.acquisition();
		    derp.leftSonar.moyData = derp.leftSonar.moyenne();		    
			derp.rightSonar.acquisition();
		    derp.rightSonar.moyData = derp.rightSonar.moyenne();
			derp.compass.acquisition();
		    derp.compass.moyData = derp.compass.moyenne();
			
		    if(derp.etat==AVANCE){
				if(derp.frontSonar.moyData>25){
					derp.motor.setTravelSpeed(30);	
					derp.motor.travel(7,true);
				}		
				else if(derp.frontSonar.moyData>20){
					derp.motor.setTravelSpeed(15);	
					derp.motor.travel(5,true);
				}	
				else if(derp.frontSonar.moyData>15){
					derp.motor.setTravelSpeed(10);	
					derp.motor.travel(3,true);
				}
				else {
					derp.etat=TOURNE;
					derp.motor.travel(-3);
				}
		    }
		    
		   if(derp.etat==TOURNE) {
			   if(derp.rightSonar.moyData>30) {
				   derp.compass.desAngle=(derp.compass.moyData-80)%360;
				   while((derp.compass.desAngle-derp.compass.moyData)>10 || (derp.compass.desAngle-derp.compass.moyData)<-10) {
					   derp.compass.acquisition();
					   derp.compass.moyData = derp.compass.moyenne();
					   derp.motor.rotate(5,true);
				   }
			   }
			   else if (derp.leftSonar.moyData>30) {
				   derp.compass.desAngle=(derp.compass.moyData+80)%360;
				   while((derp.compass.desAngle-derp.compass.moyData)>10 || (derp.compass.desAngle-derp.compass.moyData)<-10) {
					   derp.compass.acquisition();
					   derp.compass.moyData = derp.compass.moyenne();
					   derp.motor.rotate(-5,true);
				   }
			   }
			   derp.etat=AVANCE;
		   }	
		}
	}
}




