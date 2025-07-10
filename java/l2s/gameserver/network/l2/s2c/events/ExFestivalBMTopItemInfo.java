package l2s.gameserver.network.l2.s2c.events;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.data.xml.holder.FestivalBMHolder;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.FestivalBMTemplate;

/**
 * @author nexvill
 */
public class ExFestivalBMTopItemInfo implements IClientOutgoingPacket
{
	private final int _timeToEnd;
	private boolean _active;
	private Map<Integer, FestivalBMTemplate> _items = new HashMap<>();

	public ExFestivalBMTopItemInfo(boolean active, int timeToEnd)
	{
		_active = active;
		_timeToEnd = timeToEnd;
		_items = FestivalBMHolder.getInstance().getItems();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_active); // is active
		packetWriter.writeD((int) (System.currentTimeMillis() / 1000)); // current time
		packetWriter.writeD(_timeToEnd); // end time
		packetWriter.writeD(_items.size()); // 0 causes items none

		if (_items.size() > 0)
		{
			for (int id : _items.keySet())
			{
				final FestivalBMTemplate item = _items.get(id);
				int existingAmount = ServerVariables.getInt("FESTIVAL_BM_" + item.getItemId(), item.getItemCount());
				packetWriter.writeC(item.getLocationId()); // grade
				packetWriter.writeD(item.getItemId());
				packetWriter.writeD(existingAmount);
				packetWriter.writeD(item.getItemCount());
			}
		}
	}
}