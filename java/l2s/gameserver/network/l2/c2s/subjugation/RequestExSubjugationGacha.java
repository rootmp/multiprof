package l2s.gameserver.network.l2.c2s.subjugation;

import l2s.gameserver.data.xml.holder.SubjugationsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationGacha;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationSidebar;

/**
 * @author nexvill
 */
public class RequestExSubjugationGacha extends L2GameClientPacket
{
	int _zoneId, _count;

	@Override
	protected boolean readImpl()
	{
		_zoneId = readD(); // zone id
		_count = readD(); // keys used
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
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