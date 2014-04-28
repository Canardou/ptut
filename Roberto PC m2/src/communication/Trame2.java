package communication;

import java.util.ArrayList;


//import threads.ThreadRobot;
import env.Case;
//import env.Environment;
import env.ListCase;





public class Trame2 {
	
	//attributs
	
	private byte tailleTrame; 
	private byte ID;
	private byte direction;
	private byte ordre;
	private int typeTrame;
	private int orientation;
	private byte message;
	private int typeMessage;
	private int isBusy;

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
	
public Trame2(byte ID, ListCase listCase ){

		
		
		int i=2;
		//this.tailleTrame=tailleTrame;
		
		this.ID=ID;
		this.pile=listCase.getArrayList();
		
		
		this.typeTrame=1;
		
		
		
		this.contenuT= new byte[(byte)(this.pile.size()*3+3)];  
		
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
	
	//trame avec position initiale du robot (x/y/orientation(0.1.2.3))
	public Trame2(byte ID, Case firstCase, int orientation){
		this.ID=ID;
		this.orientation=orientation;
		this.typeTrame=8;
		
		this.contenuT= new byte[6];
		
		this.contenuT[0]=6;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=(byte)firstCase.getX();
		this.contenuT[3]=(byte)firstCase.getY();
		this.contenuT[4]=(byte)this.orientation;
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
	public int getBusy(){
		return this.isBusy;
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
