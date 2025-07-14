package l2s.gameserver.network.l2.c2s.enchant;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dao.JdbcEntityState;
import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.EnchantItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExItemAnnounce;
import l2s.gameserver.network.l2.s2c.InventoryUpdatePacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.enchant.EnchantResult;
import l2s.gameserver.network.l2.s2c.enchant.ExChangedEnchantTargetItemProbabilityList;
import l2s.gameserver.network.l2.s2c.enchant.ExResultMultiEnchantItemList;
import l2s.gameserver.network.l2.s2c.enchant.ExResultSetMultiEnchantItemList;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.item.support.EnchantScroll;
import l2s.gameserver.templates.item.support.EnchantVariation;
import l2s.gameserver.templates.item.support.EnchantVariation.EnchantLevel;
import l2s.gameserver.templates.item.support.FailResultType;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.Log;

/**
 * @author nexvill
 */
public class RequestExMultiEnchantItemList implements IClientIncomingPacket
{
	private static final int ENCHANT_DELAY = 1000;

	private static final Logger _log = LoggerFactory.getLogger(RequestExMultiEnchantItemList.class);

	private static final int SUCCESS_VISUAL_EFF_ID = 5965;
	private static final int FAIL_VISUAL_EFF_ID = 5949;

	private int _useLateAnnounce;
	private int _slotId;
	private final Map<Integer, Integer> _itemObjectId = new HashMap<>();
	private final Map<Integer, String> _result = new HashMap<>();
	private final Map<Integer, int[]> _successEnchant = new HashMap<>();
	private final Map<Integer, Integer> _failureEnchant = new HashMap<>();
	private final Map<Integer, ItemData> _failureReward = new HashMap<>();

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_useLateAnnounce = packet.readC();
		_slotId = packet.readD();
		for (int i = 1; packet.getReadableBytes() != 0; i++)
		{
			_itemObjectId.put(i, packet.readD());
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}

		if (player.isActionsDisabled())
		{
			player.setEnchantScroll(null);
			player.setSupportItem(null);
			player.sendActionFailed();
			return;
		}

		if (player.isInTrade())
		{
			player.setEnchantScroll(null);
			player.setSupportItem(null);
			player.sendActionFailed();
			return;
		}

		if (System.currentTimeMillis() <= (player.getLastEnchantItemTime() + ENCHANT_DELAY))
		{
			player.setEnchantScroll(null);
			player.setSupportItem(null);
			player.sendActionFailed();
			return;
		}

