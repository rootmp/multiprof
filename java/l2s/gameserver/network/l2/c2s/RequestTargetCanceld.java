package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillCastingType;

public class RequestTargetCanceld implements IClientIncomingPacket
{
	private int _unselect;

	/**
	 * packet type id 0x48 format: ch
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unselect = packet.readH();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(activeChar.getAggressionTarget() != null) // TODO: [Bonux] Проверить это условие.
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isLockedTarget())
		{
			activeChar.sendActionFailed();
			return;
		}

		if(_unselect == 0)
		{
			SkillEntry skillEntry = null;
			if(activeChar.getSkillCast(SkillCastingType.NORMAL).isCastingNow())
				skillEntry = activeChar.getSkillCast(SkillCastingType.NORMAL).getSkillEntry();

			SkillEntry dualSkillEntry = null;
			if(activeChar.getSkillCast(SkillCastingType.NORMAL_SECOND).isCastingNow())
				dualSkillEntry = activeChar.getSkillCast(SkillCastingType.NORMAL_SECOND).getSkillEntry();

			if(skillEntry != null || dualSkillEntry != null)
			{
				boolean force = skillEntry != null && (skillEntry.getTemplate().isHandler() || skillEntry.getTemplate().getHitTime() > 1000);
				if(!force)
					force = dualSkillEntry != null && (dualSkillEntry.getTemplate().isHandler() || dualSkillEntry.getTemplate().getHitTime() > 1000);
				activeChar.abortCast(force, false);
			}
			else if(activeChar.getTarget() != null)
				activeChar.setTarget(null);
		}
		else if(activeChar.getTarget() != null)
			activeChar.setTarget(null);
	}
}