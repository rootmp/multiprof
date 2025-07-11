package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.templates.item.RecipeTemplate;

public class RecipeItemMakeInfoPacket implements IClientOutgoingPacket
{
	private final int _id;
	private final boolean _isCommon;
	private final int _status;
	private final int _curMP;
	private final int _maxMP;
	private final int chanceBonus;
	private final boolean canCrit;
	private final int critChance;

	public RecipeItemMakeInfoPacket(Player player, RecipeTemplate recipe, int status)
	{
		_id = recipe.getId();
		_isCommon = recipe.isCommon();
		_status = status;
		_curMP = (int) player.getCurrentMp();
		_maxMP = player.getMaxMp();
		chanceBonus = (int) player.getStat().calc(Stats.CRAFT_CHANCE, 0);
		critChance = (int) player.getStat().calc(Stats.CRIT_CRAFT_CHANCE, 0);
		canCrit = critChance > 0 && recipe.canCrit();

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_id); // ID рецепта
		packetWriter.writeD(_isCommon ? 0x01 : 0x00);
		packetWriter.writeD(_curMP);
		packetWriter.writeD(_maxMP);
		packetWriter.writeD(_status); // итог крафта; 0xFFFFFFFF нет статуса, 0 удача, 1 провал
		packetWriter.writeF(0x00); // Add Adena rate
		packetWriter.writeC(true); // always true
		packetWriter.writeF(chanceBonus); // Craft chance bonus
		packetWriter.writeC(canCrit); // Critical craft available
		packetWriter.writeF(critChance); // Critical craft chance
		return true;
	}
}