package l2s.gameserver.network.l2.c2s;
import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.ban.BanBindType;
import l2s.commons.ban.BanInfo;
import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.GameBanManager;
import l2s.gameserver.model.CommandChannel;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExRequestInviteParty;
import l2s.gameserver.utils.ChatUtils;
import l2s.gameserver.utils.TimeUtils;

public class RequestExRequestInviteParty implements IClientIncomingPacket
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestExRequestInviteParty.class);

	private int cReqType;
	private ChatType cSayType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cReqType = packet.readC(); // 0 - Party, 1 - CC
		cSayType = l2s.commons.lang.ArrayUtils.valid(ChatType.VALUES, packet.readC());
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (cSayType != ChatType.ALL && cSayType != ChatType.TRADE && cSayType != ChatType.CLAN && cSayType != ChatType.ALLIANCE)
			return;

		if ((activeChar.getLastRequestInviteParty() + 10_000L) > System.currentTimeMillis())
			return;

		activeChar.setLastRequestInviteParty(System.currentTimeMillis());

		if (cReqType == 0)
		{
			if (activeChar.isInParty())
				return;
		}
		else if (cReqType == 1)
		{
			Party party = activeChar.getParty();
			if (party == null || !party.isLeader(activeChar) || party.getCommandChannel() != null)
				return;
		}
		else
		{
			return;
		}

		if (activeChar.isInJail() || activeChar.isInCollectionUI())
			return;

		if (activeChar.isInOlympiadMode() || Olympiad.isRegisteredInComp(activeChar))
		{
			// TODO: Check message.
			return;
		}

		if (activeChar.isChatBlocked())
		{
			activeChar.sendPacket(SystemMsg.YOU_ARE_NOT_ALLOWED_TO_CHAT_WITH_A_CONTACT_WHILE_A_CHATTING_BLOCK_IS_IMPOSED);
			return;
		}

		if (cSayType == ChatType.ALL || ArrayUtils.contains(Config.BAN_CHANNEL_LIST, cSayType.ordinal()))
		{
			BanInfo banInfo = GameBanManager.getInstance().getBanInfoIfBanned(BanBindType.CHAT, activeChar.getObjectId());
			if (banInfo != null)
			{
				if (banInfo.getEndTime() == -1)
					activeChar.sendMessage(new CustomMessage("common.ChatBannedPermanently"));
				else
					activeChar.sendMessage(new CustomMessage("common.ChatBanned").addString(TimeUtils.toSimpleFormat(banInfo.getEndTime() * 1000L)));
				activeChar.sendActionFailed();
				return;
			}

			if (activeChar.getNoChannel() != 0L)
			{
				if (activeChar.getNoChannelRemained() > 0L || activeChar.getNoChannel() < 0L)
				{
					if (activeChar.getNoChannel() > 0L)
						activeChar.sendMessage(new CustomMessage("common.ChatBanned").addString(TimeUtils.toSimpleFormat(System.currentTimeMillis() + activeChar.getNoChannelRemained())));
					else
						activeChar.sendMessage(new CustomMessage("common.ChatBannedPermanently"));
					activeChar.sendActionFailed();
					return;
				}
				activeChar.updateNoChannel(0L);
			}
		}

		Predicate<Player> predicate = null;
		if (cReqType == 0)
		{
			predicate = (p) ->
			{
				Party party = p.getParty();
				return party == null || party.isLeader(p);
			};
		}
		else if (cReqType == 1)
		{
			predicate = (p) ->
			{
				Party party = p.getParty();
				if (party == null || !party.isLeader(p))
					return false;
				CommandChannel cc = party.getCommandChannel();
				return cc == null || cc.isLeaderCommandChannel(p);
			};
		}

		switch (cSayType)
		{
			case TRADE:
			{
				if (activeChar.isInObserverMode())
				{
					activeChar.sendPacket(SystemMsg.YOU_CANNOT_CHAT_WHILE_IN_OBSERVATION_MODE);
					return;
				}

				if (!activeChar.getAntiFlood().canTrade(""))
					return;

				ExRequestInviteParty msg = new ExRequestInviteParty(activeChar, cReqType, cSayType);
				if (Config.GLOBAL_TRADE_CHAT)
					ChatUtils.announce(activeChar, msg, predicate);
				else
					ChatUtils.shout(activeChar, msg, predicate);

				activeChar.sendPacket(msg);
				break;
			}
			case ALL:
			{
				if (activeChar.isInObserverMode())
				{
					activeChar.sendPacket(SystemMsg.YOU_CANNOT_CHAT_WHILE_IN_OBSERVATION_MODE);
					return;
				}

				if (!activeChar.getAntiFlood().canAll(""))
					return;

				ExRequestInviteParty msg = new ExRequestInviteParty(activeChar, cReqType, cSayType);

				ChatUtils.say(activeChar, msg, predicate);

				msg.setName(activeChar.getVisibleName(activeChar));

				activeChar.sendPacket(msg);
				break;
			}
			case CLAN:
			{
				if (activeChar.getClan() != null)
				{
					activeChar.getClan().broadcastToOnlineMembersPredicate(predicate, new ExRequestInviteParty(activeChar, cReqType, cSayType));
				}
				break;
			}
			case ALLIANCE:
			{
				if (activeChar.getClan() != null && activeChar.getClan().getAlliance() != null)
				{
					activeChar.getClan().getAlliance().broadcastToOnlineMembersPredicate(predicate, new ExRequestInviteParty(activeChar, cReqType, cSayType));
				}
				break;
			}
			default:
			{
				LOGGER.warn("Character " + activeChar.getName() + " used unknown chat type: " + cSayType.ordinal() + ".");
				break;
			}
		}
	}
}