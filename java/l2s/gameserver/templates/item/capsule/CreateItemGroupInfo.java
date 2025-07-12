package l2s.gameserver.templates.item.capsule;

import java.util.List;

public class CreateItemGroupInfo
{
	private final int nGroup;
	private final List<CreateItemInfo> createItems;

	public CreateItemGroupInfo(int nGroup, List<CreateItemInfo> createItems)
	{
		this.nGroup = nGroup;
		this.createItems = createItems;
	}

	public int getGroup()
	{
		return nGroup;
	}

	public List<CreateItemInfo> getCreateItems()
	{
		return createItems;
	}
}
