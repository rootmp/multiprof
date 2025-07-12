package l2s.gameserver.network.l2.s2c.spExtract;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExSpExtractInfo implements IClientOutgoingPacket
{
	private int _nItemID;
	private int _nRemainCount;

	public ExSpExtractInfo(Player player, int nItemID)
	{
		_nItemID = nItemID;
		_nRemainCount = 5 - player.getVarInt(PlayerVariables.SP_EXTRACT_ITEM_VAR, 0);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_nItemID);
		packetWriter.writeD(1);//nExtractCount
		packetWriter.writeQ(5000000000L);//nNeedSP
		packetWriter.writeD(100);//nRate
		packetWriter.writeD(0);//nCriticalRate

		//failedItem
		packetWriter.writeH(14);//nSize
		packetWriter.writeD(57);//nItemClassID
		packetWriter.writeQ(10000);//nAmount
		
		//commissionItem
		packetWriter.writeH(14);//nSize
		packetWriter.writeD(57);//nItemClassID
		packetWriter.writeQ(3000000);//nAmount
		
		//criticalItem	
		packetWriter.writeH(14);//nSize
		packetWriter.writeD(98232);//nItemClassID
		packetWriter.writeQ(0);//nAmount
		
		packetWriter.writeD(_nRemainCount);//nRemainCount
		packetWriter.writeD(5);//nMaxDailyCount
		return true;
	}
}