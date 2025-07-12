package l2s.gameserver.network.l2.s2c;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.model.instances.SummonInstance;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket.UpdateType;
import l2s.gameserver.network.l2.s2c.updatetype.IUpdateTypeComponent;
import l2s.gameserver.network.l2.s2c.updatetype.NpcInfoType;
import l2s.gameserver.skills.enums.AbnormalEffect;

public class NpcInfoPacket extends AbstractMaskPacket<NpcInfoType>
{
	public static class SummonInfoPacket extends NpcInfoPacket
	{
		public SummonInfoPacket(SummonInstance summon, Creature attacker)
		{
			super(summon, attacker);
		}
	}

	public static class PetInfoPacket extends NpcInfoPacket
	{
		public PetInfoPacket(PetInstance summon, Creature attacker)
		{
			super(summon, attacker);
		}
	}
	
	private final byte[] _masks = new byte[5];
	
	private final Creature _creature;
	private final int _npcId;
	private final boolean _isAttackable;
	private final int _rHand; 
	private final int _lHand;
	private final int _chest;
	private final String _name, _title;
	private final int _state;
	private final NpcString _nameNpcString, _titleNpcString;

	private int _initSize = 0;
	private int _blockSize = 0;
	private int _showSpawnAnimation;
	private int _npcObjId;
	private Location _loc;
	private int _pAtkSpd, _mAtkSpd;
	private double _atkSpdMul, _runSpdMul;
	private int _pvpFlag, _karma;
	private boolean _running, _flying, _inWater;
	private TeamType _team;
	private int _currentHP, _currentMP;
	private int _maxHP, _maxMP;
	private int _enchantEffect;
	private int _transformId;
	private AbnormalEffect[] _AbnormalVisualEffects;
	private int _clanId, _clanCrestId, _largeClanCrestId;
	private int _allyId, _allyCrestId;
	private int _cloneOid = 0;
	private int _summoned_type = 0;


	public NpcInfoPacket(NpcInstance npc, Creature attacker)
	{
		_creature = npc;
		_cloneOid = npc.getCloneObjId();
		_name = npc.getVisibleName(attacker.getPlayer());
		_title = npc.getVisibleTitle(attacker.getPlayer());
		_npcId = npc.getDisplayId() != 0 ? npc.getDisplayId() : npc.getNpcId();
		_isAttackable = !npc.isAlikeDead() && npc.isAutoAttackable(attacker);
		_rHand = npc.getRightHandItem();
		_lHand = npc.getLeftHandItem();
		_chest = npc.getChestItem();

		_showSpawnAnimation = npc.getSpawnAnimation();
		_state = npc.getNpcState();

		NpcString nameNpcString = npc.getNameNpcString();
		_nameNpcString = nameNpcString != null ? nameNpcString : NpcString.NONE;

		NpcString titleNpcString = npc.getTitleNpcString();
		_titleNpcString = titleNpcString != null ? titleNpcString : NpcString.NONE;

		if(npc.isTargetable(attacker))
			addComponentType(NpcInfoType.IS_TARGETABLE);

		if(npc.isShowName())
			addComponentType(NpcInfoType.IS_SHOW_NAME_TAG);

		common(attacker.getPlayer());
	}

	public NpcInfoPacket(Servitor servitor, Creature attacker)
	{
		_creature = servitor;
		_name = servitor.getVisibleName(attacker.getPlayer());
		_title = servitor.getVisibleTitle(attacker.getPlayer());
		_npcId = servitor.getDisplayId() != 0 ? servitor.getDisplayId() : servitor.getNpcId();
		_isAttackable = servitor.isAutoAttackable(attacker);
		_rHand = servitor.getTemplate().rhand;
		_lHand = servitor.getTemplate().lhand;
		_chest = servitor.getTemplate().chest;
		_showSpawnAnimation = servitor.getSpawnAnimation();
		_state = servitor.getNpcState();
		_nameNpcString = NpcString.NONE;
		_titleNpcString = NpcString.NONE;

		if(servitor.isTargetable(attacker))
			addComponentType(NpcInfoType.IS_TARGETABLE);

		if(servitor.isShowName())
			addComponentType(NpcInfoType.IS_SHOW_NAME_TAG);
		
		common(attacker.getPlayer());
	}

