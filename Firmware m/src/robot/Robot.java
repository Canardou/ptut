package robot;

import env.*;
import sensor.*;
import orders.*;
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
	 private	Movement  			mov;
	 private    Environment			env;	 
	 private 	NXTRegulatedMotor	sonarMotor ;
	 private 	Compass  			compass;		
	 private	Sonar				rightSonar;
	 private    Sonar				leftFrontSonar;	 
	 private 	ListOrder			listOrder;
	 private	int     			order;   	 
	 private	ThreadCom			tCom;

	 /**********************************************/
	 /**                                          **/
	 /**               CONSTRUCTEUR               **/
	 /**                                          **/
	 /**********************************************/
	 public Robot() {
		this.mov        	= new Movement(Param.DIAMROUE, Param.DISTROUE, Motor.A, Motor.B, false, this);
		this.env			= new Environment(this);		
		this.sonarMotor 	= Motor.C;
		this.compass    	= new Compass(SensorPort.S4,this);		
		this.leftFrontSonar = new Sonar(SensorPort.S2); 
		this.rightSonar 	= new Sonar(SensorPort.S1);		
		this.listOrder      = new ListOrder();
		this.order      	= Param.STOP ;
		this.tCom			= new ThreadCom(this);
	 }
	 
	 /**********************************************/
	 /**                                          **/
	 /**                 METHODES                 **/
	 /**                                          **/
	 /**********************************************/	

	 /** Fonction principale **/
	 private void go(){		 
		 this.init();

		 while(!Button.ENTER.isDown()){			 
			this.chooseOrder();			
			this.executeOrder();	
			this.env.refreshCoord();
			//this.env.afficher();			
			//Sound.beep();
		 }
	 }
	 
	 /** Choisir un ordre : Algorithme d'exploration **/
	 private void chooseOrder(){
		 
		 // Récuperation de l'ordre depuis la liste (STOP si liste vide)
		 this.order = this.listOrder.getOrder();
		 
	 }
	 
	 /** Execute l'ordre **/
	 private void executeOrder(){
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

	 /** Fonction d'initialisation **/
	 private void init(){
	    Sound.setVolume(Param.VOLUME);
	    Sound.beep() ;	
		this.mov.getDiffPilot().setTravelSpeed(Param.SPEED_CRUISE);
		this.mov.getDiffPilot().setRotateSpeed(Param.RSPEED_CRUISE);
		this.sonarMotor.setSpeed(Param.RSPEED_SONARMOTOR);
		
		// Calibration de la boussole
		this.pause();
		this.pauseTime(1000);
		//this.compass.calibrate();	
				
		//Le robot est ici posé à son point initial, on met à jour les capteurs
		this.pause();
		this.pauseTime(2000);
		this.env.check2WallsCompassFull();
 
		// On enregistre l'angle initial qui servira de reference pour tout le trip
		this.mov.saveInitAngle();
		
		// On check tout les murs de la case initiale (rotation necessaire pour checker le mur arrière)
		this.mov.turnLeft();
		this.mov.turnRight();
	 } 
	 

	 public static void main(String[] args) {
		Robot bob = new Robot();
		bob.go();		
	}
	 
	 /** Pause attente que l'utilisateur appui sur un bouton **/
	 public void pause() {
		 Button.waitForAnyPress();
	 }
	 
	 /** Pause en ms, avec un beep bonus toute les secondes, c'est gratuit! **/
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
	 
	 public Movement getMov() {
		return this.mov ;
	 }
	 public Environment getEnv() {
		return this.env ;
	 }
	 public NXTRegulatedMotor getSonarMotor() {
		 return this.sonarMotor ;
	 }
	 public Compass getCompass() {
		 return this.compass ;
	 }
	 public Sonar getRightSonar() {
		 return this.rightSonar ;
	 }
	 public Sonar getLeftFrontSonar() {
		 return this.leftFrontSonar ;
	 }
	 public ListOrder getListOrder() {
		 return this.listOrder;
	 }
	 public int getOrder() {
		 return this.order ;
	 }
}
