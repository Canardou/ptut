package env;

import robot.Param;
import robot.Robot;

/**
 * Environment est la classe permettant de décrire l'environnement du robot. 
 * Cette classe contient les variables qui indiquent la présence eventuelle 
 * des 4 murs autour du robot. Elle contient aussi les coordonées du robot, 
 * ainsi que sa direction, et leurs valeurs initiales (qui doivent être récues
 * du superviseur). On trouve aussi une pile de cases explorées qui doit être 
 * régulierement envoyée au superviseur
 * @author Thomas
 */
public class Environment {	
	
	/**
	 * Attribut contenant l'objet robot
	 * @see Robot
	 */
	private	Robot bot ;
	 
	/**
	 * Attribut contenant l'information sur la présence d'un mur à l'avant du robot
	 * Attention cette valeur peut-être incertaine durant l'execution d'un mouvement
	 * @see Environment#getFrontWallDetected()
	 * @see Environment#setFrontWallDetected()
	 */
	private boolean frontWallDetected; 
	
	/**
	 * Attribut contenant l'information sur la présence d'un mur à droite du robot
	 * @see Environment#getRightWallDetected()
	 * @see Environment#setRightWallDetected()
	 */
	private boolean rightWallDetected; 
	
	/**
	 * Attribut contenant l'information sur la présence d'un mur à gauche du robot
	 * Attention cette valeur peut-être incertaine durant l'execution d'un mouvement
	 * @see Environment#getLeftWallDetected()
	 * @see Environment#setLeftWallDetected()
	 */
	private boolean leftWallDetected;  
	
	/**
	 * Attribut contenant l'information sur la présence d'un mur à l'arrière du robot
	 * Attention cette valeur peut-être incertaine durant l'execution d'un mouvement
	 * @see Environment#getBackWallDetected()
	 * @see Environment#setBackWallDetected()
	 */
	private boolean backWallDetected;
	
	/**
	 * Coordonée x du robot
	 * @see Environment#setPosition()
	 */
	private int x;
	
	/**
	 * Coordonée y du robot
	 * @see Environment#setPosition()
	 */
	private int y;
	
	/**
	 * Direction du robot
	 * @see Environment#setPosition()
	 * @see Param#XP
	 * @see Param#YP
	 * @see Param#XN
	 * @see Param#YN
	 */
	private int dir;	
	
	/**
	 * Coordonée initiale x du robot
	 * @see Environment#setInitValues(int, int, int)
	 */
	private	int	xinit;
	
	/**
	 * Coordonée initiale y du robot
	 * @see Environment#setInitValues(int, int, int)
	 */
	private	int	yinit;
	
	/**
	 * Direction initiale du robot
	 * @see Environment#setInitValues(int, int, int)
	 * @see Param#XP
	 * @see Param#YP
	 * @see Param#XN
	 * @see Param#YN
	 */
	private	int	dirinit;

	/**
	 * Pile contenant les cases récemment explorées, à envoyer régulierement au superviseur
	 * @see ListCase
	 */
	private	ListCase listCase;
	
	/**
	 * Attribut indiquant si au moins une initialisation de la position a été recu
	 * @see Environment#setInitValues(int, int, int)
	 */
	private	boolean	initPositionDone;
	 
	/**
	 * Constructeur de Environment
	 * @param botinit 
	 * 		Objet robot
	 */
	public Environment (Robot botinit) {
		this.bot 		      = botinit;
		this.listCase		  = new ListCase();
		this.initPositionDone = false;
	}

	/**
	 * @return la présence ou non d'un mur à l'avant du robot
	 * @see Environment#frontWallDetected
	 */
	public boolean getFrontWallDetected() {
		return this.frontWallDetected ;
	}
	
	/**
	 * @return la présence ou non d'un mur à droite du robot
	 * @see Environment#rightWallDetected
	 */
	public boolean getRightWallDetected() {
		return this.rightWallDetected ;
	}
	
	/**
	 * @return la présence ou non d'un mur à gauche du robot
	 * @see Environment#leftWallDetected
	 */
	public boolean getLeftWallDetected() {
		return this.leftWallDetected ;
	}
	
	/**
	 * @return la présence ou non d'un mur à l'arrière du robot
	 * @see Environment#backWallDetected
	 */
	public boolean getBackWallDetected() {
		return this.backWallDetected ;
	}
	
	/**
	 * @return la pile de cases
	 * @see ListCase
	 */
	public ListCase getListCase() {
		return this.listCase;
	}
	
