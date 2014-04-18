package sensors;

import env.*;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 * Cette classe repr�sente un capteur de luminosité
 * @author Clément
 * @see LihghtSensor
 */
public class Light extends LightSensor {	
	
	
	/**
	 * Attribut contenant un tableau d'�chantillons du capteur le lumière
	 * @see Param#TAB_NBDATA
	 */
	private double data[];
	
	/**
	 * Attribut contenant l'index du tableau d'�chantillons
	 */
	private int idxData;
	
	/**
	 * Attribut contenant la moyenne des valeurs dans le tableau d'�chantillons
	 */
	private double moyData;
	
	/**
	 * Constructeur de Light
	 * @param port
	 * 		Port auquel est branch� le capteur
	 * @see SensorPort
	 */
	public Light (SensorPort port) {
		super(port);
		this.data = new double[Environment.TAB_NBDATA];
		this.idxData = 0 ;
		this.moyData = 0 ;
	}
	 
	/**
	 * @return la valeur moyenne du tableau d'�chantillons
	 */
	public double getMoyData() {
		return this.moyData ;
	}
	
	/**
	 * Acquisition d'un �chantillon et mise a jour de la valeur moyenne
	 * @see Light#acquisition()
	 * @see Light#moyData
	 */
	public void refresh() {
		this.acquisition();
		this.moyData = this.moyenne();
		 LCD.drawInt((int)this.moyData,4, 0, 1);
	}	
	
	/**
	 * @return la moyenne du tableau d'�chantillons
	 */
	private double moyenne(){
		int i;
		double sum = 0;
		for(i=0;i<Environment.TAB_NBDATA;i++){
			sum+=this.data[i];
		}
		return (sum/Environment.TAB_NBDATA);
	}
	 
	/**
	 * Acquiert une valeur et la place dans le tableau
	 * @see Light#data
	 * @see Light#idxData
	 */
	private void acquisition(){
		this.data[this.idxData] = this.getNormalizedLightValue();
		this.idxData = (this.idxData+1)%Environment.TAB_NBDATA;
	} 	 
	
	
}