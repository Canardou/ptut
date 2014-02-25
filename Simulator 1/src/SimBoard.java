import javax.swing.* ;

import java.awt.* ;
import java.awt.image.* ;



public class SimBoard {

    private JFrame frame ;
    private ImagePanel panel ;
    private BufferedImage img ;
    private Graphics2D gr ;
    
    public SimBoard (int largeur, int hauteur) {
		this.frame = new JFrame ("YOLO");
		this.frame.setResizable(false) ;

		this.img   = new BufferedImage (largeur, hauteur, BufferedImage.TYPE_3BYTE_BGR) ;
		this.gr    = this.img.createGraphics() ;
		this.panel = new ImagePanel () ;
		
		this.panel.setImage(this.img) ;
	    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.frame.setSize(largeur, hauteur);
		
		this.frame.setContentPane(this.panel) ;  
	    this.frame.setVisible(true);
	
		this.setColor (new Color(255, 255, 255)) ;
		gr.fillRect(0,0,largeur,hauteur) ;
	
		this.setColor (Color.black) ;
		this.repaint() ;
    }	
    
    // Force le reaffichage de la fenetre
    public void repaint() {
    	panel.repaint() ;
    }
        
    // Fixe la couleur du crayon pour les dessins futurs
    public void setColor (Color col) {
    	this.gr.setColor (col) ;
    }
    
    public void grille() {
		for(int i=Param.TAILLECASE;i<Param.HAUTEURBOARD;i+=Param.TAILLECASE) {
			this.horLine(i);
		}
		
		for(int i=Param.TAILLECASE;i<Param.LARGBOARD;i+=Param.TAILLECASE) {
			this.verLine(i);
		}
    }
    
    public void horLine(int y) {
		this.setColor (new Color(235, 235, 235)) ;
    	this.gr.drawLine(0,y,Param.LARGBOARD,y);
    	
    	this.repaint();
    	this.setColor (Color.black) ;
    }
    
    public void verLine(int x) {
		this.setColor (new Color(235, 235, 235)) ;
    	this.gr.drawLine(x,0,x,Param.HAUTEURBOARD);
    	this.repaint();
    	this.setColor (Color.black) ;
    }
    
    public void Line(int x, int y, int width, int heigth) {
		this.setColor (new Color(235, 235, 235)) ;
    	this.gr.drawLine(x,y,x+width,y+heigth);
    	this.repaint();
    	this.setColor (Color.black) ;
    }
        
    // Cree un nouveau sprite a partir du fichier image indique
    public Sprite addSprite(String imgName, int x, int y) {
	return panel.addSprite(imgName, x, y) ;
    }

    
}


