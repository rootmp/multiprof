package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.ExReplyPostItemList;
import l2s.gameserver.network.l2.s2c.ExShowReceivedPostList;

/**
 * Нажатие на кнопку "send mail" в списке из {@link ExShowReceivedPostList},
 * запрос создания нового письма В ответ шлется {@link ExReplyPostItemList}
 */
public class RequestExPostItemList implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		// just a trigger
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isActionsDisabled())
			activeChar.sendActionFailed();

		if (!Config.ALLOW_MAIL)
		{
			activeChar.sendMessage(new CustomMessage("mail.Disabled"));
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(new ExReplyPostItemList(1, activeChar));
		activeChar.sendPacket(new ExReplyPostItemList(2, activeChar));
	}
}