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

/**
 * Classe principale du code embarqué sur le robot. Elle contient tout les éléments qui permettent
 * de représenter le robot.
 * @author Thomas
 */
public class Robot {	
		
	/**
	 * Attribut représentant les mouvements du robot
	 * @see Movement
	 * @see Robot#getMov()
	 */
	 private Movement mov;
	 
	 /**
	  * Attribut représentant l'environnement du robot
	  * @see Environment
	  * @see Robot#getEnv()
	  */
	 private Environment env;	 
	 
	 /**
	  * Attribut représentant le moteur du sonar
	  * @see NXTRegulatedMotor
	  * @see Robot#getSonarMotor
	  */
	 private NXTRegulatedMotor sonarMotor ;
	 
	 /**
	  * Attribut représentant la boussole
	  * @see Compass
	  * @see Robot#getCompass()
	  */
	 private Compass compass;	
	 
	 /**
	  * Attribut représentant le sonar droit
	  * @see Sonar
	  * @see Robot#getRightSonar
	  */
	 private Sonar rightSonar;
	 
	 /**
	  * Attribut représentant le sonar gauche
	  * @see Sonar
	  * @see Robot#getLeftFrontSonar
	  */
	 private Sonar leftFrontSonar;	 
	 
	 /**
	  * Attribut représentant la liste d'ordres
	  * @see ListOrder
	  * @see Robot#getListOrder
	  */
	 private ListOrder listOrder;
	 
	 /**
	  * Attribut représentant l'ordre actuel du robot
	  * @see Robot#getOrder()
	  */
	 private int order; 
	 
	 /**
	  * Attribut représentant la tache de communication
	  * @see ThreadCom
	  */
	 private ThreadCom tCom;
	 
	 /**
	  * Attribut permettant de savoir si le robot est prét à démarrer
	  */
	 private boolean readyToGo;

