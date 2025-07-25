package l2s.gameserver.network.l2.s2c.timerestrictfield;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExTimeRestrictFieldUserExit implements IClientOutgoingPacket
{
	private final int _fieldId;

	public ExTimeRestrictFieldUserExit(int fieldId)
	{
		_fieldId = fieldId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_fieldId);
		return true;
	}
}