package drawing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import labyrinth.*;

import javax.swing.Timer;
import javax.swing.JFrame;

public class main implements ActionListener {
	private Carte laby;
	private Carte exploration;
	private JFrame application;
	private DessinCarte test;
	private Timer timer= new Timer(20,this);
	private int attente;
	private Chemin [] temp= new Chemin[3];
	private Case [] objectifs = new Case[3];
	
	public static void main(String [] args){
		new main();
	}
	
	public main(){
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
			attente=50;
		int i=0;
		for(VirtualRobots robot : test.getRobots()){
			if(robot.isVisible()){
				if(attente==0)
					robot.setWait(false);
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
				if(!robot.busy()){
					robot.setWait(true);
					if(robot.getX()==objectifs[i].getX() && robot.getY()==objectifs[i].getY()){
							objectifs[i]=laby.getCase((int)(Math.random()*6),(int)(Math.random()*6));
					}
					temp[i] = new Chemin(exploration.createPath(robot.getX(), robot.getY(), (int)(Math.random()*6), (int)(Math.random()*6)));
					temp[i].stopToVisibility();
					for(int j=0;j<3;j++){
						if(j!=i)
							temp[i].beforeBlock(temp[j]);
					}
					robot.walkPath(temp[i]);
				}
			}
			i++;
		}
		test.update();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    this.update();
	}
}
