package l2s.gameserver.network.l2.s2c;

import java.util.concurrent.TimeUnit;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.ServerVariables;

/**
 * @author nexvill
 */
public class ExRankingCharBuffzoneNpcInfo implements IClientOutgoingPacket
{

	public ExRankingCharBuffzoneNpcInfo()
	{
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		long minsToReset = ServerVariables.getLong("buffNpcReset", 0);
		long diff = minsToReset - System.currentTimeMillis();
		if (diff <= 0)
		{
			packetWriter.writeD(0);
		}
		else
		{
			packetWriter.writeD((int) TimeUnit.MILLISECONDS.toSeconds(diff));
		}

		return true;
	}
}
