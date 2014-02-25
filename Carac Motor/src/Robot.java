import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import java.lang.System;


public class Robot {	
		
     /* ATTRIBUT */
	 protected DifferentialPilot motor;	
	
	 /* CONSTRUCTEUR */
	 public Robot() {
		System.out.println("Create Motor");	
		this.motor = new DifferentialPilot(5.6, 11.5, Motor.A, Motor.B, false);
		System.out.println("Motor created");
	 }
	
	 public void pauseTime(int ms) {
		 long initTime = System.currentTimeMillis();
		 while((System.currentTimeMillis()-initTime)<ms) {
			 System.out.print(this.motor.getMovement().getDistanceTraveled()+"\n");
		 }
	 }
	 
	 
	/* MAIN */
	public static void main(String[] args) {
		System.out.println("Demarrage");	
		Robot derp = new Robot();
		Button.waitForAnyPress();
		derp.pauseTime(1000);
		derp.motor.reset();
		derp.motor.setTravelSpeed(15);
		derp.motor.arcForward(5);
		derp.pauseTime(4000);
		derp.motor.forward();
		derp.pauseTime(4000);
		derp.motor.setTravelSpeed(35);
		derp.motor.forward();
		derp.pauseTime(4000);
	}
	
}
