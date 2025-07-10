package l2s.gameserver.network.l2.c2s.pets;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;

/**
 * @author nexvill
 */
public class RequestExTryPetExtractSystem extends L2GameClientPacket
{
	private int _petItemId;

	@Override
	protected boolean readImpl()
	{
		_petItemId = readD();
		System.out.print("received RequestExTryPetExtractSystem with data: " + _petItemId);
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
	}
}