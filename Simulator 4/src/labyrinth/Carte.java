package labyrinth;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.Collections;

/**
 * La classe carte gere une carte pouvant contenir une sortie et une marque
 * @author Olivier Hachette
 * 
 */

public class Carte {
	
	/*
	 * Attributs
	 */

	private int height;
	private int width;
	private Case[][] map;
	private Case marque;
	private Case exit;
	
	/**
	 * Random utilise pour randomMaze principalement ou des questions de determinisme dans les simulations
	 */
	public Random rand = new Random(100);
	
	/*
	 * Constructeurs
	 */
	
	/**
	 * Constructeur de class en specifiant la largeur et longueur
	 * @param width Largeur de la carte
	 * @param height Longueur de la carte
	 */
	public Carte(int width, int height){
		this.marque=null;
		this.exit=null;
		this.height = height;
		this.width = width;
		this.map = new Case[width][height];
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				this.map[i][j]=new Case(i,j);
			}
		}
		for(int i=0;i<this.width;i++){
			this.map[i][0].close(Case.UP);
			this.map[i][this.height-1].close(Case.DOWN);
		}
		for(int i=0;i<this.height;i++){
			this.map[0][i].close(Case.LEFT);
			this.map[this.width-1][i].close(Case.RIGHT);
		}
	}
	
	/**
	 * Constructeur de class en specifiant la longueur des cotes
	 * @param side Taille des cotes de la carte
	 */
	public Carte(int side){
		this(side,side);
	}
	
	/*
	 * Methodes
	 */
	
	/**
	 * Permet de verifier qu'un couple de coordonnes est bien compris sur la carte
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si les coordonnes sont valides
	 */
	private boolean checkCoord(int x, int y){
		return !(x<0 || x>=this.width || y<0 || y>=this.height);
	}
	
	/**
	 * Ouvre un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	public boolean boundUp(int x, int y){
		this.bound(x, y-1, Case.DOWN);
		return this.bound(x, y, Case.UP);
	}
	
	/**
	 * Ouvre un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	public boolean boundDown(int x, int y){
		this.bound(x, y+1, Case.UP);
		return this.bound(x, y, Case.DOWN);
	}
	
	/**
	 * Ouvre un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	public boolean boundLeft(int x, int y){
		this.bound(x-1, y, Case.RIGHT);
		return this.bound(x, y, Case.LEFT);
	}
	
	/**
	 * Ouvre un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	public boolean boundRight(int x, int y){
		this.bound(x+1, y, Case.LEFT);
		return this.bound(x, y, Case.RIGHT);
	}
	
	/**
	 * Ouvre un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @param dir Direction vers laquelle ouvrir
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	private boolean bound(int x, int y, int dir){
		if(checkCoord(x,y)){
			return this.map[x][y].bound(dir);
		}
		else
			return false;
	}
	
	/**
	 * Ferme un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	public boolean closeUp(int x, int y){
		this.close(x, y-1, Case.DOWN);
		return this.close(x, y, Case.UP);
	}
	
	/**
	 * Ferme un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	public boolean closeDown(int x, int y){
		this.close(x, y+1, Case.UP);
		return this.close(x, y, Case.DOWN);
	}
	
	/**
	 * Ferme un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	public boolean closeLeft(int x, int y){
		this.close(x-1, y, Case.RIGHT);
		return this.close(x, y, Case.LEFT);
	}
	
	/**
	 * Ferme un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	public boolean closeRight(int x, int y){
		this.close(x+1, y, Case.LEFT);
		return this.close(x, y, Case.RIGHT);
	}
	
	/**
	 * Ferme un cote d'une case dans une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @param dir Direction vers laquelle fermer
	 * @return Retourne vrai si l'action a ete effectuee
	 */
	private boolean close(int x, int y, int dir){
		if(checkCoord(x,y)){
			return this.map[x][y].close(dir);
		}
		else
			return false;
	}
	
	/**
	 * Declare une case comme revelee
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 */
	public void reveal(int x, int y){
		if(checkCoord(x,y))
			this.map[x][y].setReveal();
	}
	
	/**
	 * Verifie qu'une case est bien revelee
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si la case est revelee, faux sinon ou si les coordonnes sont fausses
	 */
	public boolean isRevealed(int x, int y){
		if(checkCoord(x,y))
			return this.map[x][y].isRevealed();
		else
			return false;
	}
	
	/**
	 * Definie l'emplacement du marqueur sur la carte
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Retourne vrai si les coordonnee etaient bonne et que la marque a ete posee
	 */
	public boolean mark(int x, int y){
		if(checkCoord(x,y)){
			this.marque=this.map[x][y];
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Renvoit la derniere marque specifiee
	 * @return La case du labyrinth ou se trouve la marque
	 */
	public Case getMark(){
		return this.marque;
	}
	
	/**
	 * La fonction met a jour la sortie et renvoie la sortie ainsi trouvee
	 * @return La sortie si elle a ete trouvee et existe, sinon null
	 */
	public Case setExit(){
		if(this.exit==null){
			for(int i=0;i<this.width;i++){
				if(this.map[i][0].isCrossable(Case.UP))
					this.exit= new Case(i,-1);
				else if(this.map[i][this.height-1].isCrossable(Case.DOWN))
					this.exit= new Case(i,this.height);
			}
			for(int i=0;i<this.height;i++){
				if(this.map[0][i].isCrossable(Case.LEFT))
					this.exit= new Case(-1,i);
				if(this.map[this.width-1][i].isCrossable(Case.RIGHT))
					this.exit= new Case(this.width,i);
			}
			if(this.exit!=null){
				for(int i=0;i<4;i++)
					this.exit.close(i);
			}
		}
		return this.exit;
	}
	
	/**
	 * Verifie que la sortie existe
	 * @return Retourne vrai si la sortie existe
	 */
	public boolean exit(){
		if(this.exit!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Cree un chemin entre la case de depart specifiee et la sortie si elle existe
	 * @param dx Coordonnee de depart x
	 * @param dy Coordonnee de depart y
	 * @return Le chemin resultant, peut être null si la sortie n'existe pas ou si le chemin n'est pas possible
	 */
	public Chemin pathToExit(int dx, int dy){
		if(checkCoord(dx,dy)){
			if(setExit()!=null){
				return createPath(this.map[dx][dy],this.exit,null);
			}
		}
		return null;
	}
	
	/**
	 * Verifie que la marque existe
	 * @return Retourne vrai si la marque existe
	 */
	public boolean mark(){
		if(this.marque!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Cree un chemin vers la marque depuis une position de depart
	 * @param dx Coordonnee de depart x
	 * @param dy Coordonnee de depart y
	 * @return Le chemin, peut etre null si il n'existe pas ou si la marque na pas deja ete trouvee
	 */
	public Chemin pathToMark(int dx, int dy){
		if(checkCoord(dx,dy)){
			if(this.mark()){
				return createPath(this.map[dx][dy],this.marque,null);
			}
		}
		return null;
	}
	
	/**
	 * Cree un chemin entre deux positions de depart et d'arrivee
	 * @param dx Coordonnee de depart x
	 * @param dy Coordonnee de depart y
	 * @param ax Coordonnee d'arrivee x
	 * @param ay Coordonnee d'arrivee y
	 * @return Le chemin, peut renvoyer un chemin null si les coordonnees ne sont pas bonnes ou le chemin impossible
	 */
	public Chemin createPath(int dx, int dy, int ax, int ay){
		if(checkCoord(dx,dy) && checkCoord(ax,ay))
			return createPath(this.map[dx][dy],this.map[ax][ay],null);
		else
			return null;
	}
	
	/**
	 * Cree un chemin entre deux positions de depart et d'arrivee
	 * @param dx Coordonnee de depart x
	 * @param dy Coordonnee de depart y
	 * @param ax Coordonnee d'arrivee x
	 * @param ay Coordonnee d'arrivee y
	 * @param blocked Liste de cases, stockees sous forme d'un chemin considerees comme impassables
	 * @return Le chemin, peut renvoyer null si les coordonnes ne sont pas bonnes ou le chemin impossible
	 */
	public Chemin createPath(int dx, int dy, int ax, int ay, Chemin blocked){
		if(checkCoord(dx,dy) && checkCoord(ax,ay)){
			return createPath(this.map[dx][dy],this.map[ax][ay],blocked);
		}
		else
			return null;
	}
	
	/**
	 * Cree un chemin entre deux cases de depart et d'arrivee
	 * @param depart Case de depart
	 * @param arrivee Case d'arrivee
	 * @param blocked Liste de cases, stockees sous forme d'un chemin considerees comme impassables
	 * @return Le chemin, peut renvoyer null si les coordonnes ne sont pas bonnes ou le chemin impossible
	 */
	@SuppressWarnings("unchecked")
	public Chemin createPath(Case depart, Case arrivee, Chemin blocked){
		Chemin path = new Chemin();
		ArrayList<ListeCase> recherche=new ArrayList<ListeCase>();
		ArrayList<Case> check=new ArrayList<Case>();
		if(blocked!=null){
				check.addAll(blocked.get());
		}
		recherche.add(new ListeCase(depart,0,null,-1));
		check.add(depart);
		//Tant que la case n'est pas celle d'arrivï¿½e on continu de chercher
		while(recherche.get(0).current()!=arrivee && !recherche.isEmpty()){	
			Case temp=recherche.get(0).current();
			if(temp.isRevealed() || (this.exit() && temp==this.exit)){
				for(int k=0;k<4;k++){
					if(temp.isCrossable(k) && checkCoord(temp.getX(k),temp.getY(k))){
						Case test=this.map[temp.getX(k)][temp.getY(k)];
						if(!check.contains(test)){
							int cout=1;
							if(temp.getDir(test)!=recherche.get(0).getDir() && recherche.get(0).getDir()!=-1)
								cout=2;
							recherche.add(new ListeCase(test,recherche.get(0).getCout()+cout,recherche.get(0),temp.getDir(test)));
							check.add(test);
						}
					}
					else if(this.exit()){
						//Cas particulier de la recherche de sortie
						if (this.exit.getX()==temp.getX(k) && this.exit.getY()==temp.getY(k)){
							int cout=1;
							if(temp.getDir(this.exit)!=recherche.get(0).getDir() && recherche.get(0).getDir()!=-1)
								cout=2;
							recherche.add(new ListeCase(this.exit,recherche.get(0).getCout()+cout,recherche.get(0),temp.getDir(this.exit)));
							check.add(this.exit);
						}
					}
				}
			}
			if(recherche.size()>=1){
				recherche.remove(0);
				//Tri de cases ï¿½ parcourir restante par distance depuis le point de dï¿½part
				if(recherche.size()>0)
					Collections.sort(recherche);
				else
					break;		
			}
		}
		if(!recherche.isEmpty()){
			//On retourne le chemin trouvï¿½ par parcours inversï¿½ des cases
			ListeCase temp = recherche.get(0);
			int yolo = temp.getCout();
			while(temp.current()!=depart){
				path.push(temp.current());
				temp=temp.previous();
			}
			path.push(temp.current());
			path.setValue(yolo);
			return path;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Cherche la n-eme case la plus proche non visitee
	 * @param dx Coordonnee de depart x
	 * @param dy Coordonnee de depart y
	 * @param number Le nombre de case n a visiter avant de s'arreter, 1 pour visiter la premiere
	 * @return Le chemin, peut renvoyer null si les coordonnes ne sont pas bonnes ou le chemin impossible
	 */
	public Chemin closestDiscover(int dx, int dy, int number){
		if(checkCoord(dx,dy) && number>=0)
			return closestDiscover(this.map[dx][dy], number, null, null, false);
		else
			return null;
	}
	
	/**
	 * Cherche la n-eme case la plus proche non visitee et en dehors des cases a eviter
	 * @param dx Coordonnee de depart x
	 * @param dy Coordonnee de depart y
	 * @param number Le nombre de case n a visiter avant de s'arreter, 1 pour visiter la premiere
	 * @param avoid Liste de cases, stockees sous forme d'un chemin considerees comme à eviter
	 * @param blocked Liste de cases, stockees sous forme d'un chemin considerees comme impassables
	 * @param exit Autoriser les robots à sortir du labyrinth pour valider les conditions sur vrai
	 * @return Le chemin, peut renvoyer null si les coordonnes ne sont pas bonnes ou le chemin impossible
	 */
	public Chemin closestDiscover(int dx, int dy, int number, Chemin avoid, Chemin blocked, boolean exit){
		if(checkCoord(dx,dy) && number>=0)
			return closestDiscover(this.map[dx][dy], number, avoid, blocked, exit);
		else
			return null;
	}
	
	/**
	 * Cherche la n-eme case la plus proche non visitee et en dehors des cases a eviter
	 * @param depart Case de depart
	 * @param number Le nombre de case n a visiter avant de s'arreter, 1 pour visiter la premiere
	 * @param avoid Liste de cases, stockees sous forme d'un chemin considerees comme à eviter
	 * @param blocked Liste de cases, stockees sous forme d'un chemin considerees comme impassables
	 * @param canExit Autoriser les robots à sortir du labyrinth pour valider les conditions sur vrai
	 * @return Le chemin, peut renvoyer null si les coordonnes ne sont pas bonnes ou le chemin impossible
	 */
	@SuppressWarnings("unchecked")
	public Chemin closestDiscover(Case depart, int number, Chemin avoid, Chemin blocked, boolean canExit){
		Chemin path = new Chemin();
		ArrayList<ListeCase> recherche=new ArrayList<ListeCase>();
		ArrayList<Case> check=new ArrayList<Case>();
		if(blocked!=null){
				check.addAll(blocked.get());
				if(this.exit()){
					check.remove(this.exit());
				}
		}
		if(avoid!=null)
			avoid.get().remove(this.exit);
		recherche.add(new ListeCase(depart,0,null,-1));
		check.add(depart);
		while(number>0 && recherche.size()>0){
			Case temp=recherche.get(0).current();
			if(avoid!=null){
				if(!avoid.get().contains(temp)){
					Boolean yolo=true;
					for(int k=0;k<4;k++){
						if(avoid.get().contains(this.getCase(temp.getX(k),temp.getY(k))) && this.isCrossable(temp.getX(), temp.getY(), k))
							yolo=false;
					}
					if(yolo)
						number--;
				}
			}
			else
				if(!temp.isRevealed())
					number--;
			if(temp.isRevealed() && number>0){
				for(int k=0;k<4;k++){
					if(temp.isCrossable(k) && checkCoord(temp.getX(k),temp.getY(k))){
						Case test=this.map[temp.getX(k)][temp.getY(k)];
						if(!check.contains(test)){
							int cout=1;
							if(temp.getDir(test)!=recherche.get(0).getDir() && recherche.get(0).getDir()!=-1)
								cout=2;
							recherche.add(new ListeCase(test,recherche.get(0).getCout()+cout,recherche.get(0),temp.getDir(test)));
							check.add(test);
						}
					}
					else if(this.exit() && canExit){
						//Cas particulier de la recherche de sortie
						if (this.exit.getX()==temp.getX(k) && this.exit.getY()==temp.getY(k)){
							int cout=1;
							if(temp.getDir(this.exit)!=recherche.get(0).getDir() && recherche.get(0).getDir()!=-1)
								cout=2;
							recherche.add(new ListeCase(this.exit,recherche.get(0).getCout()+cout,recherche.get(0),temp.getDir(this.exit)));
							check.add(this.exit);
						}
					}
				}
			}
			if(recherche.size()>0 && number>0){
				recherche.remove(0);
				if(recherche.size()>0){
					Collections.shuffle(recherche,new Random(this.rand.nextInt()));
					Collections.sort(recherche);
				}
				else
					break;		
			}
		}
		if(!recherche.isEmpty()){
			//On retourne le chemin trouvï¿½ par parcours inversï¿½ des cases
			ListeCase temp = recherche.get(0);
			int yolo = temp.getCout();
			while(temp.current()!=depart){
				path.push(temp.current());
				temp=temp.previous();
			}
			path.push(temp.current());
			path.setValue(yolo);
			return path;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Renvoit la case aux coordonnees specifiees
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return La case ou null si les coordonnees ne sont pas valides
	 */
	public Case getCase(int x, int y){
		if(checkCoord(x,y))
			return this.map[x][y];
		else
			return null;
	}
	
	/**
	 * Verifie qu'une case est ouverte vers une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Vrai si la case est ouverte vers cette direction
	 */
	public boolean isCrossableUp(int x, int y){
		return isCrossable(x,y,Case.UP);
	}
	
	/**
	 * Verifie qu'une case est ouverte vers une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Vrai si la case est ouverte vers cette direction
	 */
	public boolean isCrossableDown(int x, int y){
		return isCrossable(x,y,Case.DOWN);
	}
	
	/**
	 * Verifie qu'une case est ouverte vers une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Vrai si la case est ouverte vers cette direction
	 */
	public boolean isCrossableLeft(int x, int y){
		return isCrossable(x,y,Case.LEFT);
	}
	
	/**
	 * Verifie qu'une case est ouverte vers une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @return Vrai si la case est ouverte vers cette direction
	 */
	public boolean isCrossableRight(int x, int y){
		return isCrossable(x,y,Case.RIGHT);
	}
	
	/**
	 * Verifie qu'une case est ouverte vers une certaine direction
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @param direction - Direction a tester
	 * @return Vrai si la case est ouverte vers cette direction
	 */
	private boolean isCrossable(int x, int y, int direction){
		if(checkCoord(x,y))
			return this.map[x][y].isCrossable(direction);
		else
			return false;
	}
	
	/**
	 * Recupere la largeur de la carte
	 * @return La largeur
	 */
	public int getWidth(){
		return this.width;
	}
	
	/**
	 * Recupere la longueur de la carte
	 * @return La longueur
	 */
	public int getHeight(){
		return this.height;
	}
	
	/**
	 * Genere aleatoirement un labyrinth entierement visite
	 * @param wall Probabilites d'avoir plusieurs chemins possibles en ouvrant plusieurs fois les murs sur plusieurs cases.
	 * wall doit être compris entre 0 inclus et 1 exclus. Plus wall est proche de 1 plus le labyrinth a de chance d'etre vide.
	 */
	public void randomMaze(double wall){
		Stack<Case> stack = new Stack<Case>();
		ArrayList<Case> recherche=new ArrayList<Case>();
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				this.map[i][j].close(Case.UP);
				this.map[i][j].close(Case.DOWN);
				this.map[i][j].close(Case.LEFT);
				this.map[i][j].close(Case.RIGHT);
				recherche.add(this.map[i][j]);
				this.map[i][j].setReveal();
			}
		}
		Case temp = recherche.get(rand.nextInt(recherche.size()));
		//Tant qu'il reste des cases potentiellements non reliï¿½s aux autres, on continu la recherche
		while(recherche.size()>0){
			ArrayList<Case> random = new ArrayList<Case>();
			if(rand.nextDouble()>wall)
				recherche.remove(temp);
			for(int k=0;k<4;k++){
				if(checkCoord(temp.getX(k),temp.getY(k))){
					Case test=this.map[temp.getX(k)][temp.getY(k)];
					if(recherche.contains(test)){
						random.add(test);
					}
				}
			}
			if(!random.isEmpty()){
				Case select = random.get(rand.nextInt(random.size()));
				select.bound(select.getDir(temp));
				temp.bound(temp.getDir(select));
				stack.push(temp);
				temp=select;
			}
			else if(!stack.isEmpty()){
				temp=stack.pop();
			}
			else if(recherche.size()>0)
				temp=recherche.get(rand.nextInt(recherche.size()));
		}
		//Ajout de la sortie
		if(rand.nextInt(2)==0){
			if(rand.nextInt(2)==0){
				this.boundLeft(0,rand.nextInt(this.height));
			}
			else
				this.boundRight(this.width-1,rand.nextInt(this.height));
		}
		else{
			if(rand.nextInt(2)==0){
				this.boundUp(rand.nextInt(this.width),0);
			}
			else
				this.boundDown(rand.nextInt(this.width),this.height-1);
		}
		//Ajout de la marque
		this.mark(rand.nextInt(this.width), rand.nextInt(this.height));
		this.getMark().setMark();
	}
	
	/**
	 * Exporte la carte sous forme d'un tableau de bytes
	 * @return Le tableau de bytes
	 */
	public byte[] export(){
		byte [] tableau = new byte[this.width*this.height+2];
		if(this.getMark()!=null){
			tableau[0]=(byte)(this.getMark().getX()+1);
			tableau[1]=(byte)(this.getMark().getY());
		}
		else{
			tableau[0]=0;
			tableau[1]=0;
		}
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				tableau[i+j*this.width+2]=this.getCase(i, j).getCompo();
			}
		}
		return tableau;
	}
	
	/**
	 * Importe une carte depuis un tableau de bytes
	 * @param importation - Le tableau de bytes issu de l'exportation
	 */
	public void update(byte[] importation){
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				this.map[i][j].update(importation[i+j*this.width+2]);
			}
		}
		if(importation[0]!=0){
			this.marque=this.map[importation[0]-1][(importation[1])];
		}
		this.setExit();
	}
	
	/**
	 * Met a jour une case de la carte a partir de sa composition
	 * @param x Coordonnee x sur la carte
	 * @param y Coordonnee y sur la carte
	 * @param importation - Composition d'une case sous forme d'un bytes
	 */
	public void update(int x, int y, byte importation){
		if(checkCoord(x,y)){
			this.map[x][y].update(importation);
			for(int i=0;i<4;i++){
				if(!this.map[x][y].isCrossable(i))
					this.map[x][y].close(i);
				else
					this.map[x][y].bound(i);
			}
			if(this.map[x][y].isMark())
				this.mark(x,y);
		}
	}
	
	/**
	 * Permet de reinitialiser une carte a son etat initial
	 */
	public void reset(){
		this.marque=null;
		this.exit=null;
		this.map = new Case[width][height];
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				this.map[i][j]=new Case(i,j);
			}
		}
		for(int i=0;i<this.width;i++){
			this.map[i][0].close(Case.UP);
			this.map[i][this.height-1].close(Case.DOWN);
		}
		for(int i=0;i<this.height;i++){
			this.map[0][i].close(Case.LEFT);
			this.map[this.width-1][i].close(Case.RIGHT);
		}
	}

	/**
	 * Regarde si la carte entiere a deja ete visitee
	 * @return Retourne vrai si la carte est entierement visitee
	 */
	public boolean isWholeRevealed() {
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				if(this.map[i][j].isRevealed()!=true)
					return false;
			}
		}
		return true;
	}
}
