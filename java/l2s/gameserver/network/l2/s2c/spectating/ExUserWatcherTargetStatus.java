package l2s.gameserver.network.l2.s2c.spectating;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.model.actor.instances.player.Spectating;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExUserWatcherTargetStatus implements IClientOutgoingPacket
{
	private Spectating _player;
	boolean _login;

	public ExUserWatcherTargetStatus(Spectating player, boolean login)
	{
		_player = player;
		_login = login;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeSizedString(_player.getName()); // name
		packetWriter.writeD(Config.REQUEST_ID);
		packetWriter.writeC(_login); // is online?
		return true;
	}
}