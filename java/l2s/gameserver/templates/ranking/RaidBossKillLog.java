package l2s.gameserver.templates.ranking;

public class RaidBossKillLog
{
	private final int rbId;
	private final int clanId;
	private final int killerId;
	private final long time;

	public RaidBossKillLog(int rbId, int clanId, int killerId, long time)
	{
		this.rbId = rbId;
		this.clanId = clanId;
		this.killerId = killerId;
		this.time = time;
	}

	public int getRbId()
	{
		return rbId;
	}

	public int getClanId()
	{
		return clanId;
	}

	public int getKillerId()
	{
		return killerId;
	}

	public long getTime()
	{
		return time;
	}

	@Override
	public String toString()
	{
		return "RaidBossKillLog{rbId=" + rbId + ", clanId=" + clanId + ", killerId=" + killerId + ", time=" + time + "}";
	}
}
