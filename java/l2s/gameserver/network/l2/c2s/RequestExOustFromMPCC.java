package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;

public class RequestExOustFromMPCC implements IClientIncomingPacket
{
	private String _name;

	/**
	 * format: chS
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_name = packet.readS(16);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null || !activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
			return;

		Player target = World.getPlayer(_name);

		// Чар с таким имененм не найден в мире
		if (target == null)
		{
			activeChar.sendPacket(SystemMsg.THAT_PLAYER_IS_NOT_CURRENTLY_ONLINE);
			return;
		}

		// Сам себя нельзя
		if (activeChar == target)
			return;

		// Указанный чар не в пати, не в СС, в чужом СС
		if (!target.isInParty() || !target.getParty().isInCommandChannel() || activeChar.getParty().getCommandChannel() != target.getParty().getCommandChannel())
		{
			activeChar.sendPacket(SystemMsg.INVALID_TARGET);
			return;
		}

		// Это может делать только лидер СС
		if (activeChar.getParty().getCommandChannel().getChannelLeader() != activeChar)
		{
			activeChar.sendPacket(SystemMsg.ONLY_THE_CREATOR_OF_A_COMMAND_CHANNEL_CAN_ISSUE_A_GLOBAL_COMMAND);
			return;
		}

		target.getParty().getCommandChannel().getChannelLeader().sendPacket(new SystemMessagePacket(SystemMsg.C1S_PARTY_HAS_BEEN_DISMISSED_FROM_THE_COMMAND_CHANNEL).addName(target));
		target.getParty().getCommandChannel().removeParty(target.getParty());
		target.getParty().broadCast(SystemMsg.YOU_WERE_DISMISSED_FROM_THE_COMMAND_CHANNEL);
	}
}