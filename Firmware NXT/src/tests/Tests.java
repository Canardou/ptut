package tests;

import lejos.nxt.Button;
import env.*;
import communication.*;
import robot.*;
import sensors.*;
import threads.*;
import lejos.nxt.comm.Bluetooth;

/**
 * Méthodes de tests
 * 
 * @author Thomas
 * 
 */
public class Tests {

	private ThreadRobot tRobot;
	private ThreadCom tCom;

	public Tests(int test) {
		this.tRobot = new ThreadRobot();
		this.tCom = new ThreadCom(tRobot);
		int err;

		if (test == 0) {
			err = this.test101();
			if (err != 0) {
				System.out.println("test101:" + err);
			} else {
				System.out.println("ok");
			}
			this.tRobot.getOrder().pause();
		}
		if (test == 1) {
			this.test1();
		} else if (test == 2) {
			this.test2();
		} else if (test == 3) {
			this.test3();
		} else if (test == 4) {
			this.test4();
		} else if (test == 5) {
			this.test5();
		} else if (test == 6) {
			this.test6();
		} else if (test == 7) {
			this.test7();
		} else if (test == 8) {
			this.test8();
		} else if (test == 9) {
			this.test9();
		} else if (test == 10) {
			this.test10();
		} else if (test == 11) {
			this.test11();
		} else if (test == 12) {
			this.test12();
		} else if (test == 13) {
			this.test13();
		} else if (test == 14) {
			this.test14();
		} else if (test == 15) {
			this.test15();
		} else if (test == 16) {
			this.test16();
		}
	}

	/**
	 * Affiche les valeurs moyennes renvoyées par les 2 sonars et la présence
	 * eventuelle des murs (droite et gauche).
	 */
	public void test1() {
		System.out.println("test1:sonar droite et gauche");
		this.tRobot.getOrder().pause();
		while (!Button.ESCAPE.isDown()) {
			this.tRobot.getEnv().checkSensors();
			System.out.print(this.tRobot.getRightSonar().getMoyData());
			if (this.tRobot.getEnv().getRightWallDetected()) {
				System.out.print("mur");
			}
			System.out.print("\t"
					+ this.tRobot.getLeftFrontSonar().getMoyData());
			if (this.tRobot.getEnv().getLeftWallDetected()) {
				System.out.print("mur");
			}
			System.out.println("");
		}
	}

	/**
	 * Affiche la valeur moyenne renvoyée par le sonar avant et la présence
	 * eventuelle d'un mur avant.
	 */
	public void test2() {
		System.out.println("test2:sonar avant");
		this.tRobot.getOrder().pause();
		this.tRobot.getEnv().sonarFront();
		while (!Button.ESCAPE.isDown()) {
			this.tRobot.getEnv().checkSensors();
			System.out.print(this.tRobot.getLeftFrontSonar().getMoyData());
			if (this.tRobot.getEnv().getFrontWallDetected()) {
				System.out.print("\t mur");
			}
			System.out.println("");
		}
		this.tRobot.getEnv().sonarLeft();
	}

	/**
	 * Affiche la valeur moyenne renvoyée par la boussole.
	 */
	public void test3() {
		System.out.println("test3:demarrage");
		this.tRobot.getOrder().pause();
		while (!Button.ESCAPE.isDown()) {
			this.tRobot.getEnv().checkSensors();
			System.out.println(this.tRobot.getCompass().getMoyData());
		}
	}

	/**
	 * Test du capteur de lumière.
	 */
	public void test4() {
		System.out.println("test4:demarrage");
		while (!Button.ESCAPE.isDown()) {

		}
	}

