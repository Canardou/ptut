package env;
import robot.*;

import java.util.Stack;

public class ListCase {	
		
	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	private Stack<Case> pile;
		
	/**********************************************/
	/**                                          **/
	/**               CONSTRUCTEUR               **/
	/**                                          **/
	/**********************************************/
	public ListCase () {
		this.pile = new Stack<Case> () ;
	}
	
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/
	public void add(double x, double y, int dir, boolean frontWall, boolean leftWall, boolean backWall, boolean rightWall)
	{
		if(dir==Param.XP) {
			Case temp = new Case(x,y,frontWall,leftWall,backWall,rightWall);
			this.pile.push(temp);
		}
		else if(dir==Param.YP){
			Case temp = new Case(x,y,rightWall,frontWall,leftWall,backWall);
			this.pile.push(temp);
		}
		else if(dir==Param.XN){
			Case temp = new Case(x,y,backWall,rightWall,frontWall,leftWall);
			this.pile.push(temp);
		}
		else if(dir==Param.XN){
			Case temp = new Case(x,y,leftWall,backWall,rightWall,frontWall);
			this.pile.push(temp);
		}		
	}
	
	/*public String toString() {
		String temp = "{\n";
		for (Case ligne : this.pile) {
			temp += ligne;
  		}	
		temp += "}\n";
		return (temp);	
	}*/
	
}