package robot;
public class Param {
	
	// Carte
	public static final int    INITX            = 10;
	public static final int    INITY            = 10;
	
	// Direction
	public static final int	   XP				= 0;    // Selon les X positifs
	public static final int	   XN  				= 2;    // Selon les X negatifs
	public static final int	   YP				= 1;    // Selon les Y positifs
	public static final int	   YN		    	= 3;    // Selon les Y negatifs
	
	// Paramètres du système
	public static final int    VOLUME           = 60;   // (0-100) Volume du son
	
	// Caractéristiques du robot
 	public static final double DIAMROUE    		= 5.6;  // (cm) Diamètre des roues
    public static final double DISTROUE    		= 11.5; // (cm) Distance entre les roues
    public static final double SONAR_OFFSET     = -4;   // (cm) Offset du sonar 
 	
	// Paramètres de la carte
	public static final double UNIT_DIST	 	= 30;   // (cm) Longueur d'une case
	
	// Capteurs
	public static final int    TAB_NBDATA       = 5;    // Nombre d'échantillon dans le filtre des capteurs
	public static final int    ANGLE_CAL        = 760;  // (degrés) Angle à faire pour calibrer la boussole (2tours)
	
	// Ordres
	public static final int    STOP     		= 0;
	public static final int    FORWARD  		= 1;    // Avancer de 30cm
	public static final int    TURNL    		= 2;    // Tourner à gauche de 90°
	public static final int    TURNR    		= 3;    // Tourner à droite de 90°
	public static final int    TURNB    		= 4;	// Demi-tour 
	public static final int    BACKWARD 		= 6;    // Reculer
	
 	// Limites
 	public static final double LIMLEFTWALL 		= 18;   // (cm) Limite en dessous de laquelle on considère qu'il y a un mur juste à droite
 	public static final double LIMRIGHTWALL 	= 18;   // (cm) Limite en dessous de laquelle on considère qu'il y a un mur juste à droite
 	public static final double LIMFRONTWALL		= 8;    // (cm) Limite en dessous de laquelle on considère qu'il y a un mur juste devant et qu'on ne doit pas s'approcher plus
 	public static final double FRONTWALL_DANGER = 22;   // (cm) 
 	public static final double FRONTWALL_HYST   = 1 ;   // (cm) Hysteresis lors de la réduction de la vitesse quand on est à FRONTWALL_DANGER
 	
	// Paramètres de vitesse
	public static final double SPEED_CRUISE		= 16;   // (cm/s) (0-36) Vitesse de croisère (38 max)
	public static final double SPEED_DANGER		= 12;   // (cm/s) (0-36) Vitesse à l'approche d'un mur avant
	public static final double RSPEED_CRUISE 	= 70;   // (degrés/s) (0-366) Vitesse de rotation classique
	public static final double RSPEED_DANGER	= 25;   // (degrés/s) (0-366) Vitesse de rotation lorsqu'on approche de l'angle désiré
	public static final double RSPEED_CAL	    = 20;   // (degrés/s) (0-366) Vitesse de rotation lors de la calibration de la boussole
	public static final int    RSPEED_SONARMOTOR= 270;  // (degrés/s) Vitesse de rotation du moteur portant le sonar
	
	// Paramètres des virages
	public static final double ANGLE_DANGER		= 18;   // (degrés) Angle relatif à l'objectif à partir duquel la vitesse de rotation est réduite
	public static final double ANGLE_ERR		= 1;    // (degrés) Erreur tolérée sur l'angle (angledésiré +- ANGLE_ERR)
	public static final double ANGLE_HYST       = 1;    // (degrés) Hysteresis lors de la réduction de la vitesse de rotation quand on est à ANGLE_DANGER
	
	// Paramètres pour la forte régulation
	public static final double RIGHTWALL_ARC2   = 15;   // (cm) Rayon du cercle à suivre pour réguler fortement
	public static final double REG_ERRDIST		= 1.5;  // (cm) erreur permise pour etre bien au milieu des 2 murs
	public static final double REG_ERRANGLEMAX  = 25;   // (degrés) Angle critique à partir duquel l'erreur est considérée trop grande et que l'on doit absolument réguler
	public static final double REG_ERRANGLE		= 5;    // (degrés) Erreur permise sur l'angle de régulation
	public static final double RIGHTWALL_MAX 	= 3.5;  // (cm) Distance à partir de laquelle le robot va tourner pour s'approcher du mur
	public static final double LEFTWALL_MAX 	= 3;    // (cm) Distance à partir de laquelle le robot va tourner pour s'approcher du mur
	
	// Paramètres pour la faible régulation
	public static final double RIGHTWALL_MIN 	= 3;    // (cm) Distance à partir de laquelle le robot va tourner pour s'écarter du mur
	public static final double LEFTWALL_MIN 	= 2.5;  // (cm) Distance à partir de laquelle le robot va tourner pour s'écarter du mur
	public static final double RIGHTWALL_ARC1   = 50;   // (cm) Rayon du cercle à suivre pour réguler faiblement
	public static final double RIGHTWALL_MID    = 0;    // (cm) Distance permise par rapport au milieu du couloir
	
	public static final double REG_CHECK        = 25;   // (cm) Distance aprés laquelle on vérifie qu'on a un mur devant
	
	// Type de régulation
	public static final int    LEFTREG			= -2;
	public static final int    MIDLEFTREG       = -1;
	public static final int	   NOREG			= 0;
	public static final int    MIDRIGHTREG      = 1;
	public static final int    RIGHTREG			= 2;
	
	public static final int    MIDREG			= 3;	
	public static final int    ROTREG			= 4;
	
}