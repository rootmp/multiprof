package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubEventManager;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.GameClient;

public class RequestTutorialPassCmdToServer implements IClientIncomingPacket
{
	// format: cS
	private String _bypass = null;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_bypass = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		player.isntAfk();
		if(player.isInFightClub())
		{
			FightClubEventManager.getInstance().requestEventPlayerMenuBypass(player, this._bypass);
			return;
		}
		for(QuestState qs : player.getAllQuestsStates())
		{
			qs.getQuest().notifyTutorialEvent("BYPASS", false, _bypass, qs);
		}
	}
}