package sensor;

import robot.*;
import lejos.nxt.I2CPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;

/**
 * Cette classe représente un sonar
 * @author Thomas
 * @see UltrasonicSensor
 */
public class Sonar extends UltrasonicSensor {	
	
	/**
	 * Attribut contenant un tableau d'échantillons de la boussole
	 * @see Param#TAB_NBDATA
	 */
	private double data[];
	
	/**
	 * Attribut contenant l'index du tableau d'échantillons
	 */
	private int idxData;
	
	/**
	 * Attribut contenant la moyenne des valeurs dans le tableau d'échantillons
	 */
	private double moyData;
	
	/**
	 * Constructeur de Sonar
	 * @param port
	 * 		Port auquel est branché le capteur
	 * @see I2CPort
	 */
	public Sonar (I2CPort port) {
		super(port);
		this.data = new double[Param.TAB_NBDATA];
		this.idxData = 0 ;
		this.moyData = 0 ;
	}
	 
	/**
	 * @return la valeur moyenne du tableau d'échantillons
	 */
	public double getMoyData() {
		return this.moyData ;
	}
	
	/**
	 * Acquisition d'un échantillon et mise a jour de la valeur moyenne
	 * @see Sonar#acquisition()
	 * @see Sonar#moyData
	 */
	public void refresh() {
		this.acquisition();
		this.moyData = this.moyenne();
	}	
	
	/**
	 * @return la moyenne du tableau d'échantillons
	 */
	private double moyenne(){
		int i;
		double sum = 0;
		for(i=0;i<Param.TAB_NBDATA;i++){
			sum+=this.data[i];
		}
		return (sum/Param.TAB_NBDATA);
	}
	 
	/**
	 * Acquisitionne une valeur et la place dans le tableau
	 * @see Sonar#data
	 * @see Sonar#idxData
	 */
	private void acquisition(){
		this.data[this.idxData] = (this.getRange()+Param.SONAR_OFFSET);
		this.idxData = (this.idxData+1)%Param.TAB_NBDATA;
	} 	 
}