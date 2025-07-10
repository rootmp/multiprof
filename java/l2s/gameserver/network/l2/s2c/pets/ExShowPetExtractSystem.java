package l2s.gameserver.network.l2.s2c.pets;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExShowPetExtractSystem extends L2GameServerPacket
{

	public ExShowPetExtractSystem()
	{

	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xFE); // dummy
	}
}