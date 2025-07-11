package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExOlympiadMatchMakingResult implements IClientOutgoingPacket
{
	public final boolean join;
	public final int type;

	public ExOlympiadMatchMakingResult(boolean join, int type)
	{
		this.join = join;
		this.type = type;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(join);
		packetWriter.writeC(type);
		return true;
	}
}
