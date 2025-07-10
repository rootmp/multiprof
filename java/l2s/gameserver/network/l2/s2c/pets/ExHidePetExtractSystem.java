package l2s.gameserver.network.l2.s2c.pets;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExHidePetExtractSystem extends L2GameServerPacket
{

	public ExHidePetExtractSystem()
	{

	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xFE); // dummy
	}
}