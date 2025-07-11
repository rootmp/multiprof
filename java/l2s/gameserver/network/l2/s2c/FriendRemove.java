package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class FriendRemove implements IClientOutgoingPacket
{
	private final String _friendName;

	public FriendRemove(String name)
	{
		_friendName = name;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(1); // UNK
		packetWriter.writeS(_friendName); // FriendName
		return true;
	}
}
