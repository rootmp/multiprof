package l2s.gameserver.network.l2.c2s.enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.enchant.ExChangedEnchantTargetItemProbabilityList;
import l2s.gameserver.network.l2.s2c.enchant.ExResultSetMultiEnchantItemList;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExSetMultiEnchantItemList implements IClientIncomingPacket
{
	private static final int MAX_SIZE = 99;
	private int[] _itemObjId;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		int size = packet.readD();
		if(size > MAX_SIZE) 
			size = MAX_SIZE;
		
		_itemObjId = new int[size];
		
		for (int index = 0; index < size; index++)
		{
			_itemObjId[index] = packet.readD();
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;
		
		player.clearMultiEnchantingItemsBySlot();
		
		for (int index = 0; index < _itemObjId.length; index++)
		{
			player.addMultiEnchantingItems(index + 1, _itemObjId[index]);
		}
		
		player.sendPacket(new ExResultSetMultiEnchantItemList(0));
		player.sendPacket(new ExChangedEnchantTargetItemProbabilityList(ItemFunctions.getEnchantProbInfo(player, true, false)));
	}
}
		
