package env;

import threads.ThreadRobot;
import orders.*;

/**
 * Environment est la classe permettant de d�crire l'environnement du robot.
 * Cette classe contient les variables qui indiquent la pr�sence eventuelle des
 * 4 murs autour du robot. Elle contient aussi les coordon�es du robot, ainsi
 * que sa direction, et leurs valeurs initiales (qui doivent �tre r�cues du
 * superviseur). On trouve aussi une pile de cases explor�es qui doit �tre
 * r�gulierement envoy�e au superviseur
 * 
 * @author Thomas
 */
public class Environment {

	/**
	 * Taille du tableau (nombre d'�chantillon dans le filtre) des capteurs en
	 * nombre d'�l�ment
	 */
	public static final int TAB_NBDATA = 5;

	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur � gauche en
	 * cm
	 */
	public static final double LIMLEFTWALL = 18;

	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur � droite en
	 * cm
	 */
	public static final double LIMRIGHTWALL = 18;

	/**
	 * Limite en dessous de laquelle on consid�re qu'il y a un mur en face en cm
	 */
	public static final double LIMFRONTWALL = 9;

	/**
	 * Limite en dessous de laquelle on consid�re qu'on est � proximit� d'un mur
	 * avant en cm
	 */
	public static final double FRONTWALL_DANGER = 22;

	/**
	 * Attribut contenant l'objet robot
	 * 
	 * @see Robot
	 */
	private ThreadRobot robot;

	/**
	 * Attribut contenant l'information sur la pr�sence d'un mur � l'avant du
	 * robot Attention cette valeur peut-�tre incertaine durant l'execution d'un
	 * mouvement
	 * 
	 * @see Environment#getFrontWallDetected()
	 * @see Environment#setFrontWallDetected()
	 */
	private boolean frontWallDetected;

	/**
	 * Attribut contenant l'information sur la pr�sence d'un mur � droite du
	 * robot
	 * 
	 * @see Environment#getRightWallDetected()
	 * @see Environment#setRightWallDetected()
	 */
	private boolean rightWallDetected;

	/**
	 * Attribut contenant l'information sur la pr�sence d'un mur � gauche du
	 * robot Attention cette valeur peut-�tre incertaine durant l'execution d'un
	 * mouvement
	 * 
	 * @see Environment#getLeftWallDetected()
	 * @see Environment#setLeftWallDetected()
	 */
	private boolean leftWallDetected;

	/**
	 * Attribut contenant l'information sur la pr�sence d'un mur � l'arri�re du
	 * robot Attention cette valeur peut-�tre incertaine durant l'execution d'un
	 * mouvement
	 * 
	 * @see Environment#getBackWallDetected()
	 * @see Environment#setBackWallDetected()
	 */
	private boolean backWallDetected;

	/**
	 * Coordon�e x du robot
	 * 
	 * @see Environment#setPosition()
	 */
	private int x;

	/**
	 * Coordon�e y du robot
	 * 
	 * @see Environment#setPosition()
	 */
	private int y;

	/**
	 * Direction du robot
	 * 
	 * @see Environment#setPosition()
	 * @see Param#XP
	 * @see Param#YP
	 * @see Param#XN
	 * @see Param#YN
	 */
	private int dir;

	/**
	 * Coordon�e initiale x du robot
	 * 
	 * @see Environment#setInitValues(int, int, int)
	 */
	private int xinit;

	/**
	 * Coordon�e initiale y du robot
	 * 
	 * @see Environment#setInitValues(int, int, int)
	 */
	private int yinit;

	/**
	 * Direction initiale du robot
	 * 
	 * @see Environment#setInitValues(int, int, int)
	 * @see Param#XP
	 * @see Param#YP
	 * @see Param#XN
	 * @see Param#YN
	 */
	private int dirinit;

	/**
	 * Pile contenant les cases r�cemment explor�es, � envoyer r�gulierement au
	 * superviseur
	 * 
	 * @see ListCase
	 */
	private ListCase listCase;

	/**
	 * Attribut indiquant si au moins une initialisation de la position a �t�
	 * recu
	 * 
	 * @see Environment#setInitValues(int, int, int)
	 */
	private boolean initPositionDone;

	/**
	 * Constructeur de Environment
	 * 
	 * @param botinit
	 *            Objet robot
	 */
	public Environment(ThreadRobot botinit) {
		this.robot = botinit;
		this.listCase = new ListCase();
		this.initPositionDone = false;
	}
	
