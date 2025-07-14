package l2s.dataparser.data.holder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.dataparser.data.annotations.Element;
import l2s.dataparser.data.holder.RaidTeleport.RaidTeleportCostData;
import l2s.dataparser.data.holder.RaidTeleport.RaidTeleportData;
import l2s.dataparser.data.holder.RaidTeleport.UseSystemData;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.templates.TeleportTemplate;
import l2s.gameserver.templates.item.ItemTemplate;

public class RaidTeleportHolder extends AbstractHolder
{
	@Element(start = "raid_teleport_system_begin", end = "raid_teleport_system_end")
	private UseSystemData raid_teleport_system_data;
	
	@Element(start = "raid_teleport_cost_begin", end = "raid_teleport_cost_end")
	private RaidTeleportCostData raid_teleport_const_data;
	
	@Element(start = "raid_teleport_begin", end = "raid_teleport_end")
	private List<RaidTeleportData> raid_teleport_data;

	private static RaidTeleportHolder ourInstance = new RaidTeleportHolder();

	private final Map<Integer, TeleportTemplate> _teleportsInfos = new HashMap<>();
	
	public static RaidTeleportHolder getInstance()
	{
		return ourInstance;
	}

	@Override
	public void clear()
	{

	}

	@Override
	public void afterParsing() 
	{
		super.afterParsing();
		for(RaidTeleportData tp: raid_teleport_data)
		{
			TeleportTemplate teleport = new TeleportTemplate(tp.raid_id,ItemTemplate.ITEM_ID_MONEY_L,raid_teleport_const_data.lcoin_cost);
			teleport.addLocation(new Location(tp.raid_location));
			_teleportsInfos.put(tp.raid_id, teleport);
		}
	}
	@Override
	public int size()
	{
		return raid_teleport_data.size();
	}

	public TeleportTemplate getTeleportInfo(int _raidId)
	{
		return _teleportsInfos.get(_raidId);
	}
}