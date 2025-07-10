package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;

public class RequestMenteeWaitingList extends L2GameClientPacket
{
	private int maxLevel;
	private int minLevel;
	private int page;

	@Override
	protected boolean readImpl() throws Exception
	{
		this.page = readD();
		this.minLevel = readD();
		this.maxLevel = readD();
		return true;
	}

	@Override
	protected void runImpl() throws Exception
	{
		final Player player = getClient().getActiveChar();
		if ((player == null) || maxLevel == 0 || minLevel == 0 || page == 0)
			return;
		player.sendMenteeList();
		player.sendUserInfo(true);
	}
}