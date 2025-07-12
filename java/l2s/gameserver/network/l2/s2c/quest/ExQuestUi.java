package l2s.gameserver.network.l2.s2c.quest;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExQuestUi implements IClientOutgoingPacket
{
	private int _ProceedingQuestCount;
	private QuestState[] questInfos;

	public ExQuestUi(Player player)
	{
		questInfos = player.getAllQuestsStates();
		_ProceedingQuestCount = player.getAllActiveQuests().length;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(questInfos.length);//size
		for(QuestState qs: questInfos)
		{
			packetWriter.writeD(qs.getQuest().getId());//id
			packetWriter.writeD(qs.getQuestCond());//count
			packetWriter.writeC(qs.isCompleted()?2:1);//state
		}
		packetWriter.writeD(_ProceedingQuestCount);//nProceedingQuestCount
		return true;
	}
}