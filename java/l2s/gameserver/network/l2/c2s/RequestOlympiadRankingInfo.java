package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExOlympiadRankingInfo;

/**
 * @author NviX
 */
public class RequestOlympiadRankingInfo implements IClientIncomingPacket
{
	private int _tabId;
	private int _rankingType;
	private int _unk;
	private int _classId;
	private int _serverId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_tabId = packet.readC();
		_rankingType = packet.readC();
		_unk = packet.readC();
		_classId = packet.readD();
		_serverId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		activeChar.sendPacket(new ExOlympiadRankingInfo(activeChar, _tabId, _rankingType, _unk, _classId, _serverId));
	}
}