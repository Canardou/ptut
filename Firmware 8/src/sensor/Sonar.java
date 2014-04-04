package sensor;
import robot.*;
import lejos.nxt.I2CPort;
import lejos.nxt.UltrasonicSensor;

public class Sonar extends UltrasonicSensor {	
	
	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	private double data[];
	private int idxData;
	private double moyData;
	
	/**********************************************/
	/**                                          **/
	/**               CONSTRUCTEUR               **/
	/**                                          **/
	/**********************************************/
	public Sonar (I2CPort port) {
		super(port);
		this.data = new double[Param.TAB_NBDATA];
		this.idxData = 0 ;
		this.moyData = 0 ;
	}
	 
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/	
	
	public double getMoyData() {
		return this.moyData ;
	}
	
	/** Acquisition d'un échantillon et mise a jour de la valeur moyenne **/
	public void refresh() {
		this.acquisition();
		this.moyData = this.moyenne();
	}	
	
	/** Retourne la moyenne des 4 derniers échantillons du capteurs **/
	private double moyenne(){
		int i;
		double sum = 0;
		for(i=0;i<Param.TAB_NBDATA;i++){
			sum+=this.data[i];
		}
		return (sum/Param.TAB_NBDATA);
	}
	 
	/** Acquisitionne une valeur de distance et la place dans le tableau **/
	private void acquisition(){
		this.data[this.idxData] = (this.getRange()+Param.SONAR_OFFSET);
		this.idxData = (this.idxData+1)%Param.TAB_NBDATA;
	} 	 
}