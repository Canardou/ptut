import lejos.nxt.I2CPort;
import lejos.nxt.UltrasonicSensor;

public class Sonar extends UltrasonicSensor {	
	
	/****************/
	/** CONSTANTES **/
	/****************/
	public static final int TAB_NBDATA = 3;
	
	/***************/
	/** ATTRIBUTS **/
	/***************/
	private double data[];
	private int idxData;
	protected double moyData;
	
	
	 /******************/
	 /** CONSTRUCTEUR **/
	 /******************/
	 public Sonar (I2CPort port) {
		 super(port);
		 this.data = new double[TAB_NBDATA];
		 this.idxData = 0 ;
		 this.moyData = 0 ;
	 }
	 
	 /**************/
	 /** METHODES **/
	 /**************/
	 
	 //Retourne la moyenne du tableau de valeurs
	 public double moyenne(){
		 int i;
		 double sum = 0;
		 for(i=0;i<TAB_NBDATA;i++){
			 sum+=this.data[i];
		 }
		 return (sum/TAB_NBDATA);
	 }
	 
	 //Acquisitionne une valeur de distance et la place dans le tableau
	 public void acquisition(){
		 this.data[this.idxData] = this.getRange();
		 this.idxData = (this.idxData+1)%TAB_NBDATA;
	 } 
	 
	 
}