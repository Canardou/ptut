package commun;

public enum OrdreRole
{
	/**
	 * Defini les Ordres pour le type ROLE
	 */
	ROLE_IS_LEADER ('1'),
	ROLE_IS_SLAVE ('0'),
	ROLE_CIBLE_IS_NEAR('N'),
	ROLE_CIBLE_IS_CAPTURED('C'),
	ROLE_CIBLE_IS_FAR('F'),
	ROLE_SHUTDOWN('X');
	
	private Character value;

	private OrdreRole(char Value)
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
