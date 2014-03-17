package labyrinth;
import java.lang.Comparable;

public class ListeCase implements java.lang.Comparable {
	/*
	 * Attributs
	 */
	
	private ListeCase previous;
	private Case current;
	private int cout;
	private int distance;
	private int dir;
	
	/*
	 * Constructeurs
	 */
	
	public ListeCase(Case current,int cout,ListeCase previous, int dir, int distance){
		this.current=current;
		this.cout=cout;
		this.previous=previous;
		this.distance=distance;
		this.dir=dir;
	}
	
	/*
	 * Méthodes
	 */
	
	public Case current(){
		return this.current;
	}
	
	public int getCout(){
		return this.cout;
	}
	
	public int getDir(){
		return this.dir;
	}
	
	public void setCout(int cout){
		this.cout=cout;
	}
	
	public ListeCase previous(){
		return this.previous;
	}
	
	//From http://java.developpez.com
	public int compareTo(Object other) { 
		int nombre1 = ((ListeCase)other).cout+((ListeCase)other).distance; 
		int nombre2 = this.cout+this.distance; 
		if (nombre1 > nombre2)  return -1; 
		else if(nombre1 == nombre2) return 0; 
		else return 1; 
	} 
}
