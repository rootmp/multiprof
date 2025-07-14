package l2s.gameserver.model.base;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Bonux
 **/
public enum PledgeRank
{
	/* 0 */VAGABOND, // Кочевник
	/* 1 */VASSAL, // Вассал
	/* 2 */HEIR, // Наследник
	/* 3 */KNIGHT, // Рыцарь
	/* 4 */WISEMAN, // Старейшина
	/* 5 */BARON, // Барон
	/* 6 */VISCOUNT, // Виконт
	/* 7 */COUNT, // Граф
	/* 8 */MARQUIS, // Маркиз
	/* 9 */DUKE, // Герцог
	/* 10 */GRAND_DUKE, // Великий герцог
	/* 11 */DISTINGUISHED_KING, // Король
	/* 12 */EMPEROR; // Император

	public static final PledgeRank[] VALUES = values();

	public static PledgeRank valueOfXml(String name)
	{
		if(StringUtils.isNumeric(name))
		{
			int id = Integer.parseInt(name);
			return VALUES[id];
		}

		String upperCaseName = name.toUpperCase();
		for(PledgeRank type : VALUES)
		{
			if(type.name().equals(upperCaseName))
				return type;
		}
		throw new NoSuchElementException("Unknown name '" + name + "' for enum PledgeRank");
	}
}