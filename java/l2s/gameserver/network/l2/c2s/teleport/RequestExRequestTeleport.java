package l2s.gameserver.network.l2.c2s.teleport;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.TeleportListHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.BookMarkList;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.TeleportTemplate;
import l2s.gameserver.utils.ChatUtils;

/**
 * @author nexvill
 **/
public class RequestExRequestTeleport implements IClientIncomingPacket
{
	private int teleportId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		teleportId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(activeChar.isInJail())
		{
			activeChar.sendPacket(SystemMsg.S_13749);
			activeChar.sendActionFailed();
			return;
		}
		TeleportTemplate teleportInfo = TeleportListHolder.getInstance().getTeleportInfo(teleportId);
		if(teleportInfo == null)
		{
			activeChar.sendActionFailed();
			if(activeChar.isGM())
				ChatUtils.sys(activeChar, "Not found teleport info for ID: " + teleportId);
			return;
		}

		if(!BookMarkList.checkFirstConditions(activeChar) || !BookMarkList.checkTeleportConditions(activeChar)) // TODO:Check conditions.
			return;

		activeChar.bookmarkLocation = teleportInfo;

		if(activeChar.bookmarkLocation == null)
		{
			activeChar.sendActionFailed();
			if(activeChar.isGM())
				ChatUtils.sys(activeChar, "Not found teleport coordinates for ID: " + teleportId);
			return;
		}

		if(activeChar.isDebug())
			activeChar.sendMessage("Teleport id: " + teleportInfo.getId());

		SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, activeChar.getSkillLevel(45429, -1) == -1 ? 60018 : 60020, 1);
		if(!skillEntry.checkCondition(activeChar, activeChar, false, true, true))
		{
			activeChar.bookmarkLocation = null;
			return;
		}

		activeChar.getAI().Cast(skillEntry, activeChar, false, true);
	}
}
