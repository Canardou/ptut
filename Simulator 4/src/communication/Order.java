package communication;



public class Order {
	
	/**
	 * Constante pour d�terminer l'ordre stop
	 */
	public static final int STOP = 0;
	
	/**
	 * Constante pour d�terminer l'ordre qui demande au robot d'avancer � la case suivante
	 */
	public static final int FORWARD = 1;
	
	/**
	 * Constante pour d�terminer l'ordre qui demande au robot de tourner � gauche
	 */
	public static final int TURNL = 2;
	
	/**
	 * Constante pour d�terminer l'ordre qui demande au robot de tourner � droite
	 */
	public static final int TURNR = 3;
	
	/**
	 * Constante pour d�terminer l'ordre qui demande au robot de faire demi-tour
	 */
	public static final int TURNB = 4;	
	
	/**
	 * Constante pour d�terminer l'ordre qui demande la calibration de la boussole (2tours � faible vitesse)
	 */
	public static final int CALCOMPASS = 5;    
	
	/**
	 * Constante pour d�terminer l'ordre qui demande � enregistrer l'angle de r�f�rence
	 */
	public static final int SAVEREFANGLE = 6;
	
	/**
	 * Constante pour d�terminer l'ordre qui demande � verifier les 4 murs de la case initiale
	 */
	public static final int CHECKFIRSTCASE = 7;
	
	/**
	 * Constante pour d�terminer l'ordre qui impose la position initiale du robot
	 */
	public static final int SETPOSITION	= 8;
 	
	/**
	 * Ordre : supprimer tout les ordres pr�sents sur le robots et pas encore �xecut�s
	 */
	public static final int CLEARLISTORDER = 9;
	
	/**
	 * Ordre : attendre que l'utilisateur appuie sur le bouton ENTER du robot
	 */
	public static final int WAITBUTTON = 10;
	
	/**
	 * Ordre : attendre 1 seconde
	 */
	public static final int WAIT1SEC = 11;

	/**
	 * Ordre : attendre 1 seconde
	 */
	public static final int CASETOSEND = 12;
	
	public static final int NORMALMODE = 13;
	
	public static final int FASTMODE = 15;
	
	/**
	 * Ordre : Envoyer l'�tat du robot.</br> Attention cette ordre est execut�
	 * imm�diatement � sa reception.
	 */
	public static final int SENDBUSY = 16;
	/**
	 * Attribut repr�sentant l'ordre actuel du robot
	 * 
	 * @see Order#getOrder()
	 */
	private int currentOrder;

	

	/**
	 * Attribut permettant de connaitre l'erreur courante sur l'ordre (utile
	 * lors des v�rifications des conditions initiales)
	 */
	private int errOrder;

	/**
	 * Attribut permettant de savoir si le robot � rempli les conditions
	 * initiales pour commencer a se d�placer
	 */
	private boolean readyToGo;

	/**
	 * Attribut permettant de savoir si le robot � verifi� au moins une fois les
	 * 4 murs de sa case initiale
	 */
	private boolean checkFirstCaseDone;
	
	public static String printOrdre(int o) {
		if (o == STOP) {
			return "stop";
		} else if (o == FORWARD) {
			return "avancer";
		} else if (o == TURNR) {
			return "droite";
		} else if (o == TURNL) {
			return "gauche";
		} else if (o == TURNB) {
			return "demitour";
		} else if (o == CALCOMPASS) {
			return "cal boussole";
		} else if (o == SAVEREFANGLE) {
			return "sauv. ref";
		} else if (o == CHECKFIRSTCASE) {
			return "explo1case";
		} else if (o == CLEARLISTORDER) {
			return "vider ordres";
		} else if (o == WAITBUTTON) {
			return "wait bouton";
		} else if (o == WAIT1SEC) {
			return "wait 1sec";
		} else if (o == FASTMODE) {
			return "fast mode";
		} else if (o == NORMALMODE) {
			return "normal mode";
		} else if (o == SETPOSITION) {
			return "setPos";
		} else if (o == CASETOSEND) {
			return "sendcase";
		} else if (o == SENDBUSY) {
			return "sendbusy";
		} else {
			return String.valueOf(o);
		}
	}
	
	
}
