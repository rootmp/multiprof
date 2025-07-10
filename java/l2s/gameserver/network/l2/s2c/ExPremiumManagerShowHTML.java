package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExPremiumManagerShowHTML implements IClientOutgoingPacket
{
	private final CharSequence _html;
	private final boolean _isMain;

	public ExPremiumManagerShowHTML(boolean isMain, CharSequence html)
	{
		_isMain = isMain;
		_html = html;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0);
		packetWriter.writeS(_html);
		packetWriter.writeD(-1);
		packetWriter.writeD(_isMain ? 0 : 1);
	}
}