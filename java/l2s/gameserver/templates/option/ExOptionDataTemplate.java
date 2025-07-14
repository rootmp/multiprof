package l2s.gameserver.templates.option;

import l2s.gameserver.stats.StatTemplate;
import l2s.gameserver.stats.funcs.Func;


public class ExOptionDataTemplate extends StatTemplate
{
	private final int _id;
	private final int _level;
	
	public ExOptionDataTemplate(int id, int level)
	{
		_id = id;
		_level = level;
	}

	public int getId()
	{
		return _id;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public final Func[] getStatFuncs()
	{
		return getStatFuncs(this);
	}
}
