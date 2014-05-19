package env;

import lejos.nxt.Sound;
import robot.Order;
import threads.ThreadRobot;

/**
 * Environment est la classe permettant de décrire l'environnement du robot.
 * Cette classe contient les variables qui indiquent la présence eventuelle des
 * 4 murs autour du robot. Elle contient aussi les coordonées du robot, ainsi
 * que sa direction, et leurs valeurs initiales (qui doivent être recues du
 * superviseur). On trouve aussi une liste de cases explorées qui doit être
 * régulierement envoyée au superviseur.
 * 
 * @author Thomas
 */
public class Environment {

	/**
	 * Taille du tableau (nombre d'échantillons dans le filtre) des capteurs en
	 * nombre d'éléments.
	 */
	public static final int TAB_NBDATA = 4;

	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur à gauche. En
	 * cm.
	 */
	public static final double LIMLEFTWALL = 19;

	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur à droite. En
	 * cm.
	 */
	public static final double LIMRIGHTWALL = 19;

	/**
	 * Distance d'arret par rapport au mur d'en face.
	 */
	public static final double FRONTWALL_REG = 9.5;
	
	/**
	 * Limite en dessous de laquelle on considère qu'il y a un mur en face. En
	 * cm.
	 */
	public static final double LIMFRONTWALL = 17;

	/**
	 * Limite en dessous de laquelle on considère qu'on est à proximité d'un mur
	 * avant. En cm.
	 */
	public static final double FRONTWALL_DANGER = 22;
	
	/**
	 * Valeur renvoyé par le capteur de lumière a partir de laquelle on
	 * considère qu'il voit la cible.
	 */
	public static final double LIGHT_TARGET = 560;

	/**
	 * Attribut contenant l'objet robot.
	 * 
	 * @see Robot
	 */
	private ThreadRobot tRobot;

	/**
	 * Attribut contenant l'information sur la présence d'un mur à l'avant du
	 * robot. Attention cette valeur peut-être incertaine durant l'execution
	 * d'un mouvement (dépend de la position du sonar rotatif).
	 */
	private boolean frontWallDetected;

	/**
	 * Attribut contenant l'information sur la présence d'un mur à droite du
	 * robot.
	 */
	private boolean rightWallDetected;

	/**
	 * Attribut contenant l'information sur la présence d'un mur à gauche du
	 * robot. Attention cette valeur peut-être incertaine durant l'execution
	 * d'un mouvement (dépend de la position du sonar rotatif).
	 */
	private boolean leftWallDetected;

	/**
	 * Attribut contenant l'information sur la présence d'un mur à l'arrière du
	 * robot. Attention cette valeur peut-être incertaine durant l'execution
	 * d'un mouvement.
	 */
	private boolean backWallDetected;

	/**
	 * Coordonée x du robot.
	 */
	private int x;

	/**
	 * Coordonée y du robot.
	 */
	private int y;

	/**
	 * Direction du robot.
	 */
	private int dir;

	/**
	 * Coordonée x du robot.
	 */
	private int xinit=0;

	/**
	 * Coordonée y du robot.
	 */
	private int yinit=0;

	/**
	 * Direction du robot.
	 */
	private int dirinit=0;

	/**
	 * Liste contenant les cases récemment explorées, à envoyer régulierement au
	 * superviseur.
	 * 
	 * @see ListCase
	 */
	private ListCase listCase;

	/**
	 * Attribut indiquant si au moins une initialisation de la position a été
	 * recu.
	 */
	private boolean initPositionDone;

	/**
	 * Attribut permettant de savoir dans quelle position est le sonar rotatif.
	 * True si le sonar est dirigé vers l'avant, False s'il est dirigé vers la
	 * gauche.
	 */
	private boolean sonarIsFront;
	
	/**
	 * Attribut permettant de savoir si le robot ce situe dans la case de la
	 * cible.
	 */
	private boolean targetHere;
	
	
	/**
	 * Attribut permettant de savoir si la cible a été detectée au moins une
	 * fois par le robot.
	 */
	private boolean targetFound;

	/**
	 * Constructeur de Environment.
	 * 
	 * @param botinit
	 *            Objet robot.
	 * @see ThreadRobot     
	 */
	public Environment(ThreadRobot botinit) {
		this.tRobot = botinit;
		this.listCase = new ListCase(botinit);
		this.initPositionDone = false;
		this.sonarIsFront = false;
		this.targetHere = false;
		this.targetFound = false;
	}

	/**
	 * Tourne le sonar rotatif en position avant.
	 */
	public void sonarFront() {
		this.tRobot.getSonarMotor().rotateTo(-90);
		this.sonarIsFront = true;
		this.checkSensorsFull();
	}

