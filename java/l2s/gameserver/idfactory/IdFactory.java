package l2s.gameserver.idfactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;

public abstract class IdFactory
{
	private static final Logger _log = LoggerFactory.getLogger(IdFactory.class);

	public static final String[][] EXTRACT_OBJ_ID_TABLES = {
			{
					"characters",
					"obj_id"
			},
			{
					"items",
					"object_id"
			},
			{
					"clan_data",
					"clan_id"
			},
			{
					"ally_data",
					"ally_id"
			},
			{
					"pets",
					"objId"
			},
			{
					"couples",
					"id"
			},
			{
					"fences",
					"object_id"
			}
	};

	public static final int FIRST_OID = 0x10000000;
	public static final int LAST_OID = 0x7FFFFFFF;
	public static final int FREE_OBJECT_ID_SIZE = LAST_OID - FIRST_OID;

	protected static final IdFactory _instance = new BitSetIDFactory();

	public static final IdFactory getInstance()
	{
		return _instance;
	}

	protected boolean initialized;
	protected long releasedCount = 0;

	protected IdFactory()
	{
		resetOnlineStatus();
		globalRemoveItems(); // Удаляем глобально итемы, перед рестором Object Id's
		cleanUpDB();
	}

	private void resetOnlineStatus()
	{
		Connection con = null;
		Statement st = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			st = con.createStatement();
			st.executeUpdate("UPDATE characters SET online = 0");
			_log.info("IdFactory: Clear characters online status.");
		}
		catch(final SQLException e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, st);
		}
	}

	private void globalRemoveItems()
	{
		int itemToDeleteCount = 0;
		StringBuilder itemsToDelete = new StringBuilder();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT item_id FROM items_to_delete");
			rset = statement.executeQuery();
			while(rset.next())
			{
				if(itemsToDelete.length() > 0)
				{
					itemsToDelete.append(",");
				}
				itemsToDelete.append(rset.getInt("item_id"));
				++itemToDeleteCount;
			}

			DbUtils.closeQuietly(statement, rset);

			statement = con.prepareStatement("DELETE FROM items_to_delete");
			statement.execute();
		}
		catch(final SQLException e)
		{
			_log.error("Error while select items for global remove: ", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}

		if(itemsToDelete.length() > 0)
		{
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("DELETE FROM items WHERE item_id IN (?)");
				statement.setString(1, itemsToDelete.toString());
				statement.execute();
				DbUtils.closeQuietly(statement);
				statement = con.prepareStatement("DELETE FROM items_delayed WHERE item_id IN (?)");
				statement.setString(1, itemsToDelete.toString());
				statement.execute();
			}
			catch(SQLException e)
			{
				_log.error("Error while global remove items:", e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
			_log.info("IdFactory: Global removed {} items.", itemToDeleteCount);
		}
	}

	private void cleanUpDB()
	{
		Connection con = null;
		Statement st = null;
		try
		{
			final long cleanupStart = System.currentTimeMillis();
			int cleanCount = 0;
			con = DatabaseFactory.getInstance().getConnection();
			st = con.createStatement();

			// Чистим по аккаунту.
			cleanCount += st.executeUpdate("DELETE FROM premium_accounts WHERE premium_accounts.account NOT IN (SELECT account_name FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM account_variables WHERE account_variables.account_name NOT IN (SELECT account_name FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM bbs_memo WHERE bbs_memo.account_name NOT IN (SELECT account_name FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM product_history WHERE product_history.account_name NOT IN (SELECT account_name FROM characters);");

			// Чистим по Object ID персонажа.
			cleanCount += st.executeUpdate("DELETE FROM character_blocklist WHERE character_blocklist.obj_Id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_blocklist WHERE character_blocklist.target_Id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_bookmarks WHERE character_bookmarks.char_Id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_effects_save WHERE character_effects_save.object_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_elementals WHERE character_elementals.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_friends WHERE character_friends.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_friends WHERE character_friends.friend_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_group_reuse WHERE character_group_reuse.object_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_hennas WHERE character_hennas.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_mission_level_reward WHERE character_mission_level_reward.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_instances WHERE character_instances.obj_Id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_macroses WHERE character_macroses.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_minigame_score WHERE character_minigame_score.object_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_private_buys WHERE character_private_buys.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_private_manufactures WHERE character_private_manufactures.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_private_sells WHERE character_private_sells.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_post_friends WHERE character_post_friends.object_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_post_friends WHERE character_post_friends.post_friend NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_quests WHERE character_quests.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_recipebook WHERE character_recipebook.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_shortcuts WHERE character_shortcuts.object_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_skills WHERE character_skills.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_skills_save WHERE character_skills_save.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_subclasses WHERE character_subclasses.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_teleports WHERE character_teleports.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_training_camp WHERE character_training_camp.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_variables WHERE character_variables.obj_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM siege_players WHERE siege_players.object_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM olympiad_participants WHERE olympiad_participants.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM bans WHERE bans.obj_Id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM bbs_favorites WHERE bbs_favorites.object_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM bbs_mail WHERE bbs_mail.to_object_id NOT IN (SELECT obj_Id FROM characters);");
			// Не нужно вроде: cleanCount += st.executeUpdate("DELETE FROM bbs_mail WHERE
			// bbs_mail.from_object_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM couples WHERE couples.player1Id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM couples WHERE couples.player2Id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM event_data WHERE event_data.charId NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM heroes WHERE heroes.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM custom_heroes WHERE custom_heroes.hero_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM heroes_diary WHERE heroes_diary.charId NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM clan_leader_request WHERE clan_leader_request.new_leader_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_summons_save WHERE character_summons_save.owner_obj_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM bot_reported_char_data WHERE bot_reported_char_data.botId NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM bot_reported_char_data WHERE bot_reported_char_data.reporterId NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM bbs_teleport_bm WHERE bbs_teleport_bm.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM pvp_system_log WHERE pvp_system_log.killer NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM pvp_system_log WHERE pvp_system_log.victim NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM npcbuffer_scheme_list WHERE npcbuffer_scheme_list.player_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM clan_search_clan_applicants WHERE clan_search_clan_applicants.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM clan_search_waiting_players WHERE clan_search_waiting_players.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM olympiad_history WHERE olympiad_history.object_id_1 NOT IN (SELECT obj_Id FROM characters) AND olympiad_history.object_id_2 NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM enchant_broken_items WHERE enchant_broken_items.char_id NOT IN (SELECT obj_Id FROM characters);");

			// Чистим итемы.
			cleanCount += st.executeUpdate("DELETE FROM items_delayed WHERE items_delayed.owner_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM pets WHERE pets.item_obj_id NOT IN (SELECT object_id FROM items);");
			cleanCount += st.executeUpdate("DELETE FROM items_ensoul WHERE items_ensoul.object_id NOT IN (SELECT object_id FROM items);");
			cleanCount += st.executeUpdate("DELETE FROM items_to_restore_ensoul WHERE items_to_restore_ensoul.object_id NOT IN (SELECT object_id FROM items_to_restore);");
			cleanCount += st.executeUpdate("DELETE FROM hidden_items WHERE hidden_items.obj_id NOT IN (SELECT object_id FROM items);");
			cleanCount += st.executeUpdate("DELETE FROM character_private_sells WHERE character_private_sells.item_object_id NOT IN (SELECT object_id FROM items);");

			// Чистим кланы и альянсы.
			cleanCount += st.executeUpdate("DELETE FROM clan_data WHERE clan_data.clan_id NOT IN (SELECT clanid FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM clan_subpledges WHERE clan_subpledges.type = 0 AND clan_subpledges.leader_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM clan_data WHERE clan_data.clan_id NOT IN (SELECT clan_id FROM clan_subpledges WHERE clan_subpledges.type = 0);");
			cleanCount += st.executeUpdate("DELETE FROM clan_subpledges WHERE clan_subpledges.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_subpledges_skills WHERE clan_subpledges_skills.clan_id NOT IN (SELECT clan_id FROM clan_subpledges);");
			cleanCount += st.executeUpdate("DELETE FROM ally_data WHERE ally_data.leader_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM ally_data WHERE ally_data.ally_id NOT IN (SELECT ally_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM bbs_clannotice WHERE bbs_clannotice.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_privs WHERE clan_privs.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_skills WHERE clan_skills.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_subpledges WHERE clan_subpledges.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_wars WHERE clan_wars.attacker_clan NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_wars WHERE clan_wars.attacked_clan NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM siege_players WHERE siege_players.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM siege_clans WHERE siege_clans.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_largecrests WHERE clan_largecrests.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_search_registered_clans WHERE clan_search_registered_clans.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM instant_clanhall_owners WHERE instant_clanhall_owners.owner_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("DELETE FROM clan_leader_request WHERE clan_leader_request.clan_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("UPDATE castle SET castle.owner_id=0 WHERE castle.owner_id NOT IN (SELECT clan_id FROM clan_data);");
			cleanCount += st.executeUpdate("UPDATE clanhall SET clanhall.owner_id=0 WHERE clanhall.owner_id NOT IN (SELECT clan_id FROM clan_data);");

			// Чистим почту.
			cleanCount += st.executeUpdate("DELETE FROM character_mail WHERE character_mail.char_id NOT IN (SELECT obj_Id FROM characters);");
			cleanCount += st.executeUpdate("DELETE FROM character_mail WHERE character_mail.message_id NOT IN (SELECT message_id FROM mail);");
			cleanCount += st.executeUpdate("DELETE FROM mail WHERE mail.message_id NOT IN (SELECT message_id FROM character_mail);");
			cleanCount += st.executeUpdate("DELETE FROM mail_attachments WHERE mail_attachments.message_id NOT IN (SELECT message_id FROM mail);");

			st.executeUpdate("UPDATE clan_data SET ally_id = '0' WHERE clan_data.ally_id NOT IN (SELECT ally_id FROM ally_data);");
			st.executeUpdate("UPDATE clan_subpledges SET leader_id=0 WHERE leader_id > 0 AND clan_subpledges.leader_id NOT IN (SELECT obj_Id FROM characters);");
			st.executeUpdate("UPDATE characters SET clanid = '0', title = '', pledge_type = '0', pledge_rank = '0', lvl_joined_academy = '0', apprentice = '0' WHERE characters.clanid > 0 AND characters.clanid NOT IN (SELECT clan_id FROM clan_data);");
			st.executeUpdate("UPDATE items SET loc = 'WAREHOUSE' WHERE loc = 'MAIL' AND items.object_id NOT IN (SELECT item_id FROM mail_attachments);");
			st.executeUpdate("DELETE FROM server_variables WHERE name LIKE '%tpId%'");

			_log.info("IdFactory: Cleaned " + cleanCount + " elements from database in " + ((System.currentTimeMillis() - cleanupStart) / 1000) + "sec.");
		}
		catch(final SQLException e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, st);
		}
	}

	protected Integer[] extractUsedObjectIDTable() throws SQLException
	{
		final List<Integer> objectIds = new ArrayList<>();

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			st = con.createStatement();
			for(final String[] table : EXTRACT_OBJ_ID_TABLES)
			{
				rs = st.executeQuery("SELECT " + table[1] + " FROM " + table[0]);
				int size = objectIds.size();
				while(rs.next())
				{
					objectIds.add(rs.getInt(1));
				}

				DbUtils.close(rs);

				size = objectIds.size() - size;
				if(size > 0)
				{
					_log.info("IdFactory: Extracted " + size + " used id's from " + table[0]);
				}
			}
		}
		finally
		{
			DbUtils.closeQuietly(con, st, rs);
		}

		Collections.sort(objectIds);

		_log.info("IdFactory: Extracted total " + objectIds.size() + " used id's.");

		return objectIds.toArray(new Integer[objectIds.size()]);
	}

	public boolean isInitialized()
	{
		return initialized;
	}

	public abstract int getNextId();

	/**
	 * return a used Object ID back to the pool
	 * 
	 * @param object ID
	 */
	public void releaseId(int id)
	{
		releasedCount++;
	}

	public long getReleasedCount()
	{
		return releasedCount;
	}

	public abstract int size();
}