import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;

public class Robot {	
		
	
	 /****************/
	 /** CONSTANTES **/
	 /****************/
	
	 //Caractéristiques du robot
  	 public static final double DIAMROUE     = 5.6;  //Diamètre des roues
  	 public static final double DISTROUE     = 11.5; //Distance entre les roues
  	 public static final double LIMFRONTWALL = 16;   //Limite en dessous de laquelle on considère qu'il y a un mur juste devant
  	 public static final double LIMRIGHTWALL = 16;   //Limite en dessous de laquelle on considère qu'il y a un mur juste à droite
  	 
  	 //Ordres
	 public static final int STOP     = 0;
	 public static final int FORWARD  = 1;
	 public static final int TURNL    = 2;
	 public static final int TURNR    = 3;
	 public static final int TURNB    = 4;	 
	 public static final int BACKWARD = 5;
	
	 	 
	 /***************/
	 /** ATTRIBUTS **/
	 /***************/
	 protected Movement  mov;	
	 protected Compass   compass;	
	 protected Sonar     frontSonar;	
	 protected Sonar     rightSonar;
	 protected int       order;
	 protected boolean   frontWallDetected;
	 protected boolean   rightWallDetected;
	
	 	 
	 /******************/
	 /** CONSTRUCTEUR **/
	 /******************/	 
	 public Robot() {
		this.mov        = new Movement(DIAMROUE, DISTROUE, Motor.A, Motor.B, false);
		this.compass    = new Compass(SensorPort.S4);
		this.frontSonar = new Sonar(SensorPort.S2);
		this.rightSonar = new Sonar(SensorPort.S1);
		this.order      = STOP ;
	 }
		 
		 
	 /**************/
	 /** METHODES **/
	 /**************/	 
	 //Fonction d'initialisation 
	 public void init(){
		 //Initialisation du filtre des capteurs
		 for(int i=0;i<=(this.frontSonar.getTabLength());i++){
			 this.refreshFrontSonar();
			 this.refreshRightSonar();
			 this.refreshCompass();
		 }		 
	 } 
	 
	 //Mise à jour des données issues des capteurs
	 public void refreshFrontSonar(){
		this.frontSonar.acquisition();
		this.frontSonar.moyData = this.frontSonar.moyenne();
	 }
	 public void refreshRightSonar(){
		this.rightSonar.acquisition();
		this.rightSonar.moyData = this.rightSonar.moyenne();
	 }
	 public void refreshCompass(){
		this.compass.acquisition();
		this.compass.moyData = this.compass.moyenne();
	 }
	 	 
	 //Vérifie si le robot arrive sur un mur
	 public void checkFrontWall(){
		 if(this.frontSonar.moyData<=LIMFRONTWALL){
			 this.frontWallDetected = true;
		 }
		 else {
			 this.frontWallDetected = false;
		 }	
	 }
	 public void checkRightWall(){
		 if(this.rightSonar.moyData<=LIMRIGHTWALL){
			 this.rightWallDetected = true;
		 }
		 else {
			 this.rightWallDetected = false;
		 }	
	 }

	 //Choisir un ordre
	 //Ou recevoir un ordre du superviseur
	 //..à voir
	 //Premiere version nommé "La tactique d'exploration en carton"
	 public void chooseOrder(){
		 if(!this.frontWallDetected){
			 this.order=FORWARD;
		 }
	     else if(!this.rightWallDetected){
	    	 this.order=TURNR;
		 }
		 else{
			 this.order=TURNL;
		 }
		 Sound.beep();
	 }
	 	 	 
	 //Execute l'ordre
	 public void executeOrder(){
			 if(this.order==FORWARD){
				 this.mov.moveForward();
			 }
			 else if(this.order==TURNR){
				 this.mov.turnRight();
			 }
			 else if(this.order==TURNL){
				 this.mov.turnLeft();
			 } 
			 else if(this.order==TURNB){
				 this.mov.turnBack();
			 }
			 else if(this.order==BACKWARD){
				 this.mov.moveBackward();
			 }
			 else if(this.order==STOP){
				 this.mov.stop();
			 }
	 }
	 
	 //Fonction principale	 
	 public void go(){
		 this.init();		
		 
		 while(true) {	
			 
			//Capter l'environnement du robot
			this.refreshFrontSonar();
			this.refreshRightSonar();
			this.refreshCompass();
			this.checkFrontWall();
			this.checkRightWall();

			//Choisir un ordre
			this.chooseOrder();		
			
			//Executer l'ordre
			this.executeOrder();
		 }
	 }
	 
	 
	 public static void main(String[] args) {
		Sound.beep();
		Robot bob = new Robot();
		Button.waitForAnyPress();
		Sound.beep();
		bob.go();		
	}
}




