package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExUnionPoint implements IClientOutgoingPacket
{
	private final int _clanId;

	public ExUnionPoint(int clanId)
	{
		_clanId = clanId;

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_clanId);
		return true;
	}
}
