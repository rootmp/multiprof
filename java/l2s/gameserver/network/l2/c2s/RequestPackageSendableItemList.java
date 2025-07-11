package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.PackageSendableListPacket;

/**
 * @author VISTALL
 * @date 20:35/16.05.2011
 */
public class RequestPackageSendableItemList implements IClientIncomingPacket
{
	private int _objectId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new PackageSendableListPacket(1, _objectId, player));
		player.sendPacket(new PackageSendableListPacket(2, _objectId, player));
	}
}
