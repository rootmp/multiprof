package l2s.gameserver.model.entity.events.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.dao.SiegeClanDAO;
import l2s.gameserver.dao.SiegePlayerDAO;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.IBroadcastPacket;

/**
 * @author VISTALL
 * @date 2:23/16.06.2011
 */
public class CastleSiegeClanObject extends SiegeClanObject
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CastleSiegeClanObject.class);

	private final Map<Integer, CastleSiegeMercenaryObject> mercenaries = new HashMap<>();
	private final AtomicBoolean mercenaryReception = new AtomicBoolean(false);

	public CastleSiegeClanObject(CastleSiegeEvent siegeEvent, String type, Clan clan, long param, long date)
	{
		super(siegeEvent, type, clan, param, date);
	}

	@Override
	public CastleSiegeEvent getSiegeEvent()
	{
		return (CastleSiegeEvent) super.getSiegeEvent();
	}

	@Override
	public void broadcast(IBroadcastPacket... packet)
	{
		super.broadcast(packet);
		getMercenaries().forEach(m ->
		{
			Player player = m.getPlayer();
			if (player != null)
			{
				player.sendPacket(packet);
			}
		});
	}

	@Override
	public void setEvent(boolean start, SiegeEvent<?, ?> event)
	{
		super.setEvent(start, event);
		if (start)
		{
			getMercenaries().forEach(m ->
			{
				Player player = m.getPlayer();
				if (player != null)
				{
					player.addEvent(event);
					player.broadcastCharInfo();
				}
			});
		}
		else
		{
			getMercenaries().forEach(m ->
			{
				Player player = m.getPlayer();
				if (player != null)
				{
					player.removeEvent(event);
					// player.getAbnormalList().stop(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME);
					player.broadcastCharInfo();
				}
			});
		}
	}

	public void select()
	{
		List<Integer> objectIds = SiegePlayerDAO.getInstance().select(getSiegeEvent().getResidence(), getClan().getClanId());
		for (int objId : objectIds)
		{
			Connection con = null;
			PreparedStatement statement = null;
			ResultSet rset = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("SELECT class_id FROM character_subclasses WHERE active=1 AND obj_Id=?");
				statement.setInt(1, objId);
				rset = statement.executeQuery();
				if (rset.next())
				{
					ClassId classId = ClassId.valueOf(rset.getInt("class_id"));
					addMercenary(new CastleSiegeMercenaryObject(objId, getSiegeEvent().getLastMercenaryId().incrementAndGet(), getClan().getClanId(), classId));
				}
			}
			catch (Exception e)
			{
				LOGGER.error("CastleSiegeClanObject.restore(): " + e, e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement, rset);
			}
		}
	}

	public Collection<CastleSiegeMercenaryObject> getMercenaries()
	{
		return mercenaries.values();
	}

	public CastleSiegeMercenaryObject getMercenary(int objectId)
	{
		return mercenaries.get(objectId);
	}

	public boolean addMercenary(CastleSiegeMercenaryObject mercenaryObject)
	{
		return mercenaries.put(mercenaryObject.getPlayerObjectId(), mercenaryObject) == null;
	}

	public boolean removeMercenary(int objectId)
	{
		return mercenaries.remove(objectId) != null;
	}

	public void clearMercenaries()
	{
		mercenaries.clear();
	}

	public void startMercenaryRecruiting(long rewardRate)
	{
		setParam(rewardRate);
		SiegeClanDAO.getInstance().update(getSiegeEvent().getResidence(), this);
	}

	public void stopMercenaryRecruiting()
	{
		setParam(0);
		SiegeClanDAO.getInstance().update(getSiegeEvent().getResidence(), this);
	}

	public AtomicBoolean getMercenaryReception()
	{
		return mercenaryReception;
	}
}
