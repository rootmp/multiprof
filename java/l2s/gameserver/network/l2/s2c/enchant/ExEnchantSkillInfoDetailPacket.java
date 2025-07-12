package l2s.gameserver.network.l2.s2c.enchant;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;


public class ExEnchantSkillInfoDetailPacket implements IClientOutgoingPacket
{
	private final int _skillId;
	private final int _skillLevel;
	private final int _skillSubLevel;

	
	public ExEnchantSkillInfoDetailPacket(int type, int skillId, int skillLevel, int skillSubLevel, Player player)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
		_skillSubLevel = skillSubLevel;

	}
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(1);
		packetWriter.writeD(_skillId);
		packetWriter.writeH(_skillLevel);
		packetWriter.writeH(_skillSubLevel);
		/*
		if (_enchantSkillHolder != null)
		{
			writeLong(_enchantSkillHolder.getSp(_type));
			writeInt(_enchantSkillHolder.getChance(_type));
			final Set<ItemHolder> holders = _enchantSkillHolder.getRequiredItems(_type);
			writeInt(holders.size());
			holders.forEach(holder ->
			{
				writeInt(holder.getId());
				writeInt((int) holder.getCount());
			});
		}
		 */
		return true;
	}

}
