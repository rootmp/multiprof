package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExCollectionCloseUI implements IClientOutgoingPacket
{
	public ExCollectionCloseUI()
	{
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(-56); // unknown one byte
		return true;
	}
}