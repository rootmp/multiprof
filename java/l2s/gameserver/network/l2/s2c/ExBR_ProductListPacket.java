package l2s.gameserver.network.l2.s2c;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.product.ProductItem;
import l2s.gameserver.templates.item.product.ProductItemComponent;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author Bonux
**/
public class ExBR_ProductListPacket implements IClientOutgoingPacket
{
	private final long _adena, _freeCoins;
	private final int _type;
	private final List<ProductItem> _products;
	private Player _player;

	public ExBR_ProductListPacket(Player player, int type, List<ProductItem> products)
	{
		_adena = player.getAdena();
		_player = player;
		_freeCoins = ItemFunctions.getItemCount(player, 23805);
		_products = products;
		_type = type;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_adena); // Player Adena Count
		packetWriter.writeQ(_freeCoins); // UNK
		packetWriter.writeC(_type); // 0x00 - Home, 0x01 - History, 0x02 - Favorites
		packetWriter.writeD(_products.size());
		for(ProductItem product : _products)
		{
			packetWriter.writeD(product.getId()); //product id
			packetWriter.writeC(product.getCategory()); //category 1 - enchant 2 - supplies  3 - decoration 4 - package 5 - other
			packetWriter.writeC(product.getPointsType().ordinal()); // Points Type: 1 - Points, 2 - Adena, 3 - Hero Coins
			packetWriter.writeD(product.getPrice(true)); //points
			packetWriter.writeC(product.getTabId()); // show tab 2-th group - 1 показывает окошко про итем
			packetWriter.writeD(product.getMainCategory()); // категория главной страницы (маска) (0 - не показывать на главное (дефолт), 1 - верхнее окно, 2 - рекомендуемый товар, 4 - популярные товары)  // Glory Days 488
			packetWriter.writeD((int) (product.getStartTimeSale() / 1000)); // start sale unix date in seconds
			packetWriter.writeD((int) (product.getEndTimeSale() / 1000)); // end sale unix date in seconds
			packetWriter.writeC(127); // day week (127 = not daily goods)
			packetWriter.writeC(product.getStartHour()); // start hour
			packetWriter.writeC(product.getStartMin()); // start min
			packetWriter.writeC(product.getEndHour()); // end hour
			packetWriter.writeC(product.getEndMin()); // end min
			packetWriter.writeD(0x00); // stock
			packetWriter.writeD(product.getRepurchaseInterval() != 0 ? product.getAvailable(_player) : -1); // max stock
			packetWriter.writeC(product.getDiscount()); // % скидки
			packetWriter.writeC(0x00); // Level restriction
			packetWriter.writeC(0x00); // UNK
			packetWriter.writeD(0x00); // UNK
			packetWriter.writeD(0x00); // UNK
			packetWriter.writeD(product.getRepurchaseInterval()); // Repurchase interval (days)
			packetWriter.writeD(product.getAvailable(_player)); // Amount (per account)

			packetWriter.writeC(product.getComponents().size()); // Количество итемов в продукте.
			for(ProductItemComponent component : product.getComponents())
			{
				packetWriter.writeD(component.getId()); //item id
				packetWriter.writeD((int) component.getCount()); //quality
				packetWriter.writeD(component.getWeight()); //weight
				packetWriter.writeD(component.isDropable() ? 1 : 0); //0 - dont drop/trade
			}
		}
		return true;
	}
}