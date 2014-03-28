package orders;
import robot.*;
import java.util.Stack;

public class ListOrder {	
		
	/**********************************************/
	/**                                          **/
	/**                ATTRIBUTS                 **/
	/**                                          **/
	/**********************************************/	
	private Stack<Integer> pile;
		
	/**********************************************/
	/**                                          **/
	/**               CONSTRUCTEUR               **/
	/**                                          **/
	/**********************************************/
	public ListOrder () {
		this.pile = new Stack<Integer> () ;
	}
	
	/**********************************************/
	/**                                          **/
	/**                 METHODES                 **/
	/**                                          **/
	/**********************************************/
	public void add(int orderi)
	{
		this.pile.insertElementAt(orderi, 0);
	}
	
	public Integer getOrder(){
		if(this.pile.isEmpty()) {
			return Param.STOP;
		}
		else {
			return this.pile.pop();
		}		
	}
	
}