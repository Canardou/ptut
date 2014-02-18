import lejos.nxt.Button;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.SensorPort;

public class Robot {	
		
     /* ATTRIBUT */
	 protected CompassHTSensor compass;	
	
	 /* CONSTRUCTEUR */
	 public Robot() {
		System.out.println("Create compass");	
		this.compass = new CompassHTSensor(SensorPort.S4);
		System.out.println("Compass created");
	 }
	
	/* MAIN */
	public static void main(String[] args) {
		System.out.println("Demarrage");	
		Robot derp = new Robot();
		while(!Button.ESCAPE.isDown()) {
			//if(Button.ENTER.isDown()) {
				//System.out.println("Deg:" + derp.compass.getDegrees());
				System.out.println("Cart:" + derp.compass.getDegreesCartesian());
			//}
		}
	}
	
}