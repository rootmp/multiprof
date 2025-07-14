package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.BookMark;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExGetBookMarkInfoPacket;

/**
 * dSdS
 */
public class RequestModifyBookMarkSlot implements IClientIncomingPacket
{
	private String name, acronym;
	private int icon, slot;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		slot = packet.readD();
		name = packet.readS(32);
		icon = packet.readD();
		acronym = packet.readS(4);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar != null)
		{
			final BookMark mark = activeChar.getBookMarkList().get(slot);
			if(mark != null)
			{
				mark.setName(name);
				mark.setIcon(icon);
				mark.setAcronym(acronym);
				activeChar.sendPacket(new ExGetBookMarkInfoPacket(activeChar));
			}
		}
	}
}