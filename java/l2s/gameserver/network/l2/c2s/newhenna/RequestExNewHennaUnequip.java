package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.newhenna.ExNewHennaUnequip;
import l2s.gameserver.templates.henna.HennaTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExNewHennaUnequip extends L2GameClientPacket
{
	private int cSlotID;

	@Override
	protected boolean readImpl()
	{
		cSlotID = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		Henna henna = player.getHennaList().get(cSlotID);
		if (henna == null)
		{
			player.sendPacket(new ExNewHennaUnequip(cSlotID, false));
			return;
		}

		HennaTemplate template = henna.getTemplate();
		if (template == null)
		{
			player.sendPacket(new ExNewHennaUnequip(cSlotID, false));
			return;
		}

		long removePrice = template.getRemovePrice();
		if (removePrice > 0 && !player.reduceAdena(removePrice))
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			player.sendPacket(new ExNewHennaUnequip(cSlotID, false));
			return;
		}

		henna.setTemplate(null);
		henna.updated(true);

		long removeCount = template.getRemoveCount();
		if (removeCount > 0)
		{
			ItemFunctions.addItem(player, template.getDyeId(), removeCount, true);
		}

		player.getHennaList().refreshStats(true);
		player.sendSkillList();
		player.sendPacket(new ExNewHennaUnequip(cSlotID, true));
	}
}