	public void sonarFront() {
		this.robot.getSonarMotor().rotateTo(-90);
	}
	
	public void sonarLeft() {
		this.robot.getSonarMotor().rotateTo(0);
	}

	/**
	 * Met � jour la position � partir des valeurs pr�sente dans xinit,yinit et
	 * dirinit. Met la variable initPositionDone � true
	 * 
	 * @see Environment#x
	 * @see Environment#y
	 * @see Environment#dir
	 * @see Environment#xinit
	 * @see Environment#yinit
	 * @see Environment#dirinit
	 * @see Environment#initPositionDone
	 */
	public void setPosition() {
		this.x = this.xinit;
		this.y = this.yinit;
		this.dir = this.dirinit;
		this.initPositionDone = true;
		System.out.println("SetPx=" + this.x + "y=" + this.y + "d=" + this.dir);
	}

	/**
	 * Met � jour les valeurs xinit, yinit et diri � partir des arguments
	 * 
	 * @param xi
	 *            Nouvelle valeur de xinit
	 * @param yi
	 *            Nouvelle valeur de yinit
	 * @param diri
	 *            Nouvelle valeur de dirinit
	 * @see Environment#xinit
	 * @see Environment#yinit
	 * @see Environment#dirinit
	 */
	public void setInitValues(int xi, int yi, int diri) {
		this.xinit = xi;
		this.yinit = yi;
		this.dirinit = diri;
	}

	/**
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * tourne � gauche
	 * 
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 */
	public void wallLeft() {
		this.frontWallDetected = this.leftWallDetected;
		this.backWallDetected = this.rightWallDetected;
	}

	/**
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * tourne � droite
	 * 
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
	 * Met � jour les variables indiquant la pr�sence des murs lorsque le robot
	 * fait demi-tour
	 * 
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 */
	public void wallBack() {
		boolean tempo;
		tempo = this.frontWallDetected;
		this.frontWallDetected = this.backWallDetected;
		this.backWallDetected = tempo;
	}

