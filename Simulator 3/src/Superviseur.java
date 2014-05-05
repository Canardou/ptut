
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
	
	ArrayList<Chemin> test = new ArrayList<Chemin>();
	
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
					next_paths=this.resolution(null, current_paths[0], current_paths[1], current_paths[2], this.carte.getMark(), null, null, true);
					while(next_paths.previous()!=null){
						test.add(0,next_paths.current());
						next_paths=next_paths.previous();
					}
				}
				else{
					if(!test.isEmpty()){
						if(!dessin.getRobot(test.get(0).getValue()).busy()){
							dessin.getRobot(test.get(0).getValue()).walkPath(test.get(0));
							System.out.println(test.get(0));
							test.remove(0);
						}
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
	
	public ListeChemin resolution(ListeChemin liste, Chemin cur1, Chemin cur2, Chemin cur3, Case obj1, Case obj2, Case obj3, boolean exit){
		if(liste==null)
			liste = new ListeChemin(null,0,null);
		Chemin block = new Chemin();
		block.add(cur2.get(cur2.size()-1));
		block.add(cur3.get(cur3.size()-1));
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
		boolean collision=false;
		Chemin temp = this.carte.createPath(cur1.get(cur1.size()-1), obj1, null);
		Chemin princ = new Chemin(temp);
		princ.setValue(cur1.getValue());
		int	cout=0;
		System.out.println("-Start");
		if(temp!=null){
			if(temp.size()>1){
				cout=temp.getValue();
				temp.setValue(cur1.getValue());
				if(temp.isCollision(carte, block, true)){
					collision=true;
					temp.beforeBlock(carte, block, true);
				}
				else{
					System.out.println("--Extrema CHEMINS");
					return new ListeChemin(temp,liste.getCout()+cout,liste);
				}
			}
			else{
				System.out.println("--YOLO");
				return liste;
			}
		}
		else
			return new ListeChemin(null,Integer.MAX_VALUE,null);
		if(collision){
			ArrayList<ListeChemin> comparer = new ArrayList<ListeChemin>();
			Chemin temp1 = this.carte.createPath(cur1.get(cur1.size()-1), obj1, pour);
			if(temp1!=null){
				if(temp1.size()>1){
					int cout1=temp1.getValue();
					temp1.setValue(cur1.getValue());
					if(temp1!=null){
						System.out.println("---Extrema 2");
						comparer.add(new ListeChemin(temp1,cout1+liste.getCout(),liste));
					}
				}
			}
			ListeChemin solution=null;
			Chemin avoid = new Chemin();
			avoid.add(cur3);
			avoid.add(princ);
			Chemin temp2=this.carte.closestDiscover(cur2.get(cur2.size()-1), 1, avoid, null, exit);
			temp2.setValue(cur2.getValue());
			System.out.println("---Recure 3 - 1");
			if(temp2.size()>1){
				solution = this.resolution(liste, cur2, cur3, temp, temp2.get(temp2.size()-1), obj3, obj1, exit);
			}
			Chemin temp3;
			if(solution!=null){
				avoid = new Chemin();
				avoid.add(temp2);
				avoid.add(cur2);
				avoid.add(princ);
				temp3=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid, null, exit);
				temp3.setValue(cur3.getValue());
				System.out.println("---Recure 3 - 21");
				if(temp3.size()>1){
					solution = this.resolution(solution, cur3, temp2, temp, temp3.get(temp3.size()-1), temp2.get(temp2.size()-1), obj1, exit);
				}
			}
			else{
				avoid = new Chemin();
				avoid.add(cur2);
				avoid.add(princ);
				temp3=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid, null, exit);
				temp3.setValue(cur3.getValue());
				System.out.println("---Recure 3 - 22");
				if(temp3.size()>1){
					solution = this.resolution(liste, cur3, cur2, temp, temp3.get(temp3.size()-1), cur2.get(cur2.size()-1), obj1, exit);
				}
			}
				
			System.out.println("---Recure 3 - 3");
			if(solution!=null){
				ListeChemin aux = solution;
				while(aux.previous()!=null && aux.current().getValue()!=cur1.getValue()){
					aux=aux.previous();
				}
				if(aux.current()!=null)
					solution = this.resolution(solution, aux.current(), temp2, temp3, temp.get(0), temp2.get(temp2.size()-1), temp3.get(temp3.size()-1), exit);
				
				solution = new ListeChemin(temp, cout+solution.getCout(),solution);
				solution = this.resolution(solution, temp, temp2, temp3, obj1, temp2.get(temp2.size()-1), temp3.get(temp3.size()-1), exit);
			}
			if(solution!=null)
				comparer.add(solution);
			if(comparer.size()>0){
				System.out.println("-Fin");
				Collections.sort(comparer);
				return comparer.get(0);
			}
		}
		return new ListeChemin(null,Integer.MAX_VALUE,null);
	}
	
	/*public ListeChemin resolution(ListeChemin liste, Chemin princ, Chemin cur2, Chemin cur3, Case obj, boolean exit){
		ListeChemin chemins=null;
		//Block/Pour chemin bloqués et/ou à éviter
		Chemin block = new Chemin();
		block.add(cur2.get(cur2.size()-1));
		block.add(cur3.get(cur3.size()-1));
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
		//On initialise la recursivité
		if(liste==null)
			liste = new ListeChemin(null,0,null);
		//Le cout
		int cout=0;
		System.out.println("v-Recure");
		
		//Si chemin direct, fin du recurage
		boolean collision=false;
		Chemin temp = this.carte.createPath(princ.get(princ.size()-1), obj, null);
		if(temp!=null){
			if(temp.size()>1){
				cout=temp.getValue();
				temp.setValue(princ.getValue());
				chemins=new ListeChemin(temp,cout+liste.getCout(),liste);
				System.out.println("^-Extrema");
				System.out.println(temp);
				if(temp.isCollision(carte, pour, true)){
					collision=true;
				}
			}
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
					System.out.println(temp2);
					comparer.add(new ListeChemin(temp2,cout+liste.getCout(),liste));
				}
			}
			ListeChemin solution=null;
			Chemin avoid = new Chemin();
			avoid.add(cur3);
			avoid.add(temp);
			Chemin temp4;
			Chemin temp3=this.carte.closestDiscover(cur2.get(cur2.size()-1), 1, avoid, null, exit);
			int cout3=0;
			int cout4=0;
			if(temp3!=null){
				if(temp3.size()>1){
					avoid = new Chemin();
					avoid.add(temp3);
					avoid.add(temp);
					temp4=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid, null, exit);
					cout3=temp3.getValue();
					temp3.setValue(cur2.getValue());
					System.out.println(">--Aux 1");
					System.out.println(temp3);
					if(temp4!=null){
						if(temp4.size()>1){
							cout4=temp4.getValue();
							temp4.setValue(cur3.getValue());
							System.out.println(">--Aux 1 - 2");
							System.out.println(temp4);
							solution = new ListeChemin(temp4,temp4.getValue()+chemins.current().getValue(),chemins);
							solution = this.resolution(solution,cur2,temp4,princ,temp3.get(temp3.size()-1),exit);
						}
					}
					if(solution==null){
						solution = new ListeChemin(temp3,temp3.getValue()+chemins.getCout(),chemins);
						solution = this.resolution(solution,cur2,cur3,temp,temp3.get(temp3.size()-1),exit);
					}
				}
				if(solution!=null){
					comparer.add(solution);
				}
			}
			else{
				avoid = new Chemin();
				avoid.add(cur2);
				avoid.add(temp);
				temp4=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid, cur2, exit);
				if(temp4!=null){
					if(temp4.size()>1){
						cout4=temp4.getValue();
						temp4.setValue(cur3.getValue());
						System.out.println(">--Aux 2");
						System.out.println(temp4);
						solution = new ListeChemin(temp4,temp4.getValue()+chemins.getCout(),chemins);
						comparer.add(this.resolution(solution,cur3,cur2,temp,temp4.get(temp4.size()-1),exit));
					}
				}
			}
			
			
			if(comparer.size()>0){
				System.out.println("^-Fin");
				Collections.sort(comparer);
				ListeChemin foo = comparer.get(0);
				/*while(foo.previous()!=null){
					System.out.println("--Foo");
					foo=foo.previous();
				}
				foo.setPrevious(liste);/
				chemins = comparer.get(0);
			}
		}
		else
			chemins = new ListeChemin(null, Integer.MAX_VALUE, null);
		return chemins;
	}*/
	
}
