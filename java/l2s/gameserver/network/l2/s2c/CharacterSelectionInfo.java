package l2s.gameserver.network.l2.s2c;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.CharSelectInfoPackage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.base.SubClassType;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.utils.AutoBan;

public class CharacterSelectionInfo extends L2GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(CharacterSelectionInfo.class);

	private final String _loginName;
	private final int _sessionId;
	private final CharSelectInfoPackage[] _characterPackages;
	private final boolean _hasPremiumAccount;

	public CharacterSelectionInfo(GameClient client)
	{
		_loginName = client.getLogin();
		_sessionId = client.getSessionKey().playOkID1;
		_characterPackages = loadCharacterSelectInfo(_loginName);
		_hasPremiumAccount = client.getPremiumAccountType() > 0 && client.getPremiumAccountExpire() > (System.currentTimeMillis() / 1000L);
	}

	public CharSelectInfoPackage[] getCharInfo()
	{
		return _characterPackages;
	}

	@Override
	protected final void writeImpl()
	{
		int size = _characterPackages != null ? _characterPackages.length : 0;

		writeD(size);
		writeD(Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT); // Максимальное количество персонажей на сервере
		writeC(size >= Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT); // 0x00 - Разрешить, 0x01 - запретить. Разрешает или
																	// запрещает создание игроков
		writeC(0x00);
		writeD(0x02); // 0x01 - Выводит окно, что нужно купить игру, что создавать более 2х чаров.
						// 0х02 - обычное лобби.
		writeC(0x00); // 0x01 - Предлогает купить ПА.
		writeC(0x00); // Balthus Knights

		long lastAccess = -1L;
		int lastUsed = -1;
		for (int i = 0; i < size; i++)
			if (lastAccess < _characterPackages[i].getLastAccess())
			{
				lastAccess = _characterPackages[i].getLastAccess();
				lastUsed = i;
			}

		for (int i = 0; i < size; i++)
		{
			CharSelectInfoPackage charInfoPackage = _characterPackages[i];

			writeS(charInfoPackage.getName(false));
			writeD(charInfoPackage.getCharId()); // ?
			writeS(_loginName);
			writeD(_sessionId);
			writeD(charInfoPackage.getClanId());
			writeD(0x00); // Builder level

			writeD(charInfoPackage.getSex());
			writeD(charInfoPackage.getRace());
			writeD(charInfoPackage.getBaseClassId());

			writeD(Config.REQUEST_ID);

			writeD(charInfoPackage.getX());
			writeD(charInfoPackage.getY());
			writeD(charInfoPackage.getZ());

			writeF(charInfoPackage.getCurrentHp());
			writeF(charInfoPackage.getCurrentMp());

			writeQ(charInfoPackage.getSp());
			writeQ(charInfoPackage.getExp());
			int lvl = Experience.getLevel(charInfoPackage.getExp());
			writeF(Experience.getExpPercent(lvl, charInfoPackage.getExp()));
			writeD(lvl);

			writeD(charInfoPackage.getKarma());
			writeD(charInfoPackage.getPk());
			writeD(charInfoPackage.getPvP());

			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);

			writeD(0x00); // unk Ertheia
			writeD(0x00); // unk Ertheia

			for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
				writeD(charInfoPackage.getPaperdollItemId(PAPERDOLL_ID));

			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_RHAND)); // Внешний вид оружия (ИД Итема).
			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_LHAND)); // Внешний вид щита (ИД Итема).
			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_GLOVES)); // Внешний вид перчаток (ИД
																						// Итема).
			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_CHEST)); // Внешний вид верха (ИД Итема).
			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_LEGS)); // Внешний вид низа (ИД Итема).
			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_FEET)); // Внешний вид ботинок (ИД Итема).
			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_LRHAND)); // ???
			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_HAIR)); // Внешний вид шляпы (ИД итема).
			writeD(charInfoPackage.getPaperdollVisualId(Inventory.PAPERDOLL_DHAIR)); // Внешний вид маски (ИД итема).

			writeH(charInfoPackage.getPaperdollEnchantEffect(Inventory.PAPERDOLL_CHEST));
			writeH(charInfoPackage.getPaperdollEnchantEffect(Inventory.PAPERDOLL_LEGS));
			writeH(charInfoPackage.getPaperdollEnchantEffect(Inventory.PAPERDOLL_HEAD));
			writeH(charInfoPackage.getPaperdollEnchantEffect(Inventory.PAPERDOLL_GLOVES));
			writeH(charInfoPackage.getPaperdollEnchantEffect(Inventory.PAPERDOLL_FEET));

			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HAIR) > 0 ? charInfoPackage.getSex() : charInfoPackage.getHairStyle());
			writeD(charInfoPackage.getHairColor());
			writeD(charInfoPackage.getFace());

			writeF(charInfoPackage.getMaxHp()); // hp max
			writeF(charInfoPackage.getMaxMp()); // mp max

			writeD(charInfoPackage.getAccessLevel() > -100 ? charInfoPackage.getDeleteTimer() : -1);
			writeD(charInfoPackage.getClassId());
			writeD(i == lastUsed ? 1 : 0);

			writeC(Math.min(charInfoPackage.getPaperdollEnchantEffect(Inventory.PAPERDOLL_RHAND), 127));
			writeD(charInfoPackage.getPaperdollVariation1Id(Inventory.PAPERDOLL_RHAND));
			writeD(charInfoPackage.getPaperdollVariation2Id(Inventory.PAPERDOLL_RHAND));
			writeD(0x00); // Transform ID

			// TODO: Pet info?
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeF(0x00);
			writeF(0x00);

			writeD(charInfoPackage.getSayhasGrace()); // Vitality Points
			if (_hasPremiumAccount)
				writeD(charInfoPackage.getSayhasGrace() > 0 ? (int) (100 * Config.ALT_SAYHAS_GRACE_PA_RATE) : 0);
			else
				writeD(charInfoPackage.getSayhasGrace() > 0 ? (int) (100 * Config.ALT_SAYHAS_GRACE_RATE) : 0);
			writeD(charInfoPackage.getVitalityItemsUsed()); // Use Vitality Potions Left
			writeD(charInfoPackage.isAvailable());
			writeC(0x00); // Chaos Festival Winner
			writeC(charInfoPackage.isHero()); // hero glow
			writeC(charInfoPackage.isHairAccessoryEnabled() ? 0x01 : 0x00); // show hair accessory if enabled
			if (charInfoPackage.getAccessLevel() > -100)
			{
				writeD(0x00);
				writeD((int) (charInfoPackage.getLastLoginTime() / 1000)); // last login time
			}
			else
			{
				writeD(0x01);
				writeD(0x00); // TODO: unban time
			}
			writeD(0); // unk 338
			writeC(0); // unk 338

			int specialMountId = 0;
			if (charInfoPackage.getBaseClassId() == 217)
			{
				if (haveLearnedSkill(charInfoPackage.getObjectId(), 47865)) // wild evolution
				{
					specialMountId = 4;
				}
				else if ((charInfoPackage.getClassId() == 217) || (charInfoPackage.getClassId() == 218))
				{
					specialMountId = 1;
				}
				else if (charInfoPackage.getClassId() == 219)
				{
					specialMountId = 2;
				}
				else if (charInfoPackage.getClassId() == 220)
				{
					specialMountId = 3;
				}
			}
			writeC(specialMountId); // special mount type for vanguard riders
		}
	}

	private boolean haveLearnedSkill(int charId, int skillId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT skill_id FROM character_skills WHERE char_obj_id=?");
			statement.setInt(1, charId);
			rset = statement.executeQuery();

			while (rset.next())
			{
				if (rset.getInt("skill_id") == skillId)
				{
					return true;
				}
			}
		}
		catch (final Exception e)
		{
			_log.warn("Could not restore skills for player objId: " + charId);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return false;
	}

	public static CharSelectInfoPackage[] loadCharacterSelectInfo(String loginName)
	{
		CharSelectInfoPackage charInfopackage;
		List<CharSelectInfoPackage> characterList = new ArrayList<CharSelectInfoPackage>();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM characters AS c LEFT JOIN character_subclasses AS cs ON (c.obj_Id=cs.char_obj_id AND cs.active=1) WHERE account_name=? ORDER BY createtime LIMIT " + Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT);
			statement.setString(1, loginName);
			rset = statement.executeQuery();
			while (rset.next()) // fills the package
			{
				charInfopackage = restoreChar(rset);
				if (charInfopackage != null)
					characterList.add(charInfopackage);
			}
		}
		catch (Exception e)
		{
			_log.error("could not restore charinfo:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return characterList.toArray(new CharSelectInfoPackage[characterList.size()]);
	}

	private static int restoreBaseClassId(int objId)
	{
		int classId = 0;

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT class_id FROM character_subclasses WHERE char_obj_id=? AND type=?");
			statement.setInt(1, objId);
			statement.setInt(2, SubClassType.BASE_CLASS.ordinal());
			rset = statement.executeQuery();
			while (rset.next())
			{
				classId = rset.getInt("class_id");
			}
		}
		catch (Exception e)
		{
			_log.error("could not restore base class id:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return classId;
	}

	private static String restoreChangedOldName(int objId)
	{
		String suffix = null;

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT value FROM character_variables WHERE obj_id=? AND name=? AND (expire_time=-1 OR expire_time>=?)");
			statement.setInt(1, objId);
			statement.setString(2, Player.CHANGED_OLD_NAME);
			statement.setLong(3, System.currentTimeMillis());
			rset = statement.executeQuery();
			if (rset.next())
			{
				suffix = rset.getString("value");
			}
		}
		catch (Exception e)
		{
			_log.error("could not restore changed old name:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return suffix;
	}

	private static CharSelectInfoPackage restoreChar(ResultSet chardata)
	{
		CharSelectInfoPackage charInfopackage = null;
		try
		{
			int objectId = chardata.getInt("obj_Id");
			int classid = chardata.getInt("class_id");
			int baseClassId = ClassId.valueOf(classid).getFirstParent(chardata.getInt("sex")).getId();

			Race race = ClassId.valueOf(baseClassId).getRace();
			if (race == null)
			{
				_log.warn(CharacterSelectionInfo.class.getSimpleName() + ": Race was not found for the class id: " + baseClassId);
				return null;
			}

			String name = chardata.getString("char_name");
			charInfopackage = new CharSelectInfoPackage(objectId, name);
			charInfopackage.setMaxHp(chardata.getInt("maxHp"));
			charInfopackage.setCurrentHp(chardata.getDouble("curHp"));
			charInfopackage.setMaxMp(chardata.getInt("maxMp"));
			charInfopackage.setCurrentMp(chardata.getDouble("curMp"));

			charInfopackage.setX(chardata.getInt("x"));
			charInfopackage.setY(chardata.getInt("y"));
			charInfopackage.setZ(chardata.getInt("z"));
			charInfopackage.setPk(chardata.getInt("pkkills"));
			charInfopackage.setPvP(chardata.getInt("pvpkills"));

			int face = chardata.getInt("beautyFace");
			charInfopackage.setFace(face > 0 ? face : chardata.getInt("face"));

			int hairstyle = chardata.getInt("beautyHairstyle");
			charInfopackage.setHairStyle(hairstyle > 0 ? hairstyle : chardata.getInt("hairstyle"));

			int haircolor = chardata.getInt("beautyHaircolor");
			charInfopackage.setHairColor(haircolor > 0 ? haircolor : chardata.getInt("haircolor"));

			charInfopackage.setSex(chardata.getInt("sex"));

			charInfopackage.setExp(chardata.getLong("exp"));
			charInfopackage.setSp(chardata.getLong("sp"));
			charInfopackage.setClanId(chardata.getInt("clanid"));

			charInfopackage.setKarma(chardata.getInt("karma"));
			charInfopackage.setRace(race.ordinal());
			charInfopackage.setClassId(classid);
			charInfopackage.setBaseClassId(baseClassId);
			long deletetime = chardata.getLong("deletetime");
			int deletehours = 0;
			if (Config.CHARACTER_DELETE_AFTER_HOURS > 0)
				if (deletetime > 0)
				{
					deletetime = (int) (System.currentTimeMillis() / 1000 - deletetime);
					deletehours = (int) (deletetime / 3600);
					if (deletehours >= Config.CHARACTER_DELETE_AFTER_HOURS)
					{
						CharacterDAO.getInstance().deleteCharByObjId(objectId);
						return null;
					}
					deletetime = Config.CHARACTER_DELETE_AFTER_HOURS * 3600 - deletetime;
				}
				else
					deletetime = 0;
			charInfopackage.setDeleteTimer((int) deletetime);
			charInfopackage.setLastAccess(chardata.getLong("lastAccess") * 1000L);
			charInfopackage.setAccessLevel(chardata.getInt("accesslevel"));

			charInfopackage.setHairAccessoryEnabled(chardata.getInt("hide_head_accessories") == 0);

			if (charInfopackage.getAccessLevel() < 0 && !AutoBan.isBanned(objectId))
				charInfopackage.setAccessLevel(0);

			charInfopackage.setSayhasGrace(chardata.getInt("sayhas_grace_points"));

			charInfopackage.setChangedOldName(restoreChangedOldName(objectId));
			charInfopackage.setLastLoginTime(chardata.getLong("last_login"));
		}
		catch (Exception e)
		{
			_log.error("", e);
		}

		return charInfopackage;
	}
}