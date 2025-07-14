package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.CapsuleHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExCreateItemProbList;
import l2s.gameserver.templates.item.capsule.CapsuleData;

public class RequestExCreateItemProbList implements IClientIncomingPacket
{
	private int nClassID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nClassID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		CapsuleData _capsule = CapsuleHolder.getInstance().getCapsule(nClassID);
		if(_capsule != null)
			player.sendPacket(new ExCreateItemProbList(_capsule));
	}

}
