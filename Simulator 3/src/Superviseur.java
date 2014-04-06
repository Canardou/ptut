import java.awt.Point;

import Dialogue.Dialogue;
import labyrinth.*;
import drawing.*;

public class Superviseur {
	private Carte carte;
	private DessinCarte dessin;
	private Point [] robots;
	private Chemin []  paths;
	
	public Superviseur(Carte carte){
		this.robots=new Point[3];
		this.paths=new Chemin[3];
		this.carte=carte;
		this.dessin=new DessinCarte(carte);
		this.dessin.launch();
	}
	
	public void simulate() throws InterruptedException {
		Carte labyrinth=new Carte(this.carte.getWidth(),this.carte.getHeight());
		labyrinth.randomMaze(0.35);
		for(int i=0;i<3;i++){
			dessin.getRobots()[i]=new VirtualRobots((int)(Math.random()*this.carte.getWidth()),(int)(Math.random()*this.carte.getHeight()),(int)(Math.random()*4),i,i);
			robots[i]=new Point(dessin.getRobots()[i].getX(),dessin.getRobots()[i].getY());
			dessin.getRobots()[i].setVisible(true);
		}
		dessin.showMark(true);
		Dialogue.Warning("Lancement d'une simulation");
		//Dans une simulation dessin est utilisé comme référent pour le superviseur
		//Dans un cas réel le superviseur devra être cadencé par les appels blutooth
		while(true){
			//Choix du robot et import des informations
			int numero=(int)(Math.random()*robots.length);
			robots[numero].move(dessin.getRobots()[numero].getX(), dessin.getRobots()[numero].getY());;
			//Résolution
			int x= (int)robots[numero].getX();
			int y= (int)robots[numero].getY();
			this.carte.update(x, y, labyrinth.getCase(x, y).getCompo());
			this.carte.reveal(x, y);
			int k=1;
			boolean retry=false;
			do{
				retry=false;
				if(carte.closestDiscover(x, y, k)!=null)
					paths[numero]=carte.createPath(x, y, carte.closestDiscover(x, y, k).getX(), carte.closestDiscover(x, y, k).getY());
				//Envois infos
				for(int j=0;j<3;j++){
					if(j!=numero){
						if(paths[numero].collision(paths[j])<Math.max(paths[numero].size(),2)){
							retry=true;
						}
						paths[numero].beforeBlock(paths[j]);
					}
				}
				if(retry)
					System.out.println("retry "+k);
				k++;
			}while(retry && k<5);
			if(retry=true && k==5){
				System.out.println("last retry");
				paths[numero]=carte.createPath(x, y, (int)(Math.random()*this.carte.getWidth()),(int)(Math.random()*this.carte.getHeight()));
				paths[numero].stopToVisibility();
				for(int j=0;j<3;j++){
					if(j!=numero){
						if(paths[numero].collision(paths[j])<paths[numero].size()){
							retry=true;
						}
						paths[numero].beforeBlock(paths[j]);
					}
				}
			}
			dessin.getRobots()[numero].walkPath(paths[numero]);
			Thread.sleep(100);
		}
	}
	
	public DessinCarte dessinCarte(){
		return this.dessin;
	}
}
