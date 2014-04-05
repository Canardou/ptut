package sensor;
import robot.*;
import lejos.nxt.I2CPort;
import lejos.nxt.addon.CompassHTSensor;

/**
 * Cette classe repr�sente une boussole
 * @author Thomas
 * @see CompassHTSensor
 */
public class Compass extends CompassHTSensor {	
	
	/**
	 * Attribut repr�sentant le robot
	 * @see Robot
	 */
	private 	Robot 	bot;
	
	/**
	 * Attribut contenant un tableau d'�chantillons de la boussole
	 * @see Param#TAB_NBDATA
	 */
	private 	double 	data[];
	
	/**
	 * Attribut contenant l'index du tableau d'�chantillons
	 */
	private 	int 	idxData;
	
	/**
	 * Attribut contenant la moyenne des valeurs dans le tableau d'�chantillons
	 */
	private 	double 	moyData;
	
	/**
	 * Attribut indiquant si la calibration a �t� faite au moins une fois
	 */
	private		boolean calDone;
	
	/**
	 * Constructeur de Compass
	 * @param port
	 * 		Port auquel le capteur est branch�
	 * @param botinit
	 * 		Objet robot
	 * @see Robot
	 * @see I2CPort
	 */
	public Compass (I2CPort port,Robot botinit) {
		super(port);
		this.bot 		= botinit;
		this.data		= new double[Param.TAB_NBDATA];
		this.idxData 	= 0 ;
		this.moyData 	= 0 ;
		this.calDone	= false;
	}
	 
	/**
	 * @return la valeur moyenne du tableau d'�chantillons
	 */
	public double getMoyData() {
		return this.moyData ;
	}
	
	/**
	 * R�alise la calibration de la boussole. Met � jour l'attribut calDone
	 * @see Compass#calDone
	 */
	public void calibrate() {
		System.out.println("Cal compass");
		this.bot.getMov().getDiffPilot().setRotateSpeed(Param.RSPEED_CAL);
		this.startCalibration() ;
		this.bot.getMov().getDiffPilot().rotate(Param.ANGLE_CAL);
		this.stopCalibration() ;
		this.bot.getMov().getDiffPilot().setRotateSpeed(Param.RSPEED_CRUISE);
		this.calDone=true;		
	}
	
	/**
	 * Acquisition d'un �chantillon et mise a jour de la valeur moyenne
	 * @see Compass#acquisition()
	 * @see Compass#moyData
	 */
	public void refresh() {
		this.acquisition();
		this.moyData = this.moyenne();
	}
	
	/**
	 * @return la moyenne du tableau d'�chantillons
	 */
	private double moyenne() {
		int i;
		double sum = 0;
		for(i=0;i<Param.TAB_NBDATA;i++){
			sum+=this.data[i];
		}
		return (sum/Param.TAB_NBDATA);
	}
	 
	/**
	 * Acquisitionne une valeur et la place dans le tableau
	 * @see Compass#data
	 * @see Compass#idxData
	 */
	private void acquisition() {
		this.data[this.idxData] = this.getDegreesCartesian();
		this.idxData = (this.idxData+1)%Param.TAB_NBDATA;
	} 
	
	/**
	 * @return la valeur de la variable calDone
	 * @see Compass#calDone
	 */
	public boolean getCalDone() {
		return this.calDone;
	}
}
