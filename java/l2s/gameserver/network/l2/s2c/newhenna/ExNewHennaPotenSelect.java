package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaPotenSelect implements IClientOutgoingPacket
{
	private final int cSlotID;
	private final int nPotenID;
	private final int nActiveStep;
	private final boolean cSuccess;

	public ExNewHennaPotenSelect(Henna henna, boolean cSuccess)
	{
		this.cSlotID = henna.getSlot();
		this.nPotenID = henna.getPotentialId();
		this.nActiveStep = henna.getActiveStep();
		this.cSuccess = cSuccess;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cSlotID);
		packetWriter.writeD(nPotenID);
		packetWriter.writeH(nActiveStep);
		packetWriter.writeC(cSuccess);
		return true;
	}
}
