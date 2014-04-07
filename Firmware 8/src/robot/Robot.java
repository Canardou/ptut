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
public class Robot extends Thread {	
		
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
	  * Attribut permettant de savoir si le robot à rempli les conditions initiales pour commencer a se déplacer
	  */
	 private boolean readyToGo;
	 
	 /**
	  * Attribut permettant de savoir si le robot à verifié au moins une fois les 4 murs de sa case initiale
	  */
	 private boolean checkFirstCaseDone;
	 
	 /**
	  * Attribut permettant de connaitre l'erreur courante sur l'ordre (utile lors des vérifications des conditions initiales)
	  */
	 private int errOrder;
	 
	 private int etatRobot;

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
		this.readyToGo		= false;
		this.checkFirstCaseDone=false;
		this.errOrder		= -1;
	 }
	 
	 /**
	  * Tache principale du robot
	  */
	 public void run() {
		 while(true){			 
			this.chooseOrderInsecurely();			
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
	  *  On vérifie que les conditions initiales soit bonnes si on veut deplacer le robot, i.e. : </br>
	  *  1 - La boussole a été calibrée (au moins une fois) -> ordre CALCOMPASS</br>
	  *  2 - L'angle de référence a été enregistré et la position du robot a été recu (au moins une fois, peu importe l'ordre) -> ordre SAVEREFANGLE et SETPOSITION</br>
	  *  3 - La premiere case a été visité entièrement (au moins une fois) -> ordre CHECKFIRSTCASE</br>
	  * @see ListOrder
	  * @see Robot#order
	  */
	 private int chooseOrder(){
		 this.order = this.listOrder.getOrder();
		 
		 if(!this.readyToGo && this.order!=Param.STOP) {
			 if( !this.compass.getCalDone() ) {
				 if( this.order!=Param.CALCOMPASS ) {
					 if(this.errOrder!=Param.CALCOMPASS) {
						 System.out.println("[ERR]chooseOrder:cal");
						 this.errOrder=Param.CALCOMPASS;
					 }	
					 this.order=Param.STOP;
					 return 1;
				 }			 
			 }
			 else if( !this.mov.getRefAngleDone() || !this.env.getInitPositionDone() ) {
				 if ( this.order!=Param.SAVEREFANGLE && this.order!=Param.SETPOSITION ) {
					 if(this.errOrder!=Param.SAVEREFANGLE) {
						 System.out.println("[ERR]chooseOrder:refangle/pos");
						 this.errOrder=Param.SAVEREFANGLE;
					 }
					 this.order=Param.STOP;
					 return 1;
				 }
			 }
			 else if( !this.checkFirstCaseDone ) {
				 if ( this.order!=Param.CHECKFIRSTCASE ) {
					 if(this.errOrder!=Param.CHECKFIRSTCASE) {
						 System.out.println("[ERR]chooseOrder:checkfirstcase");
						 this.errOrder=Param.CHECKFIRSTCASE;
					 }
					 this.order=Param.STOP;
					 this.readyToGo=true;
					 return 1;
				 }			 
			 }
		 }		 
		 return 0;
	 }
	 
	 /**
	  * Méthode de test, algorithme d'exploration basique
	  */
	 private void chooseOrderExploration(){
		 if(!this.readyToGo) {
			 this.readyToGo=true;
		 }
		 if(!this.env.getFrontWallDetected()){
			 this.listOrder.addOrder(Param.FORWARD);
		 }
	     else if(!this.env.getRightWallDetected()){
	    	 this.listOrder.addOrder(Param.TURNR);
		 }
		 else if(!this.env.getLeftWallDetected()){
			 this.listOrder.addOrder(Param.TURNL);
		 }
		 else {
			 this.listOrder.addOrder(Param.TURNB);
		 }
		 this.order = this.listOrder.getOrder();
	 }
	 
	 /**
	  * Méthode de test, permet d'executer n'importe quel ordre sans verifier les conditions initiales
	  */
	 private void chooseOrderInsecurely(){
		 if(!this.readyToGo) {
			 this.readyToGo=true;
		 }
		 this.order = this.listOrder.getOrder();
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
			this.env.saveCurrentCase(true);
		 }
		 else if(this.order==Param.SETPOSITION) {
			 this.env.setPosition();		 
		 }
		 else if(this.order==Param.CLEARLISTORDER) {
			 this.listOrder.clear();
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
		
		//this.listOrder.addOrder(Param.CALCOMPASS); 		// Fait depuis la com :D
		//this.listOrder.addOrder(Param.SAVEREFANGLE); 		// Fait depuis la com :D
		//this.listOrder.addOrder(Param.CHECKFIRSTCASE);	// Fait depuis la com :D
	 } 
	 
	 /**
	  * Fonction d'initialisation test
	  */
	 private void init2(){
		Sound.setVolume(Param.VOLUME);	
		this.mov.getDiffPilot().setTravelSpeed(Param.SPEED_CRUISE);
		this.mov.getDiffPilot().setRotateSpeed(Param.RSPEED_CRUISE);
		this.sonarMotor.setSpeed(Param.RSPEED_SONARMOTOR);

		this.env.setInitValues(10, 10, 0); 					// A faire depuis la com
		this.order=Param.SETPOSITION;			// A faire depuis la com
		this.executeOrder();
		this.order=Param.SAVEREFANGLE;
		this.executeOrder();
		this.order=Param.CHECKFIRSTCASE;
		this.executeOrder();		
	 }
	 
	 /**
	  * Main
	  * @param args
	  */
	 public static void main(String[] args) {
		/*Robot bob = new Robot();
		bob.go();		*/
		 
		Robot bot = new Robot();
		ThreadCom tCom = new ThreadCom(bot);
		
		bot.init();
		
		bot.start();
		tCom.start();
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
