package drawing;
import java.awt.image.BufferedImage;

public class AnimationRobot {
	/*
	 * Attributs
	 */
	
	public static final SpriteSheet robotSheet=new SpriteSheet("So_more_doge.png",32,32);
	
	public int type;
	private int step;
	private int step_max;
	private int row;
	private int col;
	private String animation;
	
	/*
	 * Constructeur
	 */
	
	public AnimationRobot(int type){
		this.step=0;
		this.step_max=0;
		this.row=0;
		this.col=0;
		this.animation=new String();
		this.setSequence("stand",type);
	}
	
	/*
	 * Méthodes
	 */
	
	public BufferedImage getImage(){
		return robotSheet.getImage(step,row);
	}
	
	public BufferedImage nextImage(){
		if(this.step<this.col+this.step_max-1)
			this.step++;
		else{
			this.step=this.col;
			if(type>=4){
				if(this.row==4)
					this.row=5;
				else if(this.row==5)
					this.row=4;
			}
		}
		return this.getImage();
	}
	
	public void setSequence(String animation){
		this.setSequence(animation,this.type);
	}
	
	public void changeType(int type){
		this.setSequence(this.animation,type);
	}
	
	public void setSequence(String animation, int type){
		if(!(this.animation.compareTo(animation)==0) || this.type!=type){
			this.type=type;
			this.animation=animation;
			if(type>=4){
				this.step_max=3;
				this.col=(type-4)*3;
				switch(animation){
				case "down":
					this.row=0;
					break;
				case "up":
					this.row=3;
					break;
				case "left":
					this.row=1;
					break;
				case "right":
					this.row=2;
					break;
				case "icone":
					this.step_max=4;
					this.row=6;
					this.col=(type-4)*4;
					break;
				case "stand":
				default:
					this.row=4;
					break;
				}
			}
			else{
				this.step_max=2;
				if(type==3)
					this.col=14;
				else
					this.col=12;
				switch(animation){
				case "up":
					this.row=3;
					break;
				case "stand_up":
					this.step_max=1;
					this.row=3;
					break;
				case "left":
					this.row=1;
					break;
				case "stand_left":
					this.step_max=1;
					this.row=1;
					break;
				case "right":
					this.row=2;
					break;
				case "stand_right":
					this.step_max=1;
					this.row=2;
					break;
				case "down":
					this.row=0;
					break;
				case "icone":
					this.col=12;
					if(type==3)
						this.row=5;
					else
						this.row=4;
					this.step_max=4;
					break;
				case "stand":
				case "stand_down":
				default :
					this.step_max=1;
					this.row=0;
					break;
				}
			}
			this.step=this.col;
		}
	}
}
