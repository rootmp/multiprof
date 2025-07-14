package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExIsCharNameCreatable;
import l2s.gameserver.utils.Util;

public class RequestCharacterNameCreatable implements IClientIncomingPacket
{
	private String _charname;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_charname = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if(Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT != 0
				&& CharacterDAO.getInstance().accountCharNumber(client.getLogin()) >= Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT)
		{
			client.sendPacket(ExIsCharNameCreatable.TOO_MANY_CHARACTERS);
			return;
		}

		if(_charname == null || _charname.isEmpty())
		{
			client.sendPacket(ExIsCharNameCreatable.ENTER_CHAR_NAME__MAX_16_CHARS);
			return;
		}

		if(!Util.isMatchingRegexp(_charname, Config.CNAME_TEMPLATE))
		{
			client.sendPacket(ExIsCharNameCreatable.WRONG_NAME);
			return;
		}

		if(CharacterDAO.getInstance().getObjectIdByName(_charname) > 0)
		{
			client.sendPacket(ExIsCharNameCreatable.NAME_ALREADY_EXISTS);
			return;
		}
		client.sendPacket(ExIsCharNameCreatable.SUCCESS);
	}
}
