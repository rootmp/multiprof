package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaUnequip implements IClientOutgoingPacket
{
	private final int cSlotID;
	private final boolean cSuccess;

	public ExNewHennaUnequip(int cSlotID, boolean cSuccess)
	{
		this.cSlotID = cSlotID;
		this.cSuccess = cSuccess;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cSlotID);
		packetWriter.writeC(cSuccess);
	}
}
