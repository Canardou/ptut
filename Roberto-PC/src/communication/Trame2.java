package communication;





public class Trame2 {
	
	//attributs
	
	private byte tailleTrame; 
	private byte ID;
	private byte position_x;
	private byte position_y;
	private boolean mur_haut;
	private boolean mur_gauche;
	private boolean mur_droit;
	private byte direction;
	private byte typeTrame;	
	private byte ordre;	
	
	private int demandeCalibration;
	private int donneeCalibration;
	
	//private Case caseCible;
	
	//private char missionTerminee; //ou string mais necessite de convertir du sting en byte
	
	
	
	
	private byte[] contenuT;
	
	
	
	//constructeur
	
	// Trame qui contient des informations sur une case explor�e par le robot
	public Trame2(byte tailleTrame,byte ID, byte x, byte y, boolean h, boolean g, boolean d, byte dir){

		this.tailleTrame=tailleTrame;
		this.ID = ID;
		this.position_x=x;
		this.position_y=y;
		this.mur_haut=h;
		this.mur_gauche=g;
		this.mur_droit=d;
		this.direction=dir;
		this.typeTrame=1; //1
		
		this.contenuT= new byte[9];
		
		this.contenuT[0]=this.tailleTrame;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=this.position_x;
		this.contenuT[3]=this.position_y;
		this.contenuT[4]=convertBoolByte(this.mur_haut);
		this.contenuT[5]=convertBoolByte(this.mur_gauche);
		this.contenuT[6]=convertBoolByte(this.mur_droit);
		this.contenuT[7]=this.direction;
		this.contenuT[8]=this.typeTrame;
		
		
		
	}
	
	/*public Trame2(int ID, int x, int y, boolean h, boolean g, boolean d, int dir){

		this.ID = (byte)ID;
		this.position_x=(byte)x;
		this.position_y=(byte)y;
		this.mur_haut=h;
		this.mur_gauche=g;
		this.mur_droit=d;
		this.direction=dir;
	}*/
	
	// trame qui contient des informations sur la position actuelle des robots
	public Trame2(byte tailleTrame,byte ID, byte x, byte y, byte direction){

		this.tailleTrame=tailleTrame;
		this.ID = ID;
		this.position_x=x;
		this.position_y=y;
		this.direction=direction;
		this.typeTrame=2;
		
		this.contenuT= new byte[6];
		
		this.contenuT[0]=this.tailleTrame;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=this.position_x;
		this.contenuT[3]=this.position_y;
		this.contenuT[4]=this.direction;
		this.contenuT[5]=this.typeTrame;
	}
	
	//trame qui contient une demande une calibration de la boussole
	
	public Trame2(byte tailleTrame,byte ID ,int demandeC){
		
		this.tailleTrame=tailleTrame;
		this.ID=ID;
		this.demandeCalibration= demandeC;
		this.typeTrame=3;
		
		this.contenuT= new byte[4];
		
		this.contenuT[0]=this.tailleTrame;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=(byte)this.demandeCalibration;
		this.contenuT[3]=this.typeTrame;
	}
	
	//trame qui contient l'info de la calibration de la boussole
	public Trame2(byte tailleTrame,byte ID,double moyData){
		
		this.tailleTrame=tailleTrame;
		this.ID=ID;
		this.donneeCalibration=(byte)moyData;
		this.typeTrame=4;
		
		this.contenuT= new byte[4];
		
		this.contenuT[0]=this.tailleTrame;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=(byte)this.donneeCalibration;	
		this.contenuT[3]=this.typeTrame;
	}
	
	//trame qui contient les coordonnees de la case cible  --     il faut rajouter une m�thode getx et gety dans la classe Case
	/*public Trame2(byte tailleTrame,byte ID,Case caseCible){
		
		this.tailleTrame=tailleTrame;
		this.ID=ID;
		this.caseCible=caseCible;
		this.typeTrame=5;
		
		this.contenuT= new byte[5];
		
		this.contenuT[0]=this.tailleTrame;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=(byte)this.caseCible.x;	
		this.contenuT[3]=(byte)this.caseCible.y;	
		this.contenuT[4]=this.typeTrame;
		
	}*/
	
	//trame indiquant que la mission est termin�e
	
	
public Trame2(byte tailleTrame,byte ID ,byte ordre){
		
		this.tailleTrame=tailleTrame;
		this.ID=ID;
		this.ordre= ordre;
		this.typeTrame=5;
		
		this.contenuT= new byte[4];
		
		this.contenuT[0]=this.tailleTrame;
		this.contenuT[1]=this.ID;
		this.contenuT[2]=this.ordre;
		this.contenuT[3]=this.typeTrame;
	}
	
	
	//m�thodes
	
	public byte convertBoolByte(Boolean condition){
		if (condition == true){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public static boolean convertByteBool(byte condition ){
		if (condition == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public byte[] tableauTrame(){
		return this.contenuT;
	}
	
	public byte getTailleTrame(){
		return this.tailleTrame;
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
	public byte getTypeTrame(){
		return this.typeTrame;
	}
	
	public void printTrame(){
		for (int i=0; i<this.tableauTrame().length; i++){
			System.out.println(this.tableauTrame()[i]  );
		}
	}
}
