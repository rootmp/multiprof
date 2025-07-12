package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExItemAnnounceSetting;

public class RequestExSaveItemAnnounceSetting implements IClientIncomingPacket
{
	private int _Anonymity;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_Anonymity = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		player.setVar(PlayerVariables.ITEM_ANNOUNCE_SETTING, _Anonymity);
		player.sendPacket(new ExItemAnnounceSetting(player));
	}
}