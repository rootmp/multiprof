package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.item.capsule.CapsuleData;
import l2s.gameserver.templates.item.capsule.CreateItemGroupInfo;
import l2s.gameserver.templates.item.capsule.CreateItemInfo;

public final class CapsuleHolder extends AbstractHolder
{
	private static final CapsuleHolder _instance = new CapsuleHolder();

	private final Map<Integer, CapsuleData> capsules = new HashMap<>();

	public static CapsuleHolder getInstance()
	{
		return _instance;
	}

	public void addCapsule(int itemId, List<CreateItemInfo> createItems, List<CreateItemInfo> createRandomItems, List<CreateItemGroupInfo> createMultiItems)
	{
		capsules.put(itemId, new CapsuleData(itemId, createItems, createRandomItems, createMultiItems));
	}

	public CapsuleData getCapsule(int itemId)
	{
		return capsules.get(itemId);
	}

	@Override
	public int size()
	{
		return capsules.size();
	}

	@Override
	public void clear()
	{
		capsules.clear();
	}

	public boolean isCapsule(int itemId)
	{
		return capsules.containsKey(Integer.valueOf(itemId));
	}
}
