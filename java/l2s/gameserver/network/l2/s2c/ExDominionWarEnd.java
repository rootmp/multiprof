package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 * @date 12:11/05.03.2011
 */
public class ExDominionWarEnd implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExDominionWarEnd();

	@Override
	public void writeImpl()
	{
	}
}
