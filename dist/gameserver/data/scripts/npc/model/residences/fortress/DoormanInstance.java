package npc.model.residences.fortress;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Residence;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.ReflectionUtils;

/**
 * @author VISTALL
 * @date 13:47/02.04.2011
 */
public class DoormanInstance extends npc.model.residences.DoormanInstance
{
	private Location _loc;

	public DoormanInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		int cond = getCond(player);
		switch (cond)
		{
			case COND_OWNER:
				if (command.equalsIgnoreCase("openDoors"))
					for (int i : _doors)
						ReflectionUtils.getDoor(i).openMe(player, true);
				else if (command.equalsIgnoreCase("closeDoors"))
					for (int i : _doors)
						ReflectionUtils.getDoor(i).closeMe(player, true);
				break;
			case COND_SIEGE:
				player.sendPacket(new HtmlMessage(this, _siegeDialog));
				break;
			case COND_FAIL:
				player.sendPacket(new HtmlMessage(this, _failDialog));
				break;
		}
	}

	@Override
	public void setDialogs()
	{
		_mainDialog = "residence2/fortress/fortress_doorkeeper001.htm";
		_failDialog = "residence2/fortress/fortress_doorkeeper002.htm";
		_siegeDialog = "residence2/fortress/fortress_doorkeeper003.htm";
	}

	@Override
	public int getOpenPriv()
	{
		return Clan.CP_CS_ENTRY_EXIT;
	}

	@Override
	public Residence getResidence()
	{
		return getFortress();
	}
}
