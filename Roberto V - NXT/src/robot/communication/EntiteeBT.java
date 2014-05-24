package robot.communication;
import java.io.*;



public class EntiteeBT{
	
	private String nom;
	private byte ID;
	private String adr;
	private DataInputStream input;
	private DataOutputStream output;
	public Trame2 trame;
	
	public EntiteeBT(String nom, byte ID, String adr){
		
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
	
	public byte getID(){
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