package l2s.gameserver.network.l2.s2c;

import java.util.Map;
import java.util.Map.Entry;

public class ExPVPMatchCCRecord extends L2GameServerPacket
{
	private final Map<String, Integer> _scores;

	public ExPVPMatchCCRecord(Map<String, Integer> scores)
	{
		_scores = scores;
	}

	public void writeImpl()
	{
		writeD(0x00); // Open/Dont Open
		writeD(_scores.size());
		for (Entry<String, Integer> p : _scores.entrySet())
		{
			writeS(p.getKey());
			writeD(p.getValue());
		}
	}
}
