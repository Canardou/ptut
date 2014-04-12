package drawing;
import java.awt.* ;
import java.awt.image.* ;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class RobotIcone extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnimationRobot sheet;
	
	/*
	 * Attributs
	 */

	private BufferedImage img ;
	private Graphics2D gr ;
	
	/*
	 * Constructeur
	 */
	
	public RobotIcone(int type){
		this.sheet=new AnimationRobot(type);
		this.sheet.setSequence("icone");
		this.img=new BufferedImage(this.sheet.getImage().getWidth(), this.sheet.getImage().getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		this.gr=this.img.createGraphics();
		this.gr.setBackground(new Color(255,255,255,0));
		this.setPreferredSize(new Dimension(this.sheet.getImage().getWidth(),this.sheet.getImage().getHeight()));
		this.setMaximumSize(new Dimension(this.sheet.getImage().getWidth(),this.sheet.getImage().getHeight()));
	}
	
	/*
	 * Methodes
	 */
	
	public void changeType(int type){
		this.sheet.setSequence("icone", type);
	}
	
	//@override
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }
	
	public void update(){
		this.sheet.nextImage();
		this.gr.clearRect(0, 0, this.img.getWidth(), this.img.getHeight());
		this.gr.drawImage(this.sheet.getImage(), 0, 0, this);
		this.repaint();
	}
}
