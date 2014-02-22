import lejos.nxt.Button;

import java.lang.System;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;


public class Robot {	
		
	 /**********************************************/
	 /**                                          **/
	 /**                ATTRIBUTS                 **/
	 /**                                          **/
	 /**********************************************/	 
	 protected Movement  mov;
	 protected Compass   compass;	
	 protected Sonar     frontSonar;	
	 protected Sonar     rightSonar;
	 protected int       order;              // Ordre à réaliser
	 protected boolean   frontWallDetected;  // Mise a jour par l'appel de checkFrontWall()
	 protected boolean   rightWallDetected;  // Mise a jour par l'appel de checkRightWall()
	 protected double	 initAngle;          // Angle initial du robot
	 protected double    refAngle;           // Angle de référence ( = initAngle + k*90° )
	 //protected Jukebox   music;
	 
	 /**********************************************/
	 /**                                          **/
	 /**               CONSTRUCTEUR               **/
	 /**                                          **/
	 /**********************************************/	 
	 public Robot() {
		this.mov        = new Movement(Param.DIAMROUE, Param.DISTROUE, Motor.A, Motor.B, false, this);
		this.compass    = new Compass(SensorPort.S4);
		this.frontSonar = new Sonar(SensorPort.S2);
		this.rightSonar = new Sonar(SensorPort.S1);
		this.order      = Param.STOP ;
		//this.music      = new Jukebox();
	 }
	 
	 /**********************************************/
	 /**                                          **/
	 /**                 METHODES                 **/
	 /**                                          **/
	 /**********************************************/	 
	 // Fonction principale	 
	 public void go(){
		 while(true) {	
			this.checkEnv();        // Capter l'environnement du robot
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
		 else{
			 this.order=Param.TURNL;
		 }
		 
	 }
	 	 
	 // Execute l'ordre
	 public void executeOrder(){
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
	 
	 //Mise à jour des données issues des capteurs
	 public void checkEnv() {
		this.refreshFrontSonar();
		this.refreshRightSonar();
		this.refreshCompass();
		this.checkFrontWall();
		this.checkRightWall();
	 }
	 public void refreshFrontSonar() {
		this.frontSonar.acquisition();
		this.frontSonar.moyData = this.frontSonar.moyenne();
	 }
	 public void refreshRightSonar() {
		this.rightSonar.acquisition();
		this.rightSonar.moyData = this.rightSonar.moyenne();
	 }
	 public void refreshCompass() {
		this.compass.acquisition();
		this.compass.moyData = this.compass.moyenne();
	 }
	 public void checkFrontWall(){
		 if(this.frontSonar.moyData<=Param.LIMFRONTWALL){
			 this.frontWallDetected = true;
		 }
		 else {
			 this.frontWallDetected = false;
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
			this.checkEnv();
		}

		// Calibration de la boussole
		this.mov.diffPilot.setRotateSpeed(Param.RSPEED_CAL);
		this.compass.startCalibration() ;
		this.mov.diffPilot.rotate(Param.ANGLE_CAL);
		this.compass.stopCalibration() ;
		Sound.beepSequenceUp(); // ready to go ! \o/
		
		this.pause();
		this.pauseTime(2000);
				
		//Le robot est ici posé à son point initial, on met à jour les capteurs
		for(int i=0;i<=(Param.TAB_NBDATA);i++) {
			 this.checkEnv();
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
