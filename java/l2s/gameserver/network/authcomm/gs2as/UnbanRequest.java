package l2s.gameserver.network.authcomm.gs2as;

import l2s.commons.ban.BanBindType;
import l2s.gameserver.network.authcomm.SendablePacket;

public class UnbanRequest extends SendablePacket
{
	private final BanBindType bindType;
	private final String bindValue;

	public UnbanRequest(BanBindType bindType, String bindValue)
	{
		this.bindType = bindType;
		this.bindValue = bindValue;
	}

	protected void writeImpl()
	{
		writeC(0x14);
		writeC(bindType.ordinal());
		writeS(bindValue);
	}
}
