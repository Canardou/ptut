package env;

import lejos.nxt.Sound;
import robot.Order;
import threads.ThreadRobot;

/**
 * Environment est la classe permettant de d�crire l'environnement du robot.
 * Cette classe contient les variables qui indiquent la pr�sence eventuelle des
 * 4 murs autour du robot. Elle contient aussi les coordon�es du robot, ainsi
 * que sa direction, et leurs valeurs initiales (qui doivent �tre recues du
 * superviseur). On trouve aussi une liste de cases explor�es qui doit �tre
 * r�gulierement envoy�e au superviseur.
 * 
 * @author Thomas
 */
public class Environment {

	/**
	 * Taille du tableau (nombre d'�chantillons dans le filtre) des capteurs en
	 * nombre d'�l�ments.
	 */
	public static final int TAB_NBDATA = 4;

	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur � gauche. En
	 * cm.
	 */
	public static final double LIMLEFTWALL = 19;

	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur � droite. En
	 * cm.
	 */
	public static final double LIMRIGHTWALL = 19;

	/**
	 * Distance d'arret par rapport au mur d'en face.
	 */
	public static final double FRONTWALL_REG = 9.5;
	
	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur en face. En
	 * cm.
	 */
	public static final double LIMFRONTWALL = 17;

	/**
	 * Limite en dessous de laquelle on consid�re qu'on est � proximit� d'un mur
	 * avant. En cm.
	 */
	public static final double FRONTWALL_DANGER = 22;
	
	/**
	 * Valeur renvoy� par le capteur de lumi�re a partir de laquelle on
	 * consid�re qu'il voit la cible.
	 */
	public static final double LIGHT_TARGET = 560;

	/**
	 * Attribut contenant l'objet robot.
	 * 
	 * @see Robot
	 */
	private ThreadRobot tRobot;

	/**
	 * Attribut contenant l'information sur la pr�sence d'un mur � l'avant du
	 * robot. Attention cette valeur peut-�tre incertaine durant l'execution
	 * d'un mouvement (d�pend de la position du sonar rotatif).
	 */
	private boolean frontWallDetected;

	/**
	 * Attribut contenant l'information sur la pr�sence d'un mur � droite du
	 * robot.
	 */
	private boolean rightWallDetected;

	/**
	 * Attribut contenant l'information sur la pr�sence d'un mur � gauche du
	 * robot. Attention cette valeur peut-�tre incertaine durant l'execution
	 * d'un mouvement (d�pend de la position du sonar rotatif).
	 */
	private boolean leftWallDetected;

	/**
	 * Attribut contenant l'information sur la pr�sence d'un mur � l'arri�re du
	 * robot. Attention cette valeur peut-�tre incertaine durant l'execution
	 * d'un mouvement.
	 */
	private boolean backWallDetected;

	/**
	 * Coordon�e x du robot.
	 */
	private int x;

	/**
	 * Coordon�e y du robot.
	 */
	private int y;

	/**
	 * Direction du robot.
	 */
	private int dir;

	/**
	 * Coordon�e x du robot.
	 */
	private int xinit=0;

	/**
	 * Coordon�e y du robot.
	 */
	private int yinit=0;

	/**
	 * Direction du robot.
	 */
	private int dirinit=0;

	/**
	 * Liste contenant les cases r�cemment explor�es, � envoyer r�gulierement au
	 * superviseur.
	 * 
	 * @see ListCase
	 */
	private ListCase listCase;

	/**
	 * Attribut indiquant si au moins une initialisation de la position a �t�
	 * recu.
	 */
	private boolean initPositionDone;

	/**
	 * Attribut permettant de savoir dans quelle position est le sonar rotatif.
	 * True si le sonar est dirig� vers l'avant, False s'il est dirig� vers la
	 * gauche.
	 */
	private boolean sonarIsFront;
	
	/**
	 * Attribut permettant de savoir si le robot ce situe dans la case de la
	 * cible.
	 */
	private boolean targetHere;
	
	
	/**
	 * Attribut permettant de savoir si la cible a �t� detect�e au moins une
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
	 * Met � jour la position � partir des valeurs pass�es en arguments.
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
	 * Met � jour les variables d'intiialisation de la position � partir des valeurs pass�es en arguments.
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
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * tourne � gauche.
	 */
	public void wallLeft() {
		this.frontWallDetected = this.leftWallDetected;
		this.backWallDetected = this.rightWallDetected;
	}

