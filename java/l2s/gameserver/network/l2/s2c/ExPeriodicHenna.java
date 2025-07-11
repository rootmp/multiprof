package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;

public class ExPeriodicHenna implements IClientOutgoingPacket
{
	private final Henna _henna;
	private final boolean _active;

	public ExPeriodicHenna(Player player)
	{
		_henna = null;
		_active = false;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00); // Premium symbol ID
		packetWriter.writeD(0x00); // Premium symbol left time
		packetWriter.writeD(0x00); // Premium symbol active
		return true;
	}
}
