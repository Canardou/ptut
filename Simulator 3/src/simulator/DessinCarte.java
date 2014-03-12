package simulator;
import java.awt.* ;
import java.awt.image.* ;

public class DessinCarte {
	/*
	 * Attributs
	 */
	
	private int width;
	private int heigth;
	private Stack<DessinRobot> robots;
	
	/*
	 * Constructeur
	 */
	
	public DessinCarte(int width, int heigth){
		this.width=width;
		this.heigth=heigth;
		this.robots=new Stack<DessinRobot>();
	}
}
