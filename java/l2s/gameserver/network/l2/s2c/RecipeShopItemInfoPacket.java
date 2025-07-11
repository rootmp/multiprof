package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.data.xml.holder.RecipeHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.stats.Stats;

/**
 * dddddQ
 */
public class RecipeShopItemInfoPacket implements IClientOutgoingPacket
{
	private int _recipeId, _shopId, _curMp, _maxMp;
	private int _success = 0xFFFFFFFF;
	private long _price;
	private final int chanceBonus;
	private final boolean canCrit;
	private final int critChance;

	public RecipeShopItemInfoPacket(Player activeChar, Player manufacturer, int recipeId, long price, int success)
	{
		_recipeId = recipeId;
		_shopId = manufacturer.getObjectId();
		_price = price;
		_success = success;
		_curMp = (int) manufacturer.getCurrentMp();
		_maxMp = manufacturer.getMaxMp();
		chanceBonus = (int) manufacturer.getStat().calc(Stats.CRAFT_CHANCE, 0);
		critChance = (int) manufacturer.getStat().calc(Stats.CRIT_CRAFT_CHANCE, 0);
		canCrit = critChance > 0 && RecipeHolder.getInstance().getRecipeByRecipeId(_recipeId).canCrit();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_shopId);
		packetWriter.writeD(_recipeId);
		packetWriter.writeD(_curMp);
		packetWriter.writeD(_maxMp);
		packetWriter.writeD(_success);
		packetWriter.writeQ(_price);
		packetWriter.writeC(0x00); // Add Adena rate
		packetWriter.writeC(true); // always true
		packetWriter.writeF(chanceBonus); // Craft chance bonus
		packetWriter.writeC(canCrit); // Critical craft available
		packetWriter.writeF(critChance); // Critical craft chance
		return true;
	}
}