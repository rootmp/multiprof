package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.GlobalEventUiHolder;
import l2s.gameserver.data.xml.holder.MultiSellHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestExSelectGlobalEventUi implements IClientIncomingPacket
{
	private int nEventIndex;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nEventIndex = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		int multisell = GlobalEventUiHolder.getInstance().getEvent(nEventIndex);
		if(multisell!=0)
		{
			MultiSellHolder.getInstance().SeparateAndSend(multisell, player, 0);
		}
	}
}