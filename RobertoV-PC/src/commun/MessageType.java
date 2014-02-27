package commun;

public enum MessageType
{
	/**
	 * Defini les type de message possibles
	 */
	MESSAGE_DEPLACEMENT ('D'),
	MESSAGE_ROLE ('R'),
	MESSAGE_DATA ('I');

	private Character value;
	
	private MessageType(char Value)
	{
		this.value = Value;
	}
	
	public String toString()
	{
		return this.value.toString();
	}
	
	public char getValue()
	{
		return this.value.toString().charAt(0);
	}
}
