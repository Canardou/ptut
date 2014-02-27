package commun;

public enum OrdreData
{
	/**
	 * Defini les ordres pour le type DATA
	 */
	DATA_ANGLE ('O'),
	DATA_DISTANCE ('D'),
	DATA_X('X'),
	DATA_Y('Y'),
	DATA_CIBLE_IS_NOT_VISIBLE ('?'),
	DATA_CIBLE_CAPTURED('!');
		
	private Character value;
	
	private OrdreData(char Value)
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
