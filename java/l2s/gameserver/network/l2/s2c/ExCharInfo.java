package l2s.gameserver.network.l2.s2c;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.GameServer;
import l2s.gameserver.enums.PrivateStoreType;
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
import l2s.gameserver.model.matching.MatchingRoom;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.updatetype.CharInfoType;
import l2s.gameserver.skills.SkillEntry;

public class ExCharInfo implements IClientOutgoingPacket
{
	public static final int[] PAPERDOLL_ORDER = {
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
			Inventory.PAPERDOLL_DHAIR,
			Inventory.PAPERDOLL_BACK};

	private static final Logger LOGGER = LoggerFactory.getLogger(ExCharInfo.class);

	private boolean canWrite = false;

	private int[][] paperdolls;
	private int mAtkSpd, pAtkSpd;
	private int runSpd, walkSpd, swimRunSpd, swimWalkSpd, flRunSpd, flWalkSpd, flyRunSpd, flyWalkSpd;
	private Location fishLoc;
	private String sName, title;
	private int x, y, z, heading, nVehicleID;
	private int objId, race, sex, baseClass, pvpFlag, karma, recHave;
	private float speedMove, speedAtack, colRadius, colHeight;
	private int hairStyle, hairColor, face;
	private int clanId, clanCrestId, largeClanCrestId, allyId, allyCrestId, classId;
	private int sit, run, combat, cIsDead, privateStore, enchant;
	private int hero, fishing, mountType;
	private int pledgeClass, pledgeType, clanRepScore, mountId;
	private int nameColor, titleColor, transformId, agathionId;
	private Cubic[] cubics;
	private boolean partyRoomLeader, flying;
	private int curHp, maxHp, curMp, maxMp, curCp;
	private TeamType teamType;
	private Set<AbnormalVisualEffect> AbnormalVisualEffects;
	private boolean cHairAccFlag;
	private int armorSetEnchant;
	private boolean noble;
	private int ranking;
	private int cOrcRiderShapeLevel;

