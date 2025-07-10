package l2s.gameserver.network.l2.s2c.randomcraft;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.RandomCraftInfo;

/**
 * @author nexvill
 */
public class ExCraftRandomInfo implements IClientOutgoingPacket
{
	private Player _player;
	private Map<Integer, RandomCraftInfo> _randomCraftInfo = new HashMap<>();

	public ExCraftRandomInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (_player.getRandomCraftList().size() == 0)
		{
			_player.generateRandomCraftList();
		}

		if (_player.getCraftPoints() > 0)
		{
			_randomCraftInfo = _player.getRandomCraftList();

			if (_player.getVarBoolean("didCraft", true))
			{
				packetWriter.writeD(5);
				for (int i = 0; i < 5; i++)
				{
					packetWriter.writeC(0);
					packetWriter.writeD(0);
					packetWriter.writeD(0);
					packetWriter.writeQ(0);
				}

				return;
			}

			packetWriter.writeD(5);
			for (int i : _randomCraftInfo.keySet())
			{
				RandomCraftInfo info = _randomCraftInfo.get(i);

				packetWriter.writeC(info.isLocked()); // locked slot or no
				packetWriter.writeD(info.getRefreshToUnlockCount()); // refresh count to unlock slot (max 20)
				packetWriter.writeD(info.getId()); // item id
				packetWriter.writeQ(info.getCount()); // item count
			}
		}
		else
		{
			packetWriter.writeD(5);
			for (int i = 0; i < 5; i++)
			{
				packetWriter.writeC(0);
				packetWriter.writeD(0);
				packetWriter.writeD(0);
				packetWriter.writeQ(0);
			}
		}
	}
}