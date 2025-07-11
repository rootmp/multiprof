package l2s.gameserver.network.l2.c2s.subjugation;

import l2s.gameserver.data.xml.holder.SubjugationsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationGacha;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationSidebar;

/**
 * @author nexvill
 */
public class RequestExSubjugationGacha implements IClientIncomingPacket
{
	int _zoneId, _count;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_zoneId = packet.readD(); // zone id
		_count = packet.readD(); // keys used
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		int points = player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + _zoneId, 0);
		int keysHave = points / 1_000_000;
		points -= (_count * 1_000_000);
		int keysUsed = player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_KEYS_USED, 0);

		if ((keysHave < 0) || (points < 0) || (keysUsed >= SubjugationsHolder.getInstance().getFields().get(_zoneId).getMaximumKeys()))
		{
			return;
		}
		player.setVar(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + _zoneId, points);
		player.setVar(PlayerVariables.SUBJUGATION_ZONE_KEYS_USED, keysUsed + _count);
		player.sendPacket(new ExSubjugationGacha(player, _zoneId, _count));
		player.sendPacket(new ExSubjugationSidebar(player));
		player.getListeners().onPurgeReward(_zoneId);
	}
}