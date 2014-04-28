package communication;


public class InitPC {
	
	ThreadComm tComH;
	ThreadComm tComJ;
	ThreadComm tComI;
	ThreadComm tComF;
	
	public InitPC(){
		this.tComH = new ThreadComm(InfoEntitee.robotH);
		this.tComJ = new ThreadComm(InfoEntitee.robotJ);
		this.tComI = new ThreadComm(InfoEntitee.robotI);
		this.tComF = new ThreadComm(InfoEntitee.robotF);
		this.tComH.start();
		this.tComJ.start();
		this.tComI.start();
		this.tComF.start();
		
	
	}
	

/*	public static void main(String[] args) {
		
		new InitPC();
		
	}*/

}
