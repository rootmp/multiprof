package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.c2s.RequestExReceivePost;
import l2s.gameserver.network.l2.c2s.RequestExRejectPost;
import l2s.gameserver.network.l2.c2s.RequestExRequestReceivedPost;

/**
 * Просмотр полученного письма. Шлется в ответ на
 * {@link RequestExRequestReceivedPost}. При попытке забрать приложенные вещи
 * клиент шлет {@link RequestExReceivePost}. При возврате письма клиент шлет
 * {@link RequestExRejectPost}.
 * 
 * @see ExReplySentPost
 */
public class ExReplyReceivedPost implements IClientOutgoingPacket
{
	private final Mail mail;

	public ExReplyReceivedPost(Mail mail)
	{
		this.mail = mail;
	}

	// dddSSS dx[hddQdddhhhhhhhhhh] Qdd
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(mail.getType().ordinal());
		if (mail.getType() == Mail.SenderType.SYSTEM)
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
		else if (mail.getType() == Mail.SenderType.UNKNOWN)
		{
			packetWriter.writeD(3492);
			packetWriter.writeD(3493);
		}

		packetWriter.writeD(mail.getMessageId()); // id письма

		packetWriter.writeD(mail.isPayOnDelivery() ? 0x01 : 0x00); // Платное письмо или нет
		packetWriter.writeD(mail.isReturned() ? 0x01 : 0x00);// unknown3

		packetWriter.writeS(mail.getSenderName()); // от кого
		packetWriter.writeS(mail.getTopic()); // топик
		packetWriter.writeS(mail.getBody()); // тело

		packetWriter.writeD(mail.getAttachments().size()); // количество приложенных вещей
		for (ItemInstance item : mail.getAttachments())
		{
			writeItemInfo(item);
			packetWriter.writeD(item.getObjectId());
		}

		packetWriter.writeQ(mail.getPrice()); // для писем с оплатой - цена
		packetWriter.writeD(mail.isReturnable());
		packetWriter.writeD(mail.getReceiverId()); // Не известно. В сниффе оффа значение 24225 (не равняется MessageId)
	}
}