package tests;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import env.*;
import communication.*;
import orders.*;
import robot.*;
import sensors.*;
import threads.*;

/**
 * M�thodes de tests
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
		
		if(test==0) {
			err=this.test101();
			if(err!=0) {
				System.out.println("test101:"+err);
			} else {
				System.out.println("ok");
			}
			this.tRobot.getOrder().pause();
		}
		if(test==1) {
			this.test1();
		} else if(test==2) {
			this.test2();
		} else if(test==3) {
			this.test3();
		} else if(test==4) {
			this.test4();
		} else if(test==5) {
			this.test5();
		} else if(test==6) {
			this.test6();
		} else if(test==7) {
			this.test7();
		} else if(test==8){
			this.test8();
		}
		
	}
	
	/**
	 * Affiche les valeurs moyennes renvoy�es par les 2 sonars et la pr�sence eventuelle des murs (droite et gauche)
	 */
	public void test1() {
		System.out.println("test1:sonar droite et gauche");
		this.tRobot.getOrder().pause();
		while(!Button.ESCAPE.isDown()) {
			this.tRobot.getEnv().check2WallsCompass();
			System.out.print(this.tRobot.getRightSonar().getMoyData());	
			if(this.tRobot.getEnv().getRightWallDetected()) {
				System.out.print("mur");
			}
			System.out.print("\t"+this.tRobot.getLeftFrontSonar().getMoyData());
			if(this.tRobot.getEnv().getLeftWallDetected()) {
				System.out.print("mur");
			} System.out.println("");
		}
	}
	
	/**
	 * Affiche la valeur moyenne renvoy�e par le sonar avant et la pr�sence eventuelle d'un mur avant
	 */
	public void test2() {
		System.out.println("test2:sonar avant");
		this.tRobot.getOrder().pause();
		this.tRobot.getEnv().sonarFront();
		while(!Button.ESCAPE.isDown()) {
			this.tRobot.getEnv().checkFrontWallCompass();
			System.out.print(this.tRobot.getLeftFrontSonar().getMoyData());	
			if(this.tRobot.getEnv().getFrontWallDetected()) {
				System.out.print("\t mur");
			}
			System.out.println("");
		}
		this.tRobot.getEnv().sonarLeft();
	}
	
	/**
	 * Affiche la valeur moyenne renvoy�e par la boussole
	 */
	public void test3() {
		System.out.println("test3:demarrage");
		this.tRobot.getOrder().pause();
		while(!Button.ESCAPE.isDown()) {
			this.tRobot.getEnv().check2WallsCompass();
			System.out.println(this.tRobot.getCompass().getMoyData());			
		}
	}
	
	/**
	 * Affiche le type de r�gulation appliqu�, l'erreur de distance, l'erreur d'angle ainsi que la pr�sence de mur
	 */
	public void test4() {
		double errAngle;
		double errDist;
		int reg;
		System.out.println("test4:demarrage");
		this.tRobot.getOrder().pause();	
		this.tRobot.getMov().saveRefAngle();
		while(!Button.ESCAPE.isDown()) {
			this.tRobot.getEnv().check2WallsCompass();
			errDist	  = this.tRobot.getLeftFrontSonar().getMoyData() - this.tRobot.getRightSonar().getMoyData() + 1 ;
			errAngle  = this.tRobot.getMov().getRefAngle() - this.tRobot.getCompass().getMoyData();
			if(errAngle<-180) {
				errAngle = errAngle+360;
			}
			else if (errAngle>180) {
				errAngle = errAngle-360;
			}
			reg=this.tRobot.getMov().chooseReg(errAngle,errDist);
			if(reg==Movement.LEFTREG) {
				System.out.print("LE ");
			}
			else if(reg==Movement.MIDLEFTREG) {
				System.out.print("ML ");
			}
			else if(reg==Movement.NOREG) {
				System.out.print("NO ");
			}
			else if(reg==Movement.MIDRIGHTREG) {
				System.out.print("MR ");
			}
			else if(reg==Movement.RIGHTREG) {
				System.out.print("RI ");
			}
			if(this.tRobot.getEnv().getRightWallDetected()) {
				System.out.print("R");
			}
			else {
				System.out.print(" ");
			}
			if(this.tRobot.getEnv().getLeftWallDetected()) {
				System.out.print("L ");
			}
			else {
				System.out.print("  ");
			}	
			System.out.print((double)Math.round(errAngle * 100) / 100);
			System.out.println(" "+errDist);
		}
	}
	
	/**
	 * Affiche la valeur des sonars durant l'execution du code normal du robot
	 */
	public void test5() {
		System.out.println("test5:demarrage");
		this.tRobot.getOrder().pause();
		Init.initTest(this.tRobot);
		tRobot.start();
		tCom.start();
		while(!Button.ESCAPE.isDown()) {
			System.out.print(this.tRobot.getRightSonar().getMoyData());	
			if(this.tRobot.getEnv().getRightWallDetected()) {
				System.out.print("mur");
			}
			System.out.print("\t"+this.tRobot.getLeftFrontSonar().getMoyData());
			if(this.tRobot.getEnv().getLeftWallDetected()) {
				System.out.print("mur");
			} System.out.println("");
		}
	}
	
	public void test6() {
		
	}
	
	public void test7() {
		
	}
	
	public void test8() {
		System.out.println("test8: lumière");
		this.tRobot.getLightSensor().refresh();
		while(!Button.ESCAPE.isDown()) {
			this.tRobot.getLightSensor().refresh();
			if(this.tRobot.getLightSensor().getMoyData()>580){
				Sound.beep();
			}
		}
	}
	
	/**
	 * Test sur la liste de cases
	 * @return
	 */
	public int test101() {
		ListCase list = new ListCase();
		Case caseTemp;
		if(!list.isEmpty()) {
			return 1;
		}
		if(list.addCase(-1, 0, 0, false, false, false, false)==0) {
			return 2;
		}
		if(list.addCase(0, -1, 0, false, false, false, false)==0) {
			return 3;
		}
		if(list.addCase(0, 0, -1, false, false, false, false)==0) {
			return 4;
		}
		
		
		if(list.addCase(1, 0, Case.UP, false, false, false, false)!=0) {
			return 5;
		}
		if(list.isEmpty()) {
			return 6;
		}
		if(list.addCase(2, 0, Case.LEFT, false, false, false, false)!=0) {
			return 7;
		}
		if(list.addCase(3, 0, Case.DOWN, false, false, false, false)!=0) {
			return 8;
		}
		if(list.addCase(4, 0, Case.RIGHT, false, false, false, false)!=0) {
			return 9;
		}
		
		
		if(list.addCase(1, 1, Case.UP, true, false, false, false)!=0) {
			return 10;
		}
		if(list.addCase(2, 1, Case.LEFT, true, false, false, false)!=0) {
			return 11;
		}
		if(list.addCase(3, 1, Case.DOWN, true, false, false, false)!=0) {
			return 12;
		}
		if(list.addCase(4, 1, Case.RIGHT, true, false, false, false)!=0) {
			return 13;
		}
		

		if(list.addCase(1, 2, Case.UP, false, true, false, false)!=0) {
			return 14;
		}
		if(list.addCase(2, 2, Case.LEFT, false, true, false, false)!=0) {
			return 15;
		}
		if(list.addCase(3, 2, Case.DOWN, false, true, false, false)!=0) {
			return 16;
		}
		if(list.addCase(4, 2, Case.RIGHT, false, true, false, false)!=0) {
			return 17;
		}
		
		
		if(list.addCase(1, 3, Case.UP, false, false, true, false)!=0) {
			return 18;
		}
		if(list.addCase(2, 3, Case.LEFT, false, false, true, false)!=0) {
			return 19;
		}
		if(list.addCase(3, 3, Case.DOWN, false, false, true, false)!=0) {
			return 20;
		}
		if(list.addCase(4, 3, Case.RIGHT, false, false, true, false)!=0) {
			return 21;
		}
		
		
		if(list.addCase(1, 4, Case.UP, false, false, false, true)!=0) {
			return 22;
		}
		if(list.addCase(2, 4, Case.LEFT, false, false, false, true)!=0) {
			return 23;
		}
		if(list.addCase(3, 4, Case.DOWN, false, false, false, true)!=0) {
			return 24;
		}
		if(list.addCase(4, 4, Case.RIGHT, false, false, false, true)!=0) {
			return 25;
		}
		
		
		caseTemp = list.getCase();
		if(caseTemp.getX()!=1 || caseTemp.getY()!=0 || caseTemp.getCompo()!=16) {
			return 26;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=2 || caseTemp.getY()!=0 || caseTemp.getCompo()!=16) {
			return 27;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=3 || caseTemp.getY()!=0 || caseTemp.getCompo()!=16) {
			return 28;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=4 || caseTemp.getY()!=0 || caseTemp.getCompo()!=16) {
			return 29;
		}
		
		
		caseTemp = list.getCase();
		if(caseTemp.getX()!=1 || caseTemp.getY()!=1 || caseTemp.getCompo()!=(16+1)) {
			return 30;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=2 || caseTemp.getY()!=1 || caseTemp.getCompo()!=(16+2)) {
			return 31;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=3 || caseTemp.getY()!=1 || caseTemp.getCompo()!=(16+4)) {
			return 32;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=4 || caseTemp.getY()!=1 || caseTemp.getCompo()!=(16+8)) {
			return 33;
		}
		
		
		caseTemp = list.getCase();
		if(caseTemp.getX()!=1 || caseTemp.getY()!=2 || caseTemp.getCompo()!=(16+2)) {
			return 34;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=2 || caseTemp.getY()!=2 || caseTemp.getCompo()!=(16+4)) {
			return 35;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=3 || caseTemp.getY()!=2 || caseTemp.getCompo()!=(16+8)) {
			return 36;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=4 || caseTemp.getY()!=2 || caseTemp.getCompo()!=(16+1)) {
			return 37;
		}
		
		
		caseTemp = list.getCase();
		if(caseTemp.getX()!=1 || caseTemp.getY()!=3 || caseTemp.getCompo()!=(16+4)) {
			return 38;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=2 || caseTemp.getY()!=3 || caseTemp.getCompo()!=(16+8)) {
			return 39;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=3 || caseTemp.getY()!=3 || caseTemp.getCompo()!=(16+1)) {
			return 40;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=4 || caseTemp.getY()!=3 || caseTemp.getCompo()!=(16+2)) {
			return 41;
		}
		
		
		caseTemp = list.getCase();
		if(caseTemp.getX()!=1 || caseTemp.getY()!=4 || caseTemp.getCompo()!=(16+8)) {
			return 42;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=2 || caseTemp.getY()!=4 || caseTemp.getCompo()!=(16+1)) {
			return 43;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=3 || caseTemp.getY()!=4 || caseTemp.getCompo()!=(16+2)) {
			return 44;
		}
		caseTemp = list.getCase();
		if(caseTemp.getX()!=4 || caseTemp.getY()!=4 || caseTemp.getCompo()!=(16+4)) {
			return 45;
		}
		
		if(!list.isEmpty()) {
			return 46;
		}
		
		if(list.addCase(1, 0, Case.UP, false, false, false, false)!=0) {
			return 47;
		}
		if(list.addCase(1, 0, Case.UP, false, false, false, false)!=0) {
			return 48;
		}
		
		if(list.isEmpty()) {
			return 49;
		}
		
		list.clear();
		
		if(!list.isEmpty()) {
			return 50;
		}
		
		return 0;
	}
	
	public int test102() {
		return 0;
	}
	
	public int test103() {
		return 0;
	}

	
	public int test104() {
		return 0;
	}

	
	public int test105() {
		return 0;
	}

	public int test106() {
		return 0;
	}



}
