package l2s.gameserver.templates.item;

import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.utils.TimeUtils;

/**
 * @author VISTALL
 */
public enum ItemReuseType
{
	NORMAL(SystemMsg.THERE_ARE_S2_SECONDS_REMAINING_IN_S1S_REUSE_TIME, SystemMsg.THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_IN_S1S_REUSE_TIME, SystemMsg.THERE_ARE_S2_HOURS_S3_MINUTES_AND_S4_SECONDS_REMAINING_IN_S1S_REUSE_TIME)
	{
		@Override
		public long next(ItemInstance item)
		{
			return System.currentTimeMillis() + item.getTemplate().getReuseDelay();
		}
	},
	EVERY_DAY_AT_6_30(SystemMsg.THERE_ARE_S2_SECONDS_REMAINING_FOR_S1S_REUSE_TIME, SystemMsg.THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_FOR_S1S_REUSE_TIME, SystemMsg.THERE_ARE_S2_HOURS_S3_MINUTES_S4_SECONDS_REMAINING_FOR_S1S_REUSE_TIME)
	{
		@Override
		public long next(ItemInstance item)
		{
			return TimeUtils.DAILY_DATE_PATTERN.next(System.currentTimeMillis());
		}
	};

	public static final ItemReuseType[] VALUES = values();

	private SystemMsg[] _messages;

	ItemReuseType(SystemMsg... msg)
	{
		_messages = msg;
	}

	public abstract long next(ItemInstance item);

	public SystemMsg[] getMessages()
	{
		return _messages;
	}
}