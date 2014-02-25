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
							this.bound(i,j,i+k,j+l);
						}
					}
				}
			}
		}
	}
	
	public Carte(int side){
		this(side,side);
		this.exit=null;
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
	
	public void bound(int dx, int dy, int ax, int ay){
		if(ax<0 || ax>this.width || ay<0 || ay>this.heigth){
			this.map[dx][dy].bound(this.exit = new Case(ax,ay));
			this.exit.bound(this.map[dx][dy]);
		}
		else if((dx-ax==-1 && dy-ay==0) || (dx-ax==1 && dy-ay==0) || (dx-ax==0 && dy-ay==-1) || (dx-ax==0 && dy-ay==1)){
			this.map[dx][dy].bound(this.map[ax][ay]);
			this.map[ax][ay].bound(this.map[dx][dy]);
		}
	}
	
	public void close(int dx, int dy, int ax, int ay){
		if(!(ax<0 || ax>this.width || ay<0 || ay>this.heigth) && (dx-ax==-1 && dy-ay==0) || (dx-ax==1 && dy-ay==0) || (dx-ax==0 && dy-ay==-1) || (dx-ax==0 && dy-ay==1)){
			this.map[dx][dy].close(this.map[ax][ay]);
			this.map[ax][ay].close(this.map[dx][dy]);
		}
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
	public void marqueur(int x, int y, boolean marque){
		this.map[x][y].marqueur(marque);
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
	
	public Case getCase(int x, int y){
		return this.map[x][y];
	}
	
	public boolean isPath(int dx, int dy, int ax, int ay){
		if(this.exit != null){
			if(this.exit.coord()[1]==ax && this.exit.coord()[2]==ay)
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
