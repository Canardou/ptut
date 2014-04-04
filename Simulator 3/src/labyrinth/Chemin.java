package labyrinth;
import java.util.Stack;

public class Chemin {
	/*
	 * Attributs
	 */
	
	private Stack<Case> route;
	
	/*
	 * Constructeurs
	 */
	
	public Chemin(Chemin copie){
		this();
		if(copie!=null){
			this.route=copie.route;
		}
	}
	
	public Chemin(Case depart){
		this();
		this.push(depart);
	}
	
	public Chemin(){
		this.route=new Stack<Case>();
	}
	
	/*
	 * Méthodes
	 */
	
	public void push(Case nouvelle){
		this.route.add(0,nouvelle);
	}
	
	public void add(Case nouvelle){
		this.route.add(nouvelle);
	}
	
	public int direction(int k){
		if(k+1>this.route.size())
			return -1;
		else{
			return this.get(k).getDir(this.get(k+1));
		}
	}
	
	public int getX(int k){
		if(k<this.route.size())
			return this.route.get(k).getX();
		else
			return 0;
	}
	
	public int getY(int k){
		if(k<this.route.size())
			return this.route.get(k).getY();
		else
			return 0;
	}
	
	public Case get(int k){
		if(k<this.route.size())
			return this.route.get(k);
		else
			return null;
	}
	
	public Stack<Case> get(){
		return this.route;
	}
	
	public void removeTop(){
		this.route.remove(0);
	}
	
	public int size(){
		return this.route.size();
	}
	
	/**
	 * Compare deux chemins pour savoir s'ils se croisent. Dans le cas d'un croisement la fonction retourne l'index.
	 * @param autre
	 * @return
	 */
	
	public int collision(Chemin autre){
		int retour=this.size();
		if(autre!=null){
			int i=0;
			while(i<this.size() && retour>=this.size()){
				if(autre.route.contains(this.get(i)))
					retour=i-1;
				i++;
			}
		}
		if(retour<1)
			retour=1;
		return retour;
	}
	
	/**
	 * Retourne le chemin resultant de l'intersection de deux chemins
	 * @param autre
	 * @return
	**/
	
	public void beforeBlock(Chemin autre){
		this.route.setSize(this.collision(autre));
	}
	
	public void stopToVisibility(){
		int i=0;
		while(i<this.route.size()){
			if(!this.route.get(i).isRevealed()){
				this.route.setSize(i+1);
			}
			i++;
		}
	}
	
	/**
	 * Retourne vrai si le chemin ne dispose pas de possibilité de croisement
	 * @param autre
	 * @return
	 */
	public boolean blocked(){
		boolean retour=true;
		int i=0;
		while(i<this.route.size() && retour){
			int way=0;
			for(int k=0;k<this.route.size();k++){
				if(this.get(i).isCrossable(k))
					way++;
			}
			if(way>2)
				retour=false;
			i++;
		}
		return retour;
	}
	
	/**
	 * Compare deux chemins pour savoir s'ils se croisent et s'il existe une possibilité de croisement.
	 * @param autre
	 * @return
	 */
	public boolean blocked(Chemin autre){
		boolean retour;
		if(this.route.containsAll(autre.route)){
			retour=true;
			int i=0;
			while(i<autre.route.size() && retour){
				int way=0;
				for(int k=0;k<autre.route.size();k++){
					if(autre.get(i).isCrossable(k))
						way++;
				}
				if(way>2)
					retour=false;
				i++;
			}
		}
		else
			retour=false;
		return retour;
	}
	
	public String toString(){
		String temp="";
		for(Case item : this.route){
			temp+=item+"\n";
		}
		return temp;
	}
}