	public ExCharInfo(Creature cha, Player receiver)
	{
		if(cha == null)
		{
			LOGGER.error("CIPacket: cha is null!", new Exception());
			return;
		}

		if(receiver == null)
			return;

		if(cha.isInvisible(receiver))
			return;

		if(cha.isDeleted())
			return;

		objId = cha.getObjectId();
		if(objId == 0)
			return;

		if(receiver.getObjectId() == objId)
		{
			LOGGER.error("You cant send CIPacket about his character to active user!!!", new Exception());
			return;
		}

		Player player = cha.getPlayer();
		if(player == null)
			return;

		Location loc = null;
		if(player.isInBoat())
		{
			loc = player.getInBoatPosition();
			nVehicleID = player.getBoat().getBoatId();
		}

		if(loc == null)
			loc = cha.getLoc();

		x = loc.x;
		y = loc.y;
		z = loc.z;
		heading = loc.h;

		sName = player.getVisibleName(receiver);
		nameColor = player.getVisibleNameColor(receiver);

		if(player.isConnected() || player.isInOfflineMode())
		{
			title = player.getVisibleTitle(receiver);
			titleColor = player.getVisibleTitleColor(receiver);
		}
		else
		{
			title = "NO CARRIER";
			titleColor = 255;
		}

		if(player.isPledgeVisible(receiver))
		{
			Clan clan = player.getClan();
			Alliance alliance = clan == null ? null : clan.getAlliance();
			//
			clanId = clan == null ? 0 : clan.getClanId();
			clanCrestId = clan == null ? 0 : clan.getCrestId();
			largeClanCrestId = clan == null ? 0 : clan.getCrestLargeId();
			//
			allyId = alliance == null ? 0 : alliance.getAllyId();
			allyCrestId = alliance == null ? 0 : alliance.getAllyCrestId();
		}

		if(player.isMounted())
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

		paperdolls = new int[Inventory.PAPERDOLL_MAX][5];

		for(int PAPERDOLL_ID : PAPERDOLL_ORDER)
		{
			paperdolls[PAPERDOLL_ID][0] = player.getInventory().getPaperdollItemId(PAPERDOLL_ID);
			paperdolls[PAPERDOLL_ID][1] = player.getInventory().getPaperdollVariation1Id(PAPERDOLL_ID);
			paperdolls[PAPERDOLL_ID][2] = player.getInventory().getPaperdollVariation2Id(PAPERDOLL_ID);
			paperdolls[PAPERDOLL_ID][3] = player.getInventory().getPaperdollVariation3Id(PAPERDOLL_ID);
			paperdolls[PAPERDOLL_ID][4] = receiver.getVarBoolean("DisableVisual", false) ? player.getInventory().getPaperdollItemId(PAPERDOLL_ID) : player.getInventory().getPaperdollVisualId(PAPERDOLL_ID);
		}

		mAtkSpd = player.getMAtkSpd();
		pAtkSpd = player.getPAtkSpd();
		speedMove = (float) player.getMovementSpeedMultiplier();
		runSpd = (int) (player.getRunSpeed() / speedMove);
		walkSpd = (int) (player.getWalkSpeed() / speedMove);

		flRunSpd = 0; // TODO
		flWalkSpd = 0; // TODO

		if(player.isFlying())
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

		speedAtack = (float) player.getAttackSpeedMultiplier();
		colRadius = (float) player.getCurrentCollisionRadius();
		colHeight = (float) player.getCurrentCollisionHeight();
		hairStyle = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR) > 0 ? sex : (player.getBeautyHairStyle() > 0 ? player.getBeautyHairStyle() : player.getHairStyle());
		hairColor = player.getBeautyHairColor() > 0 ? player.getBeautyHairColor() : player.getHairColor();
		face = player.getBeautyFace() > 0 ? player.getBeautyFace() : player.getFace();
		if(clanId > 0 && player.getClan() != null)
			clanRepScore = player.getClan().getReputationScore();
		else
			clanRepScore = 0;
		sit = player.isSitting() ? 0 : 1; // standing = 1 sitting = 0
		run = player.isRunning() ? 1 : 0; // running = 1 walking = 0
		combat = player.isInCombat() ? 1 : 0;
		cIsDead = player.isAlikeDead() ? 1 : 0;
		privateStore = player.isInObserverMode() ? PrivateStoreType.STORE_OBSERVING_GAMES.getId() : player.getPrivateStoreType().getId();
		cubics = player.getCubics().toArray(new Cubic[0]);
		AbnormalVisualEffects = player.getAbnormalVisualEffects();
		recHave = player.isGM() ? 0 : player.getRecomHave();
		classId = player.getClassId().getId();
		teamType = player.getTeam();
		hero = player.isHero() || player.isGM() && Config.GM_HERO_AURA ? 2 : 0; // 0x01: Hero Aura
		if(hero == 0)
			hero = Hero.getInstance().isInactiveHero(objId) ? 1 : 0;
		noble = hero > 0;
		fishing = player.getFishing().isInProcess() ? 1 : 0;
		fishLoc = player.getFishing().getHookLocation();
		pledgeClass = player.getPledgeRank().ordinal();
		pledgeType = player.getPledgeType();
		transformId = player.getVisualTransformId();
		agathionId = player.getAgathionNpcId();
		partyRoomLeader = player.getMatchingRoom() != null && player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING && player.getMatchingRoom().getLeader() == player;
		flying = player.isInFlyingTransform();
		curHp = (int) player.getCurrentHp();//receiver.canReceiveStatusUpdate(player, StatusUpdatePacket.UpdateType.DEFAULT, StatusUpdatePacket.CUR_HP) ? (int) player.getCurrentHp() : 0;
		maxHp = player.getMaxHp();//receiver.canReceiveStatusUpdate(player, StatusUpdatePacket.UpdateType.DEFAULT, StatusUpdatePacket.MAX_HP) ? player.getMaxHp() : 0;
		curMp = (int) player.getCurrentMp();
		maxMp = player.getMaxMp();
		curCp = (int) player.getCurrentCp();
		cHairAccFlag = !player.hideHeadAccessories();
		armorSetEnchant = player.getArmorSetEnchant();
		ranking = RankManager.getInstance().getTypeForPacker(player, true);
		if(baseClass == 217)
		{
			SkillEntry skill = player.getKnownSkill(47865);
			if(skill != null)
				cOrcRiderShapeLevel = 4;
			else if(player.getClassId().getId() == 217 || player.getClassId().getId() == 218)
				cOrcRiderShapeLevel = 1;
			else if(player.getClassId().getId() == 219)
				cOrcRiderShapeLevel = 2;
			else if(player.getClassId().getId() == 220)
				cOrcRiderShapeLevel = 3;
		}

