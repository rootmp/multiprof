package l2s.gameserver.network.l2.s2c.randomcraft;

import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.RandomCraftInfo;

/**
 * @author nexvill
 */
public class ExCraftRandomLockSlot implements IClientOutgoingPacket
{
	private final Player _player;
	private final int _slot;
	private static final int MONEY_L = 91663;

	public ExCraftRandomLockSlot(Player player, int slot)
	{
		_player = player;
		_slot = slot;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int lockedCount = _player.getRandomCraftLockedSlots();

		if (lockedCount < 3)
		{
			switch (lockedCount)
			{
				case 0:
				{
					if (_player.getInventory().getItemByItemId(MONEY_L).getCount() < 100)
						return false;
					else
						_player.getInventory().destroyItemByItemId(MONEY_L, 100);
					break;
				}
				case 1:
				{
					if (_player.getInventory().getItemByItemId(MONEY_L).getCount() < 500)
						return false;
					else
						_player.getInventory().destroyItemByItemId(MONEY_L, 500);
					break;
				}
				case 2:
				{
					if (_player.getInventory().getItemByItemId(MONEY_L).getCount() < 1000)
						return false;
					else
						_player.getInventory().destroyItemByItemId(MONEY_L, 1000);
					break;
				}
			}

			Map<Integer, RandomCraftInfo> info = _player.getRandomCraftList();
			RandomCraftInfo data = info.get(_slot);
			data.setIsLocked(true);
			info.replace(_slot, data);
			_player.getRandomCraftLockedSlots();//?
		}
		else
		{
			return false;
		}

		packetWriter.writeC(0);
		_player.sendPacket(new ExCraftRandomLockSlot(_player, lockedCount));//?
		_player.sendPacket(new ExCraftRandomInfo(_player));
		return true;
	}
}