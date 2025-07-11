package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.ObservePoint;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class CannotMoveAnymore implements IClientIncomingPacket
{
	private static abstract class Boat extends CannotMoveAnymore
	{
		protected int _boatId = -1;

		@Override
		public boolean readImpl(GameClient client, PacketReader packet)
		{
			_boatId = packet.readD();
			_loc.x = packet.readD();
			_loc.y = packet.readD();
			_loc.z = packet.readD();
			_loc.h = packet.readD();
			return true;
		}

		@Override
		public void run(GameClient client)
		{
			Player activeChar = client.getActiveChar();
			if (activeChar == null)
				return;

			l2s.gameserver.model.entity.boat.Boat boat = activeChar.getBoat();
			if (boat != null && boat.getBoatId() == _boatId)
			{
				activeChar.setInBoatPosition(_loc);
				activeChar.setHeading(_loc.h);
				activeChar.broadcastPacket(boat.inStopMovePacket(activeChar));
			}
		}
	}

	public static class AirShip extends Boat
	{
		//
	}

	public static class Vehicle extends Boat
	{
		//
	}

	public static class Shuttle extends Boat
	{
		//
	}

	protected final Location _loc = new Location();

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_loc.x = packet.readD();
		_loc.y = packet.readD();
		_loc.z = packet.readD();
		_loc.h = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isInObserverMode())
		{
			ObservePoint observer = activeChar.getObservePoint();
			if (observer != null)
				observer.getMovement().stopMove();
			return;
		}

		activeChar.getAI().notifyEvent(CtrlEvent.EVT_ARRIVED_BLOCKED, _loc, null);
	}
}