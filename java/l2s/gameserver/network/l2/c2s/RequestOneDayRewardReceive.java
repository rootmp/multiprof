package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExConnectedTimeAndGettableReward;
import l2s.gameserver.network.l2.s2c.ExOneDayReceiveRewardList;

/**
 * @author Bonux
 **/
public class RequestOneDayRewardReceive implements IClientIncomingPacket
{
	private int _missionId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_missionId = packet.readH();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.getDailyMissionList().complete(_missionId))
		{
			activeChar.sendPacket(new ExConnectedTimeAndGettableReward(activeChar));
			activeChar.sendPacket(new ExOneDayReceiveRewardList(activeChar));
		}
	}
}