package l2s.gameserver.network.l2.s2c.spectating;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Spectating;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExUserWatcherTargetList implements IClientOutgoingPacket
{
	private Spectating[] _spectatings;

	public ExUserWatcherTargetList(Player player)
	{
		_spectatings = player.getSpectatingList().values();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_spectatings.length);
		for (Spectating s : _spectatings)
		{
			packetWriter.writeString(s.getName());
			packetWriter.writeD(Config.REQUEST_ID); // server id
			packetWriter.writeD(s.getLevel());
			packetWriter.writeD(s.getClassId());
			packetWriter.writeC(s.isOnline());
		}
	}
}