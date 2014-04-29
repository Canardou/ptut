
import java.util.ArrayList;

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
		this.carte.update(labyrinth.export());
		for(int i=0;i<3;i++){
			this.dessin.getRobot(i).moveTo(carte.rand.nextInt(this.carte.getWidth()),this.carte.rand.nextInt(this.carte.getHeight()),this.carte.rand.nextInt(4));
			this.dessin.getRobot(i).setVisible(true);
			current_paths[i]=new Chemin(this.carte.getCase(this.dessin.getRobot(i).getX(), this.dessin.getRobot(i).getY()));
			current_paths[i].setValue(i);
		}
		this.initialisation();
		/*while(step==0 && current_paths){
		
		}
			
			//Initialisation
			/*int x= (int)robots[i].getX();
			int y= (int)robots[i].getY();
			this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
			this.carte.reveal(x, y);
			paths[i]=new Chemin(this.carte.getCase(x, y));*/
		
		
		
		
		//Dans une simulation dessin est utilis� comme r�f�rent pour le superviseur
		//Dans un cas r�el le superviseur devra �tre cadenc� par les appels blutooth
		
		//int numero=labyrinth.rand.nextInt(3);
		int temps=0;
		while(true){
			this.dessin.lock.lock();
			try{
				/*
				//Choix du robot et import des informations
				if(!dessin.getRobots()[numero].busy()){
					//R�solution
					int x= dessin.getRobots()[numero].getX();
					int y= dessin.getRobots()[numero].getY();
					this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
					this.carte.reveal(x, y);
					
					
					//DEBUT SUPERVISEUR
					int k=1;
					boolean retry=false;
					if(carte.closestDiscover(x, y, k)!=null){//Comportement de closest discover lorsque plus de cases � visiter peut �tre probl�matique
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
						current_paths[numero]=carte.createPath(x, y, labyrinth.rand.nextInt(this.carte.getWidth()),labyrinth.rand.nextInt(this.carte.getHeight()));
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
					dessin.getRobots()[numero].walkPath(new Chemin(current_paths[numero]));
					}
				}
				numero=labyrinth.rand.nextInt(3);
				*/
				//FIN SUPERVISEUR
				
				if(!this.dessin.getRobot(0).busy()){
					Chemin aux=new Chemin();
					
					aux.get().addAll(current_paths[1].get());
					aux.get().addAll(current_paths[2].get());
					
					//Chemin test =carte.createPath(this.dessin.getRobot(0).getX(), this.dessin.getRobot(0).getY(), carte.getMark().getX(), carte.getMark().getY(), aux);
					//Chemin test2=carte.createPath(this.dessin.getRobot(0).getX(), this.dessin.getRobot(0).getY(), carte.getMark().getX(), carte.getMark().getY());
					Chemin blocked = new Chemin();
					blocked.add(current_paths[2]);
					
					Chemin test3=carte.createPath(this.dessin.getRobot(0).getX(), this.dessin.getRobot(0).getY(), carte.rand.nextInt(carte.getWidth()), carte.rand.nextInt(carte.getHeight()), blocked);
					this.dessin.getRobot(0).walkPath(new Chemin(test3));
					blocked = new Chemin();
					
					
					blocked.add(current_paths[2]);
					if(test3!=null)
						blocked.get().add(test3.get(0));
					this.dessin.getRobot(1).walkPath(carte.closestDiscover(this.dessin.getRobot(1).getX(), this.dessin.getRobot(1).getY(), 1, test3, blocked,false));
					/*if(test2.isCollision(carte, current_paths[1], true))
						;
						//Faire bouger robot 1 de fa�on � lib�rer le chemin -> Noter qu'il faut qu'il sorte totalement du chemin pr�vu.
						//Comparer cette solution � l'�vitement en terme de cout
					if(test.getValue()<test2.getValue())
						;*/
				}
				this.application.updatePanel();
				int steps=0;
				while(steps<50){
					this.dessin.step.await();
					steps++;
				}
				temps+=5;
				System.out.println(temps);
				}finally{
					this.dessin.lock.unlock();
				}
			}
	}
	
	public void destin() throws InterruptedException {
		while(true){   //tant que marque pas trouvée
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
