package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.matching.MatchingRoom;
import l2s.gameserver.network.l2.GameClient;

/**
 * @author VISTALL
 */
public class RequestExWithdrawMpccRoom implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;

		MatchingRoom room = player.getMatchingRoom();
		if(room == null || room.getType() != MatchingRoom.CC_MATCHING)
			return;

		if(room.getLeader() == player)
			return;

		room.removeMember(player, false);
	}
}