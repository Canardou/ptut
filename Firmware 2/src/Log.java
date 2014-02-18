import lejos.nxt.Button;
import lejos.nxt.LCD;
import java.util.Stack;

public class Log {
	/****************/
	/** CONSTANTES **/
	/****************/
	
	/***************/
	/** ATTRIBUTS **/
	/***************/
	private Stack<String> pile;
	private int size;
	
	
	 /******************/
	 /** CONSTRUCTEUR **/
	 /******************/
	 public Log(int MaxSize){
		 this.pile=new Stack<String>();
		 this.size=MaxSize;
	 }
	 
	 public Log(){
		 this.pile=new Stack<String>();
		 this.size=-1;
	 }
	 
	 /**************/
	 /** METHODES **/
	 /**************/
	 
	 public void addLog(String message){
		 if(this.pile.size()<this.size || this.size==-1)
		 	this.pile.push(message);
	 }
	 
	 public void showAllLogs(){
		 String temp;
		 int i, num=this.pile.size();
		 if(num>0){
	     temp=this.pile.pop();
	     this.showAllLogs();
	     LCD.drawString("Log "+num+" :",0,0);
	     for(i=0;i<=Math.min(temp.length()/16,6);i++){
	     	LCD.drawString(temp.substring(i*16, Math.min(temp.length(),(i+1)*16)),0,i+1);
		 }
		 Button.waitForAnyPress();
		 LCD.clear();
		 }
	 }
	 
	 public void showDescLogs(){
		 int i, j, num=this.pile.size();
		 String temp;
		 for(i=0;i<num;i++){
			 LCD.drawString("Log "+this.pile.size()+" :",0,0);
			 temp=this.pile.pop();
			 for(j=0;j<=Math.min(temp.length()/16,6);j++){
				 LCD.drawString(temp.substring(j*16, Math.min(temp.length(),(j+1)*16)),0,j+1);
			 }
			 Button.waitForAnyPress();
			 LCD.clear();
		 }
	 }
	 
	 public void showLastLog(){
		 String temp;
		 int i;
		 temp=this.pile.peek();
		 LCD.drawString("Log "+this.pile.size()+" :",0,0);
		 for(i=0;i<=Math.min(temp.length()/16,6);i++){
			 LCD.drawString(temp.substring(i*16, Math.min(temp.length(),(i+1)*16)),0,i+1);
		 }
		 Button.waitForAnyPress();
		 LCD.clear();
	 }
}
