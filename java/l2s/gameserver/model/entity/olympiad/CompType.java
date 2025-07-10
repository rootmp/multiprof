package l2s.gameserver.model.entity.olympiad;

public enum CompType
{
	// TODO: Вынести в конфиги.
	TEAM(0, 0, 0, 10, 0, 0),
	NON_CLASSED(0, 0, 0, 10, 0, 0),
	CLASSED(0, 0, 0, 10, 0, 0);

	private final int rewardId;
	private final int winnerReward;
	private final int looserReward;
	private final int winnerPoints;
	private final int loosePoints;
	private final int tiePoints;

	CompType(int rewardId, int winnerReward, int looserReward, int winnerPoints, int loosePoints, int tiePoints)
	{
		this.rewardId = rewardId;
		this.winnerReward = winnerReward;
		this.looserReward = looserReward;
		this.winnerPoints = winnerPoints;
		this.loosePoints = loosePoints;
		this.tiePoints = tiePoints;
	}

	public int getRewardId()
	{
		return rewardId;
	}

	public int getWinnerReward()
	{
		return winnerReward;
	}

	public int getLooserReward()
	{
		return looserReward;
	}

	public int getWinnerPoints()
	{
		return winnerPoints;
	}

	public int getLoosePoints()
	{
		return loosePoints;
	}

	public int getTiePoints()
	{
		return tiePoints;
	}
}