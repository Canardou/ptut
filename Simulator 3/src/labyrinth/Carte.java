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
	
	/*
	 * Constructeurs
	 */
	
	public Carte(int width, int heigth){
		this.heigth = heigth;
		this.width = width;
		this.map = new Case[width][heigth];
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.heigth;j++){
				this.map[i][j]=new Case(i,j);
			}
		}
		for(int i=0;i<this.width;i++){
			this.map[i][0].close(Case.UP);
			this.map[i][this.heigth-1].close(Case.DOWN);
		}
		for(int i=0;i<this.heigth;i++){
			this.map[0][i].close(Case.LEFT);
			this.map[this.width-1][i].close(Case.RIGHT);
		}
	}
	
	public Carte(int side){
		this(side,side);
	}
	
	/*
	 * Méthodes
	 */
	
	private boolean checkCoord(int x, int y){
		return !(x<0 || x>=this.width || y<0 || y>=this.heigth);
	}
	
	/**
	 * 
	 * @method close
	 * @param {int} position de départ x
	 * @param {int} position de départ y
	 * @param {int} position d'arrivee x
	 * @param {int} position d'arrivee y
	 * @desc déclare un chemin impraticable entre deux cases adjacentes (ou bord)
	 * 
	 */
	
	public boolean boundUp(int x, int y){
		return this.bound(x, y, Case.UP);
	}
	
	public boolean boundDown(int x, int y){
		return this.bound(x, y, Case.DOWN);
	}
	
	public boolean boundLeft(int x, int y){
		return this.bound(x, y, Case.LEFT);
	}
	
	public boolean boundRight(int x, int y){
		return this.bound(x, y, Case.RIGHT);
	}
	
	public boolean bound(int x, int y, int dir){
		if(checkCoord(x,y)){
			return this.map[x][y].bound(dir);
		}
		else
			return false;
			
	}
	
	
	public boolean closeUp(int x, int y){
		return this.close(x, y, Case.UP);
	}
	
	public boolean closeDown(int x, int y){
		return this.close(x, y, Case.DOWN);
	}
	
	public boolean closeLeft(int x, int y){
		return this.close(x, y, Case.LEFT);
	}
	
	public boolean closeRight(int x, int y){
		return this.close(x, y, Case.RIGHT);
	}
	
	public boolean close(int x, int y, int dir){
		if(checkCoord(x,y)){
			return this.map[x][y].close(dir);
		}
		else
			return false;
	}
	
	/**
	 * 
	 * @method visite
	 * @param {int} x
	 * @param {int} y
	 * @desc déclare une case déjà visitée
	 * 
	 */	
	public void reveal(int x, int y){
		if(checkCoord(x,y))
			this.map[x][y].setReveal();
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
	public void mark(int x, int y){
		if(checkCoord(x,y))
			this.map[x][y].isObjective();
	}
	
	/**
	 * 
	 * @method createPath
	 * @param {int} position de départ x
	 * @param {int} position de départ y
	 * @param {int} position d'arrivee x
	 * @param {int} position d'arrivee y
	 * @desc retourne un chemin entre deux emplacements
	 * 
	 */
	
	private int distance(int a, int b, int c, int d){
		return Math.abs(a-c)+Math.abs(b-d);
	}
	
	public Chemin createPath(int dx, int dy, int ax, int ay){
		if(checkCoord(dx,dy) && checkCoord(ax,ay))
			return createPath(this.map[dx][dy],this.map[ax][ay]);
		else
			return null;
	}
	
	public Chemin createPath(Case depart, Case arrivee){
		Chemin path = new Chemin();
		ArrayList<ListeCase> recherche=new ArrayList<ListeCase>();
		ArrayList<ListeCase> closed=new ArrayList<ListeCase>();
		ArrayList<Case> check=new ArrayList<Case>();
		recherche.add(new ListeCase(depart,0,null));
		recherche.get(0).setDistance(this.distance(depart.getX(), depart.getY(), arrivee.getX(), arrivee.getY()));
		check.add(depart);
		while(recherche.get(0).current()!=arrivee){
			for(int k=0;k<4;k++){
				Case temp=recherche.get(0).current();
				if(temp.isCrossable(k) && checkCoord(temp.getX(k),temp.getY(k))){
					Case test=this.map[temp.getX(k)][temp.getY(k)];
					if(!check.contains(test)){
						recherche.add(new ListeCase(test,recherche.get(0).getCout()+1,recherche.get(0)));
						recherche.get(0).setDistance(this.distance(test.getX(), test.getY(), arrivee.getX(), arrivee.getY()));
						check.add(test);
					}
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
		while(temp.current()!=depart){
			path.add(temp.current());
			temp=temp.previous();
		}
		path.add(temp.current());
		return path;
	}
	
	public Case getCase(int x, int y){
		return this.map[x][y];
	}
	
	public boolean isCrossableUp(int x, int y){
		return isCrossable(x,y,Case.UP);
	}
	
	public boolean isCrossableDown(int x, int y){
		return isCrossable(x,y,Case.DOWN);
	}
	
	public boolean isCrossableLeft(int x, int y){
		return isCrossable(x,y,Case.LEFT);
	}
	
	public boolean isCrossableRight(int x, int y){
		return isCrossable(x,y,Case.RIGHT);
	}
	
	public boolean isCrossable(int x, int y, int direction){
		if(checkCoord(x,y))
			return this.map[x][y].isCrossable(direction);
		else
			return false;
	}
}
