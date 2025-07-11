package npc.model.residences.fortress;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Residence;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.npc.NpcTemplate;

import npc.model.residences.ResidenceManager;

public class ManagerInstance extends ResidenceManager
{
	public ManagerInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	protected void setDialogs()
	{
		_mainDialog = "residence2/fortress/fortress_steward001.htm";
		_failDialog = "residence2/fortress/fortress_steward002.htm";
		_siegeDialog = "residence2/fortress/fortress_steward018.htm";
	}

	@Override
	protected int getCond(Player player)
	{
		Residence residence = getResidence();
		Clan residenceOwner = residence.getOwner();
		if (residenceOwner != null && player.getClan() == residenceOwner)
		{
			if (residence.getSiegeEvent().isInProgress())
				return COND_SIEGE;
			else
				return COND_OWNER;
		}
		else
			return COND_FAIL;
	}

	@Override
	protected Residence getResidence()
	{
		return getFortress();
	}

	@Override
	public IClientOutgoingPacket decoPacket()
	{
		return null;
	}

	@Override
	protected int getPrivUseFunctions()
	{
		return Clan.CP_CS_USE_FUNCTIONS;
	}

	@Override
	protected int getPrivSetFunctions()
	{
		return Clan.CP_CS_SET_FUNCTIONS;
	}

	@Override
	protected int getPrivDismiss()
	{
		return Clan.CP_CS_DISMISS;
	}

	@Override
	protected int getPrivDoors()
	{
		return Clan.CP_CS_ENTRY_EXIT;
	}

	@Override
	protected String getDialogsPrefix()
	{
		return "fortress";
	}
}