package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.StartRotatingPacket;

/**
 * packet type id 0x5b format: cdd
 */
public class StartRotatingC implements IClientIncomingPacket
{
	private int _degree;
	private int _side;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_degree = packet.readD();
		_side = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		activeChar.setHeading(_degree);
		activeChar.broadcastPacket(new StartRotatingPacket(activeChar, _degree, _side, 0));
	}
}