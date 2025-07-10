package handler.voicecommands;

import static l2s.gameserver.model.Zone.ZoneType.no_restart;
import static l2s.gameserver.model.Zone.ZoneType.no_summon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.napile.primitive.pair.IntObjectPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.commons.lang.reference.HardReference;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.CoupleManager;
import l2s.gameserver.listener.actor.player.OnAnswerListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.Couple;
import l2s.gameserver.model.entity.events.impl.SingleMatchEvent;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ConfirmDlgPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.enums.AbnormalEffect;

public class Wedding extends ScriptVoiceCommandHandler
{
	private static class CoupleAnswerListener implements OnAnswerListener
	{
		private HardReference<Player> _playerRef1;
		private HardReference<Player> _playerRef2;

		public CoupleAnswerListener(Player player1, Player player2)
		{
			_playerRef1 = player1.getRef();
			_playerRef2 = player2.getRef();
		}

		@Override
		public void sayYes()
		{
			Player player1, player2;
			if ((player1 = _playerRef1.get()) == null || (player2 = _playerRef2.get()) == null)
				return;

			CoupleManager.getInstance().createCouple(player1, player2);
			player1.sendMessage(new CustomMessage("l2s.gameserver.model.L2Player.EngageAnswerYes"));
		}

		@Override
		public void sayNo()
		{
			Player player;
			if ((player = _playerRef1.get()) == null || (player = _playerRef2.get()) == null)
			{
				return;
			}
			player.sendMessage(new CustomMessage("l2s.gameserver.model.L2Player.EngageAnswerNo"));
		}
	}

	private static final Logger _log = LoggerFactory.getLogger(Wedding.class);

	private static String[] COMMANDS =
	{
		"divorce",
		"engage",
		"gotolove"
	};

	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (!Config.ALLOW_WEDDING)
			return false;

		if (command.startsWith("engage"))
			return engage(activeChar);
		else if (command.startsWith("divorce"))
			return divorce(activeChar);
		else if (command.startsWith("gotolove"))
			return goToLove(activeChar);
		return false;
	}

	public boolean divorce(Player activeChar)
	{
		if (activeChar.getPartnerId() == 0)
			return false;

		int _partnerId = activeChar.getPartnerId();
		long AdenaAmount = 0;

		if (activeChar.isMaried())
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.Divorced"));
			AdenaAmount = Math.abs(activeChar.getAdena() / 100 * Config.WEDDING_DIVORCE_COSTS - 10);
			activeChar.reduceAdena(AdenaAmount, true);
		}
		else
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.Disengaged"));

		activeChar.setMaried(false);
		activeChar.setPartnerId(0);
		Couple couple = CoupleManager.getInstance().getCouple(activeChar.getCoupleId());
		couple.divorce();
		couple = null;

		Player partner = GameObjectsStorage.getPlayer(_partnerId);

		if (partner != null)
		{
			partner.setPartnerId(0);
			if (partner.isMaried())
				partner.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.PartnerDivorce"));
			else
				partner.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.PartnerDisengage"));
			partner.setMaried(false);

			// give adena
			if (AdenaAmount > 0)
				partner.addAdena(AdenaAmount);
		}
		return true;
	}

	public boolean engage(final Player activeChar)
	{
		// check target
		if (activeChar.getTarget() == null)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.NoneTargeted"));
			return false;
		}
		// check if target is a L2Player
		if (!activeChar.getTarget().isPlayer())
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.OnlyAnotherPlayer"));
			return false;
		}
		// check if player is already engaged
		if (activeChar.getPartnerId() != 0)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.AlreadyEngaged"));
			if (Config.WEDDING_PUNISH_INFIDELITY)
			{
				activeChar.startAbnormalEffect(AbnormalEffect.BIG_HEAD);
				// Head
				// lets recycle the sevensigns debuffs
				int skillId;

				int skillLevel = 1;

				if (activeChar.getLevel() > 40)
					skillLevel = 2;

				if (activeChar.isMageClass())
					skillId = 4361;
				else
					skillId = 4362;

				Skill skill = SkillHolder.getInstance().getSkill(skillId, skillLevel);

				if (!activeChar.getAbnormalList().contains(skill))
				{
					skill.getEffects(activeChar, activeChar);
					activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1S_EFFECT_CAN_BE_FELT).addSkillName(skillId, skillLevel));
				}
			}
			return false;
		}

		final Player ptarget = (Player) activeChar.getTarget();

		// check if player target himself
		if (ptarget.getObjectId() == activeChar.getObjectId())
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.EngagingYourself"));
			return false;
		}

		if (ptarget.isMaried())
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.PlayerAlreadyMarried"));
			return false;
		}

		if (ptarget.getPartnerId() != 0)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.PlayerAlreadyEngaged"));
			return false;
		}

		IntObjectPair<OnAnswerListener> entry = ptarget.getAskListener(false);
		if (entry != null && entry.getValue() instanceof CoupleAnswerListener)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.PlayerAlreadyAsked"));
			return false;
		}

		if (ptarget.getPartnerId() != 0)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.PlayerAlreadyEngaged"));
			return false;
		}

		if (ptarget.getSex() == activeChar.getSex() && !Config.WEDDING_SAMESEX)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.SameSex"));
			return false;
		}

		// TODO [G1ta0] Реализовать нормальный список друзей
		boolean FoundOnFriendList = false;
		int objectId;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();

			statement = con.prepareStatement("SELECT friend_id FROM character_friends WHERE char_id=?");
			statement.setInt(1, ptarget.getObjectId());
			rset = statement.executeQuery();

			while (rset.next())
			{
				objectId = rset.getInt("friend_id");
				if (objectId == activeChar.getObjectId())
				{
					FoundOnFriendList = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		if (!FoundOnFriendList)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.NotInFriendlist"));
			return false;
		}

		ConfirmDlgPacket packet = new ConfirmDlgPacket(SystemMsg.S1, 60000).addString("Player " + activeChar.getName() + " asking you to engage. Do you want to start new relationship?");
		ptarget.ask(packet, new CoupleAnswerListener(activeChar, ptarget));
		return true;
	}

	public boolean goToLove(Player activeChar)
	{
		if (!activeChar.isMaried())
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.YoureNotMarried"));
			return false;
		}

		if (activeChar.getPartnerId() == 0)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.PartnerNotInDB"));
			return false;
		}

		Player partner = GameObjectsStorage.getPlayer(activeChar.getPartnerId());
		if (partner == null)
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.PartnerOffline"));
			return false;
		}

		if (partner.isInOlympiadMode() || activeChar.isMovementDisabled() || activeChar.isMuted(null) || activeChar.isInOlympiadMode() || activeChar.containsEvent(SingleMatchEvent.class) || partner.containsEvent(SingleMatchEvent.class) || partner.isInZone(no_summon))
		{
			activeChar.sendMessage(new CustomMessage("common.TryLater"));
			return false;
		}

		if (activeChar.getTeleMode() != 0 || !activeChar.getReflection().isMain())
		{
			activeChar.sendMessage(new CustomMessage("common.TryLater"));
			return false;
		}

		// "Нельзя вызывать персонажей в/из зоны свободного PvP"
		// "в зоны осад"
		// "на Олимпийский стадион"
		// "в зоны определенных рейд-боссов и эпик-боссов"
		// в режиме обсервера или к обсерверу
		if (partner.isInZoneBattle() || partner.isInZone(Zone.ZoneType.SIEGE) || partner.isInZone(no_restart) || partner.isInOlympiadMode() || activeChar.isInZoneBattle() || activeChar.isInZone(Zone.ZoneType.SIEGE) || activeChar.isInZone(no_restart) || activeChar.isInOlympiadMode() || !partner.getReflection().isMain() || partner.isInZone(no_summon) || activeChar.isInObserverMode() || partner.isInObserverMode())
		{
			activeChar.sendPacket(SystemMsg.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING);
			return false;
		}

		if (!activeChar.reduceAdena(Config.WEDDING_TELEPORT_PRICE, true))
		{
			activeChar.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return false;
		}

		int teleportTimer = Config.WEDDING_TELEPORT_INTERVAL;

		activeChar.abortAttack(true, true);
		activeChar.abortCast(true, true);
		activeChar.sendActionFailed();
		activeChar.getMovement().stopMove();
		activeChar.getFlags().getParalyzed().start();

		activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Wedding.Teleport").addNumber(teleportTimer / 60));
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);

		// SoE Animation section
		activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1050, 1, teleportTimer, 0));
		// activeChar.sendPacket(new SetupGaugePacket(activeChar,
		// SetupGaugePacket.Colors.BLUE, teleportTimer));
		// End SoE Animation section

		// continue execution later
		ThreadPoolManager.getInstance().schedule(new EscapeFinalizer(activeChar, partner.getLoc()), teleportTimer * 1000L);
		return true;
	}

	static class EscapeFinalizer implements Runnable
	{
		private Player _activeChar;
		private Location _loc;

		EscapeFinalizer(Player activeChar, Location loc)
		{
			_activeChar = activeChar;
			_loc = loc;
		}

		@Override
		public void run()
		{
			if (_activeChar == null)
				return;

			_activeChar.getFlags().getParalyzed().stop();

			if (_activeChar.isDead())
				return;

			_activeChar.teleToLocation(_loc);
		}
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return COMMANDS;
	}
}