package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassLevel;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritInfo;

public class RequestExElementalSpiritInfo implements IClientIncomingPacket
{
	private int _unk;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unk = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (!Config.ELEMENTAL_SYSTEM_ENABLED)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.getClassLevel().ordinal() < ClassLevel.SECOND.ordinal())
		{
			activeChar.sendPacket(SystemMsg.UNABLE_TO_OPEN_ATTRIBUTE_AFTER_THE_THIRD_CLASS_CHANGE);
			return;
		}

		activeChar.sendPacket(new ExElementalSpiritInfo(activeChar, _unk));
	}
}