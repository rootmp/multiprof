package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * Reworked: VISTALL
 */
public class AcquireSkillDonePacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new AcquireSkillDonePacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}