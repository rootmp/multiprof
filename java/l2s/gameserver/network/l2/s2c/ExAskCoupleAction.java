package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExAskCoupleAction implements IClientOutgoingPacket
{
	private int _objectId, _socialId;

	public ExAskCoupleAction(int objectId, int socialId)
	{
		_objectId = objectId;
		_socialId = socialId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_socialId);
		packetWriter.writeD(_objectId);
		return true;
	}
}
