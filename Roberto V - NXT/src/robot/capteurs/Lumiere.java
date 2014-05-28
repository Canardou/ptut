package robot.capteurs;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 * Cette classe représente le capteur de lumière. 
 * 
 * @author Clément
 */
public class Lumiere extends LightSensor {	
	
	// ------------------------------------- ATTRIBUTS --------------------------------------------
	
	/**
	 * Tableau d'échantillons des données issues de la boussole.
	 */
	private double data[];
	
	/**
	 * Index du tableau d'échantillons.
	 */
	private int idxData;
	
	/**
	 * Moyenne des valeurs dans le tableau d'échantillons.
	 */
	private double moyData;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------
	
	/**
	 * Constructeur du capteur Lumière.
	 * @param port
	 */
	public Lumiere (SensorPort port) {
		super(port);
		this.data = new double[Capteurs.TAB_NBDATA];
		this.idxData = 0 ;
		this.moyData = 0 ;
	}
	
	// ------------------------------------- METHODES ---------------------------------------------
	
	/**
	 * Acquisition d'un échantillon (dans le tableau) et mise a jour de la
	 * valeur moyenne.
	 */
	public void rafraichir() {
		this.acquisition();
		this.moyData = this.moyenne();
	}	
	
	/**
	 * @return la moyenne du tableau d'échantillons.
	 */
	private double moyenne(){
		int i;
		double sum = 0;
		for(i=0;i<Capteurs.TAB_NBDATA;i++){
			sum+=this.data[i];
		}
		return (sum/Capteurs.TAB_NBDATA);
	}
	 
	/**
	 * Acquiert une valeur et la place dans le tableau.
	 */
	private void acquisition(){
		this.data[this.idxData] = this.getNormalizedLightValue();
		this.idxData = (this.idxData+1)%Capteurs.TAB_NBDATA;
	} 	 
	
	// ------------------------------------- GETTERS -----------------------------------------------
	
	/**
	 * Methode à utiliser pour lire la valeur renvoyée par le capteur.
	 * 
	 * @return la valeur moyenne du tableau d'échantillons.
	 */
	public double getMoyData() {
		return this.moyData ;
	}	
}