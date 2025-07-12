package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.utils.PositionUtils;


public class MoveToWard implements IClientIncomingPacket
{
	private int _h;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_h = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		
		if(player.isOutOfControl() || player.isActionBlocked("move"))
		{
			player.sendActionFailed();
			return;
		}

		double angle = PositionUtils.convertHeadingToDegree(_h); 
		double radian = Math.toRadians(angle - 90); 
		double range = player.getMoveSpeed();
		Location loc = new Location((int) (player.getX() - range * Math.sin(radian)), (int) (player.getY() + range * Math.cos(radian)), player.getZ());
		
		player.getMovement().moveWithKeyboard(loc);
	}
}