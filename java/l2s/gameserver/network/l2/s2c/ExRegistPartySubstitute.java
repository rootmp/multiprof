package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExRegistPartySubstitute implements IClientOutgoingPacket
{
	private final int _object;

	public ExRegistPartySubstitute(int obj)
	{
		_object = obj;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_object);
		packetWriter.writeD(0x01);
		return true;
	}
}
