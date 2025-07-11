package l2s.gameserver.network.l2.c2s;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ProductDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExBR_ProductListPacket;
import l2s.gameserver.templates.item.product.ProductItem;

public class RequestExBrProductListReq implements IClientIncomingPacket
{
	private int _type;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_type = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if(!Config.EX_USE_PRIME_SHOP)
			return;

		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		List<ProductItem> products = new ArrayList<ProductItem>();
		switch(_type)
		{
			case 0: // Продукция
				products.addAll(ProductDataHolder.getInstance().getProductsOnSale(activeChar));
				Collections.sort(products);
				break;
			case 1: // История покупок
				products.addAll(activeChar.getProductHistoryList().productValues());
				break;
			case 2: // Избранное
				// TODO: Реализовать
				break;
			default:
				break;
		}

		if(products.isEmpty())
			return;

		activeChar.sendPacket(new ExBR_ProductListPacket(activeChar, _type, products));
	}
}