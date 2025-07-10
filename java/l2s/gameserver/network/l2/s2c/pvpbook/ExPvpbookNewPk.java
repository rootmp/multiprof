package l2s.gameserver.network.l2.s2c.pvpbook;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExPvpbookNewPk extends L2GameServerPacket
{
	private final String killerName;

	public ExPvpbookNewPk(String killerName)
	{
		this.killerName = killerName;
	}

	@Override
	public void writeImpl()
	{
		writeString(killerName);
	}
}
