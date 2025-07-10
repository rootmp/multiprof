package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ManufactureItem;
import l2s.gameserver.templates.item.RecipeTemplate;

public class RecipeShopManageListPacket implements IClientOutgoingPacket
{
	private Map<Integer, ManufactureItem> createList;
	private Collection<RecipeTemplate> recipes;
	private int sellerId;
	private long adena;
	private boolean isDwarven;

	public RecipeShopManageListPacket(Player seller, boolean isDwarvenCraft)
	{
		sellerId = seller.getObjectId();
		adena = seller.getAdena();
		isDwarven = isDwarvenCraft;
		if (isDwarven)
			recipes = seller.getDwarvenRecipeBook();
		else
			recipes = seller.getCommonRecipeBook();

		createList = seller.getCreateList();

		Iterator<Map.Entry<Integer, ManufactureItem>> iterator = createList.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<Integer, ManufactureItem> entry = iterator.next();

			ManufactureItem mi = entry.getValue();
			if (!seller.findRecipe(mi.getRecipeId()))
				iterator.remove();
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(sellerId);
		packetWriter.writeD((int) Math.min(adena, Integer.MAX_VALUE)); // FIXME не менять на writeQ, в текущем клиенте там все еще D
															// (видимо баг
															// NCSoft)
		packetWriter.writeD(isDwarven ? 0x00 : 0x01);
		packetWriter.writeD(recipes.size());
		int i = 1;
		for (RecipeTemplate recipe : recipes)
		{
			packetWriter.writeD(recipe.getId());
			packetWriter.writeD(i++);
		}
		packetWriter.writeD(createList.size());
		for (ManufactureItem mi : createList.values())
		{
			packetWriter.writeD(mi.getRecipeId());
			packetWriter.writeD(0x00); // ??
			packetWriter.writeQ(mi.getCost());
		}
	}
}