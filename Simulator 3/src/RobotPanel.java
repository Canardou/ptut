import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Dialogue.Dialogue;
import drawing.DessinCarte;
import drawing.FontImport;


@SuppressWarnings("serial")
public class RobotPanel extends JPanel implements ActionListener {
	
	private Font comic = FontImport.getFont("ComicRelief.ttf");
	private Font bcomic = FontImport.getFont("ComicRelief-Bold.ttf");
	
	private JTextField x;
	private JTextField y;
	private JComboBox<String> direction;
	private JButton envoyer;
	private JTextArea log;
	
	private DessinCarte carte;
	private int id;

	RobotPanel(DessinCarte carte, int id){
		super();
		this.carte=carte;
		this.id=id;
		this.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Robot "+id, TitledBorder.LEADING, TitledBorder.TOP, this.bcomic.deriveFont(15f)));
		this.setPreferredSize(new Dimension(150,100));
		
		Box verticalBox_1 = Box.createVerticalBox();
		this.add(verticalBox_1);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox_1.add(horizontalBox_1);
		
		JLabel label_x = new JLabel("x");
		label_x.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(label_x);
		
		x = new JTextField();
		x.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(x);
		x.setColumns(2);
		
		Component verticalStrut = Box.createHorizontalStrut(2);
		horizontalBox_1.add(verticalStrut);
		
		JLabel label_y = new JLabel("y");
		label_y.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(label_y);
		
		y = new JTextField();
		y.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(y);
		y.setColumns(2);
		
		Component verticalStrut2 = Box.createHorizontalStrut(2);
		horizontalBox_1.add(verticalStrut2);
		
		String[] directions = { "Haut","Gauche","Bas","Droite"};
		direction = new JComboBox<String>(directions);
		direction.setFont(this.comic.deriveFont(12f));
		horizontalBox_1.add(direction);
		direction.addActionListener(this);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		horizontalBox_1.setEnabled(false);
		verticalBox_1.add(horizontalBox_2);
		
		horizontalBox_2.add(carte.getRobot(id).getIcone());
		
		horizontalBox_2.add(Box.createRigidArea(new Dimension(5,0)));
		
		envoyer = new JButton("M�J");
		envoyer.setFont(this.comic.deriveFont(12f));
		horizontalBox_2.add(envoyer);
		envoyer.addActionListener(this);
		
		log = new JTextArea();
		log.setVisible(false);
		log.setEditable(false);
		horizontalBox_2.add(log);
		
	}
	
	public void setPanel(int x, int y, int direction){
		this.x.setText(Integer.toString(x));
		this.y.setText(Integer.toString(y));
		this.direction.setSelectedIndex(direction);
	}
	
	public void freeze(){
		this.x.setEditable(false);
		this.y.setEditable(false);
		this.direction.setEnabled(false);
		this.envoyer.setVisible(false);
		this.log.setVisible(true);
	}
	
	public void unfreeze(){
		this.x.setEditable(true);
		this.y.setEditable(true);
		this.direction.setEnabled(true);
		this.envoyer.setVisible(true);
		this.log.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			int x = Integer.parseInt(this.x.getText());
			int y = Integer.parseInt(this.y.getText());
			if(x>=0 && x<this.carte.labyX() && y>=0 && y<this.carte.labyY()){
		this.carte.getRobot(this.id).moveTo(Integer.parseInt(this.x.getText()), Integer.parseInt(this.y.getText()),direction.getSelectedIndex());
		this.carte.getRobot(this.id).setVisible(true);}
			else{Dialogue.Warning(this.x.getText() +";"+this.y.getText() + " n'est pas une case du labyrinthe." );
 }}
		catch(Exception exept){
			Dialogue.Warning("Idiot ! " + this.x.getText() +";"+this.y.getText() + " ne sont pas des coordonnées valides." );
		}
	}
	
	
}
