package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.cache.ItemInfoCache;
import l2s.gameserver.model.items.ItemInfo;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ActionFailPacket;
import l2s.gameserver.network.l2.s2c.ExRpItemLink;

public class RequestExRqItemLink implements IClientIncomingPacket
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
		ItemInfo item;
		if ((item = ItemInfoCache.getInstance().get(_objectId)) == null)
		{
			client.sendPacket(ActionFailPacket.STATIC);
		}
		else
		{
			client.sendPacket(new ExRpItemLink(item));
			client.getActiveChar().getListeners().onQuestionMarkClicked(_objectId);
		}
	}
}