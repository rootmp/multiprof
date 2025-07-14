package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.model.base.AcquireType;
import l2s.gameserver.templates.item.data.ItemData;

/**
 * Acquire Skill Info server packet implementation.
 * 
 * @author Zoey76
 */
public class AcquireSkillInfoPacket implements IClientOutgoingPacket
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
		for(ItemData item : _learn.getRequiredItemsForLearn(type))
		{
			_reqs.add(new Require(99, item.getId(), item.getCount(), 50));
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_learn.getId());
		packetWriter.writeD(_learn.getLevel());
		packetWriter.writeQ(_learn.getCost()); // sp/rep
		packetWriter.writeD(_type.getId());
		packetWriter.writeD(_reqs.size()); // requires size
		for(Require temp : _reqs)
		{
			packetWriter.writeD(temp.type);
			packetWriter.writeD(temp.itemId);
			packetWriter.writeQ(temp.count);
			packetWriter.writeQ(temp.unk); // paperdoll slot for this item
		}
		return true;
	}
}