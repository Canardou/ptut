package drawing;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.*;

import Dialogue.Dialogue;

public class FontImport {
	
	/*
	 * Constructeur
	 */
	
	public FontImport(){}
	
	/*
	 * Méthodes
	 */
	
	public static Font getFont(String path){
		Font temp=null;
		try {
			temp = Font.createFont(Font.TRUETYPE_FONT, new File(path));
		}
		catch (IOException | FontFormatException e){
			Dialogue.Error("Problème lors de l'importation de font");
		}
		return temp;
	}
}
