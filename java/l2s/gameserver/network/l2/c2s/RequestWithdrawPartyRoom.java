package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.matching.MatchingRoom;
import l2s.gameserver.network.l2.GameClient;

/**
 * Format (ch) dd
 */
public class RequestWithdrawPartyRoom implements IClientIncomingPacket
{
	private int _roomId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_roomId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		final MatchingRoom room = player.getMatchingRoom();
		if (room == null || room.getId() != _roomId || room.getType() != MatchingRoom.PARTY_MATCHING)
			return;

		final int type = room.getMemberType(player);
		if (type == MatchingRoom.ROOM_MASTER || type == MatchingRoom.PARTY_MEMBER)
		{
			player.setMatchingRoomWindowOpened(false);
			return;
		}

		room.removeMember(player, false);
	}
}