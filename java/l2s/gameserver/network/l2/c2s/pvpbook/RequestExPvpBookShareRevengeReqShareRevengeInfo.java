package l2s.gameserver.network.l2.c2s.pvpbook;

import java.util.concurrent.TimeUnit;

import l2s.commons.network.PacketReader;
import l2s.gameserver.dao.PvpbookDAO;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Pvpbook;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpBookShareRevengeList;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpBookShareRevengeNewRevengeInfo;
import l2s.gameserver.templates.ranking.PkRankerData;

public class RequestExPvpbookShareRevengeReqShareRevengeInfo implements IClientIncomingPacket
{
	private String killedName;
	private String killerName;
	private int shareType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		killedName = packet.readSizedString();
		killerName = packet.readSizedString();
		shareType = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(shareType == 2 && RankManager.getInstance().getRankList().size() == 0)
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.UNABLE_TO_USE_BECAUSE_THERE_IS_NO_RATING_INFO));
			return;
		}

		PvpbookInfo info = activeChar.getPvpbook().getInfo(killerName, 1);
		if(info == null || info.isRequestForHelp() == 1)
			return;

		if(!activeChar.getInventory().destroyItemByItemId(57, 100_000))
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
			return;
		}
		else
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_ADENA_DISAPPEARED).addInteger(100_000));
		}
		info.setRequestForHelp();

		int objId = 0;
		if(shareType == 2)
		{
			final PkRankerData ranker = RankManager.getInstance().getRankList().get(1);
			objId = ranker.nCharId;
			Player player = GameObjectsStorage.getPlayer(objId);
			if(player != null)
			{
				Player killer = GameObjectsStorage.getPlayer(killerName);
				if(killer != null)
				{
					if(killer.getObjectId() == objId || activeChar.getObjectId() == objId)
						return;

					PvpbookInfo pvpbookInfo = player.getPvpbook().addInfo(activeChar, killer, activeChar.getPvpbook().getInfo(killer.getObjectId(), 1).getDeathTime(), (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), 0, 0, Pvpbook.MAX_TELEPORT_HELP_COUNT_PER_DAY, 2, 0);
					if(pvpbookInfo != null)
						player.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(killedName, killerName, shareType));
				}
				else
				{
					int killerObjId = info.getKillerObjectId();

					if(killerObjId == objId || objId == activeChar.getObjectId())
						return;

					int deathTime = info.getDeathTime();
					int killerLevel = info.getKillerLevel();
					int killerClassId = info.getKillerClassId();
					int karma = info.getKarma();
					PvpbookInfo pvpbookInfo = player.getPvpbook().addInfo(activeChar.getObjectId(), killerObjId, deathTime, killedName, killerName, activeChar.getLevel(), killerLevel, activeChar.getActiveClassId(), killerClassId, info.getKilledClanName(), info.getKillerClanName(), karma, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), 0, 0, Pvpbook.MAX_TELEPORT_HELP_COUNT_PER_DAY, 2, 0);
					if(pvpbookInfo != null)
						player.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(killedName, killerName, shareType));
				}
			}
			else
			{
				int killerObjId = info.getKillerObjectId();

				if(killerObjId == objId || objId == activeChar.getObjectId())
					return;

				int deathTime = info.getDeathTime();
				int killerLevel = info.getKillerLevel();
				int killerClassId = info.getKillerClassId();
				int karma = info.getKarma();
				PvpbookDAO.getInstance().insert(objId, activeChar.getObjectId(), killerObjId, deathTime, killedName, killerName, activeChar.getLevel(), killerLevel, activeChar.getActiveClassId(), killerClassId, info.getKilledClanName(), info.getKillerClanName(), karma, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), 0, 0, Pvpbook.MAX_TELEPORT_HELP_COUNT_PER_DAY, 2, 0);
			}
		}
		else
		{
			for(UnitMember member : activeChar.getClan().getAllMembers())
			{
				objId = member.getObjectId();
				Player player = GameObjectsStorage.getPlayer(objId);
				if(player != null)
				{
					Player killer = GameObjectsStorage.getPlayer(killerName);
					if(killer != null)
					{
						if(killer.getObjectId() == objId || objId == activeChar.getObjectId())
							continue;

						PvpbookInfo pvpbookInfo = player.getPvpbook().addInfo(activeChar, killer, activeChar.getPvpbook().getInfo(killer.getObjectId(), 1).getDeathTime(), (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), 0, 0, Pvpbook.MAX_TELEPORT_HELP_COUNT_PER_DAY, 2, 0);
						if(pvpbookInfo != null)
							player.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(killedName, killerName, shareType));
					}
					else
					{
						int killerObjId = info.getKillerObjectId();

						if(killerObjId == objId || objId == activeChar.getObjectId())
							continue;

						int deathTime = info.getDeathTime();
						int killerLevel = info.getKillerLevel();
						int killerClassId = info.getKillerClassId();
						int karma = info.getKarma();
						PvpbookInfo pvpbookInfo = player.getPvpbook().addInfo(activeChar.getObjectId(), killerObjId, deathTime, killedName, killerName, activeChar.getLevel(), killerLevel, activeChar.getActiveClassId(), killerClassId, info.getKilledClanName(), info.getKillerClanName(), karma, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), 0, 0, Pvpbook.MAX_TELEPORT_HELP_COUNT_PER_DAY, 2, 0);
						if(pvpbookInfo != null)
							player.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(killedName, killerName, shareType));

					}
				}
				else
				{
					int killerObjId = info.getKillerObjectId();

					if(killerObjId == objId || objId == activeChar.getObjectId())
						continue;

					int deathTime = info.getDeathTime();
					int killerLevel = info.getKillerLevel();
					int killerClassId = info.getKillerClassId();
					int karma = info.getKarma();
					PvpbookDAO.getInstance().insert(objId, activeChar.getObjectId(), killerObjId, deathTime, killedName, killerName, activeChar.getLevel(), killerLevel, activeChar.getActiveClassId(), killerClassId, info.getKilledClanName(), info.getKillerClanName(), karma, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), 0, 0, Pvpbook.MAX_TELEPORT_HELP_COUNT_PER_DAY, 2, 0);
				}
			}
		}
		activeChar.sendPacket(new ExPvpBookShareRevengeList(activeChar));
	}
}