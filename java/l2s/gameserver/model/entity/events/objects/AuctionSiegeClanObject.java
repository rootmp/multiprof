package l2s.gameserver.model.entity.events.objects;

import l2s.gameserver.model.entity.events.impl.ClanHallAuctionEvent;
import l2s.gameserver.model.pledge.Clan;

/**
 * @author VISTALL
 * @date 2:23/16.06.2011
 */
public class AuctionSiegeClanObject extends SiegeClanObject
{
	private long _bid;

	public AuctionSiegeClanObject(ClanHallAuctionEvent siegeEvent, String type, Clan clan, long param)
	{
		this(siegeEvent, type, clan, param, System.currentTimeMillis());
	}

	public AuctionSiegeClanObject(ClanHallAuctionEvent siegeEvent, String type, Clan clan, long param, long date)
	{
		super(siegeEvent, type, clan, param, date);
		_bid = param;
	}

	@Override
	public long getParam()
	{
		return _bid;
	}

	public void setParam(long param)
	{
		_bid = param;
	}
}
