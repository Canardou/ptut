package labyrinth;
import java.util.ArrayList;

public class Case {
	/*
	 * Constantes
	 */
	
	public static final int UP=0;
	public static final int LEFT=1;
	public static final int DOWN=2;
	public static final int RIGHT=3;
	
	/*
	 * Attributs
	 */
	
	private boolean marque;
	private boolean decouverte;
	private boolean [] murs;
	//Possibilitée d'utiliser un mask
	private int x;
	private int y;
	
	/*
	 * Constructeurs
	 */
	
	public Case(int x, int y){
		this.marque=false;
		this.decouverte=false;
		this.x=x;
		this.y=y;
		this.murs=new boolean[4];
	}
	
	public Case(){
		this(0,0);
	}
	
	/*
	 * Méthodes
	 */
	
	//Gestion des arcs accessibles
	private boolean changeState(int direction, boolean state){
		if(direction>=0 && direction<4){
			this.murs[direction]=state;
			return true;
		}
		else
			return false;
		
	}
	
	public boolean close(int direction){
		return changeState(direction,true);
	}
	
	public boolean bound(int direction){
		return changeState(direction,false);
	}
	
	public boolean isCrossable(int direction){
		if(direction>=0 && direction<4){
			return !this.murs[direction];
		}
		else
			return false;
	}
	
	public void setReveal(){
		this.decouverte=true;
	}
	
	public boolean isRevealed(){
		return this.decouverte;
	}
	
	public void isObjective(){
		this.marque=true;
	}
	
	public boolean getMarqueur(){
		return this.marque;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getX(int direction){
		if(direction==Case.LEFT)
			return this.x-1;
		else if(direction==Case.RIGHT)
			return this.x+1;
		else
			return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getY(int direction){
		if(direction==Case.UP)
			return this.y-1;
		else if(direction==Case.DOWN)
			return this.y+1;
		else
			return this.y;
	}
	
	public int getDir(int x, int y){
		if(this.x-x==1 && this.y-y==0)
			return Case.LEFT;
		else if(this.x-x==-1 && this.y-y==0)
			return Case.RIGHT;
		else if(this.y-y==1 && this.x-x==0)
			return Case.DOWN;
		else if(this.y-y==-1 && this.x-x==0)
			return Case.UP;
		else
			return -1;
	}
	
	public int getDir(Case autre){
		return this.getDir(autre.getX(),autre.getY());
	}
	
	public String toString(){
		return "Case ["+this.getX()+"]["+this.getY()+"] ^"+this.murs[0]+" <"+this.murs[1]+" v"+this.murs[2]+" >"+this.murs[3];
	}
}
