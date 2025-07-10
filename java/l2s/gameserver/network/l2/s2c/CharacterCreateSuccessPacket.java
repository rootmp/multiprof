package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class CharacterCreateSuccessPacket implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new CharacterCreateSuccessPacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x01);
	}
}