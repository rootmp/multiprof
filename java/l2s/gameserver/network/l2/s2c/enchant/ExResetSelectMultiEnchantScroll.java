package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExResetSelectMultiEnchantScroll implements IClientOutgoingPacket
{
	private final int _scrollObjId, _resultType;

	public ExResetSelectMultiEnchantScroll(int scrollObjId, int resultType)
	{
		_scrollObjId = scrollObjId;
		_resultType = resultType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_scrollObjId);
		packetWriter.writeD(_resultType);
		return true;
	}
}