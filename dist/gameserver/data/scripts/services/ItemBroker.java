package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.data.xml.holder.RecipeHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.handler.bypass.Bypass;
import l2s.gameserver.listener.hooks.ListenerHook;
import l2s.gameserver.listener.hooks.ListenerHookType;
import l2s.gameserver.listener.script.OnInitScriptListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.base.Element;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ManufactureItem;
import l2s.gameserver.model.items.TradeItem;
import l2s.gameserver.network.l2.s2c.ExCharInfo;
import l2s.gameserver.network.l2.s2c.ExShowTracePacket;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.network.l2.s2c.RadarControlPacket;
import l2s.gameserver.templates.item.ExItemType;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.RecipeTemplate;
import l2s.gameserver.templates.item.data.ChancedItemData;
import l2s.gameserver.utils.Functions;
import l2s.gameserver.utils.Util;

public class ItemBroker extends ListenerHook implements OnInitScriptListener
{
	private static final int MAX_ITEMS_PER_PAGE = 10;
	private static final int MAX_PAGES_PER_LIST = 9;

	private static final long UPDATE_INFO_DELAY = TimeUnit.SECONDS.toMillis(5);

	private static final Map<Integer, NpcInfo> NPC_INFOS = new ConcurrentHashMap<>();

	private static class NpcInfo
	{
		public long lastUpdate;
		public TreeMap<String, TreeMap<Long, Item>> bestSellItems;
		public TreeMap<String, TreeMap<Long, Item>> bestBuyItems;
		public TreeMap<String, TreeMap<Long, Item>> bestCraftItems;
	}

	public static class Item
	{
		public final int itemId;
		public final int itemObjId;
		public final int type;
		public final long price;
		public final long count;
		public final int enchant;
		public final String name;
		public final int merchantObjectId;
		public final String merchantName;
		public final Location player;
		public final TradeItem item;
		public final boolean isPackage;

		public Item(int itemId, int type, long price, long count, int enchant, String itemName, int mobjectId, String merchantName, Location player, int itemObjId, TradeItem item, boolean isPkg)
		{
			this.itemId = itemId;
			this.type = type;
			this.price = price;
			this.count = count;
			this.enchant = enchant;

			StringBuilder out = new StringBuilder(70);
			if (enchant > 0)
			{
				out.append("<font color=\"7CFC00\">+");
				out.append(enchant);
				out.append(" ");
			}
			else
				out.append("<font color=\"LEVEL\">");

			out.append(itemName);
			out.append("</font></a>");

			if (item.getAttackElement() != Element.NONE.getId())
			{
				out.append(" &nbsp;<font color=\"7CFC00\">+");
				out.append(item.getAttackElementValue());
				switch (item.getAttackElement())
				{
					case 0:
						out.append(" Fire");
						break;
					case 1:
						out.append(" Water");
						break;
					case 2:
						out.append(" Wind");
						break;
					case 3:
						out.append(" Earth");
						break;
					case 4:
						out.append(" Holy");
						break;
					case 5:
						out.append(" Unholy");
						break;
				}
				out.append("</font>");
			}
			else
			{
				final int fire = item.getDefenceFire();
				final int water = item.getDefenceWater();
				final int wind = item.getDefenceWind();
				final int earth = item.getDefenceEarth();
				final int holy = item.getDefenceHoly();
				final int unholy = item.getDefenceUnholy();
				if (fire + water + wind + earth + holy + unholy > 0)
				{
					out.append("&nbsp;<font color=\"7CFC00\">");
					if (fire > 0)
					{
						out.append("+");
						out.append(fire);
						out.append(" Fire ");
					}
					if (water > 0)
					{
						out.append("+");
						out.append(water);
						out.append(" Water ");
					}
					if (wind > 0)
					{
						out.append("+");
						out.append(wind);
						out.append(" Wind ");
					}
					if (earth > 0)
					{
						out.append("+");
						out.append(earth);
						out.append(" Earth ");
					}
					if (holy > 0)
					{
						out.append("+");
						out.append(holy);
						out.append(" Holy ");
					}
					if (unholy > 0)
					{
						out.append("+");
						out.append(unholy);
						out.append(" Unholy ");
					}
					out.append("</font>");
				}
			}
			name = out.toString();

			this.merchantObjectId = mobjectId;
			this.merchantName = merchantName;
			this.player = player;
			this.itemObjId = itemObjId;
			this.item = item;
			this.isPackage = isPkg;
		}
	}

	@Override
	public void onInit()
	{
		if (Config.ITEM_BROKER_ITEM_SEARCH)
			addHookGlobal(ListenerHookType.NPC_ASK);
	}

	private static TreeMap<String, TreeMap<Long, Item>> getItems(Player player, NpcInstance npc, int type)
	{
		if (player == null || npc == null)
			return null;
		updateInfo(npc);
		NpcInfo info = NPC_INFOS.get(npc.getObjectId());
		if (info == null)
			return null;
		switch (type)
		{
			case Player.STORE_PRIVATE_SELL:
				return info.bestSellItems;
			case Player.STORE_PRIVATE_BUY:
				return info.bestBuyItems;
			case Player.STORE_PRIVATE_MANUFACTURE:
				return info.bestCraftItems;
		}
		return null;
	}

