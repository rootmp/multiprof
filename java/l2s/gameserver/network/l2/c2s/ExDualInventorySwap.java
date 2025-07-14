package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class ExDualInventorySwap implements IClientIncomingPacket
{
	private int cSwapSlot;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cSwapSlot = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		/*player.getStatsRecorder().block();
		player.getInventory().lockModifyItemUpdates();
		
		
		String str = player.getInventory().getPaperdollItemsToString();
		for(ItemInstance item : player.getInventory().getPaperdollItems())
		{
			if(item != null && item.getEquipSlot() != 17 && item.getEquipSlot() != 18 && item.getEquipSlot() != 31)
				player.getInventory().unEquipItem(item);
		}
		player.getInventory().unlockModifyItemUpdates();
		
		StringBuilder hp = new StringBuilder();
		for(int henna_slot = 1; henna_slot < 5; henna_slot++)
		{
			HennaPoten hennaPoten = player.getHennaPoten(henna_slot);
			hp.append(hennaPoten.getPotenId() + ",");
		}
		
		player.setVar(PlayerVariables.INV_SLOT_INDEX_VAR, cSwapSlot);
		
		String[] dual_inv = player.getVar(PlayerVariables.INV_DUAL_VAR, "0-0").split(";");
		
		player.getInventory().lockModifyItemUpdates();
		
		if(dual_inv != null && dual_inv.length > 0)
		{
			for(String dual : dual_inv)
			{
				String[] param = dual.split("-");
				if(param == null || param.length == 0)
					continue;
				int slot = Integer.parseInt(param[1]);
		
				ItemInstance item = player.getInventory().getItemByObjectId(Integer.valueOf(param[0]));
				if(item != null && ItemFunctions.checkIfCanEquip(player, item) == null)
				{
					player.getInventory().setPaperdollItem(slot, item);
				}
			}
		}
		String[] hennaPoten = player.getVar(PlayerVariables.INV_DUAL_HENNA_VAR, "0,0,0,0").split(",");
		for(int henna_slot = 0; henna_slot < hennaPoten.length; henna_slot++)
		{
			player.potenSelect(henna_slot + 1, Integer.parseInt(hennaPoten[henna_slot]));
		}
		
		
		player.getInventory().unlockModifyItemUpdates();
		player.getStatsRecorder().unblock();
		
		player.setVar(PlayerVariables.INV_DUAL_VAR, str);
		player.setVar(PlayerVariables.INV_DUAL_HENNA_VAR, hp.toString());
		player.sendSkillList();
		player.sendPacket(new ExUserViewInfoParameter(player));
		player.sendPacket(new ExDualInventoryInfo(player));
		player.sendChanges();*/
	}

}
