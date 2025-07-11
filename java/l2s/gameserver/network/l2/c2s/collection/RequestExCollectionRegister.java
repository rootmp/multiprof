package l2s.gameserver.network.l2.c2s.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionInfo;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionRegister;

/**
 * @author nexvill
 */
public class RequestExCollectionRegister implements IClientIncomingPacket
{
	private int _collectionId, _slotId, _objectId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_collectionId = packet.readH();
		_slotId = packet.readD();
		_objectId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		if (item == null)
			return;

		player.sendPacket(new ExCollectionRegister(player, _collectionId, _slotId, item));
		for (int i = 1; i < 8; i++)
		{
			player.sendPacket(new ExCollectionInfo(player, i));
		}
	}
}