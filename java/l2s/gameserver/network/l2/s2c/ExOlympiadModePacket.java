package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExOlympiadModePacket implements IClientOutgoingPacket
{
	// chc
	private int _mode;

	/**
	 * @param _mode (0 = return, 3 = spectate)
	 */
	public ExOlympiadModePacket(int mode)
	{
		_mode = mode;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_mode);
		return true;
	}
}