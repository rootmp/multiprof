package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExEnchantChallengePointInfo extends L2GameServerPacket
{
	public ExEnchantChallengePointInfo()
	{
		//
	}

	@Override
	protected final void writeImpl()
	{
		int count = 1;
		writeD(count);
		for (int i = 0; i < count; i++)
		{
			writeD(0); // points group id
			writeD(0); // challenge point
			writeD(0); // ticket point opt1
			writeD(0); // ticket point opt2
			writeD(0); // ticket point opt3
			writeD(0); // ticket point opt4
			writeD(0); // ticket point opt5
			writeD(0); // ticket point opt6
		}

	}
}