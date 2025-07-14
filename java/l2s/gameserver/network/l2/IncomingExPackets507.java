package l2s.gameserver.network.l2;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import l2s.commons.network.IConnectionState;
import l2s.commons.network.IIncomingPacket;
import l2s.commons.network.IIncomingPackets;
import l2s.gameserver.network.l2.c2s.*;
import l2s.gameserver.network.l2.c2s.RaidAuction.RequestExRaidAuctionBid;
import l2s.gameserver.network.l2.c2s.RaidAuction.RequestExRaidAuctionCancelBid;
import l2s.gameserver.network.l2.c2s.RaidAuction.RequestExRaidAuctionPostList;
import l2s.gameserver.network.l2.c2s.RaidAuction.RequestExRaidAuctionPostReceive;
import l2s.gameserver.network.l2.c2s.RaidAuction.RequestExRaidAuctionPostReceiveAll;
import l2s.gameserver.network.l2.c2s.ability.RequestExAcquirePotentialSkill;
import l2s.gameserver.network.l2.c2s.ability.RequestExChangeAbilityPreset;
import l2s.gameserver.network.l2.c2s.ability.RequestExChangePotentialPoint;
import l2s.gameserver.network.l2.c2s.ability.RequestExRequestPotentialSkillList;
import l2s.gameserver.network.l2.c2s.ability.RequestExResetPotentialSkill;
import l2s.gameserver.network.l2.c2s.adenadistribution.RequestDivideAdena;
import l2s.gameserver.network.l2.c2s.adenadistribution.RequestDivideAdenaCancel;
import l2s.gameserver.network.l2.c2s.adenadistribution.RequestDivideAdenaStart;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabBossInfo;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabBossList;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabNormalPlay;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabNormalSlot;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabSpecialFix;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabSpecialPlay;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabSpecialProb;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabSpecialSlot;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabTranscendEnchant;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabTranscendProb;
import l2s.gameserver.network.l2.c2s.adenlab.RequestExAdenlabUnlockBoss;
import l2s.gameserver.network.l2.c2s.blessing.RequestExBlessOptionCancel;
import l2s.gameserver.network.l2.c2s.blessing.RequestExBlessOptionEnchant;
import l2s.gameserver.network.l2.c2s.blessing.RequestExBlessOptionPutItem;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionCloseUI;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionFavoriteList;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionList;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionOpenUI;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionReceiveReward;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionRegister;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionSummary;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionUpdateFavorite;
import l2s.gameserver.network.l2.c2s.dyee.RequestExDyeeffectAcquireHiddenskill;
import l2s.gameserver.network.l2.c2s.dyee.RequestExDyeeffectEnchantNormalskill;
import l2s.gameserver.network.l2.c2s.dyee.RequestExDyeeffectEnchantProbInfo;
import l2s.gameserver.network.l2.c2s.dyee.RequestExDyeeffectEnchantReset;
import l2s.gameserver.network.l2.c2s.dyee.RequestExDyeeffectList;
import l2s.gameserver.network.l2.c2s.enchant.RequestExAddEnchantScrollItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExCancelEnchantItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExEnchantFailRewardInfo;
import l2s.gameserver.network.l2.c2s.enchant.RequestExFinishMultiEnchantScroll;
import l2s.gameserver.network.l2.c2s.enchant.RequestExMultiEnchantItemList;
import l2s.gameserver.network.l2.c2s.enchant.RequestExResetEnchantChallengePoint;
import l2s.gameserver.network.l2.c2s.enchant.RequestExSetEnchantChallengePoint;
import l2s.gameserver.network.l2.c2s.enchant.RequestExSetMultiEnchantItemList;
import l2s.gameserver.network.l2.c2s.enchant.RequestExStartMultiEnchantScroll;
import l2s.gameserver.network.l2.c2s.enchant.RequestExTryToPutEnchantSupportItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExTryToPutEnchantTargetItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExViewEnchantResult;
import l2s.gameserver.network.l2.c2s.enchant.RequestExViewMultiEnchantResult;
import l2s.gameserver.network.l2.c2s.events.RequestExBalthusToken;
import l2s.gameserver.network.l2.c2s.events.RequestExFestivalBMGame;
import l2s.gameserver.network.l2.c2s.events.RequestExFestivalBMInfo;
import l2s.gameserver.network.l2.c2s.huntpass.HuntpassSayhasToggle;
import l2s.gameserver.network.l2.c2s.huntpass.RequestHuntPassBuyPremium;
import l2s.gameserver.network.l2.c2s.huntpass.RequestHuntPassInfo;
import l2s.gameserver.network.l2.c2s.huntpass.RequestHuntPassReward;
import l2s.gameserver.network.l2.c2s.huntpass.RequestHuntPassRewardAll;
import l2s.gameserver.network.l2.c2s.itemrestore.RequestExPenaltyItemList;
import l2s.gameserver.network.l2.c2s.itemrestore.RequestExPenaltyItemRestore;
import l2s.gameserver.network.l2.c2s.items.autopeel.RequestExItemAutoPeel;
import l2s.gameserver.network.l2.c2s.items.autopeel.RequestExReadyItemAutoPeel;
import l2s.gameserver.network.l2.c2s.items.autopeel.RequestExStopItemAutoPeel;
import l2s.gameserver.network.l2.c2s.limitshop.RequestExPurchaseLimitShopHtmlOpen;
import l2s.gameserver.network.l2.c2s.limitshop.RequestExPurchaseLimitShopItemBuy;
import l2s.gameserver.network.l2.c2s.limitshop.RequestExPurchaseLimitShopItemList;
import l2s.gameserver.network.l2.c2s.locked.RequestExLockedItemCancel;
import l2s.gameserver.network.l2.c2s.locked.RequestExRequestLockedItem;
import l2s.gameserver.network.l2.c2s.locked.RequestExRequestUnlockedItem;
import l2s.gameserver.network.l2.c2s.locked.RequestExUnlockedItemCancel;
import l2s.gameserver.network.l2.c2s.magiclamp.RequestExMagicLampGameInfo;
import l2s.gameserver.network.l2.c2s.magiclamp.RequestExMagicLampGameStart;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaCompose;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaEquip;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaList;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaPotenEnchant;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaPotenEnchantReset;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaPotenOpenslot;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaPotenOpenslotProbInfo;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaPotenSelect;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaUnequip;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeContributionList;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeDonationInfo;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeDonationRequest;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeEnemyDelete;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeEnemyInfoList;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeEnemyRegister;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeRankingList;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeRankingMyInfo;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeV3Info;
import l2s.gameserver.network.l2.c2s.pledge.RequestExPledgeV3SetAnnounce;
import l2s.gameserver.network.l2.c2s.privatestoresearch.RequestExPrivateStoreSearchList;
import l2s.gameserver.network.l2.c2s.privatestoresearch.RequestExPrivateStoreSearchStatistics;
import l2s.gameserver.network.l2.c2s.prot_507.RequestExBlessOptionProbList;
import l2s.gameserver.network.l2.c2s.prot_507.RequestExChatBanEnd;
import l2s.gameserver.network.l2.c2s.prot_507.RequestExChatBanStart;
import l2s.gameserver.network.l2.c2s.prot_507.RequestExClassChange;
import l2s.gameserver.network.l2.c2s.prot_507.RequestExMatchinginzoneFieldEnterUserInfo;
import l2s.gameserver.network.l2.c2s.prot_507.RequestExRepairAllEquipment;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpBookShareRevengeList;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookKillerLocation;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookList;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookShareRevengeKillerLocation;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookShareRevengeReqShareRevengeInfo;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookShareRevengeSharedTeleportToKiller;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookShareRevengeTeleportToKiller;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookTeleportToKiller;
import l2s.gameserver.network.l2.c2s.quest.RequestExQuestAccept;
import l2s.gameserver.network.l2.c2s.quest.RequestExQuestAcceptableList;
import l2s.gameserver.network.l2.c2s.quest.RequestExQuestCancel;
import l2s.gameserver.network.l2.c2s.quest.RequestExQuestComplete;
import l2s.gameserver.network.l2.c2s.quest.RequestExQuestNotificationAll;
import l2s.gameserver.network.l2.c2s.quest.RequestExQuestTeleport;
import l2s.gameserver.network.l2.c2s.quest.RequestExQuestUi;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftExtract;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftRandomInfo;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftRandomLockSlot;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftRandomMake;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftRandomRefresh;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsActive;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsCloseUi;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsCombination;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsCombinationComplete;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsConfirmCombination;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsExchange;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsExchangeConfirm;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsIdSummon;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsOpenUi;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsSummon;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsSummonCloseUi;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsSummonList;
import l2s.gameserver.network.l2.c2s.relics.RequestExRelicsUpgrade;
import l2s.gameserver.network.l2.c2s.skill_enchant.RequestEnchantSkillInfo;
import l2s.gameserver.network.l2.c2s.skill_enchant.RequestExExtractSkillEnchant;
import l2s.gameserver.network.l2.c2s.skill_enchant.RequestExSkillEnchantCharge;
import l2s.gameserver.network.l2.c2s.skill_enchant.RequestExSkillEnchantConfirm;
import l2s.gameserver.network.l2.c2s.skill_enchant.RequestExSkillEnchantInfo;
import l2s.gameserver.network.l2.c2s.skill_enchant.RequestReqEnchantSkill;
import l2s.gameserver.network.l2.c2s.spExtract.RequestExSpExtractInfo;
import l2s.gameserver.network.l2.c2s.spExtract.RequestExSpExtractItem;
import l2s.gameserver.network.l2.c2s.spectating.RequestExUserWatcherAdd;
import l2s.gameserver.network.l2.c2s.spectating.RequestExUserWatcherDelete;
import l2s.gameserver.network.l2.c2s.spectating.RequestExUserWatcherTargetList;
import l2s.gameserver.network.l2.c2s.steadybox.RequestExSteadyBoxLoad;
import l2s.gameserver.network.l2.c2s.steadybox.RequestExSteadyGetReward;
import l2s.gameserver.network.l2.c2s.steadybox.RequestExSteadyOpenBox;
import l2s.gameserver.network.l2.c2s.steadybox.RequestExSteadyOpenSlot;
import l2s.gameserver.network.l2.c2s.subjugation.RequestExSubjugationGacha;
import l2s.gameserver.network.l2.c2s.subjugation.RequestExSubjugationGachaUI;
import l2s.gameserver.network.l2.c2s.subjugation.RequestExSubjugationList;
import l2s.gameserver.network.l2.c2s.subjugation.RequestExSubjugationRanking;
import l2s.gameserver.network.l2.c2s.teleport.RequestExRequestTeleport;
import l2s.gameserver.network.l2.c2s.teleport.RequestExSharedPositionSharingUI;
import l2s.gameserver.network.l2.c2s.teleport.RequestExSharedPositionTeleport;
import l2s.gameserver.network.l2.c2s.teleport.RequestExSharedPositionTeleportUI;
import l2s.gameserver.network.l2.c2s.teleport.RequestExTeleportFavoritesAddDel;
import l2s.gameserver.network.l2.c2s.teleport.RequestExTeleportFavoritesList;
import l2s.gameserver.network.l2.c2s.teleport.RequestExTeleportFavoritesUIToggle;
import l2s.gameserver.network.l2.c2s.timerestrictfield.RequestExTimeRestrictFieldList;
import l2s.gameserver.network.l2.c2s.timerestrictfield.RequestExTimeRestrictFieldUserEnter;
import l2s.gameserver.network.l2.c2s.timerestrictfield.RequestExTimeRestrictFieldUserLeave;
import l2s.gameserver.network.l2.c2s.worldexchange.RequestExWorldExchangeAveragePrice;
import l2s.gameserver.network.l2.c2s.worldexchange.RequestExWorldExchangeBuyItem;
import l2s.gameserver.network.l2.c2s.worldexchange.RequestExWorldExchangeItemList;
import l2s.gameserver.network.l2.c2s.worldexchange.RequestExWorldExchangeRegiItem;
import l2s.gameserver.network.l2.c2s.worldexchange.RequestExWorldExchangeSettleList;
import l2s.gameserver.network.l2.c2s.worldexchange.RequestExWorldExchangeSettleRecvResult;
import l2s.gameserver.network.l2.c2s.worldexchange.RequestExWorldExchangeTotalList;

