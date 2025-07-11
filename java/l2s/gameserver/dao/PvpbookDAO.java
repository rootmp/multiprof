package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Pvpbook;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.utils.SqlBatch;

public class PvpbookDAO
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PvpbookDAO.class);

	private static final PvpbookDAO INSTANCE = new PvpbookDAO();

	public static PvpbookDAO getInstance()
	{
		return INSTANCE;
	}

	private static final String RESTORE_SQL_QUERY = "SELECT * FROM character_pvpbook WHERE char_id=?";
	private static final String STORE_SQL_QUERY = "REPLACE INTO character_pvpbook (char_id,killed_id,killer_id,death_time,killed_name,killer_name,killed_level,killer_level,killed_class_id,killer_class_id,killed_clan_name,killer_clan_name,karma, shared_time,location_show,teleport_count,teleport_help_count,share_type,request_for_help) VALUES";
	private static final String INSERT_SQL_QUERY = "INSERT IGNORE INTO character_pvpbook (char_id, killed_id, killer_id, death_time, killed_name, killer_name, killed_level, killer_level, killed_class_id, killer_class_id, killed_clan_name, killer_clan_name, karma, shared_time,location_show,teleport_count,teleport_help_count,share_type,request_for_help) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	//private static final String INSERT_SQL_QUERY = "INSERT IGNORE INTO character_pvpbook (char_id,killed_id,killer_id,death_time,killed_name,killer_name,killed_level,killer_level,killed_class_id,killer_class_id,killed_clan_name,killer_clan_name,karma, shared_time) VALUES";
	private static final String CLEANUP_SQL_QUERY = "DELETE FROM character_pvpbook WHERE char_id=?";
	private static final String DELETE_REVENGED_BY_HELP = "DELETE FROM character_pvpbook WHERE killed_id=? AND killer_id=? AND share_type=?";
	private static final String DELETE_EXPIRED_SQL_QUERY = "DELETE FROM character_pvpbook WHERE (? - death_time) > ?";

	public PvpbookDAO()
	{
		deleteExpired();
	}

	public boolean restore(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();
			Pvpbook pvpbook = player.getPvpbook();
			while(rset.next())
			{
				int deathTime = rset.getInt("death_time");
				if(Pvpbook.isExpired(deathTime))
					continue;

				int killedId = rset.getInt("killed_id");
				Player killedPlayer = GameObjectsStorage.getPlayer(killedId);

				int killerId = rset.getInt("killer_id");
				Player killerPlayer = GameObjectsStorage.getPlayer(killerId);
				int sharedTime = rset.getInt("shared_time");

				int locationShow = rset.getInt("location_show");
				int teleportСount = rset.getInt("teleport_count");
				int teleportHelpСount = rset.getInt("teleport_help_count");
				int shareType = rset.getInt("share_type");
				int request_for_help = rset.getInt("request_for_help");
				
				if((killedPlayer != null) && (killerPlayer != null))
				{
					pvpbook.addInfo(killedPlayer, killerPlayer, deathTime, sharedTime, locationShow, teleportСount, teleportHelpСount, shareType, request_for_help);
				}
				else
				{
					String killedName = rset.getString("killed_name");
					String killerName = rset.getString("killer_name");
					int killedLevel = rset.getInt("killed_level");
					int killerLevel = rset.getInt("killer_level");
					int killedClassId = rset.getInt("killed_class_id");
					int killerClassId = rset.getInt("killer_class_id");
					String killedClanName = rset.getString("killed_clan_name");
					String killerClanName = rset.getString("killer_clan_name");
					int karma = rset.getInt("karma");
					pvpbook.addInfo(killedId, killerId, deathTime, killedName, killerName, killedLevel, killerLevel, killedClassId, killerClassId, killedClanName, killerClanName, karma, sharedTime, locationShow, teleportСount, teleportHelpСount, shareType, request_for_help);
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.error("CharacterVengeancesDAO.restore(Player): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return true;
	}

	public boolean store(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CLEANUP_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			statement.execute();

			SqlBatch b = new SqlBatch(STORE_SQL_QUERY);
			for(PvpbookInfo pvpbookInfo : player.getPvpbook().getInfos(false))
			{
				StringBuilder sb = new StringBuilder("(");
				sb.append(player.getObjectId()).append(",");
				sb.append(pvpbookInfo.getKilledObjectId()).append(",");
				sb.append(pvpbookInfo.getKillerObjectId()).append(",");
				sb.append(pvpbookInfo.getDeathTime()).append(",");
				sb.append("'").append(pvpbookInfo.getKilledName()).append("'").append(",");
				sb.append("'").append(pvpbookInfo.getKillerName()).append("'").append(",");
				sb.append(pvpbookInfo.getKilledLevel()).append(",");
				sb.append(pvpbookInfo.getKillerLevel()).append(",");
				sb.append(pvpbookInfo.getKilledClassId()).append(",");
				sb.append(pvpbookInfo.getKillerClassId()).append(",");
				sb.append("'").append(pvpbookInfo.getKilledClanName()).append("'").append(",");
				sb.append("'").append(pvpbookInfo.getKillerClanName()).append("'").append(",");
				sb.append(pvpbookInfo.getKarma()).append(",");
				sb.append(pvpbookInfo.getSharedTime()).append(",");
				sb.append(pvpbookInfo.getLocationShowCount()).append(",");
				sb.append(pvpbookInfo.getTeleportCount()).append(",");
				sb.append(pvpbookInfo.getTeleportHelpCount()).append(",");
				sb.append(pvpbookInfo.getShareType()).append(",");
				sb.append(pvpbookInfo.isRequestForHelp()).append(")");
				b.write(sb.toString());
			}
			if(!b.isEmpty())
				statement.executeUpdate(b.close());
		}
		catch(Exception e)
		{
			LOGGER.error("CharacterVengeancesDAO.store(Player): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public boolean insert(int objId, int killedObjId, int killerObjId, int deathTime, String killedName, String killerName, int killedLevel, int killerLevel, int killedClassId, int killerClassId, String killedClanName, String killerClanName, int karma, int sharedTime, int locationShow, int teleportCount, int teleportHelpCount, int shareType, int request_for_help)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, objId);
			statement.setInt(2, killedObjId);
			statement.setInt(3, killerObjId);
			statement.setInt(4, deathTime);
			statement.setString(5, killedName);
			statement.setString(6, killerName);
			statement.setInt(7, killedLevel);
			statement.setInt(8, killerLevel);
			statement.setInt(9, killedClassId);
			statement.setInt(10, killerClassId);
			statement.setString(11, killedClanName);
			statement.setString(12, killerClanName);
			statement.setInt(13, karma);
			statement.setInt(14, sharedTime);
			statement.setInt(15, locationShow);
			statement.setInt(16, teleportCount);
			statement.setInt(17, teleportHelpCount);
			statement.setInt(18, shareType);
			statement.setInt(19, request_for_help);
			statement.execute();
		}
		catch(Exception e)
		{
			LOGGER.error("CharacterVengeancesDAO.insert(objId, killedObjId, killerObjId, deathTime, killedName, killerName, killedLevel, killerLevel, killedClassId, killerClassId, killedClanName, killerClanName, karma, sharedTime, locationShow, teleportСount, teleportHelpCount, share_type, request_for_help): "
					+ e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public boolean insert(Player player, PvpbookInfo pvpbookInfo)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, pvpbookInfo.getKilledObjectId());
			statement.setInt(3, pvpbookInfo.getKillerObjectId());
			statement.setInt(4, pvpbookInfo.getDeathTime());
			statement.setString(5, pvpbookInfo.getKilledName());
			statement.setString(6, pvpbookInfo.getKillerName());
			statement.setInt(7, pvpbookInfo.getKilledLevel());
			statement.setInt(8, pvpbookInfo.getKillerLevel());
			statement.setInt(9, pvpbookInfo.getKilledClassId());
			statement.setInt(10, pvpbookInfo.getKillerClassId());
			statement.setString(11, pvpbookInfo.getKilledClanName());
			statement.setString(12, pvpbookInfo.getKillerClanName());
			statement.setInt(13, pvpbookInfo.getKarma());
			statement.setInt(14, pvpbookInfo.getSharedTime());
			statement.setInt(15, pvpbookInfo.getLocationShowCount());
			statement.setInt(16, pvpbookInfo.getTeleportCount());
			statement.setInt(17, pvpbookInfo.getTeleportHelpCount());
			statement.setInt(18, pvpbookInfo.getShareType());
			statement.setInt(19, pvpbookInfo.isRequestForHelp());
			statement.execute();
		}
		catch(Exception e)
		{
			LOGGER.error("PvpbookDAO.insertSingleEntry(PvpbookInfo): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public void deleteRevengedByHelp(int killedId, int killerId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_REVENGED_BY_HELP);
			statement.setInt(1, killedId);
			statement.setInt(2, killedId);
			statement.setInt(3, 1);
			statement.execute();
		}
		catch(Exception e)
		{
			LOGGER.error("CharacterVengeancesDAO.deleteRevengedByHelp(killedId, killerId): " + e, e);
			return;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	private void deleteExpired()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_EXPIRED_SQL_QUERY);
			statement.setInt(1, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
			statement.setInt(2, Pvpbook.EXPIRATION_DELAY);
			statement.execute();
		}
		catch(final Exception e)
		{
			LOGGER.error("CharacterVengeancesDAO:deleteExpired()", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
