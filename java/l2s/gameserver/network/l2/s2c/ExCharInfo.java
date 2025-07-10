package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Config;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Cubic;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.entity.events.impl.AbstractFightClub;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.model.matching.MatchingRoom;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.updatetype.CharInfoType;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.AbnormalEffect;

/**
 * @author Bonux thx nexvill & artkill
 */
public class ExCharInfo implements IClientOutgoingPacket
{
	public static final int[] PAPERDOLL_ORDER =
	{
		Inventory.PAPERDOLL_PENDANT,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_BACK,
		Inventory.PAPERDOLL_LRHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_DHAIR
	};

	private static final Logger LOGGER = LoggerFactory.getLogger(ExCharInfo.class);

	private boolean canWrite = false;

	private int[][] paperdolls;
	private int mAtkSpd, pAtkSpd;
	private int runSpd, walkSpd, swimRunSpd, swimWalkSpd, flRunSpd, flWalkSpd, flyRunSpd, flyWalkSpd;
	private Location fishLoc;
	private String name, title;
	private int x;
	private int y;
	private int z;
	private int heading;
	private int boatId;
	private int objId, race, sex, baseClass, pvpFlag, karma, recHave;
	private double speedMove, speedAtack, colRadius, colHeight;
	private int hairStyle, hairColor, face;
	private int clanId, clanCrestId, largeClanCrestId, allyId, allyCrestId, classId;
	private int sit, run, combat, dead, privateStore, enchant;
	private int hero, fishing, mountType;
	private int pledgeClass, pledgeType, clanRepScore, mountId;
	private int nameColor, titleColor, transformId, agathionId;
	private Cubic[] cubics;
	private boolean partyRoomLeader, flying;
	private int curHp, maxHp, curMp, maxMp, curCp;
	private TeamType teamType;
	private Set<AbnormalEffect> abnormalEffects;
	private boolean showHeadAccessories;
	private int armorSetEnchant;
	private boolean noble;
	private int ranking;
	private int _specialMountId = 0;

