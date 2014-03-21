package drawing;

import labyrinth.*;

import java.awt.* ;
import java.awt.image.* ;

public class VirtualRobots {
	/*
	 * Attributs
	 */
	
	private final int precision=100;
	private final int speed=10;
	
	private static int number=0;
	private int id;
	
	private int type;
	private int exposition;
	private int x;
	private int y;
	private int dx;
	private int dy;
	private int direction;
	private int ddir;
	private Chemin path;
	private AnimationRobot sheet;
	
	/*
	 * Constructeurs
	 */
	
	public VirtualRobots(int x, int y, int direction, int type){
		VirtualRobots.number++;
		this.id=VirtualRobots.number;
		this.direction=direction;
		this.x=x;
		this.y=y;
		this.dx=x*precision;
		this.dy=y*precision;
		this.ddir=direction*precision;
		this.type=type;
		this.sheet = new AnimationRobot(type);
	}
	
	
	/*
	 * Méthodes
	 */
	
	public boolean walkPath(Chemin path){
		if(this.x==this.dx/precision && this.y==this.dy/precision && this.x==path.getX(0) && this.y==path.getY(0)){
			this.path = path;
			return true;
		}
		else
			return false;
	}
	
	public void moveTo(int x, int y){
		moveTo(x,y,0);
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public double getdX(){
		return (double)this.dx/precision;
	}
	
	public double getdY(){
		return (double)this.dy/precision;
	}
	
	public void moveTo(int x, int y, int direction){
		this.path=null;
		this.x=x;
		this.y=y;
		this.dx=x*precision;
		this.dy=y*precision;
		this.direction=direction;
		this.ddir=direction*precision;
	}
	
	public void changeType(int type){
		this.type=type;
		this.sheet.changeType(type);
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
			this.exposition=0;
		}
		if(this.path!=null){
			if(this.path.size()>1){
				if(this.dx!=this.path.getX(1)*precision || this.dy!=this.path.getY(1)*precision){
					switch(this.path.direction(0)){
					case Case.UP:
						if(this.ddir!=this.path.direction(0)*precision){
							this.sheet.setSequence("up_stand");
							if(this.direction==Case.RIGHT && this.ddir>0){
								this.ddir=-1*precision;
							}
							if(this.direction==Case.RIGHT){
								this.ddir+=0.01*speed*precision;
							}
							else{
								this.ddir-=0.01*speed*precision;
							}
						}
						else{
							this.sheet.setSequence("up");
							this.direction=Case.UP;
							this.dy-=precision*0.01*speed;
						}
						break;
					case Case.DOWN:
						if(this.ddir!=this.path.direction(0)*precision){
							this.sheet.setSequence("down_stand");
							if(this.direction>Case.DOWN){
								this.ddir-=0.01*speed*precision;
							}
							else{
								this.ddir+=0.01*speed*precision;
							}
						}
						else{
							this.sheet.setSequence("down");
							this.direction=Case.DOWN;
							this.dy+=precision*0.01*speed;
						}
						break;
					case Case.LEFT:
						if(this.ddir!=this.path.direction(0)*precision){
							this.sheet.setSequence("left_stand");
							if(this.direction>Case.LEFT){
								this.ddir-=0.01*speed*precision;
							}
							else{
								this.ddir+=0.01*speed*precision;
							}
						}
						else{
							this.sheet.setSequence("left");
							this.direction=Case.LEFT;
							this.dx-=precision*0.01*speed;
						}
						break;
					case Case.RIGHT:
						if(this.ddir!=this.path.direction(0)*precision){
							this.sheet.setSequence("right_stand");
							if(this.direction==Case.UP && this.ddir<3*precision){
								this.ddir=4*precision;
							}
							if(this.direction==Case.UP){
								this.ddir-=0.01*speed*precision;
							}
							else{
								this.ddir+=0.01*speed*precision;
							}
						}
						else{
							this.sheet.setSequence("right");
							this.direction=Case.RIGHT;
							this.dx+=precision*0.01*speed;
						}
						break;
					}
				}
				else{
					this.x=this.path.getX(1);
					this.y=this.path.getY(1);
					this.dx=x*precision;
					this.dy=y*precision;
					this.path.removeTop();
				}
			}
			else{
				this.x=this.path.getX(0);
				this.y=this.path.getY(0);
				this.dx=x*precision;
				this.dy=y*precision;
				this.path=null;
			}
		}
		else{
			this.sheet.setSequence("stand");
		}
		
	}
	
	public BufferedImage draw(){
		return this.sheet.getImage();
	}
}
