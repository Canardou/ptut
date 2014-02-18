import lejos.nxt.Button;

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
	 
	 /**************/
	 /** METHODES **/
	 /**************/
	 
	 public void addLog(String message){
		 if(this.pile.empty())
			this.pile.push(message);
		 else if(this.pile.size()<this.size)
		 	this.pile.push(message);
	 }
	 
	 public void showAllLogs(){
		 String temp;
		 if(this.pile.size()>0){
	     temp=this.pile.pop();
	     this.showAllLogs();
		 System.out.println(temp);
		 Button.waitForAnyPress();
		 }
	 }
	 
	 public void showDescLogs(){
		 int i;
		 String temp;
		 for(i=0;i<this.pile.size();i++){
		 temp=this.pile.pop();
		 System.out.println(temp);
		 Button.waitForAnyPress();
		 }
	 }
	 
	 public void showLastLog(){
		 String temp;
		 temp=this.pile.peek();
		 System.out.println(temp);
		 Button.waitForAnyPress();
	 }
}
