package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExCuriousHouseState implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket IDLE = new ExCuriousHouseState(0x00);
	public static final IClientOutgoingPacket INVITE = new ExCuriousHouseState(0x01);
	public static final IClientOutgoingPacket PREPARE = new ExCuriousHouseState(0x02);

	private int _state;

	public ExCuriousHouseState(int state)
	{
		_state = state;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_state);
		return true;
	}
}
