package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.BoatHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.boat.Boat;
import l2s.gameserver.network.l2.GameClient;

public class RequestGetOffVehicle implements IClientIncomingPacket
{
	// Format: cdddd
	private int _objectId;
	private Location _location = new Location();

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
		_location.x = packet.readD();
		_location.y = packet.readD();
		_location.z = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;

		Boat boat = BoatHolder.getInstance().getBoat(_objectId);
		if(boat == null || boat.getMovement().isMoving())
		{
			player.sendActionFailed();
			return;
		}

		boat.oustPlayer(player, _location, false);
	}
}