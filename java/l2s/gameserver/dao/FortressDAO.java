package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dao.JdbcEntityState;
import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.entity.residence.Fortress;
import l2s.gameserver.tables.ClanTable;

public class FortressDAO
{
	public static final String SELECT_SQL_QUERY = "SELECT * FROM fortress WHERE id = ?";
	private static final Logger LOGGER = LoggerFactory.getLogger(FortressDAO.class);
	private static final FortressDAO INSTANCE = new FortressDAO();
	private static final String INSERT_SQL_QUERY = "INSERT INTO fortress (id, name, last_siege_date, owner_id, own_date, siege_date, cycle) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE last_siege_date=?,owner_id=?,own_date=?,siege_date=?,cycle=?";
	private static final String UPDATE_SQL_QUERY = "UPDATE fortress SET last_siege_date=?,owner_id=?,own_date=?,siege_date=?,cycle=? WHERE id=?";

	public static FortressDAO getInstance()
	{
		return INSTANCE;
	}

	public void select(Fortress fortress)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, fortress.getId());
			rset = statement.executeQuery();
			if(rset.next())
			{
				fortress.getLastSiegeDate().setTimeInMillis(rset.getLong("last_siege_date"));
				fortress.setOwner(ClanTable.getInstance().getClan(rset.getInt("owner_id")));
				fortress.getOwnDate().setTimeInMillis(rset.getLong("own_date"));
				fortress.getSiegeDate().setTimeInMillis(rset.getLong("siege_date"));
				fortress.setCycle(rset.getInt("cycle"));
			}
		}
		catch(Exception e)
		{
			LOGGER.error("FortressDAO#select(Fortress):" + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public boolean save(Fortress fortress)
	{
		if(!fortress.getJdbcState().isSavable())
			return false;

		if(save0(fortress))
		{
			fortress.setJdbcState(JdbcEntityState.STORED);
			return true;
		}
		return false;
	}

	private boolean save0(Fortress fortress)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);

			int i = 0;
			statement.setInt(++i, fortress.getId());
			statement.setString(++i, fortress.getName());
			statement.setLong(++i, fortress.getLastSiegeDate().getTimeInMillis());
			statement.setInt(++i, fortress.getOwner() == null ? 0 : fortress.getOwner().getClanId());
			statement.setLong(++i, fortress.getOwnDate().getTimeInMillis());
			statement.setLong(++i, fortress.getSiegeDate().getTimeInMillis());
			statement.setInt(++i, fortress.getCycle());

			statement.setLong(++i, fortress.getLastSiegeDate().getTimeInMillis());
			statement.setInt(++i, fortress.getOwner() == null ? 0 : fortress.getOwner().getClanId());
			statement.setLong(++i, fortress.getOwnDate().getTimeInMillis());
			statement.setLong(++i, fortress.getSiegeDate().getTimeInMillis());
			statement.setInt(++i, fortress.getCycle());

			statement.execute();
			return true;
		}
		catch(Exception e)
		{
			LOGGER.warn("FortressDAO#save0(Fortress): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return false;
	}

	public boolean update(Fortress fortress)
	{
		if(!fortress.getJdbcState().isUpdatable())
			return false;

		if(update0(fortress))
		{
			fortress.setJdbcState(JdbcEntityState.STORED);
			return true;
		}
		return false;
	}

	private boolean update0(Fortress fortress)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_SQL_QUERY);

			int i = 0;
			statement.setLong(++i, fortress.getLastSiegeDate().getTimeInMillis());
			statement.setInt(++i, fortress.getOwner() == null ? 0 : fortress.getOwner().getClanId());
			statement.setLong(++i, fortress.getOwnDate().getTimeInMillis());
			statement.setLong(++i, fortress.getSiegeDate().getTimeInMillis());
			statement.setInt(++i, fortress.getCycle());
			statement.setInt(++i, fortress.getId());
			statement.execute();
			return true;
		}
		catch(Exception e)
		{
			LOGGER.warn("FortressDAO#update0(Fortress): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return false;
	}

	public void saveOrUpdate(Fortress fortress)
	{
		if(fortress.getJdbcState().isSavable())
			save(fortress);
		else if(fortress.getJdbcState().isUpdatable())
			update(fortress);
	}
}
