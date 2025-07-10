package l2s.gameserver.network.l2.s2c;

public class ExOlympiadMatchInfo extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeS("Team 1");
		writeD(100);
		writeS("Team 2");
		writeD(100);
		writeD(1);
		writeD(3600);
	}
}