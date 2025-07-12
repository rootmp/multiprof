package l2s.gameserver.network.l2.s2c.quest;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.QuestHolder;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExQuestAcceptableList implements IClientOutgoingPacket
{
	//private List<NewQuestData> newQuestData = new ArrayList<>();
	
	public ExQuestAcceptableList(Player player)
	{
 	/*	for(NewQuestData quest : DatParser.getInstance().getNewQuestData().values())
		{
			if(player.getQuestState(quest.quest_id)!=null)
				continue;
			
			Quest _quest = QuestHolder.getInstance().getQuest(quest.quest_id);
			if(_quest !=null)
			{
				if(_quest.checkStartCondition(player))
					newQuestData.add(quest);
			}
		}*/
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
	/*	packetWriter.writeD(newQuestData.size());//size questIDs
		for(NewQuestData q:newQuestData)
			packetWriter.writeD(q.quest_id);//questIDs*/
		return true;
	}
}