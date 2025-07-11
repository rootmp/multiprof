package l2s.gameserver.network.l2.s2c;

import java.util.Map;
import java.util.Map.Entry;

import l2s.commons.network.PacketWriter;

public class ExPVPMatchCCRecord implements IClientOutgoingPacket
{
	private final Map<String, Integer> _scores;

	public ExPVPMatchCCRecord(Map<String, Integer> scores)
	{
		_scores = scores;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00); // Open/Dont Open
		packetWriter.writeD(_scores.size());
		for (Entry<String, Integer> p : _scores.entrySet())
		{
			packetWriter.writeS(p.getKey());
			packetWriter.writeD(p.getValue());
		}
		return true;
	}
}
