package simulator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import labyrinth.*;

public class Simulator extends JFrame {

	private SimRobot bot ;
	private SimBoard board;
	
	public Simulator() {
		this.board = new SimBoard(Param.LARGBOARD, Param.HAUTEURBOARD) ;
		this.bot = new SimRobot("robot2.png", (Param.INITX*Param.TAILLECASE) - Param.TAILLECASE/2, (Param.INITY*Param.TAILLECASE) - Param.TAILLECASE/2, this.board) ;
	}
	
    public static void pause(int duree) {
		try {
		    Thread.currentThread().sleep(duree) ;
		} 
		catch (InterruptedException e) {} 
    }
    
	public void go() {
		
		//board.grille();
		
		Carte laby = new Carte(4);
		laby.boundDown(3,3);
		laby.closeRight(0, 0);
		laby.closeRight(0, 1);
		laby.closeDown(1, 0);
		laby.closeDown(0, 2);
		laby.closeRight(2, 0);
		laby.closeRight(2, 1);
		laby.closeDown(2, 1);
		laby.closeLeft(2, 2);
		laby.closeRight(2, 3);
		
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				//gauche
				if(!laby.isCrossableLeft(i, j))
					board.Line((i+1)*25,(j+1)*25,0,25);
				//droite
				if(!laby.isCrossableRight(i, j))
					board.Line((i+2)*25,(j+1)*25,0,25);
				//haut
				if(!laby.isCrossableUp(i, j))
					board.Line((i+1)*25,(j+1)*25,25,0);
				//bas
				if(!laby.isCrossableDown(i, j))
					board.Line((i+1)*25,(j+2)*25,25,0);
			}
		}
		
		Chemin test = new Chemin();
		
		test = laby.pathToExit(0, 0);
		
		Case current = test.get(0);
		
		Case temp;
		int i=0;
		while((temp=test.get(i))!=null){
			i++;
			System.out.println(current);
			System.out.println(temp+"\n");
			switch(current.getDir(temp)){
			case Case.UP:
				board.Line((current.getX()+1)*25+13,(current.getY()+1)*25+12,0,25);
				break;
			case Case.DOWN:
				board.Line((current.getX()+1)*25+13,(current.getY()+1)*25+12,0,-25);
				break;
			case Case.LEFT:
				board.Line((current.getX()+1)*25+13,(current.getY()+1)*25+12,-25,0);
				break;
			case Case.RIGHT:
				board.Line((current.getX()+1)*25+13,(current.getY()+1)*25+12,25,0);
				break;
			}
			current=temp;
		}
		
		
		while(true) {
			this.pause(1000) ;
			this.bot.moveForward() ;
			this.bot.turnLeft() ;
		}
	}
	
	
    public static void main(String[] args) {
    	Simulator sim = new Simulator() ;	
    	sim.go () ;
    }
}