package robot;

/**
 * Cette classe contient tout les param�tres necessaires au robot
 * @author Thomas
 */
public class Param {
	
	/**
	 * D�finit le rep�re du robot. D�finit l'axe des x positifs
	 */
	public static final int	XP = 0;
	
	/**
	 *D�finit le rep�re du robot. D�finit l'axe des x n�gatifs
	 */
	public static final int	XN = 2;
	
	/**
	 * D�finit le rep�re du robot. D�finit l'axe des y positifs
	 */
	public static final int	YP = 1;
	
	/**
	 * D�finit le rep�re du robot. D�finit l'axe des y n�gatifs
	 */
	public static final int YN = 3;
	
	/**
	 * Volume sonore du robot. De 0 � 100
	 */
	public static final int VOLUME = 10; 

	/**
	 * Diam�tre des roues du robot en cm
	 */
 	public static final double DIAMROUE = 5.6;
 	
 	/**
 	 * Distance inter-roues du robot en cm
 	 */
    public static final double DISTROUE = 11.5; 
    
    /**
     * Offset d'erreur des sonars en cm
     */
    public static final double SONAR_OFFSET = -4;
 	
	/**
	 * Distance unitaire inter-case du labyrinthe en cm
	 */
	public static final double UNIT_DIST = 30;
	
	/**
	 * Taille du tableau (nombre d'�chantillon dans le filtre) des capteurs en nombre d'�l�ment
	 */
	public static final int TAB_NBDATA = 5; 
	
	/**
	 * Angle � r�aliser pour faire les 2 tours de calibration de la boussole en degr�s
	 */
	public static final int ANGLE_CAL = 760; 
	
	/**
	 * Constante pour d�terminer le type de trame
	 */
	public static final int ORDRE = 5;
	
	/**
	 * Constante pour d�terminer le type de trame
	 */
	public static final int INITPOSITION = 2;
	
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
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur � gauche en cm
	 */
 	public static final double LIMLEFTWALL = 18;  
 	
	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur � droite en cm
	 */
 	public static final double LIMRIGHTWALL = 18;  
 	
	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur en face en cm
	 */
 	public static final double LIMFRONTWALL = 8;    
 	
	/**
	 * Limite en dessous de laquelle on consid�re qu'on est � proximit� d'un mur avant en cm
	 */
 	public static final double FRONTWALL_DANGER = 22; 

	/**
	 * Vitesse de croisi�re. De 0 � 36 cm/s
	 */
	public static final double SPEED_CRUISE = 16; 
	
	/**
	 * Vitesse � l'approche d'un mur avant. De 0 � 36 cm/s
	 */
	public static final double SPEED_DANGER = 12; 
	
	/**
	 * Vitesse de rotation normale. De 0 � 366 deg/s
	 */
	public static final double RSPEED_CRUISE = 70;   
	
	/**
	 * Vitesse de rotation lorsqu'on approche de l'angle voulu. De 0 � 366 deg/s
	 */
	public static final double RSPEED_DANGER = 25; 
	
	/**
	 * Vitesse de rotation lors de la calibration. De 0 � 366 deg/s
	 */
	public static final double RSPEED_CAL = 20; 
	
	/**
	 * Vitesse de rotation du moteur qui guide le sonar. De 0 � 366 deg/s
	 */
	public static final int RSPEED_SONARMOTOR= 270; 
	
	/**
	 * Angle relatif � l'objectif � partir duquel on r�duit la vitesse de rotation
	 */
	public static final double ANGLE_DANGER = 18;  
	
	/**
	 * Erreur permise sur la rotation en degr�s
	 */
	public static final double ANGLE_ERR = 1;   
	
	/**
	 * Hyst�r�sis sur la vitesse pour �viter les oscillations en vitesse qui perturbe la boussole
	 */
	public static final double ANGLE_HYST = 1; 
	
	/**
	 * Rayon du cercle pour r�guler fortement en cm.
	 * Lorsque le robot avance	
	 */
	public static final double ARC2 = 15;
	
	/**
	 * Erreur permise pour rester bien au milieu du couloir, lorsque les 2 murs lat�raux sont pr�sent
	 */
	public static final double REG_ERRDIST = 1.5; 
	
	/**
	 * Angle critique � ne pas d�passer lorqu'on avance en degr�s
	 */
	public static final double REG_ERRANGLEMAX = 25;  
	
	/**
	 * Erreur permise sur l'angle de r�gulation lorsqu'on avance
	 */
	public static final double REG_ERRANGLE = 5;
	
	/**
	 * Distance � partir de laquelle le robot va tourner pour s'approcher du mur droit en cm.
	 * Lorsqu'il n'y a que le mur droit de pr�sent.
	 */
	public static final double RIGHTWALL_MAX = 3.5; 
	
	/**
	 * Distance � partir de laquelle le robot va tourner pour s'approcher du mur  gauche en cm.
	 * Lorsqu'il n'y a que le mur gauche de pr�sent.
	 */
	public static final double LEFTWALL_MAX = 3;  

	/**
	 * Distance � partir de laquelle le robot va tourner pour s'�loigner du mur droit en cm.
	 * Lorsqu'il n'y a que le mur droit de pr�sent.
	 */
	public static final double RIGHTWALL_MIN = 3;  

	/**
	 * Distance � partir de laquelle le robot va tourner pour s'�loigner du mur gauche en cm.
	 * Lorsqu'il n'y a que le mur gauche de pr�sent.
	 */
	public static final double LEFTWALL_MIN = 2.5;  
	
	/**
	 * Rayon du cercle pour r�guler faiblement en cm.
	 * Lorsque le robot avance	
	 */
	public static final double ARC1 = 50; 
	
	/**
	 * Distance � partir de laquelle le sonar se tournera vers l'avant pour voir si un mur est pr�sent.
	 * Lorsque le robot avance	
	 */
	public static final double REG_CHECK = 25;  
	
	/**
	 * D�finit le type de r�gulation (r�gulation forte vers l� droite)
	 */
	public static final int LEFTREG = 0;
	
	/**
	 * D�finit le type de r�gulation (r�gulation faible vers l� droite)
	 */
	public static final int MIDLEFTREG = 1;
	
	/**
	 * D�finit le type de r�gulation (pas de r�gulation)
	 */
	public static final int NOREG = 2;
	
	/**
	 * D�finit le type de r�gulation (r�gulation faible vers l� gauche)
	 */
	public static final int MIDRIGHTREG = 3;
	
	/**
	 * D�finit le type de r�gulation (r�gulation forte vers l� gauche)
	 */
	public static final int RIGHTREG = 4;
}