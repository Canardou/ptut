package labyrinth;
import java.util.Stack;

public class Chemin {
	/*
	 * Attributs
	 */
	
	private Stack<Case> route;
	private int value;
	
	/*
	 * Constructeurs
	 */
	
	public Chemin(Chemin copie){
		this();
		if(copie!=null){
			this.value=copie.value;
			for(Case copy : copie.route)
			this.route.add(copy);
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
	
	public void add(Chemin autre){
		this.route.addAll(autre.route);
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
	
	public void setValue(int value){
		this.value=value;
	}
	
	public int getValue(){
		return this.value;
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
	
	public boolean isCollision(Carte carte, Chemin autre, boolean ecart){
		if(collision(carte,autre,ecart)<this.size()){
			return true;
		}
		else
			return false;
	}
	
	public int collision(Carte carte, Chemin autre, boolean ecart){
		int retour=this.size();
		if(autre!=null){
			int i=0;
			while(i<this.size() && retour>=this.size()){
				if(autre.route.contains(this.get(i))){
					if(ecart)
						retour=i-1;
					else
						retour=i;
				}
				for(int k=0;k<4;k++){
					Case temp=carte.getCase(this.get(i).getX(k),this.get(i).getY(k));
					if(autre.route.contains(temp) && temp.isCrossable(temp.getDir(this.get(i))) && this.get(i).isCrossable(this.get(i).getDir(temp))){
						if(ecart)
							retour=i;
					}
				}
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
	
	public void beforeBlock(Carte carte, Chemin autre, boolean ecart){
		this.route.setSize(this.collision(carte, autre, ecart));
	}
	
	public boolean stopToVisibility(){
		int i=0;
		while(i<this.route.size()){
			if(!this.route.get(i).isRevealed()){
				this.route.setSize(i+1);
				return true;
			}
			i++;
		}
		return false;
	}
	
	public boolean concatenation(Chemin autre){
		if (autre!=null && this.route!=null){
			if(autre.get(0)==this.get(this.size()-1)){
				autre.removeTop();
				this.route.addAll(autre.get());
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	public void cut(Case dprt){
		int cut=0;
		int sub=0;
		for(Case aux : this.route){
			if(aux==dprt){
				cut=sub;
			}
			sub++;
		}
		if(cut!=0){
			for(int i=0;i<cut;i++)
				this.route.remove(0);
		}
	}
	
	/**
	 * Retourne vrai si le chemin ne dispose pas de possibilité de croisement
	 * @param autre
	 * @return
	 
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
	}*/
	
	public String toString(){
		String temp="";
		for(Case item : this.route){
			temp+=item+"\n";
		}
		return temp;
	}
}
