package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;

public class ExPeriodicHenna extends L2GameServerPacket
{
	private final Henna _henna;
	private final boolean _active;

	public ExPeriodicHenna(Player player)
	{
		_henna = null;
		_active = false;
	}

	@Override
	protected void writeImpl()
	{
		writeD(0x00); // Premium symbol ID
		writeD(0x00); // Premium symbol left time
		writeD(0x00); // Premium symbol active
	}
}
