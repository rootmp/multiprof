package l2s.gameserver.network.l2.s2c;

import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.NpcHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.entity.events.impl.AbstractFightClub;
import l2s.gameserver.network.l2.ServerPacketOpcodes;
import l2s.gameserver.network.l2.ServerPacketOpcodes507;
import l2s.gameserver.network.l2.s2c.updatetype.NpcInfoType;
import l2s.gameserver.skills.enums.AbnormalEffect;
import l2s.gameserver.templates.npc.NpcTemplate;

public class NpcInfoPoly extends AbstractMaskPacket<NpcInfoType>
{
	// Flags
	private static final int IS_IN_COMBAT = 1 << 0;
	private static final int IS_ALIKE_DEAD = 1 << 1;
	private static final int IS_TARGETABLE = 1 << 2;
	private static final int IS_SHOW_NAME = 1 << 3;

	private final byte[] _masks = new byte[] {
			(byte) 0x00, (byte) 0x0C, (byte) 0x0C, (byte) 0x00, (byte) 0x00
	};

	private final int _npcId;
	private final boolean _isAttackable;
	private final int _rHand, _lHand;
	private String _name;
	private String _title;

	private int _initSize = 0;
	private int _blockSize = 0;

	private int _statusMask = 0;

	private int _npcObjId;
	private Location _loc;
	private int _pAtkSpd, _mAtkSpd;
	private double _atkSpdMul, _runSpdMul;
	private int _pvpFlag, _karma;
	private boolean _alive, _running, _flying, _inWater;
	private TeamType _team;
	private int _currentHP, _currentMP;
	private int _maxHP, _maxMP;
	private int _enchantEffect;
	private int _transformId;
	private Set<AbnormalEffect> _AbnormalVisualEffects;
	private int _clanId, _clanCrestId, _largeClanCrestId;
	private int _allyId, _allyCrestId;

	public NpcInfoPoly(Player player, Creature attacker)
	{
		NpcTemplate template = NpcHolder.getInstance().getTemplate(player.getPolyId());
		_npcObjId = player.getObjectId();
		_name = player.getVisibleName(attacker.getPlayer());
		_title = player.getVisibleTitle(attacker.getPlayer());
		_npcId = template.displayId != 0 ? template.displayId : template.getId();
		_isAttackable = player.isAutoAttackable(attacker);
		_loc = player.getLoc();
		_rHand = template.rhand;
		_lHand = template.lhand;
		_mAtkSpd = player.getMAtkSpd();
		_pAtkSpd = player.getPAtkSpd();
		_atkSpdMul = player.getAttackSpeedMultiplier();
		_runSpdMul = player.getMovementSpeedMultiplier();
		_pvpFlag = player.getPvpFlag();
		_karma = player.getKarma();
		_alive = !player.isAlikeDead();
		_running = player.isRunning();
		_flying = player.isFlying();
		_inWater = player.isInWater();
		_team = player.getTeam();
		_currentHP = (int) player.getCurrentHp();
		_currentMP = (int) player.getCurrentMp();
		_maxHP = player.getMaxHp();
		_maxMP = player.getMaxMp();
		_enchantEffect = player.getEnchantEffect();
		_transformId = 0;

		_AbnormalVisualEffects = player.getAbnormalEffects();

		for(NpcInfoType component : NpcInfoType.VALUES)
			addComponentType(component);

		if(player.isInCombat())
			_statusMask |= IS_IN_COMBAT;

		if(player.isAlikeDead())
			_statusMask |= IS_ALIKE_DEAD;

		if(player.isTargetable(attacker))
			_statusMask |= IS_TARGETABLE;

		_statusMask |= IS_SHOW_NAME;

		if(player.isInFightClub())
		{
			AbstractFightClub fightClubEvent = player.getFightClubEvent();
			_name = fightClubEvent.getVisibleName(player, _name, false);
			_title = fightClubEvent.getVisibleTitle(player, _title, false);
		}
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
		packetWriter.writeC(0x00); // // 0=teleported 1=default 2=summoned
		packetWriter.writeH(38); // mask_bits_37
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
		packetWriter.writeH(_blockSize);
		if(containsMask(NpcInfoType.ID))
			packetWriter.writeD(_npcId + 1000000); // npctype id

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
			packetWriter.writeD(_pAtkSpd);
			packetWriter.writeD(_mAtkSpd);
		}

		if(containsMask(NpcInfoType.SPEED_MODIFIER))
		{
			packetWriter.writeE((float) _runSpdMul);
			packetWriter.writeE((float) _atkSpdMul);
		}

		if(containsMask(NpcInfoType.EQUIPSLOT))
		{
			packetWriter.writeD(_rHand);
			packetWriter.writeD(0x00); // Armor id?
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
			packetWriter.writeD(0); // Player ObjectId with Decoy

		if(containsMask(NpcInfoType.EVOLUTION_ID))
		{
			// No visual effect
			packetWriter.writeD(0x00); // Unknown
		}

		if(containsMask(NpcInfoType.STATE))
			packetWriter.writeD(0);

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
			packetWriter.writeC(0x00); // тип клона 1 == приманка, 2 = клон у ножа

		if(containsMask(NpcInfoType.FOLLOWING_INFO))
		{
			packetWriter.writeD(0x00);
			packetWriter.writeD(0x00);
		}
		if(containsMask(NpcInfoType.NAME))
			packetWriter.writeS(_name);

		if(containsMask(NpcInfoType.NAME_NPCSTRINGID))
			packetWriter.writeD(-1); // NPCStringId for name

		if(containsMask(NpcInfoType.TITLE_NPCSTRINGID))
			packetWriter.writeD(-1); // NPCStringId for title

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

		if(containsMask(NpcInfoType.WORLD_ID))
			packetWriter.writeD(_statusMask);

		if(containsMask(NpcInfoType.ABNORMAL_VISUAL_EFFECT))
		{
			packetWriter.writeH(_AbnormalVisualEffects.size());
			for(AbnormalEffect abnormal : _AbnormalVisualEffects)
				packetWriter.writeH(abnormal.getId());
		}
		return true;
	}

	@Override
	public ByteBuf getOpcodes()
	{
		try
		{
			ServerPacketOpcodes spo = ServerPacketOpcodes507.NpcInfoPacket;
			ByteBuf opcodes = Unpooled.buffer();
			opcodes.writeByte(spo.getId());
			int exOpcode = spo.getExId();
			if(exOpcode >= 0)
				opcodes.writeShortLE(exOpcode);
			return opcodes.retain();
		}
		catch(IllegalArgumentException e)
		{}
		catch(Exception e)
		{
			LOGGER.error("Cannot find serverpacket opcode: " + getClass().getSimpleName() + "!");
		}
		return Unpooled.EMPTY_BUFFER;
	}
}