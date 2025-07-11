package l2s.gameserver.network.l2.c2s.enchant;

import l2s.gameserver.data.xml.holder.EnchantItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.enchant.ExResetSelectMultiEnchantScroll;
import l2s.gameserver.templates.item.support.EnchantScroll;

/**
 * @author nexvill
 */
public class RequestExStartMultiEnchantScroll implements IClientIncomingPacket
{
	private int _scrollObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_scrollObjId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		ItemInstance scroll = player.getInventory().getItemByObjectId(_scrollObjId);
		if (scroll == null)
		{
			return;
		}
		final EnchantScroll enchantScroll = EnchantItemHolder.getInstance().getEnchantScroll(scroll.getItemId());
		if (enchantScroll == null)
		{
			player.sendPacket(new ExResetSelectMultiEnchantScroll(_scrollObjId, 1));
			return;
		}

		player.setEnchantScroll(scroll);
		player.sendPacket(new ExResetSelectMultiEnchantScroll(_scrollObjId, 0));
	}
}