package l2s.gameserver.network.l2.s2c.newhenna;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaList extends L2GameServerPacket
{
	private final int nDailyStep;
	private final int nDailyCount;
	private final Collection<Henna> hennaInfoList;

	public ExNewHennaList(Player player)
	{
		nDailyStep = player.getHennaList().getHiddenPowerStep();
		nDailyCount = player.getHennaList().getHiddenPowerLeft();
		hennaInfoList = player.getHennaList().values();
	}

	@Override
	protected void writeImpl()
	{
		writeH(nDailyStep);
		writeH(nDailyCount);
		writeD(hennaInfoList.size());
		for (Henna info : hennaInfoList)
		{
			writeD(info.getId());
			writeD(info.getPotentialId());
			writeC(info.isActive());
			writeH(info.getEnchantStep());
			writeD(info.getCurrentEnchantExp());
			writeH(info.getActiveStep());
		}
	}
}
