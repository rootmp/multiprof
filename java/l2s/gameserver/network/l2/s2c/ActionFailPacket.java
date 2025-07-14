package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ActionFailPacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ActionFailPacket();

	private final int _castingType;

	public ActionFailPacket()
	{
		_castingType = 0;
	}

	public ActionFailPacket(int castingType)
	{
		_castingType = castingType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_castingType); // MagicSkillUse castingType
		return true;
	}
}