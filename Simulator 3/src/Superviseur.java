
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
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

		/*TODO 
		 * cas 234 Ã  voir...
		 */
		Dialogue.Warning("Lancement d'une simulation");
		this.dessin.lock.lock();
		
		this.carte.rand.setSeed(seed);
		Carte labyrinth=new Carte(this.carte.getWidth(),this.carte.getHeight());
		labyrinth.rand.setSeed(carte.rand.nextInt());
		labyrinth.randomMaze(0);
		this.carte.reset();
		this.next_paths=null;
		this.test.clear();
		this.application.reset();

		for(int i=0;i<3;i++){
			this.dessin.getRobot(i).moveTo(carte.rand.nextInt(this.carte.getWidth()),this.carte.rand.nextInt(this.carte.getHeight()),this.carte.rand.nextInt(4));
			this.dessin.getRobot(i).setVisible(true);
			current_paths[i]=new Chemin(this.carte.getCase(this.dessin.getRobot(i).getX(), this.dessin.getRobot(i).getY()));
			current_paths[i].setValue(i);
		}
		this.initialisation();
		dessin.showMark(true);
		int temps=0;
		step=0;
		//Les diffï¿½rentes ï¿½tapes sont :
		//0 - Exploration
		//1 - Aller chercher la balle, quand un trajet robot 0 - balle existe
		//2 - Sortir, quand un trajet robot 0 - sortie existe
		boolean continuer=true;
		this.dessin.lock.unlock();
		while(continuer){
			this.dessin.lock.lock();
			try{
				int x=0;
				int y=0;
				for(int numero=0; numero<3; numero++){
					x= dessin.getRobot(numero).getX();
					y= dessin.getRobot(numero).getY();
					current_paths[numero].cut(carte.getCase(x, y));
					if(current_paths[numero].size()<=1){
						current_paths[numero]=new Chemin(carte.getCase(x, y));
						current_paths[numero].setValue(numero);
					}
					if(this.carte.getCase(x, y)!=null){
						this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
						this.carte.reveal(x, y);
					}
					this.carte.setExit();
				}
				if(step<2 && carte.getCase(dessin.getRobot(0).getX(), dessin.getRobot(0).getY())==carte.getMark()){
					dessin.getRobot(0).changeType(dessin.getRobot(0).getType()+3);
					dessin.showMark(false);
					step=2;
					next_paths=null;
				}
				Chemin trajet=carte.pathToMark(dessin.getRobot(0).getX(), dessin.getRobot(0).getY());
				if(trajet!=null && step==0){
						step=1;
				}
				if(carte.exit()){
					trajet=carte.pathToExit(dessin.getRobot(0).getX(), dessin.getRobot(0).getY());
					if(trajet!=null && step==2){
							step=3;
					}
				}
				
				System.out.println("Etape "+step);
				
				
				
				
				switch(step){
				case 0:
				case 2:
					for(int numero=0; numero<3; numero++){
						if(!dessin.getRobot(numero).busy()){
							x= dessin.getRobot(numero).getX();
							y= dessin.getRobot(numero).getY();
							int k=1;
							boolean retry=false;
							if(carte.closestDiscover(x, y, k)!=null){//Comportement de closest discover lorsque plus de cases ï¿½ visiter peut ï¿½tre problï¿½matique
								do{
									retry=false;
									if(carte.closestDiscover(x, y, k)!=null){
										current_paths[numero]=carte.closestDiscover(x, y, k);
									}
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
									Chemin aux =carte.createPath(x, y, carte.rand.nextInt(this.carte.getWidth()),carte.rand.nextInt(this.carte.getHeight()));
									
									if(aux!=null){
										current_paths[numero]=aux;
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
								}
							current_paths[numero].setValue(numero);
							dessin.getRobot(numero).walkPath(new Chemin(current_paths[numero]));
							}
						}
					}
					break;
				case 1:
					if(next_paths==null){
						if(!dessin.getRobot(0).busy() && !dessin.getRobot(1).busy() && !dessin.getRobot(2).busy()){
							next_paths=this.resolution(null, current_paths[0], current_paths[1], current_paths[2], this.carte.getMark(), null, null, true);
							while(next_paths.previous()!=null){
								test.add(0,next_paths.current());
								next_paths=next_paths.previous();
							}
						}
					}
					else{
						if(!test.isEmpty()){
							if(!dessin.getRobot(0).busy() && !dessin.getRobot(1).busy() && !dessin.getRobot(2).busy()){
								if(test.get(0)!=null){
									if(test.get(0).get(0)==carte.getCase(dessin.getRobot(test.get(0).getValue()).getX(), dessin.getRobot(test.get(0).getValue()).getY())){
										dessin.getRobot(test.get(0).getValue()).walkPath(test.get(0));
										current_paths[test.get(0).getValue()]=test.get(0);
										this.application.putLog(test.get(0).getValue(),"Woof !");
										System.out.println(test.get(0));
									}
									test.remove(0);
								}
							}
						}
						else
							next_paths=null;
					}
					break;
				case 3:
					if(next_paths==null && this.carte.getCase(dessin.getRobot(0).getX(),dessin.getRobot(0).getY())!=null){
						if(!dessin.getRobot(0).busy() && !dessin.getRobot(1).busy() && !dessin.getRobot(2).busy()){
							next_paths=this.resolution(null, current_paths[0], current_paths[1], current_paths[2], this.carte.setExit(), null, null, true);
							while(next_paths.current()!=null){
								test.add(0,next_paths.current());
								next_paths=next_paths.previous();
							}
						}
					}
					else{
						if(!test.isEmpty()){
							if(!dessin.getRobot(0).busy() && !dessin.getRobot(1).busy() && !dessin.getRobot(2).busy()){
								if(test.get(0)!=null){
									if(test.get(0).get(0)==carte.getCase(dessin.getRobot(test.get(0).getValue()).getX(), dessin.getRobot(test.get(0).getValue()).getY())){
										dessin.getRobot(test.get(0).getValue()).walkPath(test.get(0));
										current_paths[test.get(0).getValue()]=test.get(0);
										System.out.println(test.get(0));
									}
								}
								test.remove(0);
							}
						}
						else if(this.carte.getCase(dessin.getRobot(0).getX(),dessin.getRobot(0).getY())==null)
							continuer=false;
						else
							next_paths=null;
					}
					break;
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
		if(this.dessin.getDoge())
			Dialogue.SuccessDoge("Simulation rÃ©solue ! Ouaf !");
		else
			Dialogue.Success("Simulation rÃ©solue ! Bilip !");
	}
	
	public void destin() throws InterruptedException {
		while(true){   //tant que marque pas trouvÃ©e
			Thread.sleep(1);
		}
	}
	
	public void initialisation(){
		//Redï¿½fini l'ï¿½tat comme initial
		for(int k=0;k<this.priority.length;k++)
			this.priority[k]=k;
		this.step=0;
		//Enlï¿½ve la balle au robot
		if(dessin.getRobot(0).getType()==3 || dessin.getRobot(0).getType()==7)
			dessin.getRobot(0).changeType(dessin.getRobot(0).getType()-3);
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
				avoid.add(princ);
				temp3=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid, null, exit);
				temp3.setValue(cur3.getValue());
				System.out.println("---Recure 3 - 21");
				ListeChemin aux = solution;
				solution = new ListeChemin(temp2, solution.getCout(),solution);
				if(temp3.size()>1){
					solution = this.resolution(solution, cur3, temp, temp2, temp3.get(temp3.size()-1), obj1, temp2.get(temp2.size()-1), exit);
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
					solution = this.resolution(liste, cur3, temp, cur2, temp3.get(temp3.size()-1), obj1, cur2.get(cur2.size()-1), exit);
				}
			}
			System.out.println("---Recure 3 - 3");
			if(solution!=null){
				solution = new ListeChemin(temp, cout+solution.getCout(),solution);
				solution = this.resolution(solution, temp, temp2, temp3, obj1, temp2.get(temp2.size()-1), temp3.get(temp3.size()-1), exit);
				ListeChemin aux = solution;
				while(aux.previous()!=null && aux.current().getValue()!=cur1.getValue()){
					aux=aux.previous();
				}
				if(aux.current()!=null)
					solution = this.resolution(solution, aux.current(), temp2, temp3, temp.get(0), temp2.get(temp2.size()-1), temp3.get(temp3.size()-1), exit);
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
