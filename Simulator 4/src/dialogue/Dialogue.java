package dialogue;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Permet de gerer facilement les boites de dialogue de java pour informer l'utilisateur
 * @author Olivier Hachette
 *
 */

public class Dialogue {
	
	public Dialogue(){}
	
	/**
	 * Ouvre une boite de dialogue pour informer d'une erreur critique et ferme ensuite le programme
	 * @param s Le message a afficher
	 */
	public static void Error(String s){
		JOptionPane.showMessageDialog(null, s, "Erreur", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	/**
	 * Ouvre une boite de dialogue pour informer d'un probleme
	 * @param s Le message a afficher
	 */
	public static void Warning(String s){
		JOptionPane.showMessageDialog(null, s, "Avertissement", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Ouvre une boite de dialogue pour informer d'une reussite avec un chien anime
	 * @param s Le message a afficher
	 */
	public static void SuccessDoge(String s){
		JOptionPane.showMessageDialog(null, s, "Succes", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("success.gif"));
	}
	
	/**
	 * Ouvre une boite de dialogue pour informer d'une reussite sobre
	 * @param s Le message a afficher
	 */
	public static void Success(String s){
		JOptionPane.showMessageDialog(null, s, "Succes", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("success.png"));
	}
}
