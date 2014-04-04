package sensor;
import robot.*;
import lejos.nxt.I2CPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassHTSensor;

public class Compass extends CompassHTSensor {	
	
	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	private 	Robot 	bot;
	private 	double 	data[];
	private 	int 	idxData;
	private 	double 	moyData;
	
	/**********************************************/
	/**                                          **/
	/**               CONSTRUCTEUR               **/
	/**                                          **/
	/**********************************************/
	public Compass (I2CPort port,Robot botinit) {
		super(port);
		this.bot 		= botinit;
		this.data		= new double[Param.TAB_NBDATA];
		this.idxData 	= 0 ;
		this.moyData 	= 0 ;
	}
	 
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/
	
	public double getMoyData() {
		return this.moyData ;
	}
	
	/** Calibre la boussole **/
	public void calibrate() {
		System.out.println("Cal boussole");
		this.bot.getMov().getDiffPilot().setRotateSpeed(Param.RSPEED_CAL);
		this.startCalibration() ;
		this.bot.getMov().getDiffPilot().rotate(Param.ANGLE_CAL);
		this.stopCalibration() ;
		this.bot.getMov().getDiffPilot().setRotateSpeed(Param.RSPEED_CRUISE);
		Sound.beepSequenceUp();
	}
	
	/** Acquisition d'un échantillon et mise a jour de la valeur moyenne **/
	public void refresh() {
		this.acquisition();
		this.moyData = this.moyenne();
	}
	
	/** Retourne la moyenne des 4 derniers échantillons du capteurs **/
	private double moyenne() {
		int i;
		double sum = 0;
		for(i=0;i<Param.TAB_NBDATA;i++){
			sum+=this.data[i];
		}
		return (sum/Param.TAB_NBDATA);
	}
	 
	/** Acquisitionne une valeur  et la place dans le tableau **/
	private void acquisition() {
		this.data[this.idxData] = this.getDegreesCartesian();
		this.idxData = (this.idxData+1)%Param.TAB_NBDATA;
	} 
}
