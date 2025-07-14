package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.QuestHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;

public class RequestQuestAbort implements IClientIncomingPacket
{
	private int _questID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_questID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		Quest quest = QuestHolder.getInstance().getQuest(_questID);
		if(activeChar == null || quest == null)
			return;

		if(!quest.canAbortByPacket())
			return;

		QuestState qs = activeChar.getQuestState(quest);
		if(qs != null && !qs.isCompleted())
		{
			if(!qs.abortQuest())
				activeChar.sendPacket(SystemMsg.THIS_QUEST_CANNOT_BE_DELETED);
		}
	}
}