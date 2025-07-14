package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.dataparser.data.holder.DyeDataHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.henna.Henna;
import l2s.gameserver.templates.item.henna.HennaPoten;

public class CharacterHennaDAO
{
	private static final String RESTORE_CHAR_HENNAS = "SELECT slot,symbol_id FROM character_hennas WHERE charId=? AND class_index=?";
	private static final String ADD_CHAR_HENNA = "INSERT INTO character_hennas (charId,symbol_id,slot,class_index) VALUES (?,?,?,?)";
	private static final String DELETE_CHAR_HENNA = "DELETE FROM character_hennas WHERE charId=? AND slot=? AND class_index=?";

	private static final String ADD_CHAR_HENNA_POTENS = "REPLACE INTO character_potens (charId,enchant_level,enchant_exp,poten_id,slot_id) VALUES (?,?,?,?,?)";
	private static final String RESTORE_CHAR_HENNAS_POTENS = "SELECT poten_id,enchant_level,enchant_exp,slot_id FROM character_potens WHERE charId=?";

	private static final Logger _log = LoggerFactory.getLogger(CharacterHennaDAO.class);

	private static final CharacterHennaDAO _instance = new CharacterHennaDAO();

	public static CharacterHennaDAO getInstance()
	{
		return _instance;
	}

	public HennaPoten[] restoreHenna(Player player)
	{
		HennaPoten[] _hennaPoten = new HennaPoten[4];
		for(int i = 1; i < 5; i++)
			_hennaPoten[i - 1] = new HennaPoten();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_CHAR_HENNAS_POTENS);
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();

			while(rset.next())
			{
				int slot_id = rset.getInt("slot_id");
				_hennaPoten[slot_id - 1].setPotenId(rset.getInt("poten_id"));
				_hennaPoten[slot_id - 1].setEnchantLevel(rset.getInt("enchant_level"));
				_hennaPoten[slot_id - 1].setEnchantExp(rset.getInt("enchant_exp"));
			}
		}
		catch(Exception e)
		{
			_log.error("Failed restore character henna Potential.", e);
		}
		finally
		{
			DbUtils.closeQuietly(statement, rset);
		}

		try
		{
			statement = con.prepareStatement(RESTORE_CHAR_HENNAS);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, player.getActiveClassId());
			rset = statement.executeQuery();

			int slot;
			int symbolId;
			while(rset.next())
			{
				slot = rset.getInt("slot");
				if((slot < 1) || (slot > player.getAvailableHennaSlots()))
					continue;

				symbolId = rset.getInt("symbol_id");
				if(symbolId == 0)
					continue;

				final Henna henna = DyeDataHolder.getInstance().getHennaByDyeId(symbolId);
				_hennaPoten[slot - 1].setHenna(henna);
			}
		}
		catch(Exception e)
		{
			_log.error("Failed restoing character " + this + " hennas.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return _hennaPoten;
	}

	public void removeHenna(Player player, int slot)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_CHAR_HENNA);

			statement.setInt(1, player.getObjectId());
			statement.setInt(2, slot);
			statement.setInt(3, player.getActiveClassId());
			statement.execute();
		}
		catch(Exception e)
		{
			_log.error("Failed removing character henna.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void addHenna(Player player, Henna henna, int slotId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(ADD_CHAR_HENNA);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, henna.getDyeId());
			statement.setInt(3, slotId);
			statement.setInt(4, player.getActiveClassId());
			statement.execute();
		}
		catch(Exception e)
		{
			_log.error("Failed saving character henna.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void storeDyePoten(Player player, HennaPoten hennaPoten, int _slotId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(ADD_CHAR_HENNA_POTENS);

			statement.setInt(1, player.getObjectId());
			statement.setInt(2, hennaPoten.getEnchantLevel());
			statement.setInt(3, hennaPoten.getEnchantExp());
			statement.setInt(4, hennaPoten.getPotenId());
			statement.setInt(5, _slotId);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.error("Failed saving character henna Potential.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
}