	/**
	 * Affiche les valeurs moyennes renvoyées par les 2 sonars et la présence
	 * eventuelle des murs (droite et gauche). Durant l'execution normale des
	 * taches tRobot et tCom.
	 */
	public void test5() {
		System.out.println("test5:demarrage");
		Init.initTest(this.tRobot);
		tRobot.start();
		tCom.start();
		while (!Button.ESCAPE.isDown()) {
			System.out.print(this.tRobot.getRightSonar().getMoyData());
			if (this.tRobot.getEnv().getRightWallDetected()) {
				System.out.print("mur");
			}
			System.out.print("\t"
					+ this.tRobot.getLeftFrontSonar().getMoyData());
			if (this.tRobot.getEnv().getLeftWallDetected()) {
				System.out.print("mur");
			}
			System.out.println("");
		}
	}

	/**
	 * Test des arcs.
	 */
	public void test6() {
		System.out.println("test6:demarrage");
		Init.init(this.tRobot);
		while (!Button.ESCAPE.isDown()) {
			this.tRobot.getMov().getDiffPilot().arcForward(5);
		}
	}

	/**
	 * Test de la régulation (avance).
	 */
	public void test7() {
		System.out.println("test7:demarrage");
		Init.initTest(this.tRobot);
		while (!Button.ESCAPE.isDown()) {
			this.tRobot.getMov().moveForward();
		}
	}

	/**
	 * Test des virages.
	 */
	public void test8() {
		System.out.println("test8:demarrage");
		Init.initTest(this.tRobot);
		tRobot.getOrder().add(Order.TURNL);
		tRobot.getOrder().add(Order.WAIT1SEC);
		tRobot.getOrder().add(Order.TURNR);
		tRobot.getOrder().add(Order.WAIT1SEC);
		tRobot.getOrder().add(Order.TURNB);
		tRobot.getOrder().add(Order.WAIT1SEC);
		tRobot.getOrder().add(Order.TURNB);
		tRobot.getOrder().add(Order.WAITBUTTON);
		tRobot.start();
		tCom.start();
	}

	/**
	 * Affiche si les threads sont en execution ou non.
	 */
	public void test9() {
		System.out.println("test9:demarrage");
		Init.initTest(this.tRobot);
		tRobot.start();
		tCom.start();
		while (!Button.ESCAPE.isDown()) {
			if (this.tRobot.isAlive()) {
				System.out.print("tRobot");
			}
			if (this.tCom.isAlive()) {
				System.out.println("\ttCom");
			} else {
				System.out.println("");
			}
		}
	}

	/**
	 * Test de la régulation (avance) en fastmode.
	 */
	public void test10() {
		System.out.println("test10:demarrage");
		this.tRobot.getOrder().pause();
		Init.initTest(this.tRobot);
		this.tRobot.getMov().setFastMode(true);
		while (!Button.ESCAPE.isDown()) {
			this.tRobot.getMov().moveForward();
		}
	}

