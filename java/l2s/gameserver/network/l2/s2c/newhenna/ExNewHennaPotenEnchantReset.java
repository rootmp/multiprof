package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExNewHennaPotenEnchantReset implements IClientOutgoingPacket
{
	private int _cSuccess;

	public ExNewHennaPotenEnchantReset(int success)
	{
		_cSuccess = success;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_cSuccess);
		return true;
	}
}