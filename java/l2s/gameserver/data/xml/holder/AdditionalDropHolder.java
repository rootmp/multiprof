package l2s.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.npc.AdditionalDrop;

public final class AdditionalDropHolder extends AbstractHolder
{
	private static final AdditionalDropHolder INSTANCE = new AdditionalDropHolder();

	public static AdditionalDropHolder getInstance()
	{
		return INSTANCE;
	}

	private final List<AdditionalDrop> dropList = new ArrayList<>();

	public void addDrop(AdditionalDrop drop)
	{
		dropList.add(drop);
	}

	public List<AdditionalDrop> getDrop()
	{
		return dropList;
	}

	@Override
	public int size()
	{
		return dropList.size();
	}

	@Override
	public void clear()
	{
		dropList.clear();
	}
}
