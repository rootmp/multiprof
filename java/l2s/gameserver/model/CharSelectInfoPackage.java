package l2s.gameserver.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l2s.gameserver.Config;
import l2s.gameserver.dao.CharacterVariablesDAO;
import l2s.gameserver.dao.CustomHeroDAO;
import l2s.gameserver.dao.ItemsDAO;
import l2s.gameserver.model.actor.instances.player.CharacterVariable;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;

/**
 * Used to Store data sent to Client for Character Selection screen.
 * 
 * @version $Revision: 1.2.2.2.2.4 $ $Date: 2005/03/27 15:29:33 $
 */
public class CharSelectInfoPackage
{
	private String _name;
	private String _changedOldName;
	private int _objectId = 0;
	private int _charId = 0x00030b7a;
	private long _exp = 0;
	private long _sp = 0;
	private int _clanId = 0;
	private int _race = 0;
	private int _classId = 0;
	private int _baseClassId = 0;
	private int _deleteTimer = 0;
	private long _lastAccess = 0L;
	private int _face = 0;
	private int _hairStyle = 0;
	private int _beautyHairStyle = 0;
	private int _hairColor = 0;
	private int _beautyHaircolor = 0;
	private int _sex = 0;
	private int _karma = 0, _pk = 0, _pvp = 0;
	private int _maxHp = 0;
	private double _currentHp = 0;
	private int _maxMp = 0;
	private double _currentMp = 0;
	private ItemInstance[] _paperdoll;
	private int _accesslevel = 0;
	private int _x = 0, _y = 0, _z = 0;
	private int _sayhasGrace = 35000;
	private boolean _hairAccessoryEnabled = true;
	private long _lastLogin = 0;
	private final Map<String, CharacterVariable> _variables = new ConcurrentHashMap<>();

	
	/**
	 * @param int1
	 */
	public CharSelectInfoPackage(int objectId, String name)
	{
		setObjectId(objectId);
		_name = name;
		Collection<ItemInstance> items = ItemsDAO.getInstance().getItemsByOwnerIdAndLoc(objectId, ItemLocation.PAPERDOLL);
		_paperdoll = new ItemInstance[Inventory.PAPERDOLL_MAX];
		for(ItemInstance item : items)
		{
			if(item.getEquipSlot() < Inventory.PAPERDOLL_MAX)
			{
				_paperdoll[item.getEquipSlot()] = item;
			}
		}
		List<CharacterVariable> variables = CharacterVariablesDAO.getInstance().restore(getObjectId());
		for(CharacterVariable var : variables)
		{
			_variables.put(var.getName(), var);
		}
	}

	public int getObjectId()
	{
		return _objectId;
	}

	public void setObjectId(int objectId)
	{
		_objectId = objectId;
	}

	public int getCharId()
	{
		return _charId;
	}

	public void setCharId(int charId)
	{
		_charId = charId;
	}

	public int getClanId()
	{
		return _clanId;
	}

	public void setClanId(int clanId)
	{
		_clanId = clanId;
	}

	public int getClassId()
	{
		return _classId;
	}

	public int getBaseClassId()
	{
		return _baseClassId;
	}

	public void setBaseClassId(int baseClassId)
	{
		_baseClassId = baseClassId;
	}

	public void setClassId(int classId)
	{
		_classId = classId;
	}

	public double getCurrentHp()
	{
		return _currentHp;
	}

	public void setCurrentHp(double currentHp)
	{
		_currentHp = currentHp;
	}

	public double getCurrentMp()
	{
		return _currentMp;
	}

	public void setCurrentMp(double currentMp)
	{
		_currentMp = currentMp;
	}

	public int getDeleteTimer()
	{
		return _deleteTimer;
	}

	public void setDeleteTimer(int deleteTimer)
	{
		_deleteTimer = deleteTimer;
	}

	public long getLastAccess()
	{
		return _lastAccess;
	}

	public void setLastAccess(long lastAccess)
	{
		_lastAccess = lastAccess;
	}

	public long getExp()
	{
		return _exp;
	}

	public void setExp(long exp)
	{
		_exp = exp;
	}

	public int getFace()
	{
		return _face;
	}

	public void setFace(int face)
	{
		_face = face;
	}

	public int getHairColor()
	{
		return _hairColor;
	}

	public void setHairColor(int hairColor)
	{
		_hairColor = hairColor;
	}

	public int getHairStyle()
	{
		return _hairStyle;
	}

	public void setHairStyle(int hairStyle)
	{
		_hairStyle = hairStyle;
	}