	public ExCharInfo(Creature cha, Player receiver)
	{
		if (cha == null)
		{
			LOGGER.error("CIPacket: cha is null!", new Exception());
			return;
		}

		if (receiver == null)
			return;

		if (cha.isInvisible(receiver))
			return;

		if (cha.isDeleted())
			return;

		objId = cha.getObjectId();
		if (objId == 0)
			return;

		if (receiver.getObjectId() == objId)
		{
			LOGGER.error("You cant send CIPacket about his character to active user!!!", new Exception());
			return;
		}

		Player player = cha.getPlayer();
		if (player == null)
			return;

		Location loc = null;
		if (player.isInBoat())
		{
			loc = player.getInBoatPosition();
			boatId = player.getBoat().getBoatId();
		}

		if (loc == null)
			loc = cha.getLoc();

		x = loc.x;
		y = loc.y;
		z = loc.z;
		heading = loc.h;

		name = player.getVisibleName(receiver);
		nameColor = player.getVisibleNameColor(receiver);

		if (player.isConnected() || player.isInOfflineMode() || player.isFakePlayer())
		{
			title = player.getVisibleTitle(receiver);
			titleColor = player.getVisibleTitleColor(receiver);
		}
		else
		{
			title = "NO CARRIER";
			titleColor = 255;
		}

		if (player.isPledgeVisible(receiver))
		{
			Clan clan = player.getVisibleClan(receiver);
			Alliance alliance = clan == null ? null : clan.getAlliance();
			//
			clanId = clan == null ? 0 : clan.getClanId();
			clanCrestId = clan == null ? 0 : clan.getCrestId();
			largeClanCrestId = clan == null ? 0 : clan.getCrestLargeId();
			//
			allyId = alliance == null ? 0 : alliance.getAllyId();
			allyCrestId = alliance == null ? 0 : alliance.getAllyCrestId();
		}

		if (player.isMounted())
		{
			enchant = 0;
			mountId = player.getMountNpcId() + 1000000;
			mountType = player.getMountType().ordinal();
		}
		else
		{
			enchant = player.getEnchantEffect();
			mountId = 0;
			mountType = 0;
		}

		paperdolls = new int[PcInventory.PAPERDOLL_MAX][4];

		for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
		{
			paperdolls[PAPERDOLL_ID][0] = player.getInventory().getPaperdollItemId(PAPERDOLL_ID);
			paperdolls[PAPERDOLL_ID][1] = player.getInventory().getPaperdollVariation1Id(PAPERDOLL_ID);
			paperdolls[PAPERDOLL_ID][2] = player.getInventory().getPaperdollVariation2Id(PAPERDOLL_ID);
			paperdolls[PAPERDOLL_ID][3] = player.getInventory().getPaperdollVisualId(PAPERDOLL_ID);
		}

		mAtkSpd = player.getMAtkSpd();
		pAtkSpd = player.getPAtkSpd();
		speedMove = player.getMovementSpeedMultiplier();
		runSpd = (int) (player.getRunSpeed() / speedMove);
		walkSpd = (int) (player.getWalkSpeed() / speedMove);

		flRunSpd = 0; // TODO
		flWalkSpd = 0; // TODO

		if (player.isFlying())
		{
			flyRunSpd = runSpd;
			flyWalkSpd = walkSpd;
		}
		else
		{
			flyRunSpd = 0;
			flyWalkSpd = 0;
		}

		swimRunSpd = player.getSwimRunSpeed();
		swimWalkSpd = player.getSwimWalkSpeed();
		race = player.getRace().ordinal();
		sex = player.getSex().ordinal();
		baseClass = ClassId.valueOf(player.getBaseClassId()).getFirstParent(sex).getId();
		pvpFlag = player.getPvpFlag();
		karma = player.getKarma();

		speedAtack = player.getAttackSpeedMultiplier();
		colRadius = player.getCurrentCollisionRadius();
		colHeight = player.getCurrentCollisionHeight();
		hairStyle = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR) > 0 ? sex : (player.getBeautyHairStyle() > 0 ? player.getBeautyHairStyle() : player.getHairStyle());
		hairColor = player.getBeautyHairColor() > 0 ? player.getBeautyHairColor() : player.getHairColor();
		face = player.getBeautyFace() > 0 ? player.getBeautyFace() : player.getFace();
		if (clanId > 0 && player.getClan() != null)
		{
			clanRepScore = player.getClan().getReputationScore();
		}
		else
		{
			clanRepScore = 0;
		}
		sit = player.isSitting() ? 0 : 1; // standing = 1 sitting = 0
		run = player.isRunning() ? 1 : 0; // running = 1 walking = 0
		combat = player.isInCombat() ? 1 : 0;
		dead = player.isAlikeDead() ? 1 : 0;
		privateStore = player.isInObserverMode() ? Player.STORE_OBSERVING_GAMES : player.getPrivateStoreType();
		cubics = player.getCubics().toArray(new Cubic[0]);
		abnormalEffects = player.getAbnormalEffects();
		recHave = player.isGM() ? 0 : player.getRecomHave();
		classId = player.getClassId().getId();
		teamType = player.getTeam();
		hero = player.isHero() || player.isGM() && Config.GM_HERO_AURA ? 2 : 0; // 0x01: Hero Aura
		if (hero == 0)
		{
			hero = Hero.getInstance().isInactiveHero(objId) ? 1 : 0;
		}
		noble = hero > 0;
		fishing = player.getFishing().isInProcess() ? 1 : 0;
		fishLoc = player.getFishing().getHookLocation();
		pledgeClass = player.getPledgeRank().ordinal();
		pledgeType = player.getPledgeType();
		transformId = player.getVisualTransformId();
		agathionId = player.getAgathionNpcId();
		partyRoomLeader = player.getMatchingRoom() != null && player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING && player.getMatchingRoom().getLeader() == player;
		flying = player.isInFlyingTransform();
		curHp = (int) player.getCurrentHp();
		maxHp = player.getMaxHp();
		curMp = (int) player.getCurrentMp();
		maxMp = player.getMaxMp();
		curCp = (int) player.getCurrentCp();
		showHeadAccessories = !player.hideHeadAccessories();
		armorSetEnchant = player.getArmorSetEnchant();
		ranking = RankManager.getInstance().getPlayerGlobalRank(player) == 1 ? 1 : RankManager.getInstance().getPlayerRaceRank(player) == 1 ? 2 : 0;
		if (player.isInFightClub())
		{
			AbstractFightClub fightClubEvent = player.getFightClubEvent();
			name = fightClubEvent.getVisibleName(player, name, false);
			title = fightClubEvent.getVisibleTitle(player, title, false);
			titleColor = fightClubEvent.getVisibleTitleColor(player, titleColor, false);
			nameColor = fightClubEvent.getVisibleNameColor(player, nameColor, false);
		}
		if ((baseClass > 216) && (baseClass < 221))
		{
			SkillEntry skill = player.getKnownSkill(47865);
			if (skill != null)
			{
				_specialMountId = 4;
			}
			else if ((player.getClassId().getId() == 217) || (player.getClassId().getId() == 218))
			{
				_specialMountId = 1;
			}
			else if (player.getClassId().getId() == 219)
			{
				_specialMountId = 2;
			}
			else if (player.getClassId().getId() == 220)
			{
				_specialMountId = 3;
			}
		}

