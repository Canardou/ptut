package orders;

import robot.*;
import java.util.ArrayList;

/**
 * Cette classe contient une liste qui permet de gerer les ordres qui seront execut�s 
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
	 * @see ArrayList
	 */
	private ArrayList<Integer> list;
	
	/**
	 * Constructeur de ListOrder
	 */
	public ListOrder () {
		this.list = new ArrayList<Integer> () ;
	}
	
	/**
	 * Ajoute un ordre � la fin de liste. Sauf s'il s'agit d'un ordre jug� � haute priorit�, 
	 * auquel cas l'ordre est mis � l'index 0 pour etre le prochain execut�
	 * @param order
	 * 		Ordre � ins�rer dans la liste
	 * @return 0 si l'op�ration s'est bien d�roul�e
	 * @see Param#STOP
	 * @see Param#FORWARD
	 * @see Param#TURNL
	 * @see Param#TURNR
 	 * @see Param#TURNB
 	 * @see Param#CALCOMPASS
 	 * @see Param#SAVEREFANGLE
 	 * @see Param#CHECKFIRSTCASE
 	 * @see Param#SETPOSITION
 	 * @see Param#CLEARLISTORDER
	 */
	public int addOrder(int order)
	{
		if( order==Param.STOP || order==Param.FORWARD 
				|| order==Param.TURNL || order==Param.TURNR 
				|| order==Param.TURNB || order==Param.CALCOMPASS 
				|| order==Param.SAVEREFANGLE || order==Param.CHECKFIRSTCASE 
				|| order==Param.SETPOSITION ) {
			this.list.add(order);
			return 0;
		}
		else if( order==Param.CLEARLISTORDER ) {
			this.list.add(0,order);
			return 0;
		}
		else {
			System.out.println("[ERR]addOrder:param");
			return 1;
		}
	}
	
	/**
	 * Retire et renvoit l'ordre le plus ancien pr�sent dans la liste
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
		if(this.list.isEmpty()) {
			return Param.STOP;
		}
		else {
			return this.list.remove(0);
		}		
	}
	
	/**
	 * Vide entierement la liste
	 */
	public void clear() {
		this.list.clear();
	}
	
	/**
	 * V�rifie si la liste est vide
	 * @return true si la liste est vide
	 */
	public boolean isEmpty() {
		if(this.list.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
}