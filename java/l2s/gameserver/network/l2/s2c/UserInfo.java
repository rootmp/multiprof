package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.NpcHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.player.Cubic;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.Element;
import l2s.gameserver.model.base.ElementalElement;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.entity.events.Event;
import l2s.gameserver.model.entity.events.impl.AbstractFightClub;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.matching.MatchingRoom;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.updatetype.UserInfoType;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.stats.Stats;

/**
 * @reworked by Bonux
 */
public class UserInfo extends AbstractMaskPacket<UserInfoType>
{
	public static final int USER_RELATION_PARTY_MEMBER = 0x08;
	public static final int USER_RELATION_PARTY_LEADER = 0x10;
	public static final int USER_RELATION_CLAN_MEMBER = 0x20;
	public static final int USER_RELATION_CLAN_LEADER = 0x40;
	public static final int USER_RELATION_IN_SIEGE = 0x80;
	public static final int USER_RELATION_ATTACKER = 0x100;
	public static final int USER_RELATION_IN_DOMINION_WAR = 0x1000;

	// Params
	private boolean _canWrite = false, partyRoom;
	private int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd, _flRunSpd, _flWalkSpd, _flyRunSpd, _flyWalkSpd, _relation, _WaitActionID;
	private double move_speed, attack_speed, col_radius, col_height;
	private Location _loc;
	private int obj_id, vehicle_obj_id, _race, sex, base_class, level, curCp, maxCp, _weaponEnchant, _armorSetEnchant, _weaponFlag;
	private long _exp, _sp;
	@SuppressWarnings("unused")
	private int curHp, maxHp, curMp, maxMp, curLoad, maxLoad, rec_left, rec_have;
	private int _str, _con, _dex, _int, _wit, _men, ClanPrivs, InventoryLimit;
	private int _patk, _patkspd, _pdef, _matk, _matkspd;
	private int _pEvasion, _pAccuracy, _pCrit, _mEvasion, _mAccuracy, _mCrit;
	private int _mdef, pvp_flag, karma, hair_style, hair_color, face, gm_commands, fame, sayhas_grace_points;
	private int clan_id, _isClanLeader, clan_crest_id, ally_id, ally_crest_id, large_clan_crest_id;
	@SuppressWarnings("unused")
	private int private_store, can_crystalize, pk_kills, pvp_kills, class_id, agathion, _partySubstitute;
	@SuppressWarnings("unused")
	private int hero, mount_id;
	private int name_color, running, pledge_class, pledge_type, title_color;
	private int defenceFire, defenceWater, defenceWind, defenceEarth, defenceHoly, defenceUnholy;
	private int mount_type;
	private String _name, _title;
	@SuppressWarnings("unused")
	private Cubic[] cubics;
	private Element attackElement;
	private int attackElementValue;
	private int _moveType;
	//private boolean _allowMap;
	private int talismans;
	private int _jewelsLimit;
	private boolean _activeMainAgathionSlot;
	private int _subAgathionsLimit;
	private double _expPercent;
	private TeamType _team;
	private final boolean _hideHeadAccessories;
	@SuppressWarnings("unused")
	private final int _elementalFireDefence, _elementalWaterDefence, _elementalWindDefence, _elementalEarthDefence, _activeElementId;
	private final boolean _noble;
	private final int _rank;
	private final int _totalStatPoints;
	private final int _bonusSTR, _bonusDEX, _bonusCON, _bonusINT, _bonusWIT, _bonusMEN, _elixirsUsed;

	private final byte[] _masks = new byte[] {
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
	};

	private int _initSize = 5;
	private int _specialMountId;
	private int _elementalFireAttackPower;
	private int _elementalWaterAttackPower;
	private int _elementalWindAttackPower;
	private int _elementalEarthAttackPower;
	private int _magicLamp = 0;
	private int _powerAttackWeapon;
	private int _magicAttackWeapon;

	public UserInfo(Player player)
	{
		this(player, true);
	}

	public void setMagicLamp(int i)
	{
		_magicLamp = i;
	}

