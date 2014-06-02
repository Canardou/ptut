package drawing;
import java.awt.image.BufferedImage;

/**
 * Permet la gestion des animations des robots :
 * @author Olivier Hachette
 *
 */

public class AnimationRobot {
	/*
	 * Attributs
	 */
	
	public static final SpriteSheet robotSheet=new SpriteSheet("So_more_doge.png",32,32);
	
	private int type;
	private int step;
	private int step_max;
	private int row;
	private int col;
	private String animation;
	
	/*
	 * Constructeur
	 */
	
	/**
	 * @param type Type du robot initialise
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
	
	/**
	 * @return L'image courante du robot
	 */
	public BufferedImage getImage(){
		return robotSheet.getImage(step,row);
	}
	
	/**
	 * Avance d'une image l'animation du robot
	 * @return L'image courante du robot
	 */
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
	
	/**
	 * Les differentes animations possibles sont
	 * <ul>
	 * <li>down</li>
	 * <li>up</li>
	 * <li>left</li>
	 * <li>right</li>
	 * <li>icone</li>
	 * <li>stand</li>
	 * <li>stand_up</li>
	 * <li>stand_down</li>
	 * <li>stand_left</li>
	 * <li>stand_right</li>
	 * </ul>
	 * @param animation La sequence voulue
	 */
	public void setSequence(String animation){
		this.setSequence(animation,this.type);
	}
	
	/**
	 * Met a jour le type du robot pour l'animation<br/>
	 * Les differents types sont
	 * <ul>
	 * <li>0,1,2 - Robot</li>
	 * <li>3 - Robot avec balle</li>
	 * <li>4 - Doge sans collier</li>
	 * <li>5 - Doge avec collier vert</li>
	 * <li>6 - Doge avec collier violet</li>
	 * <li>7 - Doge avec balle</li>
	 * </ul>
	 * @param type Nouveau type du robot
	 */
	public void changeType(int type){
		this.setSequence(this.animation,type);
	}
	
	/**
	 * Permet de regler à la fois le type et l'animation
	 * @param animation see {@link #setSequence(String)}
	 * @param type see {@link #changeType(int)}
	 */
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
