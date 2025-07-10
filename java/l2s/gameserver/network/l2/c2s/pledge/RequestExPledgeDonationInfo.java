package l2s.gameserver.network.l2.c2s.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeClassicRaidInfo;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeDonationInfo;

/**
 * @author nexvill
 */
public class RequestExPledgeDonationInfo extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		readC(); // 0
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExPledgeClassicRaidInfo(activeChar));

		if (activeChar.getClan() == null)
			return;

		if (activeChar.getVarBoolean(PlayerVariables.DONATION_BLOCKED, false) == true)
			return;

		activeChar.sendPacket(new ExPledgeDonationInfo(activeChar));
	}
}