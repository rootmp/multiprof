package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.MultiSellListContainer;
import l2s.gameserver.model.base.MultiSellEntry;
import l2s.gameserver.model.base.MultiSellIngredient;
import l2s.gameserver.templates.item.ItemTemplate;

public class MultiSellListPacket extends L2GameServerPacket
{
	public enum WindowType
	{
		NORMAL,
		CRAFT,
		MATERIALS,
		BLACK_COUPON
	}

	private final int _page;
	private final int _finished;
	private final int _listId;
	private final int _type;
	private final List<MultiSellEntry> _list;
	private final WindowType _windowType;

	public MultiSellListPacket(MultiSellListContainer list, int page, int finished, WindowType windowType)
	{
		_list = list.getEntries();
		_listId = list.getListId();
		_type = list.getType().ordinal();
		_page = page;
		_finished = finished;
		_windowType = windowType;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x00); // UNK
		writeD(_listId); // list id
		writeC(_windowType.ordinal()); // UNK
		writeD(_page); // page
		writeD(_finished); // finished
		writeD(Config.MULTISELL_SIZE); // size of pages
		writeD(_list.size()); // list length
		writeC(0x00); // [TODO]: Grand Crusade
		writeC(_type);// Type (0x00 - Нормальный, 0xD0 - с шансом)
		writeD(0x20); // UNK
		List<MultiSellIngredient> ingredients;
		for (MultiSellEntry ent : _list)
		{
			ingredients = fixIngredients(ent.getIngredients());

			writeD(ent.getEntryId());
			writeC(!ent.getProduction().isEmpty() && ent.getProduction().get(0).isStackable() ? 1 : 0); // stackable?
			writeH(0x00); // enchant level
			writeD(0x00); // инкрустация
			writeD(0x00); // инкрустация

			writeItemElements();

			int saCount = 0;
			writeC(0x00); // SA 1 count
			for (int i = 0; i < saCount; i++)
			{
				writeD(0x00); // SA 1 effect
			}

			writeC(0x00); // SA 2 count
			for (int i = 0; i < saCount; i++)
			{
				writeD(0x00); // SA 2 effect
			}

			writeC(0x00); // 286 - SA 3 count
			for (int i = 0; i < saCount; i++)
			{
				writeD(0x00); // SA 3 effect
			}

			writeH(ent.getProduction().size());
			writeH(ingredients.size());

			for (MultiSellIngredient prod : ent.getProduction())
			{
				int itemId = prod.getItemId();
				ItemTemplate template = itemId > 0 ? ItemHolder.getInstance().getTemplate(prod.getItemId()) : null;
				writeD(itemId);
				writeQ(itemId > 0 ? template.getBodyPart() : 0);
				writeH(itemId > 0 ? template.getType2() : 0);
				writeQ(prod.getItemCount());
				writeH(prod.getItemEnchant());
				writeD(prod.getChance());
				writeD(0x00); // augment id 1
				writeD(0x00); // augment id 2
				writeItemElements(prod);

				writeC(0x00); // SA 1 count
				for (int i = 0; i < saCount; i++)
				{
					writeD(0x00); // SA 1 effect
				}

				writeC(0x00); // SA 2 count
				for (int i = 0; i < saCount; i++)
				{
					writeD(0x00); // SA 2 effect
				}

				writeC(0x00); // 286 - SA 3 count
				for (int i = 0; i < saCount; i++)
				{
					writeD(0x00); // SA 3 effect
				}
			}

			for (MultiSellIngredient i : ingredients)
			{
				int itemId = i.getItemId();
				final ItemTemplate item = itemId > 0 ? ItemHolder.getInstance().getTemplate(i.getItemId()) : null;
				writeD(itemId); // ID
				writeH(itemId > 0 ? item.getType2() : 0xffff);
				writeQ(i.getItemCount()); // Count
				writeH(i.getItemEnchant()); // Enchant Level
				writeD(0x00); // инкрустация
				writeD(0x00); // инкрустация
				writeItemElements(i);

				writeC(0x00); // SA 1 count
				for (int s = 0; s < saCount; s++)
				{
					writeD(0x00); // SA 1 effect
				}

				writeC(0x00); // SA 2 count
				for (int s = 0; s < saCount; s++)
				{
					writeD(0x00); // SA 2 effect
				}

				writeC(0x00); // 286 - SA 3 count
				for (int s = 0; s < saCount; s++)
				{
					writeD(0x00); // SA 3 effect
				}
			}
		}
	}

	// FIXME временная затычка, пока NCSoft не починят в клиенте отображение
	// мультиселов где кол-во больше Integer.MAX_VALUE
	private static List<MultiSellIngredient> fixIngredients(List<MultiSellIngredient> ingredients)
	{
		int needFix = 0;
		for (MultiSellIngredient ingredient : ingredients)
		{
			if (ingredient.getItemCount() > Integer.MAX_VALUE)
			{
				needFix++;
			}
		}

		if (needFix == 0)
		{
			return ingredients;
		}

		MultiSellIngredient temp;
		List<MultiSellIngredient> result = new ArrayList<MultiSellIngredient>(ingredients.size() + needFix);
		for (MultiSellIngredient ingredient : ingredients)
		{
			ingredient = ingredient.clone();
			while (ingredient.getItemCount() > Integer.MAX_VALUE)
			{
				temp = ingredient.clone();
				temp.setItemCount(2000000000);
				result.add(temp);
				ingredient.setItemCount(ingredient.getItemCount() - 2000000000);
			}
			if (ingredient.getItemCount() > 0)
			{
				result.add(ingredient);
			}
		}

		return result;
	}
}