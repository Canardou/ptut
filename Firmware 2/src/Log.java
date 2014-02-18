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
	
	
	 /******************/
	 /** CONSTRUCTEUR **/
	 /******************/
	 public Log(){
	 }
	 
	 /**************/
	 /** METHODES **/
	 /**************/
	 
	 public void addLog(String message){
		 	this.pile.push(message);
	 }
	 
	 public void showLog(){
		 int i;
		 String temp;
		 for(i=0;i<this.pile.size();i++){
		 temp=this.pile.pop();
		 System.out.println(temp);
		 Button.waitForAnyPress();
		 }
	 }
}
