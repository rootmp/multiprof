package l2s.gameserver.network.l2.s2c.pets;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExHidePetExtractSystem implements IClientOutgoingPacket
{

	public ExHidePetExtractSystem()
	{

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0xFE); // dummy
		return true;
	}
}