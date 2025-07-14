package l2s.gameserver.network.l2.c2s.dyee;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.dyee.ExDyeeffectEnchantNormalskill;

public class RequestExDyeeffectEnchantNormalskill implements IClientIncomingPacket
{
	private int nCategory;
	private int nSlotID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nCategory = packet.readD();
		nSlotID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		player.sendPacket(new ExDyeeffectEnchantNormalskill());
	}
}
