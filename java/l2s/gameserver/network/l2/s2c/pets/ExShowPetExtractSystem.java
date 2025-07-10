package l2s.gameserver.network.l2.s2c.pets;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExShowPetExtractSystem implements IClientOutgoingPacket
{

	public ExShowPetExtractSystem()
	{

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0xFE); // dummy
	}
}