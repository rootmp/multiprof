package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class JoinPledgePacket implements IClientOutgoingPacket
{
	private int _pledgeId;

	public JoinPledgePacket(int pledgeId)
	{
		_pledgeId = pledgeId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_pledgeId);
		return true;
	}
}