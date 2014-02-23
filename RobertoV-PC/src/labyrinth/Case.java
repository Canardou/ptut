package labyrinth;
import java.util.ArrayList;

public class Case {
	/*
	 * Attributs
	 */
	
	private Object robot;
	private int marque;
	private boolean decouverte;
	private ArrayList<Case> voisines;
	private int x;
	private int y;
	
	/*
	 * Constructeurs
	 */
	
	Case(int x, int y){
		this.marque=-1;
		this.robot=null;
		this.decouverte=false;
		this.x=x;
		this.y=y;
		this.voisines=new ArrayList<Case>();
	}
	
	/*
	 * M�thodes
	 */
	
	public void bound(Case voisine){
		this.voisines.add(voisine);
	}
	
	public int boundSize(){
		return this.voisines.size();
	}
	
	public void visite(){
		this.decouverte=true;
	}
	
	public void marqueur(boolean marque){
		if(marque=true)
			this.marque=1;
		else
			this.marque=0;
	}
	
	public Case get(int index){
		return this.voisines.get(index);
	}
}
