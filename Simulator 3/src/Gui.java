import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import drawing.AnimationRobot;
import drawing.DessinCarte;
import drawing.FontImport;
import drawing.VirtualRobots;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Gui extends JFrame implements ActionListener {

	private JFrame frame;
	private JTextField nomR1;
	private JTextField adR1;
	private JTextField nomR2;
	private JTextField adR2;
	private JTextField nomR3;
	private JTextField adR3;
	
	private Font comic = FontImport.getFont("ComicRelief.ttf");
	private Font bcomic = FontImport.getFont("ComicRelief-Bold.ttf");

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public Gui(DessinCarte carte){
		
		JPanel Laby = carte;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(Laby, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		this.getContentPane().add(panel, BorderLayout.EAST);
		
		JPanel panel2 = new JPanel();
		this.getContentPane().add(panel2, BorderLayout.SOUTH);
		
		JLabel nom2 = new JLabel("x");
		nom2.setFont(this.comic.deriveFont(12f));
		panel2.add(nom2);
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setEnabled(false);
		panel.add(verticalBox);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Robot 1", TitledBorder.LEADING, TitledBorder.TOP, this.bcomic.deriveFont(15f)));
		verticalBox.add(panel_4);
		
		Box verticalBox_1 = Box.createVerticalBox();
		verticalBox.setEnabled(false);
		panel_4.add(verticalBox_1);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		horizontalBox_1.setEnabled(false);
		verticalBox_1.add(horizontalBox_1);
		
		JLabel nom = new JLabel("x");
		nom.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(nom);
		
		nomR1 = new JTextField();
		nomR1.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(nomR1);
		nomR1.setColumns(2);
		
		Component verticalStrut = Box.createHorizontalStrut(2);
		horizontalBox_1.add(verticalStrut);
		
		JLabel adR = new JLabel("y");
		adR.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(adR);
		
		adR1 = new JTextField();
		adR1.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(adR1);
		adR1.setColumns(2);
		
		Component verticalStrut2 = Box.createHorizontalStrut(2);
		horizontalBox_1.add(verticalStrut2);
		
		String[] directions = { "Haut","Gauche","Bas","Droite"};
		JComboBox<String> direction = new JComboBox<String>(directions);
		direction.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(direction);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		horizontalBox_1.setEnabled(false);
		verticalBox_1.add(horizontalBox_2);
		
		horizontalBox_2.add(carte.getRobot(0).getIcone());
		
		
		JButton valid1 = new JButton("Envoyer");
		valid1.setFont(this.comic.deriveFont(12f));
		horizontalBox_2.add(valid1);
		
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    ;
	}

}
