package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.skills.enums.SkillCastingType;

/**
 * Format: dddddddddh [h] h [ddd] Пример пакета: 48 86 99 00 4F 86 99 00 4F EF
 * 08 00 00 01 00 00 00 00 00 00 00 00 00 00 00 F9 B5 FF FF 7D E0 01 00 68 F3 FF
 * FF 00 00 00 00
 */
public class MagicSkillUse extends L2GameServerPacket
{
	public static final int NONE = -1;

	private final int _targetId;
	private final int _skillId;
	private final int _skillLevel;
	private final int _hitTime;
	private final int _reuseDelay;
	private final int _chaId;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _tx;
	private final int _ty;
	private final int _tz;
	private final SkillCastingType _castingType;
	private int _reuseGroup;
	private boolean _isServitorSkill;
	private int _actionId;
	private Location _groundLoc = null;
	private boolean _criticalBlow = false;

	public MagicSkillUse(Creature cha, Creature target, int skillId, int skillLevel, int hitTime, long reuseDelay, SkillCastingType castingType, int reuseGroup, boolean isServitorSkill, int actionId)
	{
		_chaId = cha.getObjectId();
		_targetId = target.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = (int) reuseDelay;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
		_reuseGroup = reuseGroup;
		_isServitorSkill = isServitorSkill;
		_actionId = actionId;
		_castingType = castingType;
	}

	public MagicSkillUse(Creature cha, Creature target, int skillId, int skillLevel, int hitTime, long reuseDelay, SkillCastingType castingType)
	{
		this(cha, target, skillId, skillLevel, hitTime, reuseDelay, castingType, -1, false, 0);
	}

	public MagicSkillUse(Creature cha, Creature target, int skillId, int skillLevel, int hitTime, long reuseDelay)
	{
		this(cha, target, skillId, skillLevel, hitTime, reuseDelay, SkillCastingType.NORMAL, -1, false, 0);
	}

	public MagicSkillUse(Creature cha, int skillId, int skillLevel, int hitTime, long reuseDelay)
	{
		this(cha, cha, skillId, skillLevel, hitTime, reuseDelay, SkillCastingType.NORMAL, -1, false, 0);
	}

	public MagicSkillUse setReuseSkillId(int id)
	{
		_reuseGroup = id;
		return this;
	}

	public MagicSkillUse setServitorSkillInfo(int actionId)
	{
		_isServitorSkill = true;
		_actionId = actionId;
		return this;
	}

	public MagicSkillUse setGroundLoc(Location loc)
	{
		_groundLoc = loc;
		return this;
	}

	public MagicSkillUse setCriticalBlow(boolean value)
	{
		_criticalBlow = value;
		return this;
	}

	@Override
	protected final void writeImpl()
	{
		/**
		 * Casting bar type:
		 * <li>0 - default,
		 * <li>1 - default up,
		 * <li>2 - blue,
		 * <li>3 - green,
		 * <li>4 - red.
		 **/
		writeD(_castingType.getClientBarId());
		writeD(_chaId);
		writeD(_targetId);
		writeD(_skillId);
		writeD(_skillLevel);
		writeD(_hitTime);
		writeD(_reuseGroup);
		writeD(_reuseDelay);
		writeD(_x);
		writeD(_y);
		writeD(_z);

		if (_criticalBlow) // TODO: Реализовать.
		{
			writeH(0x02);
			for (int i = 0; i < 2; i++)
			{
				writeH(0); // ???
			}
		}
		else
		{
			writeH(0x00);
		}

		if (_groundLoc != null)
		{
			writeH(0x01);
			writeD(_groundLoc.x);
			writeD(_groundLoc.y);
			writeD(_groundLoc.z);
		}
		else
		{
			writeH(0x00);
		}
		
		writeD(_tx);
		writeD(_ty);
		writeD(_tz);
		writeD(_isServitorSkill ? 0x01 : 0x00); // is Pet Skill
		writeD(_actionId); // Social Action ID
		if (_skillId != 5103)
		{
			writeD(-1);
		}
	}

	@Override
	public L2GameServerPacket packet(Player player)
	{
		if (player != null)
		{
			if (player.isNotShowBuffAnim())
				return _chaId == player.getObjectId() ? super.packet(player) : null;
		}

		return super.packet(player);
	}
}