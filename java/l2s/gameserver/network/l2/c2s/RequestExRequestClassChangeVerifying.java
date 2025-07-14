package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.network.l2.GameClient;

public class RequestExRequestClassChangeVerifying implements IClientIncomingPacket
{
	private int _classId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_classId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		ClassId classId = activeChar.getClassId();
		if(classId.getId() != _classId)
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendClassChangeAlert();
	}
}
