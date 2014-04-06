package Dialogue;
import javax.swing.JOptionPane;


public class Dialogue {
	
	public Dialogue(){}
	
	public static void Error(String s){
		JOptionPane.showMessageDialog(null, s, "Erreur", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	public static void Warning(String s){
		JOptionPane.showMessageDialog(null, s, "Avertissement", JOptionPane.WARNING_MESSAGE);
	}
}
