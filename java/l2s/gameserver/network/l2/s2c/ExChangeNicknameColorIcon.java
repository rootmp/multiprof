package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExChangeNicknameColorIcon implements IClientOutgoingPacket
{
	private int itemId;

	public ExChangeNicknameColorIcon(int itemId)
	{
		this.itemId = itemId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(itemId);
		return true;
	}
}