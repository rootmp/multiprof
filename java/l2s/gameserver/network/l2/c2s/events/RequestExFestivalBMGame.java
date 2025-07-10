package l2s.gameserver.network.l2.c2s.events;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.events.ExFestivalBMAllItemInfo;
import l2s.gameserver.network.l2.s2c.events.ExFestivalBMGame;
import l2s.gameserver.network.l2.s2c.events.ExFestivalBMInfo;

/**
 * @author nexvill
 */
public class RequestExFestivalBMGame implements IClientIncomingPacket
{
	private int _participate;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_participate = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (_participate == 1)
		{
			final PcInventory inventory = activeChar.getInventory();
			final boolean success = inventory.destroyItemByItemId(Config.BM_FESTIVAL_ITEM_TO_PLAY, Config.BM_FESTIVAL_ITEM_TO_PLAY_COUNT);

			if (success)
			{
				activeChar.sendPacket(new ExFestivalBMGame(activeChar));
				activeChar.sendPacket(new ExFestivalBMInfo(activeChar));
				activeChar.sendPacket(new ExFestivalBMAllItemInfo());
			}
			else
			{
				final SystemMessage sm = new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
				sm.addItemName(Config.BM_FESTIVAL_ITEM_TO_PLAY);
				sm.addNumber(Config.BM_FESTIVAL_ITEM_TO_PLAY_COUNT);
				activeChar.sendPacket(sm);
				activeChar.sendPacket(new ExFestivalBMInfo(activeChar));
				activeChar.sendPacket(new ExFestivalBMAllItemInfo());
			}
		}
	}
}