package labyrinth;

/**
 * Cette classe permet la gestion des cases du labyrinth
 * @author Olivier Hachette
 *
 */
public class Case {
	/*
	 * Constantes
	 */
	
	public static final int UP=0;
	public static final int LEFT=1;
	public static final int DOWN=2;
	public static final int RIGHT=3;
	private static final int decouverte=4;
	private static final int marque=5;
	
	/*
	 * Attributs
	 */
	
	private byte composition;
	private int x;
	private int y;
	
	/*
	 * Constructeurs
	 */
	
	/**
	 * Permet d'intialiser une case a certaines coordonnees
	 * @param x Coordonnee x
	 * @param y Coordonnee y
	 */
	public Case(int x, int y){
		this.x=x;
		this.y=y;
		this.composition=0;
	}
	
	/*
	 * Methodes
	 */
	
	/**
	 * Change l'etat du mur dans une certaine direction
	 * @param direction La direction a modifier
	 * @param state L'etat du mur, vrai pour ferme, faux pour ouvert
	 * @return Vrai si la modification a bien eu lieu
	 */
	private boolean changeState(int direction, boolean state){
		if(direction>=0 && direction<4){
			if(state)
				this.composition=(byte)(this.composition|(1<<direction));
			else
				this.composition=(byte)(this.composition&~(1<<direction));
			return true;
		}
		else
			return false;
		
	}
	
	/**
	 * Ferme un mur dans une certaine direction
	 * @param direction La direction vers laquelle fermer
	 * @return Vrai si la modification a bien eu lieu
	 */
	public boolean close(int direction){
		return changeState(direction,true);
	}
	
	/**
	 * ouvre un mur dans une certaine direction
	 * @param direction La direction vers laquelle ouvrir
	 * @return Vrai si la modification a bien eu lieu
	 */
	public boolean bound(int direction){
		return changeState(direction,false);
	}
	
	/**
	 * Verifie qu'un mur est ouvert ou ferme dans une direction
	 * @param direction La direction vers laquelle verifier
	 * @return Vrai si le mur est ouvert
	 */
	public boolean isCrossable(int direction){
		if(direction>=0 && direction<4){
			return !((byte)(composition&(1<<direction))!=0);
		}
		else
			return false;
	}
	
	/**
	 * Declare une case comme deja visitee
	 */
	public void setReveal(){
		this.composition=(byte)(this.composition|(1<<decouverte));
	}
	
	/**
	 * Verifie qu'une case est deja visitee
	 * @return Retourne vrai si la case est deja visitee
	 */
	public boolean isRevealed(){
		return ((byte)(composition&(1<<decouverte))!=0);
	}
	
	/**
	 * Recupere la coordonnee x
	 * @return La coordonnee x de la case
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * Recupere la coordonnee x dans une certaine direction
	 * @param direction
	 * @return coordonne x de la case dans la direction
	 */
	public int getX(int direction){
		if(direction==Case.LEFT)
			return this.x-1;
		else if(direction==Case.RIGHT)
			return this.x+1;
		else
			return this.x;
	}
	
	/**
	 * Recupere la coordonnee y
	 * @return La coordonnee y de la case
	 */
	public int getY(){
		return this.y;
	}
	
	/**
	 * Recupere la coordonnee y dans une certaine direction
	 * @param direction
	 * @return coordonne y de la case dans la direction
	 */
	public int getY(int direction){
		if(direction==Case.UP)
			return this.y-1;
		else if(direction==Case.DOWN)
			return this.y+1;
		else
			return this.y;
	}
	
	/**
	 * Donne la direction entre cette case et les coordonnees d'une autre case adjacente
	 * @param x Coordonnee en x de l'autre case
	 * @param y Coordonnee en y de l'autre case
	 * @return Retourne la direction ou -1 si les coordonnees ne sont pas adjacents
	 */
	public int getDir(int x, int y){
		if(this.x-x==1 && this.y-y==0)
			return Case.LEFT;
		else if(this.x-x==-1 && this.y-y==0)
			return Case.RIGHT;
		else if(this.y-y==1 && this.x-x==0)
			return Case.UP;
		else if(this.y-y==-1 && this.x-x==0)
			return Case.DOWN;
		else
			return -1;
	}
	
	/**
	 * Donne la direction entre cette case et une autre case
	 * @param autre L'autre case
	 * @return Retourne la direction ou -1 si l'autre case n'est pas adjacente
	 */
	public int getDir(Case autre){
		if(autre!=null){
			return this.getDir(autre.getX(),autre.getY());
		}
		else
			return -1;
	}
	
	@Override
	public String toString(){
		return "Case ["+this.getX()+"]["+this.getY()+"]-"+this.getCompo();
	}
	
	/**
	 * Met a jour la composition de la case en lui mettant la marque
	 */
	public void setMark(){
		this.composition=(byte)(this.composition|(1<<marque));
	}
	
	/**
	 * Verifie que la case contient la marque
	 * @return Retourne vrai si la case contient la marque
	 */
	public boolean isMark(){
		return ((byte)(composition&(1<<marque))!=0);
	}
	
	/**
	 * Recupere la composition de la case
	 * @return La composition de la case sous forme d'un byte
	 */
	public byte getCompo(){
		return this.composition;
	}
	
	/**
	 * Met a jour la composition d'une case et ses coordonnees
	 * @param x Nouvelle coordonnee x
	 * @param y Nouvelle coordonnee y
	 * @param composition Nouvelle composition sous forme d'un byte
	 */
	public void update(int x, int y, byte composition){
		this.x=x;
		this.y=y;
		this.composition=composition;
	}
	
	/**
	 * Met a jour seulement la compositino d'une case
	 * @param composition Nouvelle composition sous forme d'un byte
	 */
	public void update(byte composition){
		this.composition=composition;
	}
}
