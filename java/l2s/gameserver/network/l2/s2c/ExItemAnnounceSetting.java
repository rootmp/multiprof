package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class ExItemAnnounceSetting implements IClientOutgoingPacket
{
	private final boolean _Anonymity;

	public ExItemAnnounceSetting(Player player)
	{
		_Anonymity = player.isAnonymity();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_Anonymity);
		return true;
	}
}