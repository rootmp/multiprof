package l2s.gameserver.network.l2.s2c.prot_507;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExHpEffect implements IClientOutgoingPacket
{
	private int nSID;
	private int nMaxHPBlockPer;


	public ExHpEffect(int nSID, int nMaxHPBlockPer)
	{
		this.nSID = nSID;
		this.nMaxHPBlockPer = nMaxHPBlockPer;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nSID);
		packetWriter.writeH(nMaxHPBlockPer);
		return true;
	}
}
