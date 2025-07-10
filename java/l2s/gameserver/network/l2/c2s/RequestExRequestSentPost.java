package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.dao.MailDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.s2c.ExReplySentPost;
import l2s.gameserver.network.l2.s2c.ExShowSentPostList;

/**
 * Запрос информации об отправленном письме. Появляется при нажатии на письмо из
 * списка {@link ExShowSentPostList}. В ответ шлется {@link ExReplySentPost}.
 * 
 * @see RequestExRequestReceivedPost
 */
public class RequestExRequestSentPost implements IClientIncomingPacket
{
	private int postId;

	/**
	 * format: d
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		postId = packet.readD(); // id письма
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Mail mail = MailDAO.getInstance().getSentMailByMailId(activeChar.getObjectId(), postId);
		if (mail != null)
		{
			activeChar.sendPacket(new ExReplySentPost(mail));
			return;
		}

		activeChar.sendPacket(new ExShowSentPostList(activeChar));
	}
}