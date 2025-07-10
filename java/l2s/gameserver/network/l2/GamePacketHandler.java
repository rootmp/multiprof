package l2s.gameserver.network.l2;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.net.nio.impl.IClientFactory;
import l2s.commons.net.nio.impl.IMMOExecutor;
import l2s.commons.net.nio.impl.IPacketHandler;
import l2s.commons.net.nio.impl.MMOConnection;
import l2s.commons.net.nio.impl.ReceivablePacket;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.network.l2.c2s.*;
import l2s.gameserver.network.l2.c2s.adenadistribution.RequestDivideAdena;
import l2s.gameserver.network.l2.c2s.adenadistribution.RequestDivideAdenaCancel;
import l2s.gameserver.network.l2.c2s.adenadistribution.RequestDivideAdenaStart;
import l2s.gameserver.network.l2.c2s.blessing.RequestExBlessOptionCancel;
import l2s.gameserver.network.l2.c2s.blessing.RequestExBlessOptionEnchant;
import l2s.gameserver.network.l2.c2s.blessing.RequestExBlessOptionPutItem;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionCloseUI;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionFavoriteList;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionList;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionOpenUI;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionRegister;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionSummary;
import l2s.gameserver.network.l2.c2s.collection.RequestExCollectionUpdateFavorite;
import l2s.gameserver.network.l2.c2s.enchant.RequestEnchantItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExAddEnchantScrollItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExCancelEnchantItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExEnchantFailRewardInfo;
import l2s.gameserver.network.l2.c2s.enchant.RequestExFinishMultiEnchantScroll;
import l2s.gameserver.network.l2.c2s.enchant.RequestExMultiEnchantItemList;
import l2s.gameserver.network.l2.c2s.enchant.RequestExSetMultiEnchantItemList;
import l2s.gameserver.network.l2.c2s.enchant.RequestExStartMultiEnchantScroll;
import l2s.gameserver.network.l2.c2s.enchant.RequestExTryToPutEnchantSupportItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExTryToPutEnchantTargetItem;
import l2s.gameserver.network.l2.c2s.enchant.RequestExViewEnchantResult;
import l2s.gameserver.network.l2.c2s.enchant.RequestExViewMultiEnchantResult;
import l2s.gameserver.network.l2.c2s.events.RequestExBalthusToken;
import l2s.gameserver.network.l2.c2s.events.RequestExFestivalBMGame;
import l2s.gameserver.network.l2.c2s.events.RequestExFestivalBMInfo;
import l2s.gameserver.network.l2.c2s.itemrestore.RequestExItemRestore;
import l2s.gameserver.network.l2.c2s.itemrestore.RequestExItemRestoreList;
import l2s.gameserver.network.l2.c2s.itemrestore.RequestExPenaltyItemList;
import l2s.gameserver.network.l2.c2s.itemrestore.RequestExPenaltyItemRestore;
import l2s.gameserver.network.l2.c2s.items.autopeel.RequestExItemAutoPeel;
import l2s.gameserver.network.l2.c2s.items.autopeel.RequestExReadyItemAutoPeel;
import l2s.gameserver.network.l2.c2s.items.autopeel.RequestExStopItemAutoPeel;
import l2s.gameserver.network.l2.c2s.magiclamp.RequestExMagicLampGameInfo;
import l2s.gameserver.network.l2.c2s.magiclamp.RequestExMagicLampGameStart;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaCompose;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaEquip;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaList;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaPotenEnchant;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaPotenSelect;
import l2s.gameserver.network.l2.c2s.newhenna.RequestExNewHennaUnequip;
import l2s.gameserver.network.l2.c2s.pets.RequestExTryPetExtractSystem;
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
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpBookShareRevengeKillerLocation;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpBookShareRevengeList;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpBookShareRevengeReqShareRevengeInfo;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpBookShareRevengeSharedTeleportToKiller;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpBookShareRevengeTeleportToKiller;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookKillerLocation;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookList;
import l2s.gameserver.network.l2.c2s.pvpbook.RequestExPvpbookTeleportToKiller;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftExtract;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftRandomInfo;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftRandomLockSlot;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftRandomMake;
import l2s.gameserver.network.l2.c2s.randomcraft.RequestExCraftRandomRefresh;
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
import l2s.gameserver.network.l2.c2s.teleport.RequestExRaidTeleportInfo;
import l2s.gameserver.network.l2.c2s.teleport.RequestExRequestTeleport;
import l2s.gameserver.network.l2.c2s.teleport.RequestExSharedPositionSharingUI;
import l2s.gameserver.network.l2.c2s.teleport.RequestExSharedPositionTeleport;
import l2s.gameserver.network.l2.c2s.teleport.RequestExSharedPositionTeleportUI;
import l2s.gameserver.network.l2.c2s.teleport.RequestExTeleportFavoritesAddDel;
import l2s.gameserver.network.l2.c2s.teleport.RequestExTeleportFavoritesList;
import l2s.gameserver.network.l2.c2s.teleport.RequestExTeleportFavoritesUIToggle;
import l2s.gameserver.network.l2.c2s.teleport.RequestExTeleportToRaidPosition;
import l2s.gameserver.network.l2.c2s.timerestrictfield.RequestExTimeRestrictFieldList;
import l2s.gameserver.network.l2.c2s.timerestrictfield.RequestExTimeRestrictFieldUserEnter;

