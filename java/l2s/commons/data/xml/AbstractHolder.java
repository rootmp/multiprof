package l2s.commons.data.xml;

import l2s.commons.logging.LoggerObject;

/**
 * Author: VISTALL Date: 18:34/30.11.2010
 */
public abstract class AbstractHolder extends LoggerObject
{
	private long parseStartTime, parseEndTime;

	public void log()
	{
		info(String.format("loaded %d%s(s) count.", size(), formatOut(getClass().getSimpleName().replace("Holder", "")).toLowerCase()));
	}

	protected void process()
	{
		//
	}

	public void init()
	{
		//
	}

	public abstract int size();

	public abstract void clear();

	private static String formatOut(String st)
	{
		char[] chars = st.toCharArray();
		StringBuilder buf = new StringBuilder(chars.length);
		for(char ch : chars)
		{
			if(Character.isUpperCase(ch))
			{
				buf.append(" ");
			}
			buf.append(Character.toLowerCase(ch));
		}

		return buf.toString();
	}

	/**
	 * Вызывается непосредственно перед загрузкой
	 */
	public void beforeParsing()
	{
		parseStartTime = System.nanoTime();
	}

	/**
	 * Вызывается после того, как были загружены все элементы.
	 */
	public void afterParsing()
	{
		parseEndTime = System.nanoTime();
	}
}