	private void common(Player attacker)
	{
		_summoned_type = 0;
		_npcObjId = _creature.getObjectId();
		_loc = _creature.getLoc();
		_pAtkSpd = _creature.getPAtkSpd();
		_mAtkSpd = _creature.getMAtkSpd();
		_atkSpdMul = _creature.getAttackSpeedMultiplier();
		_runSpdMul = _creature.getMovementSpeedMultiplier();
		_pvpFlag = _creature.getPvpFlag();
		_karma = _creature.getKarma();
		_running = _creature.isRunning();
		_flying = _creature.isFlying();
		_inWater = _creature.isInWater();
		_team = _creature.getTeam();
		_currentHP = attacker != null && attacker.canReceiveStatusUpdate(_creature, StatusUpdatePacket.StatusType.Normal, UpdateType.VCP_HP) ? (int) _creature.getCurrentHp() : (int) _creature.getCurrentHpPercents();
		_currentMP = attacker != null && attacker.canReceiveStatusUpdate(_creature, StatusUpdatePacket.StatusType.Normal, UpdateType.VCP_MP) ? (int) _creature.getCurrentMp() : (int) _creature.getCurrentMpPercents();
		_maxHP = attacker != null && attacker.canReceiveStatusUpdate(_creature, StatusUpdatePacket.StatusType.Normal, UpdateType.VCP_MAXHP) ? _creature.getMaxHp() : 100;
		_maxMP = attacker != null && attacker.canReceiveStatusUpdate(_creature, StatusUpdatePacket.StatusType.Normal, UpdateType.VCP_MAXMP) ? _creature.getMaxMp() : 100;
		_enchantEffect = _creature.getEnchantEffect();
		_transformId = _creature.getVisualTransformId();
		_AbnormalVisualEffects = _creature.getAbnormalEffectsArray();

		Clan clan = _creature.getClan();
		Alliance alliance = clan == null ? null : clan.getAlliance();

		_clanId = clan == null ? 0 : clan.getClanId();
		_clanCrestId = clan == null ? 0 : clan.getCrestId();
		_largeClanCrestId = clan == null ? 0 : clan.getCrestLargeId();
		_allyId = alliance == null ? 0 : alliance.getAllyId();
		_allyCrestId = alliance == null ? 0 : alliance.getAllyCrestId();
	}

	public NpcInfoPacket init()
	{
		addComponentType(NpcInfoType.ATTACKABLE, NpcInfoType.RELATIONS, NpcInfoType.ID, NpcInfoType.POSITION, NpcInfoType.STOP_MODE, NpcInfoType.MOVE_MODE);

		if(_name != StringUtils.EMPTY)
			addComponentType(NpcInfoType.NAME);

		if(_title != StringUtils.EMPTY)
			addComponentType(NpcInfoType.TITLE);

		if(_loc.h > 0)
			addComponentType(NpcInfoType.HEADING);

		if(_pAtkSpd > 0 || _mAtkSpd > 0)
			addComponentType(NpcInfoType.ATK_CAST_SPEED);

		if(_running && _creature.getRunSpeed() > 0 || !_running && _creature.getWalkSpeed() > 0)
			addComponentType(NpcInfoType.SPEED_MODIFIER);

		if(_rHand > 0 || _lHand > 0)
			addComponentType(NpcInfoType.EQUIPSLOT);

		if(_team != TeamType.NONE)
			addComponentType(NpcInfoType.EVENT_MATCH_TEAM_ID);

		if(_state > 0)
			addComponentType(NpcInfoType.STATE);

		if(_inWater || _flying)
			addComponentType(NpcInfoType.ENVIRONMENT);

		if(_flying)
			addComponentType(NpcInfoType.CREATURE_MOVE_TYPE);

		if(_cloneOid > 0)
			addComponentType(NpcInfoType.DOPPELGANGER_SUMMONERS_ID);

		if(_summoned_type > 0)
			addComponentType(NpcInfoType.DOPPELGANGER_TYPE);

		if(_maxHP > 0)
			addComponentType(NpcInfoType.MAX_HP);

		if(_maxMP > 0)
			addComponentType(NpcInfoType.MAX_MP);

		if(_currentHP <= _maxHP)
			addComponentType(NpcInfoType.CURRENT_HP);

		if(_currentMP <= _maxMP)
			addComponentType(NpcInfoType.CURRENT_MP);

		addComponentType(NpcInfoType.ABNORMAL_VISUAL_EFFECT);

		if(_enchantEffect > 0)
			addComponentType(NpcInfoType.ENCHANT);

		if(_transformId > 0)
			addComponentType(NpcInfoType.MORPH_ID);

		if(_clanId > 0)
			addComponentType(NpcInfoType.DISPLAY_INFO);

		if(_creature.getPvpFlag() > 0)
			addComponentType(NpcInfoType.PVP_FLAG);

		if(_creature.getKarma() != 0)
			addComponentType(NpcInfoType.REPUTATION);
		
		if(_nameNpcString != NpcString.NONE)
			addComponentType(NpcInfoType.NAME_NPCSTRINGID);

		if(_titleNpcString != NpcString.NONE)
			addComponentType(NpcInfoType.TITLE_NPCSTRINGID);
		
		if(_creature.isInCombat())
			addComponentType(NpcInfoType.COMBAT_MODE);

		if(_creature.isAlikeDead())
			addComponentType(NpcInfoType.IS_DEAD);

		return this;
	}

