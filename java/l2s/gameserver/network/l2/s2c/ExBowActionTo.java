package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class ExBowActionTo implements IClientOutgoingPacket
{
	private int obj_id;

	public ExBowActionTo(Player player)
	{
		obj_id = player.getObjectId();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(obj_id);
	}
}