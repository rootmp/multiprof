package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.RecipeBookItemListPacket;

public class RequestRecipeBookOpen implements IClientIncomingPacket
{
	private boolean isDwarvenCraft;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		if (packet.hasRemaining())
			isDwarvenCraft = packet.readD() == 0;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		client.sendPacket(new RecipeBookItemListPacket(activeChar, isDwarvenCraft));
	}
}