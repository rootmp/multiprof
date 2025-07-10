package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.network.l2.s2c.ExBR_LoadEventTopRankers;

public class RequestExBR_EventRankerList extends L2GameClientPacket
{
	private static final String _C__D0_7B_BREVENTRANKERLIST = "[C] D0:7B BrEventRankerList";

	private int _eventId;
	private int _day;
	@SuppressWarnings("unused")
	private int _ranking;

	@Override
	protected boolean readImpl()
	{
		_eventId = readD();
		_day = readD(); // 0 - current, 1 - previous
		_ranking = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		// TODO count, bestScore, myScore
		int count = 0;
		int bestScore = 0;
		int myScore = 0;
		getClient().sendPacket(new ExBR_LoadEventTopRankers(_eventId, _day, count, bestScore, myScore));
	}

	@Override
	public String getType()
	{
		return _C__D0_7B_BREVENTRANKERLIST;
	}

}