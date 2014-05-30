package tests;

import lejos.nxt.Button;
import lejos.nxt.comm.Bluetooth;
import robot.Init;
import robot.evenements.Ordre;
import robot.taches.TacheCom;
import robot.taches.TachePrincipale;

/**
 * Méthodes de tests
 * 
 * @author Thomas
 * 
 */
public class Tests {

	private TachePrincipale tPrincipale;
	private TacheCom tCom;
	private Init init;

	public Tests(Init i, int test) {
		this.tPrincipale = new TachePrincipale();
		this.tCom = new TacheCom(tPrincipale);
		this.init=i;

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
		this.tPrincipale.getOrdre().pauseBouton();
		while (!Button.ESCAPE.isDown()) {
			this.tPrincipale.getCapteurs().miseAJour(this.tPrincipale);
			System.out.print(this.tPrincipale.getCapteurs().getSonarDroit().getMoyData());
			if (this.tPrincipale.getEnv().getMurDroit()) {
				System.out.print("mur");
			}
			System.out.print("\t"
					+ this.tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData());
			if (this.tPrincipale.getEnv().getMurGauche()) {
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
		this.tPrincipale.getOrdre().pauseBouton();
		this.tPrincipale.getCapteurs().tournerSonarDevant(this.tPrincipale);
		while (!Button.ESCAPE.isDown()) {
			this.tPrincipale.getCapteurs().miseAJour(this.tPrincipale);
			System.out.print(this.tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData());
			if (this.tPrincipale.getEnv().getMurAvant()) {
				System.out.print("\t mur");
			}
			System.out.println("");
		}
		this.tPrincipale.getCapteurs().tournerSonarAGauche(this.tPrincipale);
	}

	/**
	 * Affiche la valeur moyenne renvoyée par la boussole.
	 */
	public void test3() {
		System.out.println("test3:demarrage");
		this.tPrincipale.getOrdre().pauseBouton();
		while (!Button.ESCAPE.isDown()) {
			this.tPrincipale.getCapteurs().miseAJour(this.tPrincipale);
			System.out.println(this.tPrincipale.getCapteurs().getBoussole().getMoyData());
		}
	}

	/**
	 * Test du capteur de lumière.
	 */
	public void test4() {
		System.out.println("test4:demarrage");
		while (!Button.ESCAPE.isDown()) {
			this.tPrincipale.getCapteurs().miseAJour(this.tPrincipale);
			this.tPrincipale.getEnv().setCibleIci(this.tPrincipale.getCapteurs().getCapteurLumiere().getMoyData());
			System.out.println(this.tPrincipale.getCapteurs().getCapteurLumiere().getMoyData());
			
		}
	}

	/**
	 * Affiche les valeurs moyennes renvoyées par les 2 sonars et la présence
	 * eventuelle des murs (droite et gauche). Durant l'execution normale des
	 * taches tRobot et tCom.
	 */
	public void test5() {
		System.out.println("test5:demarrage");
		this.init.initTest(this.tPrincipale);
		tPrincipale.start();
		tCom.start();
		while (!Button.ESCAPE.isDown()) {
			System.out.print(this.tPrincipale.getCapteurs().getSonarDroit().getMoyData());
			if (this.tPrincipale.getEnv().getMurDroit()) {
				System.out.print("mur");
			}
			System.out.print("\t"
					+ this.tPrincipale.getCapteurs().getSonarAvantGauche().getMoyData());
			if (this.tPrincipale.getEnv().getMurGauche()) {
				System.out.print("mur");
			}
			System.out.println("");
		}
	}

	/**
	 * Test des arcs.
	 */
	public void test6() {
		this.init.init(this.tPrincipale);
		while (!Button.ESCAPE.isDown()) {
			this.tPrincipale.getMouv().getDiffPilot().arcForward(5);
		}
	}

	/**
	 * Test de la régulation (avance).
	 */
	public void test7() {
		System.out.println("test7:demarrage");
		this.init.initTest(this.tPrincipale);
		while (!Button.ESCAPE.isDown()) {
			this.tPrincipale.getMouv().avancer(this.tPrincipale);
		}
	}

	/**
	 * Test des virages.
	 */
	public void test8() {
		System.out.println("test8:demarrage");
		this.init.initTest(this.tPrincipale);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_GAUCHE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.ATTENDRE_1SEC);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_DROITE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.ATTENDRE_1SEC);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.DEMITOUR);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.ATTENDRE_1SEC);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.DEMITOUR);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.ATTENDRE_BOUTON);
		tPrincipale.start();
		tCom.start();
	}

	/**
	 * Affiche si les threads sont en execution ou non.
	 */
	public void test9() {
		System.out.println("test9:demarrage");
		this.init.initTest(this.tPrincipale);
		tPrincipale.start();
		tCom.start();
		while (!Button.ESCAPE.isDown()) {
			if (this.tPrincipale.isAlive()) {
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
		this.tPrincipale.getOrdre().pauseBouton();
		this.init.initTest(this.tPrincipale);
		this.tPrincipale.getMouv().setFastMode(true);
		while (!Button.ESCAPE.isDown()) {
			this.tPrincipale.getMouv().avancer(this.tPrincipale);
		}
	}

	/**
	 * Test des forwards et virages en mode normal.
	 */
	public void test11() {
		System.out.println("test11:demarrage");
		this.init.initTest(this.tPrincipale);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_GAUCHE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_GAUCHE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_GAUCHE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_GAUCHE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_GAUCHE);
		tPrincipale.start();
		tCom.start();
		while (!Button.ESCAPE.isDown()) {
		}
	}
	
	/**
	 * Test du fastMode.
	 */
	public void test12() {
		System.out.println("test12:demarrage");
		this.init.initTest(this.tPrincipale);
		this.tPrincipale.getMouv().setFastMode(true);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_DROITE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_DROITE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_DROITE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_GAUCHE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_DROITE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.AVANCER);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.TOURNER_A_GAUCHE);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.ATTENDRE_BOUTON);
		tPrincipale.start();
		tCom.start();
	}
	
	/**
	 * Test du isBusy.
	 */
	public void test13() {
		System.out.println("test13:demarrage");
		this.init.initTest(this.tPrincipale);
		tPrincipale.start();
		tCom.start();
		while (!Button.ESCAPE.isDown()) {
			System.out.println(this.tPrincipale.getOrdre().getIsBusy());
		}
	}
	
	/**
	 * Test du move forward.
	 */
	public void test14() {
		System.out.println("test14:demarrage");
		this.init.initTest(this.tPrincipale);
		while (!Button.ESCAPE.isDown()) {
			this.tPrincipale.getMouv().avancer(this.tPrincipale);
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
		this.tPrincipale.getOrdre().pauseBouton();
		tPrincipale.getOrdre().ajouterOrdre(Ordre.ATTENDRE_BOUTON);
		tPrincipale.getOrdre().choisirOrdre(this.tPrincipale);
		tPrincipale.getOrdre().executerOrdre(this.tPrincipale);
		tPrincipale.getOrdre().ajouterOrdre(Ordre.CALIBRER_BOUSSOLE);
		tPrincipale.getOrdre().choisirOrdre(this.tPrincipale);
		tPrincipale.getOrdre().executerOrdre(this.tPrincipale);
		while (!Button.ESCAPE.isDown()) {}
	}
}