package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

/**
 * @author monithly TODO this
 */
public class ExExchangeSubstitute implements IClientOutgoingPacket
{
	public ExExchangeSubstitute(Player pl, Player pl2)
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00);
		packetWriter.writeQ(3000000L);
		packetWriter.writeD(0x00);
		return true;
	}
}
