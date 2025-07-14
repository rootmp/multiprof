package l2s.gameserver.network.l2.c2s.privatestoresearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.string.ItemNameHolder;
import l2s.gameserver.data.xml.holder.EnsoulHolder;
import l2s.gameserver.instancemanager.PrivateStoreHistoryManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.PrivateStoreHistoryItem;
import l2s.gameserver.model.items.TradeItem;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.privatestoresearch.ExPrivateStoreSearchHistory;
import l2s.gameserver.network.l2.s2c.privatestoresearch.ExPrivateStoreSearchItem;
import l2s.gameserver.templates.item.EtcItemTemplate;
import l2s.gameserver.templates.item.ExItemType;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.Language;

public class RequestExPrivateStoreSearchList implements IClientIncomingPacket
{
	private String sSearchWord;
	private int cStoreType;
	private int cItemType;
	private int cItemSubtype;
	private boolean bSearchCollection;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		sSearchWord = packet.readString();
		cStoreType = packet.readC(); // 0 - sell, 1 - buy, 3 - all
		cItemType = packet.readC(); // 0 - equip, 2 - gain, 4 - grocery, 255 - collection
		cItemSubtype = packet.readC();
		bSearchCollection = packet.readC() > 0;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		final Predicate<ItemTemplate> filter;

		if(bSearchCollection)
		{
			if(cItemSubtype == 255)
			{
				switch(cItemType)
				{
					case 255:
						filter = ItemTemplate::isCollection; // Все
						break;
					case 0:
						filter = (i) -> i.isCollection() && i.isEquipment(); // Снаряжение
						break;
					case 2:
						filter = null; // TODO: Усиленные предметы
						break;
					case 4:
						filter = (i) -> i.isCollection() && !i.isEquipment(); // Прочее
						break;
					default:
						filter = null;
						break;
				}
			}
			else
			{
				filter = null;
			}
		}
		else
		{
			switch(cItemType)
			{
				case 0: // Снаряжение
					switch(cItemSubtype)
					{
						case 255:
							filter = ItemTemplate::isEquipment; // Все
							break;
						case 0:
							filter = ItemTemplate::isWeapon; // Оружие
							break;
						case 1:
							filter = ItemTemplate::isArmor; // Доспехи
							break;
						case 2:
							filter = ItemTemplate::isAccessory; // Аксессуары
							break;
						case 3:
							filter = (i) -> { // Прочее
								if(!i.isEquipment())
									return false;
								if(i.isWeapon())
									return false;
								if(i.isArmor())
									return false;
								return !i.isAccessory();
							};
							break;
						default:
							filter = null;
							break;
					};
					break;
				case 2: // Усиление
					switch(cItemSubtype)
					{
						case 255:
							filter = (i) -> { // Все
								if(i.getExType() == ExItemType.SCROLL_ENCHANT_WEAPON || i.getExType() == ExItemType.SCROLL_ENCHANT_ARMOR)
									return true;
								if(EnsoulHolder.getInstance().isEnsoulItem(i.getItemId()))
									return true;
								if(i.getExType() == ExItemType.LIFE_STONE)
									return true;
								if(i.getExType() == ExItemType.DYES)
									return true;
								if(i.getExType() == ExItemType.SPELLBOOK)
									return true;
								return i.getItemId() == 91031 || i.getItemId() == 91032 || i.getItemId() == 91033 || i.getItemId() == 91034;
							};
							break;
						case 8:
							filter = (i) -> i.getExType() == ExItemType.SCROLL_ENCHANT_WEAPON || i.getExType() == ExItemType.SCROLL_ENCHANT_ARMOR; // Свиток
							// улучшения
							break;
						case 17:
							filter = (i) -> EnsoulHolder.getInstance().isEnsoulItem(i.getItemId()); // Кристалл
							break;
						case 15:
							filter = (i) -> i.getExType() == ExItemType.LIFE_STONE; // Камень Жизни
							break;
						case 16:
							filter = (i) -> i.getExType() == ExItemType.DYES; // Краски
							break;
						case 18:
							filter = (i) -> i.getExType() == ExItemType.SPELLBOOK; // Книги Заклинаний
							break;
						case 19:
							filter = (i) -> i.getItemId() == 91031 || i.getItemId() == 91032 || i.getItemId() == 91033 || i.getItemId() == 91034; // Прочее
							break;
						default:
							filter = null;
							break;
					};
					break;
				case 4: // Бакалея
					switch(cItemSubtype)
					{
						case 255:
							filter = (i) -> { // Все
								if(i.getItemType() == EtcItemTemplate.EtcItemType.POTION || i.getItemType() == EtcItemTemplate.EtcItemType.SCROLL)
									return true;
								if(i.getItemId() == 90045 || i.getItemId() == 91972)
									return true;

								String itemName = ItemNameHolder.getInstance().getItemName(Language.ENGLISH, i.getItemId());
								if(itemName != null && itemName.contains("Ticket"))
									return true;

								if(!i.getCapsuledItems().isEmpty())
									return true;
								if(i.getItemType() == EtcItemTemplate.EtcItemType.RECIPE)
									return true;
								if(i.getItemType() == EtcItemTemplate.EtcItemType.MATERIAL)
									return true;
								return i.getItemId() == 49756 || i.getItemId() == 91076 || i.getItemId() == 94716 || i.getItemId() == 96623 || i.getItemId() == 96630
										|| i.getItemId() == 93934;
							};
							break;
						case 20:
							filter = (i) -> i.getItemType() == EtcItemTemplate.EtcItemType.POTION || i.getItemType() == EtcItemTemplate.EtcItemType.SCROLL; // Зелья/свитки
							break;
						case 21:
							filter = (i) -> { // Билет
								if(i.getItemId() == 90045 || i.getItemId() == 91972)
									return true;
								String itemName = ItemNameHolder.getInstance().getItemName(Language.ENGLISH, i.getItemId());
								return itemName != null && itemName.contains("Ticket");
							};
							break;
						case 22:
							filter = (i) -> { // Сундук/Создание
								if(!i.getCapsuledItems().isEmpty())
									return true;
								if(i.getItemType() == EtcItemTemplate.EtcItemType.RECIPE)
									return true;
								if(i.getItemType() == EtcItemTemplate.EtcItemType.MATERIAL)
									return true;
								return i.getItemId() == 49756 || i.getItemId() == 91076 || i.getItemId() == 94716 || i.getItemId() == 96623 || i.getItemId() == 96630
										|| i.getItemId() == 93934;
							};
							break;
						case 24: // Прочее
							filter = null; // TODO: Fish...
							break;
						default:
							filter = null;
							break;
					};
					break;
				default:
					filter = null;
					break;
			}
		}

