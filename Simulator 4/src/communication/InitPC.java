package communication;
import labyrinth.*;
import drawing.*;


public class InitPC {
	
	private ThreadComm tComH;
	private ThreadComm tComJ;
	private ThreadComm tComI;
	private ThreadComm tComF;
	private InfoEntitee IE;
	
	public InitPC(){
		this.IE = new InfoEntitee();
		
		
	
	}
	public void setThreadComm(VirtualRobots robot){
		if(robot.getID()==0){
		this.tComF = new ThreadComm(this.IE.robotF,new Case(robot.getX(),robot.getY()),robot.getDir());
		this.tComF.start();
		}
		if(robot.getID()==1){
		this.tComH = new ThreadComm(this.IE.robotH,new Case(robot.getX(),robot.getY()),robot.getDir());
		this.tComH.start(); //ne fonctionne pas avec mon ordi
		}
		if(robot.getID()==2){
		this.tComJ = new ThreadComm(this.IE.robotJ,new Case(robot.getX(),robot.getY()),robot.getDir());
		this.tComJ.start();
		}
	}
	public ThreadComm getThreadComm(int i){
			switch (i){
			case 0 :
			return this.tComF;
			case 1:
			return this.tComH;
			case 2:
			return this.tComJ;
			}
		return null;
	}
}
