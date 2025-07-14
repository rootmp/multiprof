package l2s.gameserver.network.l2.c2s;

import java.util.Calendar;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.MissionLevelRewardsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExMissionLevelRewardList;
import l2s.gameserver.templates.dailymissions.MissionLevelRewardTemplate;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.TimeUtils;

public class RequestExMissionLevelJumpLevel implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

		MissionLevelRewardTemplate template = MissionLevelRewardsHolder.getInstance().getRewardsInfo(month, year);
		if(template == null)
			return;

		if(player.getPlayer().getMissionLevelReward().getLevel() >= template.getMaxRewardLvl()
				|| player.getAccVar().getVarBoolean("MissionLevelJumpLevel", false))
			return;

		if(!ItemFunctions.deleteItem(player, 4037, 2000))
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
			return;
		}

		player.getAccVar().setVar("MissionLevelJumpLevel", true, TimeUtils.MONTHLY_DATE_PATTERN.next(System.currentTimeMillis()));

		player.getMissionLevelReward().setLevel(template.getMaxRewardLvl());
		player.getMissionLevelReward().setPoints(0);
		player.getMissionLevelReward().store();

		player.sendPacket(new ExMissionLevelRewardList(player, month, template));
	}
}
