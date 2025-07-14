package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.GameTimeController;

public class ClientSetTimePacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ClientSetTimePacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(GameTimeController.getInstance().getGameTime()); // time in client minutes
		packetWriter.writeD(GameTimeController.DAY_START_HOUR); // Constant to match the server time. This determines
		// the speed of the client clock.
		return true;
	}
}