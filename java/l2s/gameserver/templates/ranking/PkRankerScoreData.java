package l2s.gameserver.templates.ranking;

import l2s.gameserver.model.Player;

public class PkRankerScoreData
{
	public int nCharId;
	public String sUserName;
	public String sPledgeName;
	public int nLevel;
	public int nClassId;
	public int nRace;
	
	public int nClassRank;
	public int nRaceRank;
	public int nServerRank;
	public int nScore;
	
	public PkRankerScoreData(int nCharId, String sUserName, String sPledgeName, int nLevel, int nClassId, int nRace, int nClassRank, int nRaceRank, int nServerRank, int nScore)
	{
		this.nCharId = nCharId;
		this.sUserName = sUserName;
		this.sPledgeName = sPledgeName;
		this.nLevel = nLevel;
		this.nClassId = nClassId;
		this.nRace = nRace;
		this.nClassRank = nClassRank;
		this.nRaceRank = nRaceRank;
		this.nServerRank = nServerRank;
		this.nScore = nScore;
	}

	public PkRankerScoreData(Player player)
	{
		nCharId = player.getObjectId();
		sUserName = player.getName();
		sPledgeName = player.getClan() != null ? player.getClan().getName() : "";
		nLevel = player.getLevel();
		nRace = player.getClassId().getRace().getId();
		nClassRank = 0;
		nRaceRank = 0;
		nServerRank = 0;
		nScore = 0;
	}

	public void setSUserName(String sUserName)
	{
		this.sUserName = sUserName;
	}

	public void setSPledgeName(String sPledgeName)
	{
		this.sPledgeName = sPledgeName;
	}
	
	public void setNLevel(int nLevel)
	{
		this.nLevel = nLevel;
	}

	public void setNClass(int nClass)
	{
		this.nClassRank = nClass;
	}

	public void setNRace(int nRace)
	{
		this.nRaceRank = nRace;
	}

	public void setNRank(int nRank)
	{
		this.nServerRank = nRank;
	}
}
