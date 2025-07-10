package l2s.gameserver.network.l2.c2s.spectating;

import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.dao.CharacterSpectatingListDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Spectating;
import l2s.gameserver.model.actor.instances.player.SpectatingList;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;

import gnu.trove.map.TIntObjectMap;

/**
 * @author nexvill
 */
public class RequestExUserWatcherAdd extends L2GameClientPacket
{
	String _name;

	@Override
	protected boolean readImpl()
	{
		_name = readString();
		readD(); // 0
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.getSpectatingList().size() == SpectatingList.MAX_SPECTATING_LIST_SIZE)
		{
			activeChar.sendPacket(SystemMsg.MAXIMUM_NUMBER_OF_PEOPLE_ADDED_YOU_CANNOT_ADD_MORE);
			return;
		}

		TIntObjectMap<Spectating> player = CharacterSpectatingListDAO.getInstance().getCharDataByName(_name);
		int objId = CharacterDAO.getInstance().getObjectIdByName(_name);

		if (objId == 0)
		{
			activeChar.sendPacket(SystemMsg.THAT_CHARACTER_DOES_NOT_EXIST);
			return;
		}

		activeChar.getSpectatingList().add(objId, player);
	}
}
