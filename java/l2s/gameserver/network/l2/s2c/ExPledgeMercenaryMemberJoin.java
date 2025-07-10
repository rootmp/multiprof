package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExPledgeMercenaryMemberJoin implements IClientOutgoingPacket
{
	private final int type;
	private final boolean join;
	private final int playerObjectId;
	private final int clanObjectId;

	public ExPledgeMercenaryMemberJoin(int type, boolean join, int playerObjectId, int clanObjectId)
	{
		this.type = type;
		this.join = join;
		this.playerObjectId = playerObjectId;
		this.clanObjectId = clanObjectId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(type); // type
		packetWriter.writeD(join); // entered
		packetWriter.writeD(playerObjectId);
		packetWriter.writeD(clanObjectId);
	}
}