	@Override
	public void onNpcAsk(NpcInstance npc, int ask, long reply, int state, Player player)
	{
		if (ask == -111222330)
		{
			if (!Config.ITEM_BROKER_ITEM_SEARCH)
				return;

			int type = (int) reply;

			if (type != Player.STORE_PRIVATE_SELL && type != Player.STORE_PRIVATE_BUY && type != Player.STORE_PRIVATE_MANUFACTURE)
				return;

			String typeNameRu;
			String typeNameEn;

			switch ((int) reply)
			{
				case Player.STORE_PRIVATE_SELL:
					typeNameRu = "продаваемых";
					typeNameEn = "sell";
					break;
				case Player.STORE_PRIVATE_BUY:
					typeNameRu = "покупаемых";
					typeNameEn = "buy";
					break;
				case Player.STORE_PRIVATE_MANUFACTURE:
					typeNameRu = "создаваемых";
					typeNameEn = "craft";
					break;
				default:
					return;
			}

			StringBuilder append = new StringBuilder();

			if (player.isLangRus())
			{
				append.append("Список ").append(typeNameRu).append(" товаров:<br>");

				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 0 1 0\"><font color=\"FF9900\">Весь список</font></Button><br1>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=0").append("\"><font color=\"FF9900\">Оружие</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=1").append("\"><font color=\"FF9900\">Доспехи</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=2").append("\"><font color=\"FF9900\">Аксессуары</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=3").append("\"><font color=\"FF9900\">Припасы</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=4").append("\"><font color=\"FF9900\">Для питомцев</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=5").append("\"><font color=\"FF9900\">Остальное</font></Button>");
				append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h npc_%objectId%_Chat 0\"><font color=\"FF9900\">Назад</font></Button>");

				append.append("<center><table width=240><tr>");
				append.append("<td><edit var=\"tofind\" width=140></td>");
				append.append("<td><font color=\"FF9900\"><button action=\"bypass -h htmbypass_services.ItemBroker:find ").append(type).append(" 1 $tofind\" value=\"Найти\" width=80 height=22 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.Button_DF\"/></font></td>");
				append.append("</tr></table></center>");
			}
			else
			{
				append.append("The list of goods to ").append(typeNameEn).append(":<br>");

				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 0 1 0\"><font color=\"FF9900\">List all</font></Button><br1>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=0").append("\"><font color=\"FF9900\">Weapons</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=1").append("\"><font color=\"FF9900\">Armors</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=2").append("\"><font color=\"FF9900\">Accessories</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=3").append("\"><font color=\"FF9900\">Consumes</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=4").append("\"><font color=\"FF9900\">For pets</font></Button>");
				append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=").append(type).append("&state=5").append("\"><font color=\"FF9900\">Etc</font></Button>");
				append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h npc_%objectId%_Chat 0\"><font color=\"FF9900\">Back</font></Button>");

				append.append("<center><table width=240><tr>");
				append.append("<td><edit var=\"tofind\" width=140></td>");
				append.append("<td><font color=\"FF9900\"><button action=\"bypass -h htmbypass_services.ItemBroker:find ").append(type).append(" 1 $tofind\" value=\"Find\" width=80 height=22 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.Button_DF\"/></font></td>");
				append.append("</tr></table></center>");
			}
			Functions.show(append.toString(), player, npc);
		}
		else if (ask == -111222331)
		{// Обычное снаряжение
			if (!Config.ITEM_BROKER_ITEM_SEARCH)
				return;

			int type = (int) reply;

			if (type != Player.STORE_PRIVATE_SELL && type != Player.STORE_PRIVATE_BUY && type != Player.STORE_PRIVATE_MANUFACTURE)
				return;

			StringBuilder append = new StringBuilder();
			switch (state)
			{
				case 0:
					if (player.isLangRus())
					{
						append.append("Список оружия:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 1 1 0\"><font color=\"FF9900\">Одноручные мечи</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 2 1 0\"><font color=\"FF9900\">Магические одноручные мечи</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 3 1 0\"><font color=\"FF9900\">Кинжалы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 4 1 0\"><font color=\"FF9900\">Рапиры</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 5 1 0\"><font color=\"FF9900\">Двуручные мечи</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 6 1 0\"><font color=\"FF9900\">Древние мечи</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 7 1 0\"><font color=\"FF9900\">Парные клинки</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 8 1 0\"><font color=\"FF9900\">Парные кинжалы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 9 1 0\"><font color=\"FF9900\">Одноручные дробящие</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 10 1 0\"><font color=\"FF9900\">Одноручные магические дробящие</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 11 1 0\"><font color=\"FF9900\">Двуручные дробящие</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 12 1 0\"><font color=\"FF9900\">Двуручные магические дробящие</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 13 1 0\"><font color=\"FF9900\">Парные дробящие</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 14 1 0\"><font color=\"FF9900\">Луки</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 15 1 0\"><font color=\"FF9900\">Арбалеты</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 16 1 0\"><font color=\"FF9900\">Кастеты</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 17 1 0\"><font color=\"FF9900\">Древковые</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 18 1 0\"><font color=\"FF9900\">Огнестрельные</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 19 1 0\"><font color=\"FF9900\">Другое оружие</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Назад</font></Button>");
					}
					else
					{
						append.append("The list of weapons:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 1 1 0\"><font color=\"FF9900\">One-hand swords</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 2 1 0\"><font color=\"FF9900\">Magic one-hand swords</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 3 1 0\"><font color=\"FF9900\">Daggers</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 4 1 0\"><font color=\"FF9900\">Rapiers</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 5 1 0\"><font color=\"FF9900\">Two-hand swords</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 6 1 0\"><font color=\"FF9900\">Ancient swords</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 7 1 0\"><font color=\"FF9900\">Dual swords</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 8 1 0\"><font color=\"FF9900\">Dual daggers</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 9 1 0\"><font color=\"FF9900\">One-hand blunts</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 10 1 0\"><font color=\"FF9900\">One-hand magic blunts</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 11 1 0\"><font color=\"FF9900\">Two-hand blunts</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 12 1 0\"><font color=\"FF9900\">Two-hand magic blunts</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 13 1 0\"><font color=\"FF9900\">Dual blunts</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 14 1 0\"><font color=\"FF9900\">Bows</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 15 1 0\"><font color=\"FF9900\">Crossbows</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 16 1 0\"><font color=\"FF9900\">Dual fists</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 17 1 0\"><font color=\"FF9900\">Polearms</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 18 1 0\"><font color=\"FF9900\">Pistols</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 19 1 0\"><font color=\"FF9900\">Other weapons</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\" -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Back</font></Button>");
					}
					break;
				case 1:
					if (player.isLangRus())
					{
						append.append("Список доспехов:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 20 1 0\"><font color=\"FF9900\">Шлемы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 21 1 0\"><font color=\"FF9900\">Верхние части доспехов</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 22 1 0\"><font color=\"FF9900\">Нижние части доспехов</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 23 1 0\"><font color=\"FF9900\">Костюмы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 24 1 0\"><font color=\"FF9900\">Перчатки</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 25 1 0\"><font color=\"FF9900\">Обувь</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 26 1 0\"><font color=\"FF9900\">Щиты</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 27 1 0\"><font color=\"FF9900\">Символы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 28 1 0\"><font color=\"FF9900\">Подвески</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 29 1 0\"><font color=\"FF9900\">Плащи</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Назад</font></Button>");
					}
					else
					{
						append.append("The list of armors:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 20 1 0\"><font color=\"FF9900\">Helmets</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 21 1 0\"><font color=\"FF9900\">Upper pieces</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 22 1 0\"><font color=\"FF9900\">Lower pieces</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 23 1 0\"><font color=\"FF9900\">Full-bodies</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 24 1 0\"><font color=\"FF9900\">Gloves</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 25 1 0\"><font color=\"FF9900\">Feet</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 26 1 0\"><font color=\"FF9900\">Shields</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 27 1 0\"><font color=\"FF9900\">Sigils</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 28 1 0\"><font color=\"FF9900\">Pendants</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 29 1 0\"><font color=\"FF9900\">Cloaks</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\" -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Back</font></Button>");
					}
					break;
				case 2:
					if (player.isLangRus())
					{
						append.append("Список аксессуаров:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 30 1 0\"><font color=\"FF9900\">Кольца</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 31 1 0\"><font color=\"FF9900\">Серьги</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 32 1 0\"><font color=\"FF9900\">Ожерелья</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 33 1 0\"><font color=\"FF9900\">Пояса</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 34 1 0\"><font color=\"FF9900\">Браслеты</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 35 1 0\"><font color=\"FF9900\">Агатионы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 36 1 0\"><font color=\"FF9900\">Готовные уборы</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Назад</font></Button>");
					}
					else
					{
						append.append("The list of accessories:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 30 1 0\"><font color=\"FF9900\">Rings</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 31 1 0\"><font color=\"FF9900\">Earrings</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 32 1 0\"><font color=\"FF9900\">Necklaces</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 33 1 0\"><font color=\"FF9900\">Belts</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 34 1 0\"><font color=\"FF9900\">Bracelets</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 35 1 0\"><font color=\"FF9900\">Agathions</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 36 1 0\"><font color=\"FF9900\">Hair accessories</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\" -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Back</font></Button>");
					}
					break;
				case 3:
					if (player.isLangRus())
					{
						append.append("Список припасов:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 37 1 0\"><font color=\"FF9900\">Зелья</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 38 1 0\"><font color=\"FF9900\">Свитки модификации оружия</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 39 1 0\"><font color=\"FF9900\">Свитки модификации доспехов</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 40 1 0\"><font color=\"FF9900\">Прочие свитки</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 41 1 0\"><font color=\"FF9900\">Заряды души</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 42 1 0\"><font color=\"FF9900\">Заряды духа</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Назад</font></Button>");
					}
					else
					{
						append.append("The list of consumes:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 37 1 0\"><font color=\"FF9900\">Potions</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 38 1 0\"><font color=\"FF9900\">Weapon scrolls of enchant</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 39 1 0\"><font color=\"FF9900\">Armor scrolls of enchant</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 40 1 0\"><font color=\"FF9900\">Other scrolls</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 41 1 0\"><font color=\"FF9900\">Spirit shots</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 42 1 0\"><font color=\"FF9900\">Soul shots</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\" -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Back</font></Button>");
					}
					break;
				case 4:
					if (player.isLangRus())
					{
						append.append("Список для питомцев:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 44 1 0\"><font color=\"FF9900\">Доспехи питомцев</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 45 1 0\"><font color=\"FF9900\">Припасы питомцев</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Назад</font></Button>");
					}
					else
					{
						append.append("The list of for pets:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 44 1 0\"><font color=\"FF9900\">Pet equipments</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 45 1 0\"><font color=\"FF9900\">Pet supplies</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\" -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Back</font></Button>");
					}
					break;
				case 5:
					if (player.isLangRus())
					{
						append.append("Список остального:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 46 1 0\"><font color=\"FF9900\">Кристаллы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 47 1 0\"><font color=\"FF9900\">Рецепты</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 48 1 0\"><font color=\"FF9900\">Материалы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 49 1 0\"><font color=\"FF9900\">Камни жизни</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 50 1 0\"><font color=\"FF9900\">Кристаллы души</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 51 1 0\"><font color=\"FF9900\">Кристаллы стихии</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 52 1 0\"><font color=\"FF9900\">Камни модификации оружия</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 53 1 0\"><font color=\"FF9900\">Камни модификации доспехов</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 54 1 0\"><font color=\"FF9900\">Книги заклинаний</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 55 1 0\"><font color=\"FF9900\">Самоцветы</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 56 1 0\"><font color=\"FF9900\">Кошели</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 57 1 0\"><font color=\"FF9900\">Заколки</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 58 1 0\"><font color=\"FF9900\">Магические заколки</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 59 1 0\"><font color=\"FF9900\">Магические украшения</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 60 1 0\"><font color=\"FF9900\">Краски</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 61 1 0\"><font color=\"FF9900\">Прочие предметы</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Назад</font></Button>");
					}
					else
					{
						append.append("The list of etc:<br>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 46 1 0\"><font color=\"FF9900\">Crystals</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 47 1 0\"><font color=\"FF9900\">Recipes</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 48 1 0\"><font color=\"FF9900\">Materials</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 49 1 0\"><font color=\"FF9900\">Life stones</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 50 1 0\"><font color=\"FF9900\">Soul crystals</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 51 1 0\"><font color=\"FF9900\">Elemental crystals</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 52 1 0\"><font color=\"FF9900\">Weapon enchant stones</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 53 1 0\"><font color=\"FF9900\">Armor enchant stones</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 54 1 0\"><font color=\"FF9900\">Spellbooks</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 55 1 0\"><font color=\"FF9900\">Gemstones</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 56 1 0\"><font color=\"FF9900\">Pouches</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 57 1 0\"><font color=\"FF9900\">Pins</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 58 1 0\"><font color=\"FF9900\">Magic rune clips</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 59 1 0\"><font color=\"FF9900\">Magic ornaments</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 60 1 0\"><font color=\"FF9900\">Dyes</font></Button>");
						append.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ").append(type).append(" 61 1 0\"><font color=\"FF9900\">Other items</font></Button>");

						append.append("<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\" -h menu_select?ask=-111222330&reply=").append(type).append("\"><font color=\"FF9900\">Back</font></Button>");
					}
					break;
				default:
					return;
			}

			Functions.show(append.toString(), player, npc);
		}
	}

	@Bypass("services.ItemBroker:list")
	public void list(Player player, NpcInstance npc, String[] var)
	{
		if (player == null || npc == null)
			return;

		if (var.length != 4)
		{
			Functions.show("Некорректная длина данных", player, npc);
			return;
		}

		int type;
		int exItemType;
		int currentPage;
		int minEnchant;
		try
		{
			type = Integer.parseInt(var[0]);
			exItemType = Integer.parseInt(var[1]);
			currentPage = Integer.parseInt(var[2]);
			minEnchant = Integer.parseInt(var[3]);
		}
		catch (Exception e)
		{
			Functions.show("Некорректные данные", player, npc);
			return;
		}

		TreeMap<String, TreeMap<Long, Item>> allItems = getItems(player, npc, type);
		if (allItems == null)
		{
			Functions.show("Ошибка - предметов такого типа не найдено", player, npc);
			return;
		}

		List<Item> items = new ArrayList<>(allItems.size() * 10);
		for (TreeMap<Long, Item> tempItems : allItems.values())
		{
			TreeMap<Long, Item> tempItems2 = new TreeMap<>();
			for (Map.Entry<Long, Item> entry : tempItems.entrySet())
			{
				Item tempItem = entry.getValue();
				if (tempItem == null)
					continue;
				if (tempItem.enchant < minEnchant)
					continue;
				ItemTemplate temp = tempItem.item.getItem();
				if (temp == null)
					continue;
				if (exItemType > 0 && exItemType != temp.getExType().ordinal())
					continue;
				tempItems2.put(entry.getKey(), tempItem);
			}
			if (tempItems2.isEmpty())
				continue;

			Item item = type == Player.STORE_PRIVATE_BUY ? tempItems2.lastEntry().getValue() : tempItems2.firstEntry().getValue();
			if (item != null)
				items.add(item);
		}

		StringBuilder out = new StringBuilder(200);
		if (exItemType > 0)
		{
			out.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222331&reply=");
			out.append(type);
			out.append("&state=");
			out.append(ExItemType.VALUES[exItemType].mask());
			out.append("\">««</Button>&nbsp;&nbsp;");
		}
		else
		{
			out.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222330&reply=");
			out.append(type);
			out.append("\">««</Button>&nbsp;&nbsp;");
		}

		int totalPages = items.size();
		totalPages = totalPages / MAX_ITEMS_PER_PAGE + (totalPages % MAX_ITEMS_PER_PAGE > 0 ? 1 : 0);
		totalPages = Math.max(1, totalPages);
		currentPage = Math.min(totalPages, Math.max(1, currentPage));

		if (totalPages > 1)
		{
			int page = Math.max(1, Math.min(totalPages - MAX_PAGES_PER_LIST + 1, currentPage - MAX_PAGES_PER_LIST / 2));

			// линк на первую страницу
			if (page > 1)
				listPageNum(out, type, exItemType, 1, minEnchant, "1");
			// линк на страницу - 10
			if (currentPage > 11)
				listPageNum(out, type, exItemType, currentPage - 10, minEnchant, String.valueOf(currentPage - 10));
			// линк на предыдущую страницу
			if (currentPage > 1)
				listPageNum(out, type, exItemType, currentPage - 1, minEnchant, "<");

			for (int count = 0; count < MAX_PAGES_PER_LIST && page <= totalPages; count++, page++)
			{
				if (page == currentPage)
					out.append(page).append("&nbsp;");
				else
					listPageNum(out, type, exItemType, page, minEnchant, String.valueOf(page));
			}

			// линк на следующую страницу
			if (currentPage < totalPages)
				listPageNum(out, type, exItemType, currentPage + 1, minEnchant, ">");
			// линк на страницу + 10
			if (currentPage < totalPages - 10)
				listPageNum(out, type, exItemType, currentPage + 10, minEnchant, String.valueOf(currentPage + 10));
			// линк на последнюю страницу
			if (page <= totalPages)
				listPageNum(out, type, exItemType, totalPages, minEnchant, String.valueOf(totalPages));
		}

		out.append("<table width=100%>");

		if (items.size() > 0)
		{
			int count = 0;
			ListIterator<Item> iter = items.listIterator((currentPage - 1) * MAX_ITEMS_PER_PAGE);
			while (iter.hasNext() && count < MAX_ITEMS_PER_PAGE)
			{
				Item item = iter.next();
				ItemTemplate temp = item.item.getItem();
				if (temp == null)
					continue;

				out.append("<tr><td>");
				out.append(String.format("<img src=\"%s\" width=32 height=32>", temp.getIcon()));
				out.append("</td><td><table width=100%><tr><td><a action=\"bypass -h htmbypass_services.ItemBroker:listForItem ");
				out.append(type);
				out.append(" ");
				out.append(item.itemId);
				out.append(" ");
				out.append(minEnchant);
				out.append(" ");
				out.append(exItemType);
				out.append(" 1 ");
				out.append(currentPage);
				out.append("\">");
				out.append(item.name);
				out.append("</td></tr><tr><td>");
				out.append(player.isLangRus() ? "цена" : "price");
				out.append(": ");
				out.append(Util.formatAdena(item.price));
				if (item.isPackage)
					out.append(player.isLangRus() ? "Упаковка" : " (Package)");
				if (temp.isStackable())
				{
					out.append(", ");
					out.append(player.isLangRus() ? "количество" : "count");
					out.append(": ").append(Util.formatAdena(item.count));
				}
				out.append("</td></tr></table></td></tr>");
				count++;
			}
		}
		else if (player.isLangRus())
			out.append("<tr><td colspan=2>Ничего не найдено.</td></tr>");
		else
			out.append("<tr><td colspan=2>Nothing found.</td></tr>");

		out.append("</table><br>&nbsp;");

		Functions.show(out.toString(), player, npc);
	}

	private static void listPageNum(StringBuilder out, int type, int exItemType, int page, int minEnchant, String letter)
	{
		out.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:list ");
		out.append(type);
		out.append(" ");
		out.append(exItemType);
		out.append(" ");
		out.append(page);
		out.append(" ");
		out.append(minEnchant);
		out.append("\">");
		out.append(letter);
		out.append("</Button>&nbsp;");
	}

	@Bypass("services.ItemBroker:listForItem")
	public void listForItem(Player player, NpcInstance npc, String[] var)
	{
		if (player == null || npc == null)
			return;

		if (var.length < 6 || var.length > 11)
		{
			Functions.show("Некорректная длина данных", player, npc);
			return;
		}

		int type;
		int itemId;
		int minEnchant;
		// нужны только для запоминания, на какую страницу возвращаться
		int exItemType;
		int currentPage;
		int returnPage;
		String[] search = null;

		try
		{
			type = Integer.parseInt(var[0]);
			itemId = Integer.parseInt(var[1]);
			minEnchant = Integer.parseInt(var[2]);
			exItemType = Integer.parseInt(var[3]);
			currentPage = Integer.parseInt(var[4]);
			returnPage = Integer.parseInt(var[5]);
			if (var.length > 6)
			{
				search = new String[var.length - 6];
				System.arraycopy(var, 6, search, 0, search.length);
			}
		}
		catch (Exception e)
		{
			Functions.show("Некорректные данные", player, npc);
			return;
		}

		ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
		if (template == null)
		{
			Functions.show("Ошибка - itemId не определен.", player, npc);
			return;
		}

		TreeMap<String, TreeMap<Long, Item>> tmpItems = getItems(player, npc, type);
		if (tmpItems == null)
		{
			Functions.show("Ошибка - такой тип предмета отсутствует.", player, npc);
			return;
		}

		TreeMap<Long, Item> allItems = tmpItems.get(template.getName());
		if (allItems == null)
		{
			Functions.show("Ошибка - предметов с таким названием не найдено.", player, npc);
			return;
		}

		StringBuilder out = new StringBuilder(200);
		if (search == null) // возврат в список
			listPageNum(out, type, exItemType, returnPage, minEnchant, "««");
		else // возврат в поиск
			findPageNum(out, type, returnPage, search, "««");
		out.append("&nbsp;&nbsp;");

		NavigableMap<Long, Item> sortedItems = type == Player.STORE_PRIVATE_BUY ? allItems.descendingMap() : allItems;
		if (sortedItems == null)
		{
			Functions.show("Ошибка - ничего не найдено.", player, npc);
			return;
		}

		List<Item> items = new ArrayList<>(sortedItems.size());
		for (Item item : sortedItems.values())
		{
			if (item == null || item.enchant < minEnchant)
				continue;

			items.add(item);
		}

		int totalPages = items.size();
		totalPages = totalPages / MAX_ITEMS_PER_PAGE + (totalPages % MAX_ITEMS_PER_PAGE > 0 ? 1 : 0);
		totalPages = Math.max(1, totalPages);
		currentPage = Math.min(totalPages, Math.max(1, currentPage));

		if (totalPages > 1)
		{
			int page = Math.max(1, Math.min(totalPages - MAX_PAGES_PER_LIST + 1, currentPage - MAX_PAGES_PER_LIST / 2));

			// линк на первую страницу
			if (page > 1)
				listForItemPageNum(out, type, itemId, minEnchant, exItemType, 1, returnPage, search, "1");
			// линк на страницу - 10
			if (currentPage > 11)
				listForItemPageNum(out, type, itemId, minEnchant, exItemType, currentPage - 10, returnPage, search, String.valueOf(currentPage - 10));
			// линк на предыдущую страницу
			if (currentPage > 1)
				listForItemPageNum(out, type, itemId, minEnchant, exItemType, currentPage - 1, returnPage, search, "<");

			for (int count = 0; count < MAX_PAGES_PER_LIST && page <= totalPages; count++, page++)
			{
				if (page == currentPage)
					out.append(page).append("&nbsp;");
				else
					listForItemPageNum(out, type, itemId, minEnchant, exItemType, page, returnPage, search, String.valueOf(page));
			}

			// линк на следующую страницу
			if (currentPage < totalPages)
				listForItemPageNum(out, type, itemId, minEnchant, exItemType, currentPage + 1, returnPage, search, ">");
			// линк на страницу + 10
			if (currentPage < totalPages - 10)
				listForItemPageNum(out, type, itemId, minEnchant, exItemType, currentPage + 10, returnPage, search, String.valueOf(currentPage + 10));
			// линк на последнюю страницу
			if (page <= totalPages)
				listForItemPageNum(out, type, itemId, minEnchant, exItemType, totalPages, returnPage, search, String.valueOf(totalPages));
		}

		out.append("<table width=100%>");

		if (items.size() > 0)
		{
			int count = 0;
			ListIterator<Item> iter = items.listIterator((currentPage - 1) * MAX_ITEMS_PER_PAGE);
			while (iter.hasNext() && count < MAX_ITEMS_PER_PAGE)
			{
				Item item = iter.next();
				ItemTemplate temp = item.item.getItem();
				if (temp == null)
					continue;

				out.append("<tr><td>");
				out.append(String.format("<img src=\"%s\" width=32 height=32>", temp.getIcon()));
				out.append("</td><td><table width=100%><tr><td><a action=\"bypass -h htmbypass_services.ItemBroker:path ");
				out.append(type);
				out.append(" ");
				out.append(item.itemId);
				out.append(" ");
				out.append(item.itemObjId);
				out.append("\">");
				out.append(item.name);
				out.append("</td></tr><tr><td>");
				out.append(player.isLangRus() ? "цена" : "price");
				out.append(": ");
				out.append(Util.formatAdena(item.price));
				if (item.isPackage)
					out.append(player.isLangRus() ? "Упаковка" : " (Package)");
				if (temp.isStackable())
				{
					out.append(", ");
					out.append(player.isLangRus() ? "количество" : "count");
					out.append(": ").append(Util.formatAdena(item.count));
				}
				out.append(", ");
				out.append(player.isLangRus() ? "хозяин" : "owner");
				out.append(": ").append(item.merchantName);
				out.append("</td></tr></table></td></tr>");
				count++;
			}
		}
		else if (player.isLangRus())
			out.append("<tr><td colspan=2>Ничего не найдено.</td></tr>");
		else
			out.append("<tr><td colspan=2>Nothing found.</td></tr>");

		out.append("</table><br>&nbsp;");

		Functions.show(out.toString(), player, npc);
	}

	private static void listForItemPageNum(StringBuilder out, int type, int itemId, int minEnchant, int itemType, int page, int returnPage, String[] search, String letter)
	{
		out.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:listForItem ");
		out.append(type);
		out.append(" ");
		out.append(itemId);
		out.append(" ");
		out.append(minEnchant);
		out.append(" ");
		out.append(itemType);
		out.append(" ");
		out.append(page);
		out.append(" ");
		out.append(returnPage);
		if (search != null)
			for (String s : search)
			{
				out.append(" ");
				out.append(s);
			}
		out.append("\">");
		out.append(letter);
		out.append("</Button>&nbsp;");
	}

	@Bypass("services.ItemBroker:path")
	public void path(Player player, NpcInstance npc, String[] var)
	{
		if (player == null || npc == null)
			return;

		if (var.length != 3)
		{
			Functions.show("Некорректная длина данных", player, npc);
			return;
		}

		int type;
		int itemId;
		int itemObjId;

		try
		{
			type = Integer.parseInt(var[0]);
			itemId = Integer.parseInt(var[1]);
			itemObjId = Integer.parseInt(var[2]);
		}
		catch (Exception e)
		{
			Functions.show("Некорректные данные", player, npc);
			return;
		}

		ItemTemplate temp = ItemHolder.getInstance().getTemplate(itemId);
		if (temp == null)
		{
			Functions.show("Ошибка - itemId не определен.", player, npc);
			return;
		}

		TreeMap<String, TreeMap<Long, Item>> allItems = getItems(player, npc, type);
		if (allItems == null)
		{
			Functions.show("Ошибка - предметов такого типа не найдено.", player, npc);
			return;
		}

		TreeMap<Long, Item> items = allItems.get(temp.getName());
		if (items == null)
		{
			Functions.show("Ошибка - предметов с таким именем не найдено.", player, npc);
			return;
		}

		Item item = null;
		for (Item i : items.values())
			if (i.itemObjId == itemObjId)
			{
				item = i;
				break;
			}

		if (item == null)
		{
			Functions.show("Ошибка - предмет не найден.", player, npc);
			return;
		}

		boolean found = false;
		Player trader = GameObjectsStorage.getPlayer(item.merchantObjectId);
		if (trader == null)
		{
			Functions.show("Торговец не найден, возможно он вышел из игры.", player, npc);
			return;
		}

		switch (type)
		{
			case Player.STORE_PRIVATE_SELL:
				if (trader.getSellList() != null)
				{
					if (trader.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE)
					{
						if (item.isPackage)
						{
							long packagePrice = 0;
							for (TradeItem tradeItem : trader.getSellList().values())
							{
								packagePrice += tradeItem.getOwnersPrice() * tradeItem.getCount();
								if (tradeItem.getItemId() == item.itemId)
									found = true;
							}

							if (packagePrice != item.price)
								found = false;
						}
					}
					else
					{
						if (!item.isPackage)
							for (TradeItem tradeItem : trader.getSellList().values())
								if (tradeItem.getItemId() == item.itemId && tradeItem.getOwnersPrice() == item.price)
								{
									found = true;
									break;
								}
					}
				}
				break;
			case Player.STORE_PRIVATE_BUY:
				if (trader.getBuyList() != null)
					for (TradeItem tradeItem : trader.getBuyList())
						if (tradeItem.getItemId() == item.itemId && tradeItem.getOwnersPrice() == item.price)
						{
							found = true;
							break;
						}
				break;
			case Player.STORE_PRIVATE_MANUFACTURE:
				found = true; // not done
				break;
		}

		if (!found)
		{
			if (player.isLangRus())
				Functions.show("Внимание, цена или предмет изменились, будьте осторожны !", player, npc);
			else
				Functions.show("Caution, price or item was changed, please be careful !", player, npc);
		}

		ExShowTracePacket trace = new ExShowTracePacket(60000);
		trace.addLine(player.getLoc(), item.player, 30);
		player.sendPacket(trace);
		RadarControlPacket rc = new RadarControlPacket(0, 1, item.player);
		player.sendPacket(rc);

		// Показываем игроку торговца, если тот скрыт
		if (player.getVarBoolean(Player.NO_TRADERS_VAR))
		{
			player.sendPacket(new ExCharInfo(trader, player));

			IClientOutgoingPacket packet = trader.getPrivateStoreMsgPacket(player);
			if (packet != null)
				player.sendPacket(packet);

		}

		// Устанавливаем таргет на торговца для того, чтобы быстро до него дойти
		player.setTarget(trader);
	}

	public static void updateInfo(NpcInstance npc)
	{
		NpcInfo info = NPC_INFOS.get(npc.getObjectId());
		if (info == null || info.lastUpdate < System.currentTimeMillis() - UPDATE_INFO_DELAY)
		{
			info = new NpcInfo();
			info.lastUpdate = System.currentTimeMillis();
			info.bestBuyItems = new TreeMap<>();
			info.bestSellItems = new TreeMap<>();
			info.bestCraftItems = new TreeMap<>();

			int itemObjId = 0; // Обычный objId не подходит для покупаемых предметов

			for (Player pl : World.getAroundPlayers(npc))
			{
				TreeMap<String, TreeMap<Long, Item>> items;
				Collection<TradeItem> tradeList;

				int type = pl.getPrivateStoreType();
				switch (type)
				{
					case Player.STORE_PRIVATE_SELL:
						items = info.bestSellItems;
						tradeList = pl.getSellList().values();

						for (TradeItem item : tradeList)
						{
							ItemTemplate temp = item.getItem();
							if (temp == null)
								continue;
							TreeMap<Long, Item> oldItems = items.computeIfAbsent(temp.getName(), k -> new TreeMap<>());
							Item newItem = new Item(item.getItemId(), type, item.getOwnersPrice(), item.getCount(), item.getEnchantLevel(), temp.getName(), pl.getObjectId(), pl.getName(), pl.getLoc(), item.getObjectId(), item, false);
							long key = newItem.price * 100;
							while (key < newItem.price * 100 + 100 && oldItems.containsKey(key))
								// До 100 предметов с одинаковыми ценами
								key++;
							oldItems.put(key, newItem);
						}

						break;
					case Player.STORE_PRIVATE_SELL_PACKAGE:
						items = info.bestSellItems;
						tradeList = pl.getSellList().values();

						long packagePrice = 0;
						for (TradeItem item : tradeList)
							packagePrice += item.getOwnersPrice() * item.getCount();

						for (TradeItem item : tradeList)
						{
							ItemTemplate temp = item.getItem();
							if (temp == null)
								continue;
							TreeMap<Long, Item> oldItems = items.computeIfAbsent(temp.getName(), k -> new TreeMap<>());
							Item newItem = new Item(item.getItemId(), type, packagePrice, item.getCount(), item.getEnchantLevel(), temp.getName(), pl.getObjectId(), pl.getName(), pl.getLoc(), item.getObjectId(), item, true);
							long key = newItem.price * 100;
							while (key < newItem.price * 100 + 100 && oldItems.containsKey(key))
								// До 100 предметов с одинаковыми ценами
								key++;
							oldItems.put(key, newItem);
						}

						break;
					case Player.STORE_PRIVATE_BUY:
						items = info.bestBuyItems;
						tradeList = pl.getBuyList();

						for (TradeItem item : tradeList)
						{
							ItemTemplate temp = item.getItem();
							if (temp == null)
								continue;
							TreeMap<Long, Item> oldItems = items.computeIfAbsent(temp.getName(), k -> new TreeMap<>());
							Item newItem = new Item(item.getItemId(), type, item.getOwnersPrice(), item.getCount(), item.getEnchantLevel(), temp.getName(), pl.getObjectId(), pl.getName(), pl.getLoc(), itemObjId++, item, false);
							long key = newItem.price * 100;
							while (key < newItem.price * 100 + 100 && oldItems.containsKey(key))
								// До 100 предметов с одинаковыми ценами
								key++;
							oldItems.put(key, newItem);
						}

						break;
					case Player.STORE_PRIVATE_MANUFACTURE:
						items = info.bestCraftItems;
						Collection<ManufactureItem> createList = pl.getCreateList().values();
						for (ManufactureItem mitem : createList)
						{
							int recipeId = mitem.getRecipeId();
							RecipeTemplate recipe = RecipeHolder.getInstance().getRecipeByRecipeId(recipeId);
							if (recipe == null || recipe.getProducts().length == 0)
								continue;

							ChancedItemData product = recipe.getProducts()[0];
							ItemTemplate temp = ItemHolder.getInstance().getTemplate(product.getId());
							if (temp == null)
								continue;

							TreeMap<Long, Item> oldItems = items.computeIfAbsent(temp.getName(), k -> new TreeMap<>());
							Item newItem = new Item(product.getId(), type, mitem.getCost(), product.getCount(), 0, temp.getName(), pl.getObjectId(), pl.getName(), pl.getLoc(), itemObjId++, null, false);
							long key = newItem.price * 100;
							while (key < newItem.price * 100 + 100 && oldItems.containsKey(key))
								// До 100 предметов с одинаковыми ценами
								key++;
							oldItems.put(key, newItem);
						}
						break;
				}
			}
			NPC_INFOS.put(npc.getObjectId(), info);
		}
	}

	@Bypass("services.ItemBroker:find")
	public void find(Player player, NpcInstance npc, String[] var)
	{
		if (player == null || npc == null)
			return;

		if (var.length < 3 || var.length > 7)
		{
			if (player.isLangRus())
				Functions.show("Пожалуйста введите от 1 до 16 символов.<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h npc_%objectId%_Chat 0\"><font color=\"FF9900\">Назад</font></Button>", player, npc);
			else
				Functions.show("Please enter from 1 up to 16 symbols.<br><Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h npc_%objectId%_Chat 0\"><font color=\"FF9900\">Back</font></Button>", player, npc);
			return;
		}

		int type;
		int currentPage;
		int minEnchant = 0;
		String[] search;

		try
		{
			type = Integer.parseInt(var[0]);
			currentPage = Integer.parseInt(var[1]);
			search = new String[var.length - 2];
			String line;
			for (int i = 0; i < search.length; i++)
			{
				line = var[i + 2].trim().toLowerCase();
				search[i] = line;
				if (line.length() > 1 && line.startsWith("+"))
					minEnchant = Integer.parseInt(line.substring(1));
			}
		}
		catch (Exception e)
		{
			Functions.show("Некорректные данные", player, npc);
			return;
		}

		TreeMap<String, TreeMap<Long, Item>> allItems = getItems(player, npc, type);
		if (allItems == null)
		{
			Functions.show("Ошибка - предметов с таким типом не найдено.", player, npc);
			return;
		}

		List<Item> items = new ArrayList<>();
		String line;
		TreeMap<Long, Item> itemMap;
		Item item;
		mainLoop: for (Map.Entry<String, TreeMap<Long, Item>> entry : allItems.entrySet())
		{
			for (String s : search)
			{
				line = s;
				if (line.startsWith("+"))
					continue;
				if (!entry.getKey().toLowerCase().contains(line))
					continue mainLoop;
			}

			itemMap = entry.getValue();
			item = null;
			for (Item itm : itemMap.values()) // Ищем первый подходящий предмет
				if (itm != null && itm.enchant >= minEnchant)
				{
					item = itm;
					break;
				}

			if (item != null)
				items.add(item);
		}

		StringBuilder out = new StringBuilder(200);
		out.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h menu_select?ask=-111222330&reply=");
		out.append(type);
		out.append("\">««</Button>&nbsp;&nbsp;");

		int totalPages = items.size();
		totalPages = totalPages / MAX_ITEMS_PER_PAGE + (totalPages % MAX_ITEMS_PER_PAGE > 0 ? 1 : 0);
		totalPages = Math.max(1, totalPages);
		currentPage = Math.min(totalPages, Math.max(1, currentPage));

		if (totalPages > 1)
		{
			int page = Math.max(1, Math.min(totalPages - MAX_PAGES_PER_LIST + 1, currentPage - MAX_PAGES_PER_LIST / 2));

			// линк на первую страницу
			if (page > 1)
				findPageNum(out, type, 1, search, "1");
			// линк на страницу - 10
			if (currentPage > 11)
				findPageNum(out, type, currentPage - 10, search, String.valueOf(currentPage - 10));
			// линк на предыдущую страницу
			if (currentPage > 1)
				findPageNum(out, type, currentPage - 1, search, "<");

			for (int count = 0; count < MAX_PAGES_PER_LIST && page <= totalPages; count++, page++)
			{
				if (page == currentPage)
					out.append(page).append("&nbsp;");
				else
					findPageNum(out, type, page, search, String.valueOf(page));
			}

			// линк на следующую страницу
			if (currentPage < totalPages)
				findPageNum(out, type, currentPage + 1, search, ">");
			// линк на страницу + 10
			if (currentPage < totalPages - 10)
				findPageNum(out, type, currentPage + 10, search, String.valueOf(currentPage + 10));
			// линк на последнюю страницу
			if (page <= totalPages)
				findPageNum(out, type, totalPages, search, String.valueOf(totalPages));
		}

		out.append("<table width=100%>");

		if (items.size() > 0)
		{
			int count = 0;
			ListIterator<Item> iter = items.listIterator((currentPage - 1) * MAX_ITEMS_PER_PAGE);
			while (iter.hasNext() && count < MAX_ITEMS_PER_PAGE)
			{
				item = iter.next();
				ItemTemplate temp = item.item.getItem();
				if (temp == null)
					continue;

				out.append("<tr><td>");
				out.append(String.format("<img src=\"%s\" width=32 height=32>", temp.getIcon()));
				out.append("</td><td><table width=100%><tr><td><a action=\"bypass -h htmbypass_services.ItemBroker:listForItem ");
				out.append(type);
				out.append(" ");
				out.append(item.itemId);
				out.append(" ");
				out.append(minEnchant);
				out.append(" 0 1 ");
				out.append(currentPage);
				for (String s : search)
				{
					out.append(" ");
					out.append(s);
				}
				out.append("\">");
				out.append("<font color=\"LEVEL\">");
				out.append(temp.getName(player)); // Здесь берем название из шаблона
				out.append("</font></a>");
				out.append("</td></tr>");
				out.append("</table></td></tr>");
				count++;
			}
		}
		else if (player.isLangRus())
			out.append("<tr><td colspan=2>Ничего не найдено.</td></tr>");
		else
			out.append("<tr><td colspan=2>Nothing found.</td></tr>");
		out.append("</table><br>&nbsp;");

		Functions.show(out.toString(), player, npc);
	}

	private static void findPageNum(StringBuilder out, int type, int page, String[] search, String letter)
	{
		out.append("<Button ALIGN=LEFT ICON=\"NORMAL\" action=\"bypass -h htmbypass_services.ItemBroker:find ");
		out.append(type);
		out.append(" ");
		out.append(page);
		if (search != null)
		{
			for (String s : search)
			{
				out.append(" ");
				out.append(s);
			}
		}
		out.append("\">");
		out.append(letter);
		out.append("</Button>&nbsp;");
	}
}