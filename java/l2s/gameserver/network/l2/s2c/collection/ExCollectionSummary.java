package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExCollectionSummary implements IClientOutgoingPacket
{
	public ExCollectionSummary()
	{
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0);
		// int count = 0;
		// packetWriter.writeD(count); // count
		// for (int i = 0; i < count; i++)
		// {
		// packetWriter.writeH(0); // collection id
		// packetWriter.writeD(0); // expiration time in seconds
		// }
		return true;
	}
}