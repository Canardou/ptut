package drawing;

import labyrinth.*;

import java.awt.* ;
import java.awt.image.* ;

public class VirtualRobots {
	/*
	 * Attributs
	 */
	
	private int type;
	private int exposition;
	private int x;
	private int y;
	private double dx;
	private double dy;
	private int direction;
	private Chemin path;
	private AnimationRobot sheet;
	
	/*
	 * Constructeurs
	 */
	
	public VirtualRobots(int x, int y, int direction, int type){
		this.direction=direction;
		this.x=x;
		this.y=y;
		this.dx=(double)x;
		this.dy=(double)y;
		this.type=type;
		this.sheet = new AnimationRobot(type);
	}
	
	
	/*
	 * Méthodes
	 */
	
	public boolean walkPath(Chemin path){
		if(this.x==(int)this.dx && this.y==(int)this.dy && this.x==path.getX(0) && this.y==path.getY(0)){
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
		return this.dx;
	}
	
	public double getdY(){
		return this.dy;
	}
	
	public void moveTo(int x, int y, int direction){
		this.path=null;
		this.x=x;
		this.y=y;
		this.dx=(double)x;
		this.dy=(double)y;
		this.direction=direction;
	}
	
	public void changeType(int type){
		this.sheet.changeType(type);
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
				if((int)(this.dx*10)!=this.path.getX(1)*10 || (int)(this.dy*10)!=this.path.getY(1)*10){
					switch(this.path.direction(0)){
					case Case.UP:
						this.sheet.setSequence("up");
						this.dy-=0.05;
						break;
					case Case.DOWN:
						this.sheet.setSequence("down");
						this.dy+=0.05;
						break;
					case Case.LEFT:
						this.sheet.setSequence("left");
						this.dx-=0.05;
						break;
					case Case.RIGHT:
						this.sheet.setSequence("right");
						this.dx+=0.05;
						break;
					}
				}
				else{
					this.x=this.path.getX(1);
					this.y=this.path.getY(1);
					this.dx=(double)x;
					this.dy=(double)y;
					this.path.removeTop();
				}
			}
			else{
				this.x=this.path.getX(0);
				this.y=this.path.getY(0);
				this.dx=(double)x;
				this.dy=(double)y;
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
