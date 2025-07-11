package l2s.gameserver.network.l2.s2c.prot_507;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRepairAllEquipment implements IClientOutgoingPacket
{
	private boolean cResult;

	public ExRepairAllEquipment(boolean cResult)
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
