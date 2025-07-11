package l2s.gameserver.network.l2.s2c.randomcraft;

import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.RandomCraftInfo;

/**
 * @author nexvill
 */
public class ExCraftRandomRefresh implements IClientOutgoingPacket
{
	private Player _player;

	public ExCraftRandomRefresh(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if ((_player.getCraftPoints() < 1) || (_player.getAdena() < 10_000))
			return false;
		else
		{
			if (!_player.getVarBoolean("didCraft", true))
			{
				_player.setCraftPoints(_player.getCraftPoints() - 1, null);
			}
			_player.getInventory().destroyItemByItemId(57, 10_000);
			if (_player.isOnlyGainPoints())
			{
				_player.getInventory().addItem(91641, 2);
				_player.setOnlyGainPoints(false);
			}

			_player.generateRandomCraftList();
			_player.setVar("didCraft", false);

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
		}
		packetWriter.writeC(0);
		_player.sendPacket(new ExCraftRandomInfo(_player));
		return true;
	}
}