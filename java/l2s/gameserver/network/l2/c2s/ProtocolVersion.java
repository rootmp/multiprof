package l2s.gameserver.network.l2.c2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.SendStatus;
import l2s.gameserver.network.l2.s2c.VersionCheckPacket;

/**
 * packet type id 0x0E
 * format:	cdbd
 */
public class ProtocolVersion implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(ProtocolVersion.class);

	private int _version;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_version = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if(_version == -2)
		{
			client.closeNow();
			return;
		}
		else if(_version == -3)
		{
			_log.info("Status request from IP : " + client.getIpAddr());
			client.close(new SendStatus());
			return;
		}
		else if(!Config.AVAILABLE_PROTOCOL_REVISIONS.contains(_version))
		{
			_log.warn("Unknown protocol revision : " + _version + ", client : " + client);
			client.close(new VersionCheckPacket(null));
			return;
		}

		client.setRevision(_version);
		client.sendPacket(new VersionCheckPacket(client.enableCrypt()));
	}
}