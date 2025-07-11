package l2s.gameserver.network.l2.s2c.itemrestore;

import java.util.List;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExItemRestoreList implements IClientOutgoingPacket
{
	private final int cCategory;
	private final List<PkItemRestoreNode> items;

	public ExItemRestoreList(int cCategory, List<PkItemRestoreNode> items)
	{
		this.cCategory = cCategory;
		this.items = items;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cCategory);
		packetWriter.writeD(items.size());
		for (PkItemRestoreNode item : items)
		{
			packetWriter.writeD(item.nBrokenItemClassID);
			packetWriter.writeD(item.nFixedItemClassID);
			packetWriter.writeC(item.cEnchant);
			packetWriter.writeC(item.cOrder);
		}
		return true;
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