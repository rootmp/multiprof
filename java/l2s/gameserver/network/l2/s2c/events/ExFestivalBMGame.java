package l2s.gameserver.network.l2.s2c.events;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.FestivalBMHolder;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.s2c.ExItemAnnounce;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.templates.FestivalBMTemplate;

/**
 * @author nexvill
 */
public class ExFestivalBMGame extends L2GameServerPacket
{
	private Map<Integer, FestivalBMTemplate> _items = new HashMap<>();
	private Map<Integer, FestivalBMTemplate> _itemsGrp = new HashMap<>();
	private final Player _player;

	public ExFestivalBMGame(Player player)
	{
		_player = player;
		_items = FestivalBMHolder.getInstance().getItems();
	}

	@Override
	protected final void writeImpl()
	{
		_itemsGrp.clear();

		writeC(1); // result (0 - close window)
		writeD(Config.BM_FESTIVAL_ITEM_TO_PLAY); // item id to play
		if (Config.BM_FESTIVAL_PLAY_LIMIT != -1)
		{
			writeQ(_player.getVarInt("FESTIVAL_BM_EXIST_GAMES", Config.BM_FESTIVAL_PLAY_LIMIT)); // tickets amount.. not
																									// used?
		}
		else
		{
			writeQ(0);
		}
		writeD(Config.BM_FESTIVAL_ITEM_TO_PLAY_COUNT); // tickets used per game

		if (_items.size() > 0)
		{
			int chance = Rnd.get(1000);
			if (chance > 100)
			{
				int resultItem = giveItem(3);
				if (resultItem == 0)
				{
					writeC(3);
					writeD(0);
					writeD(0);
				}
				else
				{
					writeC(3); // location id (grade)
					writeD(resultItem); // item id
					writeD(1); // item count
				}
			}
			else if (chance > 10)
			{
				int resultItem = giveItem(2);
				if (resultItem == 0)
				{
					writeC(2);
					writeD(0);
					writeD(0);
				}
				else
				{
					writeC(2);
					writeD(resultItem);
					writeD(1);
				}
			}
			else
			{
				int resultItem = giveItem(1);
				if (resultItem == 0)
				{
					writeC(1);
					writeD(0);
					writeD(0);
				}
				else
				{
					writeC(1);
					writeD(resultItem);
					writeD(1);
				}
			}
		}
	}

	private int giveItem(int locationId)
	{
		int randomItem = 0;
		int randomCount = 0;
		makeItemsGroup(locationId);
		if ((_itemsGrp.size() > 0) && (locationId == 1))
		{
			makeItemsGroup(locationId + 1);
			if (_itemsGrp.size() == 0)
			{
				makeItemsGroup(locationId + 2);
			}
		}
		else if ((_itemsGrp.size() == 0) && locationId == 2)
		{
			makeItemsGroup(locationId + 1);
		}
		if (_itemsGrp.size() > 0)
		{
			int randomNum = Rnd.get(1, _itemsGrp.size());
			int i = 1;
			for (int id : _itemsGrp.keySet())
			{
				if (i != randomNum)
				{
					i++;
				}
				else
				{
					randomItem = id;
					final FestivalBMTemplate item = _itemsGrp.get(id);
					randomCount = item.getItemCount();
					break;
				}
			}

			final PcInventory inventory = _player.getInventory();
			final ItemInstance addedItem = inventory.addItem(randomItem, 1);
			//Hl4p3x Announce item reward event.
			if (locationId == 1)
			{
				for (Player st : GameObjectsStorage.getPlayers(true, false))
				{
					st.sendPacket(new ExItemAnnounce(_player.getName(), addedItem.getItemId(), 0, 5, 0));
				}
			}
			SystemMessage sm = new SystemMessage(SystemMessage.YOU_HAVE_EARNED_S1);
			sm.addItemName(addedItem.getItemId());
			_player.sendPacket(sm);

			if (Config.BM_FESTIVAL_PLAY_LIMIT != -1)
			{
				int existGames = _player.getVarInt("FESTIVAL_BM_EXIST_GAMES", Config.BM_FESTIVAL_PLAY_LIMIT);
				_player.setVar("FESTIVAL_BM_EXIST_GAMES", existGames - 1);
			}

			int prevAmount = ServerVariables.getInt("FESTIVAL_BM_" + randomItem, randomCount);
			ServerVariables.set("FESTIVAL_BM_" + randomItem, (prevAmount - 1));
			return randomItem;
		}
		return 0;
	}

	private void makeItemsGroup(int locationId)
	{
		for (int id : _items.keySet())
		{
			final FestivalBMTemplate item = _items.get(id);
			int existingAmount = ServerVariables.getInt("FESTIVAL_BM_" + item.getItemId(), item.getItemCount());
			if ((item.getLocationId() == locationId) && (existingAmount > 0))
			{
				_itemsGrp.put(item.getItemId(), new FestivalBMTemplate(item.getItemId(), item.getItemCount(), item.getLocationId()));
			}
		}
	}
}