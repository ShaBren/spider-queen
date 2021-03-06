package sq.enums;

/**
 * Defines the possible offerings in the game.
 */
public enum EnumOfferingType
{
	BRAIN,
	HEART,
	SKULL;
	
	public String getName()
	{
		return name().toLowerCase();
	}
	
	public String getAcceptorName()
	{
		switch (this)
		{
		case BRAIN: return "zombies";
		case HEART: return "creepers";
		case SKULL: return "skeletons";
		default: return "";
		}
	}
}
