package l2s.gameserver.network.l2.c2s.collection;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

/**
 * @author 4ipolino
 */
public class RequestExCollectionReceiveReward implements IClientIncomingPacket
{
	private int nCollectionID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nCollectionID = packet.readH();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
		/*CollectionTemplate template = CollectionsData.getInstance().getCollection(nCollectionID);
		List<CollectionTemplate> collection = player.getCollectionList().get(nCollectionID);
		
		if(template == null || collection==null || collection.isEmpty())
			return;
		
		if(collection.size() == template.getMaxSlot() && player.getVarInt(PlayerVariables.COLLECTION_REWARD_ENABLED_VAR+nCollectionID, 0) == 0)
		{
			player.setVar(PlayerVariables.COLLECTION_REWARD_ENABLED_VAR+nCollectionID, 1);
			for(ItemData reward : template.getRewardItems())
			{
				ItemFunctions.addItem(player, reward.getId(), reward.getCount(), "ExCollectionReceiveReward");
			}
			player.sendPacket(new ExCollectionReceiveReward(nCollectionID,true));
			return;
		}
		player.sendPacket(new ExCollectionReceiveReward(nCollectionID,false));*/
	}
}