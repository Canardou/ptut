package robot;
import env.*;
import sensor.*;
import orders.*;
import lejos.nxt.Button;

import java.io.IOException;
import java.lang.System;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import communication.*;

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
	 private	ListOrder			listOrder;
	 private	int     			order;   
	//private Jukebox   			music;
	 
	 private	ComBluetooth		com;
	 private 	EntiteeBT			entitee;	 
	 private	Trame2				trame;

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
		//this.music      	= new Jukebox();
		
		this.entitee		= new EntiteeBT("Robot J",(byte)1,"00:16:53:06:F5:30");
		this.com			= new ComBluetooth(entitee);
		this.trame			= new Trame2((byte)3,(byte)1 ,(byte)0);		
	 }
	 
	 /**********************************************/
	 /**                                          **/
	 /**                 METHODES                 **/
	 /**                                          **/
	 /**********************************************/	

	 /** Fonction principale **/
	 private void go(){
		 /*
		 // Test de la liste d'ordre 
		 this.listOrder.add(Param.FORWARD);
		 this.listOrder.add(Param.FORWARD);
		 this.listOrder.add(Param.FORWARD);
		 this.listOrder.add(Param.TURNR);
		 this.listOrder.add(Param.FORWARD);
		 this.listOrder.add(Param.TURNB);
		 this.listOrder.add(Param.FORWARD);
		 this.listOrder.add(Param.TURNL);
		 this.listOrder.add(Param.FORWARD);
		 this.listOrder.add(Param.FORWARD);
		 this.listOrder.add(Param.FORWARD);
		 this.listOrder.add(Param.TURNB);*/
		 
		 // Connexion bluetooth
		 System.out.println("Attente co");
		 try{
			 this.com.initialisation();
		 } catch (Exception e) {}
		 System.out.println("Co done");
		 
		 System.out.println("Go!");
		 while(!Button.ENTER.isDown()){			 
			this.chooseOrder();	
			this.executeOrder();	
			this.env.refreshCoord();
			this.env.afficher();			
			Sound.beep();
		 }
	 }
	 
	 /** Choisir un ordre : Algorithme d'exploration **/
	 private void chooseOrder(){
		 
		 // Tentative de recevoir un ordre via bluetooth
		 System.out.println("Ecoute");
		 try{
			 //this.entitee.getOutput().write(0);
			 //this.entitee.getOutput().flush();
			 System.out.println("Bla");
			 this.trame=this.com.ecouter();
		 } catch (Exception e) {}
		 System.out.println("Recep ok");		 
		 this.order=this.trame.getOrdre();
		 
		 /*
		 // Test r�cuperation de l'ordre depuis la liste
		 this.order = this.listOrder.getOrder();*/
		 
		 /*
		 // Algo d'explo basique pour tester les d�placements
		 if(!this.env.getFrontWallDetected()){
			 this.order=Param.FORWARD;
		 }
	     else if(!this.env.getRightWallDetected()){
	    	 this.order=Param.TURNR;
		 }
		 else if(!this.env.getLeftWallDetected()){
			 this.order=Param.TURNL;
		 }
		 else {
			 this.order=Param.TURNB;
		 }*/
		 
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
		System.out.println("Demarrage");
	    Sound.setVolume(Param.VOLUME);
	    Sound.beep() ;	
		this.mov.getDiffPilot().setTravelSpeed(Param.SPEED_CRUISE);
		this.sonarMotor.setSpeed(Param.RSPEED_SONARMOTOR);
		
		// Calibration de la boussole
		this.pause();
		this.pauseTime(1000);		
		//this.compass.calibrate();		
		this.pause();
		this.pauseTime(2000);
				
		//Le robot est ici pos� � son point initial, on met � jour les capteurs
		this.env.check3WallsCompass();
 
		// On enregistre l'angle initial qui servira de reference pour tout le trip
		this.mov.saveInitAngle();
		
		// On check tout les murs de la case initiale (rotation necessaire pour checker le mur arri�re)
		this.mov.turnLeft();
		this.mov.turnRight();
		
		this.env.afficher();
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
	 
	 
	 public static void main(String[] args) {
		Robot bob = new Robot();
		//bob.music.play(Jukebox.STARWARS_INTRO, false);

		bob.init();			   
		bob.go();		
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
	 public int getOrder() {
		 return this.order ;
	 }
}
