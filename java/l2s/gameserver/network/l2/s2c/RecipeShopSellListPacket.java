package l2s.gameserver.network.l2.s2c;

import java.util.Collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ManufactureItem;
import l2s.gameserver.stats.Stats;

public class RecipeShopSellListPacket implements IClientOutgoingPacket
{
	private int objId, curMp, maxMp;
	private long adena;
	private Collection<ManufactureItem> createList;
	private final int chanceBonus;
	private final int critChance;

	public RecipeShopSellListPacket(Player buyer, Player manufacturer)
	{
		objId = manufacturer.getObjectId();
		curMp = (int) manufacturer.getCurrentMp();
		maxMp = manufacturer.getMaxMp();
		adena = buyer.getAdena();
		createList = manufacturer.getCreateList().values();
		chanceBonus = (int) manufacturer.getStat().calc(Stats.CRAFT_CHANCE, 0);
		critChance = (int) manufacturer.getStat().calc(Stats.CRIT_CRAFT_CHANCE, 0);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(objId);
		packetWriter.writeD(curMp);// Creator's MP
		packetWriter.writeD(maxMp);// Creator's MP
		packetWriter.writeQ(adena);
		packetWriter.writeD(createList.size());
		for(ManufactureItem mi : createList)
		{
			packetWriter.writeD(mi.getRecipeId());
			packetWriter.writeD(0x00); // Can craft
			packetWriter.writeQ(mi.getCost());
			packetWriter.writeF(chanceBonus); // Craft chance bonus
			packetWriter.writeC(critChance > 0 && mi.canCrit()); // Critical craft available
			packetWriter.writeF(critChance); // Critical craft chance
		}
		return true;
	}
}