package drawing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import labyrinth.*;

import javax.swing.Timer;
import javax.swing.JFrame;

public class main implements ActionListener {
	private Carte laby;
	private JFrame application;
	private DessinCarte test;
	private Timer timer= new Timer(20,this);
	
	public static void main(String [] args){
		new main();
	}
	
	public main(){
		laby = new Carte(10);
		application = new JFrame();
		test = new DessinCarte(laby);
		laby.randomMaze(); 
		test.addRobot((int)(Math.random()*10),(int)(Math.random()*10),0,0);
		Chemin yolo = new Chemin(laby.pathToMarque((int)this.test.getRobot(0).getX(), (int)this.test.getRobot(0).getY()));
		test.getRobot(0).walkPath(yolo);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.add(test); 
		application.pack();
		application.setVisible(true); 
		timer.setInitialDelay(20);
		timer.start();
	}
	
	public void update(){
		
		test.update();
		if(test.getRobot(0).getX()==laby.getMark().getX() && test.getRobot(0).getY()==laby.getMark().getY()){
			test.getRobot(0).changeType(3);
			test.removeMark();
			Chemin yolo = new Chemin(laby.pathToExit((int)this.test.getRobot(0).getX(), (int)this.test.getRobot(0).getY()));
			test.getRobot(0).walkPath(yolo);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    this.update();
	}
}
