package l2s.gameserver.instancemanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.dao.WorldExchangeDAO;
import l2s.gameserver.enums.WorldExchangeItemStatusType;
import l2s.gameserver.enums.WorldExchangeItemSubType;
import l2s.gameserver.enums.WorldExchangeSortType;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.items.ItemInfo;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExBR_BuyProductPacket;
import l2s.gameserver.network.l2.s2c.ExBR_GamePointPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeBuyItem;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeRegiItem;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeSellCompleteAlarm;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeSettleList;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeSettleRecvResult;
import l2s.gameserver.templates.WorldExchangeHolder;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.Language;

public class WorldExchangeManager
{
	private static final WorldExchangeManager _instance = new WorldExchangeManager();

	private Map<Long, WorldExchangeHolder> _itemBids = new ConcurrentHashMap<>();
	private final Map<Integer, WorldExchangeItemSubType> _itemCategories = new ConcurrentHashMap<>();
	private long _lastWorldExchangeId = 0;

	private ScheduledFuture<?> _checkStatus = null;

	public static WorldExchangeManager getInstance()
	{
		return _instance;
	}

	public WorldExchangeManager()
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		_itemBids = WorldExchangeDAO.getInstance().select();
		if(!_itemBids.isEmpty())
			_lastWorldExchangeId = Collections.max(_itemBids.keySet());

