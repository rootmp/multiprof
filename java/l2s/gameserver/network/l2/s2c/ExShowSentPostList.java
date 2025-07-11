package l2s.gameserver.network.l2.s2c;

import java.util.Collections;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.dao.MailDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.c2s.RequestExDeleteSentPost;
import l2s.gameserver.network.l2.c2s.RequestExRequestSentPost;
import l2s.gameserver.network.l2.c2s.RequestExRequestSentPostList;

/**
 * Появляется при нажатии на кнопку "sent mail", исходящие письма Ответ на
 * {@link RequestExRequestSentPostList} При нажатии на письмо в списке шлется
 * {@link RequestExRequestSentPost}, а в ответ {@link ExReplySentPost}. При
 * нажатии на "delete" шлется {@link RequestExDeleteSentPost}.
 * 
 * @see ExShowReceivedPostList аналогичный список принятой почты
 */
public class ExShowSentPostList implements IClientOutgoingPacket
{
	private final List<Mail> mails;

	public ExShowSentPostList(Player cha)
	{
		mails = MailDAO.getInstance().getSentMailByOwnerId(cha.getObjectId());
		Collections.sort(mails);
	}

	// d dx[dSSddddd]
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD((int) (System.currentTimeMillis() / 1000L));
		packetWriter.writeD(mails.size()); // количество писем
		for (Mail mail : mails)
		{
			packetWriter.writeD(mail.getMessageId()); // уникальный id письма
			packetWriter.writeS(mail.getTopic()); // топик
			packetWriter.writeS(mail.getReceiverName()); // получатель
			packetWriter.writeD(mail.isPayOnDelivery() ? 1 : 0); // если тут 1 то письмо требует оплаты
			packetWriter.writeD(mail.getExpireTime()); // время действительности письма
			packetWriter.writeD(mail.isUnread() ? 1 : 0); // ?
			packetWriter.writeD(mail.isReturnable()); // returnable
			packetWriter.writeD(mail.getAttachments().isEmpty() ? 0 : 1); // 1 - письмо с приложением, 0 - просто письмо
			packetWriter.writeD(0x00); // ???
		}
		return true;
	}
}