package l2s.authserver.network.gamecomm.as2gs;

import java.util.Map;

import l2s.authserver.network.gamecomm.SendablePacket;
import l2s.commons.ban.BanBindType;
import l2s.commons.ban.BanInfo;

/**
 * @author Bonux (Head Developer L2-scripts.com) 10.04.2019 Developed for
 *         L2-Scripts.com
 **/
public class CheckBans extends SendablePacket
{
	private final BanBindType bindType;
	private final Map<String, BanInfo> bans;

	public CheckBans(BanBindType bindType, Map<String, BanInfo> bans)
	{
		this.bindType = bindType;
		this.bans = bans;
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x07);
		writeC(bindType.ordinal());
		writeH(bans.size());
		for(Map.Entry<String, BanInfo> entry : bans.entrySet())
		{
			writeS(entry.getKey());
			writeD(entry.getValue().getEndTime());
			writeS(entry.getValue().getReason());
		}
	}
}