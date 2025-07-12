package l2s.gameserver.network.l2.s2c;

import java.util.Collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.AttendanceRewardHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.data.AttendanceRewardData;

public class ExVipAttendanceList implements IClientOutgoingPacket
{
	private Collection<AttendanceRewardData> RewardItems;
	private int nMinimumLevel = 40;
	private int nRemainCheckTime = 0;
	private int cRollBookDay;
	private int cAttendanceDay;
	private int cRewardDay;
	private int cFollowBaseDay;
	private int bCheckable;
	private String sDate;
	
	public ExVipAttendanceList(Player player)
	{
		RewardItems = AttendanceRewardHolder.getInstance().getRewards(player.hasPremiumAccount());
		nRemainCheckTime = player.getVipAttendance().getLoginDelayTimeRemainingInSeconds();
		cRollBookDay = player.getVipAttendance().daysPassedSinceStartDate();

		cAttendanceDay = player.getVipAttendance().getAttendanceDay();
		cRewardDay = player.getVipAttendance().getRewardDay();

		cFollowBaseDay = player.getVipAttendance().getAttendanceDay();//getFollowBaseDay();
		bCheckable = nRemainCheckTime > 0 ? 1 : 0;
		
		sDate = AttendanceRewardHolder.getInstance().getEndDate();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(RewardItems.size());//Size
		for(AttendanceRewardData reward : RewardItems)
		{
			packetWriter.writeD(reward.getId());//nClassID
			packetWriter.writeQ(reward.getCount());//nAmount
			packetWriter.writeC(reward.isBest());//bHighlight
		}
		packetWriter.writeD(nMinimumLevel);
		packetWriter.writeD(nRemainCheckTime);
		packetWriter.writeC(cRollBookDay);
		packetWriter.writeC(cAttendanceDay);//
		packetWriter.writeC(cRewardDay);
		packetWriter.writeC(cFollowBaseDay);
		packetWriter.writeC(bCheckable);
		//custom
		packetWriter.writeSizedString(sDate);
		return true;
	}

}
