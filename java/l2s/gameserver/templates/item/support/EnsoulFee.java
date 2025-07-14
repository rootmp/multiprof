package l2s.gameserver.templates.item.support;

import java.util.Collections;
import java.util.List;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Bonux
**/
public class EnsoulFee
{
	public static class EnsoulFeeInfo
	{
		private List<EnsoulFeeItem> _insertFee = Collections.emptyList();
		private List<EnsoulFeeItem> _changeFee = Collections.emptyList();
		private List<EnsoulFeeItem> _removeFee = Collections.emptyList();

		public void setInsertFee(List<EnsoulFeeItem> value)
		{
			_insertFee = value;
		}

		public List<EnsoulFeeItem> getInsertFee()
		{
			return _insertFee;
		}

		public void setChangeFee(List<EnsoulFeeItem> value)
		{
			_changeFee = value;
		}

		public List<EnsoulFeeItem> getChangeFee()
		{
			return _changeFee;
		}

		public void setRemoveFee(List<EnsoulFeeItem> value)
		{
			_removeFee = value;
		}

		public List<EnsoulFeeItem> getRemoveFee()
		{
			return _removeFee;
		}
	}

	public static class EnsoulFeeItem
	{
		private final int _id;
		private final long _count;

		public EnsoulFeeItem(int id, long count)
		{
			_id = id;
			_count = count;
		}

		public int getId()
		{
			return _id;
		}

		public long getCount()
		{
			return _count;
		}
	}

	private TIntObjectMap<EnsoulFeeInfo> _ensoulsFee = null;

	public void addFeeInfo(int type, EnsoulFeeInfo feeInfo)
	{
		if(_ensoulsFee == null)
			_ensoulsFee = new TIntObjectHashMap<EnsoulFeeInfo>();
		_ensoulsFee.put(type, feeInfo);
	}

	public EnsoulFeeInfo getFeeInfo(int id)
	{
		if(_ensoulsFee == null)
			return null;

		return _ensoulsFee.get(id);
	}
}