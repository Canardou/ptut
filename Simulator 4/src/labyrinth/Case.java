package labyrinth;

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
	
	//murs & decouverte
	private byte composition;
	//Possibilitée d'utiliser un mask
	private int x;
	private int y;
	
	/*
	 * Constructeurs
	 */
	
	public Case(int x, int y){
		this.x=x;
		this.y=y;
		this.composition=0;
	}
	
	/*
	 * Méthodes
	 */
	
	/**
	 * @method changeState
	 * @param {int} direction
	 * @param {int} state
	 * @return {boolean} true si l'action est effectuee
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
	 * @method close
	 * @param {int} direction
	 * @return {boolean} true si action effectuee
	 */
	
	public boolean close(int direction){
		return changeState(direction,true);
	}
	
	/**
	 * @method bound
	 * @param {int} direction
	 * @return {boolean} true si action effectuee
	 */
	
	public boolean bound(int direction){
		return changeState(direction,false);
	}
	
	/**
	 * @method isCrossable
	 * @param {int} direction
	 * @return {boolean} true si absence de mur
	 */
	
	public boolean isCrossable(int direction){
		if(direction>=0 && direction<4){
			return !((byte)(composition&(1<<direction))!=0);
		}
		else
			return false;
	}
	
	/**
	 * @method setreveal
	 * @desc declare la case visitee
	 */
	
	public void setReveal(){
		this.composition=(byte)(this.composition|(1<<decouverte));
	}
	
	/**
	 * @method isRevealed
	 * @return {boolean}
	 * @desc retourne si la case est visitee
	 */
	
	public boolean isRevealed(){
		return ((byte)(composition&(1<<decouverte))!=0);
	}
	
	/**
	 * @method getX
	 * @return {int} coordonne x de la case
	 */
	
	public int getX(){
		return this.x;
	}
	
	/**
	 * @method getX
	 * @param {int} direction
	 * @return {int} coordonne x de la case dans la direction
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
	 * @method getY
	 * @return {int} coordonne y de la case
	 */
	
	public int getY(){
		return this.y;
	}
	
	/**
	 * @method getY
	 * @param {int} direction
	 * @return {int} coordonne y de la case dans la direction
	 */
	
	public int getY(int direction){
		if(direction==Case.UP)
			return this.y-1;
		else if(direction==Case.DOWN)
			return this.y+1;
		else
			return this.y;
	}
	
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
	
	public int getDir(Case autre){
		return this.getDir(autre.getX(),autre.getY());
	}
	
	public String toString(){
		return "Case ["+this.getX()+"]["+this.getY()+"]-"+this.getCompo();
	}
	
	/**
	 * @method setmark
	 * @desc declare la case visitee
	 */
	
	public void setMark(){
		this.composition=(byte)(this.composition|(1<<marque));
	}
	
	/**
	 * @method ismark
	 * @return {boolean}
	 * @desc retourne si la case est visitee
	 */
	
	public boolean isMark(){
		return ((byte)(composition&(1<<marque))!=0);
	}
	
	public byte getCompo(){
		return this.composition;
	}
	
	public void update(int x, int y, byte composition){
		this.x=x;
		this.y=y;
		this.composition=composition;
	}
	
	public void update(byte composition){
		this.composition=composition;
	}
}
