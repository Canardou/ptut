
import java.awt.Point;
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
	private ListeChemin next_paths;
	private ArrayList<Chemin> test;
	private int step;
	private Gui application;
	private Chemin [] current_paths_4ever;
	private int [] currentDir ;
	@SuppressWarnings("unused")
	private boolean ready;
	private boolean [] tabIsBusy ;
	
	private int number;
	private boolean continuer;
	
	private Point [] positions;
	
	public Superviseur(Carte carte){
		this.current_paths=new Chemin[agent];
		this.current_paths_4ever=new Chemin[agent];
		this.test=new ArrayList<Chemin>();
		this.next_paths=null;
		this.carte=carte;
		this.dessin=new DessinCarte(carte);
		this.dessin.launch();
		this.currentDir=new int[3];
		application = new Gui(this);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.pack();
		application.setVisible(true);
		this.tabIsBusy = new boolean[3];
		
		
	}
	
	public void simulation(int seed) throws InterruptedException {
		Dialogue.Warning("Lancement d'une simulation");
		
		this.dessin.lock.lock();
		this.initialisation(seed);
		
		//<Simulation-
		Carte labyrinth=new Carte(this.carte.getWidth(),this.carte.getHeight());
		labyrinth.rand.setSeed(carte.rand.nextInt());
		labyrinth.randomMaze(0.15);
		for(int i=0;i<3;i++){
			this.dessin.getRobot(i).moveTo(carte.rand.nextInt(this.carte.getWidth()),this.carte.rand.nextInt(this.carte.getHeight()),this.carte.rand.nextInt(4));
			this.dessin.getRobot(i).setVisible(true);
			current_paths[i]=new Chemin(this.carte.getCase(this.dessin.getRobot(i).getX(), this.dessin.getRobot(i).getY()));
			current_paths[i].setValue(i);
		}
		//-Simulation>
		
		//A virer
		this.test.clear();
		
		continuer=true;
		this.dessin.lock.unlock();
		while(continuer){
			this.dessin.lock.lock();
			try{
				for(int numero=0; numero<3; numero++){
					positions[numero].setLocation(dessin.getRobot(numero).getX(), dessin.getRobot(numero).getY());
					int x=(int)positions[numero].getX();
					int y=(int)positions[numero].getY();
					this.tabIsBusy[numero]=this.dessin.getRobot(numero).busy();
					if(this.carte.getCase(x, y)!=null){
						if(!this.carte.getCase(x, y).isRevealed())
						this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
						this.carte.reveal(x, y);
					}
				}
				
				this.cooperation();
				
				this.application.updatePanel();
				
				//<Simulation-
				int steps=0;
				while(steps<2){
					this.dessin.step.await();
					steps++;
				}
				//-Simulation>
				
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
		this.initialisation();
		this.dessin.lock.unlock();
		//A virer
		this.test.clear();

		InitPC comPCNXT = new InitPC();
		
		Queue<Integer> ordres = new LinkedList<Integer>();
		boolean caseVerifier = false;


		// -------------------------------------- INITIALISATION DES ROBOTS -----------------------
		
		this.ready = false;
		for(int i=0;i<3;i++){
			try{
				// On demarre la tache de com du robot
				this.dessin.lock.lock();
				comPCNXT.setThreadComm(this.dessin.getRobot(i));
				

				// On attend que le robot soit connect�
				while(!(comPCNXT.getThreadComm(i).getConnected())){}
				this.dessin.lock.unlock();
			}
			catch(Exception e){}			
		}

		// Ajout des ordres d'initialisation dans le buffer
		((LinkedList<Integer>)ordres).addFirst(Order.SETPOSITION);
		((LinkedList<Integer>)ordres).addFirst(Order.SAVEREFANGLE);
		((LinkedList<Integer>)ordres).addFirst(Order.CHECKFIRSTCASE);

		// Envoie des ordres d'initialisation aux taches de com des robots
		for(int i=0;i<3;i++){
			comPCNXT.getThreadComm(i).setOrdres(ordres);
		}

		System.out.println("Superviseur : Initialisations envoyees");

		// On vide le buffer d'ordre
		ordres.clear();

		// On attend la fin de l'initialisation des robots, c'est a dire que
		// tout les robots aient renvoy� la case correspondant a la case
		// (initiale)
		caseVerifier = false;		
		for (int i = 0; i < 3; i++) {
			while (!caseVerifier) {
				try {
					while (!comPCNXT.getThreadComm(i).getEnvoye()) {}
					System.out.println("Superviseur : info init terminee pour le robot "+i);
					caseVerifier = true;
				}
				catch (Exception e) {}
			}
			caseVerifier=false;
		}
		System.out.println("Superviseur : Initialisation des robots terminee");
		
		this.dessin.lock.lock();
		for(int i=0;i<3;i++){
			current_paths[i]=new Chemin(this.carte.getCase(this.dessin.getRobot(i).getX(), this.dessin.getRobot(i).getY()));
			current_paths[i].setValue(i);
			positions[i].setLocation(dessin.getRobot(i).getX(), dessin.getRobot(i).getY());
			this.currentDir[i]=this.dessin.getRobot(i).getDir();
			
		}
		this.dessin.lock.unlock();
		// -------------------------------------- DEBUT DE L'EXPLORATION --------------------------
		continuer=true;
		
		boolean laPremiereFois=true;
		while(continuer){
			boolean enMouvementGeneral = false;
			for(int numero=0; numero<3; numero++){
				
				int x=(int)positions[numero].getX();
				int y=(int)positions[numero].getY();
				
				
				
				if(!comPCNXT.getThreadComm(numero).getEnMouvement() && (comPCNXT.getThreadComm(numero).getCaseRecue().getX()!=x  || comPCNXT.getThreadComm(numero).getCaseRecue().getY()!=y)){
					enMouvementGeneral = true;
					positions[numero].setLocation(comPCNXT.getThreadComm(numero).getCaseRecue().getX(), comPCNXT.getThreadComm(numero).getCaseRecue().getY());
				System.out.println(positions[numero]);
				x=(int)positions[numero].getX();
				y=(int)positions[numero].getY();
				
				//this.dessin.getRobot(numero).moveTo(x, y);
				}
				if(this.carte.getCase(x, y)!=null){
					if(!this.carte.getCase(x, y).isRevealed())
						this.carte.update(x, y, comPCNXT.getThreadComm(numero).getCaseRecue().getCompo());
					this.carte.reveal(x, y);
				}
				
			}
			this.MAJTabIsBusy(comPCNXT);
			this.dessin.lock.lock();
			try{
				System.out.println("Avant coop");
				this.cooperation();
				System.out.println("Apres coop");
				this.dessin.step.await();
			}finally{
				this.dessin.lock.unlock();
			}
			
			for(int i=0;i<3;i++){
				ordres.clear();
				//...pour chaque case du chemin qu'il doit parcourir...
				if(current_paths_4ever[i]!=null) {
					if(!comPCNXT.getThreadComm(i).getEnMouvement() && comPCNXT.getThreadComm(i).getCaseRecue().getX()==current_paths_4ever[i].getX(0) && comPCNXT.getThreadComm(i).getCaseRecue().getY()==current_paths_4ever[i].getY(0) && comPCNXT.getThreadComm(i).getQueue().isEmpty()){
						for(int j=0;j<current_paths_4ever[i].size()-1;j++){
							//...on convertit le chemin en ordres
							((LinkedList<Integer>)ordres).addAll(0,this.caseToOrder(current_paths_4ever[i].get(j), this.currentDir[i], current_paths_4ever[i].get(j+1)));
							this.currentDir[i] = current_paths_4ever[i].get(j).getDir(current_paths_4ever[i].get(j+1));
							if(j==current_paths_4ever[i].size()-2){
	
								//On envoie les ordres de déplacement aux 3 robots et on reset la queue d'ordre
								System.out.println("Robot "+ i +" path: "+current_paths_4ever[i]);
								System.out.println("Robot "+ i +" suite ordres: "+ordres.toString());
								comPCNXT.getThreadComm(i).setOrdres(ordres);
								current_paths_4ever[i].setValue(-1);
								ordres.clear();
							}
						}
					}
				}
			}
			this.application.updatePanel();
			laPremiereFois=false;
		}
		ordres.clear();
		((LinkedList<Integer>)ordres).addFirst(12);

		// Envoie des ordres d'initialisation aux taches de com des robots
		for(int i=0;i<3;i++){
			comPCNXT.getThreadComm(i).setOrdres(ordres);
		}
		
	if(this.dessin.getDoge())
		Dialogue.SuccessDoge("Such success ! Ouaf !");
	else
		Dialogue.Success("Mission accomplie ô maître ! Bilip !");
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
				
					if (next.getY()<current.getY() && next.getX() == current.getX()){
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
					if (next.getY()>current.getY() && next.getX() == current.getX()){
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
		this.initialisation(100);
	}

	public void initialisation(int seed){
		this.carte.rand.setSeed(seed);
		//Remet la carte � 0
		this.carte.reset();
		this.next_paths=null;
		this.application.reset();
		//Red�fini l'�tat comme initial
		this.step=0;
		//Enl�ve la balle au robot
		if(dessin.getRobot(0).getType()==3 || dessin.getRobot(0).getType()==7)
			dessin.getRobot(0).changeType(dessin.getRobot(0).getType()-3);
		this.dessin.showMark(true);
		this.step=0;
		this.positions=new Point[3];
		for(int i=0;i<3;i++)
			positions[i]=new Point();
		this.number=0;
	}
	
	public void cooperation() {
		if(!(this.tabIsBusy[0] && this.tabIsBusy[1] && this.tabIsBusy[2])){
			System.out.print("0");
			number=0;
			Integer x [] = new Integer[3];
			Integer y [] = new Integer[3];
			for(int numero=0; numero<3; numero++){
				x[numero] =(int)positions[numero].getX();
				y[numero]= (int)positions[numero].getY();
				current_paths[numero].cut(carte.getCase(x[numero], y[numero]));
				if(current_paths[numero].size()<=1){
					if(step==1)
						number++;
					if(step==3)
						number++;
					current_paths[numero]=new Chemin(carte.getCase(x[numero], y[numero]));
					current_paths[numero].setValue(numero);
				}
			}
			this.carte.setExit();
			if(step<2 && carte.getCase(x[0], y[0])==carte.getMark()){
				dessin.getRobot(0).changeType(dessin.getRobot(0).getType()+3);
				dessin.showMark(false);
				step=2;
				next_paths=null;
			}
			Chemin trajet=carte.pathToMark(x[0], y[0]);
			if(trajet!=null && step==0){
				step=1;
			}
			if(carte.exit()){
				trajet=carte.pathToExit(x[0], y[0]);
				if(trajet!=null && step==2){
					step=3;
				}
			}
			switch(step){
			case 4:
			case 5:
			case 0:
			case 2:
				for(int numero=0; numero<3; numero++){
					if(!this.tabIsBusy[numero]){
						if(!carte.isWholeRevealed()){
							int k=1;
							boolean retry=false;
							Chemin temp=carte.closestDiscover(x[numero], y[numero], k);
							if(carte.closestDiscover(x[numero], y[numero], k)!=null){//Comportement de closest discover lorsque plus de cases � visiter peut �tre probl�matique
								do{
									retry=false;
									if(carte.closestDiscover(x[numero], y[numero], k)!=null){
										temp=carte.closestDiscover(x[numero], y[numero], k);
									}
									//Envois infos
									for(int j=0;j<3;j++){
										if(j!=numero){
											if(temp.collision(carte, current_paths[j],true)<Math.max(current_paths[numero].size(),2)){
												retry=true;
											}
											temp.beforeBlock(carte, current_paths[j],true);
										}
									}
									k++;
								}while(retry && k<5);
								if(retry=true && k==5){
									Chemin aux =carte.createPath(x[numero], y[numero], carte.rand.nextInt(this.carte.getWidth()),carte.rand.nextInt(this.carte.getHeight()));

									if(aux!=null){
										temp=aux;
										temp.stopToVisibility();
										for(int j=0;j<3;j++){
											if(j!=numero){
												if(temp.collision(carte, current_paths[j],true)<current_paths[numero].size()){
													retry=true;
												}
												temp.beforeBlock(carte, current_paths[j],true);
											}
										}
									}
								}
								temp.setValue(numero);
								if(temp.getX(0)==x[numero] && temp.getY(0)==y[numero] && temp.size()>1){
									//this.dessin.getRobot(numero).moveTo(x[numero], y[numero]);
									this.dessin.getRobot(numero).walkPath(new Chemin(temp));
									current_paths[numero]=temp;
									current_paths[numero].setValue(numero);
									current_paths_4ever[numero]=new Chemin(temp);
									this.application.putLog(numero,"Exploration");
								}
							}
						}
						else{
							Chemin temp =carte.createPath(x[numero], y[numero], carte.rand.nextInt(this.carte.getWidth()),carte.rand.nextInt(this.carte.getHeight()));
							if(temp!=null){
								temp.stopToVisibility();
								for(int j=0;j<3;j++){
									if(j!=numero){
										temp.beforeBlock(carte, current_paths[j],true);
									}
								}
								temp.setValue(numero);
								if(temp.getX(0)==x[numero] && temp.getY(0)==y[numero] && temp.size()>1){
									//this.dessin.getRobot(numero).moveTo(x[numero], y[numero]);
									this.dessin.getRobot(numero).walkPath(new Chemin(temp));
									current_paths[numero]=temp;
									current_paths[numero].setValue(numero);
									current_paths_4ever[numero]=new Chemin(temp);
									this.application.putLog(numero,"Debloquage");
								}
							}
						}
					}
				}
				if(step==4)
					step=1;
				if(step==5)
					step=3;
				break;
			case 1:
				if(next_paths==null){
					if(!this.tabIsBusy[0] && !this.tabIsBusy[1] && !this.tabIsBusy[2]){
						next_paths=this.resolution(0,null, current_paths[0], current_paths[1], current_paths[2], this.carte.getMark(), null, null, false);
						while(next_paths.previous()!=null){
							test.add(0,next_paths.current());
							next_paths=next_paths.previous();
						}
					}
				}
				else{
					if(!test.isEmpty()){
						if(!this.tabIsBusy[0] && !this.tabIsBusy[1] && !this.tabIsBusy[2]){
							if(test.get(0)!=null){
								if(test.get(0).size()>1 && test.get(0).get(0)==carte.getCase(x[test.get(0).getValue()], y[test.get(0).getValue()])){
									for(int j=0;j<3;j++){
										if(j!=test.get(0).getValue()){
											test.get(0).beforeBlock(carte, current_paths[j],true);
										}
									}
									//this.dessin.getRobot(test.get(0).getValue()).moveTo(x[test.get(0).getValue()], y[test.get(0).getValue()]);
									this.dessin.getRobot(test.get(0).getValue()).walkPath(test.get(0));
									current_paths_4ever[test.get(0).getValue()]=new Chemin(test.get(0));
									this.application.putLog(test.get(0).getValue(),"Resol. balle");
									current_paths[test.get(0).getValue()]=test.get(0);
								}
								test.remove(0);
							}
						}
					}
					else if(!this.tabIsBusy[0])
						next_paths=null;
				}
				break;
			case 3:
				if(next_paths==null && this.carte.getCase(x[0],y[0])!=null){
					if(!this.tabIsBusy[0] && !this.tabIsBusy[1] && !this.tabIsBusy[2]){
						next_paths=this.resolution(0,null, current_paths[0], current_paths[1], current_paths[2], this.carte.setExit(), null, null, true);
						while(next_paths.current()!=null){
							test.add(0,next_paths.current());
							next_paths=next_paths.previous();
						}
					}
				}
				else{
					if(!test.isEmpty()){
						if(!this.tabIsBusy[0] && !this.tabIsBusy[1] && !this.tabIsBusy[2]){
							if(test.get(0)!=null){
								if(test.get(0).size()>1 && test.get(0).get(0)==carte.getCase(x[test.get(0).getValue()], y[test.get(0).getValue()])){
									for(int j=0;j<3;j++){
										if(j!=test.get(0).getValue()){
											test.get(0).beforeBlock(carte, current_paths[j],true);
										}
									}
									//this.dessin.getRobot(test.get(0).getValue()).moveTo(x[test.get(0).getValue()], y[test.get(0).getValue()]);
									this.dessin.getRobot(test.get(0).getValue()).walkPath(test.get(0));
									current_paths_4ever[test.get(0).getValue()]=new Chemin(test.get(0));
									this.application.putLog(test.get(0).getValue(),"Resol. sortie");
									current_paths[test.get(0).getValue()]=test.get(0);
								}
							}
							test.remove(0);
						}
					}
					else if(this.carte.getCase(x[0],y[0])==null)
						continuer=false;
					else if(!this.tabIsBusy[0])
						next_paths=null;
				}
				break;
			}
			if(number==3 && step==1 && next_paths==null)
				step=4;
			if(number==3 && step==3 && next_paths==null)
				step=5;
		}
	}
	
	public DessinCarte dessinCarte(){
		return this.dessin;
	}
	
	@SuppressWarnings("unchecked")
	public ListeChemin resolution(int k, ListeChemin liste, Chemin cur1, Chemin cur2, Chemin cur3, Case obj1, Case obj2, Case obj3, boolean exit){
		if(k<30){
			k++;
			if(liste==null)
				liste = new ListeChemin(null,0,null);
			Chemin block = new Chemin();
			if(cur2!=null)
				block.add(cur2.get(cur2.size()-1));
			if(cur3!=null)
				block.add(cur3.get(cur3.size()-1));
			Chemin pour=new Chemin();
			if(block.size()>0){
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
			}
			boolean collision=false;
			Chemin temp = this.carte.createPath(cur1.get(cur1.size()-1), obj1, null);
			Chemin princ = new Chemin(temp);
			princ.setValue(cur1.getValue());
			int	cout=0;
			if(temp!=null){
				if(temp.size()>1){
					cout=temp.getValue();
					temp.setValue(cur1.getValue());
					if(temp.isCollision(carte, block, true)){
						collision=true;
						temp.beforeBlock(carte, block, true);
					}
					else{
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
							comparer.add(new ListeChemin(temp1,cout1+liste.getCout(),liste));
						}
					}
				}
				ListeChemin solution=null;
				Chemin avoid = new Chemin();
				if(cur3!=null)
					avoid.add(cur3);
				avoid.add(princ);
				Chemin temp2=null;
				if(cur2!=null){
				temp2=this.carte.closestDiscover(cur2.get(cur2.size()-1), 1, avoid,  new Chemin(cur1.get(cur1.size()-1)), exit);
					if(temp2!=null){
						temp2.setValue(cur2.getValue());
						if(temp2.size()>1){
							solution = this.resolution(k,liste, cur2, cur3, temp, temp2.get(temp2.size()-1), obj3, obj1, exit);
						}
					}
				}
				Chemin temp3=null;
				if(solution!=null && temp2!=null){
					avoid = new Chemin();
					avoid.add(temp2);
					avoid.add(princ);
					temp3=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid,  new Chemin(cur1.get(cur1.size()-1)), exit);
					if(temp3!=null){
						temp3.setValue(cur3.getValue());
						if(temp3.size()>1){
							solution = this.resolution(k,solution, cur3, temp, temp2, temp3.get(temp3.size()-1), obj1, temp2.get(temp2.size()-1), exit);
						}
					}
				}
				else{
					avoid = new Chemin();
					if(cur2!=null)
						avoid.add(cur2);
					avoid.add(princ);
					if(cur3!=null){
						temp3=this.carte.closestDiscover(cur3.get(cur3.size()-1), 1, avoid,  new Chemin(cur1.get(cur1.size()-1)), exit);
						if(temp3!=null){
							temp3.setValue(cur3.getValue());
							if(temp3.size()>1){
								solution = this.resolution(k,liste, cur3, temp, cur2, temp3.get(temp3.size()-1), obj1, cur2.get(cur2.size()-1), exit);
							}
						}
					}
				}
				if(solution!=null){
					if(temp2!=null && temp3!=null && temp!=null){
					ListeChemin aux = solution;
					if(aux.current()!=null){
						while(aux.previous()!=null && aux.current().getValue()!=cur1.getValue()){
							aux=aux.previous();
						}
					}
					if(aux.current()!=null)
						solution = this.resolution(k,solution, aux.current(), temp2, temp3, temp.get(0), temp2.get(temp2.size()-1), temp3.get(temp3.size()-1), exit);
					solution = new ListeChemin(temp, cout+solution.getCout(),solution);
					solution = this.resolution(k,solution, temp, temp2, temp3, obj1, temp2.get(temp2.size()-1), temp3.get(temp3.size()-1), exit);
					}
					else
						solution=null;
				}
				if(solution!=null)
					comparer.add(solution);
				if(comparer.size()>0){
					Collections.sort(comparer);
					return comparer.get(0);
				}
			}
			return liste;
		}
		else
			return new ListeChemin(null,Integer.MAX_VALUE,null);
	}

	
	public void MAJTabIsBusy(InitPC init){
		for(int i=0;i<3;i++){
			this.tabIsBusy[i]=init.getThreadComm(i).getEnMouvement();
		}
	}
}
