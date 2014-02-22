public class Param {
	
	// Param�tres du syst�me
	public static final int    VOLUME           = 20;   // (0-100) Volume du son
	
	 // Caract�ristiques du robot
 	public static final double DIAMROUE    		= 5.6;  // (cm) Diam�tre des roues
    public static final double DISTROUE    		= 11.5; // (cm) Distance entre les roues
    public static final double SONAR_OFFSET      = -3;  // (cm) Offset du sonar 
 	
	// Param�tres de la carte
	public static final double UNIT_DIST	 	= 30;   // (cm) Longueur d'une case
	
	// Capteurs
	public static final int    TAB_NBDATA       = 2;    // Nombre d'�chantillon dans le filtre des capteurs
	public static final int    ANGLE_CAL        = 760;  // (degr�s) Angle � faire pour calibrer la boussole (2tours)
	
	 // Ordres
	public static final int    STOP     		= 0;
	public static final int    FORWARD  		= 1;    // Avancer de 30cm
	public static final int    TURNL    		= 2;    // Tourner � gauche de 90�
	public static final int    TURNR    		= 3;    // Tourner � droite de 90�
	public static final int    TURNB    		= 4;	// Demi-tour 
	public static final int    BACKWARD 		= 6;    // Reculer
	
 	// Limites
 	public static final double LIMFRONTWALL		= 11;   // (cm) Limite en dessous de laquelle on consid�re qu'il y a un mur juste devant et qu'on ne doit pas s'approcher plus
 	public static final double FRONTWALL_DANGER = 20;   // (cm) Distance � partir de laquelle la vitesse est r�duite (approche d'un mur avant)
 	public static final double FRONTWALL_HYST   = 1 ;   // (cm) Hysteresis lors de la r�duction de la vitesse quand on est � FRONTWALL_DANGER
 	public static final double LIMRIGHTWALL 	= 25;   // (cm) Limite en dessous de laquelle on consid�re qu'il y a un mur juste � droite
	
	// Param�tres de vitesse
	public static final double SPEED_CRUISE		= 20;   // (cm/s) (0-36) Vitesse de crois�re (38 max)
	public static final double SPEED_DANGER		= 14;   // (cm/s) (0-36) Vitesse � l'approche d'un mur avant
	public static final double RSPEED_CRUISE 	= 70;   // (degr�s/s) (0-366) Vitesse de rotation classique
	public static final double RSPEED_DANGER	= 30;   // (degr�s/s) (0-366) Vitesse de rotation lorsqu'on approche de l'angle d�sir�
	public static final double RSPEED_CAL	    = 20;   // (degr�s/s) (0-366) Vitesse de rotation lors de la calibration de la boussole
	
	// Param�tres des virages
	public static final double ANGLE_DANGER		= 15;   // (degr�s) Angle relatif � l'objectif � partir duquel la vitesse de rotation est r�duite
	public static final double ANGLE_ERR		= 2;    // (degr�s) Erreur tol�r�e sur l'angle (angled�sir� +- ANGLE_ERR)
	public static final double ANGLE_HYST       = 1;    // (degr�s) Hysteresis lors de la r�duction de la vitesse de rotation quand on est � ANGLE_DANGER

	// Param�tres pour longer le mur droit
	public static final double RIGHTWALL_MIN 	= 2;    // (cm) Distance � partir de laquelle le robot va tourner pour s'�carter du mur
	public static final double RIGHTWALL_MAX 	= 8;    // (cm) Distance � partir de laquelle le robot va tourner pour s'approcher du mur
	public static final double RIGHTWALL_ANGLE 	= 4;    // (degr�s) Erreur permise sur l'angle de r�gulation
	public static final double RIGHTWALL_CORR   = 6;    // (degr�s) Si on est trop proche/loin on remet le robot droit et en plus on tourne de cet angle pour revenir au milieu du couloir
	public static final double RIGHTWALL_REG	= 8;    // (cm) Fr�quence des r�gulations par rapport au mur droit
	
	// Type de r�gulation
	public static final int	   NOREG			= 0;
	public static final int    LEFTREG			= 1;
	public static final int    RIGHTREG			= 2;
	public static final int    MIDREG			= 3;
	
}