package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class GameGuardQuery implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00); // ? - Меняется при каждом перезаходе.
		packetWriter.writeD(0x00); // ? - Меняется при каждом перезаходе.
		packetWriter.writeD(0x00); // ? - Меняется при каждом перезаходе.
		packetWriter.writeD(0x00); // ? - Меняется при каждом перезаходе.
		return true;
	}
}