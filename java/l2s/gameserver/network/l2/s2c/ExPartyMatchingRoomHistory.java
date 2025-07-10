package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExPartyMatchingRoomHistory implements IClientOutgoingPacket
{
	public ExPartyMatchingRoomHistory()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00); // Previously existent rooms count
		/*
		 * for(rooms count) { packetWriter.writeS(""); // Name packetWriter.writeS(""); // Owner }
		 */
	}
}