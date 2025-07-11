package l2s.gameserver.network.l2.c2s;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;

import l2s.commons.network.PacketReader;
import l2s.gameserver.dao.MailDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExShowSentPostList;

/**
 * Запрос на удаление отправленных сообщений. Удалить можно только письмо без
 * вложения. Отсылается при нажатии на "delete" в списке отправленных писем.
 * 
 * @see ExShowSentPostList
 * @see RequestExDeleteReceivedPost
 */
public class RequestExDeleteSentPost implements IClientIncomingPacket
{
	private int _count;
	private int[] _list;

	/**
	 * format: dx[d]
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_count = packet.readD(); // количество элементов для удаления
		if (_count * 4 > _buf.remaining() || _count > Short.MAX_VALUE || _count < 1)
		{
			_count = 0;
			return false;
		}
		_list = new int[_count];
		for (int i = 0; i < _count; i++)
			_list[i] = packet.readD(); // уникальный номер письма
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null || _count == 0)
			return;

		Collection<Mail> mails = MailDAO.getInstance().getSentMailByOwnerId(activeChar.getObjectId());
		if (!mails.isEmpty())
		{
			for (Mail mail : mails)
				if (ArrayUtils.contains(_list, mail.getMessageId()))
					if (mail.getAttachments().isEmpty())
					{
						// FIXME [G1ta0] если почта не прочитана получателем, возможно имеет смысл
						// удалять ее совсем, на офф. сервере не удаляется.
						/*
						 * if(mail.isUnread()) mail.delete(); else
						 */
						MailDAO.getInstance().deleteSentMailByMailId(activeChar.getObjectId(), mail.getMessageId());
					}
		}

		activeChar.sendPacket(new ExShowSentPostList(activeChar));
	}
}