package labyrinth;
import java.lang.Comparable;

@SuppressWarnings("rawtypes")
public class ListeChemin implements Comparable {
	private ListeChemin previous;
	private Chemin current;
	private int cout;
	
	public ListeChemin(Chemin current,int cout,ListeChemin previous){
		this.current=current;
		this.cout=cout;
		this.previous=previous;
	}
	
	public Chemin current(){
		return this.current;
	}
	
	public int getCout(){
		return this.cout;
	}
	
	public void setCout(int cout){
		this.cout=cout;
	}
	
	public ListeChemin previous(){
		return this.previous;
	}
	
	public void setPrevious(ListeChemin previous){
		this.previous=previous;
	}
	
	//From http://java.developpez.com
	public int compareTo(Object other) { 
		int nombre1 = ((ListeChemin)other).cout; 
		int nombre2 = this.cout; 
		if (nombre1 > nombre2)  return -1; 
		else if(nombre1 == nombre2) return 0; 
		else return 1; 
	} 
}
