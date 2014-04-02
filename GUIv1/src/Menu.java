import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.Box;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.Component;


public class Menu {

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
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 683, 516);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel Laby = new JPanel();
		frame.getContentPane().add(Laby, BorderLayout.CENTER);
		
		
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.EAST);
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setEnabled(false);
		panel.add(verticalBox);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Robot 1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		verticalBox.add(panel_4);
		
		Box verticalBox_1 = Box.createVerticalBox();
		verticalBox_1.setEnabled(false);
		panel_4.add(verticalBox_1);
		
		JLabel nom = new JLabel("Nom");
		verticalBox_1.add(nom);
		
		nomR1 = new JTextField();
		verticalBox_1.add(nomR1);
		nomR1.setColumns(10);
		
		JLabel adR = new JLabel("Adresse Robot");
		verticalBox_1.add(adR);
		
		adR1 = new JTextField();
		verticalBox_1.add(adR1);
		adR1.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalBox_1.add(verticalStrut);
		
		JButton valid1 = new JButton("Connecter");
		verticalBox_1.add(valid1);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Robot 2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		verticalBox.add(panel_5);
		
		Box verticalBox_2 = Box.createVerticalBox();
		panel_5.add(verticalBox_2);
		
		JLabel label = new JLabel("Nom");
		verticalBox_2.add(label);
		
		nomR2 = new JTextField();
		nomR2.setColumns(10);
		verticalBox_2.add(nomR2);
		
		JLabel label_1 = new JLabel("Adresse Robot");
		verticalBox_2.add(label_1);
		
		adR2 = new JTextField();
		adR2.setColumns(10);
		verticalBox_2.add(adR2);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalBox_2.add(verticalStrut_1);
		
		JButton valid2 = new JButton("Connecter");
		verticalBox_2.add(valid2);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "Robot 3", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		verticalBox.add(panel_6);
		
		Box verticalBox_3 = Box.createVerticalBox();
		panel_6.add(verticalBox_3);
		
		JLabel label_2 = new JLabel("Nom");
		verticalBox_3.add(label_2);
		
		nomR3 = new JTextField();
		nomR3.setColumns(10);
		verticalBox_3.add(nomR3);
		
		JLabel label_3 = new JLabel("Adresse Robot");
		verticalBox_3.add(label_3);
		
		adR3 = new JTextField();
		adR3.setColumns(10);
		verticalBox_3.add(adR3);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		verticalBox_3.add(verticalStrut_2);
		
		JButton valid3 = new JButton("Connecter");
		verticalBox_3.add(valid3);
	}

}
