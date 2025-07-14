package l2s.gameserver.network.l2.s2c.pledge;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExPledgeDonationRequest implements IClientOutgoingPacket
{
	private Player _player;
	private int _donationType;
	private final boolean _success;

	public ExPledgeDonationRequest(boolean success, Player player, int donationType)
	{
		_success = success;
		_player = player;
		_donationType = donationType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_donationType); // donation type
		packetWriter.writeD(_success ? 1 : 0); // trying donation?

		packetWriter.writeH(0); // bCritical
		packetWriter.writeD(3); // nPledgeCoin
		packetWriter.writeD(14); // nPledgeExp
		packetWriter.writeQ(0); // unk
		packetWriter.writeH(0); // unk
		packetWriter.writeD(_player.getVarInt(PlayerVariables.DONATIONS_AVAILABLE)); // existing donations
		return true;
	}
}