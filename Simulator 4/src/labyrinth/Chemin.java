package labyrinth;
import java.util.Stack;

/**
 * Cette classe permet la gestion sommaire des chemins
 * Elle est equivalente a un extend de Stack<Case> (A modifier dans cette optique)
 * @author Olivier Hachette
 *
 */
public class Chemin {
	/*
	 * Attributs
	 */
	
	private Stack<Case> route;
	private int value;
	
	/*
	 * Constructeurs
	 */
	
	/**
	 * Créer la copie d'un autre chemin
	 * @param copie Chemin a copier
	 */
	public Chemin(Chemin copie){
		this();
		if(copie!=null){
			this.value=copie.value;
			for(Case copy : copie.route)
			this.route.add(copy);
		}
	}
	
	/**
	 * Cree un nouveau chemin disposant deja d'une case
	 * @param depart Case de depart
	 */
	public Chemin(Case depart){
		this();
		this.push(depart);
	}
	
	/**
	 * Cree une chemin vide
	 */
	public Chemin(){
		this.route=new Stack<Case>();
	}
	
	/*
	 * Mï¿½thodes
	 */
	
	/**
	 * Rajoute une case au debut du chemin
	 * @param nouvelle Case a rajouter
	 */
	public void push(Case nouvelle){
		this.route.add(0,nouvelle);
	}
	
	/**
	 * Rajoute une case a la fin du chemin
	 * @param nouvelle Case a rajouter
	 */
	public void add(Case nouvelle){
		this.route.add(nouvelle);
	}
	
	/**
	 * Rajoute un autre chemin a la fin du chemin
	 * @param autre Chemin a rajouter
	 */
	public void add(Chemin autre){
		this.route.addAll(autre.route);
	}
	
	/**
	 * Renvoit la direction entre la k-eme case et la suivante
	 * @param k
	 * @return La direction
	 */
	public int direction(int k){
		if(k+1>this.route.size())
			return -1;
		else{
			return this.get(k).getDir(this.get(k+1));
		}
	}
	
	/**
	 * Renvoit la coordonnee x de la k-eme case
	 * @param k
	 * @return Coordonnee en x
	 */
	public int getX(int k){
		if(k<this.route.size())
			return this.route.get(k).getX();
		else
			return 0;
	}
	
	/**
	 * Renvoit la coordonnee y de la k-eme case
	 * @param k
	 * @return Coordonnee en y
	 */
	public int getY(int k){
		if(k<this.route.size())
			return this.route.get(k).getY();
		else
			return 0;
	}
	
	/**
	 * Renvoit la k-eme case
	 * @param k
	 * @return Case à la position k
	 */
	public Case get(int k){
		if(k<this.route.size())
			return this.route.get(k);
		else
			return null;
	}
	
	/**
	 * Met a jour la valeur personnalise du chemin (Utilise pour debuguer principalement)
	 * @param value
	 */
	public void setValue(int value){
		this.value=value;
	}
	
	/**
	 * Renvoit la valeur personnalise
	 * @return La valeur personnalise
	 */
	public int getValue(){
		return this.value;
	}
	
	/**
	 * Fonction qui renvoit la Stack<Case> constituee de toutes les cases
	 * @return
	 */
	public Stack<Case> get(){
		return this.route;
	}
	
	/**
	 * Supprime la case en haut de la liste de cases
	 */
	public void removeTop(){
		this.route.remove(0);
	}
	
	/**
	 * Renvoit la taille de la liste de cases
	 * @return Taille de la liste
	 */
	public int size(){
		return this.route.size();
	}
	
	/**
	 * Compare deux chemins entre eux sur une certaine carte
	 * @param carte La carte ou faire la comparaison
	 * @param autre L'autre chemin avec lequel comparer
	 * @param ecart Mettre a vrai pour obliger les chemins a avoir deux cases d'ecart entre eux
	 * @return Retourne vrai si il y a collision
	 */
	public boolean isCollision(Carte carte, Chemin autre, boolean ecart){
		if(collision(carte,autre,ecart)<this.size()){
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Compare deux chemins entre eux sur une certaine carte
	 * @param carte La carte ou faire la comparaison
	 * @param autre L'autre chemin avec lequel comparer
	 * @param ecart Mettre a vrai pour obliger les chemins a avoir deux cases d'ecart entre eux
	 * @return L'index a partir duquel la collision a lieu
	 */
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
					if(temp!=null){
						if(autre.route.contains(temp) && temp.isCrossable(temp.getDir(this.get(i))) && this.get(i).isCrossable(this.get(i).getDir(temp))){
							if(ecart)
								retour=i-1;
							else
								retour=i;
						}
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
	 * Met a jour le chemin pour eviter la collision avec un autre chemin en reduisant sa taille jusqu'à l'index de collison
	 * @param carte La carte ou faire la comparaison
	 * @param autre L'autre chemin avec lequel comparer
	 * @param ecart Mettre a vrai pour obliger les chemins a avoir deux cases d'ecart entre eux
	 */
	public void beforeBlock(Carte carte, Chemin autre, boolean ecart){
		this.route.setSize(this.collision(carte, autre, ecart));
	}
	
	/**
	 * Met a jour le chemin et l'arrête à la première case non visitee
	 * @return Retourne vrai si le chemin a ete coupe
	 */
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
	
	/**
	 * Concatene deux chemins entre eux (Cette fonction n'a pas un comportement sur)
	 * @param autre L'autre chemin
	 * @return Retourne vrai si les deux chemin se suivent et ont pu être concatene
	 */
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
	
	/**
	 * Coupe un chemin a partir de la case specifiee
	 * @param dprt Case a laquelle couper le chemin
	 */
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
			for(int i=0;i<cut;i++){
				if(this.route.size()>1)
					this.route.remove(0);
			}
		}
	}
	
	@Override
	public String toString(){
		String temp="";
		for(Case item : this.route){
			temp+=item+"\n";
		}
		return temp;
	}
}
