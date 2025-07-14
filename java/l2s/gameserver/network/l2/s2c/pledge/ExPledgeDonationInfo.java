package l2s.gameserver.network.l2.s2c.pledge;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.PledgeContributionHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

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
		packetWriter.writeD(_player.getVarInt(PlayerVariables.DONATIONS_AVAILABLE, PledgeContributionHolder.getInstance().getDonationsAvailable())); // available donations today
		packetWriter.writeC(0); // bNewbie
		return true;
	}
}