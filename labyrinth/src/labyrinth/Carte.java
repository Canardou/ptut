package labyrinth;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author canard
 * 
 */

public class Carte {
	/*
	 * Attributs
	 */
	
	private int heigth;
	private int width;
	private Case[][] map;
	private Case exit;
	
	/*
	 * Constructeurs
	 */
	
	Carte(int width, int heigth){
		this.heigth = heigth;
		this.width = width;
		this.map = new Case[width][heigth];
		for(int i=0;i<this.heigth;i++){
			for(int j=0;j<this.width;j++){
				this.map[i][j]=new Case(i,j);
			}
		}
	}
	
	Carte(int side){
		this(side,side);
	}
	
	/*
	 * M�thodes
	 */

	/**
	 * 
	 * @method closeUp
	 * @method closeDown
	 * @method closeLeft
	 * @method closeRight
	 * @desc Respectivement d�clarer impraticable un chemin depuis la case
	 * 
	 */
	
	public void boundUp(int x, int y){
		if(y>0){
			this.map[x][y].bound(this.map[x][y-1]);
		}
		else{
			this.boundExit(x,y);
		}
			
	}
	
	public void boundDown(int x, int y){
		if(y<this.heigth-1){
			this.map[x][y].bound(this.map[x][y+1]);
		}
		else{
			this.boundExit(x,y);
		}
	}
	
	public void boundLeft(int x, int y){
		if(x>0){
			this.map[x][y].bound(this.map[x-1][y]);
		}
		else{
			this.boundExit(x,y);
		}
	}

	public void boundRight(int x, int y){
		if(x<this.width-1){
			this.map[x][y].bound(this.map[x+1][y]);
		}
		else{
			this.boundExit(x,y);
		}
	}
	
	public void boundExit(int x, int y){
		this.exit=new Case(x,y);
		this.map[x][y].bound(this.exit);
	}
	
	/**
	 * 
	 * @method close
	 * @param {int} position de d�part x
	 * @param {int} position de d�part y
	 * @param {int} position d'arrivee x
	 * @param {int} position d'arrivee y
	 * @desc d�clare un chemin impraticable entre deux cases adjacentes (ou bord)
	 * 
	 */
	
	public void bound(int dx, int dy, int ax, int ay){
		if(dx-ax==-1 && dy-ay==0)
			this.boundRight(dx,dy);
		if(dx-ax==1 && dy-ay==0)
			this.boundLeft(dx,dy);
		if(dx-ax==0 && dy-ay==-1)
			this.boundDown(dx,dy);
		if(dx-ax==0 && dy-ay==1)
			this.boundUp(dx,dy);
	}
	
	/**
	 * 
	 * @method visite
	 * @param {int} x
	 * @param {int} y
	 * @desc d�clare une case d�j� visit�e
	 * 
	 */	
	public void visite(int x, int y){
		this.map[x][y].visite();
	}

	/**
	 * 
	 * @method marqueur
	 * @param {int} x
	 * @param {int} y
	 * @param {bool}
	 * @desc mettre true si le marqueur est sur la case, false sinon
	 * 
	 */
	public void marqueur(int x, int y, boolean marque){
		this.map[x][y].marqueur(marque);
	}
	
	/**
	 * 
	 * @method createPath
	 * @param {int} position de d�part x
	 * @param {int} position de d�part y
	 * @param {int} position d'arrivee x
	 * @param {int} position d'arrivee y
	 * @desc retourne un chemin entre deux emplacements
	 * 
	 */
	
	private int distance(int a, int b, int c, int d){
		return Math.abs(a-c)+Math.abs(b-d);
	}
	
	public Chemin createPath(int dx, int dy, int ax, int ay){
		Chemin path = new Chemin();
		ArrayList<ListeCase> recherche=new ArrayList<ListeCase>();
		ArrayList<ListeCase> closed=new ArrayList<ListeCase>();
		ArrayList<Case> check=new ArrayList<Case>();
		recherche.add(new ListeCase(this.map[dx][dy],0,null));
		recherche.get(0).setDistance(this.distance(dx, dy, ax, ay));
		check.add(this.map[dx][dy]);
		while(recherche.get(0).current()!=this.map[ax][ay]){
			for(int k=0;k<recherche.get(0).current().boundSize();k++){
				if(!check.contains(recherche.get(0).current().get(k))){
					recherche.add(new ListeCase(recherche.get(0).current().get(k),recherche.get(0).getCout()+1,recherche.get(0)));
					recherche.get(0).setDistance(this.distance(recherche.get(0).current().coord()[0], recherche.get(0).current().coord()[1], ax, ay));
					check.add(recherche.get(0).current().get(k));
				}
			}
			closed.add(recherche.get(0));
			recherche.remove(0);
			if(recherche.size()>0)
				Collections.sort(recherche);
			else
				break;
		}
		ListeCase temp = recherche.get(0);
		while(temp.current()!=this.map[dx][dy]){
			path.add(temp.current());
			temp=temp.previous();
		}
		return path;
	}
}
