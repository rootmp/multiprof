package l2s.gameserver.network.l2.c2s.randomcraft;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftExtract;

/**
 * @author nexvill
 */
public class RequestExCraftExtract implements IClientIncomingPacket
{
	private int _count;
	private int[] _objectId = new int[128];
	private int[] _itemCount = new int[128];

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_count = packet.readD();
		for (int i = 0; i < _count; i++)
		{
			int id = packet.readD();
			int cnt = packet.readD();
			_objectId[i] = id;
			_itemCount[i] = cnt;
			packet.readD(); // 0
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExCraftExtract(activeChar, _count, _objectId, _itemCount));
	}
}