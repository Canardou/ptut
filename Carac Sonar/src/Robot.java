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
		while(!Button.ESCAPE.isDown()) {
			if(Button.ENTER.isDown()) {
				System.out.println("Dist:" + derp.sonar.getDistance() + derp.sonar.getUnits());
				System.out.println("Range:" + derp.sonar.getRange() + derp.sonar.getUnits());
			}
		}
	}
	
}