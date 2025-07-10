package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class RequestUpdateFriendMemo extends L2GameClientPacket
{
	private String _name;
	private String _memo;

	@Override
	protected boolean readImpl()
	{
		_name = readS();
		_memo = readS();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.getFriendList().updateMemo(_name, _memo);
	}
}