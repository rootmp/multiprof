package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import l2s.gameserver.Config;

/**
 * @author Bonux
 **/
public class ExEnterWorldPacket implements IClientOutgoingPacket
{
	private final int _serverTime;
	private final int _utcTimeDiff;

	public ExEnterWorldPacket()
	{
		long currentTimeMillis = System.currentTimeMillis();
		_serverTime = (int) TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis);
		TimeZone timeZone = TimeZone.getDefault();
		_utcTimeDiff = (int) -TimeUnit.MILLISECONDS.toSeconds(timeZone.getRawOffset() + (timeZone.inDaylightTime(new Date(currentTimeMillis)) ? timeZone.getDSTSavings() : 0));
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_serverTime);
		packetWriter.writeD(_utcTimeDiff); // Time diff in minutes to UTC
		packetWriter.writeD(0x00); // UNK
		packetWriter.writeD(Config.GATEKEEPER_FREE); // UNK
	}
}