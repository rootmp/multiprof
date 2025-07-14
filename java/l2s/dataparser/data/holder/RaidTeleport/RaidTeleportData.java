package l2s.dataparser.data.holder.RaidTeleport;

import l2s.dataparser.data.annotations.array.IntArray;
import l2s.dataparser.data.annotations.value.IntValue;

public class RaidTeleportData
{
	@IntValue
	public int raid_id;
	@IntArray
	public int[] raid_location;
}