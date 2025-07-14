package l2s.gameserver.network.l2.c2s.pledge;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeClassicRaidInfo;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeDonationInfo;

/**
 * @author nexvill
 */
public class RequestExPledgeDonationInfo implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readC(); // 0
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		activeChar.sendPacket(new ExPledgeClassicRaidInfo(activeChar));

		if(activeChar.getClan() == null)
			return;

		if(activeChar.getVarBoolean(PlayerVariables.DONATION_BLOCKED, false) == true)
			return;

		activeChar.sendPacket(new ExPledgeDonationInfo(activeChar));
	}
}