		if(_checkStatus == null)
			_checkStatus = ThreadPoolManager.getInstance().scheduleAtFixedRate(this::checkBidStatus, Config.WORLD_EXCHANGE_SAVE_INTERVAL, Config.WORLD_EXCHANGE_SAVE_INTERVAL);
	}

	public void abortCheckStatusTask()
	{
		if(_checkStatus != null)
		{
			_checkStatus.cancel(true);
			_checkStatus = null;
		}
	}
	
	/**
	 * Little task which check and update bid items if it needs.
	 */
	private void checkBidStatus()
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		for(Entry<Long, WorldExchangeHolder> entry : _itemBids.entrySet())
		{
			final WorldExchangeHolder holder = entry.getValue();
			final long currentTime = System.currentTimeMillis();
			final long endTime = holder.getEndTime();
			if(endTime > currentTime)
				continue;

			switch(holder.getStoreType())
			{
				case WORLD_EXCHANGE_NONE:
				{
					_itemBids.remove(entry.getKey());
					continue;
				}
				case WORLD_EXCHANGE_REGISTERED:
				{
					holder.setEndTime(calculateDate(Config.WORLD_EXCHANGE_ITEM_BACK_PERIOD));
					holder.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME);
					_itemBids.replace(entry.getKey(), holder);
					WorldExchangeDAO.getInstance().insert(_itemBids.get(entry.getKey()), entry.getKey());
					break;
				}
				case WORLD_EXCHANGE_SOLD:
				case WORLD_EXCHANGE_OUT_TIME:
				{
					holder.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE);
					WorldExchangeDAO.getInstance().insert(_itemBids.remove(entry.getKey()), entry.getKey());
					ItemInstance item = holder.getItemInstance();
					item.setLocation(ItemLocation.VOID);
					if(item.getJdbcState().isPersisted())
					{
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();
					}
					break;
				}
				default:
					break;
			}
		}
	}

	private long calculateTaxRegister(Player player, int objectId, long amount, long priceForEach, int listingType)
	{
		if(listingType == 0) // продажа 
		{
			final ItemInstance itemToRemove = player.getInventory().getItemByObjectId(objectId);
			if(itemToRemove.getItemId() == ItemTemplate.ITEM_ID_ADENA)
			{
				long kk = amount/1_000_000;
				final long fee = Math.round((priceForEach * kk) * ((double)Config.WORLD_EXCHANGE_TAX / 100));
				return Math.max(fee, Config.WORLD_EXCHANGE_MIN_ADENA_TAX);
			}else
			{
				final long fee = Math.round((priceForEach * amount) * ((double)Config.WORLD_EXCHANGE_TAX / 100));
				return Math.max(fee, Config.WORLD_EXCHANGE_MIN_BALANS_TAX);
			}
		}

		return 0;
	}

	/**
	 * Forwarded from client packet "ExWorldExchangeRegisterItem" for check ops and register item if it can in World Exchange system
	 * @param player
	 * @param itemObjectId
	 * @param amount
	 * @param priceForEach
	 * @param listingType
	 * @param currencyType 
	 */
	public void registerItemBid(Player player, int itemObjectId, long amount, long priceForEach, int listingType, int currencyType)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		final Map<WorldExchangeItemStatusType, List<WorldExchangeHolder>> playerBids = getPlayerBids(player.getObjectId());
		if(playerBids.size() >= 10)
		{
			player.sendPacket(new SystemMessage(SystemMsg.YOU_HAVE_NO_OPEN_MY_TELEPORTS_SLOTS));
			player.sendPacket(ExWorldExchangeRegiItem.FAIL);
			return;
		}
		if(player.getInventory().getItemByObjectId(itemObjectId) == null)
		{
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeRegiItem.FAIL);
			return;
		}

		final ItemInstance item = player.getInventory().getItemByObjectId(itemObjectId);

		if(item.getItemId() == 57)
			amount = (amount / 1000000) * 1000000;

		if(amount<=0)
		{
			player.sendPacket(ExWorldExchangeRegiItem.FAIL);
			return;
		}
		long taxRegister = calculateTaxRegister(player, itemObjectId, amount, priceForEach, listingType);
	
		final long freeId = getNextId();

		final WorldExchangeItemSubType category = _itemCategories.get(item.getItemId());
		if(category == null)
		{
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_YOU_REGISTERED_HAS_BEEN_SOLD));
			player.sendPacket(ExWorldExchangeRegiItem.FAIL);
			return;
		}
		if(listingType == 0) // продажа
		{
			if(currencyType == 0)
			{
				if(taxRegister > player.getAdena())
				{
					player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					player.sendPacket(ExWorldExchangeRegiItem.FAIL);
					return;
				}
			}else
			{
				taxRegister = taxRegister * 5000;
				if(taxRegister > player.getAdena())
				{
					player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					player.sendPacket(ExWorldExchangeRegiItem.FAIL);
					return;
				}
			}

			final ItemInstance itemInstance = player.getInventory().removeItem(item, amount);
			if(itemInstance == null)
			{
				player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
				player.sendPacket(ExWorldExchangeRegiItem.FAIL);
				return;
			}

			itemInstance.setOwnerId(player.getObjectId());
			itemInstance.setLocation(ItemLocation.EXCHANGE);
			if(itemInstance.getJdbcState().isSavable())
			{
				itemInstance.save();
			}
			else
			{
				itemInstance.setJdbcState(JdbcEntityState.UPDATED);
				itemInstance.update();
			}
			
			player.getInventory().reduceAdena(taxRegister);
			
			final long endTime = calculateDate(Config.WORLD_EXCHANGE_ITEM_SELL_PERIOD);
			_itemBids.put(freeId, new WorldExchangeHolder(freeId, itemInstance, new ItemInfo(itemInstance), priceForEach, player.getObjectId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED, category, System.currentTimeMillis(), endTime, true, listingType, currencyType));
			player.sendPacket(new ExWorldExchangeRegiItem(itemObjectId, amount, (byte) 1));

			WorldExchangeDAO.getInstance().insert(_itemBids.get(freeId), freeId);
		}
		else // покупка
		{
			if(currencyType == 0) //адена
			{
				if(item.getItemId() == ItemTemplate.ITEM_ID_ADENA)
				{
					player.sendPacket(ExWorldExchangeBuyItem.FAIL);
					return;
				}
				long adena = amount * priceForEach;
				if(ItemFunctions.getItemCount(player, ItemTemplate.ITEM_ID_ADENA) < adena)
				{
					player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					player.sendPacket(ExWorldExchangeRegiItem.FAIL);
					return;
				}
				if(!ItemFunctions.deleteItem(player, ItemTemplate.ITEM_ID_ADENA, adena))
				{
					player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					player.sendPacket(ExWorldExchangeRegiItem.FAIL);
					return;
				}//else
					//Log.LogItemWorldExchange(player, "[BUY] prepayment adena :", null, adena);
			}

			if(currencyType == 1)
			{
				long item_amount = amount;
				if(item.getItemId() == ItemTemplate.ITEM_ID_ADENA)
					item_amount= amount/1000000;
				
				if(item_amount<=0)
				{
					player.sendPacket(ExWorldExchangeRegiItem.FAIL);
					return;
				}
				
				long amounts = item_amount * priceForEach;				
				if(ItemFunctions.getItemCount(player, ItemTemplate.ITEM_ID_LUCKY_COIN) < amounts)
				{
					player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
					player.sendPacket(ExWorldExchangeBuyItem.FAIL);
					return;
				}
				if(!ItemFunctions.deleteItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, amounts))
				{
					player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
					player.sendPacket(ExWorldExchangeBuyItem.FAIL);
					return;
				}//else
					//Log.LogItemWorldExchange(player, "[BUY] prepayment Lucky Coin :", null, amounts);
			}
			final ItemInstance newItem = ItemFunctions.createItem(item.getItemId());
			newItem.setOwnerId(item.getOwnerId());
			newItem.setEnchantLevel(0);
			newItem.setLocation(ItemLocation.EXCHANGE);
			newItem.setCount(amount);
			newItem.save();

			final long endTime = calculateDate(Config.WORLD_EXCHANGE_ITEM_SELL_PERIOD);
			_itemBids.put(freeId, new WorldExchangeHolder(freeId, newItem, new ItemInfo(newItem), priceForEach, player.getObjectId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED, category, System.currentTimeMillis(), endTime, true, listingType, currencyType));
			player.sendPacket(new ExWorldExchangeRegiItem(itemObjectId, amount, (byte) 1));

			WorldExchangeDAO.getInstance().insert(_itemBids.get(freeId), freeId);
		}
	}

	private synchronized long getNextId()
	{
		return _lastWorldExchangeId++;
	}

	public static long calculateDate(int days)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return calendar.getTimeInMillis();
	}

	/**
	 * Forwarded from ExWorldExchangeSettleRecvResult for make Action, because client send only WORLD EXCHANGE Index without anu addition info.
	 * @param player
	 * @param worldExchangeIndex
	 */
	public void getItemStatusAndMakeAction(Player player, long worldExchangeIndex)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		final WorldExchangeHolder worldExchangeItem = _itemBids.get(worldExchangeIndex);
		if(worldExchangeItem == null)
		{
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		final WorldExchangeItemStatusType storeType = worldExchangeItem.getStoreType();
		switch(storeType)
		{
			case WORLD_EXCHANGE_REGISTERED:
			{
				cancelBid(player, worldExchangeItem);
				break;
			}
			case WORLD_EXCHANGE_SOLD:
			{
				takeBidMoney(player, worldExchangeItem);
				break;
			}
			case WORLD_EXCHANGE_OUT_TIME:
			{
				returnItem(player, worldExchangeItem);
				break;
			}
			default:
				break;
		}
	}

	/**
	 * Forwarded from getItemStatusAndMakeAction / remove item and holder from active bid and take it back to owner.
	 * @param player
	 * @param worldExchangeItem
	 */
	private void cancelBid(Player player, WorldExchangeHolder worldExchangeItem)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		if(worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(!_itemBids.containsKey(worldExchangeItem.getWorldExchangeId()))
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(_itemBids.get(worldExchangeItem.getWorldExchangeId()) != worldExchangeItem)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(player.getObjectId() != worldExchangeItem.getOldOwnerId())
		{
			player.sendPacket(new SystemMessage(SystemMsg.ITEM_OUT_OF_STOCK));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_YOU_REGISTERED_HAS_BEEN_SOLD));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		player.sendPacket(new ExWorldExchangeSettleRecvResult(worldExchangeItem.getItemInstance().getObjectId(), worldExchangeItem.getItemInstance().getCount(), (byte) 1));

		if(worldExchangeItem.getListingType() == 1)
		{
			if(worldExchangeItem.getCurrencyType() == 0)
			{
				ItemFunctions.addItem(player, 57, worldExchangeItem.getItemInfo().getCount() * worldExchangeItem.getPrice());
				//Log.LogItemWorldExchange(player, "[BUY] cancelBid advance refund adena:", null, worldExchangeItem.getItemInfo().getCount() * worldExchangeItem.getPrice());
			}
			if(worldExchangeItem.getCurrencyType() == 1)
			{
				if(worldExchangeItem.getItemInfo().getItemId() == 57)
				{
					ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, (int) ((worldExchangeItem.getItemInfo().getCount()/1000000) * worldExchangeItem.getPrice()));
					//Log.LogItemWorldExchange(player, "[BUY] cancelBid advance refund Lucky Coin:", null, (int) ((worldExchangeItem.getItemInfo().getCount()/1000000) * worldExchangeItem.getPrice()));
				}
				else
				{
					ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, (int) (worldExchangeItem.getItemInfo().getCount() * worldExchangeItem.getPrice()));
					//Log.LogItemWorldExchange(player, "[BUY] cancelBid advance refund Lucky Coin:", null, (int) (worldExchangeItem.getItemInfo().getCount() * worldExchangeItem.getPrice()));
				}
				player.sendPacket(new ExBR_GamePointPacket(player));
			}

		}
		else
		{
			ItemInstance item = player.getInventory().addItem(worldExchangeItem.getItemInstance());
			//Log.LogItemWorldExchange(player, "[SALE] cancelBid advance refund:", item, worldExchangeItem.getItemInstance().getCount());
		}
		worldExchangeItem.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE);
		worldExchangeItem.setHasChanges(true);
		_itemBids.replace(worldExchangeItem.getWorldExchangeId(), worldExchangeItem);

		WorldExchangeDAO.getInstance().insert(_itemBids.remove(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());
	}

	/**
	 * Forwarded from getItemStatusAndMakeAction / takes money from bid.
	 * @param player
	 * @param worldExchangeItem
	 */
	private void takeBidMoney(Player player, WorldExchangeHolder worldExchangeItem)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		if(worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(!_itemBids.containsKey(worldExchangeItem.getWorldExchangeId()))
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(_itemBids.get(worldExchangeItem.getWorldExchangeId()) != worldExchangeItem)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(player.getObjectId() != worldExchangeItem.getOldOwnerId())
		{
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(worldExchangeItem.getStoreType() != WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_YOU_REGISTERED_HAS_BEEN_SOLD));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(worldExchangeItem.getEndTime() < System.currentTimeMillis())
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_REGISTRATION_PERIOD_FOR_THE_ITEM_YOU_REGISTERED_HAS_EXPIRED));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		player.sendPacket(new ExWorldExchangeSettleRecvResult(worldExchangeItem.getItemInstance().getObjectId(), worldExchangeItem.getItemInstance().getCount(), 1));
		
		final long fee = Math.round((worldExchangeItem.getPrice() * worldExchangeItem.getItemInfo().getCount()) * ((double)Config.WORLD_EXCHANGE_TAX / 100));
		final long fee_adena_balance = Math.round((worldExchangeItem.getPrice() * worldExchangeItem.getItemInfo().getCount()/1000000) * ((double)Config.WORLD_EXCHANGE_TAX / 100));
		
		worldExchangeItem.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE);
		if(worldExchangeItem.getListingType() == 0)
		{

			if(worldExchangeItem.getCurrencyType() == 0)
			{
				long returnPrice = worldExchangeItem.getPrice() * worldExchangeItem.getItemInfo().getCount()
						- Math.max(fee, Config.WORLD_EXCHANGE_MIN_ADENA_TAX);
				player.getInventory().addItem(ItemTemplate.ITEM_ID_ADENA, (returnPrice));
				//Log.LogItemWorldExchange(player, "[SALE] takeBidMoney adena :", worldExchangeItem.getItemInstance(), returnPrice);
			}
			if(worldExchangeItem.getCurrencyType() == 1)
			{
				long returnPrice = 0;
				if(worldExchangeItem.getItemInfo().getItemId() == 57)
				{
					long item_count = worldExchangeItem.getItemInfo().getCount();
					item_count = worldExchangeItem.getItemInfo().getCount()/1000000;
					returnPrice = worldExchangeItem.getPrice() * item_count - Math.max(fee_adena_balance, Config.WORLD_EXCHANGE_MIN_BALANS_TAX);
				}else
				{
					long item_count = worldExchangeItem.getItemInfo().getCount();
					returnPrice = worldExchangeItem.getPrice() * item_count - Math.max(fee, Config.WORLD_EXCHANGE_MIN_BALANS_TAX);
				}

				ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, (int) returnPrice);
				//Log.LogItemWorldExchange(player, "[SALE] takeBidMoney Lucky Coin :", worldExchangeItem.getItemInstance(), returnPrice);
				player.sendPacket(new ExBR_GamePointPacket(player));
			}
		}
		else
		{
			ItemFunctions.addItem(player, worldExchangeItem.getItemInstance().getItemId(), worldExchangeItem.getItemInstance().getCount());
			//Log.LogItemWorldExchange(player, "[BUY] takeBidMoney Lucky Coin :", worldExchangeItem.getItemInstance(), worldExchangeItem.getItemInstance().getCount());
		}
		ItemInstance item = worldExchangeItem.getItemInstance();
		item.setLocation(ItemLocation.VOID);
		if(item.getJdbcState().isPersisted())
		{
			item.setJdbcState(JdbcEntityState.UPDATED);
			item.update();
		}

		worldExchangeItem.setHasChanges(true);
		_itemBids.replace(worldExchangeItem.getWorldExchangeId(), worldExchangeItem);

		WorldExchangeDAO.getInstance().insert(_itemBids.remove(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());
	}

	/**
	 * Forwarded from getItemStatusAndMakeAction / take back item which placed on World Exchange.
	 * @param player
	 * @param worldExchangeItem
	 */
	private void returnItem(Player player, WorldExchangeHolder worldExchangeItem)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		if(worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(!_itemBids.containsKey(worldExchangeItem.getWorldExchangeId()))
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(_itemBids.get(worldExchangeItem.getWorldExchangeId()) != worldExchangeItem)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.ITEM_OUT_OF_STOCK));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(player.getObjectId() != worldExchangeItem.getOldOwnerId())
		{
			player.sendPacket(new SystemMessage(SystemMsg.ITEM_TO_BE_TRADED_DOES_NOT_EXIST));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(worldExchangeItem.getStoreType() != WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME)
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.ITEM_OUT_OF_STOCK));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		if(worldExchangeItem.getEndTime() < System.currentTimeMillis())
		{
			player.sendPacket(new ExWorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMsg.THE_REGISTRATION_PERIOD_FOR_THE_ITEM_YOU_REGISTERED_HAS_EXPIRED));
			player.sendPacket(ExWorldExchangeSettleRecvResult.FAIL);
			return;
		}

		player.sendPacket(new ExWorldExchangeSettleRecvResult(worldExchangeItem.getItemInstance().getObjectId(), worldExchangeItem.getItemInstance().getCount(), (byte) 1));

		if(worldExchangeItem.getListingType() == 1)
		{
			if(worldExchangeItem.getCurrencyType() == 0)
				ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_ADENA, worldExchangeItem.getItemInfo().getCount()
						* worldExchangeItem.getPrice());

			if(worldExchangeItem.getCurrencyType() == 1)
			{
				ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, (int) (worldExchangeItem.getItemInfo().getCount() * worldExchangeItem.getPrice()));
				player.sendPacket(new ExBR_GamePointPacket(player));
			}
		}
		else
			player.getInventory().addItem(worldExchangeItem.getItemInstance());

		worldExchangeItem.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE);
		worldExchangeItem.setHasChanges(true);
		_itemBids.replace(worldExchangeItem.getWorldExchangeId(), worldExchangeItem);

		WorldExchangeDAO.getInstance().insert(_itemBids.remove(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());
	}

	/**
	 * Forwarded from ExWorldExchangeBuyItem / request for but item and create a visible clone for old owner.
	 * @param player
	 * @param worldExchangeId
	 * @param nCount 
	 */
	public void buyItem(Player player, long worldExchangeId, long nCount)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		if(!_itemBids.containsKey(worldExchangeId))
		{
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeBuyItem.FAIL);
			return;
		}
		final WorldExchangeHolder worldExchangeItem = _itemBids.get(worldExchangeId);
		if(worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
		{
			player.sendPacket(new SystemMessage(SystemMsg.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(ExWorldExchangeBuyItem.FAIL);
			return;
		}

		if(worldExchangeItem.getStoreType() != WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED)
		{
			player.sendPacket(new SystemMessage(SystemMsg.ITEM_OUT_OF_STOCK));
			player.sendPacket(ExWorldExchangeBuyItem.FAIL);
			return;
		}

		switch(worldExchangeItem.getCurrencyType())
		{
			case 0://адена
			{
				if(worldExchangeItem.getListingType() == 0)//продажа 
				{
					if(worldExchangeItem.getItemInfo().getCount() < nCount)
					{
						player.sendPacket(ExWorldExchangeBuyItem.FAIL);
						return;
					}

					final ItemInstance adena = player.getInventory().getItemByItemId(ItemTemplate.ITEM_ID_ADENA);

					if(adena == null || adena.getCount() < worldExchangeItem.getPrice() * nCount)
					{
						player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
						player.sendPacket(ExWorldExchangeBuyItem.FAIL);
						return;
					}
					player.getInventory().destroyItem(adena, worldExchangeItem.getPrice() * nCount);

					if(worldExchangeItem.getItemInfo().getCount() > nCount)//поштучно
					{
						final ItemInstance oldItem = createItem(worldExchangeItem.getItemInstance(), player.getObjectId(), worldExchangeItem.getItemInstance().getCount()
								- nCount);
						final ItemInstance newItem = createItem(worldExchangeItem.getItemInstance(), worldExchangeItem.getOldOwnerId(), nCount);
						final ItemInstance new2Item = createItem(worldExchangeItem.getItemInstance(), player.getObjectId(), nCount);

						final long freeId = getNextId();

						final long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
						WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, oldItem, new ItemInfo(oldItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), worldExchangeItem.getStoreType(), worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType());
						_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);

						_itemBids.put(freeId, new WorldExchangeHolder(freeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType()));

						WorldExchangeDAO.getInstance().insert(_itemBids.get(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());
						WorldExchangeDAO.getInstance().insert(_itemBids.get(freeId), freeId);

						ItemInstance receivedItem = player.getInventory().addItem(new2Item);

						//Log.LogItemWorldExchange(player, "[SALE] buyItem adena:", receivedItem, receivedItem.getCount());
						
						//TODO player.sendItemList(false);
						player.sendPacket(new ExWorldExchangeBuyItem(receivedItem.getItemId(), receivedItem.getCount(), (byte) 1));

						for(Player oldOwner : GameObjectsStorage.getPlayers(true, true))
						{
							if(oldOwner.getObjectId() == newHolder.getOldOwnerId())
							{
								//TODO oldOwner.sendItemList(false);
								oldOwner.sendPacket(new ExWorldExchangeSellCompleteAlarm(newItem.getItemId(), newItem.getCount()));
								break;
							}
						}
					}
					else
					{
						final ItemInstance newItem = createItem(worldExchangeItem.getItemInstance(), player);
						final long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
						WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType());
						_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);

						WorldExchangeDAO.getInstance().insert(_itemBids.get(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());

						ItemInstance receivedItem = player.getInventory().addItem(worldExchangeItem.getItemInstance());
						//Log.LogItemWorldExchange(player, "[SALE] buyItem adena:", receivedItem, worldExchangeItem.getItemInstance().getCount());
						//TODO player.sendItemList(false);
						player.sendPacket(new ExWorldExchangeBuyItem(receivedItem.getItemId(), receivedItem.getCount(), (byte) 1));

						for(Player oldOwner : GameObjectsStorage.getPlayers(true, true))
						{
							if(oldOwner.getObjectId() == newHolder.getOldOwnerId())
							{
								oldOwner.sendPacket(new ExWorldExchangeSellCompleteAlarm(newItem.getItemId(), newItem.getCount()));
								break;
							}
						}
					}
				}
				if(worldExchangeItem.getListingType() == 1)//покупка
				{
					if(worldExchangeItem.getItemInfo().getCount() < nCount)
					{
						player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
						player.sendPacket(ExWorldExchangeBuyItem.FAIL);
						return;
					}
					ItemInstance item = player.getInventory().getItemByItemId(worldExchangeItem.getItemInfo().getItemId());
					if(item == null || ItemFunctions.getItemCount(player, item.getItemId()) < nCount)
					{
						player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
						player.sendPacket(ExWorldExchangeRegiItem.FAIL);
						return;
					}
					
					if(!ItemFunctions.deleteItem(player, worldExchangeItem.getItemInfo().getItemId(), nCount))
					{
						player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
						player.sendPacket(ExWorldExchangeRegiItem.FAIL);
						return;
					}
					final long fee = (long) ((worldExchangeItem.getPrice() * nCount) * ((double)Config.WORLD_EXCHANGE_TAX / 100));
					long returnPrice = worldExchangeItem.getPrice() * nCount - Math.max(fee, Config.WORLD_EXCHANGE_MIN_ADENA_TAX);
					
					if(worldExchangeItem.getItemInfo().getCount() > nCount)//покупка, поштучно
					{
						ItemInstance oldItem = createItem(worldExchangeItem.getItemInstance(), worldExchangeItem.getOldOwnerId(), worldExchangeItem.getItemInstance().getCount()
								- nCount);
						ItemInstance newItem = createItem(worldExchangeItem.getItemInstance(), worldExchangeItem.getOldOwnerId(), nCount);

						long freeId = getNextId();

						long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
						WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, oldItem, new ItemInfo(oldItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), worldExchangeItem.getStoreType(), worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType());

						_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);
						_itemBids.put(freeId, new WorldExchangeHolder(freeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType()));

						WorldExchangeDAO.getInstance().insert(_itemBids.get(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());
						WorldExchangeDAO.getInstance().insert(_itemBids.get(freeId), freeId);

						ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_ADENA, returnPrice);

						//Log.LogItemWorldExchange(player, "[BUY] buyItem adena:", null, returnPrice);
						
						//TODO player.sendItemList(false);
						player.sendPacket(new ExWorldExchangeBuyItem(newItem.getItemId(), newItem.getCount(), (byte) 1));

						for(Player oldOwner : GameObjectsStorage.getPlayers(true, true))
						{
							if(oldOwner.getObjectId() == newHolder.getOldOwnerId())
							{
								oldOwner.sendPacket(new ExWorldExchangeSellCompleteAlarm(newItem.getItemId(), newItem.getCount()));
								break;
							}
						}
					}
					else
					{
						ItemInstance newItem = createItem(worldExchangeItem.getItemInstance(), worldExchangeItem.getOldOwnerId(), worldExchangeItem.getItemInstance().getCount());

						long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
						WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType());
						
						_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);

						WorldExchangeDAO.getInstance().insert(_itemBids.get(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());

						ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_ADENA, returnPrice);

						//Log.LogItemWorldExchange(player, "[BUY] buyItem adena:", null, returnPrice);
						
						//TODO player.sendItemList(false);
						player.sendPacket(new ExWorldExchangeBuyItem(newItem.getItemId(), newItem.getCount(), (byte) 1));

						for(Player oldOwner : GameObjectsStorage.getPlayers(true,true))
						{
							if(oldOwner.getObjectId() == newHolder.getOldOwnerId())
							{
								oldOwner.sendPacket(new ExWorldExchangeSellCompleteAlarm(newItem.getItemId(), newItem.getCount()));
								break;
							}
						}
					}
				}
				break;
			}
				
			case 1://баланс
			{
				if(worldExchangeItem.getListingType() == 0)//продажа 
				{ 
					if(worldExchangeItem.getItemInstance().getItemId() == 57)
					{
						if(nCount<1000000)
						{
							player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
							player.sendPacket(ExWorldExchangeBuyItem.FAIL);
							return;
						}
						
						if(!ItemFunctions.haveItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, worldExchangeItem.getPrice() * (nCount/1000000)))
						{
							player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
							player.sendPacket(ExWorldExchangeBuyItem.FAIL);
							return;
						}
						
						if(!ItemFunctions.deleteItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, (int) (worldExchangeItem.getPrice() * (nCount/1000000))))
						{
							player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
							return;
						}
					}else
					{
						if(!ItemFunctions.haveItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, worldExchangeItem.getPrice() * nCount))
						{
							player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
							player.sendPacket(ExWorldExchangeBuyItem.FAIL);
							return;
						}

						if(!ItemFunctions.deleteItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, (int) (worldExchangeItem.getPrice() * nCount)))
						{
							player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
							return;
						}
					}

					player.sendPacket(new ExBR_GamePointPacket(player));

					if(worldExchangeItem.getItemInfo().getCount() > nCount)//покупаем поштучно
					{
						final ItemInstance oldItem = createItem(worldExchangeItem.getItemInstance(), player.getObjectId(), worldExchangeItem.getItemInstance().getCount()
								- nCount);
						final ItemInstance newItem = createItem(worldExchangeItem.getItemInstance(), worldExchangeItem.getOldOwnerId(), nCount);
						final ItemInstance new2Item = createItem(worldExchangeItem.getItemInstance(), player.getObjectId(), nCount);

						final long freeId = getNextId();

						final long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
						WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, oldItem, new ItemInfo(oldItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), worldExchangeItem.getStoreType(), worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType());
						_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);

						_itemBids.put(freeId, new WorldExchangeHolder(freeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType()));

						WorldExchangeDAO.getInstance().insert(_itemBids.get(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());
						WorldExchangeDAO.getInstance().insert(_itemBids.get(freeId), freeId);

						ItemInstance receivedItem = player.getInventory().addItem(new2Item);
						//Log.LogItemWorldExchange(player, "[SALE] buyItem Lucky Coin:", receivedItem, receivedItem.getCount());
						
						//TODO player.sendItemList(false);
						player.sendPacket(new ExWorldExchangeBuyItem(receivedItem.getItemId(), receivedItem.getCount(), (byte) 1));

						for(Player oldOwner : GameObjectsStorage.getPlayers(true, true))
						{
							if(oldOwner.getObjectId() == newHolder.getOldOwnerId())
							{
								//TODO oldOwner.sendItemList(false);
								oldOwner.sendPacket(new ExWorldExchangeSellCompleteAlarm(newItem.getItemId(), newItem.getCount()));
								break;
							}
						}
					}
					else
					{
						final ItemInstance newItem = createItem(worldExchangeItem.getItemInstance(), player);
						final long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
						WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType());
						_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);

						WorldExchangeDAO.getInstance().insert(_itemBids.get(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());

						ItemInstance receivedItem = player.getInventory().addItem(worldExchangeItem.getItemInstance());
						//Log.LogItemWorldExchange(player, "[SALE] buyItem Lucky Coin:", receivedItem, receivedItem.getCount());
						//TODO player.sendItemList(false);
						player.sendPacket(new ExWorldExchangeBuyItem(receivedItem.getItemId(), receivedItem.getCount(), (byte) 1));

						for(Player oldOwner : GameObjectsStorage.getPlayers(true, true))
						{
							if(oldOwner.getObjectId() == newHolder.getOldOwnerId())
							{
								oldOwner.sendPacket(new ExWorldExchangeSellCompleteAlarm(newItem.getItemId(), newItem.getCount()));
								break;
							}
						}
					}
				}
				
				if(worldExchangeItem.getListingType() == 1)//покупка
				{
					if(worldExchangeItem.getItemInfo().getCount() < nCount)
					{
						player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
						player.sendPacket(ExWorldExchangeBuyItem.FAIL);
						return;
					}
					ItemInstance item = player.getInventory().getItemByItemId(worldExchangeItem.getItemInfo().getItemId());
					if(item == null || ItemFunctions.getItemCount(player, item.getItemId()) < nCount)
					{
						player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
						player.sendPacket(ExWorldExchangeRegiItem.FAIL);
						return;
					}
					
					if(!ItemFunctions.deleteItem(player, worldExchangeItem.getItemInfo().getItemId(), nCount))
					{
						player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
						player.sendPacket(ExWorldExchangeRegiItem.FAIL);
						return;
					}
					
					final long fee = (long) ((worldExchangeItem.getPrice() * nCount) * ((double)Config.WORLD_EXCHANGE_TAX / 100));
					final long fee_andena_balance = (long) ((worldExchangeItem.getPrice() * nCount/1000000) * ((double)Config.WORLD_EXCHANGE_TAX / 100));
					
					long returnPrice = worldExchangeItem.getPrice() * nCount - Math.max(fee, Config.WORLD_EXCHANGE_MIN_BALANS_TAX);
					
					if(worldExchangeItem.getItemInfo().getItemId() == 57)
						 returnPrice = worldExchangeItem.getPrice() * (nCount/1000000) - Math.max(fee_andena_balance, Config.WORLD_EXCHANGE_MIN_BALANS_TAX);

					if(worldExchangeItem.getItemInfo().getCount() > nCount)//покупка, поштучно
					{

						ItemInstance oldItem = createItem(worldExchangeItem.getItemInstance(), worldExchangeItem.getOldOwnerId(), worldExchangeItem.getItemInstance().getCount()
								- nCount);
						ItemInstance newItem = createItem(worldExchangeItem.getItemInstance(), worldExchangeItem.getOldOwnerId(), nCount);

						long freeId = getNextId();

						long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
						WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, oldItem, new ItemInfo(oldItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), worldExchangeItem.getStoreType(), worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType());

						_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);
						_itemBids.put(freeId, new WorldExchangeHolder(freeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType()));

						WorldExchangeDAO.getInstance().insert(_itemBids.get(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());
						WorldExchangeDAO.getInstance().insert(_itemBids.get(freeId), freeId);

						//Log.LogItemWorldExchange(player, "[BUY] buyItem Lucky Coin add :", null, returnPrice);
						
						ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, (int) returnPrice);
						player.sendPacket(new ExBR_GamePointPacket(player));
						
						//TODO player.sendItemList(false);
						player.sendPacket(new ExWorldExchangeBuyItem(newItem.getItemId(), newItem.getCount(), (byte) 1));

						for(Player oldOwner : GameObjectsStorage.getPlayers(true,true))
						{
							if(oldOwner.getObjectId() == newHolder.getOldOwnerId())
							{
								oldOwner.sendPacket(new ExWorldExchangeSellCompleteAlarm(newItem.getItemId(), newItem.getCount()));
								break;
							}
						}
					}
					else
					{
						ItemInstance newItem = createItem(worldExchangeItem.getItemInstance(), worldExchangeItem.getOldOwnerId(), worldExchangeItem.getItemInstance().getCount());

						long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
						WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true, worldExchangeItem.getListingType(), worldExchangeItem.getCurrencyType());
						
						_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);

						WorldExchangeDAO.getInstance().insert(_itemBids.get(worldExchangeItem.getWorldExchangeId()), worldExchangeItem.getWorldExchangeId());
						//Log.LogItemWorldExchange(player, "[BUY] buyItem Lucky Coin add :", null, returnPrice);
						
						ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_LUCKY_COIN, (int) returnPrice);
						player.sendPacket(new ExBR_GamePointPacket(player));
						
						//TODO player.sendItemList(false);
						player.sendPacket(new ExWorldExchangeBuyItem(newItem.getItemId(), newItem.getCount(), (byte) 1));

						for(Player oldOwner : GameObjectsStorage.getPlayers(true, true))
						{
							if(oldOwner.getObjectId() == newHolder.getOldOwnerId())
							{
								oldOwner.sendPacket(new ExWorldExchangeSellCompleteAlarm(newItem.getItemId(), newItem.getCount()));
								break;
							}
						}
					}
				}
			}
				break;
			default:
				break;
		}

	}

	/**
	 * Create a new item for make it visible in UI for old owner.
	 * @param oldItem item from holder which will be "cloned"
	 * @param requestor
	 * @return cloned item
	 */
	private ItemInstance createItem(ItemInstance oldItem, Player requestor)
	{
		final ItemInstance newItem = ItemFunctions.createItem(oldItem.getItemId());
		newItem.setOwnerId(requestor.getObjectId());
		newItem.setEnchantLevel(oldItem.getEnchantLevel() < 1 ? 0 : oldItem.getEnchantLevel());
		newItem.setLocation(ItemLocation.EXCHANGE);
		newItem.setCount(oldItem.getCount());
		newItem.setVisualId(oldItem.getVisualId());
		newItem.setBlessed(oldItem.isBlessed());

		newItem.setVariation1Id(oldItem.getVariation1Id());
		newItem.setVariation2Id(oldItem.getVariation2Id());

		newItem.save();
		return newItem;
	}

	private ItemInstance createItem(ItemInstance oldItem, int ownerId, long nCount)
	{
		final ItemInstance newItem = ItemFunctions.createItem(oldItem.getItemId());
		newItem.setOwnerId(ownerId);
		newItem.setEnchantLevel(oldItem.getEnchantLevel() < 1 ? 0 : oldItem.getEnchantLevel());
		newItem.setLocation(ItemLocation.EXCHANGE);
		newItem.setCount(nCount);
		newItem.setVisualId(oldItem.getVisualId());
		newItem.setBlessed(oldItem.isBlessed());
		
		newItem.setVariation1Id(oldItem.getVariation1Id());
		newItem.setVariation2Id(oldItem.getVariation2Id());

		newItem.save();
		return newItem;
	}

	/**
	 * @param ownerId
	 * @param nPage 
	 * @param type
	 * @param sortType
	 * @param lang
	 * @param ListingType 
	 * @param CurrencyType 
	 * @return items, which player can buy
	 */
	public List<WorldExchangeHolder> getItemBids(int ownerId, int nPage, WorldExchangeItemSubType type, WorldExchangeSortType sortType, Language lang, int cListingType, int cCurrencyType)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return Collections.emptyList();

		final List<WorldExchangeHolder> returnList = new ArrayList<>();
		for(WorldExchangeHolder holder : _itemBids.values())
		{
			if(holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
				continue;

			if((holder.getOldOwnerId() == ownerId) || (holder.getItemInstance().getTemplate().getItemSubType() != type))
				continue;

			if(cCurrencyType != 2 && holder.getCurrencyType() != cCurrencyType)
				continue;
			if(cListingType != 2 && holder.getListingType() != cListingType)
				continue;
			if(holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED)
				returnList.add(holder);
		}

		return sortList(nPage, returnList, sortType, lang, cListingType);
	}

	/**
	 * @param ids
	 * @param nPage 
	 * @param sortType
	 * @param lang
	 * @param cListingType 
	 * @param cCurrencyType 
	 * @return items with the same id (used in registration, where shows similar items with price)
	 */
	public List<WorldExchangeHolder> getItemBids(List<Integer> ids, int nPage, WorldExchangeSortType sortType, Language lang, int cListingType, int cCurrencyType)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return Collections.emptyList();

		final List<WorldExchangeHolder> returnList = new ArrayList<>();
		for(WorldExchangeHolder holder : _itemBids.values())
		{
			if(holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
				continue;
			if(cCurrencyType != 2 && holder.getCurrencyType() != cCurrencyType)
				continue;
			if(cListingType != 2 && holder.getListingType() != cListingType)
				continue;

			if(ids.contains(holder.getItemInstance().getItemId()) && (holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED))
				returnList.add(holder);
		}

		return sortList(nPage, returnList, sortType, lang, cListingType);
	}

	/**
	 * @param nPage 
	 * @param unsortedList
	 * @param sortType
	 * @param lang
	 * @param cListingType 
	 * @param cCurrencyType 
	 * @return sort items by type if it needs 399 - that max value which can been in list buffer size - 32768 - list has 11 + cycle of 82 bytes - 32768 / 82 = 399.6 = 32718 for item info + 50 reserved = 32729 item info and initial data + 39 reserved
	 */
	private List<WorldExchangeHolder> sortList(int nPage, List<WorldExchangeHolder> unsortedList, WorldExchangeSortType sortType, Language lang, int cListingType)
	{
		final List<WorldExchangeHolder> sortedList = new ArrayList<>(unsortedList);
		switch(sortType)
		{
			case LISTING_TYPE_ASCE:
			{
				Collections.sort(sortedList, Comparator.comparing(WorldExchangeHolder::getListingType));
				break;
			}
			case LISTING_TYPE_DESC:
			{
				Collections.sort(sortedList, Comparator.comparing(WorldExchangeHolder::getListingType));
				Collections.reverse(sortedList);
				break;
			}
			case PRICE_ASCE:
			{
				Collections.sort(sortedList, Comparator.comparing(WorldExchangeHolder::getPrice));
				break;
			}
			case PRICE_DESC:
			{
				Collections.sort(sortedList, Comparator.comparing(WorldExchangeHolder::getPrice));
				Collections.reverse(sortedList);
				break;
			}
			case ITEM_NAME_ASCE:
			{
				Collections.sort(sortedList, Comparator.comparing(o -> (o.getItemInstance().isBlessed() ? "Blessed " : "") + o.getItemInstance().getName()));
				break;
			}
			case ITEM_NAME_DESC:
			{
				Collections.sort(sortedList, Comparator.comparing(o -> (o.getItemInstance().isBlessed() ? "Blessed " : "") + o.getItemInstance().getName()));

				Collections.reverse(sortedList);
				break;
			}
			case PRICE_PER_PIECE_ASCE:
			{
				Collections.sort(sortedList, Comparator.comparingLong(WorldExchangeHolder::getPrice));
				break;
			}
			case PRICE_PER_PIECE_DESC:
			{
				Collections.sort(sortedList, Comparator.comparingLong(WorldExchangeHolder::getPrice).reversed());
				break;
			}
			default:
				break;
		}

		/*if(sortedList.size() > 100)
			return sortedList.subList(0, 100);

		return sortedList;*/
		
    int itemsPerPage = 100;
    int totalItems = sortedList.size();
    int fromIndex = nPage * itemsPerPage;
    int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);

    if (fromIndex >= totalItems) 
        return new ArrayList<>(); // Return an empty list if the requested page is out of range 

    return sortedList.subList(fromIndex, toIndex);
	}

	/**
	 * @param ownerId
	 * @return items which will bid player
	 */
	public Map<WorldExchangeItemStatusType, List<WorldExchangeHolder>> getPlayerBids(int ownerId)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return Collections.emptyMap();

		final List<WorldExchangeHolder> registered = new ArrayList<>();
		final List<WorldExchangeHolder> sold = new ArrayList<>();
		final List<WorldExchangeHolder> outTime = new ArrayList<>();
		for(WorldExchangeHolder holder : _itemBids.values())
		{
			if(holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
				continue;

			if(holder.getOldOwnerId() != ownerId)
				continue;

			switch(holder.getStoreType())
			{
				case WORLD_EXCHANGE_REGISTERED:
				{
					registered.add(holder);
					break;
				}
				case WORLD_EXCHANGE_SOLD:
				{
					sold.add(holder);
					break;
				}
				case WORLD_EXCHANGE_OUT_TIME:
				{
					outTime.add(holder);
					break;
				}
				default:
					break;
			}
		}

		final EnumMap<WorldExchangeItemStatusType, List<WorldExchangeHolder>> returnMap = new EnumMap<>(WorldExchangeItemStatusType.class);
		returnMap.put(WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED, registered);
		returnMap.put(WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, sold);
		returnMap.put(WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME, outTime);

		for (WorldExchangeItemStatusType type : WorldExchangeItemStatusType.values())
			returnMap.putIfAbsent(type, new ArrayList<>());

		return returnMap;
	}

	public void addCategoryType(List<Integer> itemIds, int category)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		for(int itemId : itemIds)
		{
			_itemCategories.putIfAbsent(itemId, WorldExchangeItemSubType.getWorldExchangeItemSubType(category));
		}
	}

	/**
	 * Will send player alarm on WorldEnter if player has success sold items or items, if time is out
	 * @param player
	 */
	public void checkPlayerSellAlarm(Player player)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		for(WorldExchangeHolder holder : _itemBids.values())
		{
			if((holder.getOldOwnerId() == player.getObjectId()) && ((holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD)
					|| (holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME)))
			{
				player.sendPacket(new ExWorldExchangeSellCompleteAlarm(holder.getItemInstance().getItemId(), holder.getItemInstance().getCount()));
				break;
			}
		}
	}

	/**
	 * Returns the average price of the specified item.
	 * @param itemId the ID of the item
	 * @return the average price, or 0 if there are no items with the specified ID
	 */
	public long getAveragePriceOfItem(int itemId)
	{
		long totalPrice = 0;
		long totalItemCount = 0;
		for(WorldExchangeHolder holder : _itemBids.values())
		{
			if(holder.getItemInstance().getTemplate().getItemId() != itemId)
				continue;

			totalItemCount++;
			totalPrice += holder.getPrice();
		}
		return totalItemCount == 0 ? 0 : totalPrice / totalItemCount;
	}

	public void storeMe()
	{
		abortCheckStatusTask();
		checkBidStatus();
		WorldExchangeDAO.getInstance().storeMe(_itemBids);
	}
}
