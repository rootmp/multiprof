package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

/**
 * @author Bonux
**/
public class ExSendClientINI implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int _iniType; // 0 - Unknown, 1 - Option, 2 - ChatFilter, 3 - WindowsInfo
	@SuppressWarnings("unused")
	private byte[] _content;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_iniType = packet.readC(); // Part number
		int length = packet.readH(); // Part size
		_content = packet.readB(length); // Content
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		//System.out.println("ExSendClientINI: _iniType=" + _iniType + ", _content=" + new String(_content));
	}
}