package l2s.gameserver.network.l2.c2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.dataparser.data.holder.DyeDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.HennaItemInfoPacket;
import l2s.gameserver.templates.item.henna.Henna;

/**
 * @author Zoey76
 */
public class RequestHennaItemInfo implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestHennaItemInfo.class);

	private int _symbolId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_symbolId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		final Henna henna = DyeDataHolder.getInstance().getHennaByDyeId(_symbolId);
		if(henna == null)
		{
			if(_symbolId != 0)
			{
				_log.error(getClass().getSimpleName() + ": Invalid Henna Id: " + _symbolId + " from " + player);
			}
			player.sendActionFailed();
			return;
		}

		player.sendPacket(new HennaItemInfoPacket(henna, player));
	}

}
