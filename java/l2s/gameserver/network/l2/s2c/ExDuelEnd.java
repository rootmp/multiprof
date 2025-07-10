package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.entity.events.impl.DuelEvent;

public class ExDuelEnd implements IClientOutgoingPacket
{
	private int _duelType;

	public ExDuelEnd(DuelEvent e)
	{
		_duelType = e.getDuelType();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_duelType);
	}
}