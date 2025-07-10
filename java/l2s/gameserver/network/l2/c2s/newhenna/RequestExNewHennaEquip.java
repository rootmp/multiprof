package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.newhenna.ExNewHennaEquip;
import l2s.gameserver.templates.henna.HennaTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExNewHennaEquip implements IClientIncomingPacket
{
	private int cSlotID;
	private int nItemSid;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cSlotID = packet.readC();
		nItemSid = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		player.getInventory().writeLock();
		try
		{
			ItemInstance item = player.getInventory().getItemByObjectId(nItemSid);
			if (item == null)
			{
				player.sendPacket(SystemMsg.THE_SYMBOL_CANNOT_BE_DRAWN);
				player.sendPacket(new ExNewHennaEquip(cSlotID));
				return;
			}

			HennaTemplate template = HennaHolder.getInstance().getHennaByItemId(item.getItemId());
			if (template == null)
			{
				player.sendPacket(SystemMsg.THE_SYMBOL_CANNOT_BE_DRAWN);
				player.sendPacket(new ExNewHennaEquip(cSlotID));
				return;
			}

			Henna henna = player.getHennaList().get(cSlotID);
			if (henna == null)
			{
				player.sendPacket(SystemMsg.NO_SLOT_EXISTS_TO_DRAW_THE_SYMBOL);
				player.sendPacket(new ExNewHennaEquip(cSlotID));
				return;
			}

			if (!template.isForThisClass(player))
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_A_SYMBOL_BECAUSE_YOU_DONT_MEET_THE_CLASS_REQUIREMENTS);
				player.sendPacket(new ExNewHennaEquip(cSlotID));
				return;
			}

			if (template.getDyeLvl() > player.getLevel())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_A_SYMBOL_BECAUSE_YOU_ARE_BELOW_THE_REQUIRED_LEVEL);
				player.sendPacket(new ExNewHennaEquip(cSlotID));
				return;
			}

			if (item.getCount() < template.getDrawCount())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_A_SYMBOL_BECAUSE_YOU_DONT_HAVE_ENOUGH_DYE);
				player.sendPacket(new ExNewHennaEquip(cSlotID));
				return;
			}

			long drawPrice = template.getDrawPrice();
			if (drawPrice > 0 && drawPrice > player.getAdena())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_A_SYMBOL_BECAUSE_YOU_DONT_HAVE_ENOUGH_ADENA);
				player.sendPacket(new ExNewHennaEquip(cSlotID));
				return;
			}

			if (ItemFunctions.deleteItem(player, item, template.getDrawCount(), true) && (drawPrice == 0 || player.reduceAdena(template.getDrawPrice())))
			{
				henna.setTemplate(template);
				henna.updated(true);
				player.getHennaList().refreshStats(true);
				player.sendSkillList();
				player.sendPacket(new ExNewHennaEquip(cSlotID, template.getSymbolId()));
			}
		}
		finally
		{
			player.getInventory().writeUnlock();
		}
	}
}