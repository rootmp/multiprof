package l2s.gameserver.network.l2.c2s.spectating;

import gnu.trove.map.TIntObjectMap;
import l2s.commons.network.PacketReader;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.dao.CharacterSpectatingListDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Spectating;
import l2s.gameserver.model.actor.instances.player.SpectatingList;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;

/**
 * @author nexvill
 */
public class RequestExUserWatcherAdd implements IClientIncomingPacket
{
	String _name;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_name = packet.readString();
		packet.readD(); // 0
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(activeChar.getSpectatingList().size() == SpectatingList.MAX_SPECTATING_LIST_SIZE)
		{
			activeChar.sendPacket(SystemMsg.MAXIMUM_NUMBER_OF_PEOPLE_ADDED_YOU_CANNOT_ADD_MORE);
			return;
		}

		TIntObjectMap<Spectating> player = CharacterSpectatingListDAO.getInstance().getCharDataByName(_name);
		int objId = CharacterDAO.getInstance().getObjectIdByName(_name);

		if(objId == 0)
		{
			activeChar.sendPacket(SystemMsg.THAT_CHARACTER_DOES_NOT_EXIST);
			return;
		}

		activeChar.getSpectatingList().add(objId, player);
	}
}