	/**
	 * Tourne le sonar rotatif en position gauche.
	 */
	public void sonarLeft() {
		this.tRobot.getSonarMotor().rotateTo(0);
		this.sonarIsFront = false;
		this.checkSensorsFull();
	}

	/**
	 * @return true si le sonar est en position avant.
	 */
	public boolean getSonarIsFront() {
		return this.sonarIsFront;
	}

	/**
	 * Met à jour la position à partir des valeurs passées en arguments.
	 * @param xi
	 * @param yi
	 * @param diri
	 */
	public synchronized void setPosition() {
		this.x = this.xinit;
		this.y = this.yinit;
		this.dir = this.dirinit;
		this.initPositionDone = true;
		System.out.println("SetPx=" + this.x + "y=" + this.y + "d=" + this.dir);
	}
	
	/**
	 * Met à jour les variables d'intiialisation de la position à partir des valeurs passées en arguments.
	 * @param xi
	 * @param yi
	 * @param diri
	 */
	public synchronized void setInitPos(int xi, int yi, int diri) {
		this.xinit = xi;
		this.yinit = yi;
		this.dirinit = diri;
	}

	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot
	 * tourne à gauche.
	 */
	public void wallLeft() {
		this.frontWallDetected = this.leftWallDetected;
		this.backWallDetected = this.rightWallDetected;
	}

	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot
	 * tourne à droite.
	 */
	public void wallRight() {
		this.frontWallDetected = this.rightWallDetected;
		this.backWallDetected = this.leftWallDetected;
	}

	/**
	 * Met à jour les variables indiquant la présence des murs lorsque le robot
	 * fait demi-tour.
	 */
	public void wallBack() {
		boolean tempo;
		tempo = this.frontWallDetected;
		this.frontWallDetected = this.backWallDetected;
		this.backWallDetected = tempo;
	}

	/**
	 * Met à jour la variable de direction lorsque le robot tourne à droite
	 */
	public synchronized void dirRight() {
		if (this.dir == Case.RIGHT) {
			this.dir = Case.DOWN;
		} else if (this.dir == Case.LEFT) {
			this.dir = Case.UP;
		} else if (this.dir == Case.UP) {
			this.dir = Case.RIGHT;
		} else if (this.dir == Case.DOWN) {
			this.dir = Case.LEFT;
		}
	}

	/**
	 * Met à jour la variable de direction lorsque le robot tourne à gauche.
	 */
	public synchronized void dirLeft() {
		if (this.dir == Case.RIGHT) {
			this.dir = Case.UP;
		} else if (this.dir == Case.LEFT) {
			this.dir = Case.DOWN;
		} else if (this.dir == Case.UP) {
			this.dir = Case.LEFT;
		} else if (this.dir == Case.DOWN) {
			this.dir = Case.RIGHT;
		}
	}

	/**
	 * Met à jour la variable de direction lorsque le robot fait demi-tour.
	 */
	public synchronized void dirBack() {
		if (this.dir == Case.RIGHT) {
			this.dir = Case.LEFT;
		} else if (this.dir == Case.LEFT) {
			this.dir = Case.RIGHT;
		} else if (this.dir == Case.UP) {
			this.dir = Case.DOWN;
		} else if (this.dir == Case.DOWN) {
			this.dir = Case.UP;
		}
	}

	/**
	 * Affiche les coordonées de la case actuelle ainsi que les murs présents.
	 */
	public synchronized void printEnv() {
		System.out.print("X=" + this.x + " Y=" + this.y + "D=" + this.dir + "\n");
		if (this.frontWallDetected) {
			System.out.print("F ");
		}
		if (this.rightWallDetected) {
			System.out.print("R ");
		}
		if (this.backWallDetected) {
			System.out.print("B ");
		}
		if (this.leftWallDetected) {
			System.out.print("L");
		}
		System.out.print("\n");
	}

	/**
	 * Ajoute la case actuelle à la liste.
	 */
	public synchronized void saveCurrentCase() {
		this.listCase.addCase(this.x, this.y, this.dir, frontWallDetected, leftWallDetected, backWallDetected, rightWallDetected);
	}

	/**
	 * Met à jour les coordonées du robot en fonction de sa direction
	 * @return 0 si les coordonées ont été modifiées.
	 */
	public synchronized int refreshCoord() {
		if (this.tRobot.getOrder().getCurrentOrder() == Order.FORWARD) {
			if (this.dir == Case.RIGHT) {
				this.x++;
				return 0;
			} else if (this.dir == Case.LEFT) {
				this.x--;
				return 0;
			} else if (this.dir == Case.UP) {
				this.y++;
				return 0;
			} else if (this.dir == Case.DOWN) {
				this.y--;
				return 0;
			} else {
				System.out.println("refreshCoord:err dir");
				return 1;
			}
		} else {
			System.out.println("refreshCoord:err order");
			return 1;
		}
	}

	/**
	 * Met à jour les données issues des 2 sonars et de la boussole.
	 */
	public void checkSensors() {
		this.tRobot.getLeftFrontSonar().refresh();
		this.tRobot.getRightSonar().refresh();
		this.tRobot.getCompass().refresh();
		this.tRobot.getLightSensor().refresh();
		if (this.sonarIsFront) {
			this.checkFrontWall();
		} else {
			this.checkLeftWall();
		}
		this.checkRightWall();
		this.checkLight();
	}

	/**
	 * Met à jour les données issues des 2 sonars, du capteur de lumière et de
	 * la boussole plusieurs fois de manière à remplir leurs filtres.
	 */
	public void checkSensorsFull() {
		for (int i = 0; i < TAB_NBDATA; i++) {
			this.tRobot.getLeftFrontSonar().refresh();
			this.tRobot.getRightSonar().refresh();
			this.tRobot.getCompass().refresh();
			this.tRobot.getLightSensor().refresh();
		}
		if (this.sonarIsFront) {
			this.checkFrontWall();
		} else {
			this.checkLeftWall();
		}
		this.checkRightWall();
		this.checkLight();
	}

	/**
	 * Met à jour la variable indiquant la présence d'un mur à gauche à partir
	 * des données issues des capteurs.
	 */
	private void checkLeftWall() {
		if (this.tRobot.getLeftFrontSonar().getMoyData() <= LIMLEFTWALL) {
			this.leftWallDetected = true;
		} else {
			this.leftWallDetected = false;
		}
	}

	/**
	 * Met à jour la variable indiquant la présence d'un mur à l'avant à partir
	 * des données issues des capteurs.
	 */
	private void checkFrontWall() {
		if (this.tRobot.getLeftFrontSonar().getMoyData() <= LIMFRONTWALL) {
			this.frontWallDetected = true;
		} else {
			this.frontWallDetected = false;
		}
	}

	/**
	 * Met à jour la variable indiquant la présence d'un mur à droite à partir
	 * des données issues des capteurs.
	 */
	private void checkRightWall() {
		if (this.tRobot.getRightSonar().getMoyData() <= LIMRIGHTWALL) {
			this.rightWallDetected = true;
		} else {
			this.rightWallDetected = false;
		}
	}
	
	/**
	 * Met à jour la variable indiquant la présence de la cible au sol à partir
	 * des données issues des capteurs.
	 */
	private void checkLight() {
		if (this.tRobot.getLightSensor().getMoyData() > LIGHT_TARGET) {
			if (!this.targetFound){ 
				this.targetFound = true;
				this.targetHere = true;
				Sound.beepSequenceUp();
			} else {
				if(!this.targetHere) {
					Sound.beepSequenceUp();
				}
			}			
		}
	}

	/**
	 * @return la présence ou non d'un mur à l'avant du robot.
	 */
	public boolean getFrontWallDetected() {
		return this.frontWallDetected;
	}

	/**
	 * @return la présence ou non d'un mur à droite du robot.
	 */
	public boolean getRightWallDetected() {
		return this.rightWallDetected;
	}

	/**
	 * @return la présence ou non d'un mur à gauche du robot.
	 */
	public boolean getLeftWallDetected() {
		return this.leftWallDetected;
	}

	/**
	 * @return la présence ou non d'un mur à l'arrière du robot.
	 */
	public boolean getBackWallDetected() {
		return this.backWallDetected;
	}

	/**
	 * @return la liste de cases.
	 */
	public ListCase getListCase() {
		return this.listCase;
	}

	/**
	 * @return true si la position initiale (x,y,orientation) a été recu au
	 *         moins une fois.
	 */
	public boolean getInitPositionDone() {
		return this.initPositionDone;
	}

	/**
	 * Met à jour la variable determinant la présence d'un mur à l'arriere.
	 * 
	 * @param value
	 *            true si un mur est présent
	 */
	public void setBackWallDetected(boolean value) {
		this.backWallDetected = value;
	}
	
	/**
	 * @return la présence d'une cible.
	 */
	public boolean getTargetHere() {
		return this.targetHere;
	}
	
	/**
	 * @param arg
	 * 		Met à jour la viarable targetHere.
	 */
	public void setTargetHere(boolean arg) {
		this.targetHere=arg;
	}

}