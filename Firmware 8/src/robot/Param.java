package robot;

/**
 * Cette classe contient tout les paramètres necessaires au robot
 * @author Thomas
 */
public class Param {
	
	/**
	 * Définit le repère du robot. Définit l'axe des x positifs
	 */
	public static final int	XP = 0;
	
	/**
	 *Définit le repère du robot. Définit l'axe des x négatifs
	 */
	public static final int	XN = 2;
	
	/**
	 * Définit le repère du robot. Définit l'axe des y positifs
	 */
	public static final int	YP = 1;
	
	/**
	 * Définit le repère du robot. Définit l'axe des y négatifs
	 */
	public static final int YN = 3;
	
	/**
	 * Volume sonore du robot. De 0 à 100
	 */
	public static final int VOLUME = 10; 

	/**
	 * Diamètre des roues du robot en cm
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
	 * Taille du tableau (nombre d'échantillon dans le filtre) des capteurs en nombre d'élément
	 */
	public static final int TAB_NBDATA = 5; 
	
	/**
	 * Angle à réaliser pour faire les 2 tours de calibration de la boussole en degrés
	 */
	public static final int ANGLE_CAL = 760; 
	
	/**
	 * Constante pour déterminer le type de trame
	 */
	public static final int ORDRE = 5;
	
	/**
	 * Constante pour déterminer le type de trame
	 */
	public static final int INITPOSITION = 2;
	
	/**
	 * Constante pour déterminer l'ordre stop
	 */
	public static final int STOP = 0;
	
	/**
	 * Constante pour déterminer l'ordre qui demande au robot d'avancer à la case suivante
	 */
	public static final int FORWARD = 1;
	
	/**
	 * Constante pour déterminer l'ordre qui demande au robot de tourner à gauche
	 */
	public static final int TURNL = 2;
	
	/**
	 * Constante pour déterminer l'ordre qui demande au robot de tourner à droite
	 */
	public static final int TURNR = 3;
	
	/**
	 * Constante pour déterminer l'ordre qui demande au robot de faire demi-tour
	 */
	public static final int TURNB = 4;	
	
	/**
	 * Constante pour déterminer l'ordre qui demande la calibration de la boussole (2tours à faible vitesse)
	 */
	public static final int CALCOMPASS = 5;    
	
	/**
	 * Constante pour déterminer l'ordre qui demande à enregistrer l'angle de référence
	 */
	public static final int SAVEREFANGLE = 6;
	
	/**
	 * Constante pour déterminer l'ordre qui demande à verifier les 4 murs de la case initiale
	 */
	public static final int CHECKFIRSTCASE = 7;
	
	/**
	 * Constante pour déterminer l'ordre qui impose la position initiale du robot
	 */
	public static final int SETPOSITION	= 8;
 	
	/**
	 * Ordre : supprimer tout les ordres présents sur le robots et pas encore éxecutés
	 */
	public static final int CLEARLISTORDER = 9;
	
	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur à gauche en cm
	 */
 	public static final double LIMLEFTWALL = 18;  
 	
	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur à droite en cm
	 */
 	public static final double LIMRIGHTWALL = 18;  
 	
	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur en face en cm
	 */
 	public static final double LIMFRONTWALL = 8;    
 	
	/**
	 * Limite en dessous de laquelle on considère qu'on est à proximité d'un mur avant en cm
	 */
 	public static final double FRONTWALL_DANGER = 22; 

	/**
	 * Vitesse de croisière. De 0 à 36 cm/s
	 */
	public static final double SPEED_CRUISE = 16; 
	
	/**
	 * Vitesse à l'approche d'un mur avant. De 0 à 36 cm/s
	 */
	public static final double SPEED_DANGER = 12; 
	
	/**
	 * Vitesse de rotation normale. De 0 à 366 deg/s
	 */
	public static final double RSPEED_CRUISE = 70;   
	
	/**
	 * Vitesse de rotation lorsqu'on approche de l'angle voulu. De 0 à 366 deg/s
	 */
	public static final double RSPEED_DANGER = 25; 
	
	/**
	 * Vitesse de rotation lors de la calibration. De 0 à 366 deg/s
	 */
	public static final double RSPEED_CAL = 20; 
	
	/**
	 * Vitesse de rotation du moteur qui guide le sonar. De 0 à 366 deg/s
	 */
	public static final int RSPEED_SONARMOTOR= 270; 
	
	/**
	 * Angle relatif à l'objectif à partir duquel on réduit la vitesse de rotation
	 */
	public static final double ANGLE_DANGER = 18;  
	
	/**
	 * Erreur permise sur la rotation en degrés
	 */
	public static final double ANGLE_ERR = 1;   
	
	/**
	 * Hystérésis sur la vitesse pour éviter les oscillations en vitesse qui perturbe la boussole
	 */
	public static final double ANGLE_HYST = 1; 
	
	/**
	 * Rayon du cercle pour réguler fortement en cm.
	 * Lorsque le robot avance	
	 */
	public static final double ARC2 = 15;
	
	/**
	 * Erreur permise pour rester bien au milieu du couloir, lorsque les 2 murs latéraux sont présent
	 */
	public static final double REG_ERRDIST = 1.5; 
	
	/**
	 * Angle critique à ne pas dépasser lorqu'on avance en degrés
	 */
	public static final double REG_ERRANGLEMAX = 25;  
	
	/**
	 * Erreur permise sur l'angle de régulation lorsqu'on avance
	 */
	public static final double REG_ERRANGLE = 5;
	
	/**
	 * Distance à partir de laquelle le robot va tourner pour s'approcher du mur droit en cm.
	 * Lorsqu'il n'y a que le mur droit de présent.
	 */
	public static final double RIGHTWALL_MAX = 3.5; 
	
	/**
	 * Distance à partir de laquelle le robot va tourner pour s'approcher du mur  gauche en cm.
	 * Lorsqu'il n'y a que le mur gauche de présent.
	 */
	public static final double LEFTWALL_MAX = 3;  

	/**
	 * Distance à partir de laquelle le robot va tourner pour s'éloigner du mur droit en cm.
	 * Lorsqu'il n'y a que le mur droit de présent.
	 */
	public static final double RIGHTWALL_MIN = 3;  

	/**
	 * Distance à partir de laquelle le robot va tourner pour s'éloigner du mur gauche en cm.
	 * Lorsqu'il n'y a que le mur gauche de présent.
	 */
	public static final double LEFTWALL_MIN = 2.5;  
	
	/**
	 * Rayon du cercle pour réguler faiblement en cm.
	 * Lorsque le robot avance	
	 */
	public static final double ARC1 = 50; 
	
	/**
	 * Distance à partir de laquelle le sonar se tournera vers l'avant pour voir si un mur est présent.
	 * Lorsque le robot avance	
	 */
	public static final double REG_CHECK = 25;  
	
	/**
	 * Définit le type de régulation (régulation forte vers là droite)
	 */
	public static final int LEFTREG = 0;
	
	/**
	 * Définit le type de régulation (régulation faible vers là droite)
	 */
	public static final int MIDLEFTREG = 1;
	
	/**
	 * Définit le type de régulation (pas de régulation)
	 */
	public static final int NOREG = 2;
	
	/**
	 * Définit le type de régulation (régulation faible vers là gauche)
	 */
	public static final int MIDRIGHTREG = 3;
	
	/**
	 * Définit le type de régulation (régulation forte vers là gauche)
	 */
	public static final int RIGHTREG = 4;
}