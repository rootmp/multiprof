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
public class ExFestivalBMTopItemInfo extends L2GameServerPacket
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
	protected final void writeImpl()
	{
		writeC(_active); // is active
		writeD((int) (System.currentTimeMillis() / 1000)); // current time
		writeD(_timeToEnd); // end time
		writeD(_items.size()); // 0 causes items none

		if (_items.size() > 0)
		{
			for (int id : _items.keySet())
			{
				final FestivalBMTemplate item = _items.get(id);
				int existingAmount = ServerVariables.getInt("FESTIVAL_BM_" + item.getItemId(), item.getItemCount());
				writeC(item.getLocationId()); // grade
				writeD(item.getItemId());
				writeD(existingAmount);
				writeD(item.getItemCount());
			}
		}
	}
}