package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExPvpRankingList;

/**
 * @author nexvill
 */
public class RequestExPvpRankingList implements IClientIncomingPacket
{
	int _season, _tabId, _type, _race;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_season = packet.readC();
		_tabId = packet.readC();
		_type = packet.readC();
		_race = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		activeChar.sendPacket(new ExPvpRankingList(activeChar, _season, _tabId, _type, _race));
	}
}