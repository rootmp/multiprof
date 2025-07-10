package l2s.gameserver.network.l2.s2c.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPledgeDonationInfo extends L2GameServerPacket
{
	private Player _player;

	public ExPledgeDonationInfo(Player player)
	{
		_player = player;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_player.getVarInt(PlayerVariables.DONATIONS_AVAILABLE, 3)); // available donations today
		writeC(0); // unk
	}
}