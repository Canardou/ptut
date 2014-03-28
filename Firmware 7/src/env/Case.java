package env;

public class Case {	
		
	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	private double x ;
	private double y ;
	private boolean wallXP;
	private boolean wallXN;
	private boolean wallYP;
	private boolean wallYN;
		
	/**********************************************/
	/**                                          **/
	/**               CONSTRUCTEUR               **/
	/**                                          **/
	/**********************************************/
	public Case (double xi, double yi, boolean wallXPi, boolean wallYPi, boolean wallXNi, boolean wallYNi) {
		this.x 		= xi;
		this.y		= yi;
		this.wallXP = wallXPi;
		this.wallXN = wallXNi;
		this.wallYP = wallYPi;
		this.wallYN = wallYNi;
	}
	
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/
	
}