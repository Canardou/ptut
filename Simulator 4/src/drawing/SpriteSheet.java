package drawing;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.* ;

import Dialogue.Dialogue;

public class SpriteSheet {
	
	/*
	 * Attributs
	 */
	
	private final int WIDTH;
	private final int HEIGHT;
	private BufferedImage image;
	
	/*
	 * Constructeur
	 */
	
	public SpriteSheet(String sheet, int width, int heigth){
		this.WIDTH=width;
		this.HEIGHT=heigth;
		try {
			image = ImageIO.read(new File(sheet));
		}
		catch (IOException e){
			Dialogue.Error("Problème lors de l'importation de l'image "+sheet);
		}
	}
	
	public SpriteSheet(String sheet){
		try {
			image = ImageIO.read(new File(sheet));
		}
		catch (IOException e){
			Dialogue.Error("Problème lors de l'importation de l'image "+sheet);
		}
		if(image!=null){
			this.WIDTH=image.getWidth();
			this.HEIGHT=image.getHeight();
		}
		else{
			this.WIDTH=0;
			this.HEIGHT=0;
		}
	}
	
	/*
	 * Méthodes
	 */
	
	public BufferedImage getImage(int x, int y){
		if(image!=null)
			return image.getSubimage(x*WIDTH, y*HEIGHT,WIDTH,HEIGHT);
		else
			return null;
	}
	
	public BufferedImage getImage(){
		return this.getImage(0,0);
	}
	
	public int getHeight(){
		return this.HEIGHT;
	}
	
	public int getWidth(){
		return this.WIDTH;
	} 
}
