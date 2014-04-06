/*
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
*/
import labyrinth.*;
import Dialogue.Dialogue;

/*
import javax.swing.Timer;
*/
import javax.swing.JFrame;

/*
import drawing.DessinCarte;
*/

public class main {
	
	private static Carte exploration;
	private JFrame application;
	private Superviseur test;
	
	public static void main(String [] args){
		exploration=new Carte(10);
		new main();
	}
	
	public main(){
		test = new Superviseur(exploration);
		application = new JFrame();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.add(test.dessinCarte());
		application.pack();
		application.setVisible(true);
		try{
			test.simulate();
		}
		catch (InterruptedException ie){
			Dialogue.Error("Erreur du thread de simulation");
		}
	}
		/*
		laby = new Carte(6);
		exploration = new Carte(6);
		application = new JFrame();
		test = new DessinCarte(exploration);
		laby.randomMaze(0.35);
		int i =0;
		test.getRobots()[0] = new VirtualRobots((int)(Math.random()*6),(int)(Math.random()*6),0,0,0);
		test.getRobots()[1] = new VirtualRobots((int)(Math.random()*6),(int)(Math.random()*6),0,1,1);
		test.getRobots()[2] = new VirtualRobots((int)(Math.random()*6),(int)(Math.random()*6),0,2,2);
		for(VirtualRobots robot : test.getRobots()){
			robot.setVisible(true);
			objectifs[i]=laby.getCase(robot.getX(),robot.getY());
			i++;
		}
		//test.toggleDoge();
		attente=100;
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.add(test);
		application.pack();
		application.setVisible(true); 
		timer.setInitialDelay(20);
		timer.start();
		
	}
	
	public void update(){
		attente--;
		if(attente<0)
			attente=1;
		int i=0;
		for(VirtualRobots robot : test.getRobots()){
			if(robot.isVisible()){
				//if(attente==0)
				//	robot.setWait(false);
				exploration.reveal(robot.getX(), robot.getY());
				exploration.update(robot.getX(), robot.getY(), laby.getCase(robot.getX(), robot.getY()).getCompo());
				if(robot.getX()==laby.getMark().getX() && robot.getY()==laby.getMark().getY()){
					exploration.mark(robot.getX(), robot.getY());
					if(robot.getType()==0){
						robot.changeType(robot.getType()+3);
						test.showMark(false);
					}
				}
				exploration.reveal(robot.getX(), robot.getY());

					//robot.setWait(true);
					if(objectifs[i]!=null){
						if(robot.getX()==objectifs[i].getX() && robot.getY()==objectifs[i].getY()){
								objectifs[i]=exploration.closestDiscover(robot.getX(), robot.getY(), 1);
						}
						if(objectifs[i]!=null){
							temp[i] = new Chemin(exploration.createPath(robot.getX(), robot.getY(), objectifs[i].getX(), objectifs[i].getY()));
							temp[i].stopToVisibility();
							for(int j=0;j<3;j++){
								if(j!=i)
									temp[i].beforeBlock(temp[j]);
							}
						}
					}
					else
						temp[i] = new Chemin(exploration.pathToExit(robot.getX(), robot.getY()));
					robot.walkPath(new Chemin(temp[i]));
				
			}
			i++;
		}
		test.update();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    this.update();
	}*/
}
