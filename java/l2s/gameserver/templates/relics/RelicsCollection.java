package l2s.gameserver.templates.relics;

import java.util.List;

public class RelicsCollection
{
	public int nCollectionID;
	public byte bComplete;
	public List<CollectionRelicsInfo> relicsList;

	public RelicsCollection(int nCollectionID, byte bComplete, List<CollectionRelicsInfo> relicsList)
	{
		this.nCollectionID = nCollectionID;
		this.bComplete = bComplete;
		this.relicsList = relicsList;
	}
}
