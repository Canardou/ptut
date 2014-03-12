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
		this.route=copie.route;
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
		this.route.push(nouvelle);
	}
	
	public void add(Case nouvelle){
		this.route.add(0, nouvelle);
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
	
	/**
	 * Compare deux chemins pour savoir s'ils se croisent. Dans le cas d'un croisement la fonction retourne vrai.
	 * @param autre
	 * @return
	 */
	
	public boolean collision(Chemin autre){
		boolean retour=false;
		for(int i=0;i<autre.route.size();i++){
			if(this.route.contains(autre.get(i)))
				retour=true;
		}
		return retour;
	}
	
	/**
	 * Retourne le chemin resultant de l'intersection de deux chemins
	 * @param autre
	 * @return
	 */
	
	public Chemin intersection(Chemin autre){
		Chemin retour=new Chemin();
		if(this.collision(autre)){
			for(int i=0;i<autre.route.size();i++){
				if(this.route.contains(autre.get(i)))
					retour.add(autre.get(i));
			}
		}
		else
			retour=null;
		return retour;
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
}
