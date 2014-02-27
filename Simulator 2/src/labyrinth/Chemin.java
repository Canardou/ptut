package labyrinth;
import java.util.ArrayList;

public class Chemin {
	/*
	 * Attributs
	 */
	
	private ArrayList<Case> route;
	
	/*
	 * Constructeurs
	 */
	
	public Chemin(Chemin copie){
		this.route=copie.route;
	}
	
	public Chemin(Case depart){
		this();
		this.route.add(depart);
	}
	
	public Chemin(){
		this.route=new ArrayList<Case>();
	}
	
	/*
	 * Méthodes
	 */
	
	public void add(Case etape){
		this.route.add(etape);
	}
	
	public Case last(){
		return this.route.get(0);
	}
}
