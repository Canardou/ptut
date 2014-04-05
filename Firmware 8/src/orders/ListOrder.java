package orders;

import robot.*;
import java.util.Stack;

/**
 * Cette classe contient une pile qui permet de gerer les ordres qui seront executés 
 * par le robot (en FIFO). Elle sert de buffer de reception
 * @author Thomas
 * @see Stack
 * @see Param#STOP
 * @see Param#FORWARD
 * @see Param#TURNL
 * @see Param#TURNR
 * @see Param#TURNB
 * @see Param#CALCOMPASS
 * @see Param#SAVEREFANGLE
 * @see Param#CHECKFIRSTCASE
 * @see Param#SETPOSITION
 */
public class ListOrder {	
	
	/**
	 * Attribut contenant la pile d'ordres
	 * @see Stack
	 */
	private Stack<Integer> pile;
	
	/**
	 * Constructeur de ListOrder
	 */
	public ListOrder () {
		this.pile = new Stack<Integer> () ;
	}
	
	/**
	 * Ajoute un ordre en bas de la pile
	 * @param order
	 * 		Ordre à insérer dans la pile
	 * @see Param#STOP
	 * @see Param#FORWARD
	 * @see Param#TURNL
	 * @see Param#TURNR
 	 * @see Param#TURNB
 	 * @see Param#CALCOMPASS
 	 * @see Param#SAVEREFANGLE
 	 * @see Param#CHECKFIRSTCASE
 	 * @see Param#SETPOSITION
	 */
	public void addOrder(int order)
	{
		this.pile.insertElementAt(order, 0);
	}
	
	/**
	 * Retire l'ordre en haut de la pile
	 * @return l'ordre en haut de la pile ou l'ordre STOP si la pile est vide
	 * @see Param#STOP
	 * @see Param#FORWARD
	 * @see Param#TURNL
	 * @see Param#TURNR
 	 * @see Param#TURNB
 	 * @see Param#CALCOMPASS
 	 * @see Param#SAVEREFANGLE
 	 * @see Param#CHECKFIRSTCASE
 	 * @see Param#SETPOSITION
	 */
	public Integer getOrder(){
		if(this.pile.isEmpty()) {
			return Param.STOP;
		}
		else {
			return this.pile.pop();
		}		
	}
	
}