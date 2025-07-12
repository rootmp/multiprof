package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.skill.enchant.SkillEnchantInfo;

public class CharacterSkillEnchantDAO
{
	private static final Logger _log = LoggerFactory.getLogger(CharacterSkillEnchantDAO.class);

	private static final CharacterSkillEnchantDAO _instance = new CharacterSkillEnchantDAO();

	private static final String SELECT_QUERY = "SELECT skill_Id, sub_level, exp FROM character_skill_enchant WHERE obj_Id = ?";

	private static final String REPLACE_QUERY = "REPLACE INTO character_skill_enchant (obj_Id, skill_Id, sub_level, exp) VALUES (?, ?, ?, ?)";

	public static CharacterSkillEnchantDAO getInstance()
	{
		return _instance;
	}

	public List<SkillEnchantInfo> restore(Player owner)
	{
		List<SkillEnchantInfo> skillEnchantList = new ArrayList<>();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setInt(1, owner.getObjectId());
			rset = statement.executeQuery();

			while(rset.next())
			{
				int skillID = rset.getInt("skill_Id");
				int subLevel = rset.getInt("sub_level");
				int exp = rset.getInt("exp");

				SkillEnchantInfo skillEnchantInfo = new SkillEnchantInfo(skillID, subLevel, exp);
				skillEnchantList.add(skillEnchantInfo);
			}
		}
		catch(Exception e)
		{
			_log.error("CharacterSkillEnchantDAO.select(Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return skillEnchantList;
	}

	public boolean store(Player owner, SkillEnchantInfo enchant)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REPLACE_QUERY);
			statement.setInt(1, owner.getObjectId());
			statement.setInt(2, enchant.getSkillID());
			statement.setInt(3, enchant.getSubLevel());
			statement.setInt(4, enchant.getEXP());

			statement.execute();
		}
		catch(Exception e)
		{
			_log.warn(enchant.toString() + " could not store enchant for character: " + owner.getName() + " : ", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
}
