import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Dialogue.Dialogue;
import labyrinth.*;
import drawing.*;

public class Superviseur {
	private Carte carte;
	private DessinCarte dessin;
	private Chemin [] current_paths;
	private int [] priority;
	private ArrayList<Chemin> next_paths;
	private int step;
	private int current;
	
	
	public Superviseur(Carte carte){
		this.current_paths=new Chemin[3];
		this.carte=carte;
		this.dessin=new DessinCarte(carte);
		this.dessin.launch();
	}
	
	public void simulate() throws InterruptedException {
		Carte labyrinth=new Carte(this.carte.getWidth(),this.carte.getHeight());
		labyrinth.rand.setSeed(carte.rand.nextInt());
		labyrinth.randomMaze(0.35);
		for(int i=0;i<3;i++){
			dessin.getRobot(i).moveTo(carte.rand.nextInt(this.carte.getWidth()),carte.rand.nextInt(this.carte.getHeight()),carte.rand.nextInt(4));
			dessin.getRobot(i).setVisible(true);
		}
		this.initialisation();
		/*while(step==0 && current_paths){
		
		}
			current_paths[i]=new Chemin(this.carte.getCase(dessin.getRobots()[i].getX(), dessin.getRobots()[i].getY()));
			current_paths[i].setValue(i);
			//Initialisation
			/*int x= (int)robots[i].getX();
			int y= (int)robots[i].getY();
			this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
			this.carte.reveal(x, y);
			paths[i]=new Chemin(this.carte.getCase(x, y));*/
		
		dessin.showMark(true);
		//dessin.toggleDoge();
		Dialogue.Warning("Lancement d'une simulation");
		//Dans une simulation dessin est utilisé comme référent pour le superviseur
		//Dans un cas réel le superviseur devra être cadencé par les appels blutooth
		
		//int numero=labyrinth.rand.nextInt(3);
		int temps=0;
		while(true){
			dessin.lock.lock();
			try{
			/*
			//Choix du robot et import des informations
			if(!dessin.getRobots()[numero].busy()){
				//Résolution
				int x= dessin.getRobots()[numero].getX();
				int y= dessin.getRobots()[numero].getY();
				this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
				this.carte.reveal(x, y);
				
				
				//DEBUT SUPERVISEUR
				int k=1;
				boolean retry=false;
				if(carte.closestDiscover(x, y, k)!=null){//Comportement de closest discover lorsque plus de cases à visiter peut être problématique
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
			
			/*if(!dessin.getRobots()[0].busy()){
				Chemin aux=new Chemin();
				aux.get().addAll(current_paths[1].get());
				aux.get().addAll(current_paths[2].get());
				Chemin test = carte.createPath(dessin.getRobots()[0].getX(), dessin.getRobots()[0].getY(), carte.getMark().getX(), carte.getMark().getY(), aux);
				Chemin test2=carte.createPath(dessin.getRobots()[0].getX(), dessin.getRobots()[0].getY(), carte.getMark().getX(), carte.getMark().getY());
				Chemin test3=carte.createPath(dessin.getRobots()[0].getX(), dessin.getRobots()[0].getY(), carte.rand.nextInt(carte.getWidth()), carte.rand.nextInt(carte.getHeight()));
				dessin.getRobots()[0].walkPath(new Chemin(test3));
				Chemin blocked = new Chemin();
				blocked.get().addAll(current_paths[2].get());
				blocked.get().add(test3.get(0));
				dessin.getRobots()[1].walkPath(carte.closestDiscover(dessin.getRobots()[1].getX(), dessin.getRobots()[1].getY(), 1, test3, blocked,false));
				/*if(test2.isCollision(carte, current_paths[1], true))
					;
					//Faire bouger robot 1 de façon à libérer le chemin -> Noter qu'il faut qu'il sorte totalement du chemin prévu.
					//Comparer cette solution à l'évitement en terme de cout
				if(test.getValue()<test2.getValue())
					;
			}*/
			
			int steps=0;
			while(steps<50){
				dessin.step.await();
				steps++;
			}
			temps+=5;
			System.out.println(temps/60+":"+temps%60);
			}finally{
				dessin.lock.unlock();
			}
		}
	}
	
	public void initialisation(){
		this.priority=new int[3];
		for(int k=0;k<3;k++)
			this.priority[k]=k;
		this.step=0;
	}
	
	public DessinCarte dessinCarte(){
		return this.dessin;
	}
	
	public void test(){
		
	}
}
