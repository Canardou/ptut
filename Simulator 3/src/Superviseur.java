
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

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
	private ListeChemin next_paths;
	private int step;
	private int current;
	private Gui application;
	private int simulation;
	
	public Superviseur(Carte carte){
		this.current_paths=new Chemin[agent];
		this.priority=new int[agent];
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
		this.carte.setExit();
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
		
		
		
		
		//Dans une simulation dessin est utilisï¿½ comme rï¿½fï¿½rent pour le superviseur
		//Dans un cas rï¿½el le superviseur devra ï¿½tre cadencï¿½ par les appels blutooth
		
		//int numero=labyrinth.rand.nextInt(3);
		int temps=0;
		while(true){
			this.dessin.lock.lock();
			try{
				/*
				//Choix du robot et import des informations
				if(!dessin.getRobots()[numero].busy()){
					//Rï¿½solution
					int x= dessin.getRobots()[numero].getX();
					int y= dessin.getRobots()[numero].getY();
					this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
					this.carte.reveal(x, y);
					
					
					//DEBUT SUPERVISEUR
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
				
				if(temps==0){
					next_paths=this.resolution(null, current_paths[0], current_paths[1], current_paths[2], this.carte.getMark(), 1, true);
					//next_paths=this.resolution(null, current_paths[2], current_paths[0], current_paths[1], this.carte.getCase(2, 3), true);
				}
				else{
					if(next_paths!=null){
						if(next_paths.current()!=null){
							if(!dessin.getRobot(next_paths.current().getValue()).busy()){
								dessin.getRobot(next_paths.current().getValue()).walkPath(next_paths.current());
								next_paths=next_paths.previous();
							}
						}
						else
							next_paths=null;
					}
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
	
	public ListeChemin resolution(ListeChemin liste, Chemin princ, Chemin cur2, Chemin cur3, Case obj, int k, boolean exit){
		ListeChemin chemins=null;
		Chemin block = new Chemin();
		block.add(cur2);
		block.add(cur3);
		Chemin pour=new Chemin();
		for(Case aux : block.get()){
			pour.add(aux);
			for(int i=0;i<4;i++){
				Case add=carte.getCase(aux.getX(i),aux.getY(i));
				if(add!=null){
					if(aux.isCrossable(aux.getDir(add)) && add.isCrossable(add.getDir(aux))){
							pour.add(add);
					}
				}
			}
		}
		int cout=-1;
		if(liste==null)
			liste = new ListeChemin(null,0,null);
		System.out.println("v-Recure");
		k=k+1;
		//Si chemin direct, fin du recurage
		boolean collision=false;
		Chemin temp = this.carte.createPath(princ.get(princ.size()-1), obj, null);
		if(temp!=null){
			cout=temp.getValue();
			temp.setValue(princ.getValue());
			chemins=new ListeChemin(temp,cout+liste.getCout(),null);
			if(!temp.isCollision(carte, pour, true)){
				System.out.println("^-Extrema");
				chemins.setCout(Integer.MAX_VALUE);
			}
			else
				collision=true;
		}
		
		//Sinon, on applique la fonction à un chemin d'esquive pour les deux autres robots
		if(collision){
			ArrayList<ListeChemin> comparer = new ArrayList<ListeChemin>();
			Chemin temp2 = this.carte.createPath(princ.get(princ.size()-1), obj, pour);
			if(temp2!=null){
				cout=temp2.getValue();
				temp2.setValue(princ.getValue());
				if(temp2!=null){
					System.out.println(">-Extrema 2");
					comparer.add(new ListeChemin(temp2,cout+liste.getCout(),null));
				}
			}
			Chemin avoid = new Chemin();
			avoid.add(cur3);
			avoid.add(temp);
			Chemin temp3=this.carte.closestDiscover(cur2.get(cur2.size()-1), k/20+1, avoid, null, exit);
			avoid = new Chemin();
			avoid.add(cur2);
			avoid.add(temp);
			Chemin temp4=this.carte.closestDiscover(cur3.get(cur3.size()-1), k/20+1, avoid, null, exit);
			int cout3=0;
			int cout4=0;
			if(temp3!=null){
				cout3=temp3.getValue();
				temp3.setValue(cur2.getValue());
			}
			if(temp4!=null){
				cout4=temp4.getValue();
				temp4.setValue(cur3.getValue());
			}
			if(temp4!=null){
				if(temp.isCollision(this.carte, cur3, true)){
					System.out.println(">-Continuer aux1");
					if(temp4.size()>1 && k<=30){
						comparer.add(this.resolution(chemins,cur3,cur2,princ,temp4.get(temp4.size()-1),k+1,exit));
					}
				}
			}
			if(temp3!=null){
				if(temp.isCollision(this.carte, cur2, true)){
					System.out.println(">-Continuer aux2");
					if(temp3.size()>1 && k<=30){
						comparer.add(this.resolution(chemins,cur2,cur3,princ,temp3.get(temp3.size()-1),k+1,exit));
					}
				}
			}
			if(comparer.size()>0){
				System.out.println("^-Fin");
				Collections.sort(comparer);
				ListeChemin foo = comparer.get(0);
				while(foo.previous()!=null){
					System.out.println("--Foo");
					foo=foo.previous();
				}
				foo.setPrevious(chemins);
				chemins = comparer.get(0);
				
			}
		}
		if(k<=30)
			return chemins;
		else
			return new ListeChemin(null,Integer.MAX_VALUE,null);
	}
	
}
