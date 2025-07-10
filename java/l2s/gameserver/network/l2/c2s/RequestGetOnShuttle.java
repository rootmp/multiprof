package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.data.BoatHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.boat.Boat;

/**
 * @author Bonux
 */
public class RequestGetOnShuttle extends L2GameClientPacket
{
	private int _shuttleId;
	private Location _loc = new Location();

	@Override
	protected boolean readImpl()
	{
		_shuttleId = readD();
		_loc.x = readD();
		_loc.y = readD();
		_loc.z = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		Boat boat = BoatHolder.getInstance().getBoat(_shuttleId);
		if (boat == null)
			return;

		boat.addPlayer(player, _loc);
	}
}