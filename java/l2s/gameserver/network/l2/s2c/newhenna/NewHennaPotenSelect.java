package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class NewHennaPotenSelect implements IClientOutgoingPacket
{
	private final int _slotId;
	private final int _potenId;
	private final int _activeStep;
	private final boolean _success;

	public NewHennaPotenSelect(int slotId, int potenId, int activeStep, boolean success)
	{

		_slotId = slotId;
		_potenId = potenId;
		_activeStep = activeStep;
		_success = success;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_slotId);
		packetWriter.writeD(_potenId);
		packetWriter.writeH(_activeStep);
		packetWriter.writeC(_success ? 1 : 0);
		return true;
	}
}
