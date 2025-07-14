package l2s.gameserver.network.l2.c2s.quest;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExQuestTeleport implements IClientIncomingPacket
{
	private int ID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		ID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		/*	Quest quest = QuestHolder.getInstance().getQuest(ID);
			if(quest!=null && quest.checkStartCondition(activeChar))
				quest.QuestTeleport(activeChar);*/
	}
}