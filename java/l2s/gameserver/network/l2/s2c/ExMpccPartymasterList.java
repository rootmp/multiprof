package l2s.gameserver.network.l2.s2c;

import java.util.Collections;
import java.util.Set;

import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 * @date 6:22/12.06.2011
 */
public class ExMpccPartymasterList implements IClientOutgoingPacket
{
	private Set<String> _members = Collections.emptySet();

	public ExMpccPartymasterList(Set<String> s)
	{
		_members = s;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_members.size());
		for(String t : _members)
		{
			packetWriter.writeS(t);
		}

		return true;
	}
}
