package l2s.gameserver.templates.item.data;

public class AlterItemData extends ItemData
{
	private final int[] ids;

	public AlterItemData(int[] ids, long count)
	{
		super(ids[0], count);
		this.ids = ids;
	}

	public int[] getIds()
	{
		return ids;
	}
}