public enum IncomingExPackets507 implements IIncomingPackets<GameClient>
{
	EX_DUMMY(null, ConnectionState.IN_GAME),
	EX_REQ_MANOR_LIST(ExReqManorList::new, ConnectionState.JOINING_GAME),
	EX_PROCURE_CROP_LIST(null, ConnectionState.IN_GAME),
	EX_SET_SEED(null, ConnectionState.IN_GAME),
	EX_SET_CROP(null, ConnectionState.IN_GAME),
	EX_WRITE_HERO_WORDS(RequestWriteHeroWords::new, ConnectionState.IN_GAME),
	EX_ASK_JOIN_MPCC(RequestExMPCCAskJoin::new, ConnectionState.IN_GAME),
	EX_ACCEPT_JOIN_MPCC(RequestExMPCCAcceptJoin::new, ConnectionState.IN_GAME),
	EX_OUST_FROM_MPCC(RequestExOustFromMPCC::new, ConnectionState.IN_GAME),
	EX_OUST_FROM_PARTY_ROOM(RequestOustFromPartyRoom::new, ConnectionState.IN_GAME),
	EX_DISMISS_PARTY_ROOM(RequestDismissPartyRoom::new, ConnectionState.IN_GAME),
	EX_WITHDRAW_PARTY_ROOM(RequestWithdrawPartyRoom::new, ConnectionState.IN_GAME),
	EX_HAND_OVER_PARTY_MASTER(RequestHandOverPartyMaster::new, ConnectionState.IN_GAME),
	EX_AUTO_SOULSHOT(RequestAutoSoulShot::new, ConnectionState.IN_GAME),
	EX_ENCHANT_SKILL_INFO(RequestEnchantSkillInfo::new, ConnectionState.IN_GAME),
	EX_REQ_ENCHANT_SKILL(RequestReqEnchantSkill::new, ConnectionState.IN_GAME),
	EX_PLEDGE_EMBLEM(RequestPledgeCrestLarge::new, ConnectionState.IN_GAME),
	EX_SET_PLEDGE_EMBLEM(RequestExSetPledgeCrestLargeFirstPart::new, ConnectionState.IN_GAME),
	EX_SET_ACADEMY_MASTER(RequestPledgeSetAcademyMaster::new, ConnectionState.IN_GAME),
	EX_PLEDGE_POWER_GRADE_LIST(RequestPledgePowerGradeList::new, ConnectionState.IN_GAME),
	EX_VIEW_PLEDGE_POWER(RequestPledgeMemberPowerInfo::new, ConnectionState.IN_GAME),
	EX_SET_PLEDGE_POWER_GRADE(RequestPledgeSetMemberPowerGrade::new, ConnectionState.IN_GAME),
	EX_VIEW_PLEDGE_MEMBER_INFO(RequestPledgeMemberInfo::new, ConnectionState.IN_GAME),
	EX_VIEW_PLEDGE_WARLIST(RequestPledgeWarList::new, ConnectionState.IN_GAME),
	EX_FISH_RANKING(null, ConnectionState.IN_GAME),
	EX_PCCAFE_COUPON_USE(RequestPCCafeCouponUse::new, ConnectionState.IN_GAME),
	EX_ORC_MOVE(null, ConnectionState.IN_GAME),
	EX_DUEL_ASK_START(RequestDuelStart::new, ConnectionState.IN_GAME),
	EX_DUEL_ACCEPT_START(RequestDuelAnswerStart::new, ConnectionState.IN_GAME),
	EX_SET_TUTORIAL(RequestTutorialClientEvent::new, ConnectionState.IN_GAME),
	EX_RQ_ITEMLINK(RequestExRqItemLink::new, ConnectionState.IN_GAME),
	EX_CAN_NOT_MOVE_ANYMORE_IN_AIRSHIP(CannotMoveAnymore.AirShip::new, ConnectionState.IN_GAME),
	EX_MOVE_TO_LOCATION_IN_AIRSHIP(null, ConnectionState.IN_GAME),
	EX_LOAD_UI_SETTING(RequestKeyMapping::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME),
	EX_SAVE_UI_SETTING(RequestSaveKeyMapping::new, ConnectionState.IN_GAME),
	EX_REQUEST_BASE_ATTRIBUTE_CANCEL(RequestExRemoveItemAttribute::new, ConnectionState.IN_GAME),
	EX_CHANGE_INVENTORY_SLOT(RequestSaveInventoryOrder::new, ConnectionState.IN_GAME),
	EX_EXIT_PARTY_MATCHING_WAITING_ROOM(RequestExitPartyMatchingWaitingRoom::new, ConnectionState.IN_GAME),
	EX_TRY_TO_PUT_ITEM_FOR_VARIATION_MAKE(RequestConfirmTargetItem::new, ConnectionState.IN_GAME),
	EX_TRY_TO_PUT_INTENSIVE_FOR_VARIATION_MAKE(RequestConfirmRefinerItem::new, ConnectionState.IN_GAME),
	EX_TRY_TO_PUT_COMMISSION_FOR_VARIATION_MAKE(RequestConfirmGemStone::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_OBSERVER_END(RequestOlympiadObserverEnd::new, ConnectionState.IN_GAME),
	EX_CURSED_WEAPON_LIST(null, ConnectionState.IN_GAME),
	EX_EXISTING_CURSED_WEAPON_LOCATION(null, ConnectionState.IN_GAME),
	EX_REORGANIZE_PLEDGE_MEMBER(RequestPledgeReorganizeMember::new, ConnectionState.IN_GAME),
	EX_MPCC_SHOW_PARTY_MEMBERS_INFO(RequestExMPCCShowPartyMembersInfo::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_MATCH_LIST(RequestExOlympiadObserverEnd::new, ConnectionState.IN_GAME),
	EX_ASK_JOIN_PARTY_ROOM(RequestAskJoinPartyRoom::new, ConnectionState.IN_GAME),
	EX_ANSWER_JOIN_PARTY_ROOM(AnswerJoinPartyRoom::new, ConnectionState.IN_GAME),
	EX_LIST_PARTY_MATCHING_WAITING_ROOM(RequestListPartyMatchingWaitingRoom::new, ConnectionState.IN_GAME),
	EX_CHOOSE_INVENTORY_ATTRIBUTE_ITEM(null, ConnectionState.IN_GAME),
	EX_CHARACTER_BACK(GotoLobby::new, ConnectionState.AUTHENTICATED),
	EX_CANNOT_AIRSHIP_MOVE_ANYMORE(null, ConnectionState.IN_GAME),
	EX_MOVE_TO_LOCATION_AIRSHIP(null, ConnectionState.IN_GAME),
	EX_ITEM_AUCTION_BID(null, ConnectionState.IN_GAME),
	EX_ITEM_AUCTION_INFO(null, ConnectionState.IN_GAME),
	EX_CHANGE_NAME(RequestExChangeName::new, ConnectionState.IN_GAME),
	EX_SHOW_CASTLE_INFO(RequestAllCastleInfo::new, ConnectionState.IN_GAME),
	EX_SHOW_FORTRESS_INFO(RequestExShowFortressInfo::new, ConnectionState.IN_GAME),
	EX_SHOW_AGIT_INFO(RequestAllAgitInfo::new, ConnectionState.IN_GAME),
	EX_SHOW_FORTRESS_SIEGE_INFO(null, ConnectionState.IN_GAME),
	EX_GET_BOSS_RECORD(null, ConnectionState.IN_GAME),
	EX_TRY_TO_MAKE_VARIATION(RequestRefine::new, ConnectionState.IN_GAME),
	EX_TRY_TO_PUT_ITEM_FOR_VARIATION_CANCEL(RequestConfirmCancelItem::new, ConnectionState.IN_GAME),
	EX_CLICK_VARIATION_CANCEL_BUTTON(RequestRefineCancel::new, ConnectionState.IN_GAME),
	EX_MAGIC_SKILL_USE_GROUND(RequestExMagicSkillUseGround::new, ConnectionState.IN_GAME),
	EX_DUEL_SURRENDER(RequestDuelSurrender::new, ConnectionState.IN_GAME),
	EX_ENCHANT_SKILL_INFO_DETAIL(null, ConnectionState.IN_GAME),
	EX_REQUEST_ANTI_FREE_SERVER(null, ConnectionState.IN_GAME),
	EX_SHOW_FORTRESS_MAP_INFO(null, ConnectionState.IN_GAME),
	EX_REQUEST_PVPMATCH_RECORD(RequestPVPMatchRecord::new, ConnectionState.IN_GAME),
	EX_PRIVATE_STORE_WHOLE_SET_MSG(SetPrivateStoreWholeMsg::new, ConnectionState.IN_GAME),
	EX_DISPEL(RequestDispel::new, ConnectionState.IN_GAME),
	EX_TRY_TO_PUT_ENCHANT_TARGET_ITEM(RequestExTryToPutEnchantTargetItem::new, ConnectionState.IN_GAME),
	EX_TRY_TO_PUT_ENCHANT_SUPPORT_ITEM(RequestExTryToPutEnchantSupportItem::new, ConnectionState.IN_GAME),
	EX_CANCEL_ENCHANT_ITEM(RequestExCancelEnchantItem::new, ConnectionState.IN_GAME),
	EX_CHANGE_NICKNAME_COLOR(RequestChangeNicknameColor::new, ConnectionState.IN_GAME),
	EX_REQUEST_RESET_NICKNAME(RequestResetNickname::new, ConnectionState.IN_GAME),
	EX_USER_BOOKMARK(null, ConnectionState.IN_GAME), // more implement packet opcode				
	EX_WITHDRAW_PREMIUM_ITEM(null, ConnectionState.IN_GAME),
	EX_JUMP(RequestExJump::new, ConnectionState.IN_GAME),
	EX_START_REQUEST_PVPMATCH_CC_RANK(null, ConnectionState.IN_GAME),
	EX_STOP_REQUEST_PVPMATCH_CC_RANK(null, ConnectionState.IN_GAME),
	EX_NOTIFY_START_MINIGAME(NotifyStartMiniGame::new, ConnectionState.IN_GAME),
	EX_REQUEST_REGISTER_DOMINION(RequestExJoinDominionWar::new, ConnectionState.IN_GAME),
	EX_REQUEST_DOMINION_INFO(RequestExDominionInfo::new, ConnectionState.IN_GAME),
	EX_CLEFT_ENTER(RequestExCleftEnter::new, ConnectionState.IN_GAME),
	EX_BLOCK_UPSET_ENTER(null, ConnectionState.IN_GAME),
	EX_END_SCENE_PLAYER(RequestExEndScenePlayer::new, ConnectionState.IN_GAME),
	EX_BLOCK_UPSET_VOTE(null, ConnectionState.IN_GAME),
	EX_LIST_MPCC_WAITING(RequestExListMpccWaiting::new, ConnectionState.IN_GAME),
	EX_MANAGE_MPCC_ROOM(RequestExManageMpccRoom::new, ConnectionState.IN_GAME),
	EX_JOIN_MPCC_ROOM(RequestExJoinMpccRoom::new, ConnectionState.IN_GAME),
	EX_OUST_FROM_MPCC_ROOM(RequestExOustFromMpccRoom::new, ConnectionState.IN_GAME),
	EX_DISMISS_MPCC_ROOM(RequestExDismissMpccRoom::new, ConnectionState.IN_GAME),
	EX_WITHDRAW_MPCC_ROOM(RequestExWithdrawMpccRoom::new, ConnectionState.IN_GAME),
	EX_SEED_PHASE(RequestExSeedPhase::new, ConnectionState.IN_GAME),
	EX_MPCC_PARTYMASTER_LIST(RequestExMpccPartymasterList::new, ConnectionState.IN_GAME),
	EX_REQUEST_POST_ITEM_LIST(RequestExPostItemList::new, ConnectionState.IN_GAME),
	EX_SEND_POST(RequestExSendPost::new, ConnectionState.IN_GAME),
	EX_REQUEST_RECEIVED_POST_LIST(RequestExRequestReceivedPostList::new, ConnectionState.IN_GAME),
	EX_DELETE_RECEIVED_POST(RequestExDeleteReceivedPost::new, ConnectionState.IN_GAME),
	EX_REQUEST_RECEIVED_POST(RequestExRequestReceivedPost::new, ConnectionState.IN_GAME),
	EX_RECEIVE_POST(RequestExReceivePost::new, ConnectionState.IN_GAME),
	EX_REJECT_POST(RequestExRejectPost::new, ConnectionState.IN_GAME),
	EX_REQUEST_SENT_POST_LIST(RequestExRequestSentPostList::new, ConnectionState.IN_GAME),
	EX_DELETE_SENT_POST(RequestExDeleteSentPost::new, ConnectionState.IN_GAME),
	EX_REQUEST_SENT_POST(RequestExRequestSentPost::new, ConnectionState.IN_GAME),
	EX_CANCEL_SEND_POST(RequestExCancelSentPost::new, ConnectionState.IN_GAME),
	EX_POST_ITEM_FEE(RequestExPostItemFee::new, ConnectionState.IN_GAME),
	EX_REQUEST_SHOW_PETITION(RequestExShowNewUserPetition::new, ConnectionState.IN_GAME),
	EX_REQUEST_SHOWSTEP_TWO(RequestExShowStepTwo::new, ConnectionState.IN_GAME),
	EX_REQUEST_SHOWSTEP_THREE(RequestExShowStepThree::new, ConnectionState.IN_GAME),
	EX_CONNECT_TO_RAID_SERVER(null, ConnectionState.IN_GAME),
	EX_RETURN_FROM_RAID(null, ConnectionState.IN_GAME),
	EX_REFUND_REQ(RequestExRefundItem::new, ConnectionState.IN_GAME),
	EX_BUY_SELL_UI_CLOSE_REQ(RequestExBuySellUIClose::new, ConnectionState.IN_GAME),
	EX_EVENT_MATCH(RequestExEventMatchObserverEnd::new, ConnectionState.IN_GAME),
	EX_PARTY_LOOTING_MODIFY(RequestPartyLootModification::new, ConnectionState.IN_GAME),
	EX_PARTY_LOOTING_MODIFY_AGREEMENT(AnswerPartyLootModification::new, ConnectionState.IN_GAME),
	EX_ANSWER_COUPLE_ACTION(AnswerCoupleAction::new, ConnectionState.IN_GAME),
	EX_BR_LOAD_EVENT_TOP_RANKERS_REQ(RequestExBR_EventRankerList::new, ConnectionState.IN_GAME),
	EX_ASK_MY_MEMBERSHIP(null, ConnectionState.IN_GAME),
	EX_QUEST_NPC_LOG_LIST(RequestAddExpandQuestAlarm::new, ConnectionState.IN_GAME),
	EX_VOTE_SYSTEM(RequestVoteNew::new, ConnectionState.IN_GAME),
	EX_GETON_SHUTTLE(RequestGetOnShuttle::new, ConnectionState.IN_GAME),
	EX_GETOFF_SHUTTLE(RequestGetOffShuttle::new, ConnectionState.IN_GAME),
	EX_MOVE_TO_LOCATION_IN_SHUTTLE(RequestMoveToLocationInShuttle::new, ConnectionState.IN_GAME),
	EX_CAN_NOT_MOVE_ANYMORE_IN_SHUTTLE(CannotMoveAnymore.Shuttle::new, ConnectionState.IN_GAME),
	EX_AGITAUCTION_CMD(null, ConnectionState.IN_GAME),
	EX_ADD_POST_FRIEND(RequestExAddPostFriendForPostBox::new, ConnectionState.IN_GAME),
	EX_DELETE_POST_FRIEND(RequestExDeletePostFriendForPostBox::new, ConnectionState.IN_GAME),
	EX_SHOW_POST_FRIEND(RequestExShowPostFriendListForPostBox::new, ConnectionState.IN_GAME),
	EX_FRIEND_LIST_FOR_POSTBOX(RequestExFriendListForPostBox::new, ConnectionState.IN_GAME),
	EX_GFX_OLYMPIAD(RequestOlympiadMatchList::new, ConnectionState.IN_GAME),
	EX_BR_GAME_POINT_REQ(RequestExBR_GamePoint::new, ConnectionState.IN_GAME),
	EX_BR_PRODUCT_LIST_REQ(RequestExBrProductListReq::new, ConnectionState.IN_GAME),
	EX_BR_PRODUCT_INFO_REQ(RequestExBrProductInfoReq::new, ConnectionState.IN_GAME),
	EX_BR_BUY_PRODUCT_REQ(RequestExBR_BuyProduct::new, ConnectionState.IN_GAME),
	EX_BR_RECENT_PRODUCT_REQ(RequestExBrRecentProductReq::new, ConnectionState.IN_GAME),
	EX_BR_MINIGAME_LOAD_SCORES_REQ(RequestBR_MiniGameLoadScores::new, ConnectionState.IN_GAME),
	EX_BR_MINIGAME_INSERT_SCORE_REQ(RequestBR_MiniGameInsertScore::new, ConnectionState.IN_GAME),
	EX_BR_SET_LECTURE_MARK_REQ(RequestExBR_LectureMark::new, ConnectionState.IN_GAME),
	EX_REQUEST_CRYSTALITEM_INFO(RequestCrystallizeEstimate::new, ConnectionState.IN_GAME),
	EX_REQUEST_CRYSTALITEM_CANCEL(RequestCrystallizeItemCancel::new, ConnectionState.IN_GAME),
	EX_STOP_SCENE_PLAYER(RequestExEscapeScene::new, ConnectionState.IN_GAME),
	EX_FLY_MOVE(RequestFlyMove::new, ConnectionState.IN_GAME),
	EX_SURRENDER_PLEDGE_WAR(null, ConnectionState.IN_GAME),
	EX_DYNAMIC_QUEST(null, ConnectionState.IN_GAME),
	EX_FRIEND_DETAIL_INFO(RequestFriendDetailInfo::new, ConnectionState.IN_GAME),
	EX_UPDATE_FRIEND_MEMO(RequestUpdateFriendMemo::new, ConnectionState.IN_GAME),
	EX_UPDATE_BLOCK_MEMO(RequestUpdateBlockMemo::new, ConnectionState.IN_GAME),
	EX_LOAD_INZONE_PARTY_HISTORY(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_ITEM_LIST(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_INFO(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_REGISTER(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_CANCEL(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_DELETE(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_SEARCH(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_BUY_INFO(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_BUY_ITEM(null, ConnectionState.IN_GAME),
	EX_REQUEST_COMMISSION_REGISTERED_ITEM(null, ConnectionState.IN_GAME),
	EX_CALL_TO_CHANGE_CLASS(null, ConnectionState.IN_GAME),
	EX_CHANGE_TO_AWAKENED_CLASS(null, ConnectionState.IN_GAME),
	EX_NOT_USED_163(null, ConnectionState.IN_GAME),
	EX_NOT_USED_164(null, ConnectionState.IN_GAME),
	EX_REQUEST_WEB_SESSION_ID(null, ConnectionState.IN_GAME),
	EX_2ND_PASSWORD_CHECK(RequestEx2ndPasswordCheck::new, ConnectionState.AUTHENTICATED),
	EX_2ND_PASSWORD_VERIFY(RequestEx2ndPasswordVerify::new, ConnectionState.AUTHENTICATED),
	EX_2ND_PASSWORD_REQ(RequestEx2ndPasswordReq::new, ConnectionState.AUTHENTICATED),
	EX_CHECK_CHAR_NAME(RequestCharacterNameCreatable::new, ConnectionState.AUTHENTICATED),
	EX_REQUEST_GOODS_INVENTORY_INFO(RequestGoodsInventoryInfo::new, ConnectionState.IN_GAME),
	EX_REQUEST_USE_GOODS_IVENTORY_ITEM(null, ConnectionState.IN_GAME),
	EX_NOTIFY_PLAY_START(RequestFirstPlayStart::new, ConnectionState.IN_GAME),
	EX_FLY_MOVE_START(RequestFlyMoveStart::new, ConnectionState.IN_GAME),
	EX_USER_HARDWARE_INFO(RequestHardWareInfo::new, ConnectionState.values()),
	EX_USER_INTERFACE_INFO(null, ConnectionState.IN_GAME),
	EX_CHANGE_ATTRIBUTE_ITEM(SendChangeAttributeTargetItem::new, ConnectionState.IN_GAME),
	EX_REQUEST_CHANGE_ATTRIBUTE(RequestChangeAttributeItem::new, ConnectionState.IN_GAME),
	EX_CHANGE_ATTRIBUTE_CANCEL(RequestChangeAttributeCancel::new, ConnectionState.IN_GAME),
	EX_BR_BUY_PRODUCT_GIFT_REQ(RequestExBrBuyProductGiftReq::new, ConnectionState.IN_GAME),
	EX_MENTOR_ADD(null, ConnectionState.IN_GAME),
	EX_MENTOR_CANCEL(null, ConnectionState.IN_GAME),
	EX_MENTOR_LIST(null, ConnectionState.IN_GAME),
	EX_REQUEST_MENTOR_ADD(null, ConnectionState.IN_GAME),
	EX_MENTEE_WAITING_LIST(null, ConnectionState.IN_GAME),
	EX_JOIN_PLEDGE_BY_NAME(RequestJoinPledgeByName::new, ConnectionState.IN_GAME),
	EX_INZONE_WAITING_TIME(RequestInzoneWaitingTime::new, ConnectionState.IN_GAME),
	EX_JOIN_CURIOUS_HOUSE(null, ConnectionState.IN_GAME),
	EX_CANCEL_CURIOUS_HOUSE(null, ConnectionState.IN_GAME),
	EX_LEAVE_CURIOUS_HOUSE(null, ConnectionState.IN_GAME),
	EX_OBSERVE_LIST_CURIOUS_HOUSE(null, ConnectionState.IN_GAME),
	EX_OBSERVE_CURIOUS_HOUSE(null, ConnectionState.IN_GAME),
	EX_EXIT_OBSERVE_CURIOUS_HOUSE(null, ConnectionState.IN_GAME),
	EX_REQ_CURIOUS_HOUSE_HTML(null, ConnectionState.IN_GAME),
	EX_REQ_CURIOUS_HOUSE_RECORD(null, ConnectionState.IN_GAME),
	EX_SYS_STRING(null, ConnectionState.IN_GAME),
	EX_TRY_TO_PUT_SHAPE_SHIFTING_TARGET_ITEM(RequestExTryToPutShapeShiftingTargetItem::new, ConnectionState.IN_GAME),
	EX_TRY_TO_PUT_SHAPE_SHIFTING_EXTRACTION_ITEM(RequestExTryToPutShapeShiftingEnchantSupportItem::new, ConnectionState.IN_GAME),
	EX_CANCEL_SHAPE_SHIFTING(RequestExCancelShapeShiftingItem::new, ConnectionState.IN_GAME),
	EX_REQUEST_SHAPE_SHIFTING(RequestShapeShiftingItem::new, ConnectionState.IN_GAME),
	EX_NCGUARD(null, ConnectionState.IN_GAME),
	EX_REQUEST_KALIE_TOKEN(null, ConnectionState.IN_GAME),
	EX_REQUEST_SHOW_REGIST_BEAUTY(RequestShowBeautyList::new, ConnectionState.IN_GAME),
	EX_REQUEST_REGIST_BEAUTY(RequestRegistBeauty::new, ConnectionState.IN_GAME),
	EX_REQUEST_SHOW_RESET_BEAUTY(null, ConnectionState.IN_GAME),
	EX_REQUEST_RESET_BEAUTY(RequestShowResetShopList::new, ConnectionState.IN_GAME),
	EX_CHECK_SPEEDHACK(null, ConnectionState.IN_GAME),
	EX_BR_ADD_INTERESTED_PRODUCT(RequestExBrAddInterestedProduct::new, ConnectionState.IN_GAME),
	EX_BR_DELETE_INTERESTED_PRODUCT(RequestExBrDeleteInterestedProduct::new, ConnectionState.IN_GAME),
	EX_BR_EXIST_NEW_PRODUCT_REQ(RequestBR_NewIConCashBtnWnd::new, ConnectionState.IN_GAME),
	EX_EVENT_CAMPAIGN_INFO(null, ConnectionState.IN_GAME),
	EX_PLEDGE_RECRUIT_INFO(RequestPledgeRecruitInfo::new, ConnectionState.IN_GAME),
	EX_PLEDGE_RECRUIT_BOARD_SEARCH(RequestPledgeRecruitBoardSearch::new, ConnectionState.IN_GAME),
	EX_PLEDGE_RECRUIT_BOARD_APPLY(RequestPledgeRecruitBoardAccess::new, ConnectionState.IN_GAME),
	EX_PLEDGE_RECRUIT_BOARD_DETAIL(RequestPledgeRecruitBoardDetail::new, ConnectionState.IN_GAME),
	EX_PLEDGE_WAITING_LIST_APPLY(RequestPledgeWaitingApply::new, ConnectionState.IN_GAME),
	EX_PLEDGE_WAITING_LIST_APPLIED(RequestPledgeWaitingApplied::new, ConnectionState.IN_GAME),
	EX_PLEDGE_WAITING_LIST(RequestPledgeWaitingList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_WAITING_USER(RequestPledgeWaitingUser::new, ConnectionState.IN_GAME),
	EX_PLEDGE_WAITING_USER_ACCEPT(RequestPledgeWaitingUserAccept::new, ConnectionState.IN_GAME),
	EX_PLEDGE_DRAFT_LIST_SEARCH(RequestPledgeDraftListSearch::new, ConnectionState.IN_GAME),
	EX_PLEDGE_DRAFT_LIST_APPLY(RequestPledgeDraftListApply::new, ConnectionState.IN_GAME),
	EX_PLEDGE_RECRUIT_APPLY_INFO(RequestPledgeRecruitApplyInfo::new, ConnectionState.IN_GAME),
	EX_PLEDGE_JOIN_SYS(RequestPledgeJoinSys::new, ConnectionState.IN_GAME),
	EX_RESPONSE_WEB_PETITION_ALARM(null, ConnectionState.IN_GAME),
	EX_NOTIFY_EXIT_BEAUTYSHOP(NotifyExitBeautyshop::new, ConnectionState.IN_GAME),
	EX_EVENT_REGISTER_XMAS_WISHCARD(null, ConnectionState.IN_GAME),
	EX_ENCHANT_SCROLL_ITEM_ADD(RequestExAddEnchantScrollItem::new, ConnectionState.IN_GAME),
	EX_ENCHANT_SUPPORT_ITEM_REMOVE(RequestExRemoveEnchantSupportItem::new, ConnectionState.IN_GAME),
	EX_SELECT_CARD_REWARD(null, ConnectionState.IN_GAME),
	EX_DIVIDE_ADENA_START(RequestDivideAdenaStart::new, ConnectionState.IN_GAME),
	EX_DIVIDE_ADENA_CANCEL(RequestDivideAdenaCancel::new, ConnectionState.IN_GAME),
	EX_DIVIDE_ADENA(RequestDivideAdena::new, ConnectionState.IN_GAME),
	EX_ACQUIRE_POTENTIAL_SKILL(RequestExAcquirePotentialSkill::new, ConnectionState.IN_GAME),
	EX_REQUEST_POTENTIAL_SKILL_LIST(RequestExRequestPotentialSkillList::new, ConnectionState.IN_GAME),
	EX_RESET_POTENTIAL_SKILL(RequestExResetPotentialSkill::new, ConnectionState.IN_GAME),
	EX_CHANGE_POTENTIAL_POINT(RequestExChangePotentialPoint::new, ConnectionState.IN_GAME),
	EX_STOP_MOVE(RequestStopMove::new, ConnectionState.IN_GAME),
	EX_ABILITY_WND_OPEN(RequestAbilityWndOpen::new, ConnectionState.IN_GAME),
	EX_ABILITY_WND_CLOSE(RequestAbilityWndClose::new, ConnectionState.IN_GAME),
	EX_START_LUCKY_GAME(RequestLuckyGameStartInfo::new, ConnectionState.IN_GAME),
	EX_BETTING_LUCKY_GAME(RequestLuckyGamePlay::new, ConnectionState.IN_GAME),
	EX_TRAININGZONE_LEAVING(NotifyTrainingRoomEnd::new, ConnectionState.IN_GAME),
	EX_ENCHANT_ONE(RequestNewEnchantPushOne::new, ConnectionState.IN_GAME),
	EX_ENCHANT_ONE_REMOVE(RequestNewEnchantRemoveOne::new, ConnectionState.IN_GAME),
	EX_ENCHANT_TWO(RequestNewEnchantPushTwo::new, ConnectionState.IN_GAME),
	EX_ENCHANT_TWO_REMOVE(RequestNewEnchantRemoveTwo::new, ConnectionState.IN_GAME),
	EX_ENCHANT_CLOSE(RequestNewEnchantClose::new, ConnectionState.IN_GAME),
	EX_ENCHANT_TRY(RequestNewEnchantTry::new, ConnectionState.IN_GAME),
	EX_ENCHANT_RETRY_TO_PUT_ITEMS(RequestNewEnchantRetryToPutItems::new, ConnectionState.IN_GAME),
	EX_REQUEST_CARD_REWARD_LIST(null, ConnectionState.IN_GAME),
	EX_REQUEST_ACCOUNT_ATTENDANCE_INFO(null, ConnectionState.IN_GAME),
	EX_REQUEST_ACCOUNT_ATTENDANCE_REWARD(null, ConnectionState.IN_GAME),
	EX_TARGET(RequestTargetActionMenu::new, ConnectionState.IN_GAME),
	EX_SELECTED_QUEST_ZONEID(ExSendSelectedQuestZoneID::new, ConnectionState.IN_GAME),
	EX_ALCHEMY_SKILL_LIST(null, ConnectionState.IN_GAME),
	EX_TRY_MIX_CUBE(null, ConnectionState.IN_GAME),
	REQUEST_ALCHEMY_CONVERSION(null, ConnectionState.IN_GAME),
	EX_EXECUTED_UIEVENTS_COUNT(null, ConnectionState.IN_GAME),
	EX_CLIENT_INI(ExSendClientINI::new, ConnectionState.IN_GAME),
	EX_REQUEST_AUTOFISH(RequestExAutoFish::new, ConnectionState.IN_GAME),
	EX_REQUEST_VIP_ATTENDANCE_ITEMLIST(RequestVipAttendanceItemList::new, ConnectionState.IN_GAME),
	EX_REQUEST_VIP_ATTENDANCE_CHECK(RequestVipAttendanceCheck::new, ConnectionState.IN_GAME),
	EX_TRY_ENSOUL(RequestItemEnsoul::new, ConnectionState.IN_GAME),
	EX_CASTLEWAR_SEASON_REWARD(null, ConnectionState.IN_GAME),
	EX_BR_VIP_PRODUCT_LIST_REQ(RequestVipProductList::new, ConnectionState.IN_GAME),
	EX_REQUEST_LUCKY_GAME_INFO(RequestVipLuckyGameInfo::new, ConnectionState.IN_GAME),
	EX_REQUEST_LUCKY_GAME_ITEMLIST(RequestVipLuckyGameItemList::new, ConnectionState.IN_GAME),
	EX_REQUEST_LUCKY_GAME_BONUS(RequestVipLuckyGameBonus::new, ConnectionState.IN_GAME),
	EX_VIP_INFO(ExRequestVipInfo::new, ConnectionState.IN_GAME),
	EX_CAPTCHA_ANSWER(null, ConnectionState.IN_GAME),
	EX_REFRESH_CAPTCHA_IMAGE(null, ConnectionState.IN_GAME),
	EX_PLEDGE_SIGNIN(RequestPledgeSignInForOpenJoiningMethod::new, ConnectionState.IN_GAME),
	EX_REQUEST_MATCH_ARENA(null, ConnectionState.IN_GAME),
	EX_CONFIRM_MATCH_ARENA(null, ConnectionState.IN_GAME),
	EX_CANCEL_MATCH_ARENA(null, ConnectionState.IN_GAME),
	EX_CHANGE_CLASS_ARENA(null, ConnectionState.IN_GAME),
	EX_CONFIRM_CLASS_ARENA(null, ConnectionState.IN_GAME),
	EX_DECO_NPC_INFO(null, ConnectionState.IN_GAME),
	EX_DECO_NPC_SET(null, ConnectionState.IN_GAME),
	EX_FACTION_INFO(null, ConnectionState.IN_GAME),
	EX_EXIT_ARENA(null, ConnectionState.IN_GAME),
	EX_REQUEST_BALTHUS_TOKEN(RequestExBalthusToken::new, ConnectionState.IN_GAME),
	EX_PARTY_MATCHING_ROOM_HISTORY(RequestPartyMatchingHistory::new, ConnectionState.IN_GAME),
	EX_ARENA_CUSTOM_NOTIFICATION(null, ConnectionState.IN_GAME),
	EX_TODOLIST(RequestTodoList::new, ConnectionState.IN_GAME),
	EX_TODOLIST_HTML(RequestTodoListHTML::new, ConnectionState.IN_GAME),
	EX_ONE_DAY_RECEIVE_REWARD(RequestOneDayRewardReceive::new, ConnectionState.IN_GAME),
	EX_QUEUETICKET(null, ConnectionState.IN_GAME),
	EX_PLEDGE_BONUS_UI_OPEN(RequestPledgeBonusOpen::new, ConnectionState.IN_GAME),
	EX_PLEDGE_BONUS_REWARD_LIST(RequestPledgeBonusRewardList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_BONUS_RECEIVE_REWARD(RequestPledgeBonusReward::new, ConnectionState.IN_GAME),
	EX_SSO_AUTHNTOKEN_REQ(null, ConnectionState.IN_GAME),
	EX_QUEUETICKET_LOGIN(null, ConnectionState.IN_GAME),
	EX_BLOCK_DETAIL_INFO(RequestBlockMemoInfo::new, ConnectionState.IN_GAME),
	EX_TRY_ENSOUL_EXTRACTION(RequestTryEnSoulExtraction::new, ConnectionState.IN_GAME),
	EX_RAID_BOSS_SPAWN_INFO(RequestRaidBossSpawnInfo::new, ConnectionState.IN_GAME),
	EX_RAID_SERVER_INFO(RequestRaidServerInfo::new, ConnectionState.IN_GAME),
	EX_SHOW_AGIT_SIEGE_INFO(RequestShowAgitSiegeInfo::new, ConnectionState.IN_GAME),
	EX_ITEM_AUCTION_STATUS(RequestItemAuctionStatus::new, ConnectionState.IN_GAME),
	EX_MONSTER_BOOK_OPEN(null, ConnectionState.IN_GAME),
	EX_MONSTER_BOOK_CLOSE(null, ConnectionState.IN_GAME),
	EX_REQ_MONSTER_BOOK_REWARD(null, ConnectionState.IN_GAME),
	EX_MATCHGROUP(null, ConnectionState.IN_GAME),
	EX_MATCHGROUP_ASK(null, ConnectionState.IN_GAME),
	EX_MATCHGROUP_ANSWER(null, ConnectionState.IN_GAME),
	EX_MATCHGROUP_WITHDRAW(null, ConnectionState.IN_GAME),
	EX_MATCHGROUP_OUST(null, ConnectionState.IN_GAME),
	EX_MATCHGROUP_CHANGE_MASTER(null, ConnectionState.IN_GAME),
	EX_UPGRADE_SYSTEM_REQUEST(RequestUpgradeSystemResult::new, ConnectionState.IN_GAME),
	EX_CARD_UPDOWN_PICK_NUMB(null, ConnectionState.IN_GAME),
	EX_CARD_UPDOWN_GAME_REWARD_REQUEST(null, ConnectionState.IN_GAME),
	EX_CARD_UPDOWN_GAME_RETRY(null, ConnectionState.IN_GAME),
	EX_CARD_UPDOWN_GAME_QUIT(null, ConnectionState.IN_GAME),
	EX_ARENA_RANK_ALL(null, ConnectionState.IN_GAME),
	EX_ARENA_MYRANK(null, ConnectionState.IN_GAME),
	EX_SWAP_AGATHION_SLOT_ITEMS(RequestSwapAgathionSlotItems::new, ConnectionState.IN_GAME),
	EX_PLEDGE_CONTRIBUTION_RANK(null, ConnectionState.IN_GAME),
	EX_PLEDGE_CONTRIBUTION_INFO(null, ConnectionState.IN_GAME),
	EX_PLEDGE_CONTRIBUTION_REWARD(null, ConnectionState.IN_GAME),
	EX_PLEDGE_LEVEL_UP(null, ConnectionState.IN_GAME),
	EX_PLEDGE_MISSION_INFO(null, ConnectionState.IN_GAME),
	EX_PLEDGE_MISSION_REWARD(null, ConnectionState.IN_GAME),
	EX_PLEDGE_MASTERY_INFO(null, ConnectionState.IN_GAME),
	EX_PLEDGE_MASTERY_SET(null, ConnectionState.IN_GAME),
	EX_PLEDGE_MASTERY_RESET(null, ConnectionState.IN_GAME),
	EX_PLEDGE_SKILL_INFO(null, ConnectionState.IN_GAME),
	EX_PLEDGE_SKILL_ACTIVATE(null, ConnectionState.IN_GAME),
	EX_PLEDGE_ITEM_LIST(null, ConnectionState.IN_GAME),
	EX_PLEDGE_ITEM_ACTIVATE(null, ConnectionState.IN_GAME),
	EX_PLEDGE_ANNOUNCE(null, ConnectionState.IN_GAME),
	EX_PLEDGE_ANNOUNCE_SET(null, ConnectionState.IN_GAME),
	EX_CREATE_PLEDGE(RequestExCreatePledge::new, ConnectionState.IN_GAME),
	EX_PLEDGE_ITEM_INFO(null, ConnectionState.IN_GAME),
	EX_PLEDGE_ITEM_BUY(null, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_INFO(RequestExElementalSpiritInfo::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_EXTRACT_INFO(RequestExElementalSpiritExtractInfo::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_EXTRACT(RequestExElementalSpiritExtract::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_EVOLUTION_INFO(RequestExElementalSpiritEvolutionInfo::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_EVOLUTION(RequestExElementalSpiritEvolution::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_SET_TALENT(RequestExElementalSpiritSetTalent::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_INIT_TALENT(RequestExElementalSpiritInitTalent::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_ABSORB_INFO(RequestExElementalSpiritAbsorbInfo::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_ABSORB(RequestExElementalSpiritAbsorb::new, ConnectionState.IN_GAME),
	EX_REQUEST_LOCKED_ITEM(RequestExRequestLockedItem::new, ConnectionState.IN_GAME),
	EX_REQUEST_UNLOCKED_ITEM(RequestExRequestUnlockedItem::new, ConnectionState.IN_GAME),
	EX_LOCKED_ITEM_CANCEL(RequestExLockedItemCancel::new, ConnectionState.IN_GAME),
	EX_UNLOCKED_ITEM_CANCEL(RequestExUnlockedItemCancel::new, ConnectionState.IN_GAME),
	EX_BLOCK_PACKET_FOR_AD(RequestBlockPacketForAd::new, ConnectionState.IN_GAME),
	EX_USER_BAN_INFO(RequestUserBanInfo::new, ConnectionState.AUTHENTICATED),
	EX_INTERACT_MODIFY(ExInteractModify::new, ConnectionState.IN_GAME, ConnectionState.JOINING_GAME),
	EX_TRY_ENCHANT_ARTIFACT(null, ConnectionState.IN_GAME),
	EX_UPGRADE_SYSTEM_NORMAL_REQUEST(ExUpgradeSystemNormalRequest::new, ConnectionState.IN_GAME),
	EX_PURCHASE_LIMIT_SHOP_ITEM_LIST(RequestExPurchaseLimitShopItemList::new, ConnectionState.IN_GAME),
	EX_PURCHASE_LIMIT_SHOP_ITEM_BUY(RequestExPurchaseLimitShopItemBuy::new, ConnectionState.IN_GAME),
	EX_OPEN_HTML(RequestExPurchaseLimitShopHtmlOpen::new, ConnectionState.IN_GAME),
	EX_REQUEST_CLASS_CHANGE(RequestExRequestClassChange::new, ConnectionState.IN_GAME),
	EX_REQUEST_CLASS_CHANGE_VERIFYING(RequestExRequestClassChangeVerifying::new, ConnectionState.IN_GAME),
	EX_REQUEST_TELEPORT(RequestExRequestTeleport::new, ConnectionState.IN_GAME),
	EX_COSTUME_USE_ITEM(null, ConnectionState.IN_GAME),
	EX_COSTUME_LIST(null, ConnectionState.IN_GAME),
	EX_COSTUME_COLLECTION_SKILL_ACTIVE(null, ConnectionState.IN_GAME),
	EX_COSTUME_EVOLUTION(null, ConnectionState.IN_GAME),
	EX_COSTUME_EXTRACT(null, ConnectionState.IN_GAME),
	EX_COSTUME_LOCK(null, ConnectionState.IN_GAME),
	EX_COSTUME_CHANGE_SHORTCUT(null, ConnectionState.IN_GAME),
	EX_MAGICLAMP_GAME_INFO(RequestExMagicLampGameInfo::new, ConnectionState.IN_GAME),
	EX_MAGICLAMP_GAME_START(RequestExMagicLampGameStart::new, ConnectionState.IN_GAME),
	EX_ACTIVATE_AUTO_SHORTCUT(RequestExActivateAutoShortcut::new, ConnectionState.IN_GAME),
	EX_PREMIUM_MANAGER_LINK_HTML(RequestExPremiumManagerLinkHtml::new, ConnectionState.IN_GAME),
	EX_PREMIUM_MANAGER_PASS_CMD_TO_SERVER(RequestExPremiumManagerPassCmdToServer::new, ConnectionState.IN_GAME),
	EX_ACTIVATED_CURSED_TREASURE_BOX_LOCATION(null, ConnectionState.IN_GAME),
	EX_PAYBACK_LIST(null, ConnectionState.IN_GAME),
	EX_PAYBACK_GIVE_REWARD(null, ConnectionState.IN_GAME),
	EX_AUTOPLAY_SETTING(RequestExAutoplaySetting::new, ConnectionState.IN_GAME, ConnectionState.JOINING_GAME),
	EX_OLYMPIAD_MATCH_MAKING(RequestExOlympiadMatchMaking::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_MATCH_MAKING_CANCEL(RequestExOlympiadMatchMakingCancel::new, ConnectionState.IN_GAME),
	EX_FESTIVAL_BM_INFO(RequestExFestivalBMInfo::new, ConnectionState.IN_GAME),
	EX_FESTIVAL_BM_GAME(RequestExFestivalBMGame::new, ConnectionState.IN_GAME),
	EX_GACHA_SHOP_INFO(null, ConnectionState.IN_GAME),
	EX_GACHA_SHOP_GACHA_GROUP(null, ConnectionState.IN_GAME),
	EX_GACHA_SHOP_GACHA_ITEM(null, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_LIST(RequestExTimeRestrictFieldList::new, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_USER_ENTER(RequestExTimeRestrictFieldUserEnter::new, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_USER_LEAVE(RequestExTimeRestrictFieldUserLeave::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_INFO(RequestRankingCharInfo::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_HISTORY(RequestExRankingCharHistory::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_RANKERS(RequestRankingCharRankers::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_SPAWN_BUFFZONE_NPC(RequestExRankingCharSpawnBuffzoneNpc::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_BUFFZONE_NPC_POSITION(RequestExRankingCharBuffzoneNpcPosition::new, ConnectionState.IN_GAME),
	EX_PLEDGE_MERCENARY_RECRUIT_INFO_SET(RequestExPledgeMercenaryRecruitInfoSet::new, ConnectionState.IN_GAME),
	EX_MERCENARY_CASTLEWAR_CASTLE_INFO(RequestExMercenaryCastlewarCastleInfo::new, ConnectionState.IN_GAME),
	EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_INFO(RequestExMercenaryCastlewarCastleSiegeInfo::new, ConnectionState.IN_GAME),
	EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST(RequestExMercenaryCastlewarCastleSiegeAttackerList::new, ConnectionState.IN_GAME),
	EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_DEFENDER_LIST(RequestExMercenaryCastlewarCastleSiegeDefenderList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_MERCENARY_MEMBER_LIST(RequestExPledgeMercenaryMemberList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_MERCENARY_MEMBER_JOIN(RequestExPledgeMercenaryMemberJoin::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_LIST(RequestExPvpbookList::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_KILLER_LOCATION(RequestExPvpbookKillerLocation::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_TELEPORT_TO_KILLER(RequestExPvpbookTeleportToKiller::new, ConnectionState.IN_GAME),
	EX_LETTER_COLLECTOR_TAKE_REWARD(ExLetterCollectorTakeReward::new, ConnectionState.IN_GAME),
	EX_SET_STATUS_BONUS(RequestExSetStatusBonus::new, ConnectionState.IN_GAME),
	EX_RESET_STATUS_BONUS(RequestExResetStatusBonus::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_MY_RANKING_INFO(RequestOlympiadMyRankingInfo::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_RANKING_INFO(RequestOlympiadRankingInfo::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_HERO_AND_LEGEND_INFO(RequestOlympiadHeroAndLegendInfo::new, ConnectionState.IN_GAME),
	EX_CASTLEWAR_OBSERVER_START(null, ConnectionState.IN_GAME),
	EX_RAID_TELEPORT_INFO(RequestExRaidTeleportInfo::new, ConnectionState.IN_GAME),
	EX_TELEPORT_TO_RAID_POSITION(RequestExTeleportToRaidPosition::new, ConnectionState.IN_GAME),
	EX_CRAFT_EXTRACT(RequestExCraftExtract::new, ConnectionState.IN_GAME),
	EX_CRAFT_RANDOM_INFO(RequestExCraftRandomInfo::new, ConnectionState.IN_GAME),
	EX_CRAFT_RANDOM_LOCK_SLOT(RequestExCraftRandomLockSlot::new, ConnectionState.IN_GAME),
	EX_CRAFT_RANDOM_REFRESH(RequestExCraftRandomRefresh::new, ConnectionState.IN_GAME),
	EX_CRAFT_RANDOM_MAKE(RequestExCraftRandomMake::new, ConnectionState.IN_GAME),
	EX_MULTI_SELL_LIST(RequestExMultiSellList::new, ConnectionState.IN_GAME),
	EX_SAVE_ITEM_ANNOUNCE_SETTING(RequestExSaveItemAnnounceSetting::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_UI(RequestExOlympiadUI::new, ConnectionState.IN_GAME),
	EX_SHARED_POSITION_SHARING_UI(RequestExSharedPositionSharingUI::new, ConnectionState.IN_GAME),
	EX_SHARED_POSITION_TELEPORT_UI(RequestExSharedPositionTeleportUI::new, ConnectionState.IN_GAME),
	EX_SHARED_POSITION_TELEPORT(RequestExSharedPositionTeleport::new, ConnectionState.IN_GAME),
	EX_AUTH_RECONNECT(null, ConnectionState.IN_GAME),
	EX_PET_EQUIP_ITEM(ExPetEquipItem::new, ConnectionState.IN_GAME),
	EX_PET_UNEQUIP_ITEM(ExPetUnequipItem::new, ConnectionState.IN_GAME),
	EX_SHOW_HOMUNCULUS_INFO(null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_CREATE_START(null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_INSERT(null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_SUMMON(null, ConnectionState.IN_GAME),
	EX_DELETE_HOMUNCULUS_DATA(null, ConnectionState.IN_GAME),
	EX_REQUEST_ACTIVATE_HOMUNCULUS(null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_GET_ENCHANT_POINT(null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_INIT_POINT(null, ConnectionState.IN_GAME),
	EX_EVOLVE_PET(ExEvolvePet::new, ConnectionState.IN_GAME),
	EX_ENCHANT_HOMUNCULUS_SKILL(null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_ENCHANT_EXP(null, ConnectionState.IN_GAME),
	EX_TELEPORT_FAVORITES_LIST(RequestExTeleportFavoritesList::new, ConnectionState.IN_GAME),
	EX_TELEPORT_FAVORITES_UI_TOGGLE(RequestExTeleportFavoritesUIToggle::new, ConnectionState.IN_GAME),
	EX_TELEPORT_FAVORITES_ADD_DEL(RequestExTeleportFavoritesAddDel::new, ConnectionState.IN_GAME),
	EX_ANTIBOT(null, ConnectionState.IN_GAME),
	EX_DPSVR(null, ConnectionState.IN_GAME),
	EX_TENPROTECT_DECRYPT_ERROR(null, ConnectionState.IN_GAME),
	EX_NET_LATENCY(null, ConnectionState.IN_GAME),
	EX_MABLE_GAME_OPEN(null, ConnectionState.IN_GAME),
	EX_MABLE_GAME_ROLL_DICE(null, ConnectionState.IN_GAME),
	EX_MABLE_GAME_POPUP_OK(null, ConnectionState.IN_GAME),
	EX_MABLE_GAME_RESET(null, ConnectionState.IN_GAME),
	EX_MABLE_GAME_CLOSE(null, ConnectionState.IN_GAME),
	EX_RETURN_TO_ORIGIN(null, ConnectionState.IN_GAME),
	EX_PK_PENALTY_LIST(RequestExPkPenaltyList::new, ConnectionState.IN_GAME),
	EX_PK_PENALTY_LIST_ONLY_LOC(RequestExPkPenaltyListOnlyLoc::new, ConnectionState.IN_GAME),
	EX_BLESS_OPTION_PUT_ITEM(RequestExBlessOptionPutItem::new, ConnectionState.IN_GAME),
	EX_BLESS_OPTION_ENCHANT(RequestExBlessOptionEnchant::new, ConnectionState.IN_GAME),
	EX_BLESS_OPTION_CANCEL(RequestExBlessOptionCancel::new, ConnectionState.IN_GAME),
	EX_PVP_RANKING_MY_INFO(RequestExPvpRankingMyInfo::new, ConnectionState.IN_GAME),
	EX_PVP_RANKING_LIST(RequestExPvpRankingList::new, ConnectionState.IN_GAME),
	EX_ACQUIRE_PET_SKILL(RequestExAcquirePetSkill::new, ConnectionState.IN_GAME),
	EX_PLEDGE_V3_INFO(RequestExPledgeV3Info::new, ConnectionState.IN_GAME),
	EX_PLEDGE_ENEMY_INFO_LIST(RequestExPledgeEnemyInfoList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_ENEMY_REGISTER(RequestExPledgeEnemyRegister::new, ConnectionState.IN_GAME),
	EX_PLEDGE_ENEMY_DELETE(RequestExPledgeEnemyDelete::new, ConnectionState.IN_GAME),
	EX_TRY_PET_EXTRACT_SYSTEM(RequestExTryPetExtractSystem::new, ConnectionState.IN_GAME),
	EX_PLEDGE_V3_SET_ANNOUNCE(RequestExPledgeV3SetAnnounce::new, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_OPEN(null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_BUY(null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_BONUS(null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_RANKING(null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_MY_RECEIVED_BONUS(null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_REWARD(null, ConnectionState.IN_GAME),
	EX_TIMER_CHECK(RequestExTimerCheck::new, ConnectionState.IN_GAME),
	EX_STEADY_BOX_LOAD(RequestExSteadyBoxLoad::new, ConnectionState.IN_GAME), //0x1D5
	EX_STEADY_OPEN_SLOT(RequestExSteadyOpenSlot::new, ConnectionState.IN_GAME), //0x1D6
	EX_STEADY_OPEN_BOX(RequestExSteadyOpenBox::new, ConnectionState.IN_GAME), //0x1D7
	EX_STEADY_GET_REWARD(RequestExSteadyGetReward::new, ConnectionState.IN_GAME), //0x1D8
	EX_PET_RANKING_MY_INFO(RequestExPetRankingMyInfo::new, ConnectionState.IN_GAME),
	EX_PET_RANKING_LIST(RequestExPetRankingList::new, ConnectionState.IN_GAME),
	EX_COLLECTION_OPEN_UI(RequestExCollectionOpenUI::new, ConnectionState.IN_GAME),
	EX_COLLECTION_CLOSE_UI(RequestExCollectionCloseUI::new, ConnectionState.IN_GAME),
	EX_COLLECTION_LIST(RequestExCollectionList::new, ConnectionState.IN_GAME),
	EX_COLLECTION_UPDATE_FAVORITE(RequestExCollectionUpdateFavorite::new, ConnectionState.IN_GAME),
	EX_COLLECTION_FAVORITE_LIST(RequestExCollectionFavoriteList::new, ConnectionState.IN_GAME),
	EX_COLLECTION_SUMMARY(RequestExCollectionSummary::new, ConnectionState.IN_GAME),
	EX_COLLECTION_REGISTER(RequestExCollectionRegister::new, ConnectionState.IN_GAME),
	EX_COLLECTION_RECEIVE_REWARD(RequestExCollectionReceiveReward::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_LIST(RequestExPvpBookShareRevengeList::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_REQ_SHARE_REVENGEINFO(RequestExPvpbookShareRevengeReqShareRevengeInfo::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_KILLER_LOCATION(RequestExPvpbookShareRevengeKillerLocation::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_TELEPORT_TO_KILLER(RequestExPvpbookShareRevengeTeleportToKiller::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_SHARED_TELEPORT_TO_KILLER(RequestExPvpbookShareRevengeSharedTeleportToKiller::new, ConnectionState.IN_GAME),
	EX_PENALTY_ITEM_LIST(RequestExPenaltyItemList::new, ConnectionState.IN_GAME),
	EX_PENALTY_ITEM_RESTORE(RequestExPenaltyItemRestore::new, ConnectionState.IN_GAME),
	EX_USER_WATCHER_TARGET_LIST(RequestExUserWatcherTargetList::new, ConnectionState.IN_GAME),
	EX_USER_WATCHER_ADD(RequestExUserWatcherAdd::new, ConnectionState.IN_GAME),
	EX_USER_WATCHER_DELETE(RequestExUserWatcherDelete::new, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_ACTIVATE_SLOT(null, ConnectionState.IN_GAME),
	EX_SUMMON_HOMUNCULUS_COUPON(null, ConnectionState.IN_GAME),
	EX_SUBJUGATION_LIST(RequestExSubjugationList::new, ConnectionState.IN_GAME),
	EX_SUBJUGATION_RANKING(RequestExSubjugationRanking::new, ConnectionState.IN_GAME),
	EX_SUBJUGATION_GACHA_UI(RequestExSubjugationGachaUI::new, ConnectionState.IN_GAME),
	EX_SUBJUGATION_GACHA(RequestExSubjugationGacha::new, ConnectionState.IN_GAME),
	EX_PLEDGE_DONATION_INFO(RequestExPledgeDonationInfo::new, ConnectionState.IN_GAME),
	EX_PLEDGE_DONATION_REQUEST(RequestExPledgeDonationRequest::new, ConnectionState.IN_GAME),
	EX_PLEDGE_CONTRIBUTION_LIST(RequestExPledgeContributionList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_RANKING_MY_INFO(RequestExPledgeRankingMyInfo::new, ConnectionState.IN_GAME),
	EX_PLEDGE_RANKING_LIST(RequestExPledgeRankingList::new, ConnectionState.IN_GAME),
	EX_ITEM_RESTORE_LIST(null, ConnectionState.IN_GAME),
	EX_ITEM_RESTORE(null, ConnectionState.IN_GAME),
	EX_DETHRONE_INFO(null, ConnectionState.IN_GAME),
	EX_DETHRONE_RANKING_INFO(null, ConnectionState.IN_GAME),
	EX_DETHRONE_SERVER_INFO(null, ConnectionState.IN_GAME),
	EX_DETHRONE_DISTRICT_OCCUPATION_INFO(null, ConnectionState.IN_GAME),
	EX_DETHRONE_DAILY_MISSION_INFO(null, ConnectionState.IN_GAME),
	EX_DETHRONE_DAILY_MISSION_GET_REWARD(null, ConnectionState.IN_GAME),
	EX_DETHRONE_PREV_SEASON_INFO(null, ConnectionState.IN_GAME),
	EX_DETHRONE_GET_REWARD(null, ConnectionState.IN_GAME),
	EX_DETHRONE_ENTER(null, ConnectionState.IN_GAME),
	EX_DETHRONE_LEAVE(null, ConnectionState.IN_GAME),
	EX_DETHRONE_CHECK_NAME(null, ConnectionState.IN_GAME),
	EX_DETHRONE_CHANGE_NAME(null, ConnectionState.IN_GAME),
	EX_DETHRONE_CONNECT_CASTLE(null, ConnectionState.IN_GAME),
	EX_DETHRONE_DISCONNECT_CASTLE(null, ConnectionState.IN_GAME),
	EX_CHANGE_NICKNAME_COLOR_ICON(RequestExChangeNickNameColorIcon::new, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_MOVE_TO_HOST(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_RETURN_TO_ORIGIN_PEER(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_INFO(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_INFO(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_JOIN(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_RECRUIT_INFO_SET(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_MEMBER_LIST(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_MEMBER_JOIN(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_TELEPORT(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_OBSERVER_START(null, ConnectionState.IN_GAME),
	EX_PRIVATE_STORE_SEARCH_LIST(RequestExPrivateStoreSearchList::new, ConnectionState.IN_GAME),
	EX_PRIVATE_STORE_SEARCH_STATISTICS(RequestExPrivateStoreSearchStatistics::new, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_HOST_CASTLE_SIEGE_RANKING_INFO(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_RANKING_INFO(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_HUD_INFO(null, ConnectionState.IN_GAME),
	EX_NEW_HENNA_LIST(RequestExNewHennaList::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_EQUIP(RequestExNewHennaEquip::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_UNEQUIP(RequestExNewHennaUnequip::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_POTEN_SELECT(RequestExNewHennaPotenSelect::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_POTEN_ENCHANT(RequestExNewHennaPotenEnchant::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_COMPOSE(RequestExNewHennaCompose::new, ConnectionState.IN_GAME),
	EX_REQUEST_INVITE_PARTY(RequestExRequestInviteParty::new, ConnectionState.IN_GAME),
	EX_ITEM_USABLE_LIST(RequestExItemUsableList::new, ConnectionState.IN_GAME),
	EX_PACKETREADCOUNTPERSECOND(null, ConnectionState.IN_GAME),
	EX_SELECT_GLOBAL_EVENT_UI(RequestExSelectGlobalEventUi::new, ConnectionState.IN_GAME),
	EX_L2PASS_INFO(RequestHuntPassInfo::new, ConnectionState.IN_GAME), //0x223
	EX_L2PASS_REQUEST_REWARD(RequestHuntPassReward::new, ConnectionState.IN_GAME), //0x224
	EX_L2PASS_REQUEST_REWARD_ALL(RequestHuntPassRewardAll::new, ConnectionState.IN_GAME), //0x225
	EX_L2PASS_BUY_PREMIUM(RequestHuntPassBuyPremium::new, ConnectionState.IN_GAME), //0x226
	EX_SAYHAS_SUPPORT_TOGGLE(HuntpassSayhasToggle::new, ConnectionState.IN_GAME), //0x227
	EX_REQ_ENCHANT_FAIL_REWARD_INFO(RequestExEnchantFailRewardInfo::new, ConnectionState.IN_GAME),
	EX_SET_ENCHANT_CHALLENGE_POINT(RequestExSetEnchantChallengePoint::new, ConnectionState.IN_GAME),
	EX_RESET_ENCHANT_CHALLENGE_POINT(RequestExResetEnchantChallengePoint::new, ConnectionState.IN_GAME),
	EX_REQ_VIEW_ENCHANT_RESULT(RequestExViewEnchantResult::new, ConnectionState.IN_GAME),
	EX_REQ_START_MULTI_ENCHANT_SCROLL(RequestExStartMultiEnchantScroll::new, ConnectionState.IN_GAME),
	EX_REQ_VIEW_MULTI_ENCHANT_RESULT(RequestExViewMultiEnchantResult::new, ConnectionState.IN_GAME),
	EX_REQ_FINISH_MULTI_ENCHANT_SCROLL(RequestExFinishMultiEnchantScroll::new, ConnectionState.IN_GAME),
	EX_REQ_CHANGE_MULTI_ENCHANT_SCROLL(null, ConnectionState.IN_GAME),
	EX_REQ_SET_MULTI_ENCHANT_ITEM_LIST(RequestExSetMultiEnchantItemList::new, ConnectionState.IN_GAME),
	EX_REQ_MULTI_ENCHANT_ITEM_LIST(RequestExMultiEnchantItemList::new, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_SUPPORT_PLEDGE_FLAG_SET(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_SUPPORT_PLEDGE_INFO_SET(null, ConnectionState.IN_GAME),
	EX_REQ_HOMUNCULUS_PROB_LIST(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_HOST_CASTLE_SIEGE_ALL_RANKING_INFO(null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_ALL_RANKING_INFO(null, ConnectionState.IN_GAME),
	EX_MISSION_LEVEL_REWARD_LIST(RequestExMissionLevelRewardList::new, ConnectionState.IN_GAME),
	EX_MISSION_LEVEL_RECEIVE_REWARD(RequestExMissionLevelReceiveReward::new, ConnectionState.IN_GAME),
	EX_MISSION_LEVEL_JUMP_LEVEL(RequestExMissionLevelJumpLevel::new, ConnectionState.IN_GAME),
	EX_BALROGWAR_TELEPORT(null, ConnectionState.IN_GAME),
	EX_BALROGWAR_SHOW_UI(null, ConnectionState.IN_GAME),
	EX_BALROGWAR_SHOW_RANKING(null, ConnectionState.IN_GAME),
	EX_BALROGWAR_GET_REWARD(null, ConnectionState.IN_GAME),
	EX_USER_RESTART_LOCKER_UPDATE(RequestExUserRestartLockerUpdate::new, ConnectionState.IN_GAME),

	EX_WORLD_EXCHANGE_ITEM_LIST(RequestExWorldExchangeItemList::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_REGI_ITEM(RequestExWorldExchangeRegiItem::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_BUY_ITEM(RequestExWorldExchangeBuyItem::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_SETTLE_LIST(RequestExWorldExchangeSettleList::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_SETTLE_RECV_RESULT(RequestExWorldExchangeSettleRecvResult::new, ConnectionState.IN_GAME),

	EX_READY_ITEM_AUTO_PEEL(RequestExReadyItemAutoPeel::new, ConnectionState.IN_GAME),
	EX_REQUEST_ITEM_AUTO_PEEL(RequestExItemAutoPeel::new, ConnectionState.IN_GAME),
	EX_STOP_ITEM_AUTO_PEEL(RequestExStopItemAutoPeel::new, ConnectionState.IN_GAME),
	EX_VARIATION_OPEN_UI(RequestExVariationOpenUi::new, ConnectionState.IN_GAME),
	EX_VARIATION_CLOSE_UI(RequestExVariationCloseUi::new, ConnectionState.IN_GAME),
	EX_APPLY_VARIATION_OPTION(RequestExApplyVariationOption::new, ConnectionState.IN_GAME),
	EX_REQUEST_AUDIO_LOG_SAVE(null, ConnectionState.IN_GAME),
	EX_BR_VERSION(RequestExBRVersion::new, ConnectionState.AUTHENTICATED, ConnectionState.CONNECTED, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_INFO(null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_OPEN(null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_BUY(null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_BONUS(null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_RANKING(null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_MY_RECEIVED_BONUS(null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_REWARD(null, ConnectionState.IN_GAME),
	EX_HENNA_UNEQUIP_INFO(RequestExHennaUnEquipInfo::new, ConnectionState.IN_GAME),
	EX_HERO_BOOK_CHARGE(null, ConnectionState.IN_GAME),
	EX_HERO_BOOK_ENCHANT(null, ConnectionState.IN_GAME),
	EX_HERO_BOOK_CHARGE_PROB(null, ConnectionState.IN_GAME),
	EX_TELEPORT_UI(RequestExTeleportUi::new, ConnectionState.IN_GAME, ConnectionState.JOINING_GAME),
	EX_GOODS_GIFT_LIST_INFO(null, ConnectionState.IN_GAME),
	EX_GOODS_GIFT_ACCEPT(null, ConnectionState.IN_GAME),
	EX_GOODS_GIFT_REFUSE(null, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_AVERAGE_PRICE(RequestExWorldExchangeAveragePrice::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_TOTAL_LIST(RequestExWorldExchangeTotalList::new, ConnectionState.IN_GAME),
	EX_PRISON_USER_INFO(null, ConnectionState.IN_GAME),
	EX_PRISON_USER_DONATION(null, ConnectionState.IN_GAME),
	EX_TRADE_LIMIT_INFO(null, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_OPEN(null, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_GAME(null, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_INVEN_ITEM_LIST(null, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_INVEN_GET_ITEM(null, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_HISTORY(null, ConnectionState.IN_GAME),
	EX_SET_PLEDGE_CREST_PRESET(RequestExSetPledgeCrestPreset::new, ConnectionState.IN_GAME),
	EX_GET_PLEDGE_CREST_PRESET(RequestExGetPledgeCrestPreset::new, ConnectionState.IN_GAME),
	EX_DUAL_INVENTORY_SWAP(ExDualInventorySwap::new, ConnectionState.IN_GAME),
	EX_SP_EXTRACT_INFO(RequestExSpExtractInfo::new, ConnectionState.IN_GAME),
	EX_SP_EXTRACT_ITEM(RequestExSpExtractItem::new, ConnectionState.IN_GAME),
	EX_QUEST_TELEPORT(RequestExQuestTeleport::new, ConnectionState.IN_GAME),
	EX_QUEST_ACCEPT(RequestExQuestAccept::new, ConnectionState.IN_GAME),
	EX_QUEST_CANCEL(RequestExQuestCancel::new, ConnectionState.IN_GAME),
	EX_QUEST_COMPLETE(RequestExQuestComplete::new, ConnectionState.IN_GAME),
	EX_QUEST_NOTIFICATION_ALL(RequestExQuestNotificationAll::new, ConnectionState.IN_GAME),
	EX_QUEST_UI(RequestExQuestUi::new, ConnectionState.IN_GAME),
	EX_QUEST_ACCEPTABLE_LIST(RequestExQuestAcceptableList::new, ConnectionState.IN_GAME),
	EX_SKILL_ENCHANT_INFO(RequestExSkillEnchantInfo::new, ConnectionState.IN_GAME),
	EX_SKILL_ENCHANT_CHARGE(RequestExSkillEnchantCharge::new, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_HOST_USER_ENTER(null, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_HOST_USER_LEAVE(null, ConnectionState.IN_GAME),
	EX_DETHRONE_SHOP_OPEN_UI(null, ConnectionState.IN_GAME),
	EX_DETHRONE_SHOP_BUY(null, ConnectionState.IN_GAME),
	EX_ENHANCED_ABILITY_OF_FIRE_OPEN_UI(null, ConnectionState.IN_GAME),
	EX_ENHANCED_ABILITY_OF_FIRE_INIT(null, ConnectionState.IN_GAME),
	EX_ENHANCED_ABILITY_OF_FIRE_EXP_UP(null, ConnectionState.IN_GAME),
	EX_ENHANCED_ABILITY_OF_FIRE_LEVEL_UP(null, ConnectionState.IN_GAME),
	EX_HOLY_FIRE_OPEN_UI(null, ConnectionState.IN_GAME),
	EX_PRIVATE_STORE_BUY_SELL(null, ConnectionState.IN_GAME),

	//430
	EX_VIP_ATTENDANCE_LIST(RequestExVipAttendanceList::new, ConnectionState.IN_GAME), // 843;
	EX_VIP_ATTENDANCE_CHECK(RequestExVipAttendanceCheck::new, ConnectionState.IN_GAME), // 844;
	EX_VIP_ATTENDANCE_REWARD(RequestExVipAttendanceReward::new, ConnectionState.IN_GAME), // 845;
	EX_CHANGE_ABILITY_PRESET(RequestExChangeAbilityPreset::new, ConnectionState.IN_GAME), // 846;
	EX_NEW_HENNA_POTEN_ENCHANT_RESET(RequestExNewHennaPotenEnchantReset::new, ConnectionState.IN_GAME), // 847;
	EX_INZONE_RANKING_MY_INFO(RequestExInzoneRankingMyInfo::new, ConnectionState.IN_GAME), // 848;
	EX_INZONE_RANKING_LIST(RequestExInzoneRankingList::new, ConnectionState.IN_GAME), // 849;
	EX_TIME_RESTRICT_FIELD_HOST_USER_ENTER_BY_NPC(null, ConnectionState.IN_GAME), // 850
	//447
	EX_PREPARE_LOGIN(null, ConnectionState.IN_GAME), //851
	EX_RELICS_OPEN_UI(RequestExRelicsOpenUi::new, ConnectionState.IN_GAME), //852
	EX_RELICS_CLOSE_UI(RequestExRelicsCloseUi::new, ConnectionState.IN_GAME), //853
	EX_RELICS_SUMMON_CLOSE_UI(RequestExRelicsSummonCloseUi::new, ConnectionState.IN_GAME), //854
	EX_RELICS_ACTIVE(RequestExRelicsActive::new, ConnectionState.IN_GAME), //855
	EX_RELICS_SUMMON(RequestExRelicsSummon::new, ConnectionState.IN_GAME), //856
	EX_RELICS_EXCHANGE(RequestExRelicsExchange::new, ConnectionState.IN_GAME), //857
	EX_RELICS_EXCHANGE_CONFIRM(RequestExRelicsExchangeConfirm::new, ConnectionState.IN_GAME), //858
	EX_RELICS_UPGRADE(RequestExRelicsUpgrade::new, ConnectionState.IN_GAME), //859
	EX_RELICS_COMBINATION(RequestExRelicsCombination::new, ConnectionState.IN_GAME), //860
	EX_SERVERWAR_FIELD_ENTER_USER_INFO(null, ConnectionState.IN_GAME), //861
	EX_SERVERWAR_MOVE_TO_HOST(null, ConnectionState.IN_GAME), //862
	EX_SERVERWAR_BATTLE_HUD_INFO(null, ConnectionState.IN_GAME), //863
	EX_SERVERWAR_LEADER_LIST(null, ConnectionState.IN_GAME), //864
	EX_SERVERWAR_SELECT_LEADER(null, ConnectionState.IN_GAME), //865
	EX_SERVERWAR_SELECT_LEADER_INFO(null, ConnectionState.IN_GAME), //866
	EX_SERVERWAR_MOVE_TO_LEADER_CAMP(null, ConnectionState.IN_GAME), //867
	EX_SERVERWAR_REWARD_ITEM_INFO(null, ConnectionState.IN_GAME), //868
	EX_SERVERWAR_REWARD_INFO(null, ConnectionState.IN_GAME), //869
	EX_SERVERWAR_GET_REWARD(null, ConnectionState.IN_GAME), //870
	EX_RELICS_COMBINATION_COMPLETE(RequestExRelicsCombinationComplete::new, ConnectionState.IN_GAME), //871
	EX_VIRTUALITEM_SYSTEM(null, ConnectionState.IN_GAME), //872

	EX_CROSS_EVENT_DATA(null, ConnectionState.IN_GAME), //874;
	EX_CROSS_EVENT_INFO(null, ConnectionState.IN_GAME), //875;
	EX_CROSS_EVENT_NORMAL_REWARD(null, ConnectionState.IN_GAME), //876;
	EX_CROSS_EVENT_RARE_REWARD(null, ConnectionState.IN_GAME), //877;
	EX_CROSS_EVENT_RESET(null, ConnectionState.IN_GAME), //878;
	EX_ADENLAB_BOSS_LIST(RequestExAdenlabBossList::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //879;
	EX_ADENLAB_UNLOCK_BOSS(RequestExAdenlabUnlockBoss::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //880;
	EX_ADENLAB_BOSS_INFO(RequestExAdenlabBossInfo::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //881;
	EX_ADENLAB_NORMAL_SLOT(RequestExAdenlabNormalSlot::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //882;
	EX_ADENLAB_NORMAL_PLAY(RequestExAdenlabNormalPlay::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //883;
	EX_ADENLAB_SPECIAL_SLOT(RequestExAdenlabSpecialSlot::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //884;
	EX_ADENLAB_SPECIAL_PROB(RequestExAdenlabSpecialProb::new, ConnectionState.IN_GAME),
	EX_ADENLAB_SPECIAL_PLAY(RequestExAdenlabSpecialPlay::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //885;
	EX_ADENLAB_SPECIAL_FIX(RequestExAdenlabSpecialFix::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //886;
	EX_ADENLAB_TRANSCEND_ENCHANT(RequestExAdenlabTranscendEnchant::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME), //887;

	EX_ADENLAB_TRANSCEND_PROB(RequestExAdenlabTranscendProb::new, ConnectionState.JOINING_GAME, ConnectionState.IN_GAME),

	EX_CHAT_BACKGROUND_SETTING(RequestExChatBackgroundSetting::new, ConnectionState.IN_GAME), //888;
	EX_HOMUNCULUS_EVOLVE(null, ConnectionState.IN_GAME), //889;

	EX_EXTRACT_SKILL_ENCHANT(RequestExExtractSkillEnchant::new, ConnectionState.IN_GAME), //890;
	EX_REQUEST_SKILL_ENCHANT_CONFIRM(RequestExSkillEnchantConfirm::new, ConnectionState.IN_GAME), //891;

	EX_CREATE_ITEM_PROB_LIST(RequestExCreateItemProbList::new, ConnectionState.IN_GAME), //894;
	EX_CRAFT_SLOT_PROB_LIST(RequestExCraftSlotProbList::new, ConnectionState.IN_GAME, ConnectionState.JOINING_GAME), //895;
	EX_NEW_HENNA_COMPOSE_PROB_LIST(RequestExNewHennaComposeProbList::new, ConnectionState.IN_GAME), //896;
	EX_VARIATION_PROB_LIST(RequestExVariationProbList::new, ConnectionState.IN_GAME), //897;
	EX_RELICS_PROB_LIST(RequestExRelicsProbList::new, ConnectionState.IN_GAME), //898;
	EX_UPGRADE_SYSTEM_PROB_LIST(RequestExUpgradeSystemProbList::new, ConnectionState.IN_GAME), //899;
	EX_COMBINATION_PROB_LIST(RequestExCombinationProbList::new, ConnectionState.IN_GAME), //900;

	EX_RELICS_ID_SUMMON(RequestExRelicsIdSummon::new, ConnectionState.IN_GAME),
	EX_RELICS_SUMMON_LIST(RequestExRelicsSummonList::new, ConnectionState.IN_GAME),
	EX_RELICS_CONFIRM_COMBINATION(RequestExRelicsConfirmCombination::new, ConnectionState.IN_GAME),

	EX_NEW_HENNA_POTEN_OPENSLOT_PROB_INFO(RequestExNewHennaPotenOpenslotProbInfo::new, ConnectionState.IN_GAME), //905;
	EX_NEW_HENNA_POTEN_OPENSLOT(RequestExNewHennaPotenOpenslot::new, ConnectionState.IN_GAME), //906;

	EX_DYEEFFECT_LIST(RequestExDyeeffectList::new, ConnectionState.IN_GAME),
	EX_DYEEFFECT_ENCHANT_PROB_INFO(RequestExDyeeffectEnchantProbInfo::new, ConnectionState.IN_GAME),
	EX_DYEEFFECT_ENCHANT_NORMALSKILL(RequestExDyeeffectEnchantNormalskill::new, ConnectionState.IN_GAME),
	EX_DYEEFFECT_ACQUIRE_HIDDENSKILL(RequestExDyeeffectAcquireHiddenskill::new, ConnectionState.IN_GAME),
	EX_DYEEFFECT_ENCHANT_RESET(RequestExDyeeffectEnchantReset::new, ConnectionState.IN_GAME),
	EX_LOAD_PET_PREVIEW_BY_SID(RequestExLoadPetPreviewBySid::new, ConnectionState.IN_GAME),
	EX_LOAD_PET_PREVIEW_BY_DBID(RequestExLoadPetPreviewByDbid::new, ConnectionState.IN_GAME),
	EX_CHECK_CLIENT_INFO(RequestExCheckClientInfo::new, ConnectionState.IN_GAME),

	EX_MATCHINGINZONE_FIELD_ENTER_USER_INFO(RequestExMatchinginzoneFieldEnterUserInfo::new, ConnectionState.IN_GAME),
	EX_RAID_AUCTION_BID(RequestExRaidAuctionBid::new, ConnectionState.IN_GAME),
	EX_RAID_AUCTION_CANCEL_BID(RequestExRaidAuctionCancelBid::new, ConnectionState.IN_GAME),
	EX_RAID_AUCTION_POST_LIST(RequestExRaidAuctionPostList::new, ConnectionState.IN_GAME),
	EX_RAID_AUCTION_POST_RECEIVE(RequestExRaidAuctionPostReceive::new, ConnectionState.IN_GAME),
	EX_RAID_AUCTION_POST_RECEIVE_ALL(RequestExRaidAuctionPostReceiveAll::new, ConnectionState.IN_GAME),
	EX_REPAIR_ALL_EQUIPMENT(RequestExRepairAllEquipment::new, ConnectionState.IN_GAME),
	EX_CLASS_CHANGE(RequestExClassChange::new, ConnectionState.IN_GAME),
	EX_CHAT_BAN_START(RequestExChatBanStart::new, ConnectionState.IN_GAME),
	EX_CHAT_BAN_END(RequestExChatBanEnd::new, ConnectionState.IN_GAME),
	EX_BLESS_OPTION_PROB_LIST(RequestExBlessOptionProbList::new, ConnectionState.IN_GAME),

	EX_MAX(null, ConnectionState.IN_GAME), //901;

	EX_BOOK_MARK_SLOT_INFO(78, 0, RequestBookMarkSlotInfo::new, ConnectionState.IN_GAME),
	EX_SAVE_BOOK_MARK_SLOT(78, 1, RequestSaveBookMarkSlot::new, ConnectionState.IN_GAME),
	EX_MODIFY_BOOK_MARK_SLOT(78, 2, RequestModifyBookMarkSlot::new, ConnectionState.IN_GAME),
	EX_DELETE_BOOK_MARK_SLOT(78, 3, RequestDeleteBookMarkSlot::new, ConnectionState.IN_GAME),
	EX_TELEPORT_BOOK_MARK(78, 4, RequestTeleportBookMark::new, ConnectionState.IN_GAME),
	EX_CHANGE_BOOK_MARK_SLOT(78, 5, RequestChangeBookMarkSlot::new, ConnectionState.IN_GAME),
	;

	public static final IncomingExPackets507[] PACKET_ARRAY;
	public static final IncomingExPackets507[] PACKET_EX_ARRAY;
	public static final IncomingExPackets507[] PACKET_BOOK_MARK_ARRAY;

	static
	{
		final short maxPacketId = (short) Arrays.stream(values()).mapToInt(IncomingExPackets507::getPacketId).max().orElse(0);
		PACKET_ARRAY = new IncomingExPackets507[maxPacketId + 1];
		for(IncomingExPackets507 incomingPacket : values())
		{
			PACKET_ARRAY[incomingPacket.getPacketId()] = incomingPacket;
		}

		List<IncomingExPackets507> customPacketsList = Arrays.stream(values()).filter(packet -> packet._id_ex > 0 && packet._id == 401).toList();
		final int maxPacketExId = customPacketsList.stream().mapToInt(packet -> packet._id_ex).max().orElse(0);
		PACKET_EX_ARRAY = new IncomingExPackets507[maxPacketExId + 1];
		customPacketsList.forEach(incomingPacket -> {
			PACKET_EX_ARRAY[incomingPacket._id_ex] = incomingPacket;
		});

		List<IncomingExPackets507> BookMarkPacketsList = Arrays.stream(values()).filter(packet -> packet._id_ex >= 0 && packet._id == 78).toList();
		final int maxPacketBookMarkId = BookMarkPacketsList.stream().mapToInt(packet -> packet._id_ex).max().orElse(0);
		PACKET_BOOK_MARK_ARRAY = new IncomingExPackets507[maxPacketBookMarkId + 1];
		BookMarkPacketsList.forEach(incomingPacket -> {
			PACKET_BOOK_MARK_ARRAY[incomingPacket._id_ex] = incomingPacket;
		});
	}

	private final int _id;
	private Supplier<IIncomingPacket<GameClient>> _incomingPacketFactory;
	private Set<IConnectionState> _connectionStates;
	private int _id_ex;
	private ConnectionState[] _connectionStates2;

	IncomingExPackets507(Supplier<IIncomingPacket<GameClient>> incomingPacketFactory, ConnectionState... connectionStates)
	{
		this(-1, -1, incomingPacketFactory, connectionStates);
	}

	IncomingExPackets507(int id, int id_ex, Supplier<IIncomingPacket<GameClient>> incomingPacketFactory, ConnectionState... connectionStates)
	{
		_id = id > 0 ? id : ordinal();
		if(id_ex > 0)
			id_ex = id_ex--;
		_id_ex = id_ex;
		// packetId is an unsigned short
		if(id > 0xFFFF)
			throw new IllegalArgumentException("packetId must not be bigger than 0xFFFF");

		_incomingPacketFactory = incomingPacketFactory != null ? incomingPacketFactory : () -> null;
		_connectionStates2 = connectionStates;
		_connectionStates = Set.of(connectionStates);
	}

	@Override
	public int getPacketId()
	{
		return _id;
	}

	@Override
	public IIncomingPacket<GameClient> newIncomingPacket()
	{
		return _incomingPacketFactory.get();
	}

	@Override
	public Set<IConnectionState> getConnectionStates()
	{
		return _connectionStates;
	}

	public ConnectionState[] get_connectionStates2()
	{
		return _connectionStates2;
	}
}
