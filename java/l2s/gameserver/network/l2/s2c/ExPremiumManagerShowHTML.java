package l2s.gameserver.network.l2.s2c;

/**
 * @author nexvill
 */
public class ExPremiumManagerShowHTML extends L2GameServerPacket
{
	private final CharSequence _html;
	private final boolean _isMain;

	public ExPremiumManagerShowHTML(boolean isMain, CharSequence html)
	{
		_isMain = isMain;
		_html = html;
	}

	@Override
	protected void writeImpl()
	{
		writeD(0);
		writeS(_html);
		writeD(-1);
		writeD(_isMain ? 0 : 1);
	}
}