package robot;

import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import threads.ThreadRobot;

/**
 * Cette classe contient tout les éléments necessaires à la gestion des ordres
 * comme la liste d'ordre, le choix des ordres, et l'execution de l'ordre.
 * 
 * @author Thomas
 * 
 */
public class Order {
	
	/**
	 * Ordre : immobilise le robot.
	 */
	public static final int STOP = 0;

	/**
	 * Ordre : fait avancer le robot de ~30cm.
	 */
	public static final int FORWARD = 1;

	/**
	 * Ordre : fait tourner le robot à gauche.
	 */
	public static final int TURNL = 2;

	/**
	 * Ordre : fait tourner le robot à droite.
	 */
	public static final int TURNR = 3;

	/**
	 * Ordre : fait faire un demi tour au robot.
	 */
	public static final int TURNB = 4;

	/**
	 * Ordre : lancer un calibrage de la boussole.
	 */
	public static final int CALCOMPASS = 5;

	/**
	 * Ordre : sauvegarde de l'angle de référence.
	 */
	public static final int SAVEREFANGLE = 6;

	/**
	 * Ordre : verifier les 4 murs de la case et l'ajouter à la liste de cases.
	 */
	public static final int CHECKFIRSTCASE = 7;
	
	/**
	 * Ordre : fixer les coordonnées du robot.
	 */
	public static final int SETPOSITION = 8;

	/**
	 * Ordre : attendre que l'utilisateur appuie sur le bouton ENTER du robot.
	 */
	public static final int WAITBUTTON = 10;

	/**
	 * Ordre : le robot ne fait rien pendant 1 seconde.
	 */
	public static final int WAIT1SEC = 11;
	
	/**
	 * Ordre : met le robot en mode rapide.
	 */
	public static final int FASTMODE = 15;
	
	/**
	 * Ordre : met le robot en mode normal d'exploration (mode par défaut).
	 */
	public static final int NORMALMODE = 13;
	
	/**
	 * Ordre : vVder la liste d'ordres.</br> Attention cette ordre est executé
	 * immédiatement à sa reception.
	 */
	public static final int CLEARLISTORDER = 9;
	
	/**
	 * Ordre : Envoyer la liste de cases.</br> Attention cette ordre est executé
	 * immédiatement à sa reception.
	 */
	public static final int SENDCASE = 12;
		
	/**
	 * Ordre : Envoyer l'état du robot.</br> Attention cette ordre est executé
	 * immédiatement à sa reception.
	 */
	public static final int SENDBUSY = 16;
	
	/**
	 * Attribut représentant l'ordre actuel du robot.
	 */
	private int currentOrder;

	/**
	 * Attribut qui fait référence à la tache principale du robot.
	 */
	private ThreadRobot tRobot;

	/**
	 * Attribut permettant de connaitre l'erreur courante sur l'ordre (utile
	 * lors des vérifications des conditions initiales).
	 */
	private int errOrder;

	/**
	 * Attribut permettant de savoir si le robot à rempli les conditions
	 * initiales pour commencer à se déplacer.
	 */
	private boolean readyToGo;

	/**
	 * Attribut permettant de savoir si le robot à verifié au moins une fois les
	 * 4 murs de sa case initiale.
	 */
	private boolean checkFirstCaseDone;
	
	/**
	 * Attribut indiquant si le robot est en mouvement ou non.
	 */
	private int isBusy;

	/**
	 * Attribut contenant la liste d'ordres.
	 * 
	 * @see ArrayList
	 */
	private ArrayList<Integer> list;

	/**
	 * Constructeur de Order.
	 * 
	 * @param r
	 *            objet représentant le thread principal du robot.
	 */
	public Order(ThreadRobot r) {
		this.tRobot = r;
		this.list = new ArrayList<Integer>();
		this.currentOrder = STOP;
		this.readyToGo = false;
		this.checkFirstCaseDone = false;
		this.errOrder = -1;
		this.isBusy=1;
	}

	/**
	 * Ajoute un ordre à la fin de la liste.
	 * 
	 * @param order
	 *            Ordre à insérer dans la liste.
	 * @return 0 si l'opération s'est bien déroulée.
	 */
	public synchronized int add(int o) {
		if (o == STOP || o == FORWARD || o == TURNL
				|| o == TURNR || o == TURNB
				|| o == CALCOMPASS || o == SAVEREFANGLE
				|| o == CHECKFIRSTCASE || o == WAIT1SEC 
				|| o == WAITBUTTON || o == FASTMODE 
				|| o == NORMALMODE || o == SETPOSITION) {
			this.isBusy=1;
			this.list.add(o);
			return 0;
		} else {
			System.out.println("addOrder:err param "+o);
			return 1;
		}
	}

	/**
	 * Retire et renvoit l'ordre le plus ancien présent dans la liste.
	 * 
	 * @return l'ordre le plus ancien présent dans la liste ou l'ordre STOP si la liste est vide.
	 */
	private synchronized Integer get() {
		if (this.list.isEmpty()) {
			if(this.isBusy!=0) {
				this.isBusy=0;
				if(this.tRobot.getMov().getFastMode()) {
					this.tRobot.getEnv().saveCurrentCase();
				}
			}
			return Order.STOP;
		} else {
			return this.list.remove(0);
		}
	}
	
	/**
	 * Renvoit l'ordre souhaité en argument s' il existe (sans le retirer de la liste).
	 * L'index 0 correspond au prochain ordre executé.
	 * @param idx
	 * 		index de l'ordre à récupérer.
	 * @return l'ordre désiré, -1 s'il n'y a pas d'ordre à cet index.
	 */
	public synchronized Integer checkOrder(int idx) {
		if(this.list.size()>idx) {
			return this.list.get(idx);
		} else {
			return -1;
		}
	}
	
	/**
	 * @return le nombre d'ordre en attente.
	 */
	public synchronized int getListSize() {
		return this.list.size();
	}

	/**
	 * Vide entierement la liste.
	 */
	public synchronized void clear() {
		this.list.clear();
	}

	/**
	 * Vérifie si la liste est vide.
	 * 
	 * @return true si la liste est vide.
	 */
	public synchronized boolean isEmpty() {
		if (this.list.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Choisit un ordre en se basant sur la liste d'ordre recu du superviseur.
	 * On vérifie que les conditions initiales soient bonnes si on veut déplacer
	 * le robot, i.e. : </br> 1 - La boussole a été calibrée -> ordre
	 * CALCOMPASS.</br> 2 - L'angle de référence a été enregistré et la position
	 * du robot a été recu -> ordre SAVEREFANGLE.</br> 3 - La premiere case a
	 * été visité entièrement -> ordre CHECKFIRSTCASE.</br> L'ordre choisi est
	 * placé dans currentOrder.
	 */
	public int choose() {
		this.currentOrder = this.get();

		if (!this.readyToGo && this.currentOrder != STOP) {
			if (!this.tRobot.getCompass().getCalDone()) {
				if (this.currentOrder != CALCOMPASS) {
					if (this.errOrder != CALCOMPASS) {
						System.out.println("chooseOrder:err cal");
						this.errOrder = CALCOMPASS;
					}
					this.currentOrder = STOP;
					return 1;
				}
			} else if (!this.tRobot.getMov().getRefAngleDone()
					|| !this.tRobot.getEnv().getInitPositionDone()) {
				if (this.currentOrder != SAVEREFANGLE) {
					if (this.errOrder != SAVEREFANGLE) {
						System.out.println("chooseOrder:err refangle/pos");
						this.errOrder = SAVEREFANGLE;
					}
					this.currentOrder = STOP;
					return 1;
				}
			} else if (!this.checkFirstCaseDone) {
				if (this.currentOrder != CHECKFIRSTCASE) {
					if (this.errOrder != CHECKFIRSTCASE) {
						System.out.println("chooseOrder:err checkfirstcase");
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
	 * Méthode de test, algorithme d'exploration basique.
	 */
	public void chooseExploration() {
		if (!this.readyToGo) {
			this.readyToGo = true;
		}
		if (!this.tRobot.getEnv().getFrontWallDetected()) {
			this.add(FORWARD);
		} else if (!this.tRobot.getEnv().getRightWallDetected()) {
			this.add(TURNR);
		} else if (!this.tRobot.getEnv().getLeftWallDetected()) {
			this.add(TURNL);
		} else {
			this.add(TURNB);
		}
		this.currentOrder = this.get();
	}

	/**
	 * Méthode de test, permet d'executer n'importe quel ordre sans verifier les
	 * conditions initiales.
	 */
	public void chooseInsecurely() {
		if (!this.readyToGo) {
			this.readyToGo = true;
		}
		this.currentOrder = this.get();
	}

	/**
	 * Execute l'ordre contenu dans currentOrder.
	 */
	public void execute() {
		if (this.currentOrder == FORWARD) {
			this.tRobot.getMov().moveForward();
		} else if (this.currentOrder == TURNR) {
			this.tRobot.getMov().turnRight();
		} else if (this.currentOrder == TURNL) {
			this.tRobot.getMov().turnLeft();
		} else if (this.currentOrder == TURNB) {
			this.tRobot.getMov().turnBack();
		} else if (this.currentOrder == STOP) {
			this.tRobot.getMov().stop();
		} else if (this.currentOrder == CALCOMPASS) {
			this.tRobot.getCompass().calibrate();
		} else if (this.currentOrder == SAVEREFANGLE) {
			this.tRobot.getMov().saveRefAngle();
		} else if (this.currentOrder == CHECKFIRSTCASE) {
			this.tRobot.getMov().turnLeft();
			this.tRobot.getMov().turnRight();
			this.tRobot.getEnv().saveCurrentCase();
		} else if (this.currentOrder == WAITBUTTON) {
			this.pause();
		} else if (this.currentOrder == WAIT1SEC) {
			this.pauseTime(1000);
		} else if (this.currentOrder == FASTMODE) {
			this.tRobot.getMov().setFastMode(true);
		} else if (this.currentOrder == NORMALMODE) {
			this.tRobot.getMov().setFastMode(false);
		} else if (this.currentOrder == SETPOSITION) {
			this.tRobot.getEnv().setPosition();
		}
		
	}

	/**
	 * Renvoit une chaine de caractères correspondant à l'ordre en argument.
	 * @param o
	 * 		l'ordre à afficher.
	 * @return l'ordre sous la forme d'un String.
	 */
	public String print(int o) {
		if (o == STOP) {
			return "stop";
		} else if (o == FORWARD) {
			return "avancer";
		} else if (o == TURNR) {
			return "droite";
		} else if (o == TURNL) {
			return "gauche";
		} else if (o == TURNB) {
			return "demitour";
		} else if (o == CALCOMPASS) {
			return "cal boussole";
		} else if (o == SAVEREFANGLE) {
			return "sauv. ref";
		} else if (o == CHECKFIRSTCASE) {
			return "check1case";
		} else if (o == CLEARLISTORDER) {
			return "clear orders";
		} else if (o == WAITBUTTON) {
			return "wait bouton";
		} else if (o == WAIT1SEC) {
			return "wait 1sec";
		} else if (o == FASTMODE) {
			return "fast mode";
		} else if (o == NORMALMODE) {
			return "normal mode";
		} else if (o == SETPOSITION) {
			return "setPos";
		} else if (o == SENDCASE) {
			return "sendcase";
		} else if (o == SENDBUSY) {
			return "sendbusy";
		} else {
			return String.valueOf(o);
		}
	}

	/**
	 * Attent que l'utilisateur appuie sur un bouton.
	 */
	public void pause() {
		System.out.println("pause:bouton?");
		Button.waitForAnyPress();
	}

	/**
	 * Pause en ms.
	 * 
	 * @param ms
	 *            Temps à attendre en ms.
	 */
	public void pauseTime(int ms) {
		long initTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - initTime) < ms) {}
	}

	/**
	 * @return la valeur de l'attribut list.
	 */
	public synchronized ArrayList<Integer> getList() {
		return this.list;
	}

	/**
	 * @return la valeur de l'attribut currentOrder.
	 */
	public int getCurrentOrder() {
		return this.currentOrder;
	}
	
	/**
	 * @return la valeur de l'attribut readyToGo.
	 */
	public boolean getReadyToGo() {
		return this.readyToGo;
	}
	
	/**
	 * @return 0 si le robot ne fait rien.
	 */
	public synchronized int getIsBusy() {
		return this.isBusy;
	}
}
