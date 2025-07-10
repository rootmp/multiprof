package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author GodWorld
 * @reworked by Bonux
 **/
public class ExPledgeWaitingUser implements IClientOutgoingPacket
{
	private final int _charId;
	private final String _desc;

	public ExPledgeWaitingUser(int charId, String desc)
	{
		_charId = charId;
		_desc = desc;
	}

	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_charId);
		packetWriter.writeS(_desc);
	}
}