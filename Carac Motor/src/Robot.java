import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot {	
		
     /* ATTRIBUT */
	 protected DifferentialPilot motor;	
	
	 /* CONSTRUCTEUR */
	 public Robot() {
		System.out.println("Create Motor");	
		this.motor = new DifferentialPilot(5.6, 11.5, Motor.A, Motor.B, false);
		System.out.println("Motor created");
	 }
	
	/* MAIN */
	public static void main(String[] args) {
		System.out.println("Demarrage");	
		Robot derp = new Robot();
		Button.waitForAnyPress();
		derp.motor.travel(100);
		derp.motor.rotate(90);
		derp.motor.travel(50);
		derp.motor.rotate(90);
		derp.motor.travel(100);
		derp.motor.rotate(90);
		derp.motor.travel(50);
	}
	
}
