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
		
		board.grille();
		
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