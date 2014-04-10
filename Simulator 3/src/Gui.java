import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import drawing.AnimationRobot;
import drawing.DessinCarte;
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
		this.getContentPane().add(Laby, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		this.getContentPane().add(panel, BorderLayout.EAST);
		
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setEnabled(false);
		panel.add(verticalBox);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Robot 1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		verticalBox.add(panel_4);
		
		Box verticalBox_1 = Box.createVerticalBox();
		verticalBox.setEnabled(false);
		panel_4.add(verticalBox_1);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		horizontalBox_1.setEnabled(true);
		verticalBox_1.add(horizontalBox_1);
		
		JLabel nom = new JLabel("x");
		horizontalBox_1.add(nom);
		
		nomR1 = new JTextField();
		horizontalBox_1.add(nomR1);
		nomR1.setColumns(2);
		
		Component verticalStrut = Box.createHorizontalStrut(2);
		horizontalBox_1.add(verticalStrut);
		
		JLabel adR = new JLabel("y");
		horizontalBox_1.add(adR);
		
		adR1 = new JTextField();
		horizontalBox_1.add(adR1);
		adR1.setColumns(2);
		
		Component verticalStrut2 = Box.createHorizontalStrut(2);
		horizontalBox_1.add(verticalStrut2);
		
		String[] directions = { "Haut","Gauche","Bas","Droite"};
		JComboBox<String> direction = new JComboBox<String>(directions);
		horizontalBox_1.add(direction);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		horizontalBox_1.setEnabled(true);
		verticalBox_1.add(horizontalBox_2);
		
		carte.getRobot(0).changeType(0);
		horizontalBox_2.add(carte.getRobot(0).getIcone());
		
		JButton valid1 = new JButton("Envoyer");
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
