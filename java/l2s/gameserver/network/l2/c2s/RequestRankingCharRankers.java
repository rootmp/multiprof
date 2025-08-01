package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExRankingCharBuffzoneNpcInfo;
import l2s.gameserver.network.l2.s2c.ExRankingCharBuffzoneNpcPosition;
import l2s.gameserver.network.l2.s2c.ExRankingCharRankers;

/**
 * @author JoeAlisson
 */
public class RequestRankingCharRankers implements IClientIncomingPacket
{
	private int cRankingGroup;
	private int cRankingScope;
	private int nRace;
	private int nClass;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cRankingGroup = packet.readC(); // Tab Id
		cRankingScope = packet.readC(); // All or personal
		nRace = packet.readD();
		nClass = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		activeChar.sendPacket(new ExRankingCharRankers(activeChar, cRankingGroup, cRankingScope, nRace, nClass));

		boolean activeNpc = ServerVariables.getBool("buffNpcActive", false);
		if(activeNpc)
		{
			int x = ServerVariables.getInt("buffNpcX", -50000);
			int y = ServerVariables.getInt("buffNpcY", -50000);
			int z = ServerVariables.getInt("buffNpcZ", -50000);
			if((x != -50000) && (y != -50000) && (z != -50000))
				activeChar.sendPacket(new ExRankingCharBuffzoneNpcPosition((byte) 1, x, y, z));
			else
			{
				ServerVariables.unset("buffNpcActive");
				ServerVariables.unset("buffNpcX");
				ServerVariables.unset("buffNpcY");
				ServerVariables.unset("buffNpcZ");
				activeChar.sendPacket(new ExRankingCharBuffzoneNpcPosition((byte) 0, 0, 0, 0));
			}

		}
		else
			activeChar.sendPacket(new ExRankingCharBuffzoneNpcPosition((byte) 0, 0, 0, 0));

		if(RankManager.getInstance().getPlayerGlobalRank(activeChar) == 1)
			activeChar.sendPacket(new ExRankingCharBuffzoneNpcInfo());
	}
}