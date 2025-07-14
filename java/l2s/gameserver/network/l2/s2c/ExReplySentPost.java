package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.c2s.RequestExCancelSentPost;
import l2s.gameserver.network.l2.c2s.RequestExRequestSentPost;

/**
 * Просмотр собственного отправленного письма. Шлется в ответ на
 * {@link RequestExRequestSentPost}. При нажатии на кнопку Cancel клиент шлет
 * {@link RequestExCancelSentPost}.
 * 
 * @see ExReplyReceivedPost
 */
public class ExReplySentPost implements IClientOutgoingPacket
{
	private final Mail mail;

	public ExReplySentPost(Mail mail)
	{
		this.mail = mail;
	}

	// ddSSS dx[hddQdddhhhhhhhhhh] Qd
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(mail.getType().ordinal());
		if(mail.getType() == Mail.SenderType.SYSTEM)
		{
			packetWriter.writeD(mail.getSystemParams()[0]);
			packetWriter.writeD(mail.getSystemParams()[1]);
			packetWriter.writeD(mail.getSystemParams()[2]);
			packetWriter.writeD(mail.getSystemParams()[3]);
			packetWriter.writeD(mail.getSystemParams()[4]);
			packetWriter.writeD(mail.getSystemParams()[5]);
			packetWriter.writeD(mail.getSystemParams()[6]);
			packetWriter.writeD(mail.getSystemParams()[7]);
			packetWriter.writeD(mail.getSystemTopic());
			packetWriter.writeD(mail.getSystemBody());
		}
		else if(mail.getType() == Mail.SenderType.UNKNOWN)
		{
			packetWriter.writeD(3492);
			packetWriter.writeD(3493);
		}

		packetWriter.writeD(mail.getMessageId()); // id письма
		packetWriter.writeD(mail.isPayOnDelivery() ? 1 : 0); // 1 - письмо с запросом оплаты, 0 - просто письмо

		packetWriter.writeS(mail.getReceiverName()); // кому
		packetWriter.writeS(mail.getTopic()); // топик
		packetWriter.writeS(mail.getBody()); // тело

		packetWriter.writeD(mail.getAttachments().size()); // количество приложенных вещей
		for(ItemInstance item : mail.getAttachments())
		{
			writeItemInfo(packetWriter, item);
			packetWriter.writeD(item.getObjectId());
		}

		packetWriter.writeQ(mail.getPrice()); // для писем с оплатой - цена
		packetWriter.writeD(mail.getReceiverId()); // Не известно. В сниффе оффа значение 24225 (не равняется MessageId)
		return true;
	}
}