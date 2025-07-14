package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExVipAttendanceCheck;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExVipAttendanceCheck implements IClientIncomingPacket
{
	private int cDay;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cDay = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;

		if(player.getVipAttendance().daysPassedSinceStartDate() >= cDay && cDay <= 28)
		{
			if(ItemFunctions.deleteItem(player, 91663, 100))
			{
				player.getVipAttendance().IncAttendanceDay();
				player.sendPacket(new ExVipAttendanceCheck(1));
			}
			else
				player.sendPacket(new ExVipAttendanceCheck(0));
		}
		else
			player.sendPacket(new ExVipAttendanceCheck(0));
	}
}