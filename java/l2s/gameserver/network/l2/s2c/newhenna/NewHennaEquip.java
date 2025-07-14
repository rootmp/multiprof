package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class NewHennaEquip implements IClientOutgoingPacket
{
	private final int _slotId;
	private final int _hennaId;
	private final boolean _success;

	public NewHennaEquip(int slotId, int hennaId, boolean success)
	{
		_slotId = slotId;
		_hennaId = hennaId;
		_success = success;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_slotId);
		packetWriter.writeD(_hennaId);
		packetWriter.writeD(_success ? 1 : 0);
		return true;
	}
}
