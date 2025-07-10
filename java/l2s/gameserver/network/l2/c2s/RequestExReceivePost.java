package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.dao.JdbcEntityState;
import l2s.commons.math.SafeMath;
import l2s.gameserver.dao.MailDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExReplyReceivedPost;
import l2s.gameserver.network.l2.s2c.ExShowReceivedPostList;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.taskmanager.DelayedItemsManager;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.Log;

/**
 * Шлется клиентом при согласии принять письмо в {@link ExReplyReceivedPost}.
 * Если письмо с оплатой то создателю письма шлется запрошенная сумма.
 */
public class RequestExReceivePost implements IClientIncomingPacket
{
	private int postId;

	/**
	 * format: d
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		postId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS);
			return;
		}

		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_DURING_AN_EXCHANGE);
			return;
		}

		if (activeChar.isFishing())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}

		if (activeChar.isInTrainingCamp())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_TAKE_OTHER_ACTION_WHILE_ENTERING_THE_TRAINING_CAMP);
			return;
		}

		if (activeChar.getEnchantScroll() != null)
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT);
			return;
		}

		Mail mail = MailDAO.getInstance().getReceivedMailByMailId(activeChar.getObjectId(), postId);
		if (mail != null)
		{
			activeChar.getInventory().writeLock();
			try
			{
				Set<ItemInstance> attachments = mail.getAttachments();
				ItemInstance[] items;

				if (attachments.size() > 0 && !activeChar.isInPeaceZone())
				{
					activeChar.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_IN_A_NONPEACE_ZONE_LOCATION);
					return;
				}

				boolean safePost = false;

				synchronized (attachments)
				{
					if (mail.getAttachments().isEmpty())
						return;

					items = mail.getAttachments().toArray(new ItemInstance[attachments.size()]);

					int slots = 0;
					long weight = 0;
					for (ItemInstance item : items)
					{
						weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(item.getCount(), item.getTemplate().getWeight()));
						if (!item.getTemplate().isStackable() || activeChar.getInventory().getItemByItemId(item.getItemId()) == null)
							slots++;
					}

					if (!activeChar.getInventory().validateWeight(weight))
					{
						activeChar.sendPacket(SystemMsg.YOU_COULD_NOT_RECEIVE_BECAUSE_YOUR_INVENTORY_IS_FULL);
						return;
					}

					if (!activeChar.getInventory().validateCapacity(slots))
					{
						activeChar.sendPacket(SystemMsg.YOU_COULD_NOT_RECEIVE_BECAUSE_YOUR_INVENTORY_IS_FULL);
						return;
					}

					if (!mail.isReturned() && mail.getPrice() > 0)
					{
						safePost = true;
						if (!activeChar.reduceAdena(mail.getPrice(), true))
						{
							activeChar.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_BECAUSE_YOU_DONT_HAVE_ENOUGH_ADENA);
							return;
						}

						Player sender = World.getPlayer(mail.getSenderId());
						if (sender != null)
						{
							ItemInstance adena = sender.addAdena(mail.getPrice(), true);
							sender.sendPacket(new SystemMessagePacket(SystemMsg.S1_ACQUIRED_THE_ATTACHED_ITEM_TO_YOUR_MAIL).addName(activeChar));
							Log.LogItem(sender, Log.PostPaymentRecieve, adena, "receive mail payment: message_id[" + mail.getMessageId() + "], receiver_id[" + mail.getReceiverId() + "]");
						}
						else
						{
							DelayedItemsManager.addDelayed(mail.getSenderId(), ItemTemplate.ITEM_ID_ADENA, mail.getPrice(), 0, "receive mail payment: message_id[" + mail.getMessageId() + "], receiver_id[" + mail.getReceiverId() + "]");
						}
					}

					attachments.clear();
				}

				mail.setJdbcState(JdbcEntityState.UPDATED);
				if (StringUtils.isEmpty(mail.getBody()))
					mail.delete();
				else
					mail.update();

				for (ItemInstance item : items)
				{
					activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_HAVE_ACQUIRED_S2_S1).addItemName(item.getItemId()).addLong(item.getCount()));
					Log.LogItem(activeChar, safePost ? Log.SafePostRecieve : Log.PostRecieve, item, "receive mail attachments: message_id[" + mail.getMessageId() + "], sender_id[" + mail.getSenderId() + "]");
					activeChar.getInventory().addItem(item);
				}

				activeChar.sendPacket(SystemMsg.MAIL_SUCCESSFULLY_RECEIVED);
			}
			catch (ArithmeticException ae)
			{
				// TODO audit
			}
			finally
			{
				activeChar.getInventory().writeUnlock();
			}
		}

		activeChar.sendPacket(new ExShowReceivedPostList(activeChar));
	}
}