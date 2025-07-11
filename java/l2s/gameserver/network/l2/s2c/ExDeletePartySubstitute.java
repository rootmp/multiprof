package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author monithly
 */
public class ExDeletePartySubstitute implements IClientOutgoingPacket
{
	private final int _obj;

	public ExDeletePartySubstitute(final int objectId)
	{
		_obj = objectId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_obj);
		return true;
	}
}
