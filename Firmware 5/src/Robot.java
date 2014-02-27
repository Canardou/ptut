import lejos.nxt.Button;

import java.lang.System;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;


public class Robot {	
		
	 /**********************************************/
	 /**                                          **/
	 /**                ATTRIBUTS                 **/
	 /**                                          **/
	 /**********************************************/	 
	 protected	Movement  			mov;
	 protected 	NXTRegulatedMotor	sonarMotor ;
	 
	 protected 	Compass  			compass;		
	 protected	Sonar				rightSonar;
	 protected  Sonar				leftFrontSonar;
	 
	 protected 	boolean   			frontWallDetected; 
	 protected 	boolean   			rightWallDetected; 
	 protected 	boolean   			leftWallDetected;  
	 
	 protected	int      			order;              // Ordre à réaliser
	 protected	double	 			initAngle;          // Angle initial du robot
	 protected	double    			refAngle;           // Angle de référence ( = initAngle + k*90° )
	 
	 protected  int 				x;
	 protected  int  				y;
	 protected  int 				dir;
	 //private Jukebox   			music;
	 
	 /**********************************************/
	 /**                                          **/
	 /**               CONSTRUCTEUR               **/
	 /**                                          **/
	 /**********************************************/
	 public Robot() {
		this.mov        	= new Movement(Param.DIAMROUE, Param.DISTROUE, Motor.A, Motor.B, false, this);
		this.sonarMotor 	= Motor.C;
		
		this.compass    	= new Compass(SensorPort.S4);		
		this.leftFrontSonar = new Sonar(SensorPort.S2); 
		this.rightSonar 	= new Sonar(SensorPort.S1);
		
		this.order      	= Param.STOP ;
		this.x 				= Param.INITX;
		this.y				= Param.INITY;
		this.dir			= Param.XP;
		//this.music      	= new Jukebox();
	 }
	 
	 /**********************************************/
	 /**                                          **/
	 /**                 METHODES                 **/
	 /**                                          **/
	 /**********************************************/	 
	 // Fonction principale	 
	 public void go(){
		 System.out.print("X="+this.x+" Y="+this.y+"\n");
		 while(!Button.ENTER.isDown()){
			 
			this.refreshCoord();
			this.afficher();
			
			if ( !this.mov.frontWallReg ) {
				this.checkFullEnv(); // Capter l'environnement complet du robot
			}
			else {
				this.checkLeftRightWalls();
			}
			
			//ouvrir connexion
			//senddata(id,x,y,murg,murd,mura,angleref);
			//receivedata(x,y,murg,murd,mura,murd,angleref);
			//receivecdata2(x,y);
			//fermer connexion
			
			this.chooseOrder();		// Choisir un ordre		
			this.executeOrder();	// Executer l'ordre		
			
			Sound.beep();
		 }
	 }
	 
	 // Choisir un ordre : Algorithme d'exploration
	 public void chooseOrder(){
		 if(!this.frontWallDetected){
			 this.order=Param.FORWARD;
		 }
	     else if(!this.rightWallDetected){
	    	 this.order=Param.TURNR;
		 }
		 else if(!this.leftWallDetected){
			 this.order=Param.TURNL;
		 }
		 else {
			 this.order=Param.TURNB;
		 }
	 }
	 
	 // Execute l'ordre
	 public void executeOrder(){
		 this.mov.frontWallReg = false ;
		 
		 if(this.order==Param.FORWARD){
			 this.mov.moveForward();
		 }
		 else if(this.order==Param.TURNR){
			 this.mov.turnRight();
		 }
		 else if(this.order==Param.TURNL){
			 this.mov.turnLeft();
		 } 
		 else if(this.order==Param.TURNB){
			 this.mov.turnBack();
		 }
		 else if(this.order==Param.BACKWARD){
			 this.mov.moveBackward();
		 }
		 else if(this.order==Param.STOP){
			 this.mov.stop();
		 }
	 }
	 
	 // Mise à jour des coordonées
	 public void refreshCoord() {
		 if ( this.order == Param.FORWARD ) {
			 if (this.dir==Param.XP) {
			 	 this.x++;
			 }
			 else if (this.dir==Param.XN) {
			 	 this.x--;
			 }
			 else if (this.dir==Param.YP) {
			 	 this.y++;
			 }
			 else if (this.dir==Param.YN) {
			 	 this.y--;
			 } 
		 }	
	 }
	 
	 public void afficher() {
		 if ( this.order == Param.FORWARD ) {
			 System.out.print("X="+this.x+" Y="+this.y+"\n");
		 }
		 else if ( this.order == Param.TURNL ) {
			 System.out.print("Tourne à gauche\n");
		 }
		 else if ( this.order == Param.TURNR ) {
			 System.out.print("Tourne à droite\n");
		 }
		 else if ( this.order == Param.TURNB ) {
			 System.out.print("Demi-tour\n");
		 }
	 }
	 
