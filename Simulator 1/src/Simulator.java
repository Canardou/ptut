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
		laby.close(0, 0, 1, 0);
		laby.close(0, 1, 1, 1);
		laby.close(1, 0, 1, 1);
		laby.close(0, 2, 0, 3);
		laby.close(2, 0, 3, 0);
		laby.close(2, 1, 3, 1);
		laby.close(2, 1, 2, 2);
		laby.close(2, 2, 1, 2);
		laby.close(2, 3, 3, 3);
		
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				//gauche
				if(!laby.isPath(i, j, i-1, j))
					board.Line((i+1)*25,(j+1)*25,0,25);
				//droite
				if(!laby.isPath(i, j, i+1, j))
					board.Line((i+2)*25,(j+1)*25,0,25);
				//haut
				if(!laby.isPath(i, j, i, j-1))
					board.Line((i+1)*25,(j+1)*25,25,0);
				//bas
				if(!laby.isPath(i, j, i, j+1))
					board.Line((i+1)*25,(j+2)*25,25,0);
			}
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