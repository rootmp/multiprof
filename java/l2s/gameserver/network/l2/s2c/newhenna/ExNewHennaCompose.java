package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaCompose implements IClientOutgoingPacket
{
	private final int nResultHennaID;
	private final int nResultItemID;
	private final boolean cSuccess;

	public ExNewHennaCompose(int nResultHennaID, int nResultItemID, boolean cSuccess)
	{
		this.nResultHennaID = nResultHennaID;
		this.nResultItemID = nResultItemID;
		this.cSuccess = cSuccess;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nResultHennaID);
		packetWriter.writeD(nResultItemID);
		packetWriter.writeC(cSuccess);
		return true;
	}
}
