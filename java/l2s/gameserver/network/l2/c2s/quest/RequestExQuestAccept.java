package l2s.gameserver.network.l2.c2s.quest;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExQuestAccept implements IClientIncomingPacket
{
	private int questId;
	private boolean bAccept;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		questId = packet.readD();
		bAccept = packet.readC() == 1;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		/*	if(bAccept)
			{
				QuestState qs = activeChar.getQuestState(questId);
				if(qs == null)
				{
					Quest quest = QuestHolder.getInstance().getQuest(questId);
					if(quest != null)
					{
						if(quest.checkStartCondition(activeChar))
						{
							NewQuestData q_data = DatParser.getInstance().getNewQuestData().get(questId);
							if(q_data.start_item >0)
								ItemFunctions.deleteItem(activeChar, q_data.start_item, 1,"ExQuestAccept");
							
							if(quest.getStartNpc() != 0 && activeChar.getQuestState(quest.getId()) == null && activeChar.getAroundNpc(quest.getStartNpc(), 200, 200).size() == 0)
								activeChar.sendPacket(new ExQuestDialog(questId,1));
							else
							{
								qs = quest.newQuestState(activeChar);
								qs.setCond(1);
								quest.questAccept(qs);
							}
						}
					}
					else
						System.out.println("quest == null");
				}
			}*/
	}
}