package l2s.gameserver.network.l2.s2c.quest;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExQuestNotificationAll implements IClientOutgoingPacket
{
	private QuestState[] _allActiveQuestsStates;

	public ExQuestNotificationAll(QuestState[] allActiveQuestsStates)
	{
		_allActiveQuestsStates = allActiveQuestsStates;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_allActiveQuestsStates.length);//size
		for(QuestState q : _allActiveQuestsStates)
		{
			packetWriter.writeD(q.getQuest().getId());//id
			packetWriter.writeD(q.getCond() - 1);//count
		}
		return true;
	}
}