		if (player.isInStoreMode())
		{
			player.setEnchantScroll(null);
			player.setSupportItem(null);
			player.sendPacket(EnchantResult.CANCEL);
			player.sendPacket(SystemMsg.YOU_CANNOT_ENCHANT_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			player.sendActionFailed();
			return;
		}

		final PcInventory inventory = player.getInventory();
		inventory.writeLock();
		try
		{
			if (player.getEnchantScroll() == null)
			{
				player.sendActionFailed();
				return;
			}

			final ItemInstance scroll = player.getEnchantScroll();
			if (scroll.getCount() < _slotId)
			{
				player.sendPacket(new ExResultSetMultiEnchantItemList(1));
			}

			final EnchantScroll enchantScroll = EnchantItemHolder.getInstance().getEnchantScroll(player.getEnchantScroll().getItemId());
			if (enchantScroll == null)
			{
				player.sendActionFailed();
				return;
			}

			final int[] slots = new int[_slotId];
			for (int i = 1; i <= _slotId; i++)
			{
				if (!player.checkMultiEnchantingItemsByObjectId(_itemObjectId.get(i)))
				{
					player.sendActionFailed();
					return;
				}
				slots[i - 1] = getMultiEnchantingSlotByObjectId(player, _itemObjectId.get(i));
			}

			_itemObjectId.clear();

			for (int slotCounter = 0; slotCounter < slots.length; slotCounter++)
			{
				final int i = slots[slotCounter];
				if ((i == -1) || (player.getMultiEnchantingItemsBySlot(i) == -1))
				{
					player.sendActionFailed();
					player.sendPacket(new ExResultMultiEnchantItemList(player, true));
					return;
				}

				final ItemInstance enchantItem = player.getInventory().getItemByObjectId(player.getMultiEnchantingItemsBySlot(i));
				if ((enchantItem.getEnchantLevel() < enchantScroll.getMinEnchant()) || ((enchantScroll.getMaxEnchant() != -1) && (enchantItem.getEnchantLevel() >= enchantScroll.getMaxEnchant())))
				{
					player.sendPacket(EnchantResult.CANCEL);
					player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
					player.sendActionFailed();
					return;
				}

				if (enchantScroll.getItems().size() > 0)
				{
					if (!enchantScroll.getItems().contains(enchantItem.getItemId()))
					{
						player.sendPacket(EnchantResult.CANCEL);
						player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
						player.sendActionFailed();
						return;
					}
				}
				else
				{
					if (!enchantScroll.containsGrade(enchantItem.getGrade()))
					{
						player.sendPacket(EnchantResult.CANCEL);
						player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
						player.sendActionFailed();
						return;
					}

					final int itemType = enchantItem.getTemplate().getType2();
					switch (enchantScroll.getType())
					{
						case ARMOR:
							if ((itemType == ItemTemplate.TYPE2_WEAPON) || enchantItem.getTemplate().isHairAccessory())
							{
								player.sendPacket(EnchantResult.CANCEL);
								player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
								player.sendActionFailed();
								return;
							}
							break;
						case WEAPON:
							if ((itemType == ItemTemplate.TYPE2_SHIELD_ARMOR) || (itemType == ItemTemplate.TYPE2_ACCESSORY) || enchantItem.getTemplate().isHairAccessory())
							{
								player.sendPacket(EnchantResult.CANCEL);
								player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
								player.sendActionFailed();
								return;
							}
							break;
						case HAIR_ACCESSORY:
							if (!enchantItem.getTemplate().isHairAccessory())
							{
								player.sendPacket(EnchantResult.CANCEL);
								player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
								player.sendActionFailed();
								return;
							}
							break;
					}
				}

				if (!enchantScroll.getItems().contains(enchantItem.getItemId()) && !enchantItem.canBeEnchanted())
				{
					player.sendPacket(EnchantResult.CANCEL);
					player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
					player.sendActionFailed();
					return;
				}

				final EnchantVariation variation = EnchantItemHolder.getInstance().getEnchantVariation(enchantScroll.getVariationId());
				if (variation == null)
				{
					player.sendActionFailed();
					_log.warn("RequestEnchantItem: Cannot find variation ID[" + enchantScroll.getVariationId() + "] for enchant scroll ID[" + enchantScroll.getItemId() + "]!");
					return;
				}

				final EnchantLevel enchantLevel = variation.getLevel(enchantItem.getEnchantLevel() + 1);
				if (enchantLevel == null)
				{
					player.sendActionFailed();
					_log.warn("RequestEnchantItem: Cannot find variation ID[" + enchantScroll.getVariationId() + "] enchant level[" + (enchantItem.getEnchantLevel() + 1) + "] for enchant scroll ID[" + enchantScroll.getItemId() + "]!");
					return;
				}

				if (!inventory.destroyItem(scroll, 1))
				{
					player.sendPacket(EnchantResult.CANCEL);
					player.sendActionFailed();
					return;
				}

				final double baseChance;
				if (enchantItem.getTemplate().getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR)
				{
					baseChance = enchantLevel.getFullBodyChance();
				}
				else if (enchantItem.getTemplate().isMagicWeapon())
				{
					baseChance = enchantLevel.getMagicWeaponChance();
				}
				else
				{
					baseChance = enchantLevel.getBaseChance();
				}

				double chance = baseChance;

				chance += player.getPremiumAccount().getEnchantChanceBonus();
				chance += player.getVIP().getTemplate().getEnchantChanceBonus();
				if (enchantItem.getGrade() != ItemGrade.NONE)
				{
					chance *= player.getEnchantChanceModifier();
				}

				chance = Math.min(100, chance);

				if (Rnd.chance(chance))
				{
					enchantItem.setEnchantLevel(enchantItem.getEnchantLevel() + 1);
					enchantItem.setJdbcState(JdbcEntityState.UPDATED);
					enchantItem.update();

					player.getInventory().refreshEquip(enchantItem);

					player.sendPacket(new InventoryUpdatePacket(player).addModifiedItem(enchantItem));

					player.sendPacket(new EnchantResult(0, 0, 0, enchantItem.getEnchantLevel(), enchantItem.getEnchantLevel()));

					if (enchantLevel.haveSuccessVisualEffect())
					{
						player.broadcastPacket(new SystemMessage(SystemMessage.C1_HAS_SUCCESSFULY_ENCHANTED_A__S2_S3).addName(player).addNumber(enchantItem.getEnchantLevel()).addItemName(enchantItem.getItemId()));
						player.broadcastPacket(new MagicSkillUse(player, player, SUCCESS_VISUAL_EFF_ID, 1, 500, 1500));
						player.broadcastPacket(new ExItemAnnounce(player, enchantItem, 0));
					}

					player.getListeners().onEnchantItem(enchantItem, true);
					_result.put(i, "SUCCESS");
				}
				else
				{
					FailResultType resultType = enchantScroll.getResultType();

					switch (resultType)
					{
						case CRYSTALS:
							if (enchantItem.isEquipped())
							{
								player.sendDisarmMessage(enchantItem);
							}

							Log.LogItem(player, Log.EnchantFail, enchantItem);

							if (!inventory.destroyItem(enchantItem, 1L))
							{
								_result.put(i, "ERROR");
								player.sendActionFailed();
								return;
							}

							player.getEnchantBrokenItemList().add(enchantItem);

							int crystalId = enchantItem.getGrade().getCrystalId();
							int crystalAmount = enchantItem.getCrystalCountOnEchant();
							if ((crystalId > 0) && (crystalAmount > 0) && !enchantItem.isFlagNoCrystallize())
							{
								player.sendPacket(new EnchantResult(1, crystalId, crystalAmount, 0));
								ItemFunctions.addItem(player, crystalId, crystalAmount, true);
								ItemData itemCrystal = new ItemData(crystalId, crystalAmount);
								_failureReward.put(_failureReward.size() + 1, itemCrystal);
								_result.put(i, "FAIL");
							}
							else if ((crystalId == 0) || (crystalAmount == 0))
							{
								_failureReward.put(_failureReward.size() + 1, new ItemData(0, 0));
								_result.put(i, "NO_CRYSTAL");
								player.sendPacket(EnchantResult.FAILED_NO_CRYSTALS);
							}

							if (enchantScroll.showFailEffect())
							{
								player.broadcastPacket(new MagicSkillUse(player, player, FAIL_VISUAL_EFF_ID, 1, 500, 1500));
							}
							break;
						case DROP_ENCHANT:
							int enchantDropCount = enchantScroll.getEnchantDropCount();

							enchantItem.setEnchantLevel(Math.max(enchantItem.getEnchantLevel() - enchantDropCount, 0));
							enchantItem.setJdbcState(JdbcEntityState.UPDATED);
							enchantItem.update();

							player.getInventory().refreshEquip(enchantItem);

							player.sendPacket(new InventoryUpdatePacket(player).addModifiedItem(enchantItem));
							player.sendPacket(SystemMsg.THE_BLESSED_ENCHANT_FAILED);
							player.sendPacket(EnchantResult.BLESSED_FAILED);
							_result.put(i, "BLESSED_FAIL");
							break;
						case NOTHING:
							player.sendPacket(EnchantResult.ANCIENT_FAILED);
							break;
					}
					player.getListeners().onEnchantItem(enchantItem, false);
				}
			}

			for (int slotCounter = 0; slotCounter < slots.length; slotCounter++)
			{
				final int i = slots[slotCounter];
				if (_result.get(i).equals("SUCCESS"))
				{
					int[] intArray = new int[2];
					intArray[0] = player.getMultiEnchantingItemsBySlot(i);
					intArray[1] = player.getInventory().getItemByObjectId(player.getMultiEnchantingItemsBySlot(i)).getEnchantLevel();
					_successEnchant.put(i, intArray);
				}
				else if (_result.get(i).equals("NO_CRYSTAL") || _result.get(i).equals("FAIL"))
				{
					_failureEnchant.put(i, player.getMultiEnchantingItemsBySlot(i));
					player.changeMultiEnchantingItemsBySlot(i, 0);
				}
				else
				{
					player.sendPacket(new ExResultMultiEnchantItemList(player, true));
					return;
				}
			}

			for (int i : _failureReward.keySet())
			{
				player.addMultiEnchantFailItems(_failureReward.get(i));
			}

			player.sendItemList(false);
			player.broadcastUserInfo(true);
			player.sendPacket(new ExChangedEnchantTargetItemProbabilityList(player, true));

			if (_useLateAnnounce == 1)
			{
				player.setMultiSuccessEnchantList(_successEnchant);
				player.setMultiFailureEnchantList(_failureEnchant);
			}

			player.sendPacket(new ExResultMultiEnchantItemList(player, _successEnchant, _failureEnchant));

		}
		finally
		{
			inventory.writeUnlock();
			player.updateStats();
		}
	}

	public int getMultiEnchantingSlotByObjectId(Player player, int objectId)
	{
		int slotId = -1;
		for (int i = 1; i <= player.getMultiEnchantingItemsCount(); i++)
		{
			if ((player.getMultiEnchantingItemsCount() == 0) || (objectId == 0))
			{
				return slotId;
			}
			else if (player.getMultiEnchantingItemsBySlot(i) == objectId)
			{
				return i;
			}
		}
		return slotId;
	}
}