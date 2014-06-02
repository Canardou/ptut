package communication;

import java.util.ArrayList;



//import threads.ThreadRobot;
import labyrinth.Case;
//import env.Environment;




public class Trame2 {
	
	//attributs
	
	private byte tailleTrame; 
	private byte ID;
	private int direction;
	private byte ordre;
	private int typeTrame;
	private byte message;
	private int typeMessage;
	private int isBusy;
	private int X;
	private int Y;

	private ArrayList<Case> pile;

	
	private byte[] contenuT;

	
	
	//constructeur
	
	
	//trame permettant d'envoyer des ordres au robot
	
	public Trame2(byte ID ,byte ordre){
			
			
			this.ID=ID;
			this.ordre= ordre;
			this.typeTrame=5;
			
			this.contenuT= new byte[4];
			
			this.contenuT[0]=4;
			this.contenuT[1]=this.ID;
			this.contenuT[2]=this.ordre;
			this.contenuT[3]=(byte)this.typeTrame;
		}
	
	//trame qui contient une liste de cases � envoyer 
	
	public Trame2(byte ID, ListeCaseCommunication listCase ){

		
		
		int i=2;
		//this.tailleTrame=tailleTrame;
		
		this.ID=ID;
		this.pile=listCase.getArrayList();
		
		this.typeTrame=1;
		
		
		
		this.contenuT= new byte[(byte)(this.pile.size()*3+3)];  //on récupère la taille du tab, chaque case contient 3 infos + taille trame, type et ID
	
		this.contenuT[0]=(byte)(this.pile.size()*3+3);
		this.contenuT[1]=this.ID;

	
		
		for (Case Case1 : this.pile){   
			
			this.contenuT[i]=(byte)Case1.getX();
			this.contenuT[i+1]=(byte)Case1.getY();
			this.contenuT[i+2]=Case1.getCompo();
			i=i+3;
		}
		
		this.contenuT[i]=(byte)this.typeTrame;
		
		
	}
	
	
	//trame indiquant qui contient (ID, (byte) demande calibration/calibration termin�e/d�marrage mission/mission termin�e)
	//l'argument typeMessage sera toujours � 0 ->il sert � differencier les 2 constructeurs
	
	public Trame2(byte ID, byte message, int typeMessage){
		
		this.ID=ID;
		this.message=message;
		this.typeMessage=typeMessage;
		this.typeTrame=7;
		
		this.contenuT= new byte[5];
		
		this.contenuT[0]=5;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=this.message;
		this.contenuT[3]=(byte)this.typeMessage;
		this.contenuT[4]=(byte)this.typeTrame;
	
	}
	
	//trame avec position initiale du robot (x/y/orientation)
	public Trame2(byte ID, Case firstCase, int direction){
		this.ID=ID;
		this.direction=direction;
		this.typeTrame=2;
		this.X=firstCase.getX();
		this.Y=firstCase.getY();
		
		
		this.contenuT= new byte[6];
		
		this.contenuT[0]=6;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=(byte)this.X;
		this.contenuT[3]=(byte)this.Y;
		this.contenuT[4]=(byte)this.direction;
		this.contenuT[5]=(byte)this.typeTrame;
		
	}
	
	// trame robot busy
		public Trame2(byte ID, int isBusy){
			this.ID=ID;
			this.isBusy=isBusy;
			this.typeTrame=9;
			
			this.contenuT= new byte[3];
			
			this.contenuT[0]=3;
			this.contenuT[1]=this.ID;
			this.contenuT[2]=(byte)this.isBusy;
			
		}
		

	
	//methodes
	

	
	public byte[] tableauTrame(){
		return this.contenuT;
	}
	
	public byte getTailleTrame(){
		return this.tailleTrame;
	}
	
	public byte getID(){
		return this.ID;
	}
	

	public byte getOrdre(){
		return this.ordre;
	}		
	
	public int getDirection(){
		return this.direction;
	}
	public int getTypeTrame(){
		return this.typeTrame;
	}
	public int getPosX(){
		return this.X;
	}
	public int getPosY(){
		return this.Y;
	}
	public int getBusy(){
		return this.isBusy;
	}
	
	public Case toCase(){
		if(this.typeTrame == 1){
			Case nouvelleCase = new Case((int)this.contenuT[this.contenuT.length-4],(int)this.contenuT[this.contenuT.length-3]);
			nouvelleCase.update(this.contenuT[this.contenuT.length-2]);
			return  nouvelleCase;
		}
		else{
			return null;
		}
	}
	
	public void printTrame() throws InterruptedException{
		//Thread thread=new Thread();
		int i;
		String message = "";
		for(i=0;i<this.contenuT[0];i++){
			message = message + this.contenuT[i] +" ; ";
			//lejos.nxt.LCD.scroll();
		}
		System.out.println(message);
		Thread.sleep(4000);
	}
}
