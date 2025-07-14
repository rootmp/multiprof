package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.ProductDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.product.ProductItem;
import l2s.gameserver.templates.item.product.ProductItemComponent;
import l2s.gameserver.utils.ItemFunctions;

public class ExBR_ProductInfoPacket implements IClientOutgoingPacket
{
	private final long _adena, _premiumPoints, _freeCoins;
	private final ProductItem _productId;

	public ExBR_ProductInfoPacket(Player player, int id)
	{
		_adena = player.getAdena();
		_premiumPoints = player.getPremiumPoints();
		_freeCoins = ItemFunctions.getItemCount(player, 23805);
		_productId = ProductDataHolder.getInstance().getProduct(id);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if(_productId == null)
			return false;

		packetWriter.writeD(_productId.getId()); //product id
		packetWriter.writeD(_productId.getPrice(true)); // points
		packetWriter.writeD(_productId.getComponents().size()); //size

		for(ProductItemComponent com : _productId.getComponents())
		{
			packetWriter.writeD(com.getId()); //item id
			packetWriter.writeD((int) com.getCount()); //quality
			packetWriter.writeD(com.getWeight()); //weight
			packetWriter.writeD(com.isDropable() ? 1 : 0); //0 - dont drop/trade
		}

		packetWriter.writeQ(_adena);
		packetWriter.writeQ(_premiumPoints);
		packetWriter.writeQ(_freeCoins); // Hero coins
		return true;
	}
}