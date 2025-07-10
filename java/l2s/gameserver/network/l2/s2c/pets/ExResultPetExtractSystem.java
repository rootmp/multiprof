package l2s.gameserver.network.l2.s2c.pets;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExResultPetExtractSystem extends L2GameServerPacket
{

	public ExResultPetExtractSystem()
	{

	}

	@Override
	protected final void writeImpl()
	{
		writeC(0);
	}
}