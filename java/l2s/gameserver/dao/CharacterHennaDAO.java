package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dao.JdbcEntityState;
import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;

public class CharacterHennaDAO
{
	private static final String SELECT_QUERY = "SELECT * FROM character_hennas WHERE char_obj_id=? AND class_index=?";
	private static final String INSERT_QUERY = "INSERT INTO character_hennas (char_obj_id, class_index, slot, symbol_id, enchant_exp, potential_id) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE symbol_id=?, enchant_exp=?, potential_id=?";
	private static final String UPDATE_QUERY = "UPDATE character_hennas SET symbol_id=?, enchant_exp=?, potential_id=? WHERE char_obj_id=? AND class_index=? AND slot=?";
	private static final String DELETE_QUERY = "DELETE FROM character_hennas WHERE char_obj_id=? AND class_index=? AND slot=?";

	private static final Logger LOGGER = LoggerFactory.getLogger(CharacterHennaDAO.class);

	private static final CharacterHennaDAO INSTANCE = new CharacterHennaDAO();

	public static CharacterHennaDAO getInstance()
	{
		return INSTANCE;
	}

	public void select(Player owner, Map<Integer, Henna> hennas)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setInt(1, owner.getObjectId());
			statement.setInt(2, owner.getActiveClassId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				Henna henna = hennas.get(rset.getInt("slot"));
				henna.setTemplate(HennaHolder.getInstance().getHenna(rset.getInt("symbol_id")));
				henna.setEnchantExp(rset.getInt("enchant_exp"));
				henna.setPotentialId(rset.getInt("potential_id"));
				henna.setJdbcState(JdbcEntityState.STORED);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("CharacterHennaDAO.select(Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public boolean saveOrUpdate(Henna henna)
	{
		if (henna.getJdbcState().isSavable())
			return save(henna);
		else if (henna.getJdbcState().isUpdatable())
			return update(henna);
		return false;
	}

	private boolean save(Henna henna)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_QUERY);
			int i = 0;
			statement.setInt(++i, henna.getOwner().getObjectId());
			statement.setInt(++i, henna.getOwner().getActiveClassId());
			statement.setInt(++i, henna.getSlot());
			statement.setInt(++i, henna.getId());
			statement.setInt(++i, henna.getEnchantExp());
			statement.setInt(++i, henna.getPotentialId());
			//
			statement.setInt(++i, henna.getId());
			statement.setInt(++i, henna.getEnchantExp());
			statement.setInt(++i, henna.getPotentialId());
			statement.execute();
		}
		catch (Exception e)
		{
			LOGGER.warn(henna.getOwner().getHennaList() + " could not add henna to henna list: " + henna, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	private boolean update(Henna henna)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_QUERY);
			int i = 0;
			statement.setInt(++i, henna.getId());
			statement.setInt(++i, henna.getEnchantExp());
			statement.setInt(++i, henna.getPotentialId());
			statement.setInt(++i, henna.getOwner().getObjectId());
			statement.setInt(++i, henna.getOwner().getActiveClassId());
			statement.setInt(++i, henna.getSlot());
			statement.execute();
		}
		catch (Exception e)
		{
			LOGGER.warn(henna.getOwner().getHennaList() + " could not add henna to henna list: " + henna, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public boolean delete(Henna henna)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setInt(1, henna.getOwner().getObjectId());
			statement.setInt(2, henna.getOwner().getActiveClassId());
			statement.setInt(3, henna.getSlot());
			statement.execute();
		}
		catch (Exception e)
		{
			LOGGER.warn(henna.getOwner().getHennaList() + " could not delete henna from slot: " + henna, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
}
