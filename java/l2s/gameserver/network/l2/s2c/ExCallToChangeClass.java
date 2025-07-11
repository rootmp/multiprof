package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author : Ragnarok
 * @date : 28.03.12 16:23
 */
public class ExCallToChangeClass implements IClientOutgoingPacket
{
	private int _classId;
	private boolean _showMsg;

	public ExCallToChangeClass(int classId, boolean showMsg)
	{
		_classId = classId;
		_showMsg = showMsg;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_classId); // New Class Id
		packetWriter.writeD(_showMsg); // Show Message
		packetWriter.writeD(0x00); // unk
		return true;
	}
}
