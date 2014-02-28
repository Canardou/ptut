package labyrinth;
import java.util.ArrayList;

public class Case {
	/*
	 * Attributs
	 */
	
	private int marque;
	private boolean decouverte;
	private ArrayList<Case> voisines;
	//Possibilitée d'utiliser un mask
	private int x;
	private int y;
	
	/*
	 * Constructeurs
	 */
	
	public Case(int x, int y){
		this.marque=-1;
		this.decouverte=false;
		this.x=x;
		this.y=y;
		this.voisines=new ArrayList<Case>();
	}
	
	/*
	 * Méthodes
	 */
	
	public boolean bound(Case voisine){
		if(!this.voisines.contains(voisine)){
			return this.voisines.add(voisine);
		}
		else
			return false;
	}
	
	public boolean close(Case voisine){
		return this.voisines.remove(voisine);
	}
	
	public int boundSize(){
		return this.voisines.size();
	}
	
	public void visite(){
		this.decouverte=true;
	}
	
	public boolean isVisited(){
		return this.decouverte;
	}
	
	public void setMarqueur(boolean marque){
		if(marque=true)
			this.marque=1;
		else
			this.marque=0;
	}
	
	public int getMarqueur(){
		return this.marque;
	}
	
	public Case get(int index){
		if(index>=0 && index<this.voisines.size())
			return this.voisines.get(index);
		else
			return null;
	}
	
	public boolean linked(Case test){
		return voisines.contains(test);
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
}
