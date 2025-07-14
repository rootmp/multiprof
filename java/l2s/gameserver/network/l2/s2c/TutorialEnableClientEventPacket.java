package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class TutorialEnableClientEventPacket implements IClientOutgoingPacket
{
	private int _event = 0;

	public TutorialEnableClientEventPacket(int event)
	{
		_event = event;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_event);
		return true;
	}
}