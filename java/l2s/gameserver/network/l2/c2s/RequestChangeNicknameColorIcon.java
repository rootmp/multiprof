package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.handler.items.impl.NameColorItemHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.utils.GameStringUtils;

public class RequestChangeNicknameColorIcon implements IClientIncomingPacket
{
	private static final int COLORS[] =
	{
		0x9393FF, // Pink
		0x7C49FC, // Rose Pink
		0x97F8FC, // Lemon Yellow
		0xFA9AEE, // Lilac
		0xFF5D93, // Cobalt Violet
		0x00FCA0, // Mint Green
		0xA0A601, // Peacock Green
		0x7898AF, // Yellow Ochre
		0x486295, // Chocolate
		0x999999, // Silver
		0xF3DC09, // Heaven Cyan
		0x05D3F6, // True Gold
		0x3CB1F4, // True Orange
		0xF383F3, // True Pink
		0x0909F3, // True Red
		0xF3DC09 // Heaven Cyan
	};

	private int _colorNum, _itemId;
	private String _title;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemId = packet.readD();
		_colorNum = packet.readD();
		_title = readString();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (_colorNum < 0 || _colorNum >= COLORS.length)
			return;

		ItemInstance item = activeChar.getInventory().getItemByItemId(_itemId);
		if (item == null)
			return;

		if (!(item.getTemplate().getHandler() instanceof NameColorItemHandler))
			return;

		_title = GameStringUtils.checkTitle(_title, 16, item.getItemId() != 95892);

		if (activeChar.consumeItem(item.getItemId(), 1, true))
		{
			activeChar.setTitleColor(COLORS[_colorNum]);
			activeChar.setTitle(_title);
			activeChar.broadcastUserInfo(true);
		}
	}
}