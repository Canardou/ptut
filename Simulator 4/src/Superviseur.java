
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;

import communication.InitPC;
import communication.Order;
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
	private ArrayList<Chemin> test;
	private int step;
	private int current;
	private Gui application;
	private int simulation;
	private  Chemin [] current_paths_4ever;
	private int currentDir ;
	private boolean ready;
	
	public Superviseur(Carte carte){
		this.current_paths=new Chemin[agent];
		this.current_paths_4ever=new Chemin[agent];
		this.priority=new int[agent];
		this.test=new ArrayList<Chemin>();
		this.next_paths=null;
		this.carte=carte;
		this.dessin=new DessinCarte(carte);
		this.dessin.launch();
		this.currentDir=0;
		this.ready = false;
		
		application = new Gui(this);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.pack();
		application.setVisible(true);
		
		
	}
	
	public void simulation(int seed) throws InterruptedException { 
		
		/*TODO 
		 * cas 234 à voir...
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
		//Les diff�rentes �tapes sont :
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
							if(carte.closestDiscover(x, y, k)!=null){//Comportement de closest discover lorsque plus de cases � visiter peut �tre probl�matique
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
			Dialogue.SuccessDoge("Simulation résolue ! Ouaf !");
		else
			Dialogue.Success("Simulation résolue ! Bilip !");
	}
	
	public void destin() throws InterruptedException {
		
		//Affiche une jolie fenêtre de début 
		if(this.dessin.getDoge())
			Dialogue.Warning("Who let the dogs out ?");
		else
			Dialogue.Warning("Début de la résolution, suivez votre destin !");
		
		//Déclaration de ce dont on a besoin
		this.dessin.lock.lock();
		this.carte.reset();
		this.next_paths=null;
		this.test.clear();
		this.application.reset();
		this.initialisation();
		dessin.showMark(true);
		int temps=0;
		step=0;
		boolean continuer=true;
		this.dessin.lock.unlock();
		InitPC comPCNXT = new InitPC();
		int i;
		Queue<Integer> ordres = new LinkedList<Integer>();
		
		
		//On connecte les 3 robots
		for(i=0;i<3;i++){
			
			try{
				comPCNXT.setThreadComm(this.dessin.getRobot(i));
				System.out.println("robot "+ i +" attente connexion : ");
				while(!(comPCNXT.getThreadComm(i).getConnected())){
				//System.out.println(comPCNXT.getThreadComm(i).getConnected());
				}
				
			}
			catch(Exception e){
				System.out.println(e);
			}
			
		}

		

		//On envoie au 3 robots les ordres d'initialisation.
		((LinkedList<Integer>)ordres).addFirst(Order.SETPOSITION);
		((LinkedList<Integer>)ordres).addFirst(Order.SAVEREFANGLE);
		((LinkedList<Integer>)ordres).addFirst(Order.CHECKFIRSTCASE);
		boolean caseVerifier = false;
		for(i=0;i<3;i++){
			synchronized(comPCNXT.getThreadComm(i)){
			comPCNXT.getThreadComm(i).setOrdres(ordres);}
		}
		System.out.println(ordres.toString());
		ordres.clear();
		caseVerifier = false;
		while(!caseVerifier){
		try{
		synchronized(comPCNXT.getThreadComm(i)){
		while(!comPCNXT.getThreadComm(i).getEnvoye());
		}
		caseVerifier = true;}
		
		catch(Exception e){
			//System.out.println(e);
			caseVerifier = false;
		}
	}
		/*TODO:
		 * 		 
		 * for sur le caseToOrder pour transformer le currentpath en suite d'ordres.
		 * reset du buffer d'entree d'ordres pour la MAJ du current path.
		 * calcul orientation courante du robot.
		 * 		  
		*/
		
		for(i=0;i<3;i++){
			
			//this.dessin.getRobot(i).moveTo(this.dessin.getRobot(i).getX(),this.dessin.getRobot(i).getY(),this.dessin.getRobot(i).getDir());
			//this.dessin.getRobot(i).setVisible(true);
			current_paths[i]=new Chemin(this.carte.getCase(this.dessin.getRobot(i).getX(), this.dessin.getRobot(i).getY()));
			current_paths[i].setValue(i);
			//this.currentDir = this.dessin.getRobot(i).getDir();
		}
		
		
		
		boolean connexion ;
		
		while(continuer){
			this.dessin.lock.lock();
			try{
				int x=0;
				int y=0;
				((LinkedList<Integer>)ordres).addFirst(Order.CASETOSEND);
				for(int numero=0; numero<3; numero++){
					synchronized(comPCNXT.getThreadComm(i)){
					connexion =!(comPCNXT.getThreadComm(i).getConnected());
					System.out.println("Robot "+ i + " : etat connexion = "+connexion);
					}
					if(!connexion){
					try{
						comPCNXT.setThreadComm(this.dessin.getRobot(i));
						
					}

					catch(Exception e){
						System.out.println(e);
					}
					}
					
					else{
					
					try{
					comPCNXT.getThreadComm(i).setOrdres(ordres);}
					catch(Exception e){
						
					}
					try{
						synchronized(comPCNXT.getThreadComm(i)){
					if(comPCNXT.getThreadComm(i).getReception()){
						
						x= comPCNXT.getThreadComm(i).getCaseRecue().getX();
						y= comPCNXT.getThreadComm(i).getCaseRecue().getY();
						current_paths[numero].cut(carte.getCase(x, y));
						if(current_paths[numero].size()<=1){
							current_paths[numero]=new Chemin(carte.getCase(x, y));
							current_paths[numero].setValue(numero);
						}
						if(this.carte.getCase(x, y)!=null){
//Composition needed !
							this.carte.update(x, y, /*->*/comPCNXT.getThreadComm(i).getCaseRecue().getCompo()/*<-*/);//Il faut la composition ici !!
							this.carte.reveal(x, y);
						}
						this.carte.setExit();
					}
					}}
					catch(Exception e){
						
					}
				}
				ordres.clear();
				try{
				if(step<2 && carte.getCase(comPCNXT.getThreadComm(i).getCaseRecue().getX(), comPCNXT.getThreadComm(i).getCaseRecue().getY())==carte.getMark()){
					dessin.getRobot(0).changeType(dessin.getRobot(0).getType()+3);
					dessin.showMark(false);
					step=2;
					next_paths=null;
				}
				Chemin trajet=carte.pathToMark(comPCNXT.getThreadComm(0).getCaseRecue().getX(), comPCNXT.getThreadComm(0).getCaseRecue().getY());
				if(trajet!=null && step==0){
						step=1;
				}
				if(carte.exit()){
					trajet=carte.pathToExit(comPCNXT.getThreadComm(0).getCaseRecue().getX(), comPCNXT.getThreadComm(0).getCaseRecue().getY());
					if(trajet!=null && step==2){
							step=3;
					}
				}
				
				System.out.println("Etape "+step);}
				catch(Exception e){
					
				}
			
				/*
				 * Construction des listes d'ordres pour les robots
				 * TODO: construire current_paths_4ever
				 */
				try{
				for(i=0;i<2;i++){
					
					for(int j=0;j<current_paths_4ever[i].size()-1;j++){
							ordres.addAll(this.caseToOrder(current_paths_4ever[i].get(j), this.currentDir, current_paths_4ever[i].get(j+1)));
							this.currentDir = current_paths_4ever[i].get(j).getDir(current_paths_4ever[i].get(j+1));
							if(j==current_paths_4ever[i].size()-2){
								//On envoie les ordres de déplacement aux 3 robots et on reset la queue d'ordre
								comPCNXT.getThreadComm(i).setOrdres(ordres);
								ordres.clear();
							}
					}
					
					
				}
				}
				catch(Exception e){
					
				}
				
			
				switch(step){
				case 0:
				case 2:
					for(numero=0; numero<3; numero++){
						
						if(comPCNXT.getThreadComm(numero).getQueue().isEmpty()){
							ordres.add(Order.CASETOSEND);
							try{
							comPCNXT.getThreadComm(i).setOrdres(ordres);
							ordres.clear();
							x= comPCNXT.getThreadComm(i).getCaseRecue().getX();
							y= comPCNXT.getThreadComm(i).getCaseRecue().getY();}
							catch(Exception e){
								
							}
							
							int k=1;
							boolean retry=false;
							if(carte.closestDiscover(x, y, k)!=null){//Comportement de closest discover lorsque plus de cases � visiter peut �tre probl�matique
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
							current_paths_4ever[numero]=new Chemin(current_paths[numero]);
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
										current_paths_4ever[test.get(0).getValue()]=new Chemin(test.get(0));
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
										current_paths_4ever[test.get(0).getValue()]=new Chemin(test.get(0));
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
				}
				}finally{
					this.dessin.lock.unlock();
				}
			}
		if(this.dessin.getDoge())
			Dialogue.SuccessDoge("Simulation résolue ! Ouaf !");
		else
			Dialogue.Success("Simulation résolue ! Bilip !");
				
		}

public Queue<Integer> caseToOrder(Case current, int dir, Case next){
		
		Queue<Integer> ordre = new LinkedList<Integer>();
		
		if (current.isRevealed()){
			if(next.getX()!=current.getX() || next.getY() != current.getY()){
				if (next.getX()>current.getX() && next.getY() == current.getY()){
					switch(dir){
						case Case.UP:
							((LinkedList<Integer>)ordre).addFirst(Order.TURNR);
							((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
						break;
						case Case.DOWN:
							((LinkedList<Integer>)ordre).addFirst(Order.TURNL);
							((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
						break;
						case Case.RIGHT:
							((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
						break;
						case Case.LEFT:
							((LinkedList<Integer>)ordre).addFirst(Order.TURNB);
							((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
						break;
					}
				}
					if (next.getX()<current.getX() && next.getY() == current.getY()){
						switch(dir){
							case Case.UP:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNL);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.DOWN:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNR);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.LEFT:
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.RIGHT:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNB);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
						}
					}
				
					if (next.getY()>current.getY() && next.getX() == current.getX()){
						switch(dir){
							case Case.UP:
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.DOWN:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNB);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.LEFT:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNR);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.RIGHT:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNL);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
						}
					}
					if (next.getY()<current.getY() && next.getX() == current.getX()){
						switch(dir){
							case Case.UP:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNB);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.DOWN:
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.LEFT:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNL);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
							case Case.RIGHT:
								((LinkedList<Integer>)ordre).addFirst(Order.TURNR);
								((LinkedList<Integer>)ordre).addFirst(Order.FORWARD);
							break;
						}
					}
				}
				else {((LinkedList<Integer>)ordre).addFirst(Order.STOP);}
			}
			else {((LinkedList<Integer>)ordre).addFirst(Order.STOP);}		
		return ordre;	
	}
	
	
	public void initialisation(){
		//Red�fini l'�tat comme initial
		for(int k=0;k<this.priority.length;k++)
			this.priority[k]=k;
		this.step=0;
		//Enl�ve la balle au robot
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
			Chemin temp2=this.carte.closestDiscover(cur2.get(cur2.size()-1), 1, avoid,  new Chemin(cur1.get(cur1.size()-1)), exit);
			if(temp2!=null){
				temp2.setValue(cur2.getValue());
				System.out.println("---Recure 3 - 1");
				if(temp2.size()>1){
					solution = this.resolution(liste, cur2, cur3, temp, temp2.get(temp2.size()-1), obj3, obj1, exit);
				}
			}
			Chemin temp3;
			if(solution!=null){
				avoid = new Chemin();
				avoid.add(temp2);
				avoid.add(princ);
				temp3=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid,  new Chemin(cur1.get(cur1.size()-1)), exit);
				if(temp3!=null){
					temp3.setValue(cur3.getValue());
					System.out.println("---Recure 3 - 21");
					if(temp3.size()>1){
						solution = this.resolution(solution, cur3, temp, temp2, temp3.get(temp3.size()-1), obj1, temp2.get(temp2.size()-1), exit);
					}
				}
			}
			else{
				avoid = new Chemin();
				avoid.add(cur2);
				avoid.add(princ);
				temp3=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid,  new Chemin(cur1.get(cur1.size()-1)), exit);
				if(temp3!=null){
					temp3.setValue(cur3.getValue());
					System.out.println("---Recure 3 - 22");
					if(temp3.size()>1){
						solution = this.resolution(liste, cur3, temp, cur2, temp3.get(temp3.size()-1), obj1, cur2.get(cur2.size()-1), exit);
					}
				}
			}
			System.out.println("---Recure 3 - 3");
			if(solution!=null){
				ListeChemin aux = solution;
				if(aux.current()!=null){
					while(aux.previous()!=null && aux.current().getValue()!=cur1.getValue()){
						aux=aux.previous();
					}
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
		return liste;
	}
}
