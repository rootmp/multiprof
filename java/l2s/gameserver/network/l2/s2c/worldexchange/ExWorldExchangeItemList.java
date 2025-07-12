package l2s.gameserver.network.l2.s2c.worldexchange;

import java.util.Collections;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.enums.WorldExchangeItemSubType;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.WorldExchangeHolder;

public class ExWorldExchangeItemList implements IClientOutgoingPacket
{
	public static final ExWorldExchangeItemList EMPTY_LIST = new ExWorldExchangeItemList(null, Collections.emptyList(), null);

	private final List<WorldExchangeHolder> _holders;
	private final WorldExchangeItemSubType _type;

	private Player _player;

	public ExWorldExchangeItemList(Player player, List<WorldExchangeHolder> holders, WorldExchangeItemSubType type)
	{
		_player = player;
		_holders = holders;
		_type = type;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if(_holders.isEmpty())
		{
			packetWriter.writeH(0); // Category
			packetWriter.writeC(0); // SortType
			packetWriter.writeD(0); // Page
			packetWriter.writeD(0); // ItemIDList
			return true;
		}

		packetWriter.writeH(_type.getId());
		packetWriter.writeC(0);
		packetWriter.writeD(0);
		packetWriter.writeD(_holders.size());
		for(WorldExchangeHolder holder : _holders)
		{
			getItemInfo(_player, packetWriter, holder);
		}
		return true;
	}

	private void getItemInfo(Player player, PacketWriter packetWriter, WorldExchangeHolder holder)
	{
		ItemInstance item = holder.getItemInstance();
		
		packetWriter.writeQ(holder.getWorldExchangeId());

		if(item.getItemId()==57)
			packetWriter.writeQ(holder.getPrice()*(item.getCount()/1000000));
		else
			packetWriter.writeQ(holder.getPrice()*item.getCount());
		
		packetWriter.writeD((int) (holder.getEndTime() / 1000L));

		packetWriter.writeD(item.getItemId());
		packetWriter.writeQ(item.getCount());
		packetWriter.writeD(item.getEnchantLevel() < 1 ? 0 : item.getEnchantLevel());
		packetWriter.writeD(item.getVariation1Id());
		packetWriter.writeD(item.getVariation2Id());
		packetWriter.writeD(item.getVariation3Id());
		
		packetWriter.writeD(-1);
		packetWriter.writeH(item.getAttackElement().getId());
		packetWriter.writeH(item.getAttackElementValue());
		packetWriter.writeH(item.getDefenceFire());
		packetWriter.writeH(item.getDefenceWater());
		packetWriter.writeH(item.getDefenceWind());
		packetWriter.writeH(item.getDefenceEarth());
		packetWriter.writeH(item.getDefenceHoly());
		packetWriter.writeH(item.getDefenceUnholy());
		packetWriter.writeD(item.getVisualId());

		packetWriter.writeD(item.getEnsoulOptionsArray()[0]);
		packetWriter.writeD(item.getEnsoulOptionsArray()[1]);
		packetWriter.writeD(item.getEnsoulSpecialOptionsArray()[0]);

		packetWriter.writeH(item.isBlessed());
		
		packetWriter.writeC(holder.getListingType());
		packetWriter.writeC(holder.getCurrencyType());
		packetWriter.writeC(holder.getOldOwnerId() == player.getObjectId());
	}
}
