package l2s.gameserver.network.l2.c2s.quest;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.quest.ExQuestNotificationAll;
import l2s.gameserver.network.l2.s2c.quest.ExQuestUi;

public class RequestExQuestCancel implements IClientIncomingPacket
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
		QuestState qs = activeChar.getQuestState(nID);
		if(qs!=null)
		{
			qs.abortQuest();
			activeChar.sendPacket(new ExQuestNotificationAll(activeChar.getAllActiveQuestsStates()));
			activeChar.sendPacket(new ExQuestUi(activeChar));
		}
	}
}