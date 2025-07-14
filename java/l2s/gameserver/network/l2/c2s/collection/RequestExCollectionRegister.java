package l2s.gameserver.network.l2.c2s.collection;

import java.util.Map;
import java.util.stream.Collectors;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.clientDat.CollectionsData;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionComplete;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionRegister;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.item.data.CollectionItemData;

public class RequestExCollectionRegister implements IClientIncomingPacket
{
	private int nCollectionID, nSlotNumber, nItemSid;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nCollectionID = packet.readH();
		nSlotNumber = packet.readD();
		nItemSid = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
		
		ItemInstance item = player.getInventory().getItemByObjectId(nItemSid);
		if (item == null)
			return;
		
		CollectionTemplate collection = CollectionsData.getInstance().getCollection(nCollectionID);
		CollectionTemplate newCollection = new CollectionTemplate(nCollectionID, collection.getTabId(), 0);

		long itemCount = 0;
		for (CollectionItemData temp : collection.getItems())
		{
			if (temp.getId() == item.getItemId())
			{
				if (temp.getEnchantLevel() == item.getEnchantLevel() && temp.getCount() <= item.getCount() && temp.getSlotId() == nSlotNumber)
				{
					CollectionItemData itemData = new CollectionItemData(item.getItemId(), temp.getCount(), temp.getEnchantLevel(), nSlotNumber, temp.getBless(), temp.getBlessCondition());
					newCollection.addItem(itemData);
					player.getCollectionList().add(newCollection);
					itemCount = temp.getCount();
					player.getInventory().destroyItem(item, itemCount);
					player.sendPacket(new ExCollectionRegister(player, nCollectionID, nSlotNumber, item.getItemId(), item.getEnchantLevel(), item.isBlessed(),(int) itemCount));
					break;
				}
			}
		}
		
		Map<Integer, CollectionTemplate> tmp = CollectionsData.getInstance().getPlayerCollection(player,collection.getTabId());
		if (tmp.get(nCollectionID).getItems().stream().collect(Collectors.groupingBy(r -> r.getSlotId())).size() == collection.getMaxSlot())
		{
			player.sendPacket(new ExCollectionComplete(nCollectionID));
			player.getListeners().onPlayerCollectionComplete(nCollectionID);
		}
		player.sendUserInfo(true);
		player.broadcastUserInfo(true);
	}
}