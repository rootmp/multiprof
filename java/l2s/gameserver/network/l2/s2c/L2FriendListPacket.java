package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Friend;

/**
 * @author Bonux
 */
public class L2FriendListPacket implements IClientOutgoingPacket
{
	private Friend[] _friends;

	public L2FriendListPacket(Player player)
	{
		_friends = player.getFriendList().values();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_friends.length);
		for (Friend f : _friends)
		{
			packetWriter.writeD(f.getObjectId());
			packetWriter.writeS(f.getName());
			packetWriter.writeD(f.isOnline());
			packetWriter.writeD(f.isOnline() ? f.getObjectId() : 0);
			packetWriter.writeD(f.getLevel());
			packetWriter.writeD(f.getClassId());
			packetWriter.writeS(f.getMemo());
		}
	}
}