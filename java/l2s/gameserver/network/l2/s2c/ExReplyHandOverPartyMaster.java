package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 * @date 20:34/01.09.2011
 */
public class ExReplyHandOverPartyMaster implements IClientOutgoingPacket
{
	public static final L2GameServerPacket TRUE = new ExReplyHandOverPartyMaster(true);
	public static final L2GameServerPacket FALSE = new ExReplyHandOverPartyMaster(false);

	private boolean _isLeader;

	public ExReplyHandOverPartyMaster(boolean leader)
	{
		_isLeader = leader;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_isLeader);
		return true;
	}
}
