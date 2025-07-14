package l2s.gameserver.skills.skillclasses;

import java.util.List;
import java.util.Set;

import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.entity.events.impl.FortressSiegeEvent;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.entity.events.objects.FortressCombatFlagObject;
import l2s.gameserver.model.entity.events.objects.StaticObjectObject;
import l2s.gameserver.model.entity.residence.Fortress;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.instances.StaticObjectInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.attachment.ItemAttachment;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExNoticePostArrived;
import l2s.gameserver.network.l2.s2c.ExUnReadMailCount;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillCastingType;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.utils.ItemFunctions;

public class TakeFortress extends Skill
{
	public TakeFortress(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(SkillEntry skillEntry, Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first, boolean sendMsg, boolean trigger)
	{
		if(!super.checkCondition(skillEntry, activeChar, target, forceUse, dontMove, first, sendMsg, trigger))
		{ return false; }

		if((activeChar == null) || !activeChar.isPlayer())
		{ return false; }

		GameObject flagPole = activeChar.getTarget();
		if(!(flagPole instanceof StaticObjectInstance) || (((StaticObjectInstance) flagPole).getType() != 3))
		{
			activeChar.sendPacket(SystemMsg.THE_TARGET_IS_NOT_A_FLAGPOLE_SO_A_FLAG_CANNOT_BE_DISPLAYED);
			return false;
		}

		if(first)
		{
			List<Creature> around = World.getAroundCharacters(flagPole, getAffectRange() * 2, 100);
			for(Creature ch : around)
			{
				if(ch.getSkillCast(SkillCastingType.NORMAL).isCastingNow()
						&& ch.getSkillCast(SkillCastingType.NORMAL).getSkillEntry().getTemplate().equals(this)) // проверяел
				// ли
				// ктото
				// возле
				// нас
				// кастует
				// накойже
				// скил
				{
					activeChar.sendPacket(SystemMsg.A_FLAG_IS_ALREADY_BEING_DISPLAYED_ANOTHER_FLAG_CANNOT_BE_DISPLAYED);
					return false;
				}

				if(ch.getSkillCast(SkillCastingType.NORMAL_SECOND).isCastingNow()
						&& ch.getSkillCast(SkillCastingType.NORMAL_SECOND).getSkillEntry().getTemplate().equals(this)) // проверяел
				// ли
				// ктото
				// возле
				// нас
				// кастует
				// накойже
				// скил
				{
					activeChar.sendPacket(SystemMsg.A_FLAG_IS_ALREADY_BEING_DISPLAYED_ANOTHER_FLAG_CANNOT_BE_DISPLAYED);
					return false;
				}
			}
		}

		Player player = (Player) activeChar;
		if(player.getClan() == null)
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}

		if(player.getClan().getLevel() < 4)
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}

		// FortressSiegeEvent siegeEvent = player.getEvent(FortressSiegeEvent.class);
		// if(siegeEvent == null)
		// {
		// activeChar.sendPacket(new
		// SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
		// return false;
		// }

		if(player.isMounted())
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}

		FortressSiegeEvent siegeEvent = null;
		List<Fortress> forts = ResidenceHolder.getInstance().getResidenceList(Fortress.class);
		for(Fortress fortress : forts)
		{
			if((fortress.getId() == 117) && fortress.getSiegeEvent().isInProgress())
			{
				siegeEvent = fortress.getSiegeEvent();
				if(siegeEvent == null)
				{ return false; }
			}
		}

		ItemAttachment attach = player.getActiveWeaponFlagAttachment();
		if(!(attach instanceof FortressCombatFlagObject) || (((FortressCombatFlagObject) attach).getEvent() != siegeEvent))
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}

		if((getCastRange() > 0) && !player.isInRangeZ(target, getCastRange()))
		{
			player.sendPacket(SystemMsg.YOUR_TARGET_IS_OUT_OF_RANGE);
			return false;
		}

		if(first)
		{
			siegeEvent.broadcastTo(new SystemMessagePacket(SystemMsg.S1_CLAN_IS_TRYING_TO_DISPLAY_A_FLAG).addString(player.getClan().getName()), SiegeEvent.DEFENDERS);
		}

		return true;
	}

	@Override
	public void onFinishCast(Creature aimingTarget, Creature activeChar, Set<Creature> targets)
	{
		GameObject flagPole = activeChar.getTarget();
		if(!(flagPole instanceof StaticObjectInstance) || (((StaticObjectInstance) flagPole).getType() != 3))
		{ return; }

		// Player player = (Player) activeChar;
		// FortressSiegeEvent siegeEvent = player.getEvent(FortressSiegeEvent.class);
		// if(siegeEvent == null)
		// return;

		FortressSiegeEvent siegeEvent = null;
		List<Fortress> forts = ResidenceHolder.getInstance().getResidenceList(Fortress.class);
		for(Fortress fortress : forts)
		{
			if((fortress.getId() == 117) && fortress.getSiegeEvent().isInProgress())
			{
				siegeEvent = fortress.getSiegeEvent();
				if(siegeEvent == null)
				{ return; }
			}
		}

		StaticObjectObject object = siegeEvent.getFirstObject(FortressSiegeEvent.FLAG_POLE);
		if(((StaticObjectInstance) flagPole).getUId() != object.getUId())
		{ return; }

		siegeEvent.processStep(activeChar.getPlayer().getClan());
		sendMail(activeChar.getPlayer());
		super.onFinishCast(aimingTarget, activeChar, targets);
	}

	private void sendMail(Player player)
	{
		Mail mail = new Mail();
		mail.setSenderId(1);
		mail.setSenderName("Reward for take fortress");
		mail.setReceiverId(player.getObjectId());
		mail.setReceiverName(player.getName());
		mail.setTopic("Reward for Taking Fortress");
		mail.setBody("You successfully take fortress");

		ItemInstance item = ItemFunctions.createItem(Config.FORTRESS_REWARD_ID);
		if(item != null)
		{
			item.setLocation(ItemLocation.MAIL);
			item.setCount(Config.FORTRESS_REWARD_COUNT);
			item.save();

			mail.addAttachment(item);
			mail.setType(Mail.SenderType.BIRTHDAY);
			mail.setUnread(true);
			mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
			mail.save();

			if(player != null)
			{
				player.sendPacket(ExNoticePostArrived.STATIC_TRUE);
				player.sendPacket(new ExUnReadMailCount(player));
				player.sendPacket(SystemMsg.THE_MAIL_HAS_ARRIVED);
			}
		}
	}
}