package l2s.gameserver.model.entity.events.objects;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.utils.TimeUtils;

/**
 * @author VISTALL
 * @date 9:47/24.02.2011
 */
public class SiegeClanObject implements Comparable<SiegeClanObject>
{
	private final SiegeEvent<?, ?> _siegeEvent;
	private String _type;
	private final Clan _clan;
	private long _param;
	private NpcInstance _flag;
	private final long _date;

	public SiegeClanObject(SiegeEvent<?, ?> siegeEvent, String type, Clan clan, long param, long date)
	{
		_siegeEvent = siegeEvent;
		_type = type;
		_clan = clan;
		_param = param;
		_date = date;
	}

	public SiegeEvent<?, ?> getSiegeEvent()
	{
		return _siegeEvent;
	}

	public int getObjectId()
	{
		return _clan.getClanId();
	}

	public Clan getClan()
	{
		return _clan;
	}

	public NpcInstance getFlag()
	{
		return _flag;
	}

	public void deleteFlag()
	{
		if(_flag != null)
		{
			_flag.deleteMe();
			_flag = null;
		}
	}

	public void setFlag(NpcInstance npc)
	{
		_flag = npc;
	}

	public void setType(String type)
	{
		_type = type;
	}

	public String getType()
	{
		return _type;
	}

	public void broadcast(IBroadcastPacket... packet)
	{
		getClan().broadcastToOnlineMembers(packet);
	}

	public void setEvent(boolean start, SiegeEvent<?, ?> event)
	{
		if(start)
		{
			for(Player player : _clan.getOnlineMembers())
			{
				player.addEvent(event);
				player.broadcastCharInfo();
			}
		}
		else
		{
			for(Player player : _clan.getOnlineMembers())
			{
				player.removeEvent(event);
				// player.getAbnormalList().stop(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME);
				player.broadcastCharInfo();
			}
		}
	}

	public boolean isParticle(Player player)
	{
		return true;
	}

	public long getParam()
	{
		return _param;
	}

	public void setParam(long value)
	{
		_param = value;
	}

	public long getDate()
	{
		return _date;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getClan().getName() + ", reg: " + TimeUtils.toSimpleFormat(getDate()) + ", param: " + getParam()
				+ ", type: " + _type + "]";
	}

	@Override
	public int compareTo(SiegeClanObject o)
	{
		return Long.compare(o.getParam(), getParam());
	}
}
