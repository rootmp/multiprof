package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExActivateAutoShortcut implements IClientOutgoingPacket
{
	private final int _slot;
	private final boolean _active;

	public ExActivateAutoShortcut(int slot, int page, boolean active)
	{
		_slot = (page * 12) + slot;
		_active = active;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_slot);
		packetWriter.writeC(_active);
		return true;
	}
}
