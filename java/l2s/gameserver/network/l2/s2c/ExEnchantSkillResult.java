package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public final class ExEnchantSkillResult implements IClientOutgoingPacket
{
	public static final ExEnchantSkillResult STATIC_PACKET_TRUE = new ExEnchantSkillResult(false);
	public static final ExEnchantSkillResult STATIC_PACKET_FALSE = new ExEnchantSkillResult(true);

	private final boolean _enchanted;

	public ExEnchantSkillResult(boolean enchanted)
	{
		_enchanted = enchanted;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_enchanted);
		return true;
	}
}