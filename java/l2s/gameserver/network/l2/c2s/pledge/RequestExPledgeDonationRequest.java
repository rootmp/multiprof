package l2s.gameserver.network.l2.c2s.pledge;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeDonationRequest;
import l2s.gameserver.templates.item.ItemTemplate;

/**
 * @author nexvill
 */
public class RequestExPledgeDonationRequest extends L2GameClientPacket
{
	private int _donateType;

	@Override
	protected boolean readImpl()
	{
		_donateType = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.getClan() == null)
			return;

		if (activeChar.getVarBoolean(PlayerVariables.DONATION_BLOCKED, false) == true)
			return;

		int donations = activeChar.getVarInt(PlayerVariables.DONATIONS_AVAILABLE, 3);
		Clan clan = activeChar.getClan();
		if (donations < 1)
			return;
		if (_donateType == 0)
			if (!activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, 100_000))
				return;
			else
			{
				clan.setPoints(clan.getPoints() + 50);
				int weeklyContribution = activeChar.getVarInt(PlayerVariables.WEEKLY_CONTRIBUTION, 0) + 50;
				int totalContribution = activeChar.getVarInt(PlayerVariables.TOTAL_CONTRIBUTION, 0) + 50;
				activeChar.setVar(PlayerVariables.WEEKLY_CONTRIBUTION, weeklyContribution);
				activeChar.setVar(PlayerVariables.TOTAL_CONTRIBUTION, totalContribution);
				activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_VE_DONATED_S1).addInteger(50));
			}
		else if (_donateType == 1)
			if (!activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, 100))
				return;
			else
			{
				clan.setPoints(clan.getPoints() + 100);
				int weeklyContribution = activeChar.getVarInt(PlayerVariables.WEEKLY_CONTRIBUTION, 0) + 100;
				int totalContribution = activeChar.getVarInt(PlayerVariables.TOTAL_CONTRIBUTION, 0) + 100;
				activeChar.setVar(PlayerVariables.WEEKLY_CONTRIBUTION, weeklyContribution);
				activeChar.setVar(PlayerVariables.TOTAL_CONTRIBUTION, totalContribution);
				activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_VE_DONATED_S1).addInteger(50));

				boolean critical = Rnd.get(100) < 5 ? true : false;
				int coins = 100;
				if (!critical)
				{
					activeChar.setHonorCoins(activeChar.getHonorCoins() + coins);
				}
				else
				{
					// sendMailToMembers(activeChar, coins); TODO: critical donation get bonus coins
					// to members
					coins *= 2;
					activeChar.setHonorCoins(activeChar.getHonorCoins() + coins);
					clan.broadcastToOnlineMembers(new SystemMessagePacket(SystemMsg.S1_CRITICAL_DONATION).addString(activeChar.getName()));
				}
				activeChar.sendPacket(new SystemMessage(SystemMsg.S1_HONOR_COINS_OBTAINED).addNumber(coins));
			}
		else if (_donateType == 2)
			if (!activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, 500))
				return;
			else
			{
				clan.setPoints(clan.getPoints() + 500);
				int weeklyContribution = activeChar.getVarInt(PlayerVariables.WEEKLY_CONTRIBUTION, 0) + 500;
				int totalContribution = activeChar.getVarInt(PlayerVariables.TOTAL_CONTRIBUTION, 0) + 500;
				activeChar.setVar(PlayerVariables.WEEKLY_CONTRIBUTION, weeklyContribution);
				activeChar.setVar(PlayerVariables.TOTAL_CONTRIBUTION, totalContribution);
				activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_VE_DONATED_S1).addInteger(500));

				boolean critical = Rnd.get(100) < 5 ? true : false;
				int coins = 500;
				if (!critical)
				{
					activeChar.setHonorCoins(activeChar.getHonorCoins() + coins);
				}
				else
				{
					// sendMailToMembers(activeChar, coins); TODO: critical donation get bonus coins
					// to members
					coins *= 2;
					activeChar.setHonorCoins(activeChar.getHonorCoins() + coins);
					clan.broadcastToOnlineMembers(new SystemMessagePacket(SystemMsg.S1_CRITICAL_DONATION).addString(activeChar.getName()));
				}
				activeChar.sendPacket(new SystemMessage(SystemMsg.S1_HONOR_COINS_OBTAINED).addNumber(coins));
			}

		activeChar.setVar(PlayerVariables.DONATIONS_AVAILABLE, --donations);

		activeChar.sendPacket(new ExPledgeDonationRequest(activeChar, _donateType));
	}
}