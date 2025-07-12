package l2s.gameserver.network.l2.c2s;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.MissionLevelRewardsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExMissionLevelRewardList;
import l2s.gameserver.templates.dailymissions.MissionLevelRewardTemplate;

public class RequestExMissionLevelRewardList implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestExMissionLevelRewardList.class);

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if(player != null)
		{
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			MissionLevelRewardTemplate template = MissionLevelRewardsHolder.getInstance().getRewardsInfo(month);
			if(template == null)
			{
				_log.error("month:" + month + " template == null ");
				return;
			}
			player.sendPacket(new ExMissionLevelRewardList(player, month, template));
		}
	}

}
