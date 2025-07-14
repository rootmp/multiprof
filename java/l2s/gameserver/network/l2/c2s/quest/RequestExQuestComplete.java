package l2s.gameserver.network.l2.c2s.quest;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExQuestComplete implements IClientIncomingPacket
{
	private int nID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		/*	QuestState questState = activeChar.getQuestState(nID);
			if(questState!=null)
				questState.getQuest().QuestComplete(questState);*/
	}
}