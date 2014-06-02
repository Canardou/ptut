package drawing;

import labyrinth.*;

import java.awt.image.* ;

/**
 * Cree et gerer les robots virtuels servant a la simulation et l'affichage du dessin de la carte
 * @author Olivier Hachette
 *
 */

public class VirtualRobots {
	/*
	 * Attributs
	 */
	
	/**
	 * La vitesse de deplacement des robots
	 */
	public static int speed=5;

	private int id;
	private int type;
	private int exposition;
	private int x;
	private int y;
	private int ax;
	private int ay;
	private int direction;
	private int mouvement;
	private int objectif;
	private Chemin path;
	private AnimationRobot sheet;
	private RobotIcone icone;
	private boolean visible;
	
	/*
	 * Constructeurs
	 */
	/**
	 * Cree un robot en mettant ses coordonnee et sa direction à 0, le type prend la même valeur que l'id
	 * @param id
	 */
	public VirtualRobots(int id){
		this(0,0,0,id,id);
	}
	
	/**
	 * 
	 * @param x Coordonnee en x du robot
	 * @param y Coordonnee en y du robot
	 * @param direction Direction du robot
	 * @param type Le type du robot
	 * @param id L'id du robot
	 */
	public VirtualRobots(int x, int y, int direction, int type, int id){
		this.id=id;
		this.direction=direction;
		this.x=x;
		this.y=y;
		this.ax=x;
		this.ay=y;
		this.type=type;
		this.sheet = new AnimationRobot(type);
		this.icone = new RobotIcone(type);
		this.mouvement=0;
		this.objectif=0;
		this.visible=false;
	}
	
	
	/*
	 * Méthodes
	 */
	
	/**
	 * Fait parcourir un chemin a un robot virtuel
	 * @param path Le chemin a parcourir
	 * @return Vrai si le robot va parcourir le chemin, c'est a dire s'il n'etait pas en deplacement et que la case de depart du chemin etait bien la sienne
	 */
	public boolean walkPath(Chemin path){
		if(path!=null){
			if(!busy() && this.x==this.ax && this.y==this.ay && this.x==path.getX(0) && this.y==path.getY(0)){
				this.path = path;
				return true;
			}
			else if (this.path!=null)
				return this.path.concatenation(path);
		}
		return false;
	}
	
	/**
	 * Arrete le deplacement en cours du robot
	 */
	public void stop(){
		if(busy())
			this.path=new Chemin(this.path.get(0));
	}
	
	/**
	 * Verifie que le robot n'est pas en deplacement
	 * @return Vrai si le robot est en deplacement
	 */
	public boolean busy(){
		if(path!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @return Derniere coordonnee en x entiere du robot
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * 
	 * @return Derniere coordonnee en y entiere du robot
	 */
	public int getY(){
		return this.y;
	}
	
	/**
	 * 
	 * @return Coordonnee en x du robot
	 */
	public double getdX(){
		if(this.objectif!=0)
			return (this.x+(double)((this.ax-this.x)*this.mouvement)/this.objectif);
		else
			return this.x;
	}
	
	/**
	 * 
	 * @return Coordonnee en y du robot
	 */
	public double getdY(){
		if(this.objectif!=0)
			return (this.y+(double)((this.ay-this.y)*this.mouvement)/this.objectif);
		else
			return this.y;
	}
	
	/**
	 * 
	 * @return Direction du robot
	 */
	public int getDir(){
		return this.direction;
	}
	
	/**
	 * 
	 * @return Prochaine coordonnee en x entiere du robot
	 */
	public int getAX(){
		return this.ax;
	}
	
	/**
	 * 
	 * @return Prochaine coordonnee en y entiere du robot
	 */
	public int getAY(){
		return this.ay;
	}
	
	/**
	 * 
	 * @return Le chemin courant du robot
	 */
	public Chemin getPath(){
		return this.path;
	}
	
	/**
	 * Deplace immediatement un robot a un autre emplacement, supprime son chemin courant. Il conserve son orientation.
	 * @param x Nouvelle coordonnee en x
	 * @param y Nouvelle coordonnee en y
	 */
	public void moveTo(int x, int y){
		moveTo(x,y,this.direction);
	}
	
	/**
	 * Deplace immediatement un robot a un autre emplacement, supprime son chemin courant
	 * @param x Nouvelle coordonnee en x
	 * @param y Nouvelle coordonnee en y
	 * @param direction Nouvelle direction
	 */
	public void moveTo(int x, int y, int direction){
		this.path=null;
		this.x=x;
		this.y=y;
		this.ax=x;
		this.ay=y;
		this.mouvement=0;
		this.objectif=0;
		this.direction=direction;
	}
	
	/**
	 * 
	 * @param type @see AnimationRobot
	 */
	public void changeType(int type){
		this.type=type;
		this.sheet.changeType(type);
		this.icone.changeType(type);
	}
	
	/**
	 * 
	 * @return Le type du robot
	 */
	public int getType(){
		return this.type;
	}
	
	/**
	 * 
	 * @return L'id du robot
	 */
	public int getID(){
		return this.id;
	}
	
	/**
	 * Met a jour l'animation du robot virtuel et son deplacement
	 */
	public void update(){
		if(exposition<5){
			this.exposition++;
		}
		else{
			this.sheet.nextImage();
			this.icone.update();
			this.exposition=0;
		}
		if(this.path!=null){
			if(this.path.size()>1 || this.mouvement<this.objectif){
				if(this.mouvement>=this.objectif){
					this.x=this.path.getX(0);
					this.y=this.path.getY(0);
					this.ax=this.path.getX(1);
					this.ay=this.path.getY(1);
					this.mouvement=0;
					this.objectif=150;
					if(this.direction!=this.path.direction(0)){
						if(Math.abs(this.direction-this.path.direction(0))==2)
							this.objectif+=200;
						else
							this.objectif+=100;
					}
					this.direction=this.path.direction(0);
					switch(this.path.direction(0)){
						case Case.UP:
							this.sheet.setSequence("up");
							break;
						case Case.DOWN:
							this.sheet.setSequence("down");
							break;
						case Case.LEFT:
							this.sheet.setSequence("left");
							break;
						case Case.RIGHT:
							this.sheet.setSequence("right");
							break;
					}
					this.path.removeTop();
				}
				else{
					this.mouvement+=speed;
					if(this.mouvement>this.objectif)
						this.mouvement=this.objectif;
				}
			}
			else{
				this.x=this.path.getX(0);
				this.y=this.path.getY(0);
				this.ax=x;
				this.ay=y;
				this.mouvement=0;
				this.objectif=0;
				this.path=null;
				this.stand();
			}
		}
		else{
			this.stand();
		}
	}
	
	/**
	 * Change l'animation du robot pour celle de stand tout en prenant en compte la direction
	 */
	private void stand(){
		switch(this.direction){
		case Case.UP:
			this.sheet.setSequence("stand_up");
			break;
		case Case.DOWN:
			this.sheet.setSequence("stand_down");
			break;
		case Case.LEFT:
			this.sheet.setSequence("stand_left");
			break;
		case Case.RIGHT:
			this.sheet.setSequence("stand_right");
			break;
		default :
			this.sheet.setSequence("stand");
		}
	}
	
	/**
	 * 
	 * @return Renvoit l'image courante du robot virtuel
	 */
	public BufferedImage draw(){
		return this.sheet.getImage();
	}
	
	/**
	 * 
	 * @return renvoit l'icone du robot
	 */
	public RobotIcone getIcone(){
		return this.icone;
	}
	
	/**
	 * Permet d'afficher nou pas les robots sur le dessin de la carte
	 * @param visible Mettre vrai pour rendre affiche le robot sur la carte
	 */
	public void setVisible(boolean visible){
		this.visible=visible;
	}
	
	/**
	 * Permet de verifier qu'un robot est affiche
	 * @return Retourne vrai si le robot est affiche sur la carte
	 */
	public boolean isVisible(){
		return this.visible;
	}
}
