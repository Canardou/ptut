package labyrinth;
import java.lang.Comparable;

@SuppressWarnings({ "rawtypes", "unused" })
public class ListeCase implements java.lang.Comparable {
	/*
	 * Attributs
	 */
	
	private ListeCase previous;
	private Case current;
	private int cout;
	private int dir;
	
	/*
	 * Constructeurs
	 */
	
	public ListeCase(Case current,int cout,ListeCase previous, int dir){
		this.current=current;
		this.cout=cout;
		this.previous=previous;
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
		int nombre1 = ((ListeCase)other).cout; 
		int nombre2 = this.cout; 
		if (nombre1 > nombre2)  return -1; 
		else if(nombre1 == nombre2) return 0; 
		else return 1; 
	} 
}