	 // Vérifier les 3 murs
	 public void checkFullEnv() {
		this.checkFrontWall();
		this.refreshLeftFrontSonar();
		this.refreshRightSonar();
		this.refreshCompass();		
		this.checkLeftWall();
		this.checkRightWall();
	 }	 
	 
	 // Vérifier mur droit et gauche
	 public void checkLeftRightWalls() {
		this.refreshLeftFrontSonar();
		this.refreshRightSonar();
		this.refreshCompass();
		this.checkLeftWall();
		this.checkRightWall();
	 }

	 public void refreshLeftFrontSonar() {
		this.leftFrontSonar.acquisition();
		this.leftFrontSonar.moyData = this.leftFrontSonar.moyenne();
	 }

	 public void refreshRightSonar() {
		this.rightSonar.acquisition();
		this.rightSonar.moyData = this.rightSonar.moyenne();
	 }
	
	 public void refreshCompass() {
		this.compass.acquisition();
		this.compass.moyData = this.compass.moyenne();
	 }
	 
	 public void checkLeftWall(){
		 if(this.leftFrontSonar.moyData<=Param.LIMLEFTWALL){
			 this.leftWallDetected = true;
		 }
		 else {
			 this.leftWallDetected = false;
		 }	 
	 }
	 
	 public void checkFrontWall(){
		 this.sonarMotor.rotateTo(-90);
		 for(int i=0;i<=(Param.TAB_NBDATA);i++) {
			 this.refreshLeftFrontSonar();
		 }
		 if(this.leftFrontSonar.moyData<Param.FRONTWALL_DANGER) {
			 this.mov.diffPilot.setTravelSpeed(Param.SPEED_DANGER);
			 while(this.leftFrontSonar.moyData>Param.LIMFRONTWALL) {
				 this.mov.diffPilot.forward();
				 this.refreshLeftFrontSonar();
			 }
			 this.mov.diffPilot.setTravelSpeed(Param.SPEED_CRUISE);
			 this.mov.stop();
		 }
		 if(this.leftFrontSonar.moyData<=Param.LIMFRONTWALL){
			 this.frontWallDetected = true;
		 }
		 else {
			 this.frontWallDetected = false;
		 }	
		 this.sonarMotor.rotateTo(0);
		 for(int i=0;i<=(Param.TAB_NBDATA);i++) {
			 this.refreshLeftFrontSonar();
		 }
	 }
	 
	 public void checkRightWall(){
		 if(this.rightSonar.moyData<=Param.LIMRIGHTWALL){
			 this.rightWallDetected = true;
		 }
		 else {
			 this.rightWallDetected = false;
		 }	
	 }
	 	 
	 // Fonction d'initialisation 
	 public void init(){
		// Initialisation du filtre des capteurs
		for(int i=0;i<=(Param.TAB_NBDATA);i++) {
			this.checkLeftRightWalls();
		}

		this.mov.diffPilot.setTravelSpeed(Param.SPEED_CRUISE);
		this.sonarMotor.setSpeed(Param.RSPEED_SONARMOTOR);
		this.mov.diffPilot.setRotateSpeed(Param.RSPEED_CAL);
		// Calibration de la boussole
		this.compass.startCalibration() ;
		this.mov.diffPilot.rotate(Param.ANGLE_CAL);
		this.compass.stopCalibration() ;
		Sound.beepSequenceUp(); // ready to go ! \o/
		
		this.pause();
		this.pauseTime(2000);
				
		//Le robot est ici posé à son point initial, on met à jour les capteurs
		for(int i=0;i<=(Param.TAB_NBDATA);i++) {
			this.checkLeftRightWalls();
		 }		 
		// On enrregistre l'angle initial qui servira de reference pour tout le trip
		this.initAngle =  this.compass.moyData;
		this.refAngle = this.initAngle;		
	 } 
	 
	 // Pause attente que l'utilisateur appui sur un bouton
	 public void pause() {
		 Button.waitForAnyPress();
	 }
	 // Pause en ms, avec un beep bonus toute les secondes, c'est gratuit
	 public void pauseTime(int ms) {
		 long initTime = System.currentTimeMillis();
		 long beepTime = initTime ;
		 Sound.beep();
		 while((System.currentTimeMillis()-initTime)<ms) {
			 if((System.currentTimeMillis()-beepTime)>1000) {
				 Sound.beep();
				 beepTime = System.currentTimeMillis();
			 }			 
		 }
	 }
	 		 
	 public static void main(String[] args) {
		System.out.println("Demarrage");
        Sound.setVolume(Param.VOLUME);
		Sound.beep() ;			
		Robot bob = new Robot();
		//bob.music.play(Jukebox.STARWARS_INTRO, false);
		bob.pause();
		bob.pauseTime(1000);
		bob.init();			    // Initialisation-calibration
		bob.go();		
	}
}
