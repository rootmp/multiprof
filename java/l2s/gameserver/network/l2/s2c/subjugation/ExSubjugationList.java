package l2s.gameserver.network.l2.s2c.subjugation;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.SubjugationsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExSubjugationList implements IClientOutgoingPacket
{
	private Player _player;

	public ExSubjugationList(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int count = 0;
		if(_player.getLevel() > 84)
			count = 9;
		else if(_player.getLevel() > 77)
			count = 5;
		else if(_player.getLevel() > 75)
			count = 3;
		else if(_player.getLevel() > 69)
			count = 2;
		else if(_player.getLevel() > 64)
			count = 1;

		packetWriter.writeD(count);
		for(int i = 0; i < count; i++)
		{
			int zoneId = i + 1;
			int points = _player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + zoneId, 0);
			int keysHave = points / 1_000_000;
			points %= 1_000_000;
			int maximumKeys = SubjugationsHolder.getInstance().getFields().get(zoneId).getMaximumKeys() - keysHave;

			packetWriter.writeD(zoneId);
			packetWriter.writeD(points);
			packetWriter.writeD(keysHave);
			packetWriter.writeD(maximumKeys);
		}
		return true;
	}
}