package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.MultiSellListContainer;
import l2s.gameserver.model.base.MultiSellEntry;
import l2s.gameserver.model.base.MultiSellIngredient;
import l2s.gameserver.templates.item.ItemTemplate;

public class MultiSellListPacket implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0x00); // UNK
		packetWriter.writeD(_listId); // list id
		packetWriter.writeC(_windowType.ordinal()); // UNK
		packetWriter.writeD(_page); // page
		packetWriter.writeD(_finished); // finished
		packetWriter.writeD(Config.MULTISELL_SIZE); // size of pages
		packetWriter.writeD(_list.size()); // list length
		packetWriter.writeC(0x00); // [TODO]: Grand Crusade
		packetWriter.writeC(_type);// Type (0x00 - Нормальный, 0xD0 - с шансом)
		packetWriter.writeD(0x20); // UNK
		List<MultiSellIngredient> ingredients;
		for (MultiSellEntry ent : _list)
		{
			ingredients = fixIngredients(ent.getIngredients());

			packetWriter.writeD(ent.getEntryId());
			packetWriter.writeC(!ent.getProduction().isEmpty() && ent.getProduction().get(0).isStackable() ? 1 : 0); // stackable?
			packetWriter.writeH(0x00); // enchant level
			packetWriter.writeD(0x00); // инкрустация
			packetWriter.writeD(0x00); // инкрустация

			writeItemElements();

			int saCount = 0;
			packetWriter.writeC(0x00); // SA 1 count
			for (int i = 0; i < saCount; i++)
			{
				packetWriter.writeD(0x00); // SA 1 effect
			}

			packetWriter.writeC(0x00); // SA 2 count
			for (int i = 0; i < saCount; i++)
			{
				packetWriter.writeD(0x00); // SA 2 effect
			}

			packetWriter.writeC(0x00); // 286 - SA 3 count
			for (int i = 0; i < saCount; i++)
			{
				packetWriter.writeD(0x00); // SA 3 effect
			}

			packetWriter.writeH(ent.getProduction().size());
			packetWriter.writeH(ingredients.size());

			for (MultiSellIngredient prod : ent.getProduction())
			{
				int itemId = prod.getItemId();
				ItemTemplate template = itemId > 0 ? ItemHolder.getInstance().getTemplate(prod.getItemId()) : null;
				packetWriter.writeD(itemId);
				packetWriter.writeQ(itemId > 0 ? template.getBodyPart() : 0);
				packetWriter.writeH(itemId > 0 ? template.getType2() : 0);
				packetWriter.writeQ(prod.getItemCount());
				packetWriter.writeH(prod.getItemEnchant());
				packetWriter.writeD(prod.getChance());
				packetWriter.writeD(0x00); // augment id 1
				packetWriter.writeD(0x00); // augment id 2
				writeItemElements(prod);

				packetWriter.writeC(0x00); // SA 1 count
				for (int i = 0; i < saCount; i++)
				{
					packetWriter.writeD(0x00); // SA 1 effect
				}

				packetWriter.writeC(0x00); // SA 2 count
				for (int i = 0; i < saCount; i++)
				{
					packetWriter.writeD(0x00); // SA 2 effect
				}

				packetWriter.writeC(0x00); // 286 - SA 3 count
				for (int i = 0; i < saCount; i++)
				{
					packetWriter.writeD(0x00); // SA 3 effect
				}
			}

			for (MultiSellIngredient i : ingredients)
			{
				int itemId = i.getItemId();
				final ItemTemplate item = itemId > 0 ? ItemHolder.getInstance().getTemplate(i.getItemId()) : null;
				packetWriter.writeD(itemId); // ID
				packetWriter.writeH(itemId > 0 ? item.getType2() : 0xffff);
				packetWriter.writeQ(i.getItemCount()); // Count
				packetWriter.writeH(i.getItemEnchant()); // Enchant Level
				packetWriter.writeD(0x00); // инкрустация
				packetWriter.writeD(0x00); // инкрустация
				writeItemElements(i);

				packetWriter.writeC(0x00); // SA 1 count
				for (int s = 0; s < saCount; s++)
				{
					packetWriter.writeD(0x00); // SA 1 effect
				}

				packetWriter.writeC(0x00); // SA 2 count
				for (int s = 0; s < saCount; s++)
				{
					packetWriter.writeD(0x00); // SA 2 effect
				}

				packetWriter.writeC(0x00); // 286 - SA 3 count
				for (int s = 0; s < saCount; s++)
				{
					packetWriter.writeD(0x00); // SA 3 effect
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