	public UserInfo(Player player, boolean addAll)
	{
		_name = player.getVisibleName(player);
		name_color = player.getVisibleNameColor(player);
		_title = player.getVisibleTitle(player);
		title_color = player.getVisibleTitleColor(player);

		if(player.isPledgeVisible(player))
		{
			Clan clan = player.getClan();
			Alliance alliance = clan == null ? null : clan.getAlliance();
			//
			clan_id = clan == null ? 0 : clan.getClanId();
			_isClanLeader = player.isClanLeader() ? 1 : 0;
			clan_crest_id = clan == null ? 0 : clan.getCrestId();
			large_clan_crest_id = clan == null ? 0 : clan.getCrestLargeId();
			//
			ally_id = alliance == null ? 0 : alliance.getAllyId();
			ally_crest_id = alliance == null ? 0 : alliance.getAllyCrestId();
		}

		if(player.isGMInvisible())
			_title += "[Invisible]";
		if(player.isPolymorphed())
		{
			if(NpcHolder.getInstance().getTemplate(player.getPolyId()) != null)
				_title += "[" + NpcHolder.getInstance().getTemplate(player.getPolyId()).name + "]";
			else
				_title += "[Polymorphed]";
		}

		if(player.isMounted())
		{
			_weaponEnchant = 0;
			mount_id = player.getMountNpcId() + 1000000;
			mount_type = player.getMountType().ordinal();
		}
		else
		{
			_weaponEnchant = player.getEnchantEffect();
			mount_id = 0;
			mount_type = 0;
		}

		_weaponFlag = player.getActiveWeaponInstance() == null ? 0x14 : 0x28;

		move_speed = player.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(player.getRunSpeed() / move_speed);
		_walkSpd = (int) Math.round(player.getWalkSpeed() / move_speed);

		_flRunSpd = 0; // TODO
		_flWalkSpd = 0; // TODO

		if(player.isFlying())
		{
			_flyRunSpd = _runSpd;
			_flyWalkSpd = _walkSpd;
		}
		else
		{
			_flyRunSpd = 0;
			_flyWalkSpd = 0;
		}

		_swimRunSpd = (int) Math.round(player.getSwimRunSpeed() / move_speed);
		_swimWalkSpd = (int) Math.round(player.getSwimWalkSpeed() / move_speed);

		Party party = player.getParty();
		if(party != null)
		{
			_relation |= USER_RELATION_PARTY_MEMBER;

			if(party.isLeader(player))
				_relation |= USER_RELATION_PARTY_LEADER;
		}
		_WaitActionID = 0;

		if(player.getClan() != null)
		{
			// AFK animation.
			//if (player.isInPeaceZone() && player.getClan() != null)
			//_afk_clan_animation = player.isClanLeader() ? 100 : 101;
			//else
			_WaitActionID = 0;

			_relation |= USER_RELATION_CLAN_MEMBER;
			if(player.isClanLeader())
				_relation |= USER_RELATION_CLAN_LEADER;
		}

		for(Event e : player.getEvents())
			_relation = e.getUserRelation(player, _relation);

		_loc = player.getLoc();
		obj_id = player.getObjectId();
		vehicle_obj_id = player.isInBoat() ? player.getBoat().getBoatId() : 0x00;
		_race = player.getRace().ordinal();
		sex = player.getSex().ordinal();
		base_class = ClassId.valueOf(player.getBaseClassId()).getFirstParent(sex).getId();
		level = player.getLevel();
		_exp = player.getExp();
		_expPercent = Experience.getExpPercent(player.getLevel(), player.getExp());
		_str = player.getSTR();
		_dex = player.getDEX();
		_con = player.getCON();
		_int = player.getINT();
		_wit = player.getWIT();
		_men = player.getMEN();
		curHp = (int) player.getCurrentHp();
		maxHp = player.getMaxHp();
		curMp = (int) player.getCurrentMp();
		maxMp = player.getMaxMp();
		curLoad = player.getCurrentLoad();
		maxLoad = player.getMaxLoad();
		_sp = player.getSp();
		_patk = player.getPAtk(null);
		_patkspd = player.getPAtkSpd();
		_pdef = player.getPDef(null);
		_pEvasion = player.getPEvasionRate(null);
		_pAccuracy = player.getPAccuracy();
		_pCrit = player.getPCriticalHit(null);
		_mEvasion = player.getMEvasionRate(null);
		_mAccuracy = player.getMAccuracy();
		_mCrit = player.getMCriticalHit(null, null);
		_matk = player.getMAtk(null, null);
		_matkspd = player.getMAtkSpd();
		_mdef = player.getMDef(null, null);
		pvp_flag = player.getPvpFlag(); // 0=white, 1=purple, 2=purpleblink
		karma = player.getKarma();
		attack_speed = player.getAttackSpeedMultiplier();
		col_radius = player.getCurrentCollisionRadius();
		col_height = player.getCurrentCollisionHeight();

		if(player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR) > 0 && player.hideHeadAccessories())
		{
			hair_style = player.getBeautyHairStyle() > 0 ? player.getBeautyHairStyle() : player.getHairStyle();
			hair_color = player.getBeautyHairColor() > 0 ? player.getBeautyHairColor() : player.getHairColor();
		}
		else
		{
			hair_style = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR)
					> 0 ? player.getHairStyle() : (player.getBeautyHairStyle() > 0 ? player.getBeautyHairStyle() : player.getHairStyle());
			hair_color = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR)
					> 0 ? player.getHairColor() : player.getBeautyHairColor() > 0 ? player.getBeautyHairColor() : player.getHairColor();
		}
		face = player.getBeautyFace() > 0 ? player.getBeautyFace() : player.getFace();
		gm_commands = player.isGM() || player.getPlayerAccess().CanUseAltG ? 1 : 0;
		// builder level активирует в клиенте админские команды
		clan_id = player.getClanId();
		ally_id = player.getAllyId();
		private_store = player.getPrivateStoreType();
		can_crystalize = player.getSkillLevel(Skill.SKILL_CRYSTALLIZE) > 0 ? 1 : 0;
		pk_kills = player.getPkKills();
		pvp_kills = player.getPvpKills();
		cubics = player.getCubics().toArray(new Cubic[player.getCubics().size()]);
		ClanPrivs = player.getClanPrivileges();
		rec_left = player.getRecomLeft(); //c2 recommendations remaining
		rec_have = player.getRecomHave(); //c2 recommendations received
		InventoryLimit = player.getInventoryLimit();
		class_id = player.getClassId().getId();
		maxCp = player.getMaxCp();
		curCp = (int) player.getCurrentCp();
		_team = player.getTeam();
		hero = player.isHero() || player.isGM() && Config.GM_HERO_AURA ? 2 : 0; //0x01: Hero Aura and symbol
		if(hero == 0)
			hero = Hero.getInstance().isInactiveHero(obj_id) ? 1 : 0;
		_noble = hero > 0;
		running = player.isRunning() ? 0x01 : 0x00; //changes the Speed display on Status Window
		pledge_class = player.getPledgeRank().ordinal();
		pledge_type = player.getPledgeType();
		attackElement = player.getAttackElement();
		attackElementValue = player.getAttack(attackElement);
		defenceFire = player.getDefence(Element.FIRE);
		defenceWater = player.getDefence(Element.WATER);
		defenceWind = player.getDefence(Element.WIND);
		defenceEarth = player.getDefence(Element.EARTH);
		defenceHoly = player.getDefence(Element.HOLY);
		defenceUnholy = player.getDefence(Element.UNHOLY);
		agathion = player.getAgathionNpcId();
		fame = player.getFame();
		sayhas_grace_points = player.getSayhasGrace();
		partyRoom = player.getMatchingRoom() != null && player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING
				&& player.getMatchingRoom().getLeader() == player;
		_moveType = player.isInFlyingTransform() ? 0x02 : (player.isInWater() ? 0x01 : 0x00);
		talismans = player.getTalismanCount();
		_jewelsLimit = player.getJewelsLimit();
		_activeMainAgathionSlot = player.isActiveMainAgathionSlot();
		_subAgathionsLimit = player.getSubAgathionsLimit();
		//_allowMap = player.isActionBlocked(Zone.BLOCKED_ACTION_MINIMAP);
		_partySubstitute = player.isPartySubstituteStarted() ? 1 : 0;
		_hideHeadAccessories = player.hideHeadAccessories();
		_armorSetEnchant = player.getArmorSetEnchant();

		_elementalFireAttackPower = player.getStat().getElementalAttackPower(ElementalElement.FIRE);
		_elementalWaterAttackPower = player.getStat().getElementalAttackPower(ElementalElement.WATER);
		_elementalWindAttackPower = player.getStat().getElementalAttackPower(ElementalElement.WIND);
		_elementalEarthAttackPower = player.getStat().getElementalAttackPower(ElementalElement.EARTH);

		_elementalFireDefence = player.getStat().getElementalDefence(ElementalElement.FIRE);
		_elementalWaterDefence = player.getStat().getElementalDefence(ElementalElement.WATER);
		_elementalWindDefence = player.getStat().getElementalDefence(ElementalElement.WIND);
		_elementalEarthDefence = player.getStat().getElementalDefence(ElementalElement.EARTH);
		_activeElementId = player.getActiveElement().getId();

		_rank = RankManager.getInstance().getTypeForPacker(player, true);

		_totalStatPoints = player.getTotalStatPoints();
		_bonusSTR = player.getStatBonus(0);
		_bonusDEX = player.getStatBonus(1);
		_bonusCON = player.getStatBonus(2);
		_bonusINT = player.getStatBonus(3);
		_bonusWIT = player.getStatBonus(4);
		_bonusMEN = player.getStatBonus(5);

		_powerAttackWeapon = (int) player.getStat().getDiff(Stats.POWER_ATTACK_WEAPON);
		_magicAttackWeapon = (int) player.getStat().getDiff(Stats.MAGIC_ATTACK_WEAPON);

		_elixirsUsed = player.getActiveSubClass().getBonusPointUsed();
		if(base_class == 217)
		{
			SkillEntry skill = player.getKnownSkill(47865);
			if(skill != null)
				_specialMountId = 4;
			else if((player.getClassId().getId() == 217) || (player.getClassId().getId() == 218))
				_specialMountId = 1;
			else if(player.getClassId().getId() == 219)
				_specialMountId = 2;
			else if(player.getClassId().getId() == 220)
				_specialMountId = 3;
		}
		if(player.isInFightClub())
		{
			AbstractFightClub fightClubEvent = player.getFightClubEvent();
			_name = fightClubEvent.getVisibleName(player, _name, true);
			_title = fightClubEvent.getVisibleTitle(player, _title, true);
			title_color = fightClubEvent.getVisibleTitleColor(player, title_color, true);
			name_color = fightClubEvent.getVisibleNameColor(player, name_color, true);
		}

		_canWrite = true;

		if(addAll)
		{
			for(UserInfoType infoType : UserInfoType.values())
			{
				switch(infoType)
				{
					case VANGUARD_MOUNT:
						if(_specialMountId != 0)
							addComponentType(infoType);
						break;
					default:
						addComponentType(infoType);
						break;
				}
			}
		}
	}

	@Override
	protected byte[] getMasks()
	{
		return _masks;
	}

	@Override
	protected void onNewMaskAdded(UserInfoType component)
	{
		calcBlockSize(component);
	}

	private void calcBlockSize(UserInfoType type)
	{
		switch(type)
		{
			case BASIC_INFO:
			{
				_initSize += type.getBlockLength() + (_name.length() * 2);
				break;
			}
			case CLAN:
			{
				_initSize += type.getBlockLength() + (_title.length() * 2);
				break;
			}
			default:
			{
				_initSize += type.getBlockLength();
				break;
			}
		}
	}

	@Override
	public boolean canBeWritten()
	{
		return _canWrite;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(obj_id);

		packetWriter.writeD(_initSize);

		packetWriter.writeH(30);

		packetWriter.writeB(_masks);

		if(containsMask(UserInfoType.RELATION))
			packetWriter.writeD(_relation);

		if(containsMask(UserInfoType.BASIC_INFO))
		{
			packetWriter.writeH(UserInfoType.BASIC_INFO.getBlockLength() + (_name.length() * 2));
			packetWriter.writeSizedString(_name);
			packetWriter.writeC(gm_commands);
			packetWriter.writeC(_race);
			packetWriter.writeC(sex);
			packetWriter.writeD(base_class);
			packetWriter.writeD(class_id);
			packetWriter.writeH(level);
			packetWriter.writeH(0x00); // unk 272
			packetWriter.writeD(class_id); // 286 - again class
		}

		if(containsMask(UserInfoType.BASE_STATS))
		{
			packetWriter.writeH(UserInfoType.BASE_STATS.getBlockLength());
			packetWriter.writeH(_str);
			packetWriter.writeH(_dex);
			packetWriter.writeH(_con);
			packetWriter.writeH(_int);
			packetWriter.writeH(_wit);
			packetWriter.writeH(_men);
			packetWriter.writeH(1); // LUC
			packetWriter.writeH(1); // CHA
		}

		if(containsMask(UserInfoType.MAX_HPCPMP))
		{
			packetWriter.writeH(UserInfoType.MAX_HPCPMP.getBlockLength());
			packetWriter.writeD(maxHp);
			packetWriter.writeD(maxMp);
			packetWriter.writeD(maxCp);
		}

		if(containsMask(UserInfoType.CURRENT_HPMPCP_EXP_SP))
		{
			packetWriter.writeH(UserInfoType.CURRENT_HPMPCP_EXP_SP.getBlockLength());
			packetWriter.writeD(curHp);
			packetWriter.writeD(curMp);
			packetWriter.writeD(curCp);
			packetWriter.writeQ(_sp);
			packetWriter.writeQ(_exp);
			packetWriter.writeF(_expPercent);
			packetWriter.writeC(_magicLamp);
		}

		if(containsMask(UserInfoType.ENCHANTLEVEL))
		{
			packetWriter.writeH(UserInfoType.ENCHANTLEVEL.getBlockLength());

			packetWriter.writeC(_weaponEnchant);
			packetWriter.writeC(_armorSetEnchant);
			packetWriter.writeC(0); // 338 - cBackEnchant?

			packetWriter.writeC(0);//cHairEnchant ?
			packetWriter.writeC(0);//cHair2Enchant ?
		}

		if(containsMask(UserInfoType.APPAREANCE))
		{
			packetWriter.writeH(UserInfoType.APPAREANCE.getBlockLength());
			packetWriter.writeD(hair_style);
			packetWriter.writeD(hair_color);
			packetWriter.writeD(face);
			packetWriter.writeC(!_hideHeadAccessories); //переключения прически/головного убора
			packetWriter.writeD(0); // unk 338
		}

		if(containsMask(UserInfoType.STATUS))
		{
			packetWriter.writeH(UserInfoType.STATUS.getBlockLength());
			packetWriter.writeC(mount_type);
			packetWriter.writeC(private_store);
			packetWriter.writeC(can_crystalize);
			packetWriter.writeC(0x00); //cRemainAbilityPoint Used Abilities Points
		}

		if(containsMask(UserInfoType.STATS))
		{
			packetWriter.writeH(UserInfoType.STATS.getBlockLength());
			packetWriter.writeH(_weaponFlag);
			packetWriter.writeD(_patk);
			packetWriter.writeD(_patkspd);
			packetWriter.writeD(_pdef);
			packetWriter.writeD(_pEvasion);
			packetWriter.writeD(_pAccuracy);
			packetWriter.writeD(_pCrit);
			packetWriter.writeD(_matk);
			packetWriter.writeD(_matkspd);
			packetWriter.writeD(_patkspd);
			packetWriter.writeD(_mEvasion);
			packetWriter.writeD(_mdef);
			packetWriter.writeD(_mAccuracy);
			packetWriter.writeD(_mCrit);
			packetWriter.writeD(_powerAttackWeapon);
			packetWriter.writeD(_magicAttackWeapon);

			packetWriter.writeD(1);
		}

		if(containsMask(UserInfoType.ELEMENTALS))
		{
			packetWriter.writeH(UserInfoType.ELEMENTALS.getBlockLength());
			packetWriter.writeH(defenceFire);
			packetWriter.writeH(defenceWater);
			packetWriter.writeH(defenceWind);
			packetWriter.writeH(defenceEarth);
			packetWriter.writeH(defenceHoly);
			packetWriter.writeH(defenceUnholy);
		}

		if(containsMask(UserInfoType.POSITION))
		{
			packetWriter.writeH(UserInfoType.POSITION.getBlockLength());
			packetWriter.writeD(_loc.x);
			packetWriter.writeD(_loc.y);
			packetWriter.writeD(_loc.z);
			packetWriter.writeD(vehicle_obj_id);
		}

		if(containsMask(UserInfoType.SPEED))
		{
			packetWriter.writeH(UserInfoType.SPEED.getBlockLength());
			packetWriter.writeH(_runSpd);
			packetWriter.writeH(_walkSpd);
			packetWriter.writeH(_swimRunSpd);
			packetWriter.writeH(_swimWalkSpd);
			packetWriter.writeH(_flRunSpd);
			packetWriter.writeH(_flWalkSpd);
			packetWriter.writeH(_flyRunSpd);
			packetWriter.writeH(_flyWalkSpd);
		}

		if(containsMask(UserInfoType.MULTIPLIER))
		{
			packetWriter.writeH(UserInfoType.MULTIPLIER.getBlockLength());
			packetWriter.writeF(move_speed);
			packetWriter.writeF(attack_speed);
		}

		if(containsMask(UserInfoType.COL_RADIUS_HEIGHT))
		{
			packetWriter.writeH(UserInfoType.COL_RADIUS_HEIGHT.getBlockLength());
			packetWriter.writeF(col_radius);
			packetWriter.writeF(col_height);
		}

		if(containsMask(UserInfoType.ATK_ELEMENTAL))
		{
			packetWriter.writeH(UserInfoType.ATK_ELEMENTAL.getBlockLength());
			packetWriter.writeC(attackElement.getId());
			packetWriter.writeH(attackElementValue);
		}

		if(containsMask(UserInfoType.CLAN))
		{
			packetWriter.writeH(UserInfoType.CLAN.getBlockLength() + (_title.length() * 2));
			packetWriter.writeSizedString(_title);
			packetWriter.writeH(pledge_type);
			packetWriter.writeD(clan_id);
			packetWriter.writeD(large_clan_crest_id);
			packetWriter.writeD(clan_crest_id);
			packetWriter.writeD(ClanPrivs);
			packetWriter.writeC(_isClanLeader);
			packetWriter.writeD(ally_id);
			packetWriter.writeD(ally_crest_id);
			packetWriter.writeC(partyRoom ? 0x01 : 0x00);
		}

		if(containsMask(UserInfoType.SOCIAL))
		{
			packetWriter.writeH(UserInfoType.SOCIAL.getBlockLength());
			packetWriter.writeC(pvp_flag);
			packetWriter.writeD(karma);
			packetWriter.writeC(_noble); // Is Noble
			packetWriter.writeC(hero);
			packetWriter.writeC(pledge_class);
			packetWriter.writeD(pk_kills);
			packetWriter.writeD(pvp_kills);
			packetWriter.writeH(rec_left);
			packetWriter.writeH(rec_have);

			packetWriter.writeD(_WaitActionID); // TODO Флаг 101 трон 100

			packetWriter.writeH(0x00); // Claim left
			packetWriter.writeH(0x00); // Claim have
			packetWriter.writeD(0x00);
		}

		if(containsMask(UserInfoType.VITA_FAME))
		{
			packetWriter.writeH(UserInfoType.VITA_FAME.getBlockLength());
			packetWriter.writeD(sayhas_grace_points);
			packetWriter.writeC(0x00); // Vita Bonus
			packetWriter.writeD(fame);
			packetWriter.writeD(0x00); // Raid Points
			packetWriter.writeD(0x00); // nDyeChargeAmount?
		}

		if(containsMask(UserInfoType.SLOTS))
		{
			packetWriter.writeH(UserInfoType.SLOTS.getBlockLength());
			packetWriter.writeC(talismans);
			packetWriter.writeC(_jewelsLimit);
			packetWriter.writeC(_team.ordinal());
			packetWriter.writeD(0x00); // (1 = Red, 2 = White, 3 = White Pink) dotted ring on the floor
			packetWriter.writeC(_activeMainAgathionSlot); // 140 PROTOCOL
			packetWriter.writeC(_subAgathionsLimit); // 140 PROTOCOL
			packetWriter.writeC(0x00); //SkillModMaxArtifactSlotGroupCount SEVEN SIGNS
		}

		if(containsMask(UserInfoType.MOVEMENTS))
		{
			packetWriter.writeH(UserInfoType.MOVEMENTS.getBlockLength());
			packetWriter.writeC(_moveType);
			packetWriter.writeC(running);
		}

		if(containsMask(UserInfoType.COLOR))
		{
			packetWriter.writeH(UserInfoType.COLOR.getBlockLength());
			packetWriter.writeD(name_color);
			packetWriter.writeD(title_color);
		}

		if(containsMask(UserInfoType.INVENTORY_LIMIT))
		{
			packetWriter.writeH(UserInfoType.INVENTORY_LIMIT.getBlockLength());
			packetWriter.writeD(0x00);//Mount ?
			packetWriter.writeH(InventoryLimit);
			packetWriter.writeC(0); // 1 - don't show title CursedWeaponLevel
			packetWriter.writeD(0x00);//CursedWeaponClassID
		}

		if(containsMask(UserInfoType.TRUE_HERO))
		{
			packetWriter.writeH(UserInfoType.TRUE_HERO.getBlockLength());
			packetWriter.writeC(0x00);//PvPPointRestrain
			packetWriter.writeD(0x00);//UltimateSkillPoint
			packetWriter.writeC(0x00);//Unk
			packetWriter.writeC(0x00);//cChaosFestivalWinner
		}

		if(containsMask(UserInfoType.ELEMENTAL))
		{
			packetWriter.writeH(UserInfoType.ELEMENTAL.getBlockLength());

			packetWriter.writeD(_elementalFireAttackPower); // Сила атаки стихией Огня
			packetWriter.writeD(_elementalWaterAttackPower); // Сила атаки стихией Воды
			packetWriter.writeD(_elementalWindAttackPower); // Сила атаки стихией Ветра
			packetWriter.writeD(_elementalEarthAttackPower); // Сила атаки стихией Земли

			packetWriter.writeD(_elementalFireDefence); // Защита Огня
			packetWriter.writeD(_elementalWaterDefence); // Защита Воды
			packetWriter.writeD(_elementalWindDefence); // Защита Ветра
			packetWriter.writeD(_elementalEarthDefence); // Защита Земли

		}

		if(containsMask(UserInfoType.RANKING))
		{
			packetWriter.writeH(UserInfoType.RANKING.getBlockLength());
			packetWriter.writeD(_rank); // Rank
		}

		if(containsMask(UserInfoType.STATS_INCREASE))
		{
			packetWriter.writeH(UserInfoType.STATS_INCREASE.getBlockLength());
			packetWriter.writeH(_totalStatPoints); // Total Points
			packetWriter.writeH(_bonusSTR); // STR additional points
			packetWriter.writeH(_bonusDEX); // DEX additional points
			packetWriter.writeH(_bonusCON); // CON additional points
			packetWriter.writeH(_bonusINT); // INT additional points
			packetWriter.writeH(_bonusWIT); // WIT additional points
			packetWriter.writeH(_bonusMEN); // MEN additional points
		}

		if(containsMask(UserInfoType.ADDITIONAL_STATS))
		{
			packetWriter.writeH(UserInfoType.ADDITIONAL_STATS.getBlockLength());
			packetWriter.writeH(0x00); // UNK 235
			packetWriter.writeH(0x00); // UNK 235
			packetWriter.writeH(0x00); // UNK 235
			packetWriter.writeH(0x00); // UNK 235
			packetWriter.writeH(0x00); // UNK 235
			packetWriter.writeH(0x00); // UNK 235
			packetWriter.writeH(0x00); // UNK 235
			packetWriter.writeH(0x00); // UNK 235
		}

		if(containsMask(UserInfoType.ELIXIR_USED))
			packetWriter.writeD(_elixirsUsed);

		if(containsMask(UserInfoType.VANGUARD_MOUNT))
			packetWriter.writeC(_specialMountId);
		return true;
	}
}