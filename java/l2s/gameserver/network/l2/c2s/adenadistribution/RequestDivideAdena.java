package l2s.gameserver.network.l2.c2s.adenadistribution;

import java.util.List;

import l2s.gameserver.model.CommandChannel;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.adenadistribution.ExDivideAdenaDone;

/**
 * @author Sdw
 */
public class RequestDivideAdena extends L2GameClientPacket
{
	private long _count;

	@Override
	protected boolean readImpl()
	{
		readD();
		_count = readQ();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		final Party party = activeChar.getParty();
		if (party == null)
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_PROCEED_AS_YOU_ARE_NOT_IN_AN_ALLIANCE_OR_PARTY);
			return;
		}

		final CommandChannel commandChannel = party.getCommandChannel();
		if ((commandChannel != null) && !commandChannel.isLeaderCommandChannel(activeChar))
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_PROCEED_AS_YOU_ARE_NOT_AN_ALLIANCE_LEADER_OR_PARTY_LEADER);
			return;
		}
		else if (!party.isLeader(activeChar))
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_PROCEED_AS_YOU_ARE_NOT_A_PARTY_LEADER);
			return;
		}
		
		final List<Player> targets = commandChannel != null ? commandChannel.getMembers() : party.getPartyMembers();
		if (activeChar.getAdena() < targets.size())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_PROCEED_AS_THERE_IS_INSUFFICIENT_ADENA);
			return;
		}

		if (_count > activeChar.getAdena())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_PROCEED_AS_THERE_IS_INSUFFICIENT_ADENA);
			return;
		}

		int membersCount = activeChar.getParty().getMemberCount();
		long dividedCount = (long) Math.floor(_count / membersCount);
		activeChar.reduceAdena(membersCount * dividedCount, false);
		for (Player player : activeChar.getParty().getPartyMembers())
		{
			player.addAdena(dividedCount, player.getObjectId() != activeChar.getObjectId());
		}

		activeChar.sendPacket(new ExDivideAdenaDone(membersCount, _count, dividedCount, activeChar.getName()));
	}
}