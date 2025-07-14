package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.PremiumItem;

/**
 * @modify Eden 286 "QdQdS"
 **/
public class ExGetPremiumItemListPacket implements IClientOutgoingPacket
{
	private final int _objectId;
	private final PremiumItem[] _list;

	public ExGetPremiumItemListPacket(Player activeChar)
	{
		_objectId = activeChar.getObjectId();
		_list = activeChar.getPremiumItemList().values();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_list.length);
		for(PremiumItem premiumItem : _list)
		{
			packetWriter.writeD(_objectId);
			packetWriter.writeQ(premiumItem.getItemCount());
			packetWriter.writeD(premiumItem.getItemId());
			packetWriter.writeS(premiumItem.getSender());
		}
		return true;
	}

}