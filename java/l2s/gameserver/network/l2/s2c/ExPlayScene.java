package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * Format: ch Протокол 828: при отправке пакета клиенту ничего не происходит.
 */
public class ExPlayScene implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00); // Kamael
	}
}