package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Collections;
import java.util.Map;

import l2s.gameserver.model.Player;

/**
 * @author VISTALL
 * @date 20:24/16.05.2011
 */
public class PackageToListPacket implements IClientOutgoingPacket
{
	private Map<Integer, String> _characters = Collections.emptyMap();

	public PackageToListPacket(Player player)
	{
		_characters = player.getAccountChars();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_characters.size());
		for (Map.Entry<Integer, String> entry : _characters.entrySet())
		{
			packetWriter.writeD(entry.getKey());
			packetWriter.writeS(entry.getValue());
		}
	}
}