	 /**
	  * Constructeur de Robot
	  */
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
		this.readyToGo		= false;
	 }
	 
	/**
	 * Fonction principale du robot
	 */
	 private void go(){
		 this.init();

		 while(!Button.ENTER.isDown()){			 
			this.chooseOrder();			
			this.executeOrder();	
			if(this.readyToGo) {
				this.env.refreshCoord();
				this.env.saveCurrentCase(false);
			}
			//this.env.afficher();		
		 }
	 }
	 
	 /**
	  * Choisit un ordre en se basant sur la liste d'ordre recu du superviseur
	  * @see ListOrder
	  * @see Robot#order
	  */
	 private void chooseOrder(){
		 this.order = this.listOrder.getOrder();
		 // On vérifie que les conditions initiales soit bonnes si on veut deplacer le robot, i.e. :
		 //  - La boussole a été calibrée
		 //  - L'angle de référence a été enregistré
		 //  - La position du robot a été recu
		 if( (!this.mov.getRefAngleDone() || !this.compass.getCalDone() || !this.env.getInitPositionDone() )
				 && (this.order==Param.FORWARD || this.order==Param.TURNL || this.order==Param.TURNR || this.order==Param.TURNB || this.order==Param.CHECKFIRSTCASE)) {
			 this.order=Param.STOP;
			 System.out.println("Impossible de deplacer le robot sans cal,angleref ou coordinit");
		 }
		 // Si les conditions initiales sont bonnes, le prochain ordre de deplacement ne peut etre que CHECKFIRSTCASE
		 else if ( this.mov.getRefAngleDone() && this.compass.getCalDone() && this.env.getInitPositionDone() 
				 && !this.readyToGo 
				 && ( this.order==Param.FORWARD || this.order==Param.TURNL || this.order==Param.TURNR || this.order==Param.TURNB) ) {
			 this.order=Param.STOP;
			 System.out.println("Le premier deplacement doit etre un CHECKFIRSTCASE");
		 }
	 }
	 
	 /**
	  * Execute l'ordre demandé
	  * @see Robot#chooseOrder()
	  * @see Robot#order
	  */
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
		 else if(this.order==Param.STOP){
			 this.mov.stop();
		 }
		 else if(this.order==Param.CALCOMPASS){
			this.compass.calibrate();	
	 	 }
		 else if(this.order==Param.SAVEREFANGLE){
			this.env.check2WallsCompassFull();		 
			this.mov.saveRefAngle();
		 }
		 else if(this.order==Param.CHECKFIRSTCASE) {
			this.mov.turnLeft();
			this.mov.turnRight();
			this.readyToGo=true;
			this.env.saveCurrentCase(true);
		 }
		 else if(this.order==Param.SETPOSITION) {
			 this.env.setPosition();		 
		 }
	 }

	 /**
	  * Initialise divers paramètre
	  * @see Param#VOLUME
	  * @see Param#SPEED_CRUISE
	  * @see Param#RSPEED_CRUISE
	  * @see Param#RSPEED_SONARMOTOR
	  */
	 private void init(){
	    Sound.setVolume(Param.VOLUME);	
		this.mov.getDiffPilot().setTravelSpeed(Param.SPEED_CRUISE);
		this.mov.getDiffPilot().setRotateSpeed(Param.RSPEED_CRUISE);
		this.sonarMotor.setSpeed(Param.RSPEED_SONARMOTOR);

		this.env.setInitValues(10, 10, 0); 					// A faire depuis la com
		this.listOrder.addOrder(Param.SETPOSITION);			// A faire depuis la com
		
		//this.listOrder.addOrder(Param.CALCOMPASS); 		// Fait depuis la com :D
		//this.listOrder.addOrder(Param.SAVEREFANGLE); 		// Fait depuis la com :D
		//this.listOrder.addOrder(Param.CHECKFIRSTCASE);	// Fait depuis la com :D
	 } 
	 
	 /**
	  * Main
	  * @param args
	  */
	 public static void main(String[] args) {
		Robot bob = new Robot();
		bob.go();		
	}
	 
	 /**
	  * Attent que l'utilisateur appui sur un bouton
	  */
	 public void pause() {
		 Button.waitForAnyPress();
	 }
	 
	 /**
	  * Pause en ms
	  * @param ms
	  * 	Temps à attendre en ms
	  */
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
	 
	 /**
	  * @return la valeur de l'attribut mov
	  * @see Movement
	  * @see Robot#mov
	  */
	 public Movement getMov() {
		return this.mov ;
	 }
	 
	 /**
	  * @return la valeur de l'attribut env
	  * @see Environment
	  * @see Robot#env
	  */
	 public Environment getEnv() {
		return this.env ;
	 }
	 
	 /**
	  * @return la valeur de l'attribut sonarMotor
	  * @see NXTRegulatedMotor
	  * @see Robot#sonarMotor
	  */
	 public NXTRegulatedMotor getSonarMotor() {
		 return this.sonarMotor ;
	 }
	 
	 /**
	  * @return la valeur de l'attribut compass
	  * @see Compass
	  * @see Robot#compass
	  */
	 public Compass getCompass() {
		 return this.compass ;
	 }
	 
	 /**
	  * @return la valeur de l'attribut rightSonar
	  * @see Sonar
	  * @see Robot#rightSonar
	  */
	 public Sonar getRightSonar() {
		 return this.rightSonar ;
	 }
	 
	 /**
	  * @return la valeur de l'attribut leftFrontSonar
	  * @see Sonar
	  * @see Robot#leftFrontSonar
	  */
	 public Sonar getLeftFrontSonar() {
		 return this.leftFrontSonar ;
	 }
	 
	 /**
	  * @return la valeur de l'attribut listOrder
	  * @see ListOrder
	  * @see Robot#listOrder
	  */
	 public ListOrder getListOrder() {
		 return this.listOrder;
	 }
	 
	 /**
	  * @return la valeur de l'attribut order
	  * @see Robot#order
	  */
	 public int getOrder() {
		 return this.order ;
	 }
}
