package drawing;
import labyrinth.*;

import javax.swing.JFrame;

public class main {
	public static void main(String [] args){
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
		JFrame application = new JFrame();
		DessinCarte test = new DessinCarte(laby);
		        
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.add(test); 
		application.pack();
		application.setVisible(true);  
		
		test.update();
		while(true) {
			
		}
		
	}
}
