import labyrinth.*;

public class mainSimu {
	
	private static Carte exploration;
	public static Superviseur test;
	
	public static void main(String [] args){
		exploration=new Carte(6);
		new mainSimu();
	}
	
	public mainSimu(){
		test = new Superviseur(exploration);
	}
}
