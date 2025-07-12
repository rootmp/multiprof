package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.network.l2.GameClient;

public class RequestSetAllyCrest implements IClientIncomingPacket
{
	private int _length;
	private byte[] _data;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_length = packet.readD();
		if (_length == CrestCache.ALLY_CREST_SIZE && _length == packet.getReadableBytes())
		{
			_data = new byte[_length];
			readB(_data);
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Alliance ally = activeChar.getAlliance();
		if (ally != null && activeChar.isAllyLeader())
		{
			int crestId = 0;

			if (_data != null)
				crestId = CrestCache.getInstance().saveAllyCrest(ally.getAllyId(), _data);
			else if (ally.hasAllyCrest())
				CrestCache.getInstance().removeAllyCrest(ally.getAllyId());

			ally.setAllyCrestId(crestId);
			ally.broadcastAllyStatus();
		}
	}
}