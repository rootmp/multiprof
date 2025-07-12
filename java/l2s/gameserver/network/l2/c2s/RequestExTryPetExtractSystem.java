package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.dao.PetDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.pets.ExResultPetExtractSystem;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExTryPetExtractSystem implements IClientIncomingPacket
{
	private int nPetItemSID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nPetItemSID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
		/*ItemInstance petItem = player.getInventory().getItemByObjectId(nPetItemSID);
		if(petItem == null)
			return;

		if(player.getPet() != null && player.getPet().getControlItemObjId() == nPetItemSID)
		{
			player.sendPacket(new ExResultPetExtractSystem(0));
			player.sendActionFailed();
			return;
		}

		PetData pet_data = PetDataHolder.getInstance().getPetDataByItem(petItem.getItemId());

		if(petItem.getEnchantLevel() < pet_data.extract_level[0] && petItem.getEnchantLevel() > pet_data.extract_level[1])
		{
			player.sendPacket(new ExResultPetExtractSystem(0));
			player.sendActionFailed();
			return;
		}
		PetLevelStat stat_for_level = pet_data.getLevelStatForLevel(petItem.getEnchantLevel());
		if(stat_for_level == null)
		{
			player.sendPacket(new ExResultPetExtractSystem(0));
			player.sendActionFailed();
			return;
		}

		//проверка фикс ставки
		if(!ItemFunctions.haveItem(player, stat_for_level.default_cost.itemName, stat_for_level.default_cost.count))
		{
			player.sendPacket(new ExResultPetExtractSystem(0));
			player.sendActionFailed();
			return;
		}
		//проверка за каждый полученный камень
		long pet_exp = PetDAO.getInstance().getPetExpByItemId(nPetItemSID);
		//количество итемов зависит от експы пета 
		int extract_item_count = (int) Math.floor(pet_exp / stat_for_level.extract_exp);

		long extract_cost_count = stat_for_level.extract_cost.count * extract_item_count;
		//проверяем адену
		if(!ItemFunctions.haveItem(player, stat_for_level.extract_cost.itemName, extract_cost_count))
		{
			player.sendPacket(new ExResultPetExtractSystem(0));
			player.sendActionFailed();
			return;
		}
		//удаляем пета 
		PetDAO.getInstance().deletePet(petItem, player);
		if(!ItemFunctions.deleteItem(player, petItem, 1, getClass().getName()))
		{
			player.sendPacket(new ExResultPetExtractSystem(0));
			player.sendActionFailed();
			return;
		}
		//выдаем итемы

		ItemFunctions.addItem(player, stat_for_level.extract_item, extract_item_count, getClass().getName());

		player.sendPacket(new ExResultPetExtractSystem(1));*/
	}

}
