package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;

public class RequestMenteeAdd extends L2GameClientPacket
{
	private String _newMentee;

	@Override
	protected boolean readImpl()
	{
		_newMentee = readS();
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		activeChar.updateMenteeStatus();
		activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_HAVE_OFFERED_TO_BECOME_S1_MENTOR).addString(_newMentee));
	}
}