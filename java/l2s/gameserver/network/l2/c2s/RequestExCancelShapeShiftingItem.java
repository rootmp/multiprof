package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExShape_Shifting_Result;

/**
 * @author Bonux
 **/
public class RequestExCancelShapeShiftingItem implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		player.setAppearanceStone(null);
		player.setAppearanceExtractItem(null);
		player.sendPacket(ExShape_Shifting_Result.FAIL);
	}
}