	/**
	 * @return true si la position initiale (x,y,orientation) a été recu au moins une fois
	 * @see Environment#initPositionDone
	 */
	public boolean getInitPositionDone() {
		return this.initPositionDone;
	}
	 
	/**
	 * Met à jour la variable determinant la présence d'un mur à l'arriere
	 * @param value 
	 * 		true si un mur est présent
	 * @see Environment#backWallDetected
	 */
	public void setBackWallDetected(boolean value) {
		this.backWallDetected = value ;
	}
	
	/**
	 * Met à jour la position à partir des valeurs présente dans xinit,yinit et dirinit.
	 * Met la variable initPositionDone à true
	 * @see Environment#x
	 * @see Environment#y
	 * @see Environment#dir
	 * @see Environment#xinit
	 * @see Environment#yinit
	 * @see Environment#dirinit
	 * @see Environment#initPositionDone
	 */
	public void setPosition() {
		this.x=this.xinit;
		this.y=this.yinit;
		this.dir=this.dirinit;
		this.initPositionDone = true;
		System.out.println("SetPx="+this.x+"y="+this.y+"d="+this.dir);
	}
	
	/**
	 * Met à jour les valeurs xinit, yinit et diri à partir des arguments
	 * @param xi
	 * 		Nouvelle valeur de xinit
	 * @param yi
	 * 		Nouvelle valeur de yinit
	 * @param diri	 
	 * 		Nouvelle valeur de dirinit
	 * @see Environment#xinit
	 * @see Environment#yinit
	 * @see Environment#dirinit
	 */
	public void setInitValues(int xi, int yi, int diri) {
		this.xinit=xi;
		this.yinit=yi;
		this.dirinit=diri;		
	}
	
	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot tourne à gauche
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 */
	public void wallLeft() {
		this.frontWallDetected = this.leftWallDetected;
		this.backWallDetected  = this.rightWallDetected;
	}
	
	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot tourne à droite
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 */
	public void wallRight() {
		this.frontWallDetected = this.rightWallDetected;
		this.backWallDetected = this.leftWallDetected;
	}
	
	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot fait demi-tour
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 */
	public void wallBack() {
		boolean tempo ;
		tempo = this.frontWallDetected; 
		this.frontWallDetected = this.backWallDetected;
		this.backWallDetected = tempo; 
	}
	
	/**
	 * Met à jour la variable de direction lorsque le robot tourne à droite
	 * @see Environment#dir
	 */
	public void dirRight() {
		if(this.dir==Param.XP) {
			this.dir = Param.YN ;
		}
		else if (this.dir==Param.XN) {
			this.dir = Param.YP ;
		}
		else if (this.dir==Param.YP) {
			this.dir = Param.XP ;
		}
		else if (this.dir==Param.YN) {
			this.dir = Param.XN ;
		}
	}	
	
	/**
	 * Met à jour la variable de direction lorsque le robot tourne à gauche
	 * @see Environment#dir
	 */
	public void dirLeft() {
		if(this.dir==Param.XP) {
			this.dir = Param.YP ;
		}
		else if (this.dir==Param.XN) {
			this.dir = Param.YN ;
		}
		else if (this.dir==Param.YP) {
			this.dir = Param.XN ;
		}
		else if (this.dir==Param.YN) {
			this.dir = Param.XP ;
		}
	}	
	
	/**
	 * Met à jour la variable de direction lorsque le robot fait demi-tour
	 * @see Environment#dir
	 */
	public void dirBack() {
		if(this.dir==Param.XP) {
			this.dir = Param.XN ;
		}
		else if (this.dir==Param.XN) {
			this.dir = Param.XP ;
		}
		else if (this.dir==Param.YP) {
			this.dir = Param.YN ;
		}
		else if (this.dir==Param.YN) {
			this.dir = Param.YP ;
		}
	}

	/**
	 * Affiche les coordonées de la case actuelle ainsi que les murs présents 
	 * @see Environment#x
	 * @see Environment#y
	 * @see Environment#dir
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 */
	 public void afficher() {
		 System.out.print("X="+this.x+" Y="+this.y+"D="+this.dir+"\n");
		 if(this.frontWallDetected) {
			 System.out.print("F ");
		 }
		 if(this.rightWallDetected) {
			 System.out.print("R ");
		 }
		 if(this.backWallDetected) {
			 System.out.print("B ");
		 }
		 if(this.leftWallDetected) {
			 System.out.print("L");
		 }
		 System.out.print("\n");
	 }
	
