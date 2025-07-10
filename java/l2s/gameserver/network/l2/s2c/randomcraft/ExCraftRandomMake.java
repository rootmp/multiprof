package l2s.gameserver.network.l2.s2c.randomcraft;

import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExItemAnnounce;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.RandomCraftInfo;
import l2s.gameserver.templates.RandomCraftItem;

/**
 * @author nexvill
 */
public class ExCraftRandomMake implements IClientOutgoingPacket
{
	private Player _player;

	public ExCraftRandomMake(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// select random item
		int _slot = Rnd.get(5);
		RandomCraftInfo data = _player.getRandomCraftList().get(_slot);

		int resultId = data.getResultId() == 0 ? data.getId() : data.getResultId();
		ItemInstance item = _player.getInventory().addItem(data.getId(), data.getCount(), data.getEnchantLevel());

		packetWriter.writeC(0); // 0 for open window
		packetWriter.writeH(0x0F); // unk
		packetWriter.writeD(resultId); // item id
		packetWriter.writeQ(data.getCount()); // item count
		packetWriter.writeC(data.getEnchantLevel()); // enchant Level

		if (RandomCraftItem.isAnnounce(item.getItemId()))
		{
			for (Player player : GameObjectsStorage.getPlayers(false, false))
			{
				player.sendPacket(new ExItemAnnounce(_player, item, 2));
				player.sendMessage(_player.getPlayer().getName() + " adquired " + item.getName() + " and obtained: " + item.getName() + ((item.getEnchantLevel() > 0) ? (" + (" + item.getEnchantLevel() + ") ") : "") + " x" + item.getCount() + "  through Random Craft!");
			}
		}

		_player.getInventory().addItem(resultId, data.getCount(), data.getEnchantLevel());
		_player.sendPacket(new SystemMessagePacket(SystemMsg.CONGRATULATIONS_YOU_HAVE_RECEIVED_S1).addItemName(data.getId()));
		_player.setCraftPoints(_player.getCraftPoints() - 1, null);
		_player.setVar("didCraft", true);

		Map<Integer, RandomCraftInfo> list = _player.getRandomCraftList();
		for (int i = 0; i < 5; i++)
		{
			if (list.get(i).isLocked())
			{
				list.get(i).setRefreshToUnlockCount((byte) 20);
				list.get(i).setIsLocked(false);
			}
		}
		_player.setRandomCraftList(list);

		_player.sendPacket(new ExCraftRandomInfo(_player));
	}
}