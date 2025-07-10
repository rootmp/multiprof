package l2s.gameserver.network.l2.s2c.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPledgeDonationRequest extends L2GameServerPacket
{
	private Player _player;
	private int _donationType;

	public ExPledgeDonationRequest(Player player, int donationType)
	{
		_player = player;
		_donationType = donationType;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(_donationType); // donation type
		writeD(1); // trying donation?
		writeH(0); // unk
		writeD(3); // maximum donations/day
		writeD(14); // unk
		writeD(0); // unk
		writeD(0); // unk
		writeH(0); // unk
		writeD(_player.getVarInt(PlayerVariables.DONATIONS_AVAILABLE)); // existing donations
	}
}