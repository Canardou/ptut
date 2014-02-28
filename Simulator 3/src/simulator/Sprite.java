package simulator;
/**
 *   Un sprite est une image qui a vocation a se deplacer.
 *   (bas niveau)
 */

import java.awt.* ;
import java.awt.image.* ;

public class Sprite {

    // Position, dimension et image
    public int x, y ;
    private int width, height ;
    public BufferedImage image ;

    // ImagePanel ou se trouve le sprite
    private ImagePanel ip ;

    // Animation du robot : sequence d'images
    private BufferedImage[] seq ;

    // Index dans la sequence. -1 signifie qu'on ne la joue pas.
    private int indexSeq ; 

    // Nombre d'expositions de chaque image de la sequence
    private final int dureeExposition = 6 ;
    private int compteurExposition ;

    private boolean repeat ;
    


    /** Renvoie la largeur du sprite */
    public int getLarg() { return this.width ; }

    /** Renvoie la hauteur du sprite */
    public int getHaut() { return this.height ; }

    public Sprite (String imgfile, int x, int y, ImagePanel ip) {
	
	this.seq = null ;
	this.indexSeq = -1 ;
	this.ip = ip ;

	this.image = Images.get(imgfile) ;
	this.width  = this.image.getWidth() ;
	this.height = this.image.getHeight() ;
	
	this.x = x-this.image.getWidth()/2 ;
	this.y = y-this.image.getHeight()/2 ;
    }

    private void repaint() {
	this.ip.repaint (0, this.x, this.y, width, height) ;
    }

    
    public void moveTo (int x, int y) {
	this.repaint() ;
	this.x = x ;
	this.y = y ;
	this.repaint() ;
    }

    public void playSequence(BufferedImage[] seq, boolean repeat) {
	this.seq = seq ;
	this.indexSeq = 0 ;
	this.compteurExposition = this.dureeExposition ;
	this.repeat = repeat ;
    }

    public void dessine(Graphics g, ImagePanel im) {

	if (this.indexSeq >= 0 && this.indexSeq < this.seq.length) {
	    this.image = this.seq[this.indexSeq] ;

	    this.width  = this.image.getWidth() ;
	    this.height = this.image.getHeight() ;

	    if (this.compteurExposition-- <= 0) {
		this.compteurExposition = this.dureeExposition ;
		this.indexSeq ++ ;
	    }

	    if (this.indexSeq >= this.seq.length && this.repeat) {
		this.indexSeq = 0 ;
	    }
	}

	g.drawImage(this.image, this.x, this.y, im) ;

    }
}
