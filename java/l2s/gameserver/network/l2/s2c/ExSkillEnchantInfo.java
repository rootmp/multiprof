package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.skill.enchant.SkillEnchantInfo;

public class ExSkillEnchantInfo implements IClientOutgoingPacket
{
	private SkillEnchantInfo _enchant;

	public ExSkillEnchantInfo(SkillEnchantInfo enchant)
	{
		_enchant = enchant;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_enchant.getSkillID());
		packetWriter.writeD(_enchant.getSubLevel());
		packetWriter.writeD(_enchant.getEXP());
		packetWriter.writeD(_enchant.getMaxEXP());
		packetWriter.writeD(_enchant.getChance());

		packetWriter.writeH(14);
		packetWriter.writeD(57);
		packetWriter.writeQ(1000000);
		return true;
	}
}