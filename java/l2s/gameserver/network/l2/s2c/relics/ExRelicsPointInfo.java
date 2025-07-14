package l2s.gameserver.network.l2.s2c.relics;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.relics.RelicsPointInfo;

public class ExRelicsPointInfo implements IClientOutgoingPacket
{
	private List<RelicsPointInfo> infos;

	public ExRelicsPointInfo()
	{
		System.out.println("NOTDONE " + this.getClass().getSimpleName());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(infos.size());
		for(RelicsPointInfo info : infos)
		{
			packetWriter.writeD(info.nGrade);
			packetWriter.writeD(info.nCurrentCount);
			packetWriter.writeD(info.nOneTimeCount);
			packetWriter.writeD(info.nMaxCount);
		}
		return true;
	}
}
