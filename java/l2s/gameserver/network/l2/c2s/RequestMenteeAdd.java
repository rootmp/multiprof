package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;

public class RequestMenteeAdd implements IClientIncomingPacket
{
	private String _newMentee;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_newMentee = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		activeChar.updateMenteeStatus();
		activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_HAVE_OFFERED_TO_BECOME_S1_MENTOR).addString(_newMentee));
	}
}