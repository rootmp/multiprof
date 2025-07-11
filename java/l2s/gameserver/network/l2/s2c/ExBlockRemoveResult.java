package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExBlockRemoveResult implements IClientOutgoingPacket
{
	private final String _blockName;

	public ExBlockRemoveResult(String name)
	{
		_blockName = name;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(1); // UNK
		packetWriter.writeS(_blockName);
		return true;
	}
}
