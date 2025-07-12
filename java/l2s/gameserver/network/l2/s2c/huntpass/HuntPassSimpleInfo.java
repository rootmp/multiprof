package l2s.gameserver.network.l2.s2c.huntpass;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.HuntPass;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class HuntPassSimpleInfo implements IClientOutgoingPacket
{
	private final HuntPass _huntPassInfo;

	public HuntPassSimpleInfo(Player player)
	{
		_huntPassInfo = player.getHuntPass();
	}

	/*
	 * 
	struct _PkL2PassInfo
	{
	var int cPassType;
	var byte bIsOn;
	};
	
	var array<_PkL2PassInfo> passInfos;
	var byte bAvailableReward;
	var array<int> condIndex;
	 */
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(1); // nSize
		//for
		packetWriter.writeC(0); //cPassType
		packetWriter.writeC(1); // bIsOn

		packetWriter.writeC(_huntPassInfo.rewardAlert() ? 1 : 0); //bAvailableReward
		packetWriter.writeD(0);//nSize
		//for 
		//packetWriter.writeD(0); condIndex
		return true;
	}
}