	/**
	 * Met � jour la variable de direction lorsque le robot tourne � droite
	 * 
	 * @see Environment#dir
	 */
	public void dirRight() {
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
	 * Met � jour la variable de direction lorsque le robot tourne � gauche
	 * 
	 * @see Environment#dir
	 */
	public void dirLeft() {
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
	 * Met � jour la variable de direction lorsque le robot fait demi-tour
	 * 
	 * @see Environment#dir
	 */
	public void dirBack() {
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
	 * Affiche les coordon�es de la case actuelle ainsi que les murs pr�sents
	 * 
	 * @see Environment#x
	 * @see Environment#y
	 * @see Environment#dir
	 * @see Environment#frontWallDetected
	 * @see Environment#leftWallDetected
	 * @see Environment#backWallDetected
	 * @see Environment#rightWallDetected
	 */
	public void afficher() {
		System.out.print("X=" + this.x + " Y=" + this.y + "D=" + this.dir
				+ "\n");
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
	 * Ajoute la case actuelle � la pile si elle est diff�rente de la derniere
	 * case ins�r�e dans la pile
	 * 
	 * @param forced
	 *            Permet de forcer l'ajout d'une case
	 * @see Environment#listCase
	 */
	public void saveCurrentCase(boolean forced) {
		if (this.robot.getEnv().getListCase().isDifferent(this.x, this.y)
				|| forced) {
			this.listCase.addCase(this.x, this.y, this.dir, frontWallDetected,
					leftWallDetected, backWallDetected, rightWallDetected);
		}
	}

	/**
	 * Met � jour les coordon�es du robot en fonction de sa direction
	 * 
	 * @see Environment#x
	 * @see Environment#y
	 * @see Environment#dir
	 * @see Robot#order
	 */
	public void refreshCoord() {
		if (this.robot.getOrder().getCurrentOrder() == Order.FORWARD) {
			if (this.dir == Case.RIGHT) {
				this.x++;
			} else if (this.dir == Case.LEFT) {
				this.x--;
			} else if (this.dir == Case.UP) {
				this.y++;
			} else if (this.dir == Case.DOWN) {
				this.y--;
			}
		}
	}

	/**
	 * Met � jour les donn�es issues des 2 sonars et de la boussole
	 * 
	 * @see Sonar
	 * @see Compass
	 */
	public void check2WallsCompass() {
		this.robot.getLeftFrontSonar().refresh();
		this.robot.getRightSonar().refresh();
		this.robot.getCompass().refresh();
		this.checkLeftWall();
		this.checkRightWall();
	}

	/**
	 * Met � jour les donn�es issues des 2 sonars et de la boussole plusieurs
	 * fois de mani�re � remplir leurs filtres
	 * 
	 * @see Sonar
	 * @see Compass
	 */
	public void check2WallsCompassFull() {
		for (int i = 0; i < TAB_NBDATA; i++) {
			this.check2WallsCompass();
		}
	}

	/**
	 * Met � jour les donn�es issues du sonar gauche/avant plusieurs fois de
	 * mani�re � remplir son filtre. Attention cette m�thode n'agit pas sur le
	 * moteur du sonar et ne v�rifie pas que le sonar est en position avant
	 * 
	 * @see Sonar
	 */
	public void checkFrontWallFull() {
		for (int i = 0; i < TAB_NBDATA; i++) {
			this.robot.getLeftFrontSonar().refresh();
			this.checkFrontWall();
		}
	}

	/**
	 * Met � jour les donn�es issues du sonar gauche/avant et de la boussole.
	 * Attention cette m�thode n'agit pas sur le moteur du sonar et ne v�rifie
	 * pas que le sonar est en position avant
	 * 
	 * @see Sonar
	 * @see Compass
	 */
	public void checkFrontWallCompass() {
		this.robot.getLeftFrontSonar().refresh();
		this.checkFrontWall();
		this.robot.getCompass().refresh();
	}

	/**
	 * Met � jour la variable indiquant la pr�sence d'un mur � gauche � partir
	 * des donn�es issues des capteurs
	 * 
	 * @see Environment#leftWallDetected
	 * @see Sonar
	 * @see Param#LIMLEFTWALL
	 */
	private void checkLeftWall() {
		if (this.robot.getLeftFrontSonar().getMoyData() <= LIMLEFTWALL) {
			this.leftWallDetected = true;
		} else {
			this.leftWallDetected = false;
		}
	}

	/**
	 * Met � jour la variable indiquant la pr�sence d'un mur � l'avant � partir
	 * des donn�es issues des capteurs
	 * 
	 * @see Environment#leftWallDetected
	 * @see Sonar
	 * @see Param#LIMFRONTWALL
	 */
	private void checkFrontWall() {
		if (this.robot.getLeftFrontSonar().getMoyData() <= LIMFRONTWALL) {
			this.frontWallDetected = true;
		} else {
			this.frontWallDetected = false;
		}
	}

	/**
	 * Met � jour la variable indiquant la pr�sence d'un mur � droite � partir
	 * des donn�es issues des capteurs
	 * 
	 * @see Environment#leftWallDetected
	 * @see Sonar
	 * @see Param#LIMRIGHTWALL
	 */
	private void checkRightWall() {
		if (this.robot.getRightSonar().getMoyData() <= LIMRIGHTWALL) {
			this.rightWallDetected = true;
		} else {
			this.rightWallDetected = false;
		}
	}

	/**
	 * @return la pr�sence ou non d'un mur � l'avant du robot
	 * @see Environment#frontWallDetected
	 */
	public boolean getFrontWallDetected() {
		return this.frontWallDetected;
	}

	/**
	 * @return la pr�sence ou non d'un mur � droite du robot
	 * @see Environment#rightWallDetected
	 */
	public boolean getRightWallDetected() {
		return this.rightWallDetected;
	}

	/**
	 * @return la pr�sence ou non d'un mur � gauche du robot
	 * @see Environment#leftWallDetected
	 */
	public boolean getLeftWallDetected() {
		return this.leftWallDetected;
	}

	/**
	 * @return la pr�sence ou non d'un mur � l'arri�re du robot
	 * @see Environment#backWallDetected
	 */
	public boolean getBackWallDetected() {
		return this.backWallDetected;
	}

	/**
	 * @return la pile de cases
	 * @see ListCase
	 */
	public ListCase getListCase() {
		return this.listCase;
	}

	/**
	 * @return true si la position initiale (x,y,orientation) a �t� recu au
	 *         moins une fois
	 * @see Environment#initPositionDone
	 */
	public boolean getInitPositionDone() {
		return this.initPositionDone;
	}

	/**
	 * Met � jour la variable determinant la pr�sence d'un mur � l'arriere
	 * 
	 * @param value
	 *            true si un mur est pr�sent
	 * @see Environment#backWallDetected
	 */
	public void setBackWallDetected(boolean value) {
		this.backWallDetected = value;
	}

}