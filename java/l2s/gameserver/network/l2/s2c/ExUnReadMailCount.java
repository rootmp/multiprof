package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.List;

import l2s.gameserver.dao.MailDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.mail.Mail;

/**
 * @author Bonux
 **/
public class ExUnReadMailCount implements IClientOutgoingPacket
{
	private final int _count;

	public ExUnReadMailCount(Player player)
	{
		int count = 0;
		List<Mail> mails = MailDAO.getInstance().getReceivedMailByOwnerId(player.getObjectId());
		for (Mail mail : mails)
		{
			if (mail.isUnread())
				count++;
		}
		_count = count;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_count);
	}
}