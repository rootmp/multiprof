package handler.dailymissions;

import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.listener.CharListener;
import l2s.gameserver.listener.actor.player.OnUseItemListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

/**
 * @author nexvill
 */
public class UseItem extends ProgressDailyMissionHandler
{
	protected static final int[] EMPTY_ITEM_IDS = new int[0];

	private class HandlerListeners implements OnUseItemListener
	{
		@Override
		public void onUseItem(Player player, ItemInstance item, boolean success)
		{
			if (!success)
				return;

			if ((getItemIds().length == 0) || ArrayUtils.contains(getItemIds(), item.getItemId()))
			{
				progressMission(player, 1, true, -1);
			}
		}
	}

	private final HandlerListeners _handlerListeners = new HandlerListeners();

	protected int[] getItemIds()
	{
		return EMPTY_ITEM_IDS;
	}

	@Override
	public CharListener getListener()
	{
		return _handlerListeners;
	}
}
