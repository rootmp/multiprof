package l2s.gameserver.model.entity.residence;

import java.util.concurrent.TimeUnit;

import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.dao.FortressDAO;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.templates.StatsSet;

public class Fortress extends Residence
{
	private static final long REMOVE_CYCLE = TimeUnit.DAYS.toHours(1); // 1 день форт может пренадлежать овнеру

	public Fortress(StatsSet set)
	{
		super(set);
	}

	@Override
	public ResidenceType getType()
	{
		return ResidenceType.FORTRESS;
	}

	@Override
	public void cancelCycleTask()
	{
		super.cancelCycleTask();
	}

	@Override
	public void changeOwner(Clan clan)
	{
		// Если клан уже владел каким-либо замком/крепостью, отбираем его.
		if(clan != null)
		{
			if(clan.getHasFortress() != 0)
			{
				Fortress oldFortress = ResidenceHolder.getInstance().getResidence(Fortress.class, clan.getHasFortress());
				if(oldFortress != null)
					oldFortress.changeOwner(null);
			}
			if(clan.getCastle() != 0)
			{
				Castle oldCastle = ResidenceHolder.getInstance().getResidence(Castle.class, clan.getCastle());
				if(oldCastle != null)
					oldCastle.changeOwner(null);
			}
		}

		// Если этой крепостью уже кто-то владел, отбираем у него крепость
		if(getOwnerId() > 0 && (clan == null || clan.getClanId() != getOwnerId()))
		{
			// Удаляем фортовые скилы у старого владельца
			removeSkills();
			Clan oldOwner = getOwner();
			if(oldOwner != null)
				oldOwner.setHasFortress(0);

			cancelCycleTask();
		}

		setOwner(clan);
		removeFunctions();

		// Выдаем крепость новому владельцу
		if(clan != null)
		{
			clan.setHasFortress(getId());
			clan.broadcastClanStatus(true, false, false);
		}

		// Выдаем фортовые скилы новому владельцу
		rewardSkills();
		if(getJdbcState().isPersisted())
		{
			setJdbcState(JdbcEntityState.UPDATED);
			update();
		}
		else
		{
			save();
		}
	}

	@Override
	protected void loadData()
	{
		FortressDAO.getInstance().select(this);
	}

	@Override
	public void chanceCycle()
	{
		super.chanceCycle();

		if(getCycle() >= REMOVE_CYCLE)
		{
			getOwner().broadcastToOnlineMembers(SystemMsg.ENEMY_BLOOD_PLEDGES_HAVE_INTRUDED_INTO_THE_FORTRESS);
			changeOwner(null);
		}
	}

	@Override
	public void update()
	{
		FortressDAO.getInstance().saveOrUpdate(this);
	}

	@Override
	public void save()
	{
		FortressDAO.getInstance().saveOrUpdate(this);
	}
}