	public NpcInfoPacket update(IUpdateTypeComponent... components)
	{
		_showSpawnAnimation = 1;
		
		addComponentType(NpcInfoType.ATTACKABLE, NpcInfoType.RELATIONS, NpcInfoType.ID, NpcInfoType.POSITION, NpcInfoType.STOP_MODE, NpcInfoType.MOVE_MODE);

		for(IUpdateTypeComponent component : components)
		{
			if(component instanceof NpcInfoType npcinfotype)
			{
				if(npcinfotype == NpcInfoType.ABNORMAL_VISUAL_EFFECT)
					continue;
				addComponentType((NpcInfoType) component);
			}
		}
		return this;
	}

	@Override
	protected byte[] getMasks()
	{
		return _masks;
	}

	@Override
	protected void onNewMaskAdded(NpcInfoType component)
	{
		switch(component)
		{
			case ATTACKABLE:
			case RELATIONS:
			{
				_initSize += component.getBlockLength();
				break;
			}
			case TITLE:
			{
				_initSize += component.getBlockLength() + (_title.length() * 2);
				break;
			}
			case NAME:
			{
				_blockSize += component.getBlockLength() + (_name.length() * 2);
				break;
			}
			default:
			{
				_blockSize += component.getBlockLength();
				break;
			}
		}
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_npcObjId);
		packetWriter.writeC(_showSpawnAnimation); // // 0=teleported 1=default 2=summoned
		packetWriter.writeH(39); // 338 - mask_bits_38
		packetWriter.writeB(_masks);

		// Block 1
		packetWriter.writeC(_initSize);

		if(containsMask(NpcInfoType.ATTACKABLE))
			packetWriter.writeC(_isAttackable);

		if(containsMask(NpcInfoType.RELATIONS))
			packetWriter.writeQ(0);

		if(containsMask(NpcInfoType.TITLE))
			packetWriter.writeS(_title);

		// Block 2
		packetWriter.writeH(_blockSize+1);//+1 status mask

		if(containsMask(NpcInfoType.ID))
			packetWriter.writeD(_npcId + 1000000);

		if(containsMask(NpcInfoType.POSITION))
		{
			packetWriter.writeD(_loc.x);
			packetWriter.writeD(_loc.y);
			packetWriter.writeD(_loc.z);
		}

		if(containsMask(NpcInfoType.HEADING))
			packetWriter.writeD(_loc.h);

		if(containsMask(NpcInfoType.VEHICLE))
			packetWriter.writeD(0x00); // Unknown

		if(containsMask(NpcInfoType.ATK_CAST_SPEED))
		{
			packetWriter.writeD(1);
			packetWriter.writeD(0);
		}

		if(containsMask(NpcInfoType.SPEED_MODIFIER))
		{
			packetWriter.writeE((float) _runSpdMul);
			packetWriter.writeE((float) _atkSpdMul);
		}

		if(containsMask(NpcInfoType.EQUIPSLOT))
		{
			packetWriter.writeD(_rHand);
			packetWriter.writeD(_chest);
			packetWriter.writeD(_lHand);
		}

