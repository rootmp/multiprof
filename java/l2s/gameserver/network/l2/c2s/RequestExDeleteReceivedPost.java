package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.dao.MailDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.s2c.ExShowReceivedPostList;

/**
 * Запрос на удаление полученных сообщений. Удалить можно только письмо без
 * вложения. Отсылается при нажатии на "delete" в списке полученных писем.
 * 
 * @see ExShowReceivedPostList
 * @see RequestExDeleteSentPost
 */
public class RequestExDeleteReceivedPost implements IClientIncomingPacket
{
	private int _count;
	private int[] _list;

	/**
	 * format: dx[d]
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_count = packet.readD();
		if (_count * 4 > _buf.remaining() || _count > Short.MAX_VALUE || _count < 1)
		{
			_count = 0;
			return false;
		}
		_list = new int[_count]; // количество элементов для удаления
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

		List<Mail> mails = MailDAO.getInstance().getReceivedMailByOwnerId(activeChar.getObjectId());
		if (!mails.isEmpty())
		{
			for (Mail mail : mails)
				if (ArrayUtils.contains(_list, mail.getMessageId()))
					if (mail.getAttachments().isEmpty())
					{
						MailDAO.getInstance().deleteReceivedMailByMailId(activeChar.getObjectId(), mail.getMessageId());
					}
		}

		activeChar.sendPacket(new ExShowReceivedPostList(activeChar));
	}
}