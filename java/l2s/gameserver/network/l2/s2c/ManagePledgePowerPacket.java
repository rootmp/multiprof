package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.RankPrivs;

public class ManagePledgePowerPacket implements IClientOutgoingPacket
{
	private int _action, _clanId, privs;

	public ManagePledgePowerPacket(Player player, int action, int rank)
	{
		_clanId = player.getClanId();
		_action = action;
		RankPrivs temp = player.getClan().getRankPrivs(rank);
		privs = temp == null ? 0 : temp.getPrivs();
		player.sendPacket(new PledgeReceiveUpdatePower(privs));
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_clanId);
		packetWriter.writeD(_action);
		packetWriter.writeD(privs);
		return true;
	}
}