		List<ExPrivateStoreSearchItem.Item> items = new ArrayList<>();
		Set<Integer> itemIds = new HashSet<>();
		if(filter != null)
		{
			Predicate<TradeItem> itemFilter = (i) -> {
				if(!filter.test(i.getItem()))
					return false;
				if(StringUtils.isEmpty(sSearchWord))
					return true;
				String itemName = ItemNameHolder.getInstance().getItemName(activeChar, i.getItemId());
				return itemName == null || itemName.toLowerCase().contains(sSearchWord.toLowerCase());
			};

			for(Player player : GameObjectsStorage.getPlayers(false, true))
			{
				if(player.getPrivateStoreType() == Player.STORE_PRIVATE_SELL)
				{
					if(cStoreType == 1)
						continue;

					Map<Integer, TradeItem> sellList = player.getSellList(false);
					if(sellList == null)
						continue;

					sellList.values().stream().filter(itemFilter).forEach(tradeItem -> {
						items.add(new ExPrivateStoreSearchItem.Item(player.getName(), PrivateStoreHistoryManager.STORE_TYPE_SELL, player.getLoc(), tradeItem));
						itemIds.add(tradeItem.getItemId());
					});
				}
				else if(player.getPrivateStoreType() == Player.STORE_PRIVATE_BUY)
				{
					if(cStoreType == 0)
						continue;

					List<TradeItem> buyList = player.getBuyList();
					if(buyList == null)
						continue;

					buyList.stream().filter(itemFilter).forEach(tradeItem -> {
						items.add(new ExPrivateStoreSearchItem.Item(player.getName(), PrivateStoreHistoryManager.STORE_TYPE_BUY, player.getLoc(), tradeItem));
						itemIds.add(tradeItem.getItemId());
					});
				}
			}
		}
		if(items.isEmpty())
		{
			activeChar.sendPacket(new ExPrivateStoreSearchItem(1, 1, items));
		}
		else
		{
			int maxPage = (int) Math.ceil(items.size() / (double) ExPrivateStoreSearchItem.ITEMS_LIMIT_PER_PAGE);
			for(int currentPage = 1; currentPage <= maxPage; currentPage++)
			{
				int endIndex = currentPage * ExPrivateStoreSearchItem.ITEMS_LIMIT_PER_PAGE;
				int startIndex = endIndex - ExPrivateStoreSearchItem.ITEMS_LIMIT_PER_PAGE;
				activeChar.sendPacket(new ExPrivateStoreSearchItem(currentPage, maxPage, items.subList(startIndex, Math.min(items.size(), endIndex))));
			}
		}

		List<PrivateStoreHistoryItem> history = PrivateStoreHistoryManager.getInstance().getHistory();
		if(history.isEmpty())
		{
			activeChar.sendPacket(new ExPrivateStoreSearchHistory(1, 1, history));
		}
		else
		{
			history = history.stream().filter(i -> itemIds.contains(i.getItemId())).collect(Collectors.toList());
			history.sort((o1, o2) -> Integer.compare(o2.getTime(), o1.getTime()));
			int maxPage = (int) Math.ceil(history.size() / (double) ExPrivateStoreSearchHistory.ITEMS_LIMIT_PER_PAGE);
			for(int currentPage = 1; currentPage <= maxPage; currentPage++)
			{
				int endIndex = currentPage * ExPrivateStoreSearchHistory.ITEMS_LIMIT_PER_PAGE;
				int startIndex = endIndex - ExPrivateStoreSearchHistory.ITEMS_LIMIT_PER_PAGE;
				activeChar.sendPacket(new ExPrivateStoreSearchHistory(currentPage, maxPage, history.subList(startIndex, Math.min(history.size(), endIndex))));
			}
		}
	}
}