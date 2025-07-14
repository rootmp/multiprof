package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.dataparser.data.holder.UpgradeSystemNormalHolder;
import l2s.dataparser.data.holder.upgrade_system.NormalUpgradeData;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExUpgradeSystemProbList;

public class RequestExUpgradeSystemProbList implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int nType;
	private int nUpgradeID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nType = packet.readD();
		nUpgradeID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		NormalUpgradeData upgradeData = UpgradeSystemNormalHolder.getInstance().getNormalUpgradeData(nUpgradeID);
		if(upgradeData == null || upgradeData.success_result_items.isEmpty())
			return;

		player.sendPacket(new ExUpgradeSystemProbList(upgradeData));
	}

}
