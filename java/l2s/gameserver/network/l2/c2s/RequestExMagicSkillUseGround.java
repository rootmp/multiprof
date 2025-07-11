package l2s.gameserver.network.l2.c2s;
import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExMagicSkillUseGround;
import l2s.gameserver.network.l2.s2c.MagicSkillLaunchedPacket;
import l2s.gameserver.network.l2.s2c.ValidateLocationPacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillCastingType;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.stats.triggers.TriggerInfo;
import l2s.gameserver.stats.triggers.TriggerType;
import l2s.gameserver.utils.PositionUtils;

/**
 * @author SYS
 * @date 08/9/2007 Format: chdddddc Пример пакета: D0 2F 00 E4 35 00 00 x 62 D1
 *       02 00 y 22 F2 FF FF z 90 05 00 00 skill id 00 00 00 00 ctrlPressed 00
 *       shiftPressed
 */
public class RequestExMagicSkillUseGround implements IClientIncomingPacket
{
	private Location _loc = new Location();
	private int _skillId;
	private boolean _ctrlPressed;
	private boolean _shiftPressed;

	/**
	 * packet type id 0xd0
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_loc.x = packet.readD();
		_loc.y = packet.readD();
		_loc.z = packet.readD();
		_skillId = packet.readD();
		_ctrlPressed = packet.readD() != 0;
		_shiftPressed = packet.readC() != 0;
		return true;
	}

	@Override
	public void run(GameClient client) throws InterruptedException
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}

		Skill skill = SkillHolder.getInstance().getSkill(_skillId, activeChar.getSkillLevel(_skillId));
		if (skill != null)
		{
			// В режиме трансформации доступны только скилы трансформы
			if (activeChar.isTransformed() && !activeChar.getAllSkills().contains(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skill)))
				return;

			Creature target = skill.getAimingTarget(activeChar, activeChar.getTarget());

			// normally magicskilluse packet turns char client side but for these skills, it
			// doesn't (even with correct target)
			activeChar.setHeading(PositionUtils.calculateHeadingFrom(activeChar.getX(), activeChar.getY(), _loc.x, _loc.y));
			activeChar.broadcastPacketToOthers(new ValidateLocationPacket(activeChar));
			activeChar.setGroundSkillLoc(_loc);
			activeChar.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skill), target, _ctrlPressed, _shiftPressed);
			if (skill.getId() == 47001)
			{
				TriggerInfo trigger = new TriggerInfo(47020, 1, TriggerType.ON_FINISH_CAST, 100, false, 0, false, "");
				skill.addTrigger(trigger);
				GameObject oldTarget = activeChar.getTarget();
				List<Creature> characters = new ArrayList<Creature>();
				List<Creature> fullList = new ArrayList<Creature>();
				for (Creature cr : World.getAroundCharacters(_loc, activeChar.getObjectId(), activeChar.getReflectionId(), 200, 300))
				{
					if (activeChar.isInRange(_loc, 1500))
					{
						characters.add(cr);
						fullList.add(cr);
					}
				}
				for (Creature cr : World.getAroundCharacters(activeChar, 100, 300))
				{
					if (activeChar.isInRange(_loc, 1500))
					{
						if (!characters.contains(cr))
						{
							fullList.add(cr);
						}
					}
				}
				SkillEntry entry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47001, 1);
				activeChar.broadcastPacket(new MagicSkillLaunchedPacket(activeChar.getObjectId(), entry.getDisplayId(), entry.getDisplayLevel(), fullList, SkillCastingType.NORMAL));
				activeChar.sendPacket(new ExMagicSkillUseGround(activeChar, _loc));
				Thread.sleep(skill.getHitTime() + skill.getCoolTime() + 100);
				for (Creature cr : fullList)
				{
					if (activeChar.isInRange(_loc, 1500))
					{
						activeChar.setTarget(cr);
						activeChar.useTriggers(cr, TriggerType.ON_FINISH_CAST, null, skill, 0);
					}
				}
				activeChar.setTarget(oldTarget);
				activeChar.abortCast(true, false);
			}
		}
		else
			activeChar.sendActionFailed();
	}
}