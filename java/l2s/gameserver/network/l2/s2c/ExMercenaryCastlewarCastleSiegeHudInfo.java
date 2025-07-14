package l2s.gameserver.network.l2.s2c;

import java.util.concurrent.TimeUnit;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.entity.events.impl.CastleSiegeEvent;

public class ExMercenaryCastlewarCastleSiegeHudInfo implements IClientOutgoingPacket
{
	private static final int PREPARE_STATUS = 0;
	private static final int IN_PROGRESS_STATUS = 1;
	private static final int DONE_STATUS = 2;

	private final int castleId;
	private final int status;
	private final int currentTime;
	private final int prepareLeftTime;

	public ExMercenaryCastlewarCastleSiegeHudInfo(CastleSiegeEvent castleSiegeEvent)
	{
		castleId = castleSiegeEvent.getResidence().getId();
		status = castleSiegeEvent.isInProgress() ? IN_PROGRESS_STATUS : (!castleSiegeEvent.isRegistrationOver() ? PREPARE_STATUS : DONE_STATUS);
		currentTime = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		if(status == PREPARE_STATUS)
		{
			prepareLeftTime = (int) TimeUnit.MILLISECONDS.toSeconds(castleSiegeEvent.getSiegeDate().getTimeInMillis() - System.currentTimeMillis());
		}
		else if(status == IN_PROGRESS_STATUS)
		{
			prepareLeftTime = (int) (TimeUnit.MINUTES.toSeconds(60)
					+ (int) TimeUnit.MILLISECONDS.toSeconds(castleSiegeEvent.getSiegeDate().getTimeInMillis() - System.currentTimeMillis()));
		}
		else
		{
			prepareLeftTime = (int) TimeUnit.MILLISECONDS.toSeconds(castleSiegeEvent.getSiegeDate().getTimeInMillis() - System.currentTimeMillis());
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(castleId);
		packetWriter.writeD(status);
		packetWriter.writeD(currentTime);
		packetWriter.writeD(prepareLeftTime);
		return true;
	}
}
