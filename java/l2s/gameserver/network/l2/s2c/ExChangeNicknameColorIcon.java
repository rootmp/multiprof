package l2s.gameserver.network.l2.s2c;

public class ExChangeNicknameColorIcon extends L2GameServerPacket
{
	private int itemId;

	public ExChangeNicknameColorIcon(int itemId)
	{
		this.itemId = itemId;
	}

	@Override
	protected void writeImpl()
	{
		writeD(itemId);
	}
}