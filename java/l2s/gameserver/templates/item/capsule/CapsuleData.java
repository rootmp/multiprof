package l2s.gameserver.templates.item.capsule;

import java.util.List;

public class CapsuleData
{
	private final int itemId;
	private final List<CreateItemInfo> createItems;
	private final List<CreateItemInfo> createRandomItems;
	private final List<CreateItemGroupInfo> createMultiItems;

	public CapsuleData(int itemId, List<CreateItemInfo> createItems, List<CreateItemInfo> createRandomItems, List<CreateItemGroupInfo> createMultiItems)
	{
		this.itemId = itemId;
		this.createItems = createItems;
		this.createRandomItems = createRandomItems;
		this.createMultiItems = createMultiItems;
	}

	public int getItemId()
	{
		return itemId;
	}

	public List<CreateItemInfo> getCreateItems()
	{
		return createItems;
	}

	public List<CreateItemInfo> getCreateRandomItems()
	{
		return createRandomItems;
	}

	public List<CreateItemGroupInfo> getCreateMultiItems()
	{
		return createMultiItems;
	}
}
