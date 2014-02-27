import lejos.nxt.Button;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.SensorPort;

public class Robot {	
		
     /* ATTRIBUT */
	 protected UltrasonicSensor sonar;	
	
	 /* CONSTRUCTEUR */
	 public Robot() {
		System.out.println("Create sonar");	
		this.sonar = new UltrasonicSensor(SensorPort.S1);
		System.out.println("Sonar created");
	 }
	
	/* MAIN */
	public static void main(String[] args) {
		System.out.println("Demarrage");	
		Robot derp = new Robot();
		while(!Button.ENTER.isDown()) {
				System.out.println(derp.sonar.getDistance()-4+"\n");
		}
	}
	
}