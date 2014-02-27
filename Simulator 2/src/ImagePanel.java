/**
 *  Cette classe gere une image de fond + des sprites qui peuvent se deplacer
 *  (Bas-niveau)
 */

import javax.swing.* ;
import java.awt.* ;
import java.util.* ;

public class ImagePanel extends JPanel {

    private Image image ;
    private Vector<Sprite> sprites = new Vector<Sprite> () ;

    public void setImage(Image image) {
	this.image = image;
    }
    
    public Sprite addSprite(String imgName, int x, int y) {
	Sprite s = new Sprite(imgName, x, y, this) ;
	sprites.add(s) ;
	return s ;
    }

    public void paintComponent(Graphics g) {
	if(image != null) {
	    g.drawImage(image, 0, 0, this);
	    
	    for (Enumeration<Sprite> e = sprites.elements () ; e.hasMoreElements() ;) {
		Sprite s = e.nextElement () ;
		s.dessine (g, this) ;
	    }
	}
    }
    
    public Dimension getPreferredSize() {
	int w, h;
	if(image == null) {
	    return new Dimension(0, 0);
	}
	w = image.getWidth(null);
	h = image.getHeight(null);
	return new Dimension(w > 0 ? w : 0, h > 0 ? h : 0);
    }
}
