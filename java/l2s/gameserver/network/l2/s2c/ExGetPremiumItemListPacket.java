package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.PremiumItem;

/**
 * @modify Eden 286 "QdQdS"
 **/
public class ExGetPremiumItemListPacket extends L2GameServerPacket
{
	private final int _objectId;
	private final PremiumItem[] _list;

	public ExGetPremiumItemListPacket(Player activeChar)
	{
		_objectId = activeChar.getObjectId();
		_list = activeChar.getPremiumItemList().values();
	}

	@Override
	protected void writeImpl()
	{
		writeQ(_list.length);
		for (PremiumItem premiumItem : _list)
		{
			writeD(_objectId);
			writeQ(premiumItem.getItemCount());
			writeD(premiumItem.getItemId());
			writeS(premiumItem.getSender());
		}
	}

}