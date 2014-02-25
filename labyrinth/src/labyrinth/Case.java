package labyrinth;
import java.util.ArrayList;

class Case {
	/*
	 * Attributs
	 */
	
	private boolean occupe;
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
		this.occupe=false;
		this.decouverte=false;
		this.x=x;
		this.y=y;
		this.voisines=new ArrayList<Case>();
	}
	
	/*
	 * Méthodes
	 */
	
	public void bound(Case voisine){
		this.voisines.add(voisine);
	}
	
	public void close(Case voisine){
		this.voisines.remove(voisine);
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
	
	public int [] coord(){
		int [] retour;
		retour = new int[2];
		retour[1]=this.x;
		retour[2]=this.y;
		return retour;
	}
}
