package l2s.gameserver.network.l2.c2s.relics;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExRelicsCombination implements IClientIncomingPacket
{
	private int nGrade;
	private List<Integer> stuffList;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nGrade = packet.readD();
		int stuffListSize = packet.readD();
		stuffList = new ArrayList<>(stuffListSize);
		for(int i = 0; i < stuffListSize; i++)
		{
			stuffList.add(packet.readD());
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		activeChar.getRelics().synthesizeRelics(nGrade, stuffList);
	}

}