public final class GamePacketHandler
		implements IPacketHandler<GameClient>, IClientFactory<GameClient>, IMMOExecutor<GameClient>
{
	private static final Logger _log = LoggerFactory.getLogger(GamePacketHandler.class);
	private static final List<ReceivablePacket<GameClient>> _sentPackets = new CopyOnWriteArrayList<>();

	@Override
	public ReceivablePacket<GameClient> handlePacket(ByteBuffer buf, GameClient client)
	{
		final int id = buf.get() & 0xFF;

		ReceivablePacket<GameClient> msg = null;
		try
		{
			int id2 = 0;
			switch (client.getState())
			{
				case CONNECTED:
					switch (id)
					{
						case 0x00:
							msg = new RequestStatus();
							break;
						case 0x0e:
							msg = new ProtocolVersion();
							break;
						case 0x1F:
							// UNK
							break;
						case 0x2b:
							msg = new AuthLogin();
							break;
						case 0xcb:
							msg = new ReplyGameGuardQuery();
							break;
						case 0xd0:
							final int id3 = buf.getShort() & 0xffff;
							switch (id3)
							{
								case 0x104:
									msg = new ExSendClientINI();
									break;
								case 0x248:
									msg = new RequestExBRVersion();
									break;
								default:
									client.onUnknownPacket();
									_log.warn("Unknown client packet! State: CONNECTED, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id3).toUpperCase());
									break;
							}
							break;
						default:
							client.onUnknownPacket();
							_log.warn("Unknown client packet! State: CONNECTED, packet ID: " + Integer.toHexString(id).toUpperCase());
							break;
					}
					break;
				case AUTHED:
					switch (id)
					{
						case 0x00:
							msg = new Logout();
							break;
						case 0x0c:
							msg = new CharacterCreate(); // RequestCharacterCreate();
							break;
						case 0x0d:
							msg = new CharacterDelete(); // RequestCharacterDelete();
							break;
						case 0x12:
							msg = new CharacterSelected(); // CharacterSelect();
							break;
						case 0x13:
							msg = new NewCharacter(); // RequestNewCharacter();
							break;
						case 0x7b:
							msg = new CharacterRestore(); // RequestCharacterRestore();
							break;
						case 0xcb:
							msg = new ReplyGameGuardQuery();
							break;
						case 0xd0:
							final int id3 = buf.getShort() & 0xffff;
							switch (id3)
							{
								case 0x01:
									// msg = new RequestManorList();
									break;
								case 0x21:
									msg = new RequestKeyMapping();
									break;
								case 0x33:
									msg = new GotoLobby();
									break;
								case 0x3A:
									// msg = new RequestAllFortressInfo();
									break;
								case 0xA6:
									msg = new RequestEx2ndPasswordCheck();
									break;
								case 0xA7:
									msg = new RequestEx2ndPasswordVerify();
									break;
								case 0xA8:
									msg = new RequestEx2ndPasswordReq();
									break;
								case 0xA9:
									msg = new RequestCharacterNameCreatable();
									break;
								case 0xD1:
									msg = new RequestBR_NewIConCashBtnWnd();
									break;
								case 0x103:
									// C_EX_CLIENT_INI
									break;
								case 0x104:
									msg = new ExSendClientINI();
									break;
								case 0x11D:
									msg = new RequestTodoList();
									break;
								case 0x138:
									msg = new RequestUserBanInfo();
									break;
								case 0x15E:
									// C_EX_USER_BAN_INFO
									break;
								case 0x15F:
									// C_EX_INTERACT_MODIFY
									break;
								case 0x248:
									msg = new RequestExBRVersion();
									break;
								default:
									client.onUnknownPacket();
									_log.warn("Unknown client packet! State: AUTHED, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id3).toUpperCase());
									break;
							}
							break;
						default:
							client.onUnknownPacket();
							_log.warn("Unknown client packet! State: AUTHE, packet ID: " + Integer.toHexString(id).toUpperCase());
							break;
					}
					break;
				case IN_GAME:
					switch (id)
					{
						case 0x00:
							msg = new Logout();
							break;
						case 0x01:
							msg = new AttackRequest();
							break;
						case 0x02:
							// msg = new ?();
							break;
						case 0x03:
							msg = new RequestStartPledgeWar();
							break;
						case 0x04:
							msg = new RequestReplyStartPledgeWar();
							break;
						case 0x05:
							msg = new RequestStopPledgeWar();
							break;
						case 0x06:
							msg = new RequestReplyStopPledgeWar();
							break;
						case 0x07:
							msg = new RequestSurrenderPledgeWar();
							break;
						case 0x08:
							msg = new RequestReplySurrenderPledgeWar();
							break;
						case 0x09:
							msg = new RequestSetPledgeCrest();
							break;
						case 0x0a:
							// msg = new RequestRefreshPrivateMarketInfo();
							break;
						case 0x0b:
							msg = new RequestGiveNickName();
							break;
						case 0x0c:
							// wtf???
							break;
						case 0x0d:
							// wtf???
							break;
						case 0x0f:
							msg = new MoveBackwardToLocation();
							break;
						case 0x10:
							// msg = new Say(); Format: cS // старый ?
							break;
						case 0x11:
							msg = new RequestEnterWorld();
							break;
						case 0x12:
							// wtf???
							break;
						case 0x14:
							msg = new RequestItemList();
							break;
						case 0x15:
							msg = new RequestEquipItem();
							break;
						case 0x16:
							msg = new RequestUnEquipItem();
							break;
						case 0x17:
							msg = new RequestDropItem();
							break;
						case 0x18:
							// msg = new ?();
							break;
						case 0x19:
							msg = new UseItem();
							break;
						case 0x1a:
							msg = new TradeRequest();
							break;
						case 0x1b:
							msg = new AddTradeItem();
							break;
						case 0x1c:
							msg = new TradeDone();
							break;
						case 0x1d:
							// msg = new ?();
							break;
						case 0x1e:
							// msg = new ?();
							break;
						case 0x1f:
							msg = new Action();
							break;
						case 0x20:
							// msg = new ?();
							break;
						case 0x21:
							// msg = new ?();
							break;
						case 0x22:
							msg = new RequestLinkHtml();
							break;
						case 0x23:
							msg = new RequestBypassToServer();
							break;
						case 0x24:
							msg = new RequestBBSwrite(); // RequestBBSWrite();
							break;
						case 0x25:
							msg = new RequestCreatePledge();
							break;
						case 0x26:
							msg = new RequestJoinPledge();
							break;
						case 0x27:
							msg = new RequestAnswerJoinPledge();
							break;
						case 0x28:
							msg = new RequestWithdrawalPledge();
							break;
						case 0x29:
							msg = new RequestOustPledgeMember();
							break;
						case 0x2a:
							// msg = new ?();
							break;
						case 0x2c:
							msg = new RequestGetItemFromPet();
							break;
						case 0x2d:
							// RequestDismissParty
							break;
						case 0x2e:
							msg = new RequestAllyInfo();
							break;
						case 0x2f:
							msg = new RequestCrystallizeItem();
							break;
						case 0x30:
							// RequestPrivateStoreManage, устарел
							break;
						case 0x31:
							msg = new SetPrivateStoreSellList();
							break;
						case 0x32:
							// RequestPrivateStoreManageCancel, устарел
							break;
						case 0x33:
							msg = new RequestTeleport();
							break;
						case 0x34:
							// msg = new RequestSocialAction();
							break;
						case 0x35:
							// ChangeMoveType, устарел
							break;
						case 0x36:
							// ChangeWaitTypePacket, устарел
							break;
						case 0x37:
							msg = new RequestSellItem();
							break;
						case 0x38:
							msg = new RequestMagicSkillList();
							break;
						case 0x39:
							msg = new RequestMagicSkillUse();
							break;
						case 0x3a:
							msg = new Appearing(); // Appering();
							break;
						case 0x3b:
							if (Config.ALLOW_WAREHOUSE)
							{
								msg = new SendWareHouseDepositList();
							}
							break;
						case 0x3c:
							msg = new SendWareHouseWithDrawList();
							break;
						case 0x3d:
							msg = new RequestShortCutReg();
							break;
						case 0x3e:
							// msg = new RequestShortCutUse(); // Format: cddc ?
							break;
						case 0x3f:
							msg = new RequestShortCutDel();
							break;
						case 0x40:
							msg = new RequestBuyItem();
							break;
						case 0x41:
							// msg = new RequestDismissPledge(); //Format: c ?
							break;
						case 0x42:
							msg = new RequestJoinParty();
							break;
						case 0x43:
							msg = new RequestAnswerJoinParty();
							break;
						case 0x44:
							msg = new RequestWithDrawalParty();
							break;
						case 0x45:
							msg = new RequestOustPartyMember();
							break;
						case 0x46:
							msg = new RequestDismissParty();
							break;
						case 0x47:
							msg = new CannotMoveAnymore();
							break;
						case 0x48:
							msg = new RequestTargetCanceld();
							break;
						case 0x49:
							msg = new Say2C();
							break;
						// -- maybe GM packet's
						case 0x4a:
							id2 = buf.get() & 0xff;
							switch (id2)
							{
								case 0x00:
									// msg = new SendCharacterInfo(); // Format: S
									break;
								case 0x01:
									// msg = new SendSummonCmd(); // Format: S
									break;
								case 0x02:
									// msg = new SendServerStatus(); // Format: (noargs)
									break;
								case 0x03:
									// msg = new SendL2ParamSetting(); // Format: dd
									break;
								default:
									client.onUnknownPacket();
									_log.warn("Unknown client packet! State: IN_GAME, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id2).toUpperCase());
									break;
							}
							break;
						case 0x4b:
							// msg = new ?();
							break;
						case 0x4c:
							// msg = new ?();
							break;
						case 0x4d:
							msg = new RequestPledgeMemberList();
							break;
						case 0x4e:
							// msg = new ?();
							break;
						case 0x4f:
							// msg = new RequestMagicItem(); // Format: c ?
							break;
						case 0x50:
							msg = new RequestSkillList(); // trigger
							break;
						case 0x51:
							// msg = new ?();
							break;
						case 0x52:
							msg = new MoveWithDelta();
							break;
						case 0x53:
							msg = new RequestGetOnVehicle();
							break;
						case 0x54:
							msg = new RequestGetOffVehicle();
							break;
						case 0x55:
							msg = new AnswerTradeRequest();
							break;
						case 0x56:
							msg = new RequestActionUse();
							break;
						case 0x57:
							msg = new RequestRestart();
							break;
						case 0x58:
							msg = new RequestSiegeInfo();
							break;
						case 0x59:
							msg = new ValidatePosition();
							break;
						case 0x5a:
							msg = new RequestSEKCustom();
							break;
						case 0x5b:
							msg = new StartRotatingC();
							break;
						case 0x5c:
							msg = new FinishRotatingC();
							break;
						case 0x5d:
							// msg = new ?();
							break;
						case 0x5e:
							msg = new RequestShowBoard();
							break;
						case 0x5f:
							msg = new RequestEnchantItem();
							break;
						case 0x60:
							msg = new RequestDestroyItem();
							break;
						case 0x61:
							// msg = new ?();
							break;
						case 0x62:
							msg = new RequestQuestList();
							break;
						case 0x63:
							msg = new RequestQuestAbort(); // RequestDestroyQuest();
							break;
						case 0x64:
							// msg = new ?();
							break;
						case 0x65:
							msg = new RequestPledgeInfo();
							break;
						case 0x66:
							msg = new RequestPledgeExtendedInfo();
							break;
						case 0x67:
							msg = new RequestPledgeCrest();
							break;
						case 0x68:
							// msg = new ?();
							break;
						case 0x69:
							// msg = new ?();
							break;
						case 0x6a:
							msg = new RequestFriendInfoList();
							break;
						case 0x6b:
							msg = new RequestSendL2FriendSay();
							break;
						case 0x6c:
							msg = new RequestShowMiniMap(); // RequestOpenMinimap();
							break;
						case 0x6d:
							msg = new RequestSendMsnChatLog();
							break;
						case 0x6e:
							msg = new RequestReload(); // record video
							break;
						case 0x6f:
							msg = new RequestHennaEquip();
							break;
						case 0x70:
							msg = new RequestHennaUnequipList();
							break;
						case 0x71:
							msg = new RequestHennaUnequipInfo();
							break;
						case 0x72:
							msg = new RequestHennaUnequip();
							break;
						case 0x73:
							msg = new RequestAcquireSkillInfo(); // RequestAcquireSkillInfo();
							break;
						case 0x74:
							msg = new SendBypassBuildCmd();
							break;
						case 0x75:
							msg = new RequestMoveToLocationInVehicle();
							break;
						case 0x76:
							msg = new CannotMoveAnymore.Vehicle();
							break;
						case 0x77:
							msg = new RequestFriendInvite();
							break;
						case 0x78:
							msg = new RequestFriendAddReply();
							break;
						case 0x79:
							// msg = new RequestFriendList();
							break;
						case 0x7a:
							msg = new RequestFriendDel();
							break;
						case 0x7c:
							msg = new RequestAcquireSkill();
							break;
						case 0x7d:
							msg = new RequestRestartPoint();
							break;
						case 0x7e:
							msg = new RequestGMCommand();
							break;
						case 0x7f:
							msg = new RequestPartyMatchConfig();
							break;
						case 0x80:
							msg = new RequestPartyMatchList();
							break;
						case 0x81:
							msg = new RequestPartyMatchDetail();
							break;
						case 0x82:
							msg = new RequestPrivateStoreList();
							break;
						case 0x83:
							msg = new RequestPrivateStoreBuy();
							break;
						case 0x84:
							// msg = new ReviveReply(); // format: cd ?
							break;
						case 0x85:
							msg = new RequestTutorialLinkHtml();
							break;
						case 0x86:
							msg = new RequestTutorialPassCmdToServer();
							break;
						case 0x87:
							msg = new RequestTutorialQuestionMark(); // RequestTutorialQuestionMarkPressed();
							break;
						case 0x88:
							msg = new RequestTutorialClientEvent();
							break;
						case 0x89:
							msg = new RequestPetition();
							break;
						case 0x8a:
							msg = new RequestPetitionCancel();
							break;
						case 0x8b:
							msg = new RequestGmList();
							break;
						case 0x8c:
							msg = new RequestJoinAlly();
							break;
						case 0x8d:
							msg = new RequestAnswerJoinAlly();
							break;
						case 0x8e:
							// Команда /allyleave - выйти из альянса
							msg = new RequestWithdrawAlly();
							break;
						case 0x8f:
							// Команда /allydismiss - выгнать клан из альянса
							msg = new RequestOustAlly();
							break;
						case 0x90:
							// Команда /allydissolve - распустить альянс
							msg = new RequestDismissAlly();
							break;
						case 0x91:
							msg = new RequestSetAllyCrest();
							break;
						case 0x92:
							msg = new RequestAllyCrest();
							break;
						case 0x93:
							msg = new RequestChangePetName();
							break;
						case 0x94:
							msg = new RequestPetUseItem();
							break;
						case 0x95:
							msg = new RequestGiveItemToPet();
							break;
						case 0x96:
							msg = new RequestPrivateStoreQuitSell();
							break;
						case 0x97:
							msg = new SetPrivateStoreMsgSell();
							break;
						case 0x98:
							msg = new RequestPetGetItem();
							break;
						case 0x99:
							msg = new RequestPrivateStoreBuyManage();
							break;
						case 0x9a:
							msg = new SetPrivateStoreBuyList();
							break;
						case 0x9b:
							//
							break;
						case 0x9c:
							msg = new RequestPrivateStoreQuitBuy();
							break;
						case 0x9d:
							msg = new SetPrivateStoreMsgBuy();
							break;
						case 0x9e:
							//
							break;
						case 0x9f:
							msg = new RequestPrivateStoreBuySellList();
							break;
						case 0xa0:
							msg = new RequestTimeCheck();
							break;
						case 0xa1:
							// msg = new ?();
							break;
						case 0xa2:
							// msg = new ?();
							break;
						case 0xa3:
							// msg = new ?();
							break;
						case 0xa4:
							// msg = new ?();
							break;
						case 0xa5:
							// msg = new ?();
							break;
						case 0xa6:
							msg = new RequestSkillCoolTime();
							break;
						case 0xa7:
							msg = new RequestPackageSendableItemList();
							break;
						case 0xa8:
							msg = new RequestPackageSend();
							break;
						case 0xa9:
							msg = new RequestBlock();
							break;
						case 0xaa:
							// msg = new RequestCastleSiegeInfo(); // format: cd ?
							break;
						case 0xab:
							msg = new RequestCastleSiegeAttackerList();
							break;
						case 0xac:
							msg = new RequestCastleSiegeDefenderList();
							break;
						case 0xad:
							msg = new RequestJoinCastleSiege();
							break;
						case 0xae:
							msg = new RequestConfirmCastleSiegeWaitingList();
							break;
						case 0xaf:
							msg = new RequestSetCastleSiegeTime();
							break;
						case 0xb0:
							msg = new RequestMultiSellChoose();
							break;
						case 0xb1:
							msg = new NetPing();
							break;
						case 0xb2:
							msg = new RequestRemainTime();
							break;
						case 0xb3:
							msg = new BypassUserCmd();
							break;
						case 0xb4:
							msg = new SnoopQuit();
							break;
						case 0xb5:
							msg = new RequestRecipeBookOpen();
							break;
						case 0xb6:
							msg = new RequestRecipeItemDelete();
							break;
						case 0xb7:
							msg = new RequestRecipeItemMakeInfo();
							break;
						case 0xb8:
							msg = new RequestRecipeItemMakeSelf();
							break;
						case 0xb9:
							// msg = new RequestRecipeShopManageList(); deprecated // format: c
							break;
						case 0xba:
							msg = new RequestRecipeShopMessageSet();
							break;
						case 0xbb:
							msg = new RequestRecipeShopListSet();
							break;
						case 0xbc:
							msg = new RequestRecipeShopManageQuit();
							break;
						case 0xbd:
							msg = new RequestRecipeShopManageCancel();
							break;
						case 0xbe:
							msg = new RequestRecipeShopMakeInfo();
							break;
						case 0xbf:
							msg = new RequestRecipeShopMakeDo();
							break;
						case 0xc0:
							msg = new RequestRecipeShopSellList();
							break;
						case 0xc1:
							msg = new RequestObserverEnd();
							break;
						case 0xc2:
							// msg = new VoteSociality(); // Recommend
							break;
						case 0xc3:
							msg = new RequestHennaList(); // RequestHennaItemList();
							break;
						case 0xc4:
							msg = new RequestHennaItemInfo();
							break;
						case 0xc5:
							// msg = new RequestBuySeed();
							break;
						case 0xc6:
							msg = new ConfirmDlg();
							break;
						case 0xc7:
							msg = new RequestPreviewItem();
							break;
						case 0xc8:
							msg = new RequestSSQStatus();
							break;
						case 0xc9:
							msg = new PetitionVote();
							break;
						case 0xca:
							// msg = new ?();
							break;
						case 0xcb:
							msg = new ReplyGameGuardQuery();
							break;
						case 0xcc:
							msg = new RequestPledgePower();
							break;
						case 0xcd:
							msg = new RequestMakeMacro();
							break;
						case 0xce:
							msg = new RequestDeleteMacro();
							break;
						case 0xcf:
							// msg = new RequestProcureCrop(); // ?
							break;
						case 0xd0:
							final int id3 = buf.getShort() & 0xffff;
							switch (id3)
							{
								case 0x00:
									// msg = new ?();
									break;
								case 0x01:
									// msg = new RequestManorList();
									break;
								case 0x02:
									// msg = new RequestProcureCropList();
									break;
								case 0x03:
									// msg = new RequestSetSeed();
									break;
								case 0x04:
									// msg = new RequestSetCrop();
									break;
								case 0x05:
									msg = new RequestWriteHeroWords();
									break;
								case 0x06:
									msg = new RequestExMPCCAskJoin(); // RequestExAskJoinMPCC();
									break;
								case 0x07:
									msg = new RequestExMPCCAcceptJoin(); // RequestExAcceptJoinMPCC();
									break;
								case 0x08:
									msg = new RequestExOustFromMPCC();
									break;
								case 0x09:
									msg = new RequestOustFromPartyRoom();
									break;
								case 0x0A:
									msg = new RequestDismissPartyRoom();
									break;
								case 0x0B:
									msg = new RequestWithdrawPartyRoom();
									break;
								case 0x0C:
									msg = new RequestHandOverPartyMaster();
									break;
								case 0x0D:
									msg = new RequestAutoSoulShot();
									break;
								case 0x0E:
									// msg = new RequestExEnchantSkillInfo();
									break;
								case 0x0F:
									final int type = buf.getInt();
									switch (type)
									{
										case 0x00:
											// msg = new RequestExEnchantSkill();
											break;
										case 0x01:
											// msg = new RequestExEnchantSkillSafe();
											break;
										case 0x02:
											// msg = new RequestExEnchantSkillUntrain();
											break;
										case 0x03:
											// msg = new RequestExEnchantSkillRouteChange();
											break;
										case 0x04:
											// msg = new RequestExEnchantSkillImmortal();
											break;
										default:
											client.onUnknownPacket();
											_log.warn("Unknown client packet! State: IN_GAME, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id3).toUpperCase() + ":" + Integer.toHexString(type).toUpperCase());
											break;
									}
									break;
								case 0x10:
									msg = new RequestPledgeCrestLarge();
									break;
								case 0x11:
									msg = new RequestExSetPledgeCrestLargeFirstPart();
									break;
								case 0x12:
									msg = new RequestPledgeSetAcademyMaster();
									break;
								case 0x13:
									msg = new RequestPledgePowerGradeList();
									break;
								case 0x14:
									msg = new RequestPledgeMemberPowerInfo();
									break;
								case 0x15:
									msg = new RequestPledgeSetMemberPowerGrade();
									break;
								case 0x16:
									msg = new RequestPledgeMemberInfo();
									break;
								case 0x17:
									msg = new RequestPledgeWarList();
									break;
								case 0x18:
									// msg = new RequestExFishRanking();
									break;
								case 0x19:
									msg = new RequestPCCafeCouponUse();
									break;
								case 0x1A:
									// msg = new RequestServerLogin(); (chb) b - array размером в 64 байта
									break;
								case 0x1B:
									msg = new RequestDuelStart();
									break;
								case 0x1C:
									msg = new RequestDuelAnswerStart();
									break;
								case 0x1D:
									msg = new RequestTutorialClientEvent(); // RequestExSetTutorial(); Format: d /
																			// требует отладки,
																			// ИМХО, это совсем другой
																			// пакет (с) Drin
									break;
								case 0x1E:
									msg = new RequestExRqItemLink();
									break;
								case 0x1F:
									msg = new CannotMoveAnymore.AirShip(); // () (ddddd)
									break;
								case 0x20:
									// msg = new RequestExMoveToLocationInAirShip();
									break;
								case 0x21:
									msg = new RequestKeyMapping();
									break;
								case 0x22:
									msg = new RequestSaveKeyMapping();
									break;
								case 0x23:
									msg = new RequestExRemoveItemAttribute();
									break;
								case 0x24:
									msg = new RequestSaveInventoryOrder();
									break;
								case 0x25:
									msg = new RequestExitPartyMatchingWaitingRoom();
									break;
								case 0x26:
									msg = new RequestConfirmTargetItem();
									break;
								case 0x27:
									msg = new RequestConfirmRefinerItem();
									break;
								case 0x28:
									msg = new RequestConfirmGemStone();
									break;
								case 0x29:
									msg = new RequestOlympiadObserverEnd();
									break;
								case 0x2A:
									// msg = new RequestCursedWeaponList();
									break;
								case 0x2B:
									// msg = new RequestCursedWeaponLocation();
									break;
								case 0x2C:
									msg = new RequestPledgeReorganizeMember();
									break;
								case 0x2D:
									msg = new RequestExMPCCShowPartyMembersInfo();
									break;
								case 0x2E:
									msg = new RequestExOlympiadObserverEnd(); // не уверен (в клиенте называется
																				// RequestOlympiadMatchList)
									break;
								case 0x2F:
									msg = new RequestAskJoinPartyRoom();
									break;
								case 0x30:
									msg = new AnswerJoinPartyRoom();
									break;
								case 0x31:
									msg = new RequestListPartyMatchingWaitingRoom();
									break;
								case 0x32:
									// msg = new RequestEnchantItemAttribute();
									break;
								case 0x33:
									// msg = new RequestGotoLobby();
									break;
								case 0x35:
									// msg = new RequestExMoveToLocationAirShip();
									break;
								case 0x36:
									// msg = new RequestBidItemAuction();
									break;
								case 0x37:
									// msg = new RequestInfoItemAuction();
									break;
								case 0x38:
									msg = new RequestExChangeName();
									break;
								case 0x39:
									msg = new RequestAllCastleInfo();
									break;
								case 0x3A:
									// msg = new RequestAllFortressInfo();
									break;
								case 0x3B:
									msg = new RequestAllAgitInfo();
									break;
								case 0x3C:
									// msg = new RequestFortressSiegeInfo();
									break;
								case 0x3D:
									// msg = new RequestGetBossRecord();
									break;
								case 0x3E:
									msg = new RequestRefine();
									break;
								case 0x3F:
									msg = new RequestConfirmCancelItem();
									break;
								case 0x40:
									msg = new RequestRefineCancel();
									break;
								case 0x41:
									msg = new RequestExMagicSkillUseGround();
									break;
								case 0x42:
									msg = new RequestDuelSurrender();
									break;
								case 0x43:
									// msg = new RequestExEnchantSkillInfoDetail();
									break;
								/** 0xD0:0x44 - пропущен корейцами */
								case 0x45:
									// msg = new RequestFortressMapInfo();
									break;
								case 0x46:
									msg = new RequestPVPMatchRecord();
									break;
								case 0x47:
									msg = new SetPrivateStoreWholeMsg();
									break;
								case 0x48:
									msg = new RequestDispel();
									break;
								case 0x49:
									msg = new RequestExTryToPutEnchantTargetItem();
									break;
								case 0x4A:
									msg = new RequestExTryToPutEnchantSupportItem();
									break;
								case 0x4B:
									msg = new RequestExCancelEnchantItem();
									break;
								case 0x4C:
									msg = new RequestChangeNicknameColor();
									break;
								case 0x4D:
									msg = new RequestResetNickname();
									break;
								case 0x4E:
									final int id4 = buf.getInt();
									switch (id4)
									{
										case 0x00:
											msg = new RequestBookMarkSlotInfo();
											break;
										case 0x01:
											msg = new RequestSaveBookMarkSlot();
											break;
										case 0x02:
											msg = new RequestModifyBookMarkSlot();
											break;
										case 0x03:
											msg = new RequestDeleteBookMarkSlot();
											break;
										case 0x04:
											msg = new RequestTeleportBookMark();
											break;
										case 0x05:
											msg = new RequestChangeBookMarkSlot();
											break;
										default:
											client.onUnknownPacket();
											_log.warn("Unknown client packet! State: IN_GAME, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id3).toUpperCase() + ":" + Integer.toHexString(id4).toUpperCase());
											break;
									}
									break;
								case 0x4F:
									msg = new RequestWithDrawPremiumItem();
									break;
								case 0x50:
									msg = new RequestExJump();
									break;
								case 0x51:
									// msg = new RequestExStartShowCrataeCubeRank();
									break;
								case 0x52:
									// msg = new RequestExStopShowCrataeCubeRank();
									break;
								case 0x53:
									msg = new NotifyStartMiniGame();
									break;
								case 0x54:
									msg = new RequestExJoinDominionWar();
									break;
								case 0x55:
									msg = new RequestExDominionInfo();
									break;
								case 0x56:
									msg = new RequestExCleftEnter();
									break;
								case 0x57:
									// msg = new RequestExCubeGameChangeTeam();
									break;
								case 0x58:
									msg = new RequestExEndScenePlayer();
									break;
								case 0x59:
									// msg = new RequestExCubeGameReadyAnswer(); // RequestExBlockGameVote
									break;
								case 0x5A:
									msg = new RequestExListMpccWaiting();
									break;
								case 0x5B:
									msg = new RequestExManageMpccRoom();
									break;
								case 0x5C:
									msg = new RequestExJoinMpccRoom();
									break;
								case 0x5D:
									msg = new RequestExOustFromMpccRoom();
									break;
								case 0x5E:
									msg = new RequestExDismissMpccRoom();
									break;
								case 0x5F:
									msg = new RequestExWithdrawMpccRoom();
									break;
								case 0x60:
									// msg = new RequestExSeedPhase();
									break;
								case 0x61:
									msg = new RequestExMpccPartymasterList();
									break;
								case 0x62:
									msg = new RequestExPostItemList();
									break;
								case 0x63:
									msg = new RequestExSendPost();
									break;
								case 0x64:
									msg = new RequestExRequestReceivedPostList();
									break;
								case 0x65:
									msg = new RequestExDeleteReceivedPost();
									break;
								case 0x66:
									msg = new RequestExRequestReceivedPost();
									break;
								case 0x67:
									msg = new RequestExReceivePost();
									break;
								case 0x68:
									msg = new RequestExRejectPost();
									break;
								case 0x69:
									msg = new RequestExRequestSentPostList();
									break;
								case 0x6A:
									msg = new RequestExDeleteSentPost();
									break;
								case 0x6B:
									msg = new RequestExRequestSentPost();
									break;
								case 0x6C:
									msg = new RequestExCancelSentPost();
									break;
								case 0x6D:
									msg = new RequestExShowNewUserPetition();
									break;
								case 0x6E:
									msg = new RequestExShowStepTwo();
									break;
								case 0x6F:
									msg = new RequestExShowStepThree();
									break;
								case 0x70:
									// msg = new ExConnectToRaidServer(); (chddd)
									break;
								case 0x71:
									// msg = new ExReturnFromRaidServer(); (chd)
									break;
								case 0x72:
									msg = new RequestExRefundItem();
									break;
								case 0x73:
									msg = new RequestExBuySellUIClose();
									break;
								case 0x74:
									msg = new RequestExEventMatchObserverEnd();
									break;
								case 0x75:
									msg = new RequestPartyLootModification();
									break;
								case 0x76:
									msg = new AnswerPartyLootModification();
									break;
								case 0x77:
									msg = new AnswerCoupleAction();
									break;
								case 0x78:
									msg = new RequestExBR_EventRankerList();
									break;
								case 0x79:
									// msg = new RequestAskMemberShip();
									break;
								case 0x7A:
									msg = new RequestAddExpandQuestAlarm();
									break;
								case 0x7B:
									msg = new RequestVoteNew();
									break;
								case 0x7C:
									msg = new RequestGetOnShuttle();
									break;
								case 0x7D:
									msg = new RequestGetOffShuttle();
									break;
								case 0x7E:
									msg = new RequestMoveToLocationInShuttle();
									break;
								case 0x7F:
									msg = new CannotMoveAnymore.Shuttle(); // CannotMoveAnymoreInShuttle(); (chddddd)
									break;
								case 0x80:
									final int id5 = buf.getInt();
									switch (id5)
									{
										case 0x01:
											// msg = new RequestExAgitInitialize chd 0x01
											break;
										case 0x02:
											// msg = new RequestExAgitDetailInfo chdcd 0x02
											break;
										case 0x03:
											// msg = new RequestExMyAgitState chd 0x03
											break;
										case 0x04:
											// msg = new RequestExRegisterAgitForBidStep1 chd 0x04
											break;
										case 0x05:
											// msg = new RequestExRegisterAgitForBidStep2 chddQd 0x05 //msg = new
											// RequestExRegisterAgitForBidStep3 chddQd 0x05 -no error? 0x05
											break;
										case 0x07:
											// msg = new RequestExConfirmCancelRegisteringAgit chd 0x07
											break;
										case 0x08:
											// msg = new RequestExProceedCancelRegisteringAgit chd 0x08
											break;
										case 0x09:
											// msg = new RequestExConfirmCancelAgitBid chdd 0x09
											break;
										case 0x10:
											// msg = new RequestExReBid chdd 0x10
											break;
										case 0x11:
											// msg = new RequestExAgitListForLot chd 0x11
											break;
										case 0x12:
											// msg = new RequestExApplyForAgitLotStep1 chdc 0x12
											break;
										case 0x13:
											// msg = new RequestExApplyForAgitLotStep2 chdc 0x13
											break;
										case 0x14:
											// msg = new RequestExAgitListForBid chdd 0x14
											break;
										case 0x0D:
											// msg = new RequestExApplyForBidStep1 chdd 0x0D
											break;
										case 0x0E:
											// msg = new RequestExApplyForBidStep2 chddQ 0x0E
											break;
										case 0x0F:
											// msg = new RequestExApplyForBidStep3 chddQ 0x0F
											break;
										// case 0x09:
										// msg = new RequestExConfirmCancelAgitLot chdc 0x09
										// break;
										case 0x0A:
											// msg = new RequestExProceedCancelAgitLot chdc 0x0A
											break;
										default:
											client.onUnknownPacket();
											_log.warn("Unknown client packet! State: IN_GAME, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id3).toUpperCase() + ":" + Integer.toHexString(id5).toUpperCase());
											break;
										// case 0x0A:
										// msg = new RequestExProceedCancelAgitBid chdd 0x0A
										// break;
									}
									break;
								case 0x81:
									msg = new RequestExAddPostFriendForPostBox();
									break;
								case 0x82:
									msg = new RequestExDeletePostFriendForPostBox();
									break;
								case 0x83:
									msg = new RequestExShowPostFriendListForPostBox();
									break;
								case 0x84:
									msg = new RequestExFriendListForPostBox(); // TODO[K] - по сути является 84 у
																				// оверов, но в
																				// клиенте никак не используется!
									break;
								case 0x85:
									msg = new RequestOlympiadMatchList(); // TODO[K] - должен работать в буфере (на 00
																			// позиции).
																			// Может заготовка корейцев
																			// на будущее О_О?
									break;
								case 0x86:
									msg = new RequestExBR_GamePoint();
									break;
								case 0x87:
									// msg = new RequestExBR_ProductList();
									break;
								case 0x88:
									// msg = new RequestExBR_ProductInfo();
									break;
								case 0x89:
									msg = new RequestExBR_BuyProduct();
									break;
								case 0x8A:
									// msg = new RequestExBR_RecentProductList();
									break;
								case 0x8B:
									msg = new RequestBR_MiniGameLoadScores();
									break;
								case 0x8C:
									msg = new RequestBR_MiniGameInsertScore();
									break;
								case 0x8D:
									msg = new RequestExBR_LectureMark();
									break;
								case 0x8E:
									msg = new RequestCrystallizeEstimate();
									break;
								case 0x8F:
									msg = new RequestCrystallizeItemCancel();
									break;
								case 0x90:
									msg = new RequestExEscapeScene();
									break;
								case 0x91:
									// msg = new RequestFlyMove();
									break;
								case 0x92:
									// msg = new RequestSurrenderPledgeWarEX(); (chS)
									break;
								case 0x93:
									final int id6 = buf.get();
									switch (id6)
									{
										case 0x02:
											// msg = new RequestDynamicQuestProgressInfo();
											break;
										case 0x03:
											// msg = new RequestDynamicQuestScoreBoard();
											break;
										case 0x04:
											// msg = new RequestDynamicQuestHTML();
											break;
										default:
											client.onUnknownPacket();
											_log.warn("Unknown client packet! State: IN_GAME, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id3).toUpperCase() + ":" + Integer.toHexString(id6).toUpperCase());
											break;
									}
									break;
								case 0x94:
									msg = new RequestFriendDetailInfo();
									break;
								case 0x95:
									msg = new RequestUpdateFriendMemo();
									break;
								case 0x96:
									msg = new RequestUpdateBlockMemo();
									break;
								case 0x97:
									// msg = new RequestInzonePartyInfoHistory(); (ch) TODO[K]
									break;
								case 0x98:
									// msg = new RequestCommissionRegistrableItemList();
									break;
								case 0x99:
									// msg = new RequestCommissionInfo();
									break;
								case 0x9A:
									// msg = new RequestCommissionRegister();
									break;
								case 0x9B:
									// msg = new RequestCommissionCancel();
									break;
								case 0x9C:
									// msg = new RequestCommissionDelete();
									break;
								case 0x9D:
									// msg = new RequestCommissionList();
									break;
								case 0x9E:
									// msg = new RequestCommissionBuyInfo();
									break;
								case 0x9F:
									// msg = new RequestCommissionBuyItem();
									break;
								case 0xA0:
									// msg = new RequestCommissionRegisteredItem();
									break;
								case 0xA1:
									// msg = new RequestCallToChangeClass();
									break;
								case 0xA2:
									// msg = new RequestChangeToAwakenedClass();
									break;
								case 0xA3:
									// msg = new RequestWorldStatistics();
									break;
								case 0xA4:
									// msg = new RequestUserStatistics();
									break;
								case 0xA5:
									msg = new RequestRegistPartySubstitute();
									break;
								case 0xA6:
									msg = new RequestDeletePartySubstitute();
									break;
								case 0xA7:
									msg = new RequestRegistWaitingSubstitute();
									break;
								case 0xA8:
									msg = new RequestAcceptWaitingSubstitute();
									break;
								case 0xA9:
									// msg = new Request24HzSessionID();
									break;
								case 0xAA:
									msg = new RequestGoodsInventoryInfo();
									break;
								case 0xAB:
									final int id7 = buf.getInt();
									switch (id7)
									{
										case 0x00:
											// msg = new RequestUseGoodsInventoryItem();
											break;
										case 0x01:
											// msg = new RequestUseGoodsInventoryItem();
											break;
										default:
											client.onUnknownPacket();
											_log.warn("Unknown client packet! State: IN_GAME, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id7).toUpperCase());
									}
									break;
								case 0xAC:
									msg = new RequestFirstPlayStart();
									break;
								case 0xAD:
									// msg = new RequestFlyMoveStart();
									break;
								case 0xAE:
									msg = new RequestHardWareInfo();
									break;
								case 0xB0:
									msg = new SendChangeAttributeTargetItem();
									break;
								case 0xB1:
									msg = new RequestChangeAttributeItem();
									break;
								case 0xB2:
									msg = new RequestChangeAttributeCancel();
									break;
								case 0xB3:
									// msg = new RequestBR_PresentBuyProduct();
									break;
								case 0xB4:
									// msg = new ConfirmMenteeAdd();
									break;
								case 0xB5:
									// msg = new RequestMentorCancel();
									break;
								case 0xB6:
									// msg = new RequestMentorList();
									break;
								case 0xB7:
									msg = new RequestMenteeAdd();
									break;
								case 0xB8:
									msg = new RequestMenteeWaitingList();
									break;
								case 0xB9:
									msg = new RequestJoinPledgeByName();
									break;
								case 0xBA:
									msg = new RequestInzoneWaitingTime();
									break;
								case 0xBB:
									// msg = new RequestJoinCuriousHouse();
									break;
								case 0xBC:
									// msg = new RequestCancelCuriousHouse();
									break;
								case 0xBD:
									// msg = new RequestLeaveCuriousHouse();
									break;
								case 0xBE:
									// msg = new RequestObservingListCuriousHouse();
									break;
								case 0xBF:
									// msg = new RequestObservingCuriousHouse();
									break;
								case 0xC0:
									// msg = new RequestLeaveObservingCuriousHouse();
									break;
								case 0xC1:
									// msg = new RequestCuriousHouseHtml();
									break;
								case 0xC2:
									// msg = new RequestCuriousHouseRecord();
									break;
								case 0xC3:
									// msg = new ExSysstring();
									break;
								case 0xC4:
									msg = new RequestExTryToPutShapeShiftingTargetItem();
									break;
								case 0xC5:
									msg = new RequestExTryToPutShapeShiftingEnchantSupportItem();
									break;
								case 0xC6:
									msg = new RequestExCancelShapeShiftingItem();
									break;
								case 0xC7:
									msg = new RequestShapeShiftingItem();
									break;
								case 0xC8:
									// msg = new NCGuardSendDataToServer();
									break;
								case 0xC9:
									// msg = new RequestEventKalieToken();
									break;
								case 0xCA:
									// msg = new RequestShowBeautyList();
									break;
								case 0xCB:
									// msg = new RequestRegistBeauty();
									break;
								case 0xCC:
									// TODO
									break;
								case 0xCD:
									// msg = new RequestShowResetShopList(); // (ch) TODO[K]
									break;
								case 0xCE:
									// msg = new NetPing();
									break;
								case 0xCF:
									// msg = new RequestBR_AddBasketProductInfo();
									break;
								case 0xD0:
									// msg = new RequestBR_DeleteBasketProductInfo();
									break;
								case 0xD1:
									msg = new RequestBR_NewIConCashBtnWnd();
									break;
								case 0xD2:
									// msg = new RequestExEvent_Campaign_Info();
									break;
								case 0xD3:
									msg = new RequestPledgeRecruitInfo();
									break;
								case 0xD4:
									msg = new RequestPledgeRecruitBoardSearch();
									break;
								case 0xD5:
									msg = new RequestPledgeRecruitBoardAccess();
									break;
								case 0xD6:
									msg = new RequestPledgeRecruitBoardDetail();
									break;
								case 0xD7:
									msg = new RequestPledgeWaitingApply();
									break;
								case 0xD8:
									msg = new RequestPledgeWaitingApplied();
									break;
								case 0xD9:
									msg = new RequestPledgeWaitingList();
									break;
								case 0xDA:
									msg = new RequestPledgeWaitingUser();
									break;
								case 0xDB:
									msg = new RequestPledgeWaitingUserAccept();
									break;
								case 0xDC:
									msg = new RequestPledgeDraftListSearch();
									break;
								case 0xDD:
									msg = new RequestPledgeDraftListApply();
									break;
								case 0xDE:
									msg = new RequestPledgeRecruitApplyInfo();
									break;
								case 0xDF:
									msg = new RequestPledgeJoinSys();
									break;
								case 0xE0:
									// msg = new ResponsePetitionAlarm();
									break;
								case 0xE1:
									msg = new NotifyExitBeautyshop();
									break;
								case 0xE2:
									// msg = new RequestRegisterXMasWishCard();
									break;
								case 0xE3:
									msg = new RequestExAddEnchantScrollItem();
									break;
								case 0xE4:
									msg = new RequestExRemoveEnchantSupportItem();
									break;
								case 0xE5:
									// msg = new RequestCardReward();
									break;
								case 0xE6:
									msg = new RequestDivideAdenaStart();
									break;
								case 0xE7:
									msg = new RequestDivideAdenaCancel();
									break;
								case 0xE8:
									msg = new RequestDivideAdena();
									break;
								case 0xE9:
									// msg = new RequestAcquireAbilityList();
									break;
								case 0xEA:
									// msg = new RequestAbilityList();
									break;
								case 0xEB:
									// msg = new RequestResetAbilityPoint();
									break;
								case 0xEC:
									// msg = new RequestChangeAbilityPoint();
									break;
								case 0xED:
									msg = new RequestStopMove();
									break;
								case 0xEE:
									// msg = new RequestAbilityWndOpen();
									break;
								case 0xEF:
									// msg = new RequestAbilityWndClose();
									break;
								case 0xF0:
									msg = new RequestLuckyGameStartInfo();
									break;
								case 0xF1:
									msg = new RequestLuckyGamePlay();
									break;
								case 0xF2:
									msg = new NotifyTrainingRoomEnd();
									break;
								case 0xF3:
									msg = new RequestNewEnchantPushOne();
									break;
								case 0xF4:
									msg = new RequestNewEnchantRemoveOne();
									break;
								case 0xF5:
									msg = new RequestNewEnchantPushTwo();
									break;
								case 0xF6:
									msg = new RequestNewEnchantRemoveTwo();
									break;
								case 0xF7:
									msg = new RequestNewEnchantClose();
									break;
								case 0xF8:
									msg = new RequestNewEnchantTry();
									break;
								case 0xF9:
									// msg = new RequestNewEnchantRetryToPutItems();
									break;
								case 0xFA:
									// TODO
									break;
								case 0xFB:
									// TODO
									break;
								case 0xFC:
									// TODO
									break;
								case 0xFD:
									msg = new RequestTargetActionMenu();
									break;
								case 0xFE:
									msg = new ExSendSelectedQuestZoneID();
									break;
								case 0xFF:
									// msg = new RequestAlchemySkillList();
									break;
								case 0x100:
									// msg = new RequestAlchemyTryMixCube();
									break;
								case 0x101:
									// msg = new RequestAlchemyConversion();
									break;
								case 0x102:
									// TODO
									break;
								case 0x103:
									msg = new ExSendClientINI();
									break;
								case 0x104:
									msg = new RequestExAutoFish();
									break;
								case 0x105:
									msg = new RequestVipAttendanceItemList();
									break;
								case 0x106:
									msg = new RequestVipAttendanceCheck();
									break;
								case 0x107:
									msg = new RequestItemEnsoul();
									break;
								case 0x108:
									// msg = new RequestCastleWarSeasonReward();
									break;
								case 0x109:
									msg = new RequestVipProductList();
									break;
								case 0x10A:
									msg = new RequestVipLuckyGameInfo();
									break;
								case 0x10B:
									msg = new RequestVipLuckyGameItemList();
									break;
								case 0x10C:
									msg = new RequestVipLuckyGameBonus();
									break;
								case 0x10D:
									msg = new ExRequestVipInfo();
									break;
								case 0x10E:
									// msg = new RequestCaptchaAnswer();
									break;
								case 0x10F:
									// msg = new RequestRefreshCaptchaImage();
									break;
								case 0x110:
									msg = new RequestPledgeSignInForOpenJoiningMethod();
									break;
								case 0x111:
									// msg = new ExRequestMatchArena();
									break;
								case 0x112:
									// msg = new ExConfirmMatchArena();
									break;
								case 0x113:
									// msg = new ExCancelMatchArena();
									break;
								case 0x114:
									// msg = new ExChangeClassArena();
									break;
								case 0x115:
									// msg = new ExConfirmClassArena();
									break;
								case 0x116:
									// msg = new RequestOpenDecoNPCUI();
									break;
								case 0x117:
									// msg = new RequestCheckAgitDecoAvailability();
									break;
								case 0x118:
									// msg = new RequestUserFactionInfo();
									break;
								case 0x119:
									// msg = new ExExitArena();
									break;
								case 0x11A:
									msg = new RequestExBalthusToken();
									break;
								case 0x11B:
									msg = new RequestPartyMatchingHistory();
									break;
								case 0x11C:
									// msg = new ExArenaCustomNotification();
									break;
								case 0x11D:
									msg = new RequestTodoList();
									break;
								case 0x11E:
									msg = new RequestTodoListHTML();
									break;
								case 0x11F:
									msg = new RequestOneDayRewardReceive();
									break;
								case 0x120:
									// msg = new RequestQueueTicket();
									break;
								case 0x121:
									msg = new RequestPledgeBonusOpen();
									break;
								case 0x122:
									msg = new RequestPledgeBonusRewardList();
									break;
								case 0x123:
									msg = new RequestPledgeBonusReward();
									break;
								case 0x124:
									// msg = new RequestSSOAuthnToken();
									break;
								case 0x125:
									// msg = new RequestQueueTicketLogin();
									break;
								case 0x126:
									msg = new RequestBlockMemoInfo();
									break;
								case 0x127:
									msg = new RequestTryEnSoulExtraction();
									break;
								case 0x128:
									msg = new RequestRaidBossSpawnInfo();
									break;
								case 0x129:
									msg = new RequestRaidServerInfo();
									break;
								case 0x12A:
									msg = new RequestShowAgitSiegeInfo();
									break;
								case 0x12B:
									msg = new RequestItemAuctionStatus();
									break;
								case 0x12C:
									// msg = new RequestMonsterBookOpen();
									break;
								case 0x12D:
									// msg = new RequestMonsterBookClose();
									break;
								case 0x12E:
									// msg = new RequestMonsterBookReward();
									break;
								case 0x12F:
									// msg = new ExRequestMatchGroup();
									break;
								case 0x130:
									// msg = new ExRequestMatchGroupAsk();
									break;
								case 0x131:
									// msg = new ExRequestMatchGroupAnswer();
									break;
								case 0x132:
									// msg = new ExRequestMatchGroupWithdraw();
									break;
								case 0x133:
									// msg = new ExRequestMatchGroupOust();
									break;
								case 0x134:
									// msg = new ExRequestMatchGroupChangeMaster();
									break;
								case 0x135:
									msg = new RequestUpgradeSystemResult();
									break;
								case 0x136:
									// msg = new RequestCardUpdownGamePickNumber();
									break;
								case 0x137:
									// msg = new RequestCardUpdownGameReward();
									break;
								case 0x138:
									// msg = new RequestCardUpdownGameRetry();
									break;
								case 0x139:
									// msg = new RequestCardUpdownGameQuit();
									break;
								case 0x13A:
									// msg = new ExRequestArenaRankAll();
									break;
								case 0x13B:
									// msg = new ExRequestArenaMyRank();
									break;
								case 0x13C:
									msg = new RequestSwapAgathionSlotItems();
									break;
								case 0x13D:
									// msg = new RequestExPledgeContributionRank();
									break;
								case 0x13E:
									// msg = new RequestExPledgeContributionInfo();
									break;
								case 0x13F:
									// msg = new RequestExPledgeContributionReward();
									break;
								case 0x140:
									// msg = new RequestExPledgeLevelUp();
									break;
								case 0x141:
									// msg = new RequestPledgeMissionInfo();
									break;
								case 0x142:
									// msg = new RequestPledgeMissionReward();
									break;
								case 0x143:
									// msg = new RequestExPledgeMasteryInfo();
									break;
								case 0x144:
									// msg = new RequestExPledgeMasterySet();
									break;
								case 0x145:
									// msg = new RequestExPledgeMasteryReset();
									break;
								case 0x146:
									// msg = new RequestExPledgeSkillInfo();
									break;
								case 0x147:
									// msg = new RequestExPledgeSkillActivate();
									break;
								case 0x148:
									// msg = new RequestExPledgeItemList();
									break;
								case 0x149:
									// msg = new RequestExPledgeItemActivate();
									break;
								case 0x14A:
									// msg = new RequestExPledgeAnnounce();
									break;
								case 0x14B:
									// msg = new RequestExPledgeAnnounceSet();
									break;
								case 0x14C:
									// msg = new RequestCreatePledge();
									break;
								case 0x14D:
									// msg = new RequestExPledgeItemInfo();
									break;
								case 0x14E:
									// msg = new RequestExPledgeItemBuy();
									break;
								case 0x14F:
									msg = new RequestExElementalSpiritInfo();
									break;
								case 0x150:
									msg = new RequestExElementalSpiritExtractInfo();
									break;
								case 0x151:
									msg = new RequestExElementalSpiritExtract();
									break;
								case 0x152:
									msg = new RequestExElementalSpiritEvolutionInfo();
									break;
								case 0x153:
									msg = new RequestExElementalSpiritEvolution();
									break;
								case 0x154:
									msg = new RequestExElementalSpiritSetTalent();
									break;
								case 0x155:
									msg = new RequestExElementalSpiritInitTalent();
									break;
								case 0x156:
									msg = new RequestExElementalSpiritAbsorbInfo();
									break;
								case 0x157:
									msg = new RequestExElementalSpiritAbsorb();
									break;
								case 0x158:
									// msg = new RequestExRequestLockedItem();
									break;
								case 0x159:
									// msg = new RequestExRequestUnlockedItem();
									break;
								case 0x15A:
									// msg = new RequestExLockedItemCancel();
									break;
								case 0x15B:
									// msg = new RequestExUnLockedItemCancel();
									break;
								case 0x15C:
									msg = new RequestExElementalSpiritChangeType();
									break;
								case 0x15D:
									// C_EX_BLOCK_PACKET_FOR_AD
									break;
								case 0x15E:
									// C_EX_USER_BAN_INFO
									break;
								case 0x15F:
									// C_EX_INTERACT_MODIFY
									break;
								case 0x160:
									// msg = new RequestExTryEnchantArtifact(); //C_EX_TRY_ENCHANT_ARTIFACT
									break;
								case 0x161:
									msg = new ExUpgradeSystemNormalRequest();
									break;
								case 0x162:
									msg = new RequestExPurchaseLimitShopItemList(); // C_EX_PURCHASE_LIMIT_SHOP_ITEM_LIST
									break;
								case 0x163:
									msg = new RequestExPurchaseLimitShopItemBuy(); // C_EX_PURCHASE_LIMIT_SHOP_ITEM_BUY
									break;
								case 0x164:
									msg = new RequestExPurchaseLimitShopHtmlOpen(); // C_EX_PURCHASE_LIMIT_SHOP_HTML_OPEN
									break;
								case 0x165:
									msg = new RequestExRequestClassChange(); // C_EX_REQUEST_CLASS_CHANGE
									break;
								case 0x166:
									msg = new RequestExRequestClassChangeVerifying(); // C_EX_REQUEST_CLASS_CHANGE_VERIFYING
									break;
								case 0x167:
									msg = new RequestExRequestTeleport(); // C_EX_REQUEST_TELEPORT
									break;
								case 0x168:
									// msg = new RequestExCostumeUseItem(); //C_EX_COSTUME_USE_ITEM
									break;
								case 0x169:
									// msg = new RequestExCostumeList(); //C_EX_COSTUME_LIST
									break;
								case 0x16A:
									// msg = new RequestExCostumeCollectionSkillActive();
									// //C_EX_COSTUME_COLLECTION_SKILL_ACTIVE
									break;
								case 0x16B:
									// msg = new RequestExCostumeEvolution(); //C_EX_COSTUME_EVOLUTION
									break;
								case 0x16C:
									// msg = new RequestExCostumeExtract(); //C_EX_COSTUME_EXTRACT
									break;
								case 0x16D:
									// msg = new RequestExCostumeLock(); //C_EX_COSTUME_LOCK
									break;
								case 0x16E:
									// msg = new RequestExCostumeChangeShortcut(); //C_EX_COSTUME_CHANGE_SHORTCUT
									break;
								case 0x16F:
									msg = new RequestExMagicLampGameInfo(); // C_EX_MAGICLAMP_GAME_INFO
									break;
								case 0x170:
									msg = new RequestExMagicLampGameStart(); // C_ EX_MAGICLAMP_GAME_START
									break;
								case 0x171:
									msg = new RequestExActivateAutoShortcut(); // C_EX_ACTIVATE_AUTO_SHORTCUT
									break;
								case 0x172:
									msg = new RequestExPremiumManagerLinkHtml(); // C_EX_PREMIUM_MANAGER_LINK_HTML
									break;
								case 0x173:
									msg = new RequestExPremiumManagerPassCmdToServer(); // C_EX_PREMIUM_MANAGER_PASS_CMD_TO_SERVER
									break;
								case 0x174:
									// C_EX_ACTIVATED_CURSED_TREASURE_BOX_LOCATION
									break;
								case 0x175:
									// C_EX_PAYBACK_LIST
									break;
								case 0x176:
									// C_EX_PAYBACK_GIVE_REWARD
									break;
								case 0x177:
									msg = new RequestExAutoplaySetting(); // C_EX_AUTOPLAY_SETTING
									break;
								case 0x178:
									msg = new RequestExOlympiadMatchMaking(); // C_EX_OLYMPIAD_MATCH_MAKING
									break;
								case 0x179:
									msg = new RequestExOlympiadMatchMakingCancel(); // C_EX_OLYMPIAD_MATCH_MAKING_CANCEL
									break;
								case 0x17A:
									msg = new RequestExFestivalBMInfo(); // C_EX_FESTIVAL_BM_INFO
									break;
								case 0x17B:
									msg = new RequestExFestivalBMGame(); // C_EX_FESTIVAL_BM_GAME
									break;
								case 0x17C:
									// C_EX_GACHA_SHOP_INFO
									break;
								case 0x17D:
									// C_EX_GACHA_SHOP_GACHA_GROUP
									break;
								case 0x17E:
									// C_EX_GACHA_SHOP_GACHA_ITEM
									break;
								case 0x17F:
									msg = new RequestExTimeRestrictFieldList(); // C_EX_TIME_RESTRICT_FIELD_LIST
									break;
								case 0x180:
									msg = new RequestExTimeRestrictFieldUserEnter(); // C_EX_TIME_RESTRICT_FIELD_USER_ENTER
									break;
								case 0x181:
									msg = new RequestRankingCharInfo();
									break;
								case 0x182:
									msg = new RequestExRankingCharHistory();
									break;
								case 0x183:
									msg = new RequestRankingCharRankers();
									break;
								case 0x184:
									msg = new RequestExRankingCharSpawnBuffzoneNpc();
									break;
								case 0x185:
									msg = new RequestExRankingCharBuffzoneNpcPosition();
									break;
								case 0x186:
									msg = new RequestExPledgeMercenaryRecruitInfoSet(); // C_EX_PLEDGE_MERCENARY_RECRUIT_INFO_SET
									break;
								case 0x187:
									msg = new RequestExMercenaryCastlewarCastleInfo(); // C_EX_MERCENARY_CASTLEWAR_CASTLE_INFO
									break;
								case 0x188:
									msg = new RequestExMercenaryCastlewarCastleSiegeInfo(); // C_EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_INFO
									break;
								case 0x189:
									msg = new RequestExMercenaryCastlewarCastleSiegeAttackerList(); // C_EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST
									break;
								case 0x18A:
									msg = new RequestExMercenaryCastlewarCastleSiegeDefenderList(); // C_EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_DEFENDER_LIST
									break;
								case 0x18B:
									msg = new RequestExPledgeMercenaryMemberList(); // C_EX_PLEDGE_MERCENARY_MEMBER_LIST
									break;
								case 0x18C:
									msg = new RequestExPledgeMercenaryMemberJoin(); // C_EX_PLEDGE_MERCENARY_MEMBER_JOIN
									break;
								case 0x18D:
									msg = new RequestExPvpbookList(); // C_EX_PVPBOOK_LIST
									break;
								case 0x18E:
									msg = new RequestExPvpbookKillerLocation(); // C_EX_PVPBOOK_KILLER_LOCATION
									break;
								case 0x18F:
									msg = new RequestExPvpbookTeleportToKiller(); // C_EX_PVPBOOK_TELEPORT_TO_KILLER
									break;
								case 0x190:
									// C_EX_LETTER_COLLECTOR_TAKE_REWARD
									break;
								case 0x191:
									msg = new RequestExSetStatusBonus(); // C_EX_SET_STATUS_BONUS
									break;
								case 0x192:
									msg = new RequestExResetStatusBonus(); // C_EX_RESET_STATUS_BONUS
									break;
								case 0x193:
									msg = new RequestOlympiadMyRankingInfo();// C_EX_OLYMPIAD_MY_RANKING_INFO
									break;
								case 0x194:
									msg = new RequestOlympiadRankingInfo();// C_EX_OLYMPIAD_RANKING_INFO
									break;
								case 0x195:
									msg = new RequestOlympiadHeroAndLegendInfo();// C_EX_OLYMPIAD_HERO_AND_LEGEND_INFO
									break;
								case 0x196:
									// C_EX_CASTLEWAR_OBSERVER_START
									break;
								case 0x197:
									msg = new RequestExRaidTeleportInfo(); // C_EX_RAID_TELEPORT_INFO
									break;
								case 0x198:
									msg = new RequestExTeleportToRaidPosition(); // C_EX_TELEPORT_TO_RAID_POSITION
									break;
								case 0x199:
									msg = new RequestExCraftExtract(); // C_EX_CRAFT_EXTRACT
									break;
								case 0x19A:
									msg = new RequestExCraftRandomInfo(); // C_EX_CRAFT_RANDOM_INFO
									break;
								case 0x19B:
									msg = new RequestExCraftRandomLockSlot(); // C_EX_CRAFT_RANDOM_LOCK_SLOT
									break;
								case 0x19C:
									msg = new RequestExCraftRandomRefresh(); // C_EX_CRAFT_RANDOM_REFRESH
									break;
								case 0x19D:
									msg = new RequestExCraftRandomMake(); // C_EX_CRAFT_RANDOM_MAKE
									break;
								case 0x19E:
									msg = new RequestExMultiSellList(); // C_EX_MULTI_SELL_LIST
									break;
								case 0x19F:
									// C_EX_SAVE_ITEM_ANNOUNCE_SETTING
									break;
								case 0x1A0:
									msg = new RequestExOlympiadUI(); // C_EX_OLYMPIAD_UI
									break;
								case 0x1A1:
									msg = new RequestExSharedPositionSharingUI(); // C_EX_SHARED_POSITION_SHARING_UI
									break;
								case 0x1A2:
									msg = new RequestExSharedPositionTeleportUI(); // C_EX_SHARED_POSITION_TELEPORT_UI
									break;
								case 0x1A3:
									msg = new RequestExSharedPositionTeleport(); // C_EX_SHARED_POSITION_TELEPORT
									break;
								case 0x1A4:
									// C_EX_AUTH_RECONNECT
									break;
								case 0x1A5:
									msg = new ExPetEquipItem();
									break;
								case 0x1A6:
									msg = new ExPetUnequipItem();
									break;
								case 0x1A7:
									// msg = new RequestExShowHomunculusInfo(); // C_EX_SHOW_HOMUNCULUS_INFO
									break;
								case 0x1A8:
									// msg = new RequestExHomunculusCreateStart();// C_EX_HOMUNCULUS_CREATE_START
									break;
								case 0x1A9:
									// msg = new RequestExHomunculusInsert();// C_EX_HOMUNCULUS_INSERT
									break;
								case 0x1AA:
									// msg = new RequestExHomunculusSummon();// C_EX_HOMUNCULUS_SUMMON
									break;
								case 0x1AB:
									// msg = new RequestExDeleteHomunculusData();// C_EX_DELETE_HOMUNCULUS_DATA
									break;
								case 0x1AC:
									// msg = new RequestExActivateHomunculus();// C_EX_REQUEST_ACTIVATE_HOMUNCULUS
									break;
								case 0x1AD:
									// msg = new RequestExHomunculusGetEnchantPoint();//
									// C_EX_HOMUNCULUS_GET_ENCHANT_POINT
									break;
								case 0x1AE:
									// msg = new RequestExHomunculusInitPoint();// C_EX_HOMUNCULUS_INIT_POINT
									break;
								case 0x1AF:
									msg = new ExEvolvePet(); // C_EX_EVOLVE_PET
									break;
								case 0x1B0:
									// msg = new RequestExEnchantHomunculusSkill();// C_EX_ENCHANT_HOMUNCULUS_SKILL
									break;
								case 0x1B1:
									// msg = new RequestExHomunculusEnchantExp();// C_EX_HOMUNCULUS_ENCHANT_EXP
									break;
								case 0x1B2:
									msg = new RequestExTeleportFavoritesList();// C_EX_TELEPORT_FAVORITES_LIST
									break;
								case 0x1B3:
									msg = new RequestExTeleportFavoritesUIToggle();// C_EX_TELEPORT_FAVORITES_UI_TOGGLE
									break;
								case 0x1B4:
									msg = new RequestExTeleportFavoritesAddDel();// C_EX_TELEPORT_FAVORITES_ADD_DEL
									break;
								case 0x1B5:
									// C_EX_ANTIBOT
									break;
								case 0x1B6:
									// C_EX_DPSVR
									break;
								case 0x1B7:
									// C_EX_TENPROTECT_DECRYPT_ERROR
									break;
								case 0x1B8:
									// C_EX_NET_LATENCY
									break;
								case 0x1B9:
									// C_EX_MABLE_GAME_OPEN
									break;
								case 0x1BA:
									// C_EX_MABLE_GAME_ROLL_DICE
									break;
								case 0x1BB:
									// C_EX_MABLE_GAME_POPUP_OK
									break;
								case 0x1BC:
									// C_EX_MABLE_GAME_RESET
									break;
								case 0x1BD:
									// C_EX_MABLE_GAME_CLOSE
									break;
								case 0x1BE:
									// C_EX_RETURN_TO_ORIGIN
									break;
								case 0x1BF:
									new RequestExPkPenaltyList(); // C_EX_PK_PENALTY_LIST
									break;
								case 0x1C0:
									msg = new RequestExPkPenaltyListOnlyLoc(); // C_EX_PK_PENALTY_LIST_ONLY_LOC
									break;
								case 0x1C1:
									msg = new RequestExBlessOptionPutItem(); // C_EX_BLESS_OPTION_PUT_ITEM
									break;
								case 0x1C2:
									msg = new RequestExBlessOptionEnchant(); // C_EX_BLESS_OPTION_ENCHANT
									break;
								case 0x1C3:
									msg = new RequestExBlessOptionCancel(); // C_EX_BLESS_OPTION_CANCEL
									break;
								case 0x1C4:
									msg = new RequestExPvpRankingMyInfo(); // C_EX_PVP_RANKING_MY_INFO
									break;
								case 0x1C5:
									msg = new RequestExPvpRankingList(); // C_EX_PVP_RANKING_LIST
									break;
								case 0x1C6:
									msg = new RequestExAcquirePetSkill(); // C_EX_ACQUIRE_PET_SKILL
									break;
								case 0x1C7:
									msg = new RequestExPledgeV3Info(); // C_EX_PLEDGE_V3_INFO
									break;
								case 0x1C8:
									msg = new RequestExPledgeEnemyInfoList(); // C_EX_PLEDGE_ENEMY_INFO_LIST
									break;
								case 0x1C9:
									msg = new RequestExPledgeEnemyRegister(); // C_EX_PLEDGE_ENEMY_REGISTER
									break;
								case 0x1CA:
									msg = new RequestExPledgeEnemyDelete(); // C_EX_PLEDGE_ENEMY_DELETE
									break;
								case 0x1CB:
									msg = new RequestExTryPetExtractSystem();
									break;
								case 0x1CC:
									msg = new RequestExPledgeV3SetAnnounce();
									break;
								case 0x1CD:
									// C_EX_RANKING_FESTIVAL_OPEN
									break;
								case 0x1CE:
									// C_EX_RANKING_FESTIVAL_BUY
									break;
								case 0x1CF:
									// C_EX_RANKING_FESTIVAL_BONUS
									break;
								case 0x1D0:
									// C_EX_RANKING_FESTIVAL_RANKING
									break;
								case 0x1D1:
									// C_EX_RANKING_FESTIVAL_MY_RECEIVED_BONUS
									break;
								case 0x1D2:
									// C_EX_RANKING_FESTIVAL_REWARD
									break;
								case 0x1D3:
									// C_EX_TIMER_CHECK
									break;
								case 0x1D4:
									msg = new RequestExSteadyBoxLoad(); // C_EX_STEADY_BOX_LOAD
									break;
								case 0x1D5:
									msg = new RequestExSteadyOpenSlot(); // C_EX_STEADY_OPEN_SLOT
									break;
								case 0x1D6:
									msg = new RequestExSteadyOpenBox(); // C_EX_STEADY_OPEN_BOX
									break;
								case 0x1D7:
									msg = new RequestExSteadyGetReward(); // C_EX_STEADY_GET_REWARD
									break;
								case 0x1D8:
									// C_EX_PET_RANKING_MY_INFO
									break;
								case 0x1D9:
									// C_EX_PET_RANKING_LIST
									break;
								case 0x1DA:
									msg = new RequestExCollectionOpenUI(); // C_EX_COLLECTION_OPEN_UI
									break;
								case 0x1DB:
									msg = new RequestExCollectionCloseUI(); // C_EX_COLLECTION_CLOSE_UI
									break;
								case 0x1DC:
									msg = new RequestExCollectionList(); // C_EX_COLLECTION_LIST
									break;
								case 0x1DD:
									msg = new RequestExCollectionUpdateFavorite(); // C_EX_COLLECTION_UPDATE_FAVORITE
									break;
								case 0x1DE:
									msg = new RequestExCollectionFavoriteList(); // C_EX_COLLECTION_FAVORITE_LIST
									break;
								case 0x1DF:
									msg = new RequestExCollectionSummary(); // C_EX_COLLECTION_SUMMARY
									break;
								case 0x1E0:
									msg = new RequestExCollectionRegister(); // C_EX_COLLECTION_REGISTER
									break;
								case 0x1E1:
									// C_EX_COLLECTION_RECEIVE_REWARD
									break;
								case 0x1E2:
									msg = new RequestExPvpBookShareRevengeList(); // C_EX_PVPBOOK_SHARE_REVENGE_LIST
									break;
								case 0x1E3:
									msg = new RequestExPvpBookShareRevengeReqShareRevengeInfo(); // C_EX_PVPBOOK_SHARE_REVENGE_REQ_SHARE_REVENGEINFO
									break;
								case 0x1E4:
									msg = new RequestExPvpBookShareRevengeKillerLocation(); // C_EX_PVPBOOK_SHARE_REVENGE_KILLER_LOCATION
									break;
								case 0x1E5:
									msg = new RequestExPvpBookShareRevengeTeleportToKiller(); // C_EX_PVPBOOK_SHARE_REVENGE_TELEPORT_TO_KILLER
									break;
								case 0x1E6:
									msg = new RequestExPvpBookShareRevengeSharedTeleportToKiller(); // C_EX_PVPBOOK_SHARE_REVENGE_SHARED_TELEPORT_TO_KILLER
									break;
								case 0x1E7:
									msg = new RequestExPenaltyItemList(); // C_EX_PENALTY_ITEM_LIST
									break;
								case 0x1E8:
									msg = new RequestExPenaltyItemRestore(); // C_EX_PENALTY_ITEM_RESTORE
									break;
								case 0x1E9:
									msg = new RequestExUserWatcherTargetList(); // C_EX_USER_WATCHER_TARGET_LIST
									break;
								case 0x1EA:
									msg = new RequestExUserWatcherAdd(); // C_EX_USER_WATCHER_ADD
									break;
								case 0x1EB:
									msg = new RequestExUserWatcherDelete(); // C_EX_USER_WATCHER_DELETE
									break;
								case 0x1EC:
									// C_EX_HOMUNCULUS_ACTIVATE_SLOT
									break;
								case 0x1ED:
									// C_EX_SUMMON_HOMUNCULUS_COUPON
									break;
								case 0x1EE:
									msg = new RequestExSubjugationList(); // C_EX_SUBJUGATION_LIST
									break;
								case 0x1EF:
									msg = new RequestExSubjugationRanking(); // C_EX_SUBJUGATION_RANKING
									break;
								case 0x1F0:
									msg = new RequestExSubjugationGachaUI(); // C_EX_SUBJUGATION_GACHA_UI
									break;
								case 0x1F1:
									msg = new RequestExSubjugationGacha(); // C_EX_SUBJUGATION_GACHA
									break;
								case 0x1F2:
									msg = new RequestExPledgeDonationInfo(); // C_EX_PLEDGE_DONATION_INFO
									break;
								case 0x1F3:
									msg = new RequestExPledgeDonationRequest(); // C_EX_PLEDGE_DONATION_REQUEST
									break;
								case 0x1F4:
									msg = new RequestExPledgeContributionList(); // C_EX_PLEDGE_CONTRIBUTION_LIST
									break;
								case 0x1F5:
									msg = new RequestExPledgeRankingMyInfo(); // C_EX_PLEDGE_RANKING_MY_INFO
									break;
								case 0x1F6:
									msg = new RequestExPledgeRankingList(); // C_EX_PLEDGE_RANKING_LIST
									break;
								case 0x1F7:
									msg = new RequestExItemRestoreList(); // C_EX_ITEM_RESTORE_LIST
									break;
								case 0x1F8:
									msg = new RequestExItemRestore(); // C_EX_ITEM_RESTORE
									break;
								case 0x1F9:
									// C_EX_DETHRONE_INFO
									break;
								case 0x1FA:
									// C_EX_DETHRONE_RANKING_INFO
									break;
								case 0x1FB:
									// C_EX_DETHRONE_SERVER_INFO
									break;
								case 0x1FC:
									// C_EX_DETHRONE_DISTRICT_OCCUPATION_INFO
									break;
								case 0x1FD:
									// C_EX_DETHRONE_DAILY_MISSION_INFO
									break;
								case 0x1FE:
									// C_EX_DETHRONE_DAILY_MISSION_GET_REWARD
									break;
								case 0x1FF:
									// C_EX_DETHRONE_PREV_SEASON_INFO
									break;
								case 0x200:
									// C_EX_DETHRONE_GET_REWARD
									break;
								case 0x201:
									// C_EX_DETHRONE_ENTER
									break;
								case 0x202:
									// C_EX_DETHRONE_LEAVE
									break;
								case 0x203:
									// C_EX_DETHRONE_CHECK_NAME
									break;
								case 0x204:
									// C_EX_DETHRONE_CHANGE_NAME
									break;
								case 0x205:
									// C_EX_DETHRONE_CONNECT_CASTLE
									break;
								case 0x206:
									// C_EX_DETHRONE_DISCONNECT_CASTLE
									break;
								case 0x207:
									msg = new RequestChangeNicknameColorIcon(); // C_EX_CHANGE_NICKNAME_COLOR_ICON
									break;
								case 0x208:
									// C_EX_WORLDCASTLEWAR_MOVE_TO_HOST
									break;
								case 0x209:
									// C_EX_WORLDCASTLEWAR_RETURN_TO_ORIGIN_PEER
									break;
								case 0x20A:
									// C_EX_WORLDCASTLEWAR_CASTLE_INFO
									break;
								case 0x20B:
									// C_EX_WORLDCASTLEWAR_CASTLE_SIEGE_INFO
									break;
								case 0x20C:
									// C_EX_WORLDCASTLEWAR_CASTLE_SIEGE_JOIN
									break;
								case 0x20D:
									// C_EX_WORLDCASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST
									break;
								case 0x20E:
									// C_EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_RECRUIT_INFO_SET
									break;
								case 0x20F:
									// C_EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_MEMBER_LIST
									break;
								case 0x210:
									// C_EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_MEMBER_JOIN
									break;
								case 0x211:
									// C_EX_WORLDCASTLEWAR_TELEPORT
									break;
								case 0x212:
									// C_EX_WORLDCASTLEWAR_OBSERVER_START
									break;
								case 0x213:
									msg = new RequestExPrivateStoreSearchList(); // C_EX_PRIVATE_STORE_SEARCH_LIST
									break;
								case 0x214:
									msg = new RequestExPrivateStoreSearchStatistics(); // C_EX_PRIVATE_STORE_SEARCH_STATISTICS
									break;
								case 0x215:
									// C_EX_WORLDCASTLEWAR_HOST_CASTLE_SIEGE_RANKING_INFO
									break;
								case 0x216:
									// C_EX_WORLDCASTLEWAR_CASTLE_SIEGE_RANKING_INFO
									break;
								case 0x217:
									// C_EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_HUD_INFO
									break;
								case 0x218:
									msg = new RequestExNewHennaList(); // C_EX_NEW_HENNA_LIST
									break;
								case 0x219:
									msg = new RequestExNewHennaEquip(); // C_EX_NEW_HENNA_EQUIP
									break;
								case 0x21A:
									msg = new RequestExNewHennaUnequip(); // C_EX_NEW_HENNA_UNEQUIP
									break;
								case 0x21B:
									msg = new RequestExNewHennaPotenSelect(); // C_EX_NEW_HENNA_POTEN_SELECT
									break;
								case 0x21C:
									msg = new RequestExNewHennaPotenEnchant(); // C_EX_NEW_HENNA_POTEN_ENCHANT
									break;
								case 0x21D:
									msg = new RequestExNewHennaCompose(); // C_EX_NEW_HENNA_COMPOSE
									break;
								case 0x21E:
									msg = new RequestExRequestInviteParty(); // C_EX_REQUEST_INVITE_PARTY
									break;
								case 0x21F:
									// C_EX_ITEM_USABLE_LIST
									break;
								case 0x220:
									// C_EX_PACKETREADCOUNTPERSECOND
									break;
								case 0x221:
									// C_EX_SELECT_GLOBAL_EVENT_UI
									break;
								case 0x222:
									// C_EX_L2PASS_INFO
									break;
								case 0x223:
									// C_EX_L2PASS_REQUEST_REWARD
									break;
								case 0x224:
									// C_EX_L2PASS_REQUEST_REWARD_ALL
									break;
								case 0x225:
									// C_EX_L2PASS_BUY_PREMIUM
									break;
								case 0x226:
									// C_EX_SAYHAS_SUPPORT_TOGGLE
									break;
								case 0x227:
									msg = new RequestExEnchantFailRewardInfo(); // C_EX_REQ_ENCHANT_FAIL_REWARD_INFO
									break;
								case 0x228:
									// C_EX_SET_ENCHANT_CHALLENGE_POINT
									break;
								case 0x229:
									// C_EX_RESET_ENCHANT_CHALLENGE_POINT
									break;
								case 0x22A:
									msg = new RequestExViewEnchantResult(); // C_EX_REQ_VIEW_ENCHANT_RESULT
									break;
								case 0x22B:
									msg = new RequestExStartMultiEnchantScroll(); // C_EX_REQ_START_MULTI_ENCHANT_SCROLL
									break;
								case 0x22C:
									msg = new RequestExViewMultiEnchantResult(); // C_EX_REQ_VIEW_MULTI_ENCHANT_RESULT
									break;
								case 0x22D:
									msg = new RequestExFinishMultiEnchantScroll(); // C_EX_REQ_FINISH_MULTI_ENCHANT_SCROLL
									break;
								case 0x22E:
									// C_EX_REQ_CHANGE_MULTI_ENCHANT_SCROLL
									break;
								case 0x22F:
									msg = new RequestExSetMultiEnchantItemList(); // C_EX_REQ_SET_MULTI_ENCHANT_ITEM_LIST
									break;
								case 0x230:
									msg = new RequestExMultiEnchantItemList(); // C_EX_REQ_MULTI_ENCHANT_ITEM_LIST
									break;
								case 0x231:
									// C_EX_WORLDCASTLEWAR_SUPPORT_PLEDGE_FLAG_SET
									break;
								case 0x232:
									// C_EX_WORLDCASTLEWAR_SUPPORT_PLEDGE_INFO_SET
									break;
								case 0x233:
									// C_EX_REQ_HOMUNCULUS_PROB_LIST
									break;
								case 0x234:
									// C_EX_WORLDCASTLEWAR_HOST_CASTLE_SIEGE_ALL_RANKING_INFO
									break;
								case 0x235:
									// C_EX_WORLDCASTLEWAR_CASTLE_SIEGE_ALL_RANKING_INFO"
									break;
								case 0x236:
									msg = new RequestExMissionLevelRewardList(); // C_EX_MISSION_LEVEL_REWARD_LIST
									break;
								case 0x237:
									msg = new RequestExMissionLevelReceiveReward(); // C_EX_MISSION_LEVEL_RECEIVE_REWARD
									break;
								case 0x238:
									// C_EX_BALROGWAR_TELEPORT
									break;
								case 0x239:
									// C_EX_BALROGWAR_SHOW_UI
									break;
								case 0x23A:
									// C_EX_BALROGWAR_SHOW_RANKING
									break;
								case 0x23B:
									// C_EX_BALROGWAR_GET_REWARD
									break;
								case 0x23C:
									// C_EX_USER_RESTART_LOCKER_UPDATE
									break;
								case 0x23D:
									// C_EX_WORLD_EXCHANGE_ITEM_LIST
									break;
								case 0x23E:
									// C_EX_WORLD_EXCHANGE_REGI_ITEM
									break;
								case 0x23F:
									// C_EX_WORLD_EXCHANGE_BUY_ITEM
									break;
								case 0x240:
									// C_EX_WORLD_EXCHANGE_SETTLE_LIST
									break;
								case 0x241:
									// C_EX_WORLD_EXCHANGE_SETTLE_RECV_RESULT
									break;
								case 0x242:
									msg = new RequestExReadyItemAutoPeel(); // C_EX_READY_ITEM_AUTO_PEEL
									break;
								case 0x243:
									msg = new RequestExItemAutoPeel(); // C_EX_REQUEST_ITEM_AUTO_PEEL
									break;
								case 0x244:
									msg = new RequestExStopItemAutoPeel(); // C_EX_STOP_ITEM_AUTO_PEEL
									break;
								case 0x245:
									// C_EX_VARIATION_OPEN_UI
									break;
								case 0x246:
									// C_EX_VARIATION_CLOSE_UI
									break;
								case 0x247:
									// C_EX_APPLY_VARIATION_OPTION
									break;
								case 0x248:
									msg = new RequestExBRVersion(); // C_EX_BR_VERSION // Auth, unreal here
									break;
								case 0x249:
									// C_EX_MAX
									break;
								default:
									client.onUnknownPacket();
									_log.warn("Unknown client packet! State: IN_GAME, packet ID: " + Integer.toHexString(id).toUpperCase() + ":" + Integer.toHexString(id3).toUpperCase());
									break;
							}
							break;

						default:
						{
							client.onUnknownPacket();
							break;
						}
					}
					break;
			}
		}
		catch (final BufferUnderflowException e)
		{
			client.onPacketReadFail();
		}
		_sentPackets.add(msg);
		return msg;
	}

	@Override
	public GameClient create(MMOConnection<GameClient> con)
	{
		return new GameClient(con);
	}

	@Override
	public void execute(Runnable r)
	{
		ThreadPoolManager.getInstance().execute(r);
	}
}