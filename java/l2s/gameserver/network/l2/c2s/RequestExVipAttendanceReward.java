package l2s.gameserver.network.l2.c2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExVipAttendanceReward;
import l2s.gameserver.tables.GmListTable;

public class RequestExVipAttendanceReward implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestExVipAttendanceReward.class);
	private int bResult;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		bResult = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		if(player.getVipAttendance().getAttendanceDay() < bResult)
		{
			GmListTable.broadcastMessageToGMs("VipAttendanceReward: " + player + " hack! " + bResult);
			_log.warn("ExVipAttendanceReward: " + player + " hack! bResult" + bResult);
		}
		else
		{
			if(player.getVipAttendance().getAvailableRewards())
			{
				player.getVipAttendance().receiveReward(bResult);
				player.sendPacket(new ExVipAttendanceReward(1));
			}
			else
				player.sendPacket(new ExVipAttendanceReward(0));
		}
	}
}