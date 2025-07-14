package l2s.gameserver.network.l2.s2c;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.MissionLevelReward;
import l2s.gameserver.templates.dailymissions.MissionLevelRewardTemplate;
import l2s.gameserver.templates.item.data.MissionLevelRewardData;

public class ExMissionLevelRewardList implements IClientOutgoingPacket
{
	private final Player _player;
	private MissionLevelRewardTemplate _template;
	private int _month;

	public ExMissionLevelRewardList(Player player, int month, MissionLevelRewardTemplate template)
	{
		_player = player;
		_month = month;
		_template = template;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int maxRewardLvl = _template.getMaxRewardLvl();
		int size = maxRewardLvl + _template.additionalRewardsSize() + 11;
		MissionLevelReward info = _player.getMissionLevelReward();
		int currentLvl = info.getLevel();
		double points = info.getPoints();
		int lastTakenBasic = info.lastTakenBasic();
		int lastTakenAdditional = info.lastTakenAdditional();
		int lastTakenBonus = info.lastTakenBonus();
		boolean takenFinal = info.takenFinal();

		int totalAvailable = 0;
		int extraAvailable = 0;

		packetWriter.writeD(size); // rewards size

		int i = 1;
		while(i <= lastTakenBasic)
		{
			packetWriter.writeD(1); // type
			packetWriter.writeD(i); // level
			packetWriter.writeD(2); // state (0 - cannot received; 1 - can received; 2 - received)

			i++;
		}
		while(i <= maxRewardLvl)
		{
			if(currentLvl >= i)
			{
				packetWriter.writeD(1);
				packetWriter.writeD(i);
				packetWriter.writeD(1);
				totalAvailable++;
			}
			else
			{
				packetWriter.writeD(1);
				packetWriter.writeD(i);
				packetWriter.writeD(0);
			}
			i++;
		}
		// add reward
		i = 1;
		while(i <= lastTakenAdditional)
		{
			MissionLevelRewardData data = _template.getRewards().get(i - 1);
			if(data.getAdditionalReward().getId() != 0)
			{
				packetWriter.writeD(2);
				packetWriter.writeD(i);
				packetWriter.writeD(2);
			}
			i++;
		}
		while(i <= maxRewardLvl)
		{
			MissionLevelRewardData data = _template.getRewards().get(i - 1);
			if(currentLvl >= i)
			{
				if(data.getAdditionalReward().getId() != 0)
				{
					packetWriter.writeD(2);
					packetWriter.writeD(i);
					packetWriter.writeD(1);
					totalAvailable++;
					extraAvailable++;
				}
			}
			else
			{
				if(data.getAdditionalReward().getId() != 0)
				{
					packetWriter.writeD(2);
					packetWriter.writeD(i);
					packetWriter.writeD(0);
				}
			}
			i++;
		}
		// final reward
		if(currentLvl < maxRewardLvl)
		{
			packetWriter.writeD(3);
			packetWriter.writeD(20);
			packetWriter.writeD(0);
		}
		else
		{
			if(!takenFinal)
			{
				packetWriter.writeD(3);
				packetWriter.writeD(20);
				packetWriter.writeD(1);
				totalAvailable++;
				extraAvailable++;
			}
			else
			{
				packetWriter.writeD(3);
				packetWriter.writeD(20);
				packetWriter.writeD(2);
			}
		}
		// bonus reward
		i = 21;
		while(i <= lastTakenBonus)
		{
			packetWriter.writeD(4);
			packetWriter.writeD(i);
			packetWriter.writeD(2);
			i++;
		}
		while(i <= 30)
		{
			if(currentLvl >= i)
			{
				packetWriter.writeD(4);
				packetWriter.writeD(i);
				packetWriter.writeD(1);
				totalAvailable++;
				extraAvailable++;
			}
			else
			{
				packetWriter.writeD(4);
				packetWriter.writeD(i);
				packetWriter.writeD(0);
			}
			i++;
		}

		MissionLevelRewardData data = _template.getRewards().get(Math.min(currentLvl, _template.getRewards().size() - 1));
		double percents = (points / data.getValue()) * 100;
		LocalDateTime nextChange;
		long seasonEnd;
		if(_month == 12)
		{
			nextChange = LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR) + 1, Month.JANUARY, 1, 6, 30);
		}
		else
		{
			nextChange = LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR), _month + 1, 1, 6, 30);
		}

		seasonEnd = nextChange.atZone(ZoneId.systemDefault()).toEpochSecond() - (System.currentTimeMillis() / 1000);

		packetWriter.writeD(currentLvl); // current level
		packetWriter.writeD((int) percents); // current percents on level
		packetWriter.writeD(Calendar.getInstance().get(Calendar.YEAR)); // season year
		packetWriter.writeD(_month); // season month
		packetWriter.writeD(totalAvailable); // total rewards available
		packetWriter.writeD(extraAvailable); // extra rewards available
		packetWriter.writeD((int) seasonEnd); // remain season time

		packetWriter.writeC(_player.getAccVar().getVarBoolean("MissionLevelJumpLevel", false)); //bAccountLevelJumped
		return true;
	}
}
