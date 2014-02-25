import javax.imageio.* ;
import java.awt.* ;
import java.awt.image.* ;
import java.io.* ;

public class Images {

    public static BufferedImage[] explosion ;

    public static BufferedImage get(String imgfile) {
	try {
	    // Read from a file
	    File file = new File(imgfile);
	    return ImageIO.read(file);
	} catch (IOException e) {
	    System.err.println("Le fichier " + imgfile + " est introuvable.") ;
	    System.exit(1) ;
	    return null ;
	}
    }

    public static void init() {
	explosion = new BufferedImage[18] ;
	for (int i = 0 ; i < explosion.length ; i++) {
	    explosion[i] = get("Images/ex" + (i+1) + ".png") ;
	}
    }

}
