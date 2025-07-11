package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class MagicSkillCanceled implements IClientOutgoingPacket
{
	private int _objectId;

	public MagicSkillCanceled(int objectId)
	{
		_objectId = objectId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);
		return true;
	}
}