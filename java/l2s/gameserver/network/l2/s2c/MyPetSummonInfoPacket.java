package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.entity.events.impl.AbstractFightClub;
import l2s.gameserver.skills.enums.AbnormalEffect;

/**
 * Eden 286 "cddddddddhhhhhhhhffffdddcdSdScdddddddQhQQQdddddddddddddddccccddcc"
 * "ddd"
 */
public class MyPetSummonInfoPacket implements IClientOutgoingPacket
{
	private static final int IS_ATTACKABLE = 1 << 0; // 1
	private static final int IS_UNK_FLAG_2 = 1 << 1; // 2
	private static final int IS_RUNNING = 1 << 2; // 4
	private static final int IS_IN_COMBAT = 1 << 3; // 8
	private static final int IS_ALIKE_DEAD = 1 << 4; // 16
	private static final int IS_RIDEABLE = 1 << 5; // 32

	private int _runSpd, _walkSpd, MAtkSpd, PAtkSpd, pvp_flag, karma;
	private int _type, obj_id, npc_id, incombat, dead, _sp, level;
	private int curFed, maxFed, curHp, maxHp, curMp, maxMp, curLoad, maxLoad;
	private int PAtk, PDef, MAtk, MDef, sps, ss, type, _showSpawnAnimation;
	private int _pAccuracy, _pEvasion, _pCrit, _mAccuracy, _mEvasion, _mCrit;
	private Location _loc;
	private double col_redius, col_height;
	private long exp, exp_this_lvl, exp_next_lvl;
	private String _name, title;
	private TeamType _team;
	private double _atkSpdMul, _runSpdMul;
	private int _transformId;
	private AbnormalEffect[] _abnormalEffects;
	private int _rhand, _lhand;
	private int _flags;
	private final int evolveLevel, petType;

	public MyPetSummonInfoPacket(Servitor summon)
	{
		_type = summon.getServitorType();
		obj_id = summon.getObjectId();
		npc_id = summon.getNpcId();
		_loc = summon.getLoc();
		MAtkSpd = summon.getMAtkSpd();
		PAtkSpd = summon.getPAtkSpd();
		_runSpd = summon.getRunSpeed();
		_walkSpd = summon.getWalkSpeed();
		col_redius = summon.getCurrentCollisionRadius();
		col_height = summon.getCurrentCollisionHeight();
		incombat = summon.isInCombat() ? 1 : 0;
		dead = summon.isAlikeDead() ? 1 : 0;
		_name = summon.getVisibleName(summon.getPlayer());
		title = summon.getVisibleTitle(summon.getPlayer());

		pvp_flag = summon.getPvpFlag();
		karma = summon.getKarma();
		curFed = summon.getCurrentFed();
		maxFed = summon.getMaxFed();
		curHp = (int) summon.getCurrentHp();
		maxHp = summon.getMaxHp();
		curMp = (int) summon.getCurrentMp();
		maxMp = summon.getMaxMp();
		_sp = summon.getSp();
		level = summon.getLevel();
		exp = summon.getExp();
		exp_this_lvl = summon.getExpForThisLevel();
		exp_next_lvl = summon.getExpForNextLevel();
		curLoad = summon.getCurrentLoad();
		maxLoad = summon.getMaxLoad();
		PAtk = summon.getPAtk(null);
		PDef = summon.getPDef(null);
		MAtk = summon.getMAtk(null, null);
		MDef = summon.getMDef(null, null);
		_pAccuracy = summon.getPAccuracy();
		_pEvasion = summon.getPEvasionRate(null);
		_pCrit = summon.getPCriticalHit(null);
		_mAccuracy = summon.getMAccuracy();
		_mEvasion = summon.getMEvasionRate(null);
		_mCrit = summon.getMCriticalHit(null, null);
		_abnormalEffects = summon.getAbnormalEffectsArray();
		_team = summon.getTeam();
		ss = summon.getSoulshotConsumeCount();
		sps = summon.getSpiritshotConsumeCount();
		_showSpawnAnimation = summon.getSpawnAnimation();
		type = summon.getFormId();
		_atkSpdMul = summon.getAttackSpeedMultiplier();
		_runSpdMul = summon.getMovementSpeedMultiplier();
		_transformId = summon.getVisualTransformId();
		evolveLevel = summon.getEvolveLevel();
		petType = summon.getPetType();
		boolean rideable = summon.isMountable();

		Player owner = summon.getPlayer();
		if (owner != null)
		{
			// В режиме трансформации значек mount/dismount не отображается
			if (owner.isTransformed())
			{
				rideable = false;
			}
			if (owner.isInFightClub())
			{
				AbstractFightClub fightClubEvent = owner.getFightClubEvent();
				_name = fightClubEvent.getVisibleName(owner, _name, false);
				title = fightClubEvent.getVisibleTitle(owner, title, false);
			}
		}

		_rhand = summon.getTemplate().rhand;
		_lhand = summon.getTemplate().lhand;

		if (summon.isAutoAttackable(summon.getPlayer()))
			_flags |= IS_ATTACKABLE;

		_flags |= IS_UNK_FLAG_2;

		if (summon.isRunning())
			_flags |= IS_RUNNING;

		if (summon.isInCombat())
			_flags |= IS_IN_COMBAT;

		if (summon.isAlikeDead())
			_flags |= IS_ALIKE_DEAD;

		if (rideable)
			_flags |= IS_RIDEABLE;
	}

