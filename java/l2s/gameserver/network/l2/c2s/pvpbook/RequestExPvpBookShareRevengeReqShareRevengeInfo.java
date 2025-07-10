package l2s.gameserver.network.l2.c2s.pvpbook;

import java.util.concurrent.TimeUnit;

import l2s.gameserver.dao.PvpbookDAO;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpBookShareRevengeNewRevengeInfo;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class RequestExPvpBookShareRevengeReqShareRevengeInfo extends L2GameClientPacket
{
	private String killedName;
	private String killerName;
	private int shareType;

	@Override
	protected boolean readImpl()
	{
		killedName = readString();
		killerName = readString();
		shareType = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (RankManager.getInstance().getRankList().size() == 0)
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.UNABLE_TO_USE_BECAUSE_THERE_IS_NO_RATING_INFO));
			return;
		}

		if (!activeChar.getInventory().destroyItemByItemId(57, 100_000))
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
			return;
		}
		else
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_ADENA_DISAPPEARED).addInteger(100_000));
		}

		int objId = 0;
		if (shareType == 2)
		{
			final StatsSet ranker = RankManager.getInstance().getRankList().get(1);
			objId = ranker.getInteger("charId");
			Player player = GameObjectsStorage.getPlayer(objId);
			if (player != null)
			{
				Player killer = GameObjectsStorage.getPlayer(killerName);
				if (killer != null)
				{
					if (killer.getObjectId() == objId)
					{
						return;
					}

					PvpbookInfo pvpbookInfo = player.getPvpbook().addInfo(activeChar, killer, activeChar.getPvpbook().getInfo(killer.getObjectId()).getDeathTime(), (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
					if (pvpbookInfo != null)
					{
						player.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(killedName, killerName, shareType));
					}
				}
				else
				{
					PvpbookInfo info = activeChar.getPvpbook().getInfo(killerName);
					int killerObjId = info.getKillerObjectId();

					if (killerObjId == objId)
					{
						return;
					}

					int deathTime = info.getDeathTime();
					int killerLevel = info.getKillerLevel();
					int killerClassId = info.getKillerClassId();
					int karma = info.getKarma();
					PvpbookInfo pvpbookInfo = player.getPvpbook().addInfo(activeChar.getObjectId(), killerObjId, deathTime, killedName, killerName, activeChar.getLevel(), killerLevel, activeChar.getActiveClassId(), killerClassId, info.getKilledClanName(), info.getKillerClanName(), karma, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
					if (pvpbookInfo != null)
					{
						player.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(killedName, killerName, shareType));
					}
				}
			}
			else
			{
				PvpbookInfo info = activeChar.getPvpbook().getInfo(killerName);
				int killerObjId = info.getKillerObjectId();

				if (killerObjId == objId)
				{
					return;
				}

				int deathTime = info.getDeathTime();
				int killerLevel = info.getKillerLevel();
				int killerClassId = info.getKillerClassId();
				int karma = info.getKarma();
				PvpbookDAO.getInstance().insert(objId, activeChar.getObjectId(), killerObjId, deathTime, killedName, killerName, activeChar.getLevel(), killerLevel, activeChar.getActiveClassId(), killerClassId, info.getKilledClanName(), info.getKillerClanName(), karma, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
			}
		}
		else
		{
			for (UnitMember member : activeChar.getClan().getAllMembers())
			{
				objId = member.getObjectId();
				Player player = GameObjectsStorage.getPlayer(objId);
				if (player != null)
				{
					Player killer = GameObjectsStorage.getPlayer(killerName);
					if (killer != null)
					{
						if (killer.getObjectId() == objId)
						{
							return;
						}

						PvpbookInfo pvpbookInfo = player.getPvpbook().addInfo(activeChar, killer, activeChar.getPvpbook().getInfo(killer.getObjectId()).getDeathTime(), (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
						if (pvpbookInfo != null)
						{
							player.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(killedName, killerName, shareType));
						}
					}
					else
					{
						PvpbookInfo info = activeChar.getPvpbook().getInfo(killerName);
						int killerObjId = info.getKillerObjectId();

						if (killerObjId == objId)
						{
							return;
						}

						int deathTime = info.getDeathTime();
						int killerLevel = info.getKillerLevel();
						int killerClassId = info.getKillerClassId();
						int karma = info.getKarma();
						PvpbookInfo pvpbookInfo = player.getPvpbook().addInfo(activeChar.getObjectId(), killerObjId, deathTime, killedName, killerName, activeChar.getLevel(), killerLevel, activeChar.getActiveClassId(), killerClassId, info.getKilledClanName(), info.getKillerClanName(), karma, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
						if (pvpbookInfo != null)
						{
							player.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(killedName, killerName, shareType));
						}
					}
				}
				else
				{
					PvpbookInfo info = activeChar.getPvpbook().getInfo(killerName);
					int killerObjId = info.getKillerObjectId();

					if (killerObjId == objId)
					{
						return;
					}

					int deathTime = info.getDeathTime();
					int killerLevel = info.getKillerLevel();
					int killerClassId = info.getKillerClassId();
					int karma = info.getKarma();
					PvpbookDAO.getInstance().insert(objId, activeChar.getObjectId(), killerObjId, deathTime, killedName, killerName, activeChar.getLevel(), killerLevel, activeChar.getActiveClassId(), killerClassId, info.getKilledClanName(), info.getKillerClanName(), karma, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
				}
			}
		}
	}
}