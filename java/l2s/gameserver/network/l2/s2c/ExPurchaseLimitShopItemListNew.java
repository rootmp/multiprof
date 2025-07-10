package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.List;

import l2s.gameserver.model.LimitedShopContainer;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.base.LimitedShopEntry;
import l2s.gameserver.model.base.LimitedShopIngredient;
import l2s.gameserver.model.base.LimitedShopProduction;

/**
 * @author nexvill
 */
public class ExPurchaseLimitShopItemListNew implements IClientOutgoingPacket
{
	private final Player _player;
	private final int _listId;
	private final List<LimitedShopEntry> _list;
	private final int _size;

	public ExPurchaseLimitShopItemListNew(Player player, LimitedShopContainer list)
	{
		_player = player;
		_list = list.getEntries();
		_listId = list.getListId();
		_size = list.getEntries().size();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_listId);
		packetWriter.writeC(1); // page
		packetWriter.writeC(1); // max page
		packetWriter.writeD(_size);

		for (int i = 0; i < _size; i++)
		{
			final LimitedShopEntry entry = _list.get(i);
			LimitedShopProduction product = entry.getProduction().get(0);

			packetWriter.writeD(product.getInfo().getInteger("index")); // item index
			packetWriter.writeD(product.getInfo().getInteger("product1Id"));

			int ingredientsSize = entry.getIngredients().size();
			if ((ingredientsSize > 5) || (ingredientsSize == 0))
			{
				return;
			}

			for (LimitedShopIngredient ingredient : entry.getIngredients())
			{
				packetWriter.writeD(ingredient.getItemId());
			}
			for (int j = 5; j > ingredientsSize; j--)
				packetWriter.writeD(0);

			for (LimitedShopIngredient ingredient : entry.getIngredients())
			{
				packetWriter.writeQ(ingredient.getItemCount());
			}
			for (int j = 5; j > ingredientsSize; j--)
				packetWriter.writeQ(0);

			for (LimitedShopIngredient ingredient : entry.getIngredients())
			{
				packetWriter.writeH(ingredient.getEnchantLevel());
			}
			for (int j = 5; j > ingredientsSize; j--)
			{
				packetWriter.writeH(0); // sCostItemEnchantment
			}
			packetWriter.writeD(_player.getVarInt(PlayerVariables.LIMIT_ITEM_REMAIN + "_" + product.getInfo().getInteger("product1Id"), product.getInfo().getInteger("dailyLimit"))); // nRemainItemAmount
			packetWriter.writeD(0); // nRemainSec
			packetWriter.writeD(0); // remain server item amount
			packetWriter.writeH(0); // circle num
		}
	}
}