package l2s.gameserver.network.l2.s2c.adenlab;

import java.util.Map;
import java.util.Map.Entry;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabBossInfo implements IClientOutgoingPacket
{
	private int nBossID;
	private int nCurrentSlot;
	private int nTranscendEnchant;
	private int nNormalGameSaleDailyCount;
	private int nNormalGameDailyCount;
	private Map<Integer, int[]> specialSlotInfo;

	public ExAdenlabBossInfo(int nBossID, int nCurrentSlot, int nTranscendEnchant, int nNormalGameSaleDailyCount, int nNormalGameDailyCount, Map<Integer, int[]> specialSlotInfo)
	{
		this.nBossID = nBossID;
		this.nCurrentSlot = nCurrentSlot;
		this.nTranscendEnchant = nTranscendEnchant;
		this.nNormalGameSaleDailyCount = nNormalGameSaleDailyCount;
		this.nNormalGameDailyCount = nNormalGameDailyCount;
		this.specialSlotInfo = specialSlotInfo;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeD(nCurrentSlot);
		packetWriter.writeD(nTranscendEnchant);
		packetWriter.writeD(nNormalGameSaleDailyCount);
		packetWriter.writeD(nNormalGameDailyCount);

		packetWriter.writeD(specialSlotInfo.size());
		for(Entry<Integer, int[]> slot : specialSlotInfo.entrySet())
		{
			packetWriter.writeD(slot.getKey());
			packetWriter.writeD(slot.getValue().length);
			for(int grade : slot.getValue())
			{
				packetWriter.writeD(grade);
			}
		}
		return true;
	}
}