package commun;

public enum OrdreDeplacement
{
	/**
	 * Defini les ordres pour le type DEPLACEMENT
	 */
	DEP_TURN_LEFT ('L'),
	DEP_TURN_RIGHT ('R'),
	DEP_GO_FORWARD ('F'),
	DEP_GO_BACKWARD ('B'),
	DEP_STOP ('S'),
	DEP_SET_TRAVEL_SPEED ('V'),
	DEP_TURN_ANGLE ('A'),
	DEP_ARC_BACKWARD('W'),
	DEP_ARC_FORWARD('K');
	
	private Character value;
	
	private OrdreDeplacement(char Value)
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
