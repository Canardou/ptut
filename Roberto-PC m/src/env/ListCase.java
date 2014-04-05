package env;

import robot.*;
import env.Case;

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
	public void addCase(int x, int y, int dir, boolean frontWall, boolean leftWall, boolean backWall, boolean rightWall)
	{
		Case caseTemp = new Case(x,y);
		if(dir==Param.XP) {
			if(frontWall) {
				caseTemp.bound(Case.RIGHT);
			}
			else if (leftWall) {
				caseTemp.bound(Case.UP);
			}
			else if (backWall) {
				caseTemp.bound(Case.LEFT);
			}
			else if (rightWall) {
				caseTemp.bound(Case.DOWN);
			}			
		}
		else if(dir==Param.YP){
			if(frontWall) {
				caseTemp.bound(Case.UP);
			}
			else if (leftWall) {
				caseTemp.bound(Case.LEFT);
			}
			else if (backWall) {
				caseTemp.bound(Case.DOWN);
			}
			else if (rightWall) {
				caseTemp.bound(Case.RIGHT);
			}	
		}
		else if(dir==Param.XN){
			if(frontWall) {
				caseTemp.bound(Case.LEFT);
			}
			else if (leftWall) {
				caseTemp.bound(Case.DOWN);
			}
			else if (backWall) {
				caseTemp.bound(Case.RIGHT);
			}
			else if (rightWall) {
				caseTemp.bound(Case.UP);
			}	
		}
		else if(dir==Param.YN){
			if(frontWall) {
				caseTemp.bound(Case.DOWN);
			}
			else if (leftWall) {
				caseTemp.bound(Case.RIGHT);
			}
			else if (backWall) {
				caseTemp.bound(Case.UP);
			}
			else if (rightWall) {
				caseTemp.bound(Case.LEFT);
			}	
		}	
		this.pile.insertElementAt(caseTemp, 0);
	}
	
	//utile dans BluetoothCommPC2 reception
	public void addCase(int x, int y, byte composition){
		Case caseTemp = new Case(x,y,composition);
		this.pile.insertElementAt(caseTemp, 0);
	}
	
	
	public Case getCase() {
		if(this.pile.isEmpty()) {
			return null;
		}
		else {
			return this.pile.pop();
		}		
	}
	
	public Stack<Case> getListCase(){
		return this.pile;
	}
	


	
}