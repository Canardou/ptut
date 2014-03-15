package drawing;
import java.awt.image.BufferedImage;

public class AnimationRobot {
	/*
	 * Attributs
	 */
	
	private static final SpriteSheet robotSheet=new SpriteSheet("So_more_doge.png",32,32);
	
	private int type;
	private int step;
	private int step_max;
	private int row;
	private int col;
	private String current;
	
	/*
	 * Constructeur
	 */
	
	public AnimationRobot(int type){
		if(type>=0 && type<=4)
			this.type=type;
		else
			this.type=5;
		this.step=0;
		this.step_max=0;
		this.row=0;
		this.col=0;
		this.current="";
	}
	
	public AnimationRobot(int type, String sequence){
		this();
		this.setSequence(sequence);
	}
	
	public AnimationRobot(){
		this(4);
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
			if(type!=5){
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
	
	public void setSequence(String animation, int type){
		if(!animation.contentEquals(this.current) || this.type!=type){
			this.current=animation;
			if(type!=4){
				this.step_max=3;
				this.col=type*3;
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
				case "stand":
					this.row=4;
					break;
				case "icone":
					this.step_max=4;
					if(type<3){
						this.row=6;
						this.col=type*4;
					}else{
						this.row=7;
						this.col=0;
					}
					break;
				}
			}
			else{
				this.step_max=1;
				this.row=7;
				switch(animation){
				case "down":
				case "stand":
					this.col=4;
					break;
				case "up":
					this.col=7;
					break;
				case "left":
					this.col=6;
					break;
				case "right":
					this.col=5;
					break;
				case "wow":
					this.col=11;
				case "icone":
					this.col=8;
					this.step_max=3;
				}
			}
			this.step=this.col;
		}
	}
	
	public void changeType(int type){
		setSequence(this.current,type);
		this.type=type;
	}

}
