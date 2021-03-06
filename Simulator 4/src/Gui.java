import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import java.awt.BorderLayout;

import dialogue.Dialogue;
import drawing.DessinCarte;
import drawing.FontImport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Affichage de la fenetre du programme
 * @author Olivier Hachette & Platon Clement
 *
 */

@SuppressWarnings("serial")
public class Gui extends JFrame implements ActionListener {

	private RobotPanel [] robots;
	
	private Font comic = FontImport.getFont("ComicRelief.ttf");
	private Font bcomic = FontImport.getFont("ComicRelief-Bold.ttf");
	
	private Superviseur superviseur;
	private DessinCarte laby;
	private JButton start;
	private JButton stop;
	private JButton simuler;
	private JTextField seed;
	private JCheckBox doge;
	
	private SwingWorker<String, Object> thread;

	public Gui(Superviseur superviseur){
		this.setTitle("Dogeberto v 1.7.0.1b Final-bis Stable ULTIMATE");
		this.superviseur=superviseur;
		//Labyrinth sur le cote gauche
		this.laby = superviseur.dessinCarte();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(this.laby, BorderLayout.WEST);
		
		//Robots sur le cote droit
		JPanel panel = new JPanel();
		this.getContentPane().add(panel, BorderLayout.EAST);
		
		//Aligne verticalement les boites robots
		Box tempBox = Box.createVerticalBox();
		tempBox.setEnabled(false);
		panel.add(tempBox);
		
		robots = new RobotPanel[3];
		for(int i=0;i<3;i++){
			robots[i]=new RobotPanel(laby,i);
			tempBox.add(robots[i]);
		}
		
		JPanel panel2 = new JPanel();
		this.getContentPane().add(panel2, BorderLayout.SOUTH);

		Box tempHoriBox = Box.createHorizontalBox();
		panel2.add(tempHoriBox);
		
		start = new JButton("Commencer");
		start.setFont(this.bcomic.deriveFont(12f));
		start.addActionListener(this);
		tempHoriBox.add(start);
		
		tempHoriBox.add(Box.createRigidArea(new Dimension(5,0)));
		
		stop = new JButton("Arreter");
		stop.setFont(this.bcomic.deriveFont(12f));
		stop.addActionListener(this);
		stop.setVisible(false);
		tempHoriBox.add(stop);
		
		simuler = new JButton("Simulation");
		simuler.setFont(this.bcomic.deriveFont(12f));
		simuler.addActionListener(this);
		tempHoriBox.add(simuler);
		
		tempHoriBox.add(Box.createRigidArea(new Dimension(5,0)));
		
		seed = new JTextField();
		seed.setText("100");
		seed.setFont(this.comic.deriveFont(12f));
		tempHoriBox.add(seed);
		seed.setColumns(5);
		
		tempHoriBox.add(Box.createRigidArea(new Dimension(5,0)));
		
		doge = new JCheckBox("Doge");
		doge.addActionListener(this);
		tempHoriBox.add(doge);
		
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Met a jour l'affichage des coordonnees des robots par rapport au dessin
	 */
	public void updatePanel(){
		for(int i=0;i<3;i++){
			robots[i].setPanel(laby.getRobot(i).getX(), laby.getRobot(i).getY(), laby.getRobot(i).getDir());
		}
	}
	
	/**
	 * Met a jour l'affichage pour un robot par rapport aux arguements rentres
	 * @param x Nouvelle coordonnee x
	 * @param y Nouvelle coordonnee y
	 * @param dir Nouvelle direction
	 * @param id Identifiant du robot a mettre a jour
	 */
	public void updatePanel(int id, int x, int y, int dir){
		robots[id].setPanel(x, y, dir);
	}
	
	/**
	 * Rajoute une ligne de log pour un robot
	 * @param id Identifiant du robot
	 * @param s Log a afficher
	 */
	public void putLog(int id, String s){
		robots[id].putLog(s);
	}
	
	/**
	 * Remet a zero le log des robots
	 */
	public void reset(){
		for(int i=0;i<3;i++){
			robots[i].resetLog();
		}
	}
	
	/**
	 * Remise de la gui a l'etat principal lors de l'arret d'une simulation ou resolution reelle
	 */
	private void arreter(){
		//this.superviseur.end();
		this.thread.cancel(true);
		for(int i=0;i<3;i++){
			robots[i].unfreeze();
		}
		this.start.setVisible(true);
		this.stop.setVisible(false);
		this.simuler.setVisible(true);
		this.seed.setVisible(true);
		this.seed.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().compareTo("Simulation")==0){
			this.start.setVisible(false);
			this.stop.setVisible(true);
			this.simuler.setVisible(false);
			this.seed.setEnabled(false);
			for(int i=0;i<3;i++){
				robots[i].freeze();
			}
			class ThreadTemporaire extends SwingWorker<String, Object> {
				@Override
				public String doInBackground() {
					try{
						Gui.this.superviseur.simulation(Integer.parseInt(seed.getText()));
					}catch(InterruptedException ie){
						Dialogue.Warning("Arret de la simulation");
					}catch(Exception e){
						e.printStackTrace();
					}
					
					return null;
				}

				@Override
				protected void done() {
					Gui.this.arreter();
				}
			}
			(thread=new ThreadTemporaire()).execute();
		}
		if(e.getActionCommand().compareTo("Arreter")==0){
			this.arreter();
		}
		if(e.getActionCommand().compareTo("Commencer")==0){
			this.start.setVisible(false);
			this.stop.setVisible(true);
			this.simuler.setVisible(false);
			this.seed.setVisible(false);
			for(int i=0;i<3;i++){
				robots[i].freeze();
			}
			class ThreadTemporaire extends SwingWorker<String, Object> {
				@Override
				public String doInBackground() {
					try{
					Gui.this.superviseur.destin();
					}catch(InterruptedException ie){
						Dialogue.Warning("Arret de la supervision");
					}catch(Exception e){
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void done() {
					Gui.this.arreter();
				}
			}
			(thread=new ThreadTemporaire()).execute();
		}
		if(e.getActionCommand().compareTo("Doge")==0){
			this.laby.toggleDoge();
		}
		
	}
}
