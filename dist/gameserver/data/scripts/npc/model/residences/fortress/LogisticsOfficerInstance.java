package npc.model.residences.fortress;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.data.xml.holder.MultiSellHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Fortress;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.templates.npc.NpcTemplate;

public class LogisticsOfficerInstance extends NpcInstance
{
	protected static final int COND_OWNER = 0;
	protected static final int COND_SIEGE = 1;
	protected static final int COND_FAIL = 2;

	private static final String MAIN_DIALOG = "residence2/fortress/fortress_logistics_officer001.htm";
	private static final String FAIL_DIALOG = "residence2/fortress/fortress_logistics_officer002.htm";
	private static final String SIEGE_DIALOG = "residence2/fortress/fortress_logistics_officer003.htm";

	public LogisticsOfficerInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void onMenuSelect(Player player, int ask, long reply, int state)
	{
		int cond = getCond(player);
		switch (cond)
		{
			case COND_OWNER:
				if (ask == -1000)
				{
					if (reply == 1)
					{
						MultiSellHolder.getInstance().SeparateAndSend(34148, player, 0);
						return;
					}
				}
				break;
			case COND_SIEGE:
				player.sendPacket(new HtmlMessage(this, SIEGE_DIALOG));
				return;
			case COND_FAIL:
				player.sendPacket(new HtmlMessage(this, FAIL_DIALOG));
				return;
		}
		super.onMenuSelect(player, ask, reply, state);
	}

	@Override
	public void showChatWindow(Player player, int val, boolean firstTalk, Object... arg)
	{
		String filename = null;
		int cond = getCond(player);
		switch (cond)
		{
			case COND_OWNER:
				filename = MAIN_DIALOG;
				break;
			case COND_SIEGE:
				filename = SIEGE_DIALOG;
				break;
			case COND_FAIL:
				filename = FAIL_DIALOG;
				break;
		}
		player.sendPacket(new HtmlMessage(this, filename).setPlayVoice(firstTalk));
	}

	protected int getCond(Player player)
	{
		Fortress fortress = getFortress();
		if (fortress != null)
		{
			Clan residenceOwner = fortress.getOwner();
			if (residenceOwner != null && player.getClan() == residenceOwner)
			{
				if (fortress.getSiegeEvent().isInProgress())
					return COND_SIEGE;
				else
					return COND_OWNER;
			}
			else
			{
				return COND_FAIL;
			}
		}
		else
		{
			return COND_FAIL;
		}
	}
}
