package env;

import robot.Param;
import robot.Robot;

public class Environment {	
		
	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	private		Robot 		bot ;
	 
	private 	boolean   	frontWallDetected; 
	private 	boolean   	rightWallDetected; 
	private 	boolean   	leftWallDetected;  
	private     boolean		backWallDetected;
	
	private 	int 		x;
	private 	int  		y;
	private 	int 		dir;
	
	/**********************************************/
	/**                                          **/
	/**               CONSTRUCTEUR               **/
	/**                                          **/
	/**********************************************/	
	public Environment (Robot botinit) {
		this.bot 		= botinit;
		this.backWallDetected = true;
		this.x 				= Param.INITX;
		this.y				= Param.INITY;
		this.dir			= Param.XP;
	}
	
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/

	public boolean getFrontWallDetected() {
		return this.frontWallDetected ;
	}
	public boolean getRightWallDetected() {
		return this.rightWallDetected ;
	}
	public boolean getLeftWallDetected() {
		return this.leftWallDetected ;
	}
	public boolean getBackWallDetected() {
		return this.backWallDetected ;
	}
	public void setBackWallDetected(boolean value) {
		this.backWallDetected = value ;
	}
	
	public void wallLeft() {
		this.frontWallDetected = this.leftWallDetected;
		this.backWallDetected  = this.rightWallDetected;
	}
	public void wallRight() {
		this.frontWallDetected = this.rightWallDetected;
		this.backWallDetected = this.leftWallDetected;
	}
	public void wallBack() {
		boolean tempo ;
		tempo = this.frontWallDetected; 
		this.frontWallDetected = this.backWallDetected;
		this.backWallDetected = tempo; 
	}
	
	/** Permet de mettre a jour la variable de direction lorsque le robot tourne **/
	public void dirRight() {
		if(this.dir==Param.XP) {
			this.dir = Param.YN ;
		}
		else if (this.dir==Param.XN) {
			this.dir = Param.YP ;
		}
		else if (this.dir==Param.YP) {
			this.dir = Param.XP ;
		}
		else if (this.dir==Param.YN) {
			this.dir = Param.XN ;
		}
	}	
	public void dirLeft() {
		if(this.dir==Param.XP) {
			this.dir = Param.YP ;
		}
		else if (this.dir==Param.XN) {
			this.dir = Param.YN ;
		}
		else if (this.dir==Param.YP) {
			this.dir = Param.XN ;
		}
		else if (this.dir==Param.YN) {
			this.dir = Param.XP ;
		}
	}	
	public void dirBack() {
		if(this.dir==Param.XP) {
			this.dir = Param.XN ;
		}
		else if (this.dir==Param.XN) {
			this.dir = Param.XP ;
		}
		else if (this.dir==Param.YP) {
			this.dir = Param.YN ;
		}
		else if (this.dir==Param.YN) {
			this.dir = Param.YP ;
		}
	}
	
	 /** Affichage des coordonées **/
	 public void afficher() {

		 if(this.x == Param.INITX && this.y == Param.INITY) {
			 System.out.print("D="+this.dir+" X="+this.x+" Y="+this.y+" (pt init)\n");
		 }
		 else {
			 System.out.print("D="+this.dir+" X="+this.x+" Y="+this.y+"\n");
		 }
		 
		 if(this.frontWallDetected) {
			 System.out.print("F ");
		 }
		 if(this.rightWallDetected) {
			 System.out.print("R ");
		 }
		 if(this.backWallDetected) {
			 System.out.print("B ");
		 }
		 if(this.leftWallDetected) {
			 System.out.print("L");
		 }
		 System.out.print("\n");
	 }
	
	 /** Mise à jour des coordonées **/
	 public void refreshCoord() {
		 if ( this.bot.getOrder() == Param.FORWARD ) {
			 if (this.dir==Param.XP) {
			 	 this.x++;
			 }
			 else if (this.dir==Param.XN) {
			 	 this.x--;
			 }
			 else if (this.dir==Param.YP) {
			 	 this.y++;
			 }
			 else if (this.dir==Param.YN) {
			 	 this.y--;
			 } 
		 }
	 }
	 
	 /** Mise à jour mur droit avant et gauche **/
	 public void check3WallsCompass() {
		this.checkFrontWall();
		this.bot.getLeftFrontSonar().refresh();
		for(int i=0 ; i < Param.TAB_NBDATA ; i++ ) {
			this.bot.getRightSonar().refresh();
			this.bot.getCompass().refresh();	
		}
		this.checkLeftWall();
		this.checkRightWall();
	 }	 
	 
	 /** Mise à jour mur droit et gauche **/
	 public void check2WallsCompass() {
		this.bot.getLeftFrontSonar().refresh();
		this.bot.getRightSonar().refresh();
		this.bot.getCompass().refresh();
		this.checkLeftWall();
		this.checkRightWall();
	 }

	 /** Mise a jour mur droit et gauche en remplissant entierement les filtres **/
	 public void check2WallsCompassFull() {
		 for( int i = 0 ; i<Param.TAB_NBDATA ; i++ ) {
			 this.check2WallsCompass();
		 }
	 }
	 
	 public void checkFrontWallFull() {
		for( int i = 0 ; i<Param.TAB_NBDATA ; i++ ) {
			this.bot.getLeftFrontSonar().refresh();
			this.checkFrontWall();
		}
	 }
	 
	 public void checkFrontWallCompass() {
		 this.bot.getLeftFrontSonar().refresh();
		 this.checkFrontWall();
		 this.bot.getCompass().refresh();
	 }
	 
	 /** Verification de la présence de murs **/
	 private void checkLeftWall(){
		 if(this.bot.getLeftFrontSonar().getMoyData()<=Param.LIMLEFTWALL){
			 this.leftWallDetected = true;
		 }
		 else {
			 this.leftWallDetected = false;
		 }	 
	 }
	 private void checkFrontWall(){
		 if(this.bot.getLeftFrontSonar().getMoyData()<=Param.LIMFRONTWALL){
			 this.frontWallDetected = true;
		 }
		 else {
			 this.frontWallDetected = false;
		 }	
	 }
	 private void checkRightWall(){
		 if(this.bot.getRightSonar().getMoyData()<=Param.LIMRIGHTWALL){
			 this.rightWallDetected = true;
		 }
		 else {
			 this.rightWallDetected = false;
		 }	
	 }
}