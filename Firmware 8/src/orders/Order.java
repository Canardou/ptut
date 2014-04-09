package orders;

import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import threads.ThreadRobot;

public class Order {
	
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
	 * Ordre : attendre que l'utilisateur appuie sur le bouton ENTER du robot
	 */
	public static final int WAITBUTTON = 10;
	
	/**
	 * Ordre : attendre 1 seconde
	 */
	public static final int WAIT1SEC = 11;

	/**
	 * Attribut représentant l'ordre actuel du robot
	 * 
	 * @see Order#getOrder()
	 */
	private int currentOrder;

	/**
	 * Attribut qui fait référence à la tache principale du robot
	 */
	private ThreadRobot robot;

	/**
	 * Attribut permettant de connaitre l'erreur courante sur l'ordre (utile
	 * lors des vérifications des conditions initiales)
	 */
	private int errOrder;

	/**
	 * Attribut permettant de savoir si le robot à rempli les conditions
	 * initiales pour commencer a se déplacer
	 */
	private boolean readyToGo;

	/**
	 * Attribut permettant de savoir si le robot à verifié au moins une fois les
	 * 4 murs de sa case initiale
	 */
	private boolean checkFirstCaseDone;
	
	/**
	 * Attribut contenant la pile d'ordres
	 * @see ArrayList
	 */
	private ArrayList<Integer> list;

	/**
	 * Constructeur de Order
	 * @param r
	 *            objet représentant le thread principal du robot
	 */
	public Order(ThreadRobot r) {
		this.robot = r;
		this.list = new ArrayList<Integer> () ;
		this.currentOrder = STOP;
		this.readyToGo = false;
		this.checkFirstCaseDone = false;
		this.errOrder = -1;
	}
	
	/**
	 * Ajoute un ordre à la fin de liste. Sauf s'il s'agit d'un ordre jugé à haute priorité, 
	 * auquel cas l'ordre est mis à l'index 0 pour etre le prochain executé
	 * @param order
	 * 		Ordre à insérer dans la liste
	 * @return 0 si l'opération s'est bien déroulée
	 */
	public int add(int o)
	{
		if( o==Order.STOP || o==Order.FORWARD 
				|| o==Order.TURNL || o==Order.TURNR 
				|| o==Order.TURNB || o==Order.CALCOMPASS 
				|| o==Order.SAVEREFANGLE || o==Order.CHECKFIRSTCASE 
				|| o==Order.SETPOSITION || o==Order.WAIT1SEC
				|| o==Order.WAITBUTTON) {
			this.list.add(o);
			return 0;
		}
		else if( o==Order.CLEARLISTORDER ) {
			this.list.add(0,o);
			return 0;
		}
		else {
			System.out.println("[ERR]add:param");
			return 1;
		}
	}
	
	/**
	 * Retire et renvoit l'ordre le plus ancien présent dans la liste
	 * @return l'ordre en haut de la pile ou l'ordre STOP si la pile est vide
	 * @see Param#STOP
	 * @see Param#FORWARD
	 * @see Param#TURNL
	 * @see Param#TURNR
 	 * @see Param#TURNB
 	 * @see Param#CALCOMPASS
 	 * @see Param#SAVEREFANGLE
 	 * @see Param#CHECKFIRSTCASE
 	 * @see Param#SETPOSITION
	 */
	public Integer get(){
		if(this.list.isEmpty()) {
			return Order.STOP;
		}
		else {
			return this.list.remove(0);
		}		
	}
	
	/**
	 * Vide entierement la liste
	 */
	public void clear() {
		this.list.clear();
	}
	
	/**
	 * Vérifie si la liste est vide
	 * @return true si la liste est vide
	 */
	public boolean isEmpty() {
		if(this.list.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Choisit un ordre en se basant sur la liste d'ordre recu du superviseur On
	 * vérifie que les conditions initiales soit bonnes si on veut deplacer le
	 * robot, i.e. : </br> 1 - La boussole a été calibrée (au moins une fois) ->
	 * ordre CALCOMPASS</br> 2 - L'angle de référence a été enregistré et la
	 * position du robot a été recu (au moins une fois, peu importe l'ordre) ->
	 * ordre SAVEREFANGLE et SETPOSITION</br> 3 - La premiere case a été visité
	 * entièrement (au moins une fois) -> ordre CHECKFIRSTCASE</br>
	 * 
	 * @see ListOrder
	 * @see Robot#order
	 */
	public int choose() {
		this.currentOrder = this.get();

		if (!this.readyToGo && this.currentOrder != STOP) {
			if (!this.robot.getCompass().getCalDone()) {
				if (this.currentOrder != CALCOMPASS) {
					if (this.errOrder != CALCOMPASS) {
						System.out.println("[ERR]chooseOrder:cal");
						this.errOrder = CALCOMPASS;
					}
					this.currentOrder = STOP;
					return 1;
				}
			} else if (!this.robot.getMov().getRefAngleDone()
					|| !this.robot.getEnv().getInitPositionDone()) {
				if (this.currentOrder != SAVEREFANGLE
						&& this.currentOrder != SETPOSITION) {
					if (this.errOrder != SAVEREFANGLE) {
						System.out.println("[ERR]chooseOrder:refangle/pos");
						this.errOrder = SAVEREFANGLE;
					}
					this.currentOrder = STOP;
					return 1;
				}
			} else if (!this.checkFirstCaseDone) {
				if (this.currentOrder != CHECKFIRSTCASE) {
					if (this.errOrder != CHECKFIRSTCASE) {
						System.out.println("[ERR]chooseOrder:checkfirstcase");
						this.errOrder = CHECKFIRSTCASE;
					}
					this.currentOrder = STOP;
					this.readyToGo = true;
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * Méthode de test, algorithme d'exploration basique
	 */
	public void chooseExploration() {
		if (!this.readyToGo) {
			this.readyToGo = true;
		}
		if (!this.robot.getEnv().getFrontWallDetected()) {
			this.add(FORWARD);
		} else if (!this.robot.getEnv().getRightWallDetected()) {
			this.add(TURNR);
		} else if (!this.robot.getEnv().getLeftWallDetected()) {
			this.add(TURNL);
		} else {
			this.add(TURNB);
		}
		this.currentOrder = this.get();
	}

	/**
	 * Méthode de test, permet d'executer n'importe quel ordre sans verifier les
	 * conditions initiales
	 */
	public void chooseInsecurely() {
		if (!this.readyToGo) {
			this.readyToGo = true;
		}
		this.currentOrder = this.get();
	}

	/**
	 * Execute l'ordre demandé
	 * 
	 * @see Robot#chooseOrder()
	 * @see Robot#order
	 */
	public void execute() {
		if (this.currentOrder == FORWARD) {
			this.robot.getMov().moveForward();
		} else if (this.currentOrder == TURNR) {
			this.robot.getMov().turnRight();
		} else if (this.currentOrder == TURNL) {
			this.robot.getMov().turnLeft();
		} else if (this.currentOrder == TURNB) {
			this.robot.getMov().turnBack();
		} else if (this.currentOrder == STOP) {
			this.robot.getMov().stop();
		} else if (this.currentOrder == CALCOMPASS) {
			this.robot.getCompass().calibrate();
		} else if (this.currentOrder == SAVEREFANGLE) {
			this.robot.getMov().saveRefAngle();
		} else if (this.currentOrder == CHECKFIRSTCASE) {
			this.robot.getMov().turnLeft();
			this.robot.getMov().turnRight();
			this.robot.getEnv().saveCurrentCase(true);
		} else if (this.currentOrder == SETPOSITION) {
			this.robot.getEnv().setPosition();
		} else if (this.currentOrder == CLEARLISTORDER) {
			this.list.clear();
		} else if (this.currentOrder == WAITBUTTON) {
			this.pause();
		} else if (this.currentOrder == WAIT1SEC) {
			this.pauseTime(1000);
		}
	}
	
	
	public String print(int o) {
		if(o==STOP) {
			return "stop";
		}
		else if (o==FORWARD) {
			return "avancer";
		}
		else if (o==TURNR) {
			return "droite";
		}
		else if (o==TURNL) {
			return "gauche";
		}
		else if (o==TURNB) {
			return "demitour";
		}
		else if (o==CALCOMPASS) {
			return "cal boussole";
		}
		else if (o==SAVEREFANGLE) {
			return "sauv. ref";
		}
		else if (o==CHECKFIRSTCASE) {
			return "check1case";
		}
		else if (o==SETPOSITION) {
			return "MAJ position";
		}
		else if (o==CLEARLISTORDER) {
			return "vider liste ordres";
		}
		else if (o==WAITBUTTON) {
			return "attente bouton";
		}
		else if (o==WAIT1SEC) {
			return "attente 1sec";
		}
		else {
			return "";
		}
	}

	/**
	 * Attent que l'utilisateur appui sur un bouton
	 */
	public void pause() {
		System.out.println("pause:bouton?");
		Button.waitForAnyPress();
	}

	/**
	 * Pause en ms 
	 * @param ms
	 *            Temps à attendre en ms
	 */
	public void pauseTime(int ms) {
		long initTime = System.currentTimeMillis();
		long beepTime = initTime;
		Sound.beep();
		while ((System.currentTimeMillis() - initTime) < ms) {
			if ((System.currentTimeMillis() - beepTime) > 1000) {
				Sound.beep();
				beepTime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * @return la valeur de l'attribut listOrder
	 * @see ListOrder
	 * @see Robot#listOrder
	 */
	public ArrayList<Integer> getListOrder() {
		return this.list;
	}

	/**
	 * @return la valeur de l'attribut order
	 * @see Robot#order
	 */
	public int getCurrentOrder() {
		return this.currentOrder;
	}
	
	public boolean getReadyToGo() {
		return this.readyToGo;
	}
}
