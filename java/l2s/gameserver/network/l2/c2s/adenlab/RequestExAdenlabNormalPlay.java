package l2s.gameserver.network.l2.c2s.adenlab;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExAdenlabNormalPlay implements IClientIncomingPacket
{
	private int nBossID;
	private int nSlotID;
	private int nFeeIndex;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nBossID = packet.readD();
		nSlotID = packet.readD();
		nFeeIndex = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		
		if(nFeeIndex < 0 || nFeeIndex > 1)
			return;

		activeChar.getAdenLab().normalPlay(nBossID, nSlotID, nFeeIndex);
	}
}
