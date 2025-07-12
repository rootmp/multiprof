package l2s.gameserver.network.l2.c2s;

import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.RelicHolder;
import l2s.gameserver.data.xml.holder.RelicsCouponHolder;
import l2s.gameserver.data.xml.holder.RelicsSynthesisHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExRelicsProbList;
import l2s.gameserver.templates.relics.RelicsProb;

public class RequestExRelicsProbList implements IClientIncomingPacket
{
	private int Type;
	private int Key;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		Type = packet.readD();
		Key = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		//Type = 0 shop
		//Type = 1 synthesis
		//Type = 4 coupon
		List<RelicsProb> relicsProb = null;
		switch(Type)
		{
			case 4:
				relicsProb = RelicsCouponHolder.getInstance().getCouponProb(Key);
				break;
			case 1:
				relicsProb = RelicsSynthesisHolder.getInstance().getRelicsProb(Key);
				break;
			case 0:
				relicsProb = RelicHolder.getInstance().getSummonRelicsProb(Key);
				break;
		}
		if(relicsProb!=null)
			player.sendPacket(new ExRelicsProbList(Type, Key, relicsProb));
	}

}
