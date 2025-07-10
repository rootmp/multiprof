package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.clansearch.ClanSearchWaiterParams;
import l2s.gameserver.model.clansearch.base.ClanSearchPlayerRoleType;
import l2s.gameserver.model.clansearch.base.ClanSearchPlayerSortType;
import l2s.gameserver.model.clansearch.base.ClanSearchSortOrder;
import l2s.gameserver.network.l2.s2c.ExPledgeDraftListSearch;

/**
 * @author GodWorld
 * @reworked by Bonux
 **/
public class RequestPledgeDraftListSearch implements IClientIncomingPacket
{
	private int _minLevel;
	private int _maxLevel;
	private ClanSearchPlayerRoleType _role;
	private String _charName;
	private ClanSearchPlayerSortType _sortType;
	private ClanSearchSortOrder _sortOrder;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_minLevel = Math.max(0, Math.min(readD(), 99));
		_maxLevel = Math.max(0, Math.min(readD(), 99));
		_role = ClanSearchPlayerRoleType.valueOf(readD());

		_charName = readS().trim().toLowerCase();

		if (_charName.length() > 255)
			_charName = _charName.substring(0, 255);

		_sortType = ClanSearchPlayerSortType.valueOf(readD());
		_sortOrder = ClanSearchSortOrder.valueOf(readD());
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		// if(!((L2GameClient)getClient()).getFloodProtectors().getClanSearch().tryPerformAction(FloodAction.CLAN_BOARD_DRAFT_SEARCH))
		// return;

		activeChar.sendPacket(new ExPledgeDraftListSearch(new ClanSearchWaiterParams(_minLevel, _maxLevel, _role, _charName, _sortType, _sortOrder)));
	}
}