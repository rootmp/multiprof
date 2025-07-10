package npc.model;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.templates.npc.NpcTemplate;

/**
 * @author nexvill
 */
public class DetonInstance extends NpcInstance
{
	public DetonInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void showMainChatWindow(Player player, boolean firstTalk, Object... replace)
	{
		if (getLoc().isInRange(new Location(183578, -16041, -2712), 1000))
		{
			showChatWindow(player, "teleporter/34143-FieldsOfMassacre.htm", firstTalk);
		}
		else if (getLoc().isInRange(new Location(138735, 19646, -3648), 1000))
		{
			showChatWindow(player, "teleporter/34143-PlainsOfGlory.htm", firstTalk);
		}
		else if (getLoc().isInRange(new Location(159801, 20810, -3704), 1000))
		{
			showChatWindow(player, "teleporter/34143-WarTornPlains.htm", firstTalk);
		}
		else if (getReflectionId() == -1001)
		{
			showChatWindow(player, "teleporter/34143-AlligatorIsland.htm", firstTalk);
		}
	}
}
