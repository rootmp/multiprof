package l2s.gameserver.network.l2.s2c;

import java.util.concurrent.TimeUnit;

import l2s.gameserver.model.entity.events.impl.FortressSiegeEvent;

/**
 * @author nexvill
 */
public class ExAdenFortressSiegeHUDInfo extends L2GameServerPacket
{
	private static final int PREPARE_STATUS = 0;
	private static final int IN_PROGRESS_STATUS = 1;
	private static final int DONE_STATUS = 2;

	private final int fortressId;
	private final int status;
	private final int currentTime;
	private final int prepareLeftTime;

	public ExAdenFortressSiegeHUDInfo(FortressSiegeEvent fortressSiegeEvent)
	{
		fortressId = fortressSiegeEvent.getResidence().getId();
		status = fortressSiegeEvent.isInProgress() ? IN_PROGRESS_STATUS : (!fortressSiegeEvent.isRegistrationOver() ? PREPARE_STATUS : DONE_STATUS);
		currentTime = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		if (status == PREPARE_STATUS)
		{
			prepareLeftTime = (int) TimeUnit.MILLISECONDS.toSeconds(fortressSiegeEvent.getSiegeDate().getTimeInMillis() - System.currentTimeMillis());
		}
		else if (status == IN_PROGRESS_STATUS)
		{
			prepareLeftTime = (int) (TimeUnit.MINUTES.toSeconds(30) + (int) TimeUnit.MILLISECONDS.toSeconds(fortressSiegeEvent.getSiegeDate().getTimeInMillis() - System.currentTimeMillis()));
		}
		else
		{
			prepareLeftTime = (int) TimeUnit.MILLISECONDS.toSeconds(fortressSiegeEvent.getSiegeDate().getTimeInMillis() - System.currentTimeMillis());
		}
	}

	@Override
	protected void writeImpl()
	{
		writeD(fortressId);
		writeD(status);
		writeD(currentTime);
		writeD(prepareLeftTime);
	}
}
