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
		 while((System.currentTimeMillis()-initTime)<ms);
	 }
	 
	 
	/* MAIN */
	public static void main(String[] args) {
		System.out.println("Demarrage");	
		Robot derp = new Robot();
		Button.waitForAnyPress();
		derp.pauseTime(1000);
		derp.motor.reset();
		derp.motor.forward();
		while(true) {		
			System.out.print(derp.motor.getMovement().getDistanceTraveled()+"\n");
			derp.pauseTime(500);
			if(Button.ENTER.isDown()) {
				derp.motor.setTravelSpeed(4);
			}
			
		}
	}
	
}