	public int getPaperdollObjectId(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		if(item != null)
		{ return item.getObjectId(); }
		return 0;
	}

	public int getPaperdollVariation1Id(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		if((item != null) && item.isAugmented())
		{ return item.getVariation1Id(); }
		return 0;
	}

	public int getPaperdollVariation2Id(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		if((item != null) && item.isAugmented())
		{ return item.getVariation2Id(); }
		return 0;
	}

	public int getPaperdollItemId(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		if(item != null)
		{ return item.getItemId(); }
		return 0;
	}

	public int getPaperdollVisualId(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		if(item != null)
		{ return item.getVisualId(); }
		return 0;
	}

	public int getPaperdollEnchantEffect(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		if(item != null)
		{ return item.getEnchantLevel(); }
		return 0;
	}

	public int getMaxHp()
	{
		return _maxHp;
	}

	public void setMaxHp(int maxHp)
	{
		_maxHp = maxHp;
	}

	public int getMaxMp()
	{
		return _maxMp;
	}

	public void setMaxMp(int maxMp)
	{
		_maxMp = maxMp;
	}

	public String getName(boolean oldIfExists)
	{
		if(oldIfExists)
		{
			if(_changedOldName != null)
			{ return _changedOldName; }
		}
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public int getRace()
	{
		return _race;
	}

	public void setRace(int race)
	{
		_race = race;
	}

	public int getSex()
	{
		return _sex;
	}

	public void setSex(int sex)
	{
		_sex = sex;
	}

	public long getSp()
	{
		return _sp;
	}

	public void setSp(long sp)
	{
		_sp = sp;
	}

	public int getKarma()
	{
		return _karma;
	}

	public void setKarma(int karma)
	{
		_karma = karma;
	}

	public int getAccessLevel()
	{
		return _accesslevel;
	}

	public void setAccessLevel(int accesslevel)
	{
		_accesslevel = accesslevel;
	}

	public int getX()
	{
		return _x;
	}

	public void setX(int x)
	{
		_x = x;
	}

	public int getY()
	{
		return _y;
	}

	public void setY(int y)
	{
		_y = y;
	}

	public int getZ()
	{
		return _z;
	}

	public void setZ(int z)
	{
		_z = z;
	}

	public int getPk()
	{
		return _pk;
	}

	public void setPk(int pk)
	{
		_pk = pk;
	}

	public int getPvP()
	{
		return _pvp;
	}

	public void setPvP(int pvp)
	{
		_pvp = pvp;
	}

	public boolean isHairAccessoryEnabled()
	{
		return _hairAccessoryEnabled;
	}

	public void setHairAccessoryEnabled(boolean value)
	{
		_hairAccessoryEnabled = value;
	}

	public boolean isAvailable()
	{
		return getAccessLevel() > -100;
	}

	public boolean isHero()
	{
		if(Config.ENABLE_OLYMPIAD && Hero.getInstance().isHero(getObjectId()))
		{ return true; }

		return CustomHeroDAO.getInstance().isCustomHero(getObjectId());
	}

	public void setChangedOldName(String name)
	{
		_changedOldName = name;
	}

	public String getChangedOldName()
	{
		return _changedOldName;
	}

	public void setLastLoginTime(long time)
	{
		_lastLogin = time;
	}

	public long getLastLoginTime()
	{
		return _lastLogin;
	}

	public int getVitalityItemsUsed()
	{
		return getVarInt(PlayerVariables.VITALITY_ITEMS_USED, 0);
	}

	public int getVarInt(String name, int defaultValue)
	{
		String var = getVar(name);
		if(var != null)
		{ return Integer.parseInt(var); }
		return defaultValue;
	}

	public String getVar(String name)
	{
		return getVar(name, null);
	}

	public String getVar(String name, String defaultValue)
	{
		CharacterVariable var = _variables.get(name);
		if((var != null) && !var.isExpired())
		{ return var.getValue(); }
		return defaultValue;
	}

	public int getSayhasGrace()
	{
		return _sayhasGrace;
	}

	public void setSayhasGrace(int points)
	{
		_sayhasGrace = points;
	}
	
	public int getBeautyHairStyle()
	{
		return _beautyHairStyle;
	}

	public int getBeautyHairColor()
	{
		return _beautyHaircolor;
	}
	
	public void setBeautyHairStyle(int beautyHairStyle)
	{
		_beautyHairStyle=beautyHairStyle;
	}

	public void setBeautyHairColor(int beautyHaircolor)
	{
		_beautyHaircolor =beautyHaircolor;
	}
}