package communication;


public class InitPC {
	
	ThreadComm tComH;
	ThreadComm tComJ;
	ThreadComm tComI;
	ThreadComm tComF;
	InfoEntitee IE;
	
	public InitPC(){
		this.IE = new InfoEntitee();
		this.tComH = new ThreadComm(this.IE.robotH);
		this.tComJ = new ThreadComm(this.IE.robotJ);
		this.tComF = new ThreadComm(this.IE.robotF);
		this.tComH.start();
		this.tComJ.start();
		this.tComF.start();
		
	
	}
	

	public static void main(String[] args) {
		new InitPC();
		
	}
}
