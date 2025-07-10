package l2s.gameserver.network.authcomm.as2gs;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.ban.BanBindType;
import l2s.commons.ban.BanInfo;
import l2s.gameserver.instancemanager.AuthBanManager;
import l2s.gameserver.instancemanager.GameBanManager;
import l2s.gameserver.network.authcomm.ReceivablePacket;

public class CheckBans extends ReceivablePacket
{
	private BanBindType bindType;
	private Map<String, BanInfo> bans;

	@Override
	public boolean readImpl()
	{
		try
		{
			bindType = BanBindType.VALUES[readC()];
		}
		catch (Exception e)
		{
			return false;
		}
		int size = readH();
		bans = new HashMap<>(size);
		for (int i = 0; i < size; i++)
		{
			String bindValue = readS();
			int endTime = readD();
			String reason = readS();
			bans.put(bindValue, new BanInfo(endTime, reason));
		}
		return true;
	}

	@Override
	protected void runImpl()
	{
		if (!bindType.isAuth())
			return;

		AuthBanManager.getInstance().getCachedBans().put(bindType, bans);

		for (Map.Entry<String, BanInfo> entry : bans.entrySet())
		{
			GameBanManager.onBan(bindType, entry.getKey(), entry.getValue(), true);
		}
	}
}