	public MyPetSummonInfoPacket update()
	{
		_showSpawnAnimation = 1;
		return this;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_type);
		packetWriter.writeD(obj_id);
		packetWriter.writeD(npc_id + 1000000);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		packetWriter.writeD(_loc.h);
		packetWriter.writeD(MAtkSpd);
		packetWriter.writeD(PAtkSpd);
		packetWriter.writeH(_runSpd);
		packetWriter.writeH(_walkSpd);
		packetWriter.writeH(_runSpd/* _swimRunSpd */);
		packetWriter.writeH(_walkSpd/* _swimWalkSpd */);
		packetWriter.writeH(_runSpd/* _flRunSpd */);
		packetWriter.writeH(_walkSpd/* _flWalkSpd */);
		packetWriter.writeH(_runSpd/* _flyRunSpd */);
		packetWriter.writeH(_walkSpd/* _flyWalkSpd */);
		packetWriter.writeF(_runSpdMul);
		packetWriter.writeF(_atkSpdMul);
		packetWriter.writeF(col_redius);
		packetWriter.writeF(col_height);
		packetWriter.writeD(_rhand); // right hand weapon
		packetWriter.writeD(0);
		packetWriter.writeD(_lhand); // left hand weapon
		packetWriter.writeC(_showSpawnAnimation); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon
										// animation)
		packetWriter.writeD(-1);
		packetWriter.writeS(_name);
		packetWriter.writeD(-1);
		packetWriter.writeS(title);
		packetWriter.writeC(pvp_flag); // 0=white, 1=purple, 2=purpleblink, if its greater then karma = purple
		packetWriter.writeD(karma); // hmm karma ??
		packetWriter.writeD(curFed); // how fed it is
		packetWriter.writeD(maxFed); // max fed it can be
		packetWriter.writeD(curHp); // current hp
		packetWriter.writeD(maxHp); // max hp
		packetWriter.writeD(curMp); // current mp
		packetWriter.writeD(maxMp); // max mp
		packetWriter.writeQ(_sp); // sp
		packetWriter.writeH(level);// lvl
		packetWriter.writeQ(exp);
		packetWriter.writeQ(exp_this_lvl); // 0% absolute value
		packetWriter.writeQ(exp_next_lvl); // 100% absoulte value
		packetWriter.writeD(curLoad); // weight
		packetWriter.writeD(maxLoad); // max weight it can carry
		packetWriter.writeD(PAtk);// patk
		packetWriter.writeD(PDef);// pdef
		packetWriter.writeD(_pAccuracy); // P. Accuracy
		packetWriter.writeD(_pEvasion); // P. Evasion
		packetWriter.writeD(_pCrit); // P. Critical
		packetWriter.writeD(MAtk);// matk
		packetWriter.writeD(MDef);// mdef
		packetWriter.writeD(_mAccuracy); // M. Accuracy
		packetWriter.writeD(_mEvasion); // M. Evasion
		packetWriter.writeD(_mCrit); // M. Critical
		packetWriter.writeD(_runSpd);// speed
		packetWriter.writeD(PAtkSpd);// atkspeed
		packetWriter.writeD(MAtkSpd);// casting speed
		packetWriter.writeC(0);// unk
		packetWriter.writeC(_team.ordinal()); // team aura (1 = blue, 2 = red)
		packetWriter.writeC(ss);
		packetWriter.writeC(sps);
		packetWriter.writeD(_type);
		packetWriter.writeD(_transformId); // transform id
		packetWriter.writeC(1); // sum points
		packetWriter.writeC(1); // max sum points

		packetWriter.writeH(_abnormalEffects.length);
		for (AbnormalEffect abnormal : _abnormalEffects)
			packetWriter.writeH(abnormal.getId());

		packetWriter.writeC(_flags);
		packetWriter.writeD(petType);// pet type id

		packetWriter.writeD(evolveLevel);
		packetWriter.writeD(npc_id); // pet name from id. 0 evolve lvl without name,
		return true;
	}
}