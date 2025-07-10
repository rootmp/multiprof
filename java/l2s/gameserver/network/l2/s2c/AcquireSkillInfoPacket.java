package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.model.base.AcquireType;
import l2s.gameserver.templates.item.data.ItemData;

/**
 * Acquire Skill Info server packet implementation.
 * 
 * @author Zoey76
 */
public class AcquireSkillInfoPacket extends L2GameServerPacket
{
	private SkillLearn _learn;
	private AcquireType _type;
	private List<Require> _reqs = Collections.emptyList();

	/**
	 * Private class containing learning skill requisites.
	 */
	private static class Require
	{
		public int itemId;
		public long count;
		public int type;
		public int unk;

		/**
		 * @param pType     TODO identify.
		 * @param pItemId   the item Id.
		 * @param itemCount the item count.
		 * @param pUnk      TODO identify.
		 */
		public Require(int pType, int pItemId, long pCount, int pUnk)
		{
			itemId = pItemId;
			type = pType;
			count = pCount;
			unk = pUnk;
		}
	}

	public AcquireSkillInfoPacket(AcquireType type, SkillLearn learn)
	{
		_type = type;
		_learn = learn;
		_reqs = new ArrayList<Require>();
		for (ItemData item : _learn.getRequiredItemsForLearn(type))
		{
			_reqs.add(new Require(99, item.getId(), item.getCount(), 50));
		}
	}

	@Override
	public void writeImpl()
	{
		writeD(_learn.getId());
		writeD(_learn.getLevel());
		writeQ(_learn.getCost()); // sp/rep
		writeD(_type.getId());
		writeD(_reqs.size()); // requires size
		for (Require temp : _reqs)
		{
			writeD(temp.type);
			writeD(temp.itemId);
			writeQ(temp.count);
			writeQ(temp.unk); // paperdoll slot for this item
		}
	}
}