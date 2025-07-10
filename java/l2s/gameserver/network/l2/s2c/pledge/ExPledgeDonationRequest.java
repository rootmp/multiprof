package l2s.gameserver.network.l2.s2c.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPledgeDonationRequest implements IClientOutgoingPacket
{
	private Player _player;
	private int _donationType;

	public ExPledgeDonationRequest(Player player, int donationType)
	{
		_player = player;
		_donationType = donationType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_donationType); // donation type
		packetWriter.writeD(1); // trying donation?
		packetWriter.writeH(0); // unk
		packetWriter.writeD(3); // maximum donations/day
		packetWriter.writeD(14); // unk
		packetWriter.writeD(0); // unk
		packetWriter.writeD(0); // unk
		packetWriter.writeH(0); // unk
		packetWriter.writeD(_player.getVarInt(PlayerVariables.DONATIONS_AVAILABLE)); // existing donations
	}
}