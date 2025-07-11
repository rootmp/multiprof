package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.relics.RelicsInfo;

public class AccountRelicsDAO
{
	private static final AccountRelicsDAO INSTANCE = new AccountRelicsDAO();

	public static AccountRelicsDAO getInstance()
	{
		return INSTANCE;
	}

	private static final String INSERT_QUERY = "INSERT INTO account_relics (account_name, relic_id, level, count) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_QUERY = "UPDATE account_relics SET level=?, count=? WHERE account_name=? AND relic_id=?";
	private static final String SELECT_QUERY = "SELECT relic_id, level, count FROM account_relics WHERE account_name=?";

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRelicsDAO.class);

	public boolean saveOrUpdate(Player owner, RelicsInfo relicInfo)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();

			statement = con.prepareStatement(UPDATE_QUERY);
			statement.setInt(1, relicInfo.nLevel);
			statement.setInt(2, relicInfo.nCount);
			statement.setString(3, owner.getAccountName());
			statement.setInt(4, relicInfo.nRelicsID);
			int rowsUpdated = statement.executeUpdate();

			if(rowsUpdated == 0)
			{
				DbUtils.closeQuietly(statement);
				statement = con.prepareStatement(INSERT_QUERY);
				statement.setString(1, owner.getAccountName());
				statement.setInt(2, relicInfo.nRelicsID);
				statement.setInt(3, relicInfo.nLevel);
				statement.setInt(4, relicInfo.nCount);
				statement.execute();
			}
		}
		catch(Exception e)
		{
			LOGGER.error("AccountRelicsDAO.saveOrUpdate(Player, RelicsInfo): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public Map<Integer, RelicsInfo> restore(Player owner)
	{
		Map<Integer, RelicsInfo> relics = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setString(1, owner.getAccountName());
			rs = statement.executeQuery();
			while(rs.next())
			{
				int relic_id = rs.getInt("relic_id");
				relics.put(relic_id, new RelicsInfo(relic_id, rs.getInt("level"), rs.getInt("count")));
			}
		}
		catch(Exception e)
		{
			LOGGER.error("AccountRelicsDAO.restore(Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
		return relics;
	}
}
