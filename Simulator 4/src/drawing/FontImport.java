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
	 * M�thodes
	 */
	
	public static Font getFont(String path){
		Font temp=null;
		try {
			temp = Font.createFont(Font.TRUETYPE_FONT, new File(path));
		}
		catch (IOException | FontFormatException e){
			Dialogue.Error("Probl�me lors de l'importation de font");
		}
		return temp;
	}
}
