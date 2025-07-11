package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExUISettingPacket;

/**
 * format: (ch)db
 */
public class RequestSaveKeyMapping implements IClientIncomingPacket
{
	private byte[] _data;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		int length = packet.readD();
		if (length > _buf.remaining() || length > Short.MAX_VALUE || length < 0)
		{
			_data = null;
			return false;
		}
		_data = new byte[length];
		readB(_data);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null || _data == null)
			return;
		activeChar.setKeyBindings(_data);
		activeChar.sendPacket(new ExUISettingPacket(activeChar));
	}
}