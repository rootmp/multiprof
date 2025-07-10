package handler.items;

import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;

/**
 * @author nexvill
 */
public class BookMark extends SimpleItemHandler
{
	private static final int[] SLOT_1_BOOKS =
	{
		90404, // My Teleport Book (Sealed)
		90821 // My Teleport Book - 1 slot
	};
	private static final int[] SLOT_3_BOOKS =
	{
		13015 // My Teleport Book
	};

	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		if (player == null)
			return false;

		if (ArrayUtils.contains(SLOT_3_BOOKS, item.getItemId()) && (player.getBookMarkList().getCapacity() + 3) > 48)
		{
			player.sendPacket(SystemMsg.YOU_HAVE_REACHED_THE_MAXIMUM_NUMBER_OF_MY_TELEPORT_SLOTS_OR_USE_CONDITIONS_ARE_NOT_OBSERVED);
			return false;
		}
		else if (ArrayUtils.contains(SLOT_1_BOOKS, item.getItemId()) && (player.getBookMarkList().getCapacity() + 1) > 48)
		{
			player.sendPacket(SystemMsg.YOU_HAVE_REACHED_THE_MAXIMUM_NUMBER_OF_MY_TELEPORT_SLOTS_OR_USE_CONDITIONS_ARE_NOT_OBSERVED);
			return false;
		}

		if (!reduceItem(player, item))
			return false;

		sendUseMessage(player, item);

		if (ArrayUtils.contains(SLOT_3_BOOKS, item.getItemId()))
		{
			player.getBookMarkList().incCapacity(3);
		}
		else if (ArrayUtils.contains(SLOT_1_BOOKS, item.getItemId()))
		{
			player.getBookMarkList().incCapacity(1);
		}
		return true;
	}
}