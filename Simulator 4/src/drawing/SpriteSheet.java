package drawing;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.* ;

import dialogue.Dialogue;

/**
 * Permet la gestion des feuilles d'animations
 * @author Olivier Hachette
 *
 */

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
	
	/**
	 * 
	 * @param sheet Le chemin vers l'image de sprites
	 * @param width La largeur du sprite
	 * @param heigth La longueur du sprite
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
	
	/**
	 * La longueur et la largeur seront celle de l'image entiere, permet d'importer facilement des images vers java
	 * @param sheet Le chemin vers l'image
	 */
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
