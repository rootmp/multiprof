package l2s.gameserver.network.l2.s2c.enchant;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.item.data.ItemData;

/**
 * @author nexvill
 */
public class ExResultMultiEnchantItemList implements IClientOutgoingPacket
{
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	public static final int ERROR = 2;
	private final Player _player;
	private boolean _error;
	private boolean _isResult;
	private Map<Integer, int[]> _successEnchant = new HashMap<>();
	private Map<Integer, Integer> _failureEnchant = new HashMap<>();
	private Map<Integer, ItemData> _failureReward = new HashMap<>();

	public ExResultMultiEnchantItemList(Player player, boolean error)
	{
		_player = player;
		_error = error;
	}

	public ExResultMultiEnchantItemList(Player player, Map<Integer, ItemData> failureReward)
	{
		_player = player;
		_failureReward = failureReward;
	}

	public ExResultMultiEnchantItemList(Player player, Map<Integer, int[]> successEnchant, Map<Integer, Integer> failureEnchant)
	{
		_player = player;
		_successEnchant = successEnchant;
		_failureEnchant = failureEnchant;
	}

	public ExResultMultiEnchantItemList(Player player, Map<Integer, int[]> successEnchant, Map<Integer, Integer> failureEnchant, boolean isResult)
	{
		_player = player;
		_successEnchant = successEnchant;
		_failureEnchant = failureEnchant;
		_isResult = isResult;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (_error)
		{
			packetWriter.writeC(0);
			return true;
		}

		packetWriter.writeC(1);

		// EnchantSuccessItem
		if (_failureReward.size() == 0)
		{
			packetWriter.writeD(_successEnchant.size());
			if (_successEnchant.size() != 0)
			{
				for (int i : _successEnchant.keySet())
				{
					int[] intArray = _successEnchant.get(i);
					packetWriter.writeD(intArray[0]);
					packetWriter.writeD(intArray[1]);
				}
			}
		}
		else
		{
			packetWriter.writeD(0);
		}

		// EnchantFailItem
		packetWriter.writeD(_failureEnchant.size());
		if (_failureEnchant.size() != 0)
		{
			for (int i : _failureEnchant.keySet())
			{
				packetWriter.writeD(_failureEnchant.get(i));
				packetWriter.writeD(0);
			}
		}
		else
		{
			packetWriter.writeD(0);
		}

		// EnchantFailRewardItem
		if (((_successEnchant.size() == 0) && (_player.getMultiFailItemsCount() != 0)) || (_isResult && (_player.getMultiFailItemsCount() != 0)))
		{
			packetWriter.writeD(_player.getMultiFailItemsCount());
			_failureReward = _player.getMultiEnchantFailItems();
			for (int i : _failureReward.keySet())
			{
				ItemData item = _failureReward.get(i);
				packetWriter.writeD(item.getId());
				packetWriter.writeD((int) item.getCount());
			}
			if (_isResult)
			{
				_player.clearMultiSuccessEnchantList();
				_player.clearMultiFailureEnchantList();
			}
			_player.clearMultiFailReward();
		}
		else
		{
			packetWriter.writeD(0);
		}

		// EnchantFailChallengePointInfo
		packetWriter.writeD(1);
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		return true;
	}
}