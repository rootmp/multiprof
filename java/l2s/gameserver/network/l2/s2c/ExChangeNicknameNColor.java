package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExChangeNicknameNColor implements IClientOutgoingPacket
{
	private int _itemObjId;

	public ExChangeNicknameNColor(int itemObjId)
	{
		_itemObjId = itemObjId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemObjId);
	}
}