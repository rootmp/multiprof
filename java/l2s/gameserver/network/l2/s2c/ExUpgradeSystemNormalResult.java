package l2s.gameserver.network.l2.s2c;

import java.util.Collection;
import java.util.Collections;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.items.ItemInfo;

public class ExUpgradeSystemNormalResult implements IClientOutgoingPacket
{
	public static final ExUpgradeSystemNormalResult FAIL = new ExUpgradeSystemNormalResult();

	private final int upgradeId;
	private final int result;
	private final boolean success;
	private final Collection<ItemInfo> items;

	public ExUpgradeSystemNormalResult(int upgradeId, boolean success, Collection<ItemInfo> items)
	{
		this.upgradeId = upgradeId;
		this.result = 1;
		this.success = success;
		this.items = items;
	}

	protected ExUpgradeSystemNormalResult()
	{
		this.upgradeId = 0;
		this.result = 0;
		this.success = false;
		this.items = Collections.emptyList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(result); // Result
		packetWriter.writeD(upgradeId); // ResultID
		packetWriter.writeC(success); // IsSuccess
		int unk = 0;
		packetWriter.writeD(unk); // Count
		if (unk <= 0)
		{
			packetWriter.writeC(0); // IsBonus
			packetWriter.writeD(items.size());
			for (ItemInfo item : items)
			{
				packetWriter.writeD(item.getObjectId()); // ItemServerID
				packetWriter.writeD(item.getItemId()); // ItemClassID
				packetWriter.writeD(item.getEnchantLevel()); // ItemEnchant
				packetWriter.writeD((int) item.getCount()); // ItemCount
			}
		}
		else
		{
			for (ItemInfo item : items)
			{
				packetWriter.writeD(item.getObjectId()); // ItemServerID
				packetWriter.writeD(item.getItemId()); // ItemClassID
				packetWriter.writeD(item.getEnchantLevel()); // ItemEnchant
				packetWriter.writeD((int) item.getCount()); // ItemCount
			}
		}
		return true;
	}
}