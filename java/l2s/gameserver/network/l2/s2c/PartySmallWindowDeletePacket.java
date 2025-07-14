package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class PartySmallWindowDeletePacket implements IClientOutgoingPacket
{
	private final int _objId;
	private final String _name;

	public PartySmallWindowDeletePacket(Player member)
	{
		_objId = member.getObjectId();
		_name = member.getName();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objId);
		packetWriter.writeS(_name);
		return true;
	}
}