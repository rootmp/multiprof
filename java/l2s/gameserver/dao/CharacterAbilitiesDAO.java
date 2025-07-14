package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.enums.AbilitiesScheme;

public class CharacterAbilitiesDAO
{
	private static final Logger _log = LoggerFactory.getLogger(CharacterAbilitiesDAO.class);

	private static final CharacterAbilitiesDAO _instance = new CharacterAbilitiesDAO();

	public static CharacterAbilitiesDAO getInstance()
	{
		return _instance;
	}

	public String[] restore(int charId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		String[] data = new String[4];
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM character_abilities WHERE char_id=?");
			statement.setInt(1, charId);
			rset = statement.executeQuery();
			if(rset.next())
			{
				data[0] = rset.getString("a");
				data[1] = rset.getString("b");
				data[2] = rset.getString("active");
				data[3] = String.valueOf(rset.getInt("points"));
			}
			else
			{
				data[0] = "";
				data[1] = "";
				data[2] = "A";
				data[3] = "0";
			}
		}
		catch(SQLException e)
		{
			_log.error("CharacterAbilitiesDAO.restore(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return data;
	}

	public boolean save(int charId, String aSkills, String bSkills, AbilitiesScheme activeScheme, int points)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO character_abilities (char_id, a, b, active, points) VALUES (?, ?, ?, ?, ?)");
			statement.setInt(1, charId);
			statement.setString(2, aSkills);
			statement.setString(3, bSkills);
			statement.setString(4, activeScheme.name());
			statement.setInt(5, points);
			statement.executeUpdate();
		}
		catch(SQLException e)
		{
			_log.warn("CharacterAbilitiesDAO.save(): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public void delete(int charId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_abilities WHERE char_id=?");
			statement.setInt(1, charId);
			statement.execute();
		}
		catch(SQLException e)
		{
			_log.warn("CharacterAbilitiesDAO.delete(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void updateActiveScheme(int charId, AbilitiesScheme scheme)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE character_abilities SET active=? WHERE char_id=?");
			statement.setString(1, scheme.name());
			statement.setInt(2, charId);
			statement.executeUpdate();
		}
		catch(SQLException e)
		{
			_log.warn("CharacterAbilitiesDAO.updateActiveScheme(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void updatePoints(int charId, int points)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE character_abilities SET points=? WHERE char_id=?");
			statement.setInt(1, points);
			statement.setInt(2, charId);
			statement.executeUpdate();
		}
		catch(SQLException e)
		{
			_log.warn("CharacterAbilitiesDAO.updatePoints(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
