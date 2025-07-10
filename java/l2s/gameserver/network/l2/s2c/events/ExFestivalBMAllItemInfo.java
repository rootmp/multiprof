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
public class ExFestivalBMAllItemInfo extends L2GameServerPacket
{
	private Map<Integer, FestivalBMTemplate> _items = new HashMap<>();

	public ExFestivalBMAllItemInfo()
	{
		_items = FestivalBMHolder.getInstance().getItems();
	}

	@Override
	protected final void writeImpl()
	{
		writeD((int) (System.currentTimeMillis() / 1000));
		writeD(_items.size());

		if (_items.size() > 0)
		{
			for (int id : _items.keySet())
			{
				final FestivalBMTemplate item = _items.get(id);

				writeC(item.getLocationId());
				writeD(item.getItemId());

				int existingAmount = ServerVariables.getInt("FESTIVAL_BM_" + item.getItemId(), item.getItemCount());

				writeD(existingAmount);
				writeD(item.getItemCount());
			}
		}
	}
}