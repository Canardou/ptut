
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import Dialogue.Dialogue;
import labyrinth.*;
import drawing.*;

public class Superviseur {
	
	private static int agent=3;
	
	private Carte carte;
	private DessinCarte dessin;
	private Chemin [] current_paths;
	private int [] priority;
	private ArrayList<Chemin> next_paths;
	private int step;
	private int current;
	private Gui application;
	private int simulation;
	
	public Superviseur(Carte carte){
		this.current_paths=new Chemin[agent];
		this.priority=new int[agent];
		this.next_paths=new ArrayList<Chemin>();
		this.carte=carte;
		this.dessin=new DessinCarte(carte);
		this.dessin.launch();
		
		
		application = new Gui(this);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.pack();
		application.setVisible(true);
	}
	
	public void simulation(int seed) throws InterruptedException {
		Dialogue.Warning("Lancement d'une simulation");
		this.carte.rand.setSeed(seed);
		Carte labyrinth=new Carte(this.carte.getWidth(),this.carte.getHeight());
		labyrinth.rand.setSeed(carte.rand.nextInt());
		labyrinth.randomMaze(0.35);
		this.carte.reset();
		for(int i=0;i<3;i++){
			this.dessin.getRobot(i).moveTo(carte.rand.nextInt(this.carte.getWidth()),this.carte.rand.nextInt(this.carte.getHeight()),this.carte.rand.nextInt(4));
			this.dessin.getRobot(i).setVisible(true);
			current_paths[i]=new Chemin(this.carte.getCase(this.dessin.getRobot(i).getX(), this.dessin.getRobot(i).getY()));
			current_paths[i].setValue(i);
		}
		this.initialisation();
		
		int temps=0;
		step=0;
		//Les différentes étapes sont :
		//0 - Exploration
		//1 - Aller chercher la balle, quand un trajet robot 0 - balle existe
		//2 - Sortir, quand un trajet robot 0 - sortie existe
		while(true){
			this.dessin.lock.lock();
			try{
				for(int numero=0; numero<3; numero++){
					int x= dessin.getRobot(numero).getX();
					int y= dessin.getRobot(numero).getY();
					current_paths[numero].cut(carte.getCase(x, y));
					this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
					this.carte.reveal(x, y);
				
				switch(step){
				case 0:
						if(!dessin.getRobot(numero).busy()){
							int k=1;
							boolean retry=false;
							if(carte.closestDiscover(x, y, k)!=null){//Comportement de closest discover lorsque plus de cases ï¿½ visiter peut ï¿½tre problï¿½matique
								do{
									retry=false;
									if(carte.closestDiscover(x, y, k)!=null)
										current_paths[numero]=carte.closestDiscover(x, y, k);
									//Envois infos
									for(int j=0;j<3;j++){
										if(j!=numero){
											if(current_paths[numero].collision(carte, current_paths[j],true)<Math.max(current_paths[numero].size(),2)){
												retry=true;
											}
										current_paths[numero].beforeBlock(carte, current_paths[j],true);
										}
									}
								k++;
								}while(retry && k<5);
								if(retry=true && k==5){
									current_paths[numero]=carte.createPath(x, y, carte.rand.nextInt(this.carte.getWidth()),carte.rand.nextInt(this.carte.getHeight()));
									current_paths[numero].stopToVisibility();
									for(int j=0;j<3;j++){
										if(j!=numero){
											if(current_paths[numero].collision(carte, current_paths[j],true)<current_paths[numero].size()){
												retry=true;
											}
										current_paths[numero].beforeBlock(carte, current_paths[j],true);
										}
									}
								}
							current_paths[numero].setValue(numero);
							dessin.getRobot(numero).walkPath(new Chemin(current_paths[numero]));
							}
						}
					break;
				case 1:
					break;
				}
			}
					
				Chemin trajet=carte.pathToMark(dessin.getRobot(0).getX(), dessin.getRobot(0).getY());
				if(trajet!=null){
					if(!trajet.stopToVisibility())
						step=1;
				}
					this.application.updatePanel();
					int steps=0;
					while(steps<20){
						this.dessin.step.await();
						steps++;
					}
					temps+=2;
					System.out.println(temps/60+":"+temps%60);
				}finally{
					this.dessin.lock.unlock();
				}
			}
	}
	
	public void destin() throws InterruptedException {
		while(true){   //tant que marque pas trouvÃ©e
			Thread.sleep(1);
		}
	}
	
	public void initialisation(){
		for(int k=0;k<this.priority.length;k++)
			this.priority[k]=k;
		this.step=0;
	}
	
	public DessinCarte dessinCarte(){
		return this.dessin;
	}
}
