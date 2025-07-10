package l2s.gameserver.network.l2.s2c;

public class ExChooseCostumeItem extends L2GameServerPacket
{
	private final int itemId;

	public ExChooseCostumeItem(int itemId)
	{
		this.itemId = itemId;
	}

	@Override
	protected void writeImpl()
	{
		writeD(itemId); // ItemClassID*/
	}
}