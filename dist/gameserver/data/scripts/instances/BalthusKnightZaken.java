package instances;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.NpcUtils;

public class BalthusKnightZaken extends Reflection
{
	private final int Zaken_RAID_NPC_ID = 29119; // Закен

	private final IntSet rewardedPlayers = new HashIntSet();

	public boolean spawnZakenmRaid(NpcInstance stoneNpc, Player player)
	{

		NpcUtils.spawnSingle(Zaken_RAID_NPC_ID, 55368, 219160, -3225, this);
		return true;
	}

	public boolean isRewardReceived(Player player)
	{
		return rewardedPlayers.contains(player.getObjectId());
	}

	public void setRewardReceived(Player player)
	{
		rewardedPlayers.add(player.getObjectId());
	}
}