		canWrite = true;
	}

	@Override
	protected boolean canWrite()
	{
		return canWrite;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(347 + (2 + (title.length() * 2)) + (cubics.length * 2) + (abnormalEffects.size() * 2));

		packetWriter.writeD(objId);
		packetWriter.writeH(race);
		packetWriter.writeC(sex);
		packetWriter.writeD(baseClass);

		packetWriter.writeH(CharInfoType.PAPERDOLL.getBlockLength()); // Paperdoll block size (2 + (4 * 12))

		for (int paperdollId : PAPERDOLL_ORDER)
		{
			packetWriter.writeD(paperdolls[paperdollId][0]);
		}
		packetWriter.writeH(CharInfoType.VARIATION.getBlockLength()); // Augmentation block size (2 + (4 * 6))

		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_RHAND][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_RHAND][2]);

		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LHAND][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LHAND][2]);

		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LRHAND][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LRHAND][2]);

		packetWriter.writeC(armorSetEnchant); // Armor Enchant Abnormal

		packetWriter.writeH(CharInfoType.SHAPE_SHIFTING.getBlockLength()); // Shape shifting item block size (2 + (4 * 9))

		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_RHAND][3]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LHAND][3]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LRHAND][3]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_GLOVES][3]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_CHEST][3]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LEGS][3]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_FEET][3]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_HAIR][3]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_DHAIR][3]);

		packetWriter.writeC(pvpFlag);
		packetWriter.writeD(karma);

		packetWriter.writeD(mAtkSpd);
		packetWriter.writeD(pAtkSpd);

		packetWriter.writeD(runSpd);
		packetWriter.writeD(walkSpd);
		packetWriter.writeD(swimRunSpd);
		packetWriter.writeD(swimWalkSpd);
		packetWriter.writeD(flRunSpd);
		packetWriter.writeD(flWalkSpd);
		packetWriter.writeD(flyRunSpd);
		packetWriter.writeD(flyWalkSpd);

		writeCutF(speedMove);
		writeCutF(speedAtack);
		writeCutF(colRadius);
		writeCutF(colHeight);

		packetWriter.writeD(hairStyle);
		packetWriter.writeD(hairColor);
		packetWriter.writeD(face);

		writeString(title);
		packetWriter.writeD(clanId);
		packetWriter.writeD(clanCrestId);
		packetWriter.writeD(allyId);
		packetWriter.writeD(allyCrestId);

		packetWriter.writeC(sit);
		packetWriter.writeC(run);

		packetWriter.writeC(combat);

		packetWriter.writeC(mountType); // 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
		packetWriter.writeH(privateStore);

		packetWriter.writeD(cubics.length);
		for (Cubic cubic : cubics)
		{
			packetWriter.writeH(cubic == null ? 0 : cubic.getId());
		}
		packetWriter.writeC(partyRoomLeader); // find party members
		packetWriter.writeC(flying ? 0x02 : 0x00);
		packetWriter.writeC(recHave);
		packetWriter.writeD(mountId);
		packetWriter.writeD(classId);
		packetWriter.writeD(0); // Foot Effect
		packetWriter.writeC(enchant);
		packetWriter.writeC(0); // back enchant
		packetWriter.writeC(teamType.ordinal()); // team circle around feet 1 = Blue, 2 = red

		packetWriter.writeD(largeClanCrestId);

		packetWriter.writeC(noble); // Is Noble
		packetWriter.writeC(hero);

		packetWriter.writeC(fishing);
		packetWriter.writeD(fishLoc.x);
		packetWriter.writeD(fishLoc.y);
		packetWriter.writeD(fishLoc.z);

		packetWriter.writeD(nameColor);
		packetWriter.writeD(heading);

		packetWriter.writeC(pledgeClass);
		packetWriter.writeH(pledgeType);

		packetWriter.writeD(titleColor);

		packetWriter.writeC(0); // Cursed Weapon Level

		packetWriter.writeD(clanRepScore);

		packetWriter.writeD(transformId);
		packetWriter.writeD(agathionId);
		packetWriter.writeC(1); // nPvPRestrainStatus

		packetWriter.writeD(curCp);
		packetWriter.writeD(curHp);
		packetWriter.writeD(maxHp);
		packetWriter.writeD(curMp);
		packetWriter.writeD(maxMp);

		packetWriter.writeC(1); // cBRLectureMark

		packetWriter.writeD(abnormalEffects.size());
		for (AbnormalEffect abnormal : abnormalEffects)
		{
			packetWriter.writeH(abnormal.getId());
		}
		packetWriter.writeC(0); // Chaos Festival Winner
		packetWriter.writeC(showHeadAccessories);
		packetWriter.writeC(0); // Used Abilities Points
		packetWriter.writeD(0); // nCursedWeaponClassId: Меняет имя на название итема (Item ID)
		packetWriter.writeD(-1); // nWaitActionId
		packetWriter.writeD(ranking);
		packetWriter.writeH(0); // hNotoriety
		packetWriter.writeD(-1); // again class id
		packetWriter.writeD(0); // character color index
		packetWriter.writeD(Config.REQUEST_ID); // Server Id

		packetWriter.writeH(CharInfoType.REALTIME_INFO.getBlockLength() + (2 + (name.length() * 2)));

		packetWriter.writeC(0); // cCreateOrUpdate
		packetWriter.writeC(0); // cShowSpawnEvent
		packetWriter.writeD(x);
		packetWriter.writeD(y);
		packetWriter.writeD(z);
		packetWriter.writeD(boatId); // nVehicleID
		writeString(name);
		packetWriter.writeC(dead); // is player dead
		packetWriter.writeC(_specialMountId);
	}
}