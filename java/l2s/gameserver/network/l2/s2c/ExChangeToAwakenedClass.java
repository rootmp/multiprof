package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;

/**
 * @author Bonux
 */
public class ExChangeToAwakenedClass implements IClientOutgoingPacket
{
	private int _classId;

	public ExChangeToAwakenedClass(Player player, NpcInstance npc, int classId)
	{
		_classId = classId;
		player.setLastNpc(npc);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_classId);
	}
}