	/**
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * tourne � droite.
	 */
	public void wallRight() {
		this.frontWallDetected = this.rightWallDetected;
		this.backWallDetected = this.leftWallDetected;
	}

	/**
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * fait demi-tour.
	 */
	public void wallBack() {
		boolean tempo;
		tempo = this.frontWallDetected;
		this.frontWallDetected = this.backWallDetected;
		this.backWallDetected = tempo;
	}

	/**
	 * Met � jour la variable de direction lorsque le robot tourne � droite
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
	 * Met � jour la variable de direction lorsque le robot tourne � gauche.
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
	 * Met � jour la variable de direction lorsque le robot fait demi-tour.
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
	 * Affiche les coordon�es de la case actuelle ainsi que les murs pr�sents.
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
	 * Ajoute la case actuelle � la liste.
	 */
	public synchronized void saveCurrentCase() {
		this.listCase.addCase(this.x, this.y, this.dir, frontWallDetected, leftWallDetected, backWallDetected, rightWallDetected);
	}

	/**
	 * Met � jour les coordon�es du robot en fonction de sa direction
	 * @return 0 si les coordon�es ont �t� modifi�es.
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
	 * Met � jour les donn�es issues des 2 sonars et de la boussole.
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
	 * Met � jour les donn�es issues des 2 sonars, du capteur de lumi�re et de
	 * la boussole plusieurs fois de mani�re � remplir leurs filtres.
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
	 * Met � jour la variable indiquant la pr�sence d'un mur � gauche � partir
	 * des donn�es issues des capteurs.
	 */
	private void checkLeftWall() {
		if (this.tRobot.getLeftFrontSonar().getMoyData() <= LIMLEFTWALL) {
			this.leftWallDetected = true;
		} else {
			this.leftWallDetected = false;
		}
	}

	/**
	 * Met � jour la variable indiquant la pr�sence d'un mur � l'avant � partir
	 * des donn�es issues des capteurs.
	 */
	private void checkFrontWall() {
		if (this.tRobot.getLeftFrontSonar().getMoyData() <= LIMFRONTWALL) {
			this.frontWallDetected = true;
		} else {
			this.frontWallDetected = false;
		}
	}

	/**
	 * Met � jour la variable indiquant la pr�sence d'un mur � droite � partir
	 * des donn�es issues des capteurs.
	 */
	private void checkRightWall() {
		if (this.tRobot.getRightSonar().getMoyData() <= LIMRIGHTWALL) {
			this.rightWallDetected = true;
		} else {
			this.rightWallDetected = false;
		}
	}
	
	/**
	 * Met � jour la variable indiquant la pr�sence de la cible au sol � partir
	 * des donn�es issues des capteurs.
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
	 * @return la pr�sence ou non d'un mur � l'avant du robot.
	 */
	public boolean getFrontWallDetected() {
		return this.frontWallDetected;
	}

	/**
	 * @return la pr�sence ou non d'un mur � droite du robot.
	 */
	public boolean getRightWallDetected() {
		return this.rightWallDetected;
	}

	/**
	 * @return la pr�sence ou non d'un mur � gauche du robot.
	 */
	public boolean getLeftWallDetected() {
		return this.leftWallDetected;
	}

	/**
	 * @return la pr�sence ou non d'un mur � l'arri�re du robot.
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
	 * @return true si la position initiale (x,y,orientation) a �t� recu au
	 *         moins une fois.
	 */
	public boolean getInitPositionDone() {
		return this.initPositionDone;
	}

	/**
	 * Met � jour la variable determinant la pr�sence d'un mur � l'arriere.
	 * 
	 * @param value
	 *            true si un mur est pr�sent
	 */
	public void setBackWallDetected(boolean value) {
		this.backWallDetected = value;
	}
	
	/**
	 * @return la pr�sence d'une cible.
	 */
	public boolean getTargetHere() {
		return this.targetHere;
	}
	
	/**
	 * @param arg
	 * 		Met � jour la viarable targetHere.
	 */
	public void setTargetHere(boolean arg) {
		this.targetHere=arg;
	}

}