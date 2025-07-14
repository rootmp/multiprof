package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.dataparser.data.holder.upgrade_system.NormalUpgradeData;

public class ExUpgradeSystemProbList implements IClientOutgoingPacket
{
	private NormalUpgradeData upgradeData;

	public ExUpgradeSystemProbList(NormalUpgradeData upgradeData)
	{
		this.upgradeData = upgradeData;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(upgradeData.getType().ordinal());
		packetWriter.writeD(upgradeData.upgrade_id);
		packetWriter.writeD((int) upgradeData.getSuccessProb());
		packetWriter.writeD((int) upgradeData.getBonusProb());
		return true;
	}
}