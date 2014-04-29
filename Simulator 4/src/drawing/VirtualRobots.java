package drawing;

import labyrinth.*;

import java.awt.image.* ;

public class VirtualRobots {
	/*
	 * Attributs
	 */
	
	public static int speed=10;

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
	
	public VirtualRobots(int id){
		this(0,0,0,id,id);
	}
	
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
	
	public void stop(){
		if(busy())
			this.path=new Chemin(this.path.get(0));
	}
	
	public boolean busy(){
		if(path!=null)
			return true;
		else
			return false;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public double getdX(){
		if(this.objectif!=0)
			return (this.x+(double)((this.ax-this.x)*this.mouvement)/this.objectif);
		else
			return this.x;
	}
	
	public double getdY(){
		if(this.objectif!=0)
			return (this.y+(double)((this.ay-this.y)*this.mouvement)/this.objectif);
		else
			return this.y;
	}
	
	public int getDir(){
		return this.direction;
	}
	
	public int getAX(){
		return this.ax;
	}
	
	public int getAY(){
		return this.ay;
	}
	
	public Chemin getPath(){
		return this.path;
	}
	
	public void moveTo(int x, int y){
		moveTo(x,y,this.direction);
	}
	
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
	
	public void changeType(int type){
		this.type=type;
		this.sheet.changeType(type);
		this.icone.changeType(type);
	}
	
	public int getType(){
		return this.type;
	}
	
	public int getID(){
		return this.id;
	}
	
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
	
	public BufferedImage draw(){
		return this.sheet.getImage();
	}
	
	public RobotIcone getIcone(){
		return this.icone;
	}
	
	public void setVisible(boolean visible){
		this.visible=visible;
	}
	
	public boolean isVisible(){
		return this.visible;
	}
}