		if(player.isInFightClub())
		{
			AbstractFightClub fightClubEvent = player.getFightClubEvent();
			sName = fightClubEvent.getVisibleName(player, sName, false);
			title = fightClubEvent.getVisibleTitle(player, title, false);
			titleColor = fightClubEvent.getVisibleTitleColor(player, titleColor, false);
			nameColor = fightClubEvent.getVisibleNameColor(player, nameColor, false);
		}
		canWrite = true;
	}

	@Override
	public boolean canBeWritten()
	{
		return canWrite;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(351 + (title.length() * 2) + (cubics.length * 2) + (AbnormalVisualEffects.size() * 2));

		writeCachedParameters(packetWriter);

		packetWriter.writeH(CharInfoType.REALTIME_INFO.getBlockLength() + (sName.length() * 2));// Realtime parameters block size (2 + (1 x 2) + (4 x 4) + (2 + name size))

		writeRealtimeParameters(packetWriter);
		return true;
	}

	private void writeCachedParameters(PacketWriter packetWriter)
	{
		packetWriter.writeD(objId); //nID
		packetWriter.writeH(race); //hRace
		packetWriter.writeC(sex); //cSex
		packetWriter.writeD(baseClass); //nOriginalClass

		//SlotItemClassID
		packetWriter.writeH(CharInfoType.PAPERDOLL.getBlockLength()); // Paperdoll block size (2 + (4 * 12))
		for(int paperdollId : PAPERDOLL_ORDER)
			packetWriter.writeD(paperdolls[paperdollId][0]);


		packetWriter.writeH(CharInfoType.VARIATION.getBlockLength());
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_RHAND][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_RHAND][2]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_RHAND][3]);

		packetWriter.writeH(CharInfoType.VARIATION.getBlockLength());
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LHAND][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LHAND][2]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LHAND][3]);

		packetWriter.writeH(CharInfoType.VARIATION.getBlockLength());
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LRHAND][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LRHAND][2]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LRHAND][3]);

		packetWriter.writeH(CharInfoType.VARIATION.getBlockLength());
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_HAIR][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_HAIR][2]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_HAIR][3]);

		packetWriter.writeH(CharInfoType.VARIATION.getBlockLength());
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_DHAIR][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_DHAIR][2]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_DHAIR][3]);

		packetWriter.writeH(CharInfoType.VARIATION.getBlockLength());
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_BACK][1]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_BACK][2]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_BACK][3]);



		packetWriter.writeC(armorSetEnchant); //nMinNewSetItemEchantedEffect Armor Enchant Abnormal

		//SlotItemShapeShiftClassID
		packetWriter.writeH(CharInfoType.SHAPE_SHIFTING.getBlockLength()); // Shape shifting item block size (2 + (4 * 9))

		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_RHAND][4]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LHAND][4]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LRHAND][4]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_GLOVES][4]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_CHEST][4]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_LEGS][4]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_FEET][4]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_HAIR][4]);
		packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_DHAIR][4]);
		if(GameServer.SERVER_PROTOCOL == 507)
			packetWriter.writeD(paperdolls[Inventory.PAPERDOLL_BACK][4]);
		
		packetWriter.writeC(pvpFlag); //cGuilty
		packetWriter.writeD(karma);//nCriminalRate

		packetWriter.writeD(mAtkSpd);//nMCastingSpeed
		packetWriter.writeD(pAtkSpd);//nPCastingSpeed

		packetWriter.writeD(runSpd);
		packetWriter.writeD(walkSpd);
		packetWriter.writeD(swimRunSpd);
		packetWriter.writeD(swimWalkSpd);
		packetWriter.writeD(flRunSpd);
		packetWriter.writeD(flWalkSpd);
		packetWriter.writeD(flyRunSpd);
		packetWriter.writeD(flyWalkSpd);

		packetWriter.writeE(speedMove);//fMoveSpeedModifier
		packetWriter.writeE(speedAtack);//fAttackSpeedModifier
		packetWriter.writeE(colRadius);//fCollisionRadius
		packetWriter.writeE(colHeight);//fCollisionHeight

		packetWriter.writeD(face); //nFace
		packetWriter.writeD(hairStyle); //nHairShape
		packetWriter.writeD(hairColor); //nHairColor

		packetWriter.writeSizedString(title); //sNickName

		packetWriter.writeD(clanId); //nPledgeSId
		packetWriter.writeD(clanCrestId); //nPledgeCrestId
		packetWriter.writeD(allyId); //nAllianceID
		packetWriter.writeD(allyCrestId); //nAllianceCrestId

		packetWriter.writeC(sit);//cStopMode
		packetWriter.writeC(run);//cSlow
		packetWriter.writeC(combat);//cIsCombatMode
		packetWriter.writeC(mountType); //cYongmaType 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
		packetWriter.writeC(privateStore); //nPrivateStore

		packetWriter.writeD(cubics.length); //nCubicCount
		for(Cubic cubic : cubics)
			packetWriter.writeH(cubic == null ? 0 : cubic.getId());

		packetWriter.writeC(partyRoomLeader); //cDeosShowPartyWantedMessage find party members
		packetWriter.writeC(flying ? 0x02 : 0x00);//cEnvironment
		packetWriter.writeH(recHave);//hBonusCount
		packetWriter.writeD(mountId);//nYongmaClass
		packetWriter.writeD(classId); //nNowClass
		packetWriter.writeD(0); //nFootEffect
		packetWriter.writeC(enchant);//cSNEnchant
		packetWriter.writeC(0);//cBackEnchant

		packetWriter.writeC(0);//cHairEnchant
		packetWriter.writeC(0);//cHair2Enchant

		packetWriter.writeC(teamType.ordinal()); //cEventMatchTeamID team circle around feet 1 = Blue, 2 = red

		packetWriter.writeD(largeClanCrestId);//nPledgeEmblemId

		packetWriter.writeC(noble); //cIsNobless Is Noble
		packetWriter.writeC(hero);//cHeroType

		packetWriter.writeC(fishing); //cIsFishingState
		packetWriter.writeD(fishLoc.x); //nFishingPosX
		packetWriter.writeD(fishLoc.y); //nFishingPosY
		packetWriter.writeD(fishLoc.z); //nFishingPosZ

		packetWriter.writeD(nameColor); //nNameColor
		packetWriter.writeD(heading); //nDirection

		packetWriter.writeC(pledgeClass);//cSocialClass
		packetWriter.writeH(pledgeType);//hPledgeType

		packetWriter.writeD(titleColor);//nNickNameColor

		packetWriter.writeC(0); //nCursedWeaponLevel Cursed Weapon Level

		packetWriter.writeD(clanRepScore);//nPledgeNameValue

		packetWriter.writeD(transformId);//nTransformID
		packetWriter.writeD(agathionId);//nAgathionID
		packetWriter.writeC(1); // nPvPRestrainStatus

		packetWriter.writeD(curCp); //nCP
		packetWriter.writeD(curHp); //nHP
		packetWriter.writeD(maxHp); //nBaseHP
		packetWriter.writeD(curMp); //nMP
		packetWriter.writeD(maxMp); //nBaseMP

		packetWriter.writeC(0); //cBRLectureMark

		packetWriter.writeD(AbnormalVisualEffects.size()); //nAVECount
		for(AbnormalVisualEffect abnormal : AbnormalVisualEffects)
			packetWriter.writeH(abnormal.getId());

		packetWriter.writeC(0); //cPledgeGameUserFlag Chaos Festival Winner
		packetWriter.writeC(cHairAccFlag); //cHairAccFlag
		packetWriter.writeC(0); //cRemainAP  Used Abilities Points
		packetWriter.writeD(0); //nCursedWeaponClassId: Меняет имя на название итема (Item ID)
		packetWriter.writeD(-1); //nWaitActionId
		packetWriter.writeD(ranking);//nFirstRank
		packetWriter.writeH(0); // hNotoriety
		packetWriter.writeD(-1); //nMainClass again class id
		packetWriter.writeD(hairColor); //nCharacterColorIndex
		packetWriter.writeD(0); //nWorldID
	}

	private void writeRealtimeParameters(PacketWriter packetWriter)
	{
		packetWriter.writeC(0); // cCreateOrUpdate
		packetWriter.writeC(0); // cShowSpawnEvent
		packetWriter.writeD(x); //nInformingPosX
		packetWriter.writeD(y); //nInformingPosY
		packetWriter.writeD(z); //nInformingPosZ
		packetWriter.writeD(nVehicleID); // nVehicleID
		packetWriter.writeSizedString(sName); //sName
		packetWriter.writeC(cIsDead); //cIsDead 
		packetWriter.writeC(cOrcRiderShapeLevel);//388 cOrcRiderShapeLevel
		packetWriter.writeD(0); // nlastDeadStatus
		packetWriter.writeD(0); // nEnemyKillCount
	}
}