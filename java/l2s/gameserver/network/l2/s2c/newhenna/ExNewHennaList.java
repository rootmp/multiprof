package l2s.gameserver.network.l2.s2c.newhenna;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaList implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(nDailyStep);
		packetWriter.writeH(nDailyCount);
		packetWriter.writeD(hennaInfoList.size());
		for (Henna info : hennaInfoList)
		{
			packetWriter.writeD(info.getId());
			packetWriter.writeD(info.getPotentialId());
			packetWriter.writeC(info.isActive());
			packetWriter.writeH(info.getEnchantStep());
			packetWriter.writeD(info.getCurrentEnchantExp());
			packetWriter.writeH(info.getActiveStep());
		}
	}
}
