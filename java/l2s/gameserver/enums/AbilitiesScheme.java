package l2s.gameserver.enums;

public enum AbilitiesScheme
{
	A(0),
	B(1);

	private int id;

	AbilitiesScheme(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public static AbilitiesScheme invert(AbilitiesScheme scheme)
	{
		return scheme == A ? B : A;
	}

	public static AbilitiesScheme valueOf(int cPreset)
	{
		for(AbilitiesScheme scheme : values())
		{
			if(scheme.getId() == cPreset)
				return scheme;
		}
		throw new IllegalArgumentException("Invalid AbilitiesScheme id: " + cPreset);
	}
}
