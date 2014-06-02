package drawing;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.*;

import dialogue.Dialogue;

/**
 * Permet la gestion de l'import des polices d'ecriture
 * @author Olivier Hachette
 *
 */

public class FontImport {
	/*
	 * Méthodes
	 */
	/**
	 * Permet d'importer plus facilement une police d'ecriture dans java
	 * @param path Le chemin du fichier de police d'ecriture
	 * @return La police d'ecriture importee
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
