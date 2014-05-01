package labyrinth;
import java.util.ArrayList;
import java.util.Random;
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
	
	public Random rand = new Random(100);
	
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
	
	public Carte(byte[] importation){
		this.update(importation);
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
	
	/**
	 * @method isRevealed
	 * @param {int} x
	 * @param {int} y
	 * @desc retourne si une case est visitee ou non
	 * 
	 */

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
	 * @method setExit
	 * @return {Case} exit
	 * @desc Cherche/Retourne automatiquement la sortie
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
			for(int i=0;i<4;i++)
				this.exit.close(i);
		}
		return this.exit;
	}
	
	/**
	 * @method pathToExit
	 * @param {int} position de départ x
	 * @param {int} position de départ y
	 * @return {Chemin} chemin
	 * @desc retourne un chemin entre la position et la sortie si existante, sinon null
	 * 
	 */
	
	public boolean exit(){
		if(this.exit!=null)
			return true;
		else
			return false;
	}
	
	public Chemin pathToExit(int dx, int dy){
		if(checkCoord(dx,dy)){
			if(setExit()!=null){
				return createPath(this.map[dx][dy],this.exit,null);
			}
		}
		return null;
	}
	
	/**
	 * @method pathToMark
	 * @param {int} position de départ x
	 * @param {int} position de départ y
	 * @return {Chemin} chemin
	 * @desc retourne un chemin entre la position et le marqueur si existant, sinon null
	 * 
	 */
	
	public boolean mark(){
		if(this.marque!=null)
			return true;
		else
			return false;
	}
	
	public Chemin pathToMark(int dx, int dy){
		if(checkCoord(dx,dy)){
			if(this.mark()){
				return createPath(this.map[dx][dy],this.marque,null);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @method createPath
	 * @param {int} position de départ x
	 * @param {int} position de départ y
	 * @param {int} position d'arrivee x
	 * @param {int} position d'arrivee y
	 * @return {Chemin} chemin
	 * @desc retourne un chemin entre deux emplacements
	 * 
	 */
	
	public Chemin createPath(int dx, int dy, int ax, int ay){
		if(checkCoord(dx,dy) && checkCoord(ax,ay))
			return createPath(this.map[dx][dy],this.map[ax][ay],null);
		else
			return null;
	}
	
	public Chemin createPath(int dx, int dy, int ax, int ay, Chemin blocked){
		if(checkCoord(dx,dy) && checkCoord(ax,ay)){
			return createPath(this.map[dx][dy],this.map[ax][ay],blocked);
		}
		else
			return null;
	}
	
	/**
	 * 
	 * @method createPath
	 * @param {Case} case de départ
	 * @param {Case} case d'arrivee
	 * @return {Chemin} chemin
	 * @desc retourne un chemin entre deux emplacements
	 * 
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
		//Tant que la case n'est pas celle d'arrivée on continu de chercher
		while(recherche.get(0).current()!=arrivee && !recherche.isEmpty()){	
			Case temp=recherche.get(0).current();
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
			if(recherche.size()>=1){
				recherche.remove(0);
				//Tri de cases à parcourir restante par distance depuis le point de départ
				if(recherche.size()>0)
					Collections.sort(recherche);
				else
					break;		
			}
		}
		if(!recherche.isEmpty()){
			//On retourne le chemin trouvé par parcours inversé des cases
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
	
	
	public Chemin closestDiscover(int dx, int dy, int number){
		if(checkCoord(dx,dy) && number>=0)
			return closestDiscover(this.map[dx][dy], number, null, null, false);
		else
			return null;
	}
	
	public Chemin closestDiscover(int dx, int dy, int number, Chemin avoid, Chemin blocked, boolean exit){
		if(checkCoord(dx,dy) && number>=0)
			return closestDiscover(this.map[dx][dy], number, avoid, blocked, exit);
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public Chemin closestDiscover(Case depart, int number, Chemin avoid, Chemin blocked, boolean canExit){
		Chemin path = new Chemin();
		ArrayList<ListeCase> recherche=new ArrayList<ListeCase>();
		ArrayList<Case> check=new ArrayList<Case>();
		if(blocked!=null){
				check.addAll(blocked.get());
		}
		if(this.exit()){
			check.remove(this.exit);
		}
		recherche.add(new ListeCase(depart,0,null,-1));
		check.add(depart);
		while(number>0 && recherche.size()>0){
			Case temp=recherche.get(0).current();
			if(avoid!=null){
				if(!avoid.get().contains(temp)){
					Boolean yolo=true;
					for(int k=0;k<4;k++){
						if(avoid.get().contains(this.getCase(temp.getX(k),temp.getY(k))) && this.isCrossable(temp.getX(), temp.getY(), k)){
							yolo=false;
						}
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
			//On retourne le chemin trouvé par parcours inversé des cases
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
	 * @method getCase
	 * @param {int} x
	 * @param {int} y
	 * @return {Case} case
	 */
	
	public Case getCase(int x, int y){
		if(checkCoord(x,y))
			return this.map[x][y];
		else
			return null;
	}
	
	/**
	 * 
	 * @method isCrossable
	 * @param {int} x
	 * @param {int} y
	 * @return {boolean} possible
	 * @desc retourne si un chemin existe vers une direction depuis la case
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
	
	/**
	 * 
	 * @method getWidth/Height
	 * @return {int} width/height
	 */
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	/**
	 * @method randomMaze
	 * @param {double} probabilite
	 * @desc Permet de générer un labyrinthe aléatoire, disposant d'une sortie, à des fins de tests. La probabilité augmente le nombre probable de murs enlevés au delà du minimum.
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
		//Tant qu'il reste des cases potentiellements non reliés aux autres, on continu la recherche
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
	
	//Fct de tests
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
	//Fct de tests
	public void update(byte[] importation){
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				this.map[i][j].update(importation[i+j*this.width+2]);
			}
		}
		if(importation[0]!=0){
			this.marque=this.map[(int)(importation[0]-1)][(int)(importation[1])];
		}
		this.setExit();
	}
	
	public void update(int x, int y, byte importation){
		if(checkCoord(x,y)){
			this.map[x][y].update(importation);
			if(this.map[x][y].isMark())
				this.mark(x,y);
		}
	}
}
