/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.
 */
package handler.voicecommands;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.listener.actor.player.QuestionMarkListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.JoinPartyPacket;
import l2s.gameserver.network.l2.s2c.SayPacket2;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.utils.Functions;

public class FindParty extends Functions implements IVoicedCommandHandler
{
	private static final String[] COMMANDS =
	{
		"party",
		"invite",
		"partylist"
	};

	// 10 minutes delay until the request is rendered invalid.
	private static final int PARTY_REQUEST_DURATION = 600_000;
	// 1 minute delay until you can send a party request to the whole server again.
	private static final int PARTY_REQUEST_DELAY = 60_000;

	private static final OnPartyQuestionMarkClicked LISTENER = new OnPartyQuestionMarkClicked();
	private static final Map<Integer, FindPartyRequest> _requests = new ConcurrentHashMap<Integer, FindPartyRequest>();
	private static ScheduledFuture<?> _requestsCleanupTask = null;

	static
	{
		CharListenerList.addGlobal(LISTENER);
		_requestsCleanupTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				synchronized (_requests) // Dont touch me while cleaning-up!
				{
					for (Entry<Integer, FindPartyRequest> entry : _requests.entrySet())
					{
						if (entry.getValue()._requestStartTimeMilis + PARTY_REQUEST_DURATION < System.currentTimeMillis())
						{
							_requests.remove(entry.getKey());
						}
					}
				}
			}
		}, 60000, 60000);
	}

	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		// Only players 60+
		if (activeChar.getLevel() < 60)
		{
			activeChar.sendMessage("Command available only for players lvl 60+");
			return true;
		}

		if (command.startsWith("partylist"))
		{
			int i = 0;
			SayPacket2[] packets = new SayPacket2[_requests.size() + 2];
			packets[i++] = new SayPacket2(activeChar.getObjectId(), ChatType.BATTLEFIELD, 0, "[Party Request]", "---------=[List Party Requests]=---------");
			for (FindPartyRequest request : _requests.values())
			{
				// .partylist freya -> will result in searching party requests for freya only.
				if (target != null && !target.isEmpty() && !request._message.toLowerCase().contains(target.toLowerCase()))
				{
					continue;
				}

				Player partyLeader = World.getPlayer(request._requestorObjId);
				if (partyLeader == null)
				{
					continue;
				}

				int freeSlots = Party.MAX_SIZE - 1; // One taken by the party leader.
				if (partyLeader.getParty() != null)
				{
					freeSlots = Party.MAX_SIZE - partyLeader.getParty().getMemberCount();
				}
				if (freeSlots <= 0)
				{
					continue;
				}

				packets[i++] = new SayPacket2(activeChar.getObjectId(), ChatType.PARTY, 0, "[Find Party]", "\b\tType=1 \tID=" + partyLeader.getObjectId() + " \tColor=0 \tUnderline=0 \tTitle=\u001B\u001B\b" + partyLeader.getName() + " (" + freeSlots + "/" + Party.MAX_SIZE + ")" + " free slots. " + request._message);
			}
			packets[i++] = new SayPacket2(activeChar.getObjectId(), ChatType.BATTLEFIELD, 0, "[Party Request]", "---------=[End Party Requests]=---------");
			activeChar.sendPacket(packets);
			return true;
		}
		else if (command.startsWith("invite"))
		{
			Player playerToInvite = null;
			if (activeChar.isInParty() && !activeChar.getParty().isLeader(activeChar) && activeChar.getParty().getMemberCount() >= Party.MAX_SIZE)
			{
				playerToInvite = GameObjectsStorage.getPlayer(target); // Possibly this is a player invite request
																		// within the party.
			}

			if (playerToInvite != null) // A party member asks the party leader to invite specified player.
			{
				SayPacket2 packetLeader = new SayPacket2(activeChar.getObjectId(), ChatType.PARTY, 0, "[Party Request]", "Please invite " + playerToInvite.getName() + " to the party. \b\tType=1 \tID=" + playerToInvite.getObjectId() + " \tColor=0 \tUnderline=0 \tTitle=\u001B\u001B\b");
				SayPacket2 packet = new SayPacket2(activeChar.getObjectId(), ChatType.PARTY, 0, "[Party Request]", "Please invite " + playerToInvite.getName() + " to the party.");
				for (Player ptMem : activeChar.getParty())
				{
					if (activeChar.getParty().getPartyLeader() == ptMem)
					{
						ptMem.sendPacket(packetLeader);
					}
					else
					{
						ptMem.sendPacket(packet);
					}
				}
			}
		}
		else if (command.startsWith("party")) // The party leader requests whole server for party members.
		{
			// Only party leaders can use it.
			if (activeChar.isInParty() && !activeChar.getParty().isLeader(activeChar))
			{
				activeChar.sendMessage("Only your party leaader can use this command now.");
				return true;
			}

			int partyRequestObjId = 0;
			for (Entry<Integer, FindPartyRequest> entry : _requests.entrySet())
			{
				if (entry.getValue()._requestorObjId == activeChar.getObjectId())
				{
					partyRequestObjId = entry.getKey();
					break;
				}
			}
			if (partyRequestObjId == 0)
			{
				partyRequestObjId = IdFactory.getInstance().getNextId();
			}

			int freeSlots = Party.MAX_SIZE - 1; // One taken by the party leader.
			if (activeChar.getParty() != null)
			{
				freeSlots = Party.MAX_SIZE - activeChar.getParty().getMemberCount();
			}
			if (freeSlots <= 0)
			{
				activeChar.sendMessage("Your party is full. Try again when you have free slots.");
				return true;
			}

			if (target != null && !target.isEmpty())
			{
				target = String.valueOf(target.charAt(0)).toUpperCase() + target.substring(1);
			}

			FindPartyRequest request = _requests.get(partyRequestObjId);
			if (request == null)
			{
				request = new FindPartyRequest(activeChar, target);
			}
			else
			{
				long delay = System.currentTimeMillis() - request._requestStartTimeMilis;
				if (delay < PARTY_REQUEST_DELAY)
				{
					activeChar.sendMessage("You can send a request every " + PARTY_REQUEST_DELAY / 1000 + " seconds. " + (PARTY_REQUEST_DELAY - delay) / 1000 + " seconds remaining until you can try again.");
					return true;
				}

				if (target == null || target.isEmpty())
				{
					request.update(); // Update perserving the message so players can type only .party, but displaying
										// the same message as before.
				}
				else
				{
					request.update(target); // Update with overriding the message
				}
			}
			_requests.put(partyRequestObjId, request);

			// [Party Find]: [?] Nik (3/9) free slots. Message
			SayPacket2 packet = new SayPacket2(activeChar.getObjectId(), ChatType.PARTY, 0, "[Party]", activeChar.getName() + "'s party (" + freeSlots + "/" + Party.MAX_SIZE + ")" + " free slots. " + "\b\tType=1 \tID=" + partyRequestObjId + " \tColor=0 \tUnderline=0 \tTitle=\u001B\u001B\b" + request._message);
			for (Player player : GameObjectsStorage.getPlayers(false, false))
			{
				// Do not display to players who cant join party, but display to the requesting
				// party so they can see their own message working.
				if (player.canJoinParty(activeChar) != null && !(activeChar.isInParty() && activeChar.getParty().containsMember(player)))
				{
					continue;
				}

				player.sendPacket(packet);
			}
		}
		return false;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return COMMANDS;
	}

	private static class OnPartyQuestionMarkClicked implements QuestionMarkListener
	{
		@Override
		public void onQuestionMarkClicked(Player player, int targetObjId)
		{
			int requestorObjId = _requests.containsKey(targetObjId) ? _requests.get(targetObjId)._requestorObjId : 0;
			if (requestorObjId > 0) // Its a regular party request to the server for additional party members.
			{
				if (player.getObjectId() != requestorObjId)
				{
					Player partyLeader = World.getPlayer(requestorObjId);
					if (partyLeader == null)
					{
						player.sendMessage("Party leader is offline.");
					}
					else// if (partyLeader.isInParty())
					{
						// requestParty(partyLeader, player);
						long delay = System.currentTimeMillis() - player.getVarLong(PlayerVariables.FINDPARTY, 0);
						if (delay < PARTY_REQUEST_DELAY)
						{
							player.sendMessage("You can send a request every " + PARTY_REQUEST_DELAY / 1000 + " seconds. " + (PARTY_REQUEST_DELAY - delay) / 1000 + " seconds remaining until you can try again.");
							return;
						}
						player.setVar(PlayerVariables.FINDPARTY, System.currentTimeMillis());
						SayPacket2 packetLeader = new SayPacket2(player.getObjectId(), ChatType.TELL, 0, "", "I'm Level: " + player.getLevel() + ", Class: " + player.getClassId().getName(player) + ". Invite \b\tType=1 \tID=" + player.getObjectId() + " \tColor=0 \tUnderline=0 \tTitle=\u001B\u001B\b");
						partyLeader.sendPacket(packetLeader);
						player.sendMessage("Party request sent to " + partyLeader.getName());
					}
				}
			}
			else
			{
				Player target = GameObjectsStorage.getPlayer(targetObjId); // Looks like a party request within a party
																			// to invite a certain member.
				if (target != null)
				{
					requestParty(player, target);
				}
				else
				{
					player.sendMessage("The request is no longer valid.");
				}
			}
		}

		private void requestParty(Player partyLeader, Player target)
		{
			if (partyLeader.isOutOfControl())
			{
				partyLeader.sendActionFailed();
				return;
			}

			if (partyLeader.isProcessingRequest())
			{
				partyLeader.sendPacket(SystemMsg.WAITING_FOR_ANOTHER_REPLY);
				return;
			}

			if (target == null)
			{
				partyLeader.sendPacket(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE);
				return;
			}

			if (target == partyLeader)
			{
				partyLeader.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
				partyLeader.sendActionFailed();
				return;
			}

			if (target.isBusy())
			{
				partyLeader.sendPacket(new SystemMessage(SystemMsg.C1_IS_ON_ANOTHER_TASK).addName(target));
				return;
			}

			IBroadcastPacket problem = target.canJoinParty(partyLeader);
			if (problem != null)
			{
				partyLeader.sendPacket(problem);
				return;
			}

			if (partyLeader.isInParty())
			{
				if (partyLeader.getParty().getMemberCount() >= Party.MAX_SIZE)
				{
					partyLeader.sendPacket(SystemMsg.THE_PARTY_IS_FULL);
					return;
				}

				// Only the Party Leader may invite new members
				if (Config.PARTY_LEADER_ONLY_CAN_INVITE && !partyLeader.getParty().isLeader(partyLeader))
				{
					partyLeader.sendPacket(SystemMsg.ONLY_THE_LEADER_CAN_GIVE_OUT_INVITATIONS);
					return;
				}
			}

			int itemDistribution = partyLeader.getParty() == null ? 0 : partyLeader.getParty().getLootDistribution();
			Party party = partyLeader.getParty();
			if (party == null)
			{
				partyLeader.setParty(party = new Party(partyLeader, itemDistribution));
			}

			target.joinParty(party, false);
			partyLeader.sendPacket(JoinPartyPacket.SUCCESS);
		}
	}

	private static class FindPartyRequest
	{
		final int _requestorObjId;
		long _requestStartTimeMilis;
		String _message;

		public FindPartyRequest(Player player, String msg)
		{
			_requestorObjId = player.getObjectId();
			_requestStartTimeMilis = System.currentTimeMillis();
			_message = msg == null ? "" : msg;
		}

		public void update()
		{
			_requestStartTimeMilis = System.currentTimeMillis();
		}

		public void update(String newMsg)
		{
			_requestStartTimeMilis = System.currentTimeMillis();
			_message = newMsg;
		}
	}
}