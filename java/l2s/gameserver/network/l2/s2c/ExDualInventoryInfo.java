package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.templates.item.henna.HennaPoten;

public class ExDualInventoryInfo implements IClientOutgoingPacket
{
	private int _cSwapSlot;
	private Player _player;

	public ExDualInventoryInfo(Player player)
	{
		_player = player;
		_cSwapSlot = player.getVarInt(PlayerVariables.INV_SLOT_INDEX_VAR, 0);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_cSwapSlot);//cActiveSlot
		packetWriter.writeC(1);//bSuccess
		packetWriter.writeC(1);//bStableSwapping
		ItemInstance[] slot0 = _player.getInventory().getPaperdollItems();
		packetWriter.writeD(slot0.length);
		if(slot0.length > 0)
			for(ItemInstance lSlotDBID : slot0)
			{
				if(lSlotDBID == null)
					packetWriter.writeD(0);
				else
					packetWriter.writeD(lSlotDBID.getObjectId());
			}

		packetWriter.writeD(4);//size lHennaPotenID
		packetWriter.writeD(_player.getHennaPoten(1).getPotenId());
		packetWriter.writeD(_player.getHennaPoten(2).getPotenId());
		packetWriter.writeD(_player.getHennaPoten(3).getPotenId());
		packetWriter.writeD(_player.getHennaPoten(4).getPotenId());

		String[] dual_inv = _player.getVar(PlayerVariables.INV_DUAL_VAR, "0").split(";");
		packetWriter.writeD(dual_inv.length);
		if(dual_inv != null && dual_inv.length > 0)
		{
			for(String dual : dual_inv)
			{
				String[] param = dual.split("-");
				if(param == null || param.length == 0)
					continue;

				ItemInstance item = _player.getInventory().getItemByObjectId(Integer.valueOf(param[0]));
				if(item != null)
					packetWriter.writeD(item.getObjectId());
				else
					packetWriter.writeD(0);
			}
		}

		packetWriter.writeD(4);//size lHennaPotenID
		String[] hennaPoten = _player.getVar(PlayerVariables.INV_DUAL_HENNA_VAR, "0,0,0,0").split(",");
		for(int henna_slot = 0; henna_slot < hennaPoten.length; henna_slot++)
		{
			HennaPoten hP = _player.getHennaPoten(henna_slot + 1);
			packetWriter.writeD(hP.getPotenId());
		}
		/*packetWriter.writeD(0);
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		packetWriter.writeD(0);*/
		return true;
	}

}
