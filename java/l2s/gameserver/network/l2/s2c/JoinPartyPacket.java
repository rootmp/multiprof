package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * dd dd
 */
public class JoinPartyPacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket SUCCESS = new JoinPartyPacket(1);
	public static final IClientOutgoingPacket FAIL = new JoinPartyPacket(0);

	private int _response;

	public JoinPartyPacket(int response)
	{
		_response = response;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_response);
		return true;
	}
}