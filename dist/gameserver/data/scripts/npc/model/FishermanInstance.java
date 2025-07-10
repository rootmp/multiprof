package npc.model;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.MerchantInstance;
import l2s.gameserver.templates.npc.NpcTemplate;

/**
 * @author Bonux
 **/
public class FishermanInstance extends MerchantInstance
{
	public FishermanInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void onMenuSelect(Player player, int ask, long reply, int state)
	{
		if (ask == -141210)
		{
			if (reply == 1)
				showChatWindow(player, "default/fishing_manual001.htm", false);
			else if (reply == 2)
				showChatWindow(player, "default/fishing_manual002.htm", false);
			else if (reply == 3)
				showChatWindow(player, "default/fishing_manual003.htm", false);
			else if (reply == 4)
				showChatWindow(player, "default/fishing_manual004.htm", false);
			else if (reply == 5)
				showChatWindow(player, "default/fishing_manual005.htm", false);
		}
		else
			super.onMenuSelect(player, ask, reply, state);
	}

	@Override
	public void showChatWindow(Player player, int val, boolean firstTalk, Object... replace)
	{
		if (val == 0)
			showChatWindow(player, "default/fisherman001.htm", firstTalk);
		else
			super.showChatWindow(player, val, firstTalk, replace);
	}

	@Override
	public void onSkillLearnBypass(Player player)
	{
		showFishingSkillList(player);
	}
}