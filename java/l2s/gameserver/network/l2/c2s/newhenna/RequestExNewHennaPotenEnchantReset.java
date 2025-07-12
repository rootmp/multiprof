package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.newhenna.ExNewHennaPotenEnchantReset;
import l2s.gameserver.network.l2.s2c.newhenna.NewHennaList;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExNewHennaPotenEnchantReset implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		int _reset_count = player.getDyePotentialDailyResetCount();
		int count = 1000;
    if (_reset_count >= 3 && _reset_count <= 5) 
        count = 2000;
     else if (_reset_count >= 6) 
        count = 5000;
    
    if(!ItemFunctions.haveItem(player, 91663, count))
    {
    	player.sendPacket(new ExNewHennaPotenEnchantReset(0));
    	return;
    }
    if(ItemFunctions.deleteItem(player, 91663, count))
    {
    	player.resetHennaPotenDaily();
    	player.setDyePotentialDailyResetCount(player.getDyePotentialDailyResetCount()+1);
    	player.sendPacket(new ExNewHennaPotenEnchantReset(1));
    	player.sendPacket(new NewHennaList(player,1));

    }
	}

}


