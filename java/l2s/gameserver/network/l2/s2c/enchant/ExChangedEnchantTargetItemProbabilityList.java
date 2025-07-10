package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.data.xml.holder.EnchantItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.support.EnchantScroll;
import l2s.gameserver.templates.item.support.EnchantStone;
import l2s.gameserver.templates.item.support.EnchantVariation;
import l2s.gameserver.templates.item.support.EnchantVariation.EnchantLevel;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public class ExChangedEnchantTargetItemProbabilityList extends L2GameServerPacket
{
	private final Player _player;
	private boolean _isMultiEnchant;

	public ExChangedEnchantTargetItemProbabilityList(Player player, boolean isMultiEnchant)
	{
		_player = player;
		_isMultiEnchant = isMultiEnchant;
	}

	@Override
	protected final void writeImpl()
	{
		double supportRate = 0;
		if (!_isMultiEnchant && (_player.getSupportItem() != null))
		{
			EnchantStone stone = ItemFunctions.getEnchantStone(_player.getEnchantItem(), _player.getSupportItem());
			if (stone != null)
			{
				supportRate = stone.getChance() * 100;
			}
		}

		double passiveRate = 0;
		passiveRate += _player.getPremiumAccount().getEnchantChanceBonus();
		passiveRate += _player.getVIP().getTemplate().getEnchantChanceBonus();
		passiveRate *= _player.getEnchantChanceModifier();
		passiveRate *= 100;

		int count = 1;
		if (_isMultiEnchant)
		{
			count = _player.getMultiEnchantingItemsCount();
		}
		writeD(count);
		for (int i = 1; i <= count; i++)
		{
			double baseRate;
			if (!_isMultiEnchant || (_player.getMultiEnchantingItemsBySlot(i) != 0))
			{
				baseRate = getBaseRate(_player, i);
			}
			else
			{
				baseRate = 0;
			}
			double totalRate = baseRate + supportRate + passiveRate;
			if (totalRate > 10000)
			{
				totalRate = 10000;
			}
			if (!_isMultiEnchant)
			{
				writeD(_player.getEnchantItem().getObjectId());
			}
			else
			{
				writeD(_player.getMultiEnchantingItemsBySlot(i));
			}
			writeD((int) totalRate);
			writeD((int) baseRate);
			writeD((int) supportRate);
			writeD((int) passiveRate);
		}
	}

	private int getBaseRate(Player player, int i)
	{
		double baseRate = 0;
		if (!_isMultiEnchant)
		{
			final EnchantScroll enchantScroll = EnchantItemHolder.getInstance().getEnchantScroll(_player.getEnchantScroll().getItemId());
			final EnchantVariation variation = EnchantItemHolder.getInstance().getEnchantVariation(enchantScroll.getVariationId());
			final EnchantLevel enchantLevel = variation.getLevel(_player.getEnchantItem().getEnchantLevel() + 1);
			if (_player.getEnchantItem().getTemplate().getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR)
				baseRate = enchantLevel.getFullBodyChance() * 100;
			else if (_player.getEnchantItem().getTemplate().isMagicWeapon())
				baseRate = enchantLevel.getMagicWeaponChance() * 100;
			else
				baseRate = enchantLevel.getBaseChance() * 100;
		}
		else
		{
			final ItemInstance item = _player.getInventory().getItemByObjectId(player.getMultiEnchantingItemsBySlot(i));
			final EnchantScroll enchantScroll = EnchantItemHolder.getInstance().getEnchantScroll(_player.getEnchantScroll().getItemId());
			final EnchantVariation variation = EnchantItemHolder.getInstance().getEnchantVariation(enchantScroll.getVariationId());
			final EnchantLevel enchantLevel = variation.getLevel(item.getEnchantLevel() + 1);
			if (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR)
				baseRate = enchantLevel.getFullBodyChance() * 100;
			else if (item.getTemplate().isMagicWeapon())
				baseRate = enchantLevel.getMagicWeaponChance() * 100;
			else
				baseRate = enchantLevel.getBaseChance() * 100;
		}

		return (int) baseRate;
	}
}