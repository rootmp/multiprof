package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class NewHennaUnequip implements IClientOutgoingPacket
{
	private final int _slotId;
	private final int _success;

	public NewHennaUnequip(int slotId, int success)
	{
		_slotId = slotId;
		_success = success;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_slotId);
		packetWriter.writeC(_success);
		return true;
	}
}