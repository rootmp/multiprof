package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Friend;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExFriendDetailInfo;

/**
 * @author Bonux
 **/
public class RequestFriendDetailInfo implements IClientIncomingPacket
{
	private String _name;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_name = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Friend friend = activeChar.getFriendList().get(_name);
		if (friend == null)
			return;

		activeChar.sendPacket(new ExFriendDetailInfo(activeChar, friend));
	}
}