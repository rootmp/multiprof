package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class NewHennaPotenEnchant implements IClientOutgoingPacket
{
	private final int _slotId;
	private final int _enchantStep;
	private final int _enchantExp;
	private final int _dailyStep;
	private final int _dailyCount;
	private final int _activeStep;
	private final boolean _success;
	
	public NewHennaPotenEnchant(int slotId, int enchantStep, int enchantExp, int dailyStep, int dailyCount, int activeStep, boolean success)
	{
		_slotId = slotId;
		_enchantStep = enchantStep;
		_enchantExp = enchantExp;
		_dailyStep = dailyStep;
		_dailyCount = dailyCount;
		_activeStep = activeStep-1;
		_success = success;
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_slotId);
		packetWriter.writeH(_enchantStep);
		packetWriter.writeD(_enchantExp);
		packetWriter.writeH(_dailyStep);
		packetWriter.writeH(_dailyCount);
		packetWriter.writeH(_activeStep);
		packetWriter.writeC(_success ? 1 : 0);
		//TODO 388
		packetWriter.writeH(0); //nSlotDailyStep
		packetWriter.writeH(0);//nSlotDailyStep
		return true;
	}
}
