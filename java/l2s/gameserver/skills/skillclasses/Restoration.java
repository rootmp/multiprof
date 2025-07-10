package l2s.gameserver.skills.skillclasses;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExItemAnnounce;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.skill.restoration.RestorationInfo;
import l2s.gameserver.templates.skill.restoration.RestorationItem;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author Bonux
 */
public class Restoration extends Skill
{
	private static final Logger _log = LoggerFactory.getLogger(Restoration.class);

	private final RestorationInfo _restoration;

	public Restoration(final StatsSet set)
	{
		super(set);

		_restoration = (RestorationInfo) set.get("restoration");
	}

	@Override
	public boolean checkCondition(SkillEntry skillEntry, Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first, boolean sendMsg, boolean trigger)
	{
		if (!super.checkCondition(skillEntry, activeChar, target, forceUse, dontMove, first, sendMsg, trigger))
			return false;

		if (_restoration == null)
		{
			_log.warn(getClass().getSimpleName() + ": Cannot find restoration info for skill[" + getId() + "-" + getLevel() + "]");
			return false;
		}

		if (!activeChar.isPlayable())
			return false;

		if (activeChar.isPlayer())
		{
			Player player = (Player) activeChar;
			if (player.getWeightPenalty() >= 3 || player.getInventory().getSize() > player.getInventoryLimit() - 10)
			{
				player.sendPacket(SystemMsg.THE_CORRESPONDING_WORK_CANNOT_BE_PROCEEDED_BECAUSE_THE_INVENTORY_WEIGHTQUANTITY_LIMIT_HAS_BEEN_EXCEEDED);
				return false;
			}
		}
		return true;
	}

	@Override
	public void onEndCast(final Creature activeChar, final Set<Creature> targets)
	{
		super.onEndCast(activeChar, targets);

		if (!activeChar.isPlayable())
			return;

		final Playable playable = (Playable) activeChar;
		final int itemConsumeId = _restoration.getItemConsumeId();
		final int itemConsumeCount = _restoration.getItemConsumeCount();
		if (itemConsumeId > 0 && itemConsumeCount > 0)
		{
			if (ItemFunctions.getItemCount(playable, itemConsumeId) < itemConsumeCount)
			{
				playable.sendPacket(SystemMsg.THERE_ARE_NOT_ENOUGH_NECESSARY_ITEMS_TO_USE_THE_SKILL);
				return;
			}

			ItemFunctions.deleteItem(playable, itemConsumeId, itemConsumeCount, true);
		}

		final List<RestorationItem> restorationItems = _restoration.getRandomGroupItems();
		if (restorationItems == null || restorationItems.size() == 0)
		{
			SystemMsg msg = _restoration.getOnFailMessage();
			if (msg != null)
				playable.sendPacket(msg);
			return;
		}

		for (Creature target : targets)
		{
			if (target != null)
			{
				for (RestorationItem item : restorationItems)
				{
					long count = item.getRandomCount();
					List<ItemInstance> madeItems = ItemFunctions.addItem(playable, item.getId(), count, item.getEnchantLevel(), true);
					String itemName = ItemHolder.getInstance().getTemplate(getItemConsumeId()).getName();
					ItemInstance madeItem = madeItems.get(0);
					for (Player playerr : GameObjectsStorage.getPlayers(false, false))
					{
						playerr.sendPacket(new ExItemAnnounce(activeChar.getPlayer(), madeItem, 1, getItemConsumeId()));
						playerr.sendMessage(activeChar.getPlayer().getName() + " opened " + itemName + " and obtained: " + madeItem.getName() + ((madeItem.getEnchantLevel() > 0) ? (" + (" + madeItem.getEnchantLevel() + ") ") : "") + " x" + count);
					}
				}
			}
		}
	}
}