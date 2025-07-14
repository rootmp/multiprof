package l2s.gameserver.templates.ranking;

import l2s.gameserver.model.Player;

public class OlympiadRankInfo
{
	public int nCharId;

	public String sCharName;
	public String sPledgeName;
	public int nSex;
	public int nRank;
	public int nElectionRank;
	public int nElectionMatchCount;
	public int nClassRank;
	public int nPrevRank;
	public int nWorldID;
	public int nLevel;
	public int nClassID;
	public int nPledgeLevel;
	public int nWinCount;
	public int nLoseCount;
	public int nDrawCount;
	public int nOlympiadPoint;
	public int nHeroCount;
	public int nLegendCount;

	public OlympiadRankInfo(Player player)
	{
		sCharName = player.getName();
		sPledgeName = player.getClan() != null ? player.getClan().getName() : "";
		nSex = player.getSex().ordinal();
		nLevel = player.getLevel();
		nClassID = player.getClassId().getId();
		nPledgeLevel = player.getClan() != null ? player.getClan().getLevel() : 0;
	}

	public OlympiadRankInfo()
	{}

	@Override
	public String toString()
	{
		return "OlympiadRankInfo{" +
				"nCharId=" + nCharId +
				", sCharName='" + sCharName + '\'' +
				", sPledgeName='" + sPledgeName + '\'' +
				", nSex=" + nSex +
				", nRank=" + nRank +
				", nElectionRank=" + nElectionRank +
				", nClassRank=" + nClassRank +
				", nPrevRank=" + nPrevRank +
				", nWorldID=" + nWorldID +
				", nLevel=" + nLevel +
				", nClassID=" + nClassID +
				", nPledgeLevel=" + nPledgeLevel +
				", nWinCount=" + nWinCount +
				", nLoseCount=" + nLoseCount +
				", nDrawCount=" + nDrawCount +
				", nOlympiadPoint=" + nOlympiadPoint +
				", nHeroCount=" + nHeroCount +
				", nLegendCount=" + nLegendCount +
				'}';
	}
}
