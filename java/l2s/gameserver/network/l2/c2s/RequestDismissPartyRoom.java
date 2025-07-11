package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.matching.MatchingRoom;
import l2s.gameserver.network.l2.GameClient;

/**
 * Format: (ch) dd
 */
public class RequestDismissPartyRoom implements IClientIncomingPacket
{
	private int _roomId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_roomId = packet.readD(); // room id
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		MatchingRoom room = player.getMatchingRoom();
		if (room == null || room.getId() != _roomId || room.getType() != MatchingRoom.PARTY_MATCHING)
			return;

		if (room.getLeader() != player)
			return;

		room.disband();
	}
}