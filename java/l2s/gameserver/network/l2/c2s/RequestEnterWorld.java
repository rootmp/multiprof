package l2s.gameserver.network.l2.c2s;
import java.util.List;

import org.napile.primitive.pair.IntObjectPair;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.dao.MailDAO;
import l2s.gameserver.data.clientDat.CollectionsData;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.instancemanager.CoupleManager;
import l2s.gameserver.instancemanager.OfflineBufferManager;
import l2s.gameserver.instancemanager.PetitionManager;
import l2s.gameserver.instancemanager.PlayerMessageStack;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.listener.actor.player.OnAnswerListener;
import l2s.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import l2s.gameserver.listener.hooks.ListenerHook;
import l2s.gameserver.listener.hooks.ListenerHookType;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.World;
import l2s.gameserver.model.actor.CreatureSkillCast;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.actor.instances.player.ShortCut;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.entity.residence.Fortress;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.network.authcomm.gs2as.ChangeAllowedHwid;
import l2s.gameserver.network.authcomm.gs2as.ChangeAllowedIp;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.network.l2.components.StatusUpdate;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ChangeWaitTypePacket;
import l2s.gameserver.network.l2.s2c.ConfirmDlgPacket;
import l2s.gameserver.network.l2.s2c.DiePacket;
import l2s.gameserver.network.l2.s2c.EtcStatusUpdatePacket;
import l2s.gameserver.network.l2.s2c.ExActivateAutoShortcut;
import l2s.gameserver.network.l2.s2c.ExAdenFortressSiegeHUDInfo;
import l2s.gameserver.network.l2.s2c.ExAdenaInvenCount;
import l2s.gameserver.network.l2.s2c.ExBR_NewIConCashBtnWnd;
import l2s.gameserver.network.l2.s2c.ExBR_PremiumStatePacket;
import l2s.gameserver.network.l2.s2c.ExBasicActionList;
import l2s.gameserver.network.l2.s2c.ExBloodyCoinCount;
import l2s.gameserver.network.l2.s2c.ExCastleState;
import l2s.gameserver.network.l2.s2c.ExChangeMPCost;
import l2s.gameserver.network.l2.s2c.ExConnectedTimeAndGettableReward;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritInfo;
import l2s.gameserver.network.l2.s2c.ExEnterWorldPacket;
import l2s.gameserver.network.l2.s2c.ExGetBookMarkInfoPacket;
import l2s.gameserver.network.l2.s2c.ExInitGlobalEventUI;
import l2s.gameserver.network.l2.s2c.ExLightingCandleEvent;
import l2s.gameserver.network.l2.s2c.ExMercenaryCastlewarCastleSiegeHudInfo;
import l2s.gameserver.network.l2.s2c.ExNoticePostArrived;
import l2s.gameserver.network.l2.s2c.ExNotifyPremiumItem;
import l2s.gameserver.network.l2.s2c.ExOlympiadInfo;
import l2s.gameserver.network.l2.s2c.ExOneDayReceiveRewardList;
import l2s.gameserver.network.l2.s2c.ExOpenMPCCPacket;
import l2s.gameserver.network.l2.s2c.ExPCCafePointInfoPacket;
import l2s.gameserver.network.l2.s2c.ExPeriodicHenna;
import l2s.gameserver.network.l2.s2c.ExPledgeCount;
import l2s.gameserver.network.l2.s2c.ExReceiveShowPostFriend;
import l2s.gameserver.network.l2.s2c.ExSetCompassZoneCode;
import l2s.gameserver.network.l2.s2c.ExStorageMaxCountPacket;
import l2s.gameserver.network.l2.s2c.ExUnReadMailCount;
import l2s.gameserver.network.l2.s2c.ExUserInfoAbnormalVisualEffect;
import l2s.gameserver.network.l2.s2c.ExUserInfoCubic;
import l2s.gameserver.network.l2.s2c.ExUserInfoEquipSlot;
import l2s.gameserver.network.l2.s2c.ExUserInfoInvenWeight;
import l2s.gameserver.network.l2.s2c.ExUserViewInfoParameter;
import l2s.gameserver.network.l2.s2c.ExVitalityEffectInfo;
import l2s.gameserver.network.l2.s2c.ExWorldChatCnt;
import l2s.gameserver.network.l2.s2c.HennaInfoPacket;
import l2s.gameserver.network.l2.s2c.MagicAndSkillList;
import l2s.gameserver.network.l2.s2c.MagicSkillLaunchedPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.MyPetSummonInfoPacket;
import l2s.gameserver.network.l2.s2c.PartySmallWindowAllPacket;
import l2s.gameserver.network.l2.s2c.PartySpelledPacket;
import l2s.gameserver.network.l2.s2c.PledgeSkillListPacket;
import l2s.gameserver.network.l2.s2c.QuestListPacket;
import l2s.gameserver.network.l2.s2c.ReciveVipInfo;
import l2s.gameserver.network.l2.s2c.RelationChangedPacket;
import l2s.gameserver.network.l2.s2c.RidePacket;
import l2s.gameserver.network.l2.s2c.ShortCutInitPacket;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket.StatusType;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket.UpdateType;
import l2s.gameserver.network.l2.s2c.UserInfo;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionActiveEvent;
import l2s.gameserver.network.l2.s2c.enchant.ExEnchantChallengePointInfo;
import l2s.gameserver.network.l2.s2c.itemrestore.ExPenaltyItemInfo;
import l2s.gameserver.network.l2.s2c.magiclamp.ExMagicLampExpInfo;
import l2s.gameserver.network.l2.s2c.pets.ExPetSkillList;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeCoinInfo;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftInfo;
import l2s.gameserver.network.l2.s2c.steadybox.ExSteadyBoxUIInit;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationSidebar;
import l2s.gameserver.network.l2.s2c.updatetype.NpcInfoType;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.AbnormalEffect;
import l2s.gameserver.skills.enums.SkillCastingType;
import l2s.gameserver.skills.enums.SkillMagicType;
import l2s.gameserver.stats.triggers.TriggerType;
import l2s.gameserver.utils.GameStats;
import l2s.gameserver.utils.HtmlUtils;
import l2s.gameserver.utils.TradeHelper;

public class RequestEnterWorld implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		// packet.readS(); - клиент всегда отправляет строку "narcasse"
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();

		if (activeChar == null)
		{
			client.closeNow();
			return;
		}

		GameStats.incrementPlayerEnterGame();

		onEnterWorld(activeChar);
	}

	public static void onEnterWorld(Player activeChar)
	{
		boolean first = activeChar.entering;

		activeChar.sendPacket(ExLightingCandleEvent.DISABLED);
		activeChar.sendPacket(new ExEnterWorldPacket());
		if (Config.EX_USE_TO_DO_LIST)
		{
			activeChar.sendPacket(new ExConnectedTimeAndGettableReward(activeChar));
			activeChar.sendPacket(new ExOneDayReceiveRewardList(activeChar));
		}
		activeChar.sendPacket(new ExPeriodicHenna(activeChar));
		activeChar.sendPacket(new HennaInfoPacket(activeChar));

		List<Castle> castleList = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		for (Castle c : castleList)
		{
			activeChar.sendPacket(new ExCastleState(c));
		}
		activeChar.sendSkillList();
		activeChar.sendPacket(new EtcStatusUpdatePacket(activeChar));

		activeChar.sendPacket(new UserInfo(activeChar));
		activeChar.sendPacket(new ExUserInfoInvenWeight(activeChar));
		activeChar.sendPacket(new ExUserInfoEquipSlot(activeChar));
		activeChar.sendPacket(new ExUserInfoCubic(activeChar));
		activeChar.sendPacket(new ExUserInfoAbnormalVisualEffect(activeChar));

		activeChar.sendPacket(SystemMsg.WELCOME_TO_THE_WORLD_OF_LINEAGE_II);

		double mpCostDiff = activeChar.getMPCostDiff(SkillMagicType.PHYSIC);
		if (mpCostDiff != 0)
		{
			activeChar.sendPacket(new ExChangeMPCost(SkillMagicType.PHYSIC, mpCostDiff));
		}
		mpCostDiff = activeChar.getMPCostDiff(SkillMagicType.MAGIC);
		if (mpCostDiff != 0)
		{
			activeChar.sendPacket(new ExChangeMPCost(SkillMagicType.MAGIC, mpCostDiff));
		}
		mpCostDiff = activeChar.getMPCostDiff(SkillMagicType.MUSIC);
		if (mpCostDiff != 0)
		{
			activeChar.sendPacket(new ExChangeMPCost(SkillMagicType.MUSIC, mpCostDiff));
		}
		activeChar.sendPacket(new QuestListPacket(activeChar));
		activeChar.initActiveAutoShots();
		activeChar.sendPacket(new ExGetBookMarkInfoPacket(activeChar));

		activeChar.sendItemList(false);
		activeChar.sendPacket(new ExAdenaInvenCount(activeChar));
		activeChar.sendPacket(new ExBloodyCoinCount(activeChar));
		activeChar.sendPacket(new ExPledgeCoinInfo(activeChar));
		activeChar.sendPacket(new ShortCutInitPacket(activeChar));
		activeChar.sendPacket(new ExBasicActionList(activeChar));

		activeChar.getMacroses().sendMacroses();

		Announcements.getInstance().showAnnouncements(activeChar);

		if (first)
		{
			activeChar.setOnlineStatus(true);
			if (activeChar.getPlayerAccess().GodMode && !Config.SHOW_GM_LOGIN && !Config.EVERYBODY_HAS_ADMIN_RIGHTS)
			{
				activeChar.setGMInvisible(true);
				activeChar.startAbnormalEffect(AbnormalEffect.STEALTH);
			}

			activeChar.setNonAggroTime(Long.MAX_VALUE);
			activeChar.setNonPvpTime(System.currentTimeMillis() + Config.NONPVP_TIME_ONTELEPORT);

			if (activeChar.isInBuffStore())
			{
				activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
			}
			else if (activeChar.isInStoreMode())
			{
				if (!TradeHelper.validateStore(activeChar))
				{
					activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
					activeChar.storePrivateStore();
				}
			}

			activeChar.setRunning();
			activeChar.standUp();
			activeChar.spawnMe();
			activeChar.startTimers();
		}

		activeChar.sendPacket(new ExVitalityEffectInfo(activeChar));
		activeChar.sendPacket(new ExBR_PremiumStatePacket(activeChar, activeChar.hasPremiumAccount()));

		activeChar.sendPacket(new ExSetCompassZoneCode(activeChar));
		// TODO: Исправить посылаемые данные.
		activeChar.sendPacket(new MagicAndSkillList(activeChar, 3503292, 730502));
		activeChar.sendPacket(new ExStorageMaxCountPacket(activeChar));
		activeChar.getAttendanceRewards().onEnterWorld();
		activeChar.sendPacket(new ExReceiveShowPostFriend(activeChar));

		if (Config.ALLOW_WORLD_CHAT)
		{
			activeChar.sendPacket(new ExWorldChatCnt(activeChar));
		}
		if (Config.EX_USE_PRIME_SHOP)
		{
			activeChar.sendPacket(new ExBR_NewIConCashBtnWnd(activeChar));
			activeChar.sendPacket(new ReciveVipInfo(activeChar));
		}

		activeChar.sendPacket(new ExElementalSpiritInfo(activeChar, 0));

		if (Config.RANDOM_CRAFT_SYSTEM_ENABLED)
		{
			activeChar.sendPacket(new ExCraftInfo(activeChar));
		}
		checkNewMail(activeChar);

		if (first)
		{
			activeChar.getListeners().onEnter();
			RankManager.getInstance().onPlayerEnter(activeChar);
		}

		activeChar.checkAndDeleteOlympiadItems();

		if (activeChar.getClan() != null)
		{
			activeChar.getClan().loginClanCond(activeChar, true);
			activeChar.sendPacket(activeChar.getClan().listAll());
			activeChar.sendPacket(new PledgeSkillListPacket(activeChar.getClan()));
		}
		else
		{
			activeChar.sendPacket(new ExPledgeCount(0));
		}
		// engage and notify Partner
		if (first && Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance().engage(activeChar);
			CoupleManager.getInstance().notifyPartner(activeChar);
		}

		if (first)
		{
			activeChar.getFriendList().notifyFriends(true);
			activeChar.getSpectatingList().notifySpectatings(true);
		}

		activeChar.checkHpMessages(activeChar.getMaxHp(), activeChar.getCurrentHp());
		activeChar.checkDayNightMessages();

		if (Config.SHOW_HTML_WELCOME)
		{
			String html = HtmCache.getInstance().getHtml("welcome.htm", activeChar);
			HtmlMessage msg = new HtmlMessage(5);
			msg.setHtml(HtmlUtils.bbParse(html));
			activeChar.sendPacket(msg);
		}

		if (Config.PETITIONING_ALLOWED)
		{
			PetitionManager.getInstance().checkPetitionMessages(activeChar);
		}
		if (!first)
		{
			CreatureSkillCast skillCast = activeChar.getSkillCast(SkillCastingType.NORMAL);
			if (skillCast.isCastingNow())
			{
				Creature castingTarget = skillCast.getTarget();
				SkillEntry castingSkillEntry = skillCast.getSkillEntry();
				long animationEndTime = skillCast.getAnimationEndTime();
				if (castingSkillEntry != null && !castingSkillEntry.getTemplate().isNotBroadcastable() && castingTarget != null && castingTarget.isCreature() && animationEndTime > 0)
				{
					activeChar.sendPacket(new MagicSkillUse(activeChar, castingTarget, castingSkillEntry.getId(), castingSkillEntry.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0, SkillCastingType.NORMAL));
				}
			}

			skillCast = activeChar.getSkillCast(SkillCastingType.NORMAL_SECOND);
			if (skillCast.isCastingNow())
			{
				Creature castingTarget = skillCast.getTarget();
				SkillEntry castingSkillEntry = skillCast.getSkillEntry();
				long animationEndTime = skillCast.getAnimationEndTime();
				if (castingSkillEntry != null && !castingSkillEntry.getTemplate().isNotBroadcastable() && castingTarget != null && castingTarget.isCreature() && animationEndTime > 0)
				{
					activeChar.sendPacket(new MagicSkillUse(activeChar, castingTarget, castingSkillEntry.getId(), castingSkillEntry.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0, SkillCastingType.NORMAL_SECOND));
				}
			}

			if (activeChar.isInBoat())
			{
				activeChar.sendPacket(activeChar.getBoat().getOnPacket(activeChar, activeChar.getInBoatPosition()));
			}
			if (activeChar.getMovement().isMoving() || activeChar.getMovement().isFollow())
			{
				activeChar.sendPacket(activeChar.movePacket());
			}
			if (activeChar.getMountNpcId() != 0)
			{
				activeChar.sendPacket(new RidePacket(activeChar));
			}
			if (activeChar.isFishing())
			{
				activeChar.getFishing().stop();
			}
		}

		activeChar.entering = false;

		if (activeChar.isSitting())
		{
			activeChar.sendPacket(new ChangeWaitTypePacket(activeChar, ChangeWaitTypePacket.WT_SITTING));
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(activeChar.getPrivateStoreMsgPacket(activeChar));
		}
		activeChar.unsetVar("offline");
		activeChar.unsetVar("offlinebuff");
		activeChar.unsetVar("offlinebuff_price");
		activeChar.unsetVar("offlinebuff_skills");
		activeChar.unsetVar("offlinebuff_title");

		OfflineBufferManager.getInstance().getBuffStores().remove(activeChar.getObjectId());

		// на всякий случай
		activeChar.sendActionFailed();

		if (first && activeChar.isGM() && Config.SAVE_GM_EFFECTS && activeChar.getPlayerAccess().CanUseGMCommand)
		{
			// silence
			if (activeChar.getVarBoolean("gm_silence"))
			{
				activeChar.setMessageRefusal(true);
				activeChar.sendPacket(SystemMsg.MESSAGE_REFUSAL_MODE);
			}
			// invul
			if (activeChar.getVarBoolean("gm_invul"))
			{
				activeChar.getFlags().getInvulnerable().start();
				activeChar.getFlags().getDebuffImmunity().start();
				activeChar.startAbnormalEffect(AbnormalEffect.INVINCIBILITY);
				activeChar.sendMessage(activeChar.getName() + " is now immortal.");
			}
			// undying
			if (activeChar.getVarBoolean("gm_undying"))
			{
				activeChar.setGMUndying(true);
				activeChar.sendMessage("Undying state has been enabled.");
			}
			// gmspeed
			activeChar.setGmSpeed(activeChar.getVarInt("gm_gmspeed", 0));
		}

		PlayerMessageStack.getInstance().CheckMessages(activeChar);

		IntObjectPair<OnAnswerListener> entry = activeChar.getAskListener(false);
		if (entry != null && entry.getValue() instanceof ReviveAnswerListener)
		{
			activeChar.sendPacket(new ConfirmDlgPacket(SystemMsg.C1_IS_MAKING_AN_ATTEMPT_TO_RESURRECT_YOU_IF_YOU_CHOOSE_THIS_PATH_S2_EXPERIENCE_WILL_BE_RETURNED_FOR_YOU, 0).addString("Other player").addString("some"));
		}
		if (!first)
		{
			// Персонаж вылетел во время просмотра
			if (activeChar.isInObserverMode())
			{
				if (activeChar.getObserverMode() == Player.OBSERVER_LEAVING)
				{
					activeChar.returnFromObserverMode();
				}
				else
				{
					activeChar.leaveObserverMode();
				}
			}
			else if (activeChar.isVisible())
			{
				World.showObjectsToPlayer(activeChar);
			}

			final List<Servitor> servitors = activeChar.getServitors();
			for (Servitor servitor : servitors)
			{
				activeChar.sendPacket(new MyPetSummonInfoPacket(servitor));
				if (servitor.isPet())
				{
					activeChar.sendPacket(new ExPetSkillList((PetInstance) servitor, true));
				}
			}
			if (activeChar.isInParty())
			{
				Party party = activeChar.getParty();
				Player leader = party.getPartyLeader();
				if (leader != null) // некрасиво, но иначе NPE.
				{
					// sends new member party window for all members
					// we do all actions before adding member to a list, this speeds things up a
					// little
					activeChar.sendPacket(new PartySmallWindowAllPacket(party, leader, activeChar));

					RelationChangedPacket rcp = new RelationChangedPacket();
					for (Player member : party.getPartyMembers())
					{
						if (member != activeChar)
						{
							activeChar.sendPacket(new PartySpelledPacket(member, true));
							for (Servitor servitor : servitors)
							{
								activeChar.sendPacket(new PartySpelledPacket(servitor, true));
							}
							rcp.add(member, activeChar);
							for (Servitor servitor : member.getServitors())
							{
								rcp.add(servitor, activeChar);
							}
							for (Servitor servitor : servitors)
							{
								servitor.broadcastCharInfoImpl(activeChar, NpcInfoType.VALUES);
							}
						}
					}

					activeChar.sendPacket(rcp);
					// Если партия уже в СС, то вновь прибывшем посылаем пакет открытия окна СС
					if (party.isInCommandChannel())
					{
						activeChar.sendPacket(ExOpenMPCCPacket.STATIC);
					}
				}
			}

			activeChar.sendPacket(new ExEnchantChallengePointInfo());
			activeChar.sendActiveAutoShots();
			for (Abnormal e : activeChar.getAbnormalList())
			{
				if (e.getSkill().isToggle() && !e.getSkill().isNotBroadcastable())
				{
					activeChar.sendPacket(new MagicSkillLaunchedPacket(activeChar.getObjectId(), e.getSkill().getId(), e.getSkill().getLevel(),0, activeChar, SkillCastingType.NORMAL));
				}
			}

			activeChar.broadcastCharInfo();
		}

		if (activeChar.getPremiumItemList().size() > 0)
		{
			activeChar.sendPacket(ExNotifyPremiumItem.STATIC);
		}

		CollectionsData.getInstance().sendExCollectionInfo(activeChar);
		
		activeChar.sendPacket(new ExCollectionActiveEvent());
		activeChar.sendPacket(new ExOlympiadInfo(activeChar));
		activeChar.sendPacket(new ExSteadyBoxUIInit(activeChar));
		activeChar.sendPacket(new ExInitGlobalEventUI());
		activeChar.sendPacket(new ExPledgeCoinInfo(activeChar));

		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, 3);
		if ((castle != null) && (castle.getSiegeEvent().hasState(SiegeEvent.REGISTRATION_STATE) || castle.getSiegeEvent().hasState(SiegeEvent.PROGRESS_STATE)))
		{
			activeChar.sendPacket(new ExMercenaryCastlewarCastleSiegeHudInfo(castle.getSiegeEvent()));
		}
		Fortress fortress = ResidenceHolder.getInstance().getResidence(Fortress.class, 117);
		if ((fortress != null) && (fortress.getSiegeEvent().hasState(SiegeEvent.REGISTRATION_STATE) || fortress.getSiegeEvent().hasState(SiegeEvent.PROGRESS_STATE)))
		{
			activeChar.sendPacket(new ExAdenFortressSiegeHUDInfo(fortress.getSiegeEvent()));
		}

		if (Config.MAGIC_LAMP_ENABLED)
		{
			activeChar.sendPacket(new ExMagicLampExpInfo(activeChar));
		}
		if (activeChar.isDead())
		{
			activeChar.sendPacket(new DiePacket(activeChar));
		}

		activeChar.sendPacket(new ExPenaltyItemInfo(activeChar));
		activeChar.setKarma(activeChar.getKarma());

		activeChar.updateAbnormalIcons();
		activeChar.updateStatBonus();
		activeChar.updateStats();
		activeChar.updateUserBonus();

		activeChar.sendPacket(new ExUserViewInfoParameter(activeChar));

		if (Config.ALT_PCBANG_POINTS_ENABLED)
		{
			if (!Config.ALT_PCBANG_POINTS_ONLY_PREMIUM || activeChar.hasPremiumAccount())
			{
				activeChar.sendPacket(new ExPCCafePointInfoPacket(activeChar, 0, 1, 2, 12));
			}
		}

		activeChar.checkLevelUpReward(true);
		activeChar.sendClassChangeAlert();
		activeChar.sendPacket(new ExSubjugationSidebar(activeChar));

		// shortcut fix
		for (ShortCut shortCut : activeChar.getAllShortCuts())
		{
			if (shortCut.getAutoUse())
			{
				activeChar.sendPacket(new ExActivateAutoShortcut(shortCut.getSlot(), shortCut.getPage(), true));
				activeChar.getAutoShortCuts().activate(shortCut.getSlot(), shortCut.getPage(), true, true);
			}
		}

		if (first)
		{
			activeChar.useTriggers(activeChar, TriggerType.ON_ENTER_WORLD, null, null, 0);

			for (ListenerHook hook : ListenerHook.getGlobalListenerHooks(ListenerHookType.PLAYER_ENTER_GAME))
			{
				hook.onPlayerEnterGame(activeChar);
			}
			if (Config.ALLOW_IP_LOCK && Config.AUTO_LOCK_IP_ON_LOGIN)
			{
				AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedIp(activeChar.getAccountName(), activeChar.getIP()));
			}
			if (Config.ALLOW_HWID_LOCK && Config.AUTO_LOCK_HWID_ON_LOGIN)
			{
				GameClient client = activeChar.getNetConnection();
				if (client != null)
				{
					AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedHwid(activeChar.getAccountName(), client.getHWID()));
				}
			}
		}

		activeChar.getInventory().checkItems();
		if (activeChar.getRace() == Race.SYLPH)
		{
			activeChar.addAbnormalBoard();
		}
		activeChar.broadcastPacket(new StatusUpdate(activeChar, StatusType.Normal, UpdateType.VCP_BP, UpdateType.VCP_MAXBP));
	}

	private static void checkNewMail(Player activeChar)
	{
		activeChar.sendPacket(new ExUnReadMailCount(activeChar));
		for (Mail mail : MailDAO.getInstance().getReceivedMailByOwnerId(activeChar.getObjectId()))
		{
			if (mail.isUnread())
			{
				activeChar.sendPacket(ExNoticePostArrived.STATIC_FALSE);
				break;
			}
		}
	}
}