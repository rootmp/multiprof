package npc.model;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.MerchantInstance;
import l2s.gameserver.network.l2.s2c.pets.ExShowPetExtractSystem;
import l2s.gameserver.templates.npc.NpcTemplate;

/**
 * @author nexvill
 */
public class PetManagerInstance extends MerchantInstance
{
	public PetManagerInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void onMenuSelect(Player player, int ask, long reply, int state)
	{
		if (ask == -20200416)
		{
			player.sendPacket(new ExShowPetExtractSystem());
		}
		if (ask == 1)
		{
			showShopWindow(player, 1, true);
		}
		else
			super.onMenuSelect(player, ask, reply, state);
	}

	@Override
	public void showChatWindow(Player player, int val, boolean firstTalk, Object... arg)
	{
		if (val == 0)
			showChatWindow(player, "default/pet_manager.htm", firstTalk);
		else
			super.showChatWindow(player, val, firstTalk, arg);
	}
}
