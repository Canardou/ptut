package communication;

public class Trame {
	

	private byte ID;
	private byte position_x;
	private byte position_y;
	private boolean mur_haut;
	private boolean mur_gauche;
	private boolean mur_droit;
	private int direction;

	public Trame(byte ID, byte x, byte y, boolean h, boolean g, boolean d, int dir){

		this.ID = ID;
		this.position_x=x;
		this.position_y=y;
		this.mur_haut=h;
		this.mur_gauche=g;
		this.mur_droit=d;
		this.direction=dir;
	}
	
	public Trame(int ID, int x, int y, boolean h, boolean g, boolean d, int dir){

		this.ID = (byte)ID;
		this.position_x=(byte)x;
		this.position_y=(byte)y;
		this.mur_haut=h;
		this.mur_gauche=g;
		this.mur_droit=d;
		this.direction=dir;
	}
	
	public Trame(byte ID, byte x, byte y, int direction){

		this.ID = ID;
		this.position_x=x;
		this.position_y=y;
		this.direction=direction;
	}
	
	public byte getID(){
		return this.ID;
	}
	
	public byte getPosX(){
		return this.position_x;
	}
	
	public byte getPosY(){
		return this.position_y;
	}	

	public boolean getMurHaut(){
		return this.mur_haut;
	}

	public boolean getMurGauche(){
		return this.mur_gauche;
	}	
	
	public boolean getMurDroit(){
		return this.mur_droit;
	}		
	
	public int getDirection(){
		return this.direction;
	}


}
