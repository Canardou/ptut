package labyrinth;

/**
 * Liste chaine d'abord
 * Cette classe permet de gerer des listes de Cases et de comparer leur taille pour les differents algorithmes de pathfinding
 * Ensuite lors du rassemblement des codes une Arraylist s'est sauvagement attache... pourquoi ? :(
 *
 */

@SuppressWarnings({ "rawtypes" })
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
	
	
	
	/**
	 * Cree une nouvelle liste de case en definissant ses parametres
	 * @param current La case courante
	 * @param cout Le cout jusqu'a cette case
	 * @param previous La listeCase precedente, ou case parent
	 * @param dir La direction entre cette case et la precedente
	 */
	public ListeCase(Case current,int cout,ListeCase previous, int dir){
		this.current=current;
		this.cout=cout;
		this.previous=previous;
		this.dir=dir;
	}
	
	/*
	 * Methodes
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
	@Override
	public int compareTo(Object other) { 
		int nombre1 = ((ListeCase)other).cout; 
		int nombre2 = this.cout; 
		if (nombre1 > nombre2)  return -1; 
		else if(nombre1 == nombre2) return 0; 
		else return 1; 
	}
}
