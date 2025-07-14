package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class ExBalthusEvent implements IClientOutgoingPacket
{
	public ExBalthusEvent(Player player)
	{}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00); // currentState
		packetWriter.writeD(0x00); // progress
		packetWriter.writeD(0x00); // Reward Item ID
		packetWriter.writeD(0x00); // Available Coins count
		packetWriter.writeD(0x00); // Participated
		packetWriter.writeD(0x00); // Running
		return true;
	}
}