package l2s.gameserver.network.l2.s2c.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPledgeDonationInfo implements IClientOutgoingPacket
{
	private Player _player;

	public ExPledgeDonationInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_player.getVarInt(PlayerVariables.DONATIONS_AVAILABLE, 3)); // available donations today
		packetWriter.writeC(0); // unk
	}
}