package l2s.gameserver.network.l2.s2c.worldexchange;

import java.util.List;
import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.enums.WorldExchangeItemStatusType;
import l2s.gameserver.instancemanager.WorldExchangeManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.WorldExchangeHolder;

public class ExWorldExchangeSettleList implements IClientOutgoingPacket
{
	private final Player _player;

	public ExWorldExchangeSettleList(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		final Map<WorldExchangeItemStatusType, List<WorldExchangeHolder>> holders = WorldExchangeManager.getInstance().getPlayerBids(_player.getObjectId());
		if(holders.isEmpty())
		{
			packetWriter.writeD(0); // RegiItemDataList
			packetWriter.writeD(0); // RecvItemDataList
			packetWriter.writeD(0); // TimeOutItemDataList
			return true;
		}

		packetWriter.writeD(holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED).size());
		for(WorldExchangeHolder holder : holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED))
		{
			getItemInfo(packetWriter, holder);
		}

		packetWriter.writeD(holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD).size());
		for(WorldExchangeHolder holder : holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD))
		{
			getItemInfo(packetWriter, holder);
		}

		packetWriter.writeD(holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME).size());
		for(WorldExchangeHolder holder : holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME))
		{
			getItemInfo(packetWriter, holder);
		}

		packetWriter.writeD(holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_WAIT).size());
		for(WorldExchangeHolder holder : holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_WAIT))
		{
			getItemInfo(packetWriter, holder);
		}

		return true;
	}

	private void getItemInfo(PacketWriter packetWriter, WorldExchangeHolder holder)
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
		
		packetWriter.writeD(-1); // IntensiveItemClassID
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
		packetWriter.writeC(holder.getOldOwnerId() == _player.getObjectId());
	}
}
