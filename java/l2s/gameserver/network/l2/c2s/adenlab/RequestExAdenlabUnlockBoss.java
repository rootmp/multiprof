package l2s.gameserver.network.l2.c2s.adenlab;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExAdenlabUnlockBoss implements IClientIncomingPacket
{
	private int nBossID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nBossID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		activeChar.getAdenLab().unlockBoss(nBossID);
	}
}
