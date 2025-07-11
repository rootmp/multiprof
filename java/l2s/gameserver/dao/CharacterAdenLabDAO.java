package l2s.gameserver.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.templates.adenLab.AdenLabData;

public class CharacterAdenLabDAO
{
	private static final Logger _log = LoggerFactory.getLogger(CharacterAdenLabDAO.class);

	private static final CharacterAdenLabDAO _instance = new CharacterAdenLabDAO();

	private static final String SELECT_QUERY = "SELECT * FROM character_adenlab WHERE char_id=?";
	private static final String SELECT_SPECIAL_SLOTS_QUERY = "SELECT slot, option1_level, option2_level FROM character_adenlab_special WHERE char_id=? AND boss_id=?";
	private static final String UPSERT_QUERY = "INSERT INTO character_adenlab (char_Id, bossID, currentSlot, openCards, transcendEnchant, normalGameSaleDailyCount, normalGameDailyCount) "
			+
			"VALUES (?, ?, ?, ?, ?, ?, ?) " +
			"ON DUPLICATE KEY UPDATE " +
			"currentSlot = VALUES(currentSlot), " +
			"openCards = VALUES(openCards), " +
			"transcendEnchant = VALUES(transcendEnchant), " +
			"normalGameSaleDailyCount = VALUES(normalGameSaleDailyCount), " +
			"normalGameDailyCount = VALUES(normalGameDailyCount)";

	private static final String UPSERT_SPECIAL_SLOT_QUERY = "INSERT INTO character_adenlab_special (char_id, boss_id, slot, option1_level, option2_level) "
			+
			"VALUES (?, ?, ?, ?, ?) " +
			"ON DUPLICATE KEY UPDATE " +
			"option1_level = VALUES(option1_level), " +
			"option2_level = VALUES(option2_level)";

	public static CharacterAdenLabDAO getInstance()
	{
		return _instance;
	}

	public Map<Integer, AdenLabData> load(int charId)
	{
		Map<Integer, AdenLabData> data = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setInt(1, charId);
			rset = statement.executeQuery();

			while(rset.next())
			{
				int bossId = rset.getInt("bossID");
				int currentSlot = rset.getInt("currentSlot");
				int openCards = rset.getInt("openCards");
				int transcendEnchant = rset.getInt("transcendEnchant");
				int normalGameSaleDailyCount = rset.getInt("normalGameSaleDailyCount");
				int normalGameDailyCount = rset.getInt("normalGameDailyCount");

				// Загружаем специальные слоты
				Map<Integer, int[]> specialSlot = loadSpecialSlots(charId, bossId);

				AdenLabData adenLabData = new AdenLabData(bossId, currentSlot, openCards, transcendEnchant, normalGameSaleDailyCount, normalGameDailyCount, specialSlot);

				data.put(bossId, adenLabData);
			}
		}
		catch(Exception e)
		{
			_log.error("CharacterAdenLabDAO.load(int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return data;
	}

	private Map<Integer, int[]> loadSpecialSlots(int charId, int bossId)
	{
		Map<Integer, int[]> specialSlots = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SPECIAL_SLOTS_QUERY);
			statement.setInt(1, charId);
			statement.setInt(2, bossId);
			rset = statement.executeQuery();

			while(rset.next())
			{
				int slotId = rset.getInt("slot");
				int option1Level = rset.getInt("option1_level");
				int option2Level = rset.getInt("option2_level");
				specialSlots.put(slotId, new int[] {option1Level, option2Level});
			}
		}
		catch(Exception e)
		{
			_log.error("CharacterAdenLabDAO.loadSpecialSlots(int, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return specialSlots;
	}

	public void save(int charId, AdenLabData data)
	{
		Connection con = null;
		PreparedStatement statement = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();

			statement = con.prepareStatement(UPSERT_QUERY);
			statement.setInt(1, charId);
			statement.setInt(2, data.getBossID());
			statement.setInt(3, data.getCurrentSlot());
			statement.setInt(4, data.getOpenCards());
			statement.setInt(5, data.getTranscendEnchant());
			statement.setInt(6, data.getNormalGameSaleDailyCount());
			statement.setInt(7, data.getNormalGameDailyCount());
			statement.execute();
			DbUtils.closeQuietly(statement);

			Map<Integer, int[]> specialSlots = data.getSpecialSlots();
			if(specialSlots != null && !specialSlots.isEmpty())
			{
				statement = con.prepareStatement(UPSERT_SPECIAL_SLOT_QUERY);
				for(Map.Entry<Integer, int[]> entry : specialSlots.entrySet())
				{
					int slotId = entry.getKey();
					int[] options = entry.getValue();
					int option1Level = options.length > 0 ? options[0] : 0;
					int option2Level = options.length > 1 ? options[1] : 0;

					statement.setInt(1, charId);
					statement.setInt(2, data.getBossID());
					statement.setInt(3, slotId);
					statement.setInt(4, option1Level);
					statement.setInt(5, option2Level);
					statement.addBatch();
				}
				statement.executeBatch();
			}
		}
		catch(Exception e)
		{
			_log.error("CharacterAdenLabDAO.save(AdenLabData): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