	 /**
	  * Ajoute la case actuelle à la pile si elle est différente de la derniere case insérée dans la pile
	  * @param forced
	  * 	Permet de forcer l'ajout d'une case
	  * @see Environment#listCase
	  */
	 public void saveCurrentCase(boolean forced) {
		 if(this.bot.getEnv().getListCase().isDifferent(this.x, this.y) || forced) {
			 this.listCase.addCase(this.x, this.y, this.dir, frontWallDetected, leftWallDetected, backWallDetected, rightWallDetected);
		 }				 
	 }
	 
	 /**
	  * Met à jour les coordonées du robot en fonction de sa direction
	  * @see Environment#x
	  * @see Environment#y
	  * @see Environment#dir
	  * @see Robot#order
	  */
	 public void refreshCoord() {
		 if ( this.bot.getOrder() == Param.FORWARD ) {
			 if (this.dir==Param.XP) {
			 	 this.x++;
			 }
			 else if (this.dir==Param.XN) {
			 	 this.x--;
			 }
			 else if (this.dir==Param.YP) {
			 	 this.y++;
			 }
			 else if (this.dir==Param.YN) {
			 	 this.y--;
			 } 
		 }
	 }
 
	 /**
	  * Met à jour les données issues des 2 sonars et de la boussole
	  * @see Sonar
	  * @see Compass
	  */
	 public void check2WallsCompass() {
		this.bot.getLeftFrontSonar().refresh();
		this.bot.getRightSonar().refresh();
		this.bot.getCompass().refresh();
		this.checkLeftWall();
		this.checkRightWall();
	 }

	 /**
	  * Met à jour les données issues des 2 sonars et de la boussole plusieurs fois de manière à 
	  * remplir leurs filtres
	  * @see Sonar
	  * @see Compass
	  */
	 public void check2WallsCompassFull() {
		 for( int i = 0 ; i<Param.TAB_NBDATA ; i++ ) {
			 this.check2WallsCompass();
		 }
	 }
	 
	 /**
	  * Met à jour les données issues du sonar gauche/avant plusieurs fois de manière à remplir son 
	  * filtre. Attention cette méthode n'agit pas sur le moteur du sonar et ne vérifie pas que le 
	  * sonar est en position avant
	  * @see Sonar
	  */
	 public void checkFrontWallFull() {
		for( int i = 0 ; i<Param.TAB_NBDATA ; i++ ) {
			this.bot.getLeftFrontSonar().refresh();
			this.checkFrontWall();
		}
	 }
	 
	 /**
	  * Met à jour les données issues du sonar gauche/avant et de la boussole. Attention cette méthode 
	  * n'agit pas sur le moteur du sonar et ne vérifie pas que le sonar est en position avant
	  * @see Sonar
	  * @see Compass
	  */
	 public void checkFrontWallCompass() {
		 this.bot.getLeftFrontSonar().refresh();
		 this.checkFrontWall();
		 this.bot.getCompass().refresh();
	 }
	 
	 /**
	  * Met à jour la variable indiquant la présence d'un mur à gauche à partir des données issues des capteurs
	  * @see Environment#leftWallDetected
	  * @see Sonar
	  * @see Param#LIMLEFTWALL
	  */
	 private void checkLeftWall(){
		 if(this.bot.getLeftFrontSonar().getMoyData()<=Param.LIMLEFTWALL){
			 this.leftWallDetected = true;
		 }
		 else {
			 this.leftWallDetected = false;
		 }	 
	 }
	 
	 /**
	  * Met à jour la variable indiquant la présence d'un mur à l'avant à partir des données issues des capteurs
	  * @see Environment#leftWallDetected
	  * @see Sonar
	  * @see Param#LIMFRONTWALL
	  */
	 private void checkFrontWall(){
		 if(this.bot.getLeftFrontSonar().getMoyData()<=Param.LIMFRONTWALL){
			 this.frontWallDetected = true;
		 }
		 else {
			 this.frontWallDetected = false;
		 }	
	 }
	 
	 /**
	  * Met à jour la variable indiquant la présence d'un mur à droite à partir des données issues des capteurs
	  * @see Environment#leftWallDetected
	  * @see Sonar
	  * @see Param#LIMRIGHTWALL
	  */
	 private void checkRightWall(){
		 if(this.bot.getRightSonar().getMoyData()<=Param.LIMRIGHTWALL){
			 this.rightWallDetected = true;
		 }
		 else {
			 this.rightWallDetected = false;
		 }	
	 }
}