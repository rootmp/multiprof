package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.stats.Stats;

/**
 * @author Sdw
 */
public class ExVitalityEffectInfo extends L2GameServerPacket
{
	private final int _sayhasGracePoints;
	private final int _bonusPercent;
	private final int _vitalityItemsRemaining;
	private final int _addBonusPercent;

	public ExVitalityEffectInfo(Player player)
	{
		_sayhasGracePoints = player.getSayhasGrace();
		_bonusPercent = (int) (player.getSayhasGraceBonus() * 100.);
		_vitalityItemsRemaining = Config.VITALITY_MAX_ITEMS_ALLOWED - player.getVitalityItemsUsed();
		_addBonusPercent = (int) player.getStat().calc(Stats.VITAL_RATE);
	}

	protected void writeImpl()
	{
		writeD(_sayhasGracePoints);
		writeD(_bonusPercent);
		writeH(_addBonusPercent);
		writeH(_vitalityItemsRemaining);
		writeH(Config.VITALITY_MAX_ITEMS_ALLOWED);
	}
}
