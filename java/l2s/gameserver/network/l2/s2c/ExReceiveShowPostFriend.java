package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import org.napile.primitive.maps.IntObjectMap;

import l2s.gameserver.model.Player;

/**
 * @author VISTALL
 * @date 22:01/22.03.2011
 */
public class ExReceiveShowPostFriend implements IClientOutgoingPacket
{
	private IntObjectMap<String> _list;

	public ExReceiveShowPostFriend(Player player)
	{
		_list = player.getPostFriends();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_list.size());
		for (String t : _list.valueCollection())
			packetWriter.writeS(t);
	}
}
