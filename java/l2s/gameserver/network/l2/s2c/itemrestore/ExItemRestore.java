package l2s.gameserver.network.l2.s2c.itemrestore;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

public class ExItemRestore implements IClientOutgoingPacket
{
	public static ExItemRestore FAIL = new ExItemRestore(0);
	public static ExItemRestore SUCCESS = new ExItemRestore(1);

	private final int cResult;

	private ExItemRestore(int cResult)
	{
		this.cResult = cResult;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cResult);
		return true;
	}
}