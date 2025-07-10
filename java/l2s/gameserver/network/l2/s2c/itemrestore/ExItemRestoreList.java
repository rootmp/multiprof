package l2s.gameserver.network.l2.s2c.itemrestore;

import java.util.List;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExItemRestoreList extends L2GameServerPacket
{
	private final int cCategory;
	private final List<PkItemRestoreNode> items;

	public ExItemRestoreList(int cCategory, List<PkItemRestoreNode> items)
	{
		this.cCategory = cCategory;
		this.items = items;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(cCategory);
		writeD(items.size());
		for (PkItemRestoreNode item : items)
		{
			writeD(item.nBrokenItemClassID);
			writeD(item.nFixedItemClassID);
			writeC(item.cEnchant);
			writeC(item.cOrder);
		}
	}

	public static class PkItemRestoreNode
	{
		public final int nBrokenItemClassID, nFixedItemClassID, cEnchant, cOrder;

		public PkItemRestoreNode(int nBrokenItemClassID, int nFixedItemClassID, int cEnchant, int cOrder)
		{
			this.nBrokenItemClassID = nBrokenItemClassID;
			this.nFixedItemClassID = nFixedItemClassID;
			this.cEnchant = cEnchant;
			this.cOrder = cOrder;
		}
	}
}