	/**
	 * Test des forwards et virages en mode normal.
	 */
	public void test11() {
		System.out.println("test11:demarrage");
		Init.initTest(this.tRobot);
		tRobot.getOrder().add(Order.TURNL);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNL);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNL);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNL);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNL);
		tRobot.start();
		tCom.start();
		while (!Button.ESCAPE.isDown()) {
		}
	}
	
	/**
	 * Test du fastMode.
	 */
	public void test12() {
		System.out.println("test12:demarrage");
		Init.initTest(this.tRobot);
		this.tRobot.getMov().setFastMode(true);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNR);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNR);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNR);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNL);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNR);
		tRobot.getOrder().add(Order.FORWARD);
		tRobot.getOrder().add(Order.TURNL);
		tRobot.getOrder().add(Order.WAITBUTTON);
		tRobot.start();
		tCom.start();
	}
	
	/**
	 * Test du isBusy.
	 */
	public void test13() {
		System.out.println("test13:demarrage");
		Init.initTest(this.tRobot);
		tRobot.start();
		tCom.start();
		while (!Button.ESCAPE.isDown()) {
			System.out.println(this.tRobot.getOrder().getIsBusy());
		}
	}
	
	/**
	 * Test du move forward.
	 */
	public void test14() {
		System.out.println("test14:demarrage");
		Init.initTest(this.tRobot);
		while (!Button.ESCAPE.isDown()) {
			this.tRobot.getMov().moveForward();
		}
	}
	
	/**
	 * Test pour le bluetooth.
	 */
	public void test15() {
		System.out.println("test15:demarrage");
		System.out.println(Bluetooth.getFriendlyName());
		System.out.println(Bluetooth.getLocalAddress());
		while (!Button.ESCAPE.isDown()) {}
	}
	
	public void test16() {
		System.out.println("test16:demarrage");
		this.tRobot.getOrder().pause();
		tRobot.getOrder().add(Order.WAITBUTTON);
		tRobot.getOrder().chooseInsecurely();
		tRobot.getOrder().execute();
		tRobot.getOrder().add(Order.CALCOMPASS);
		tRobot.getOrder().chooseInsecurely();
		tRobot.getOrder().execute();
		while (!Button.ESCAPE.isDown()) {}
	}

	/**
	 * Test sur la liste de cases
	 * 
	 * @return 0 si le test est réussi
	 */
	public int test101() {
		ListCase list = new ListCase(this.tRobot);
		Case caseTemp;
		if (!list.isEmpty()) {
			return 1;
		}
		if (list.addCase(-1, 0, 0, false, false, false, false) == 0) {
			return 2;
		}
		if (list.addCase(0, -1, 0, false, false, false, false) == 0) {
			return 3;
		}
		if (list.addCase(0, 0, -1, false, false, false, false) == 0) {
			return 4;
		}

		if (list.addCase(1, 0, Case.UP, false, false, false, false) != 0) {
			return 5;
		}
		if (list.isEmpty()) {
			return 6;
		}
		if (list.addCase(2, 0, Case.LEFT, false, false, false, false) != 0) {
			return 7;
		}
		if (list.addCase(3, 0, Case.DOWN, false, false, false, false) != 0) {
			return 8;
		}
		if (list.addCase(4, 0, Case.RIGHT, false, false, false, false) != 0) {
			return 9;
		}

		if (list.addCase(1, 1, Case.UP, true, false, false, false) != 0) {
			return 10;
		}
		if (list.addCase(2, 1, Case.LEFT, true, false, false, false) != 0) {
			return 11;
		}
		if (list.addCase(3, 1, Case.DOWN, true, false, false, false) != 0) {
			return 12;
		}
		if (list.addCase(4, 1, Case.RIGHT, true, false, false, false) != 0) {
			return 13;
		}

		if (list.addCase(1, 2, Case.UP, false, true, false, false) != 0) {
			return 14;
		}
		if (list.addCase(2, 2, Case.LEFT, false, true, false, false) != 0) {
			return 15;
		}
		if (list.addCase(3, 2, Case.DOWN, false, true, false, false) != 0) {
			return 16;
		}
		if (list.addCase(4, 2, Case.RIGHT, false, true, false, false) != 0) {
			return 17;
		}

		if (list.addCase(1, 3, Case.UP, false, false, true, false) != 0) {
			return 18;
		}
		if (list.addCase(2, 3, Case.LEFT, false, false, true, false) != 0) {
			return 19;
		}
		if (list.addCase(3, 3, Case.DOWN, false, false, true, false) != 0) {
			return 20;
		}
		if (list.addCase(4, 3, Case.RIGHT, false, false, true, false) != 0) {
			return 21;
		}

		if (list.addCase(1, 4, Case.UP, false, false, false, true) != 0) {
			return 22;
		}
		if (list.addCase(2, 4, Case.LEFT, false, false, false, true) != 0) {
			return 23;
		}
		if (list.addCase(3, 4, Case.DOWN, false, false, false, true) != 0) {
			return 24;
		}
		if (list.addCase(4, 4, Case.RIGHT, false, false, false, true) != 0) {
			return 25;
		}

		caseTemp = list.getCase();
		if (caseTemp.getX() != 1 || caseTemp.getY() != 0
				|| caseTemp.getCompo() != 16) {
			return 26;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 2 || caseTemp.getY() != 0
				|| caseTemp.getCompo() != 16) {
			return 27;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 3 || caseTemp.getY() != 0
				|| caseTemp.getCompo() != 16) {
			return 28;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 4 || caseTemp.getY() != 0
				|| caseTemp.getCompo() != 16) {
			return 29;
		}

		caseTemp = list.getCase();
		if (caseTemp.getX() != 1 || caseTemp.getY() != 1
				|| caseTemp.getCompo() != (16 + 1)) {
			return 30;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 2 || caseTemp.getY() != 1
				|| caseTemp.getCompo() != (16 + 2)) {
			return 31;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 3 || caseTemp.getY() != 1
				|| caseTemp.getCompo() != (16 + 4)) {
			return 32;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 4 || caseTemp.getY() != 1
				|| caseTemp.getCompo() != (16 + 8)) {
			return 33;
		}

		caseTemp = list.getCase();
		if (caseTemp.getX() != 1 || caseTemp.getY() != 2
				|| caseTemp.getCompo() != (16 + 2)) {
			return 34;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 2 || caseTemp.getY() != 2
				|| caseTemp.getCompo() != (16 + 4)) {
			return 35;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 3 || caseTemp.getY() != 2
				|| caseTemp.getCompo() != (16 + 8)) {
			return 36;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 4 || caseTemp.getY() != 2
				|| caseTemp.getCompo() != (16 + 1)) {
			return 37;
		}

		caseTemp = list.getCase();
		if (caseTemp.getX() != 1 || caseTemp.getY() != 3
				|| caseTemp.getCompo() != (16 + 4)) {
			return 38;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 2 || caseTemp.getY() != 3
				|| caseTemp.getCompo() != (16 + 8)) {
			return 39;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 3 || caseTemp.getY() != 3
				|| caseTemp.getCompo() != (16 + 1)) {
			return 40;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 4 || caseTemp.getY() != 3
				|| caseTemp.getCompo() != (16 + 2)) {
			return 41;
		}

		caseTemp = list.getCase();
		if (caseTemp.getX() != 1 || caseTemp.getY() != 4
				|| caseTemp.getCompo() != (16 + 8)) {
			return 42;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 2 || caseTemp.getY() != 4
				|| caseTemp.getCompo() != (16 + 1)) {
			return 43;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 3 || caseTemp.getY() != 4
				|| caseTemp.getCompo() != (16 + 2)) {
			return 44;
		}
		caseTemp = list.getCase();
		if (caseTemp.getX() != 4 || caseTemp.getY() != 4
				|| caseTemp.getCompo() != (16 + 4)) {
			return 45;
		}

		if (!list.isEmpty()) {
			return 46;
		}

		if (list.addCase(1, 0, Case.UP, false, false, false, false) != 0) {
			return 47;
		}
		if (list.addCase(1, 0, Case.UP, false, false, false, false) != 0) {
			return 48;
		}

		if (list.isEmpty()) {
			return 49;
		}

		list.clear();

		if (!list.isEmpty()) {
			return 50;
		}

		return 0;
	}

	/**
	 * Test de la classe Order
	 * 
	 * @return 0 si le test est réussi
	 */
	public int test102() {
		return 0;
	}

	/**
	 * Test de la classe Movement
	 * 
	 * @return 0 si le test est réussi
	 */
	public int test103() {
		return 0;
	}

	/**
	 * 
	 * @return 0 si le test est réussi
	 */
	public int test104() {
		return 0;
	}

	/**
	 * 
	 * @return 0 si le test est réussi
	 */
	public int test105() {
		return 0;
	}

	/**
	 * 
	 * @return 0 si le test est réussi
	 */
	public int test106() {
		return 0;
	}

}
