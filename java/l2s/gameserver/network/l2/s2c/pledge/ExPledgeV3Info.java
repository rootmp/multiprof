package l2s.gameserver.network.l2.s2c.pledge;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author Eden
 */
public class ExPledgeV3Info implements IClientOutgoingPacket
{
	private final int points, rank;
	private final String announce;
	private final boolean isShowOnEnter;

	public ExPledgeV3Info(int points, int rank, String announce, boolean isShowOnEnter)
	{
		this.points = points;
		this.rank = rank;
		this.announce = announce;
		this.isShowOnEnter = isShowOnEnter;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(points);
		packetWriter.writeD(rank);
		packetWriter.writeSizedString(announce == null ? "" : announce);
		packetWriter.writeC(isShowOnEnter);
		return true;
	}
}