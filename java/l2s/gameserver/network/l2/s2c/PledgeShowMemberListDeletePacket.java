package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class PledgeShowMemberListDeletePacket implements IClientOutgoingPacket
{
	private String _player;

	public PledgeShowMemberListDeletePacket(String playerName)
	{
		_player = playerName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_player);
		return true;
	}
}