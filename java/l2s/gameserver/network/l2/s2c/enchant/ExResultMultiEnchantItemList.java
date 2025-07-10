package l2s.gameserver.network.l2.s2c.enchant;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.item.data.ItemData;

/**
 * @author nexvill
 */
public class ExResultMultiEnchantItemList extends L2GameServerPacket
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
	protected final void writeImpl()
	{
		if (_error)
		{
			writeC(0);
			return;
		}

		writeC(1);

		// EnchantSuccessItem
		if (_failureReward.size() == 0)
		{
			writeD(_successEnchant.size());
			if (_successEnchant.size() != 0)
			{
				for (int i : _successEnchant.keySet())
				{
					int[] intArray = _successEnchant.get(i);
					writeD(intArray[0]);
					writeD(intArray[1]);
				}
			}
		}
		else
		{
			writeD(0);
		}

		// EnchantFailItem
		writeD(_failureEnchant.size());
		if (_failureEnchant.size() != 0)
		{
			for (int i : _failureEnchant.keySet())
			{
				writeD(_failureEnchant.get(i));
				writeD(0);
			}
		}
		else
		{
			writeD(0);
		}

		// EnchantFailRewardItem
		if (((_successEnchant.size() == 0) && (_player.getMultiFailItemsCount() != 0)) || (_isResult && (_player.getMultiFailItemsCount() != 0)))
		{
			writeD(_player.getMultiFailItemsCount());
			_failureReward = _player.getMultiEnchantFailItems();
			for (int i : _failureReward.keySet())
			{
				ItemData item = _failureReward.get(i);
				writeD(item.getId());
				writeD((int) item.getCount());
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
			writeD(0);
		}

		// EnchantFailChallengePointInfo
		writeD(1);
		writeD(0);
		writeD(0);
	}
}