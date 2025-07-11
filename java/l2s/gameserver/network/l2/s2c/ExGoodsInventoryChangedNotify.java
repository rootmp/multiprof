package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 * @date 11:33/03.07.2011
 */
public class ExGoodsInventoryChangedNotify implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExGoodsInventoryChangedNotify();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}
