package communication;
import java.io.*;



public class EntiteeBT{
	
	private String nom;
	private int ID;
	private String adr;
	private DataInputStream input;
	private DataOutputStream output;
	
	public EntiteeBT(String nom, int ID, String adr){
		
		this.nom=nom;
		this.ID=ID;
		this.adr=adr;
	}
	
	public EntiteeBT(String nom, String adr){
		
		this.nom=nom;
		this.adr=adr;
	}
	
	public String getNom(){
		return this.nom;
	}
	
	public String getAdr(){
		return this.adr;
	}
	
	public int getID(){
		return this.ID;
	}	
	public DataInputStream getInput(){
		return this.input;
	}
	
	public DataOutputStream getOutput(){
		return this.output;
	}	
	
	public void setInput(DataInputStream in){
		this.input=in;
	}
	
	public void setOutput(DataOutputStream out){
		this.output=out;
	}	
	
}