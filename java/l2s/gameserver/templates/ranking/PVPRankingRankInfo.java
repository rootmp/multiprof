package l2s.gameserver.templates.ranking;

public class PVPRankingRankInfo
{
	public int nCharId;
	public String sCharName;
	public String sPledgeName;
	public int nLevel;
	public int nRace;
	public int nClass;
	public long nPVPPoint;
	public int nRank;
	public int nPrevRank;
	
	public int nRaceRank;
	public int nKillCount;
	public int nDieCount;
	
  public PVPRankingRankInfo(int nCharId, String sCharName, String sPledgeName, int nLevel, int nRace, int nClass, long nPVPPoint, int nRank, int nRaceRank, int nKillCount, int nDieCount) 
  {
    this.nCharId = nCharId;
    this.sCharName = sCharName;
    this.sPledgeName = sPledgeName;
    this.nLevel = nLevel;
    this.nRace = nRace;
    this.nRaceRank = nRaceRank;
    this.nClass = nClass;
    this.nPVPPoint = nPVPPoint;
    this.nRank = nRank;
    this.nPrevRank = nRank;
    this.nKillCount = nKillCount;
    this.nDieCount = nDieCount;
  }
}
