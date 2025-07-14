package l2s.gameserver.network.l2.s2c.events;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.FestivalBMHolder;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.FestivalBMTemplate;

/**
 * @author nexvill
 */
public class ExFestivalBMAllItemInfo implements IClientOutgoingPacket
{
	private Map<Integer, FestivalBMTemplate> _items = new HashMap<>();

	public ExFestivalBMAllItemInfo()
	{
		_items = FestivalBMHolder.getInstance().getItems();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD((int) (System.currentTimeMillis() / 1000));
		packetWriter.writeD(_items.size());

		if(_items.size() > 0)
		{
			for(int id : _items.keySet())
			{
				final FestivalBMTemplate item = _items.get(id);

				packetWriter.writeC(item.getLocationId());
				packetWriter.writeD(item.getItemId());

				int existingAmount = ServerVariables.getInt("FESTIVAL_BM_" + item.getItemId(), item.getItemCount());

				packetWriter.writeD(existingAmount);
				packetWriter.writeD(item.getItemCount());
			}
		}
		return true;
	}
}