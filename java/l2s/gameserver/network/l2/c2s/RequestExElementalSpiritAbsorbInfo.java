package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritAbsorbInfo;

/**
 * @author Bonux
 **/
public class RequestExElementalSpiritAbsorbInfo implements IClientIncomingPacket
{
	private int _unk, _elementId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unk = packet.readC();
		_elementId = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExElementalSpiritAbsorbInfo(activeChar, _unk, _elementId));
	}
}