package l2s.gameserver.network.l2.s2c.prot_507;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExClassChangeUiOpen implements IClientOutgoingPacket
{
	private List<Integer> prevClassList;

	public ExClassChangeUiOpen(List<Integer> prevClassList)
	{
		this.prevClassList = prevClassList;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(prevClassList.size());
		for(int prevClass : prevClassList)
		{
			packetWriter.writeD(prevClass);
		}
		return true;
	}
}
