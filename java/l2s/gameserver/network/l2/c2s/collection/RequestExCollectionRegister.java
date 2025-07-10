package l2s.gameserver.network.l2.c2s.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionInfo;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionRegister;

/**
 * @author nexvill
 */
public class RequestExCollectionRegister extends L2GameClientPacket
{
	private int _collectionId, _slotId, _objectId;

	@Override
	protected boolean readImpl()
	{
		_collectionId = readH();
		_slotId = readD();
		_objectId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
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