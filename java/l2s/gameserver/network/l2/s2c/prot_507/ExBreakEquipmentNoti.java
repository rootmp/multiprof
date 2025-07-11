package l2s.gameserver.network.l2.s2c.prot_507;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExBreakEquipmentNoti implements IClientOutgoingPacket
{
	private int nBreakCount;

	public ExBreakEquipmentNoti(int nBreakCount)
	{
		this.nBreakCount = nBreakCount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBreakCount);
		return true;
	}
}
