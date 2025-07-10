package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * Даный пакет показывает менюшку для ввода серийника. Можно что-то придумать :)
 * Format: (ch)
 */
public class ShowPCCafeCouponShowUI implements IClientOutgoingPacket
{
	public static final ShowPCCafeCouponShowUI STATIC = new ShowPCCafeCouponShowUI();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
	}
}