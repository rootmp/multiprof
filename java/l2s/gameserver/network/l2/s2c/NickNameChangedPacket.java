package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Creature;

public class NickNameChangedPacket implements IClientOutgoingPacket
{
	private final int objectId;
	private final String title;

	public NickNameChangedPacket(Creature cha)
	{
		objectId = cha.getObjectId();
		title = cha.getTitle();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(objectId);
		packetWriter.writeS(title);
	}
}