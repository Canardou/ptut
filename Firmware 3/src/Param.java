public class Param {
	
	// Paramètres du système
	public static final int    VOLUME           = 20;   // (0-100) Volume du son
	
	 // Caractéristiques du robot
 	public static final double DIAMROUE    		= 5.6;  // (cm) Diamètre des roues
    public static final double DISTROUE    		= 11.5; // (cm) Distance entre les roues
    public static final double SONAR_OFFSET      = -3;  // (cm) Offset du sonar 
 	
	// Paramètres de la carte
	public static final double UNIT_DIST	 	= 30;   // (cm) Longueur d'une case
	
	// Capteurs
	public static final int    TAB_NBDATA       = 2;    // Nombre d'échantillon dans le filtre des capteurs
	public static final int    ANGLE_CAL        = 760;  // (degrés) Angle à faire pour calibrer la boussole (2tours)
	
	 // Ordres
	public static final int    STOP     		= 0;
	public static final int    FORWARD  		= 1;    // Avancer de 30cm
	public static final int    TURNL    		= 2;    // Tourner à gauche de 90°
	public static final int    TURNR    		= 3;    // Tourner à droite de 90°
	public static final int    TURNB    		= 4;	// Demi-tour 
	public static final int    BACKWARD 		= 6;    // Reculer
	
 	// Limites
 	public static final double LIMFRONTWALL		= 11;   // (cm) Limite en dessous de laquelle on considère qu'il y a un mur juste devant et qu'on ne doit pas s'approcher plus
 	public static final double FRONTWALL_DANGER = 20;   // (cm) Distance à partir de laquelle la vitesse est réduite (approche d'un mur avant)
 	public static final double FRONTWALL_HYST   = 1 ;   // (cm) Hysteresis lors de la réduction de la vitesse quand on est à FRONTWALL_DANGER
 	public static final double LIMRIGHTWALL 	= 25;   // (cm) Limite en dessous de laquelle on considère qu'il y a un mur juste à droite
	
	// Paramètres de vitesse
	public static final double SPEED_CRUISE		= 20;   // (cm/s) (0-36) Vitesse de croisère (38 max)
	public static final double SPEED_DANGER		= 14;   // (cm/s) (0-36) Vitesse à l'approche d'un mur avant
	public static final double RSPEED_CRUISE 	= 70;   // (degrés/s) (0-366) Vitesse de rotation classique
	public static final double RSPEED_DANGER	= 30;   // (degrés/s) (0-366) Vitesse de rotation lorsqu'on approche de l'angle désiré
	public static final double RSPEED_CAL	    = 20;   // (degrés/s) (0-366) Vitesse de rotation lors de la calibration de la boussole
	
	// Paramètres des virages
	public static final double ANGLE_DANGER		= 15;   // (degrés) Angle relatif à l'objectif à partir duquel la vitesse de rotation est réduite
	public static final double ANGLE_ERR		= 2;    // (degrés) Erreur tolérée sur l'angle (angledésiré +- ANGLE_ERR)
	public static final double ANGLE_HYST       = 1;    // (degrés) Hysteresis lors de la réduction de la vitesse de rotation quand on est à ANGLE_DANGER

	// Paramètres pour longer le mur droit
	public static final double RIGHTWALL_MIN 	= 2;    // (cm) Distance à partir de laquelle le robot va tourner pour s'écarter du mur
	public static final double RIGHTWALL_MAX 	= 8;    // (cm) Distance à partir de laquelle le robot va tourner pour s'approcher du mur
	public static final double RIGHTWALL_ANGLE 	= 4;    // (degrés) Erreur permise sur l'angle de régulation
	public static final double RIGHTWALL_CORR   = 6;    // (degrés) Si on est trop proche/loin on remet le robot droit et en plus on tourne de cet angle pour revenir au milieu du couloir
	public static final double RIGHTWALL_REG	= 8;    // (cm) Fréquence des régulations par rapport au mur droit
	
	// Type de régulation
	public static final int	   NOREG			= 0;
	public static final int    LEFTREG			= 1;
	public static final int    RIGHTREG			= 2;
	public static final int    MIDREG			= 3;
	
}