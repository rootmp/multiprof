package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.entity.events.impl.DuelEvent;

public class ExDuelReady implements IClientOutgoingPacket
{
	private int _duelType;

	public ExDuelReady(DuelEvent event)
	{
		_duelType = event.getDuelType();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_duelType);
	}
}