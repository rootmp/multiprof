package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;

public class RequestExNewHennaPotenEnchant extends L2GameClientPacket
{
	private int cSlotID;

	@Override
	protected boolean readImpl()
	{
		cSlotID = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		player.getHennaList().tryPotenEnchant(cSlotID);
	}
}