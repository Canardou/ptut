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
	
	public Carte(int width, int heigth){
		this.heigth = heigth;
		this.width = width;
		this.exit=null;
		this.map = new Case[width][heigth];
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.heigth;j++){
				this.map[i][j]=new Case(i,j);
			}
		}
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.heigth;j++){
				for(int k=-1;k<=1;k++){
					for(int l=-1;l<=1;l++){
						if(i+k>=0 && j+l>=0 && i+k<this.width && j+l<this.heigth){
							this.bound(i,j,k,l);
						}
					}
				}
			}
		}
	}
	
	public Carte(int side){
		this(side,side);
	}
	
	/*
	 * Méthodes
	 */
	
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
		return this.bound(x, y, 0, -1);
	}
	
	public boolean boundDown(int x, int y){
		return this.bound(x, y, 0, 1);
	}
	
	public boolean boundLeft(int x, int y){
		return this.bound(x, y, -1, 0);
	}
	
	public boolean boundRight(int x, int y){
		return this.bound(x, y, 1, 0);
	}
	
	public boolean bound(int x, int y, int w, int h){
		int ax=x+w;
		int ay=y+h;
		if(ax<0 || ax>=this.width || ay<0 || ay>=this.heigth){
			this.map[x][y].bound(this.exit = new Case(ax,ay));
			return this.exit.bound(this.map[x][y]);
		}
		else if((w==-1 && h==0) || (w==1 && h==0) || (w==0 && h==-1) || (w==0 && h==1)){
			this.map[x][y].bound(this.map[ax][ay]);
			return this.map[ax][ay].bound(this.map[x][y]);
		}
		else
			return false;
	}
	
	public boolean close(int x, int y, int w, int h){
		int ax=x+w;
		int ay=y+h;
		if(!(ax<0 || ax>=this.width || ay<0 || ay>=this.heigth) && (w==-1 && h==0) || (w==1 && h==0) || (w==0 && h==-1) || (w==0 && h==1)){
			this.map[x][y].close(this.map[ax][ay]);
			return this.map[ax][ay].close(this.map[x][y]);
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
	public void setMarqueur(int x, int y, boolean marque){
		this.map[x][y].setMarqueur(marque);
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
		return createPath(this.map[dx][dy],this.map[ax][ay]);
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
			for(int k=0;k<recherche.get(0).current().boundSize();k++){
				if(!check.contains(recherche.get(0).current().get(k))){
					recherche.add(new ListeCase(recherche.get(0).current().get(k),recherche.get(0).getCout()+1,recherche.get(0)));
					recherche.get(0).setDistance(this.distance(recherche.get(0).current().getX(), recherche.get(0).current().getY(), arrivee.getX(), arrivee.getY()));
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
		while(temp.current()!=depart){
			path.add(temp.current());
			temp=temp.previous();
		}
		path.add(temp.current());
		return path;
	}
	
	public Chemin toExitPath(int x, int y){
		if(this.exit!=null){
			return createPath(this.map[x][y],this.exit);
		}
		else
			return null;
	}
	
	public Case getCase(int x, int y){
		return this.map[x][y];
	}
	
	public boolean isCrossable(int dx, int dy, int ax, int ay){
		if(this.exit != null){
			if(this.exit.getX()==ax && this.exit.getY()==ay)
				return true;
		}
		if(ax<0 || ax>=this.width || ay<0 || ay>=this.heigth) {
			return false;
		}
		else if(dx<0 || dx>=this.width || dy<0 || dy>=this.heigth) {
			return false;
		}
		else if((dx-ax==-1 && dy-ay==0) || (dx-ax==1 && dy-ay==0) || (dx-ax==0 && dy-ay==-1) || (dx-ax==0 && dy-ay==1)){
			return this.map[dx][dy].linked(this.map[ax][ay]);
		}
		else
			return false;
	}
}
