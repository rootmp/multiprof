package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.RandomCraftListHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExCraftSlotProbList;

public class RequestExCraftSlotProbList implements IClientIncomingPacket
{
	private int nSlot;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nSlot = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;

		player.sendPacket(new ExCraftSlotProbList(nSlot, RandomCraftListHolder.getInstance().getRewardData(nSlot)));
	}

}
