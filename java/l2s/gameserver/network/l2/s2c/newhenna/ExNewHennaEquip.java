package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaEquip implements IClientOutgoingPacket
{
	private final int cSlotID;
	private final int nHennaID;
	private final boolean cSuccess;

	public ExNewHennaEquip(int cSlotID, int nHennaID)
	{
		this.cSlotID = cSlotID;
		this.nHennaID = nHennaID;
		this.cSuccess = true;
	}

	public ExNewHennaEquip(int cSlotID)
	{
		this.cSlotID = cSlotID;
		this.nHennaID = 0;
		this.cSuccess = false;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cSlotID);
		packetWriter.writeD(nHennaID);
		packetWriter.writeC(cSuccess);
		return true;
	}
}
