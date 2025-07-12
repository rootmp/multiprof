package l2s.gameserver.network.l2.c2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.Zone.ZoneType;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;

public final class RequestFlyMoveStart implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestFlyMoveStart.class);

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
		 return; 
/*
		if(activeChar.isInFlyMove() || activeChar.isTransformed() || activeChar.isMounted())
		{ return; }
		final Zone zone = activeChar.getZone(ZoneType.JUMPING);
		if(zone == null)
			return;

		if(activeChar.hasServitor())
		{
			activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_USE_SAYUNE_WHILE_PET_OR_SUMMONED_PET_IS_OUT);
			return;
		}
		if(activeChar.isPK())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_USE_SAYUNE_WHILE_IN_A_CHAOTIC_STATE);
			return;
		}
		if(activeChar.isProcessingRequest())
		{
			activeChar.sendPacket(SystemMsg.SAYUNE_CANNOT_BE_USED_WHILE_TAKING_OTHER_ACTIONS);
			return;
		}
		final int trackId = zone.getTemplate().getJumpTrackId();
		final JumpTrack track = JumpTracksHolder.getInstance().getTrack(trackId);
		if(track == null || track.getWays().isEmpty())
		{
			_log.warn("RequestFlyMoveStart: Track ID[" + trackId + "] was not found or empty!");
			return;
		}
		if(track.getMaxLevel() != 0 && activeChar.getLevel() > track.getMaxLevel())
		{
			activeChar.sendPacket(SystemMsg.YOUR_LEVEL_IS_NOT_SUITABLE_USE_IS_NOT_POSSIBLE);
			return;
		}

		new FlyMove(activeChar, track).move(0);

		QuestState st = activeChar.getQuestState(999);
		if(st == null)
			return;
		if(st.getInt("question_mark_state") == 20)
		{
			st.set("question_mark_state", 21);
			st.getQuest().onTutorialEvent("EW", false, "", st);
		}*/
	}

}
