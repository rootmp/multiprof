package l2s.gameserver.network.l2.s2c.spectating;

import l2s.gameserver.Config;
import l2s.gameserver.model.actor.instances.player.Spectating;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExUserWatcherTargetStatus extends L2GameServerPacket
{
	private Spectating _player;
	boolean _login;

	public ExUserWatcherTargetStatus(Spectating player, boolean login)
	{
		_player = player;
		_login = login;
	}

	@Override
	protected final void writeImpl()
	{
		writeString(_player.getName()); // name
		writeD(Config.REQUEST_ID);
		writeC(_login); // is online?
	}
}