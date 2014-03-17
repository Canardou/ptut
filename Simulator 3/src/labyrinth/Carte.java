package labyrinth;
import java.util.ArrayList;
import java.util.Stack;
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
	
	private int height;
	private int width;
	private Case[][] map;
	private Case marque;
	private Case exit;
	
	/*
	 * Constructeurs
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
	
	public Carte(int side){
		this(side,side);
	}
	
	/*
	 * Méthodes
	 */
	
	private boolean checkCoord(int x, int y){
		return !(x<0 || x>=this.width || y<0 || y>=this.height);
	}
	
	/**
	 * 
	 * @method bound/close
	 * @param {int} coordonne x
	 * @param {int} coordonne y
	 * @return {bool} true si changement effectue
	 * @desc permet de notifier la présence de murs ou non aux bords de la case.
	 * 
	 */
	
	public boolean boundUp(int x, int y){
		this.bound(x, y-1, Case.DOWN);
		return this.bound(x, y, Case.UP);
	}
	
	public boolean boundDown(int x, int y){
		this.bound(x, y+1, Case.UP);
		return this.bound(x, y, Case.DOWN);
	}
	
	public boolean boundLeft(int x, int y){
		this.bound(x-1, y, Case.RIGHT);
		return this.bound(x, y, Case.LEFT);
	}
	
	public boolean boundRight(int x, int y){
		this.bound(x+1, y, Case.LEFT);
		return this.bound(x, y, Case.RIGHT);
	}
	
	private boolean bound(int x, int y, int dir){
		if(checkCoord(x,y)){
			return this.map[x][y].bound(dir);
		}
		else
			return false;
	}
	
	public boolean closeUp(int x, int y){
		this.close(x, y-1, Case.DOWN);
		return this.close(x, y, Case.UP);
	}
	
	public boolean closeDown(int x, int y){
		this.close(x, y+1, Case.UP);
		return this.close(x, y, Case.DOWN);
	}
	
	public boolean closeLeft(int x, int y){
		this.close(x-1, y, Case.RIGHT);
		return this.close(x, y, Case.LEFT);
	}
	
	public boolean closeRight(int x, int y){
		this.close(x+1, y, Case.LEFT);
		return this.close(x, y, Case.RIGHT);
	}
	
	private boolean close(int x, int y, int dir){
		if(checkCoord(x,y)){
			return this.map[x][y].close(dir);
		}
		else
			return false;
	}
	
	/**
	 * 
	 * @method reveal
	 * @param {int} x
	 * @param {int} y
	 * @desc declare une case visitee
	 * 
	 */
	
	public void reveal(int x, int y){
		if(checkCoord(x,y))
			this.map[x][y].setReveal();
	}

	public boolean isRevealed(int x, int y){
		if(checkCoord(x,y))
			return this.map[x][y].isRevealed();
		else
			return false;
	}
	
	/**
	 * 
	 * @method mark
	 * @param {int} coordonne x
	 * @param {int} coordonne y
	 * @param {bool} true si emplacement placé/replacé
	 * @desc definie l'emplacement du marqueur
	 * 
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
	 * @method getMark
	 * @return {Case} emplacement de la marque ou null
	 */
	
	public Case getMark(){
		return this.marque;
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
		}
		return this.exit;
	}
	
	public Chemin pathToExit(int dx, int dy){
		if(checkCoord(dx,dy)){
			if(setExit()!=null){
				return createPath(this.map[dx][dy],this.exit);
			}
		}
		return null;
	}
	
	public Chemin pathToMarque(int dx, int dy){
		if(checkCoord(dx,dy)){
			if(this.marque!=null){
				return createPath(this.map[dx][dy],this.marque);
			}
		}
		return null;
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
		recherche.add(new ListeCase(depart,0,null,-1,this.distance(depart.getX(), depart.getY(), arrivee.getX(), arrivee.getY())));
		check.add(depart);
		while(recherche.get(0).current()!=arrivee){
			for(int k=0;k<4;k++){
				Case temp=recherche.get(0).current();
				if(temp.isCrossable(k) && checkCoord(temp.getX(k),temp.getY(k))){
					Case test=this.map[temp.getX(k)][temp.getY(k)];
					test.setReveal();
					if(!check.contains(test)){
						int cout=1;
						if(temp.getDir(test)!=recherche.get(0).getDir() && recherche.get(0).getDir()!=-1)
							cout=2;
						recherche.add(new ListeCase(test,recherche.get(0).getCout()+cout,recherche.get(0),temp.getDir(test),this.distance(test.getX(), test.getY(), arrivee.getX(), arrivee.getY())));
						check.add(test);
					}
				}
				else if(this.exit!=null){
					if (this.exit.getX()==temp.getX(k) && this.exit.getY()==temp.getY(k)){
						int cout=1;
						if(temp.getDir(this.exit)!=recherche.get(0).getDir() && recherche.get(0).getDir()!=-1)
							cout=2;
						recherche.add(new ListeCase(this.exit,recherche.get(0).getCout()+cout,recherche.get(0),temp.getDir(this.exit),0));
						check.add(this.exit);
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
			path.push(temp.current());
			temp=temp.previous();
		}
		path.push(temp.current());
		return path;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	
	public Case getCase(int x, int y){
		return this.map[x][y];
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	
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
	
	private boolean isCrossable(int x, int y, int direction){
		if(checkCoord(x,y))
			return this.map[x][y].isCrossable(direction);
		else
			return false;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	/**
	 * Permet de générer un labyrinthe aléatoire, disposant d'une sortie, à des fins de tests
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
			}
		}
		Case temp = recherche.get((int)(Math.random()*recherche.size()));
		while(recherche.size()>0){
			ArrayList<Case> random = new ArrayList<Case>();
			if(Math.random()>wall)
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
				Case select = random.get((int)(Math.random()*random.size()));
				select.bound(select.getDir(temp));
				temp.bound(temp.getDir(select));
				stack.push(temp);
				temp=select;
			}
			else if(!stack.isEmpty()){
				temp=stack.pop();
			}
			else
				temp=recherche.get((int)(Math.random()*recherche.size()));
		}
		if(Math.random()>0.5){
			if(Math.random()>0.5){
				this.boundLeft(0,(int)(Math.random()*this.height));
			}
			else
				this.boundRight(this.width-1,(int)(Math.random()*this.height));
		}
		else{
			if(Math.random()>0.5){
				this.boundUp((int)(Math.random()*this.width),0);
			}
			else
				this.boundDown((int)(Math.random()*this.width),this.height-1);
		}
		this.mark((int)(Math.random()*this.width), (int)(Math.random()*this.height));
	}
}