		if(containsMask(NpcInfoType.STOP_MODE))
			packetWriter.writeC(1);

		if(containsMask(NpcInfoType.MOVE_MODE))
			packetWriter.writeC(_running);

		if(containsMask(NpcInfoType.ENVIRONMENT))
			packetWriter.writeC(_inWater ? 1 : _flying ? 2 : 0);

		if(containsMask(NpcInfoType.EVENT_MATCH_TEAM_ID))
			packetWriter.writeC(_team.ordinal());

		if(containsMask(NpcInfoType.ENCHANT))
			packetWriter.writeD(_enchantEffect);

		if(containsMask(NpcInfoType.CREATURE_MOVE_TYPE))
			packetWriter.writeD(_flying);

		if(containsMask(NpcInfoType.DOPPELGANGER_SUMMONERS_ID))
			packetWriter.writeD(_cloneOid); // Player ObjectId with Decoy

		if(containsMask(NpcInfoType.EVOLUTION_ID))
			packetWriter.writeD(0);

		if(containsMask(NpcInfoType.STATE))
			packetWriter.writeD(_state);

		if(containsMask(NpcInfoType.MORPH_ID))
			packetWriter.writeD(_transformId);

		if(containsMask(NpcInfoType.CURRENT_HP))
			packetWriter.writeQ(_currentHP);

		if(containsMask(NpcInfoType.CURRENT_MP))
			packetWriter.writeD(_currentMP);
		
		if(containsMask(NpcInfoType.MAX_HP))
			packetWriter.writeQ(_maxHP);
		
		if(containsMask(NpcInfoType.MAX_MP))
			packetWriter.writeD(_maxMP);
		
		if(containsMask(NpcInfoType.DOPPELGANGER_TYPE))
			packetWriter.writeC(_summoned_type); // тип клона 1 == приманка, 2 = клон у ножа

		if(containsMask(NpcInfoType.FOLLOWING_INFO))
		{
			packetWriter.writeD(0x00);//FollowerId
			packetWriter.writeD(0x00);//FollowDistance
		}

		if(containsMask(NpcInfoType.NAME))
			packetWriter.writeS(_name);

		if(containsMask(NpcInfoType.NAME_NPCSTRINGID))
			packetWriter.writeD(_nameNpcString.getId()); // NPCStringId for name

		if(containsMask(NpcInfoType.TITLE_NPCSTRINGID))
			packetWriter.writeD(_titleNpcString.getId()); // NPCStringId for title

		if(containsMask(NpcInfoType.PVP_FLAG))
			packetWriter.writeC(_pvpFlag); // PVP flag

		if(containsMask(NpcInfoType.REPUTATION))
			packetWriter.writeD(_karma); // Karma

		if(containsMask(NpcInfoType.DISPLAY_INFO))
		{
			packetWriter.writeD(_clanId);
			packetWriter.writeD(_clanCrestId);
			packetWriter.writeD(_largeClanCrestId);
			packetWriter.writeD(_allyId);
			packetWriter.writeD(_allyCrestId);
		}
		
		byte _statusMask = 0x00;
		if (containsMask(NpcInfoType.COMBAT_MODE)) _statusMask |= (1 << (7 - 7));
		if (containsMask(NpcInfoType.IS_DEAD)) _statusMask |= (1 << (7 - 6));
		if (containsMask(NpcInfoType.IS_TARGETABLE)) _statusMask |= (1 << (7 - 5));
		if (containsMask(NpcInfoType.IS_SHOW_NAME_TAG)) _statusMask |= (1 << (7 - 4));
		
		packetWriter.writeC(_statusMask);

		if(containsMask(NpcInfoType.WORLD_ID))
			packetWriter.writeD(0);

		if(containsMask(NpcInfoType.MASTER_ID))
			packetWriter.writeD(0);
		// Block 2 end
		if(containsMask(NpcInfoType.ABNORMAL_VISUAL_EFFECT))
		{
			packetWriter.writeH(_AbnormalVisualEffects.length);
			for(AbnormalEffect abnormal : _AbnormalVisualEffects)
				packetWriter.writeH(abnormal.getId());
		}
		return true;
	}
}