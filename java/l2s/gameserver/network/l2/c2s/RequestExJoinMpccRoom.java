package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.instancemanager.MatchingRoomManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.matching.MatchingRoom;

/**
 * @author VISTALL
 */
public class RequestExJoinMpccRoom implements IClientIncomingPacket
{
	private int _roomId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_roomId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		if (player.getMatchingRoom() != null)
			return;

		MatchingRoom room = MatchingRoomManager.getInstance().getMatchingRoom(MatchingRoom.CC_MATCHING, _roomId);
		if (room == null)
			return;

		room.addMember(player);
	}
}