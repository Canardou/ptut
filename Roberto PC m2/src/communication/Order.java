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
	

}
