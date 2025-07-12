package l2s.gameserver.network.l2.c2s.worldexchange;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.WorldExchangeManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.stats.Stats;

public class RequestExWorldExchangeRegiItem implements IClientIncomingPacket
{
	private long _price;
	private int _itemId;
	private long _amount;
	private int currencyType;
	private int listingType;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_price = packet.readQ();
		_itemId = packet.readD();
		_amount = packet.readQ();
		currencyType = packet.readC();
		listingType = packet.readC();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
			return;
		
		Player player = client.getActiveChar();
		if (player == null)
			return;
		
		if(player.getStat().calc(Stats.P_BLOCK_WORLD_TRADE) != 0)
		{
			player.sendPacket(SystemMsg.S_19020);
			player.sendActionFailed();
			return;
		}
		
		
		WorldExchangeManager.getInstance().registerItemBid(player, _itemId, _amount, _price, listingType, currencyType);
	}
}
