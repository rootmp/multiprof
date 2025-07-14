package l2s.gameserver.network.l2.s2c;

import java.util.List;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.relics.RelicsProb;

public class ExRelicsProbList implements IClientOutgoingPacket
{
	private int Type;
	private int Key;
	private List<RelicsProb> Relics;

	public ExRelicsProbList(int type, int key, List<RelicsProb> relics)
	{
		Type = type;
		Key = key;
		Relics = relics;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(Type);// Type
		packetWriter.writeD(Key);// Key
		packetWriter.writeD(Relics.size());

		for (RelicsProb relic : Relics)
		{
			packetWriter.writeD(relic.nRelicsID);// nRelicsID
			packetWriter.writeQ(relic.nProb);// nProb
		}
		return true;
	}
}
