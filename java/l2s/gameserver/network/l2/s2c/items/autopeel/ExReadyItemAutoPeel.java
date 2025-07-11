package l2s.gameserver.network.l2.s2c.items.autopeel;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExReadyItemAutoPeel implements IClientOutgoingPacket
{
	private static int _result;
	private static int _itemObjId;

	public ExReadyItemAutoPeel(int result, int itemObjId)
	{
		_result = result;
		_itemObjId = itemObjId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_result);
		packetWriter.writeD(_itemObjId);
		return true;
	}
}