package l2s.gameserver.network.l2.c2s.pledge;

import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.gameserver.data.string.StringsHolder;
import l2s.gameserver.data.xml.holder.PledgeContributionHolder;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExBloodyCoinCount;
import l2s.gameserver.network.l2.s2c.ExNoticePostArrived;
import l2s.gameserver.network.l2.s2c.ExUnReadMailCount;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeContributionList;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeDonationInfo;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeDonationRequest;
import l2s.gameserver.templates.ClanContribution;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public class RequestExPledgeDonationRequest implements IClientIncomingPacket
{
	private int _donateType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_donateType = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(activeChar.getClan() == null)
			return;

		//if (activeChar.getVarBoolean(PlayerVariables.DONATION_BLOCKED, false) == true)
		//return;

		int donations = activeChar.getVarInt(PlayerVariables.DONATIONS_AVAILABLE, PledgeContributionHolder.getInstance().getDonationsAvailable());
		Clan clan = activeChar.getClan();
		if(donations < 1)
			return;
		ClanContribution contribution = PledgeContributionHolder.getInstance().getContribution(_donateType);
		if(contribution == null)
			return;

		if(!ItemFunctions.deleteItem(activeChar, contribution._ingredientItemId, contribution._ingredientCount))
		{
			activeChar.sendPacket(new ExPledgeDonationRequest(false, activeChar, _donateType));
			return;
		}
		else
		{
			activeChar.getListeners().onPlayerClanContributions(_donateType);

			clan.addExp(contribution._expCount);
			int weeklyContribution = activeChar.getVarInt(PlayerVariables.WEEKLY_CONTRIBUTION, 0) + contribution._expCount;
			int totalContribution = activeChar.getVarInt(PlayerVariables.TOTAL_CONTRIBUTION, 0) + contribution._expCount;
			activeChar.setVar(PlayerVariables.WEEKLY_CONTRIBUTION, weeklyContribution);
			activeChar.setVar(PlayerVariables.TOTAL_CONTRIBUTION, totalContribution);

			if(contribution._rewardItemId != 0)
			{
				if(contribution._rewardItemId == -700)
				{
					boolean critical = Rnd.get(100) < contribution._rewardChance ? true : false;
					int coins = contribution._rewardCount;
					if(!critical)
					{
						activeChar.setHonorCoins(activeChar.getHonorCoins() + coins);
					}
					else
					{
						sendMailToMembers(activeChar, coins);
						coins *= 2;
						activeChar.setHonorCoins(activeChar.getHonorCoins() + coins);
						clan.broadcastToOnlineMembers(new SystemMessagePacket(SystemMsg.S1_CRITICAL_DONATION).addString(activeChar.getName()));
					}
					activeChar.sendPacket(new SystemMessage(SystemMsg.S1_HONOR_COINS_OBTAINED).addNumber(coins));
				}
			}
		}

		activeChar.setVar(PlayerVariables.DONATIONS_AVAILABLE, --donations);

		activeChar.sendPacket(new ExBloodyCoinCount(activeChar));
		activeChar.sendPacket(new ExPledgeDonationRequest(true, activeChar, _donateType));
		activeChar.sendPacket(new ExPledgeContributionList(activeChar.getClan()));
		activeChar.sendPacket(new ExPledgeDonationInfo(activeChar));
	}

	private static void sendMailToMembers(Player player, int count)
	{
		for(UnitMember member : player.getClan().getAllMembers())
		{
			if(member.getObjectId() == player.getObjectId())
				continue;
			Mail mail = new Mail();
			mail.setSenderId(1);
			mail.setSenderName(StringsHolder.getInstance().getString(player, "birthday.npc"));
			mail.setReceiverId(member.getObjectId());
			mail.setReceiverName(member.getName());
			mail.setTopic("Critical Donation Reward");
			mail.setBody("Clan member " + player.getName() + " makes critical donation. This is reward to clan members.");

			ItemInstance item = ItemFunctions.createItem(95570);
			item.setLocation(ItemLocation.MAIL);
			item.setCount(count / 2);
			item.save();

			mail.addAttachment(item);
			mail.setUnread(true);
			mail.setType(Mail.SenderType.BIRTHDAY);
			mail.setExpireTime(720 * 3600 + (int) (System.currentTimeMillis() / 1000L));
			mail.save();

			Player plr = GameObjectsStorage.getPlayer(member.getObjectId());
			if(plr != null)
			{
				plr.sendPacket(ExNoticePostArrived.STATIC_TRUE);
				plr.sendPacket(new ExUnReadMailCount(plr));
				plr.sendPacket(SystemMsg.THE_MAIL_HAS_ARRIVED);
			}
		}
	}
}