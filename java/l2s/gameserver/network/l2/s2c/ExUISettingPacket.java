package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class ExUISettingPacket implements IClientOutgoingPacket
{
	private final byte data[];

	public ExUISettingPacket(Player player)
	{
		data = player.getKeyBindings();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(data.length);
		writeB(data);
		return true;
	}
}
