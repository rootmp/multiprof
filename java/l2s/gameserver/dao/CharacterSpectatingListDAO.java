package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Spectating;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author nexvill
 */
public class CharacterSpectatingListDAO
{
	private static final Logger _log = LoggerFactory.getLogger(CharacterSpectatingListDAO.class);

	private static final CharacterSpectatingListDAO _instance = new CharacterSpectatingListDAO();

	public static CharacterSpectatingListDAO getInstance()
	{
		return _instance;
	}

	public TIntObjectMap<Spectating> select(Player owner)
	{
		TIntObjectMap<Spectating> map = new TIntObjectHashMap<Spectating>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT s.target_Id, s.char_name, cs.class_id, cs.level FROM character_spectatinglist s LEFT JOIN characters c ON s.target_Id = c.obj_Id LEFT JOIN character_subclasses cs ON (s.target_id = cs.char_obj_id AND cs.active = 1) WHERE s.obj_Id = ?");
			statement.setInt(1, owner.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				String name = rset.getString("s.char_name");
				if (name == null)
					continue;

				int objectId = rset.getInt("s.target_Id");
				int classId = rset.getInt("cs.class_id");
				int level = rset.getInt("cs.level");

				map.put(objectId, new Spectating(objectId, name, classId, level));
			}
		}
		catch (Exception e)
		{
			_log.error("CharacterSpectatingListDAO.select(L2Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return map;
	}

	public void insert(Player owner, int objId, String name)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO character_spectatinglist (obj_Id,target_Id, char_name) VALUES(?,?,?)");
			statement.setInt(1, owner.getObjectId());
			statement.setInt(2, objId);
			statement.setString(3, name);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn(owner.getSpectatingList() + " could not add player to spectating list objectid: " + objId, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void delete(Player owner, int spectatingObjectId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_spectatinglist WHERE obj_Id=? AND target_Id=?");
			statement.setInt(1, owner.getObjectId());
			statement.setInt(2, spectatingObjectId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn(owner.getSpectatingList() + " could not delete spectating objectId: " + spectatingObjectId + " ownerId: " + owner.getObjectId(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public TIntObjectMap<Spectating> getCharDataByName(String name)
	{
		TIntObjectMap<Spectating> result = new TIntObjectHashMap<Spectating>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT cs.char_obj_id, cs.class_id, cs.level FROM character_subclasses cs LEFT JOIN CHARACTERS c ON cs.char_obj_id = c.obj_Id WHERE c.char_name = ?");
			statement.setString(1, name);
			rset = statement.executeQuery();
			if (rset.next())
			{
				int objId = rset.getInt("cs.char_obj_id");
				int classId = rset.getInt("cs.class_id");
				int level = rset.getInt("cs.level");
				result.put(objId, new Spectating(objId, name, classId, level));
			}
		}
		catch (Exception e)
		{
			_log.error("CharacterSpectatingListDAO.getCharDataByName(String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return result;
	}
}
