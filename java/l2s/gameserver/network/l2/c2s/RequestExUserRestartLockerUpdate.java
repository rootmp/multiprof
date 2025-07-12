package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExUserRestartLockerUpdate;

public class RequestExUserRestartLockerUpdate implements IClientIncomingPacket
{
	private int _nRestartPoint;
	@SuppressWarnings("unused")
	private int _nClassID;
	private int _bLocked;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_nRestartPoint = packet.readD();
		_nClassID = packet.readD();
		_bLocked = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		player.getRestartPointSetting().changeButtonStatus(_nRestartPoint, _bLocked);
		player.sendPacket(new ExUserRestartLockerUpdate());
	}
}