package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritExtractInfo;

/**
 * @author Bonux
 **/
public class RequestExElementalSpiritExtractInfo extends L2GameClientPacket
{
	private int _elementId;

	@Override
	protected boolean readImpl()
	{
		_elementId = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExElementalSpiritExtractInfo(activeChar, _elementId));
	}
}