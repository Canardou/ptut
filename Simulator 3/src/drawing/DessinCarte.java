package drawing;
import java.util.*;
import java.awt.* ;
import java.awt.image.* ;

import javax.swing.JPanel;

import labyrinth.*;

public class DessinCarte extends JPanel {
	
	public static final int largeur=42;
	public static final int hauteur=42;
	private static final SpriteSheet wallSheet=new SpriteSheet("So_wall.png",25,25);
	private static final SpriteSheet grass=new SpriteSheet("So_grass.png");
	private static final SpriteSheet gray=new SpriteSheet("So_gray.png");
	private static final SpriteSheet ball=new SpriteSheet("So_ball.png");
	
	/*
	 * Attributs
	 */

	private Carte carte;
	private Stack<VirtualRobots> robots;
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean mark;
	private BufferedImage img ;
	private Graphics2D gr ;
	private boolean dogeMode;
	
	/*
	 * Constructeur
	 */
	
	public DessinCarte(Carte carte){
		super();
		this.carte=carte;
		this.mark=true;
		this.x=carte.getWidth();
		this.y=carte.getHeight();
		this.width=(x+1)*DessinCarte.largeur;
		this.height=(y+1)*DessinCarte.hauteur;
		this.robots = new Stack<VirtualRobots>();
		this.img   = new BufferedImage (width, height, BufferedImage.TYPE_4BYTE_ABGR) ;
		this.setPreferredSize(new Dimension(this.img.getWidth(),this.img.getWidth()));
		this.gr=this.img.createGraphics() ;
		
		this.dogeMode=true;
	}
	
	/*
	 * Methodes
	 */
	
	public void toggleDoge(){
		if(this.dogeMode)
			this.dogeMode=false;
		else
			this.dogeMode=true;
	}
	
	public void addRobot(int x, int y, int direction, int type){
		this.robots.add(new VirtualRobots(x,y,direction,type));
	}
	
	public VirtualRobots getRobot(int k){
		return this.robots.get(k);
	}
	
	//@override
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }
	
	protected void drawImageAt(BufferedImage image, int x, int y) {
		this.gr.drawImage(image, (x+1)*DessinCarte.largeur+(-image.getWidth())/2, (y+1)*DessinCarte.hauteur+(-image.getHeight())/2, this) ;
    }
	
	protected void drawImageAt(BufferedImage image, double x, double y) {
		this.gr.drawImage(image, (int)((x+1)*DessinCarte.largeur+(-image.getWidth())/2), (int)((y+1)*DessinCarte.hauteur+(-image.getHeight())/2), this) ;
    }
	
	public void update(){
		if(this.dogeMode==true){
			for(int i=0;i<=this.x+1;i++){
				for(int j=0;j<=this.y+1;j++){
					if((i>0 && i<=this.x || j>0 && j<=this.y) && this.carte.isRevealed(i-1, j-1)){
						this.gr.drawImage(grass.getImage(), i*DessinCarte.largeur-grass.getWidth()/2, j*DessinCarte.largeur-grass.getHeight()/2, this) ;
					}else{
						this.gr.drawImage(gray.getImage(), i*DessinCarte.largeur-grass.getWidth()/2, j*DessinCarte.largeur-grass.getHeight()/2, this) ;
					}
				}
			}
			for(int i=0;i<this.x;i++){
				for(int j=0;j<this.y;j++){
					if(j==0 && !this.carte.isCrossableUp(i, j)){
						this.gr.drawImage(wallSheet.getImage(0,0), i*DessinCarte.largeur+DessinCarte.largeur/2, (j)*DessinCarte.hauteur+DessinCarte.hauteur/2-wallSheet.getHeight()/2, this) ;
						this.gr.drawImage(wallSheet.getImage(1,0), i*DessinCarte.largeur+DessinCarte.largeur/2+wallSheet.getWidth(), (j)*DessinCarte.hauteur+DessinCarte.hauteur/2-wallSheet.getHeight()/2, this) ;
					}
					if(i==0 && !this.carte.isCrossableLeft(i, j)){
						this.gr.drawImage(wallSheet.getImage(0,2), (i)*DessinCarte.largeur+DessinCarte.largeur/2-wallSheet.getWidth()/2, j*DessinCarte.hauteur+DessinCarte.hauteur/2, this) ;
						this.gr.drawImage(wallSheet.getImage(0,3), (i)*DessinCarte.largeur+DessinCarte.largeur/2-wallSheet.getWidth()/2, j*DessinCarte.hauteur+DessinCarte.hauteur/2+wallSheet.getHeight(), this) ;
					}
					if(!this.carte.isCrossableRight(i, j)){
						this.gr.drawImage(wallSheet.getImage(0,2), (i+1)*DessinCarte.largeur+DessinCarte.largeur/2-wallSheet.getWidth()/2, j*DessinCarte.hauteur+DessinCarte.hauteur/2, this) ;
						this.gr.drawImage(wallSheet.getImage(0,3), (i+1)*DessinCarte.largeur+DessinCarte.largeur/2-wallSheet.getWidth()/2, j*DessinCarte.hauteur+DessinCarte.hauteur/2+wallSheet.getHeight(), this) ;
					}
					if(!this.carte.isCrossableDown(i, j)){
						this.gr.drawImage(wallSheet.getImage(0,0), i*DessinCarte.largeur+DessinCarte.largeur/2, (j+1)*DessinCarte.hauteur+DessinCarte.hauteur/2-wallSheet.getHeight()/2, this) ;
						this.gr.drawImage(wallSheet.getImage(1,0), i*DessinCarte.largeur+DessinCarte.largeur/2+wallSheet.getWidth(), (j+1)*DessinCarte.hauteur+DessinCarte.hauteur/2-wallSheet.getHeight()/2, this) ;
					}
				}
			}
			if(this.mark)
				this.drawImageAt(ball.getImage(),this.carte.getMark().getX(),this.carte.getMark().getY());
		}
		for(VirtualRobots robot : robots){
			robot.update();
			this.drawImageAt(robot.draw(),robot.getdX(),robot.getdY());
			}
		this.repaint();
		
	}
	
	public void removeMark(){
		this.mark=false;
	}
}
