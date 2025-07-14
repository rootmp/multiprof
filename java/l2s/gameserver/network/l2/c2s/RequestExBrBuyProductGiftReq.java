package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.data.xml.holder.ProductDataHolder;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.model.mail.Mail.SenderType;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExBR_BuyProductPacket;
import l2s.gameserver.network.l2.s2c.ExBR_NewIConCashBtnWnd;
import l2s.gameserver.network.l2.s2c.ExNoticePostArrived;
import l2s.gameserver.network.l2.s2c.ExUnReadMailCount;
import l2s.gameserver.network.l2.s2c.ReciveVipInfo;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.product.ProductItem;
import l2s.gameserver.templates.item.product.ProductItemComponent;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExBrBuyProductGiftReq implements IClientIncomingPacket
{
	private int _productId;
	private int _count;
	private String _charName;
	private String _mailTitle;
	private String _mailBody;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_productId = packet.readD();
		_count = packet.readD();
		_charName = packet.readS();
		_mailTitle = packet.readS();
		_mailBody = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if(!Config.EX_USE_PRIME_SHOP)
			return;

		Player player = client.getActiveChar();
		if(player == null)
			return;

		if(_count > 100 || _count <= 0)
			return;

		final int receiverId = CharacterDAO.getInstance().getObjectIdByName(_charName);
		if(receiverId <= 0 || player.getObjectId() == receiverId)
		{
			player.sendPacket(ExBR_BuyProductPacket.RESULT_RECIPIENT_DOESNT_EXIST);
			return;
		}

		ProductItem product = ProductDataHolder.getInstance().getProduct(_productId);
		if(product == null)
		{
			player.sendPacket(ExBR_BuyProductPacket.RESULT_WRONG_PRODUCT);
			return;
		}

		if(!product.isOnSale() || (System.currentTimeMillis() < product.getStartTimeSale()) || (System.currentTimeMillis() > product.getEndTimeSale()))
		{
			player.sendPacket(ExBR_BuyProductPacket.RESULT_SALE_PERIOD_ENDED);
			return;
		}

		if(product.getLimit() > 0 && product.getGroupLimit() == 0)
		{
			player.sendPacket(ExBR_BuyProductPacket.RESULT_ITEM_LIMITED);
			return;
		}

		player.getProductHistoryList().writeLock();
		try
		{
			final int pointsRequired = product.getPrice() * _count;
			if(pointsRequired <= 0 && product.getLimit() == -1) // Лимитированные вещи можно выдавать бесплатно.
			{
				player.sendPacket(ExBR_BuyProductPacket.RESULT_WRONG_PRODUCT);
				return;
			}

			player.getInventory().writeLock();
			try
			{
				if(pointsRequired > player.getPremiumPoints())
				{
					player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
					return;
				}

				if(pointsRequired > 0 && Config.DISABLE_SHOPPING_IN_THE_STORE)
				{
					player.sendPacket(ExBR_BuyProductPacket.RESULT_WRONG_SALE_PERIOD);
					return;
				}

				for(ProductItemComponent com : product.getComponents())
				{
					ItemTemplate item = ItemHolder.getInstance().getTemplate(com.getId());
					if(item == null)
					{
						player.sendPacket(ExBR_BuyProductPacket.RESULT_WRONG_PRODUCT);
						return; //what
					}
				}

				if(pointsRequired > 0 && !player.reducePremiumPoints(pointsRequired))
				{
					player.sendPacket(ExBR_BuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
					return;
				}

				player.getVIP().addPoints((int) (pointsRequired * player.getVIP().getPointsRefillPercent() / 100.));

				player.getProductHistoryList().onPurchaseProduct(product, _count);

				player.sendPacket(new ExBR_NewIConCashBtnWnd(player));

				Mail mail = new Mail();

				mail.setType(SenderType.PRESENT);
				mail.setReceiverId(receiverId);
				mail.setReceiverName(_charName);
				mail.setSenderName(player.getName());
				mail.setSenderId(player.getObjectId());
				mail.setTopic(_mailTitle);
				mail.setBody(_mailBody);

				for(ProductItemComponent $comp : product.getComponents())
				{
					ItemInstance item = ItemFunctions.createItem($comp.getId());
					item.setCount($comp.getCount() * _count);
					item.setLocation(ItemLocation.MAIL);
					item.save();

					mail.setUnread(true);
					mail.addAttachment(item);
					mail.save();
				}
				Player p_receiver = GameObjectsStorage.getPlayer(receiverId);
				if(p_receiver != null)
				{
					p_receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
					p_receiver.sendPacket(new ExUnReadMailCount(p_receiver));
					p_receiver.sendPacket(SystemMsg.THE_MAIL_HAS_ARRIVED);
				}
				player.sendPacket(ExBR_BuyProductPacket.RESULT_OK);
				player.sendPacket(new ReciveVipInfo(player));
			}
			finally
			{
				player.getInventory().writeUnlock();
			}
		}
		finally
		{
			player.getProductHistoryList().writeUnlock();
		}
	}
}
