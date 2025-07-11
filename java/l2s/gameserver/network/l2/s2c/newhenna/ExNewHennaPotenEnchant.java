package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaPotenEnchant implements IClientOutgoingPacket
{
	private final int cSlotID;
	private final int nEnchantStep;
	private final int nEnchantExp;
	private final int nDailyStep;
	private final int nDailyCount;
	private final int nActiveStep;
	private final boolean cSuccess;

	public ExNewHennaPotenEnchant(Player player, Henna henna, boolean cSuccess)
	{
		cSlotID = henna.getSlot();
		nEnchantStep = henna.getEnchantStep();
		nEnchantExp = henna.getCurrentEnchantExp();
		nDailyStep = player.getHennaList().getHiddenPowerStep();
		nDailyCount = player.getHennaList().getHiddenPowerLeft();
		nActiveStep = henna.getActiveStep();
		this.cSuccess = cSuccess;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cSlotID);
		packetWriter.writeH(nEnchantStep);
		packetWriter.writeD(nEnchantExp);
		packetWriter.writeH(nDailyStep);
		packetWriter.writeH(nDailyCount);
		packetWriter.writeH(nActiveStep);
		packetWriter.writeC(cSuccess);
		return true;
	}
}
