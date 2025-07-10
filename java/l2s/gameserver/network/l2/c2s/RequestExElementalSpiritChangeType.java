package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ElementalElement;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritInfo;

/**
 * @author Bonux
 **/
public class RequestExElementalSpiritChangeType implements IClientIncomingPacket
{
	private int _elementId;
	private int _unk1;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unk1 = packet.readC(); // TODO: Приходит 2.
		_elementId = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (!activeChar.changeActiveElement(ElementalElement.getElementById(_elementId)))
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.sendElementalInfo();
		activeChar.sendPacket(new ExElementalSpiritInfo(activeChar, 0));
	}
}