package l2s.gameserver.network.l2.s2c.spectating;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Spectating;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

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
		for(Spectating s : _spectatings)
		{
			packetWriter.writeSizedString(s.getName());
			packetWriter.writeD(Config.REQUEST_ID); // server id
			packetWriter.writeD(s.getLevel());
			packetWriter.writeD(s.getClassId());
			packetWriter.writeC(s.isOnline());
		}

		//@formatter:off
		/*
		int count = 0;
		packetWriter.writeD(count);
		for (int i = 0; i < count; i++)
		{
			packetWriter.writeSizedString(""); // name
			packetWriter.writeD(Config.REQUEST_ID); // Server Id
			packetWriter.writeD(1); // level
			packetWriter.writeD(1); // class id
			packetWriter.writeC(0); // online?
		}
		*/
		return true;
	}
}