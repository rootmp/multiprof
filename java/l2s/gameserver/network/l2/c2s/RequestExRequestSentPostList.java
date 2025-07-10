package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExShowSentPostList;

/**
 * Нажатие на кнопку "sent mail",запрос списка исходящих писем. В ответ шлется
 * {@link ExShowSentPostList}
 */
public class RequestExRequestSentPostList implements IClientIncomingPacket
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
		Player cha = client.getActiveChar();
		if (cha != null)
			cha.sendPacket(new ExShowSentPostList(cha));
	}
}