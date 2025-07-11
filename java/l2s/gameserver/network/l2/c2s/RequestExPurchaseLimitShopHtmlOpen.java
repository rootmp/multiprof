package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.utils.DimensionalMerchantUtils;

public class RequestExPurchaseLimitShopHtmlOpen implements IClientIncomingPacket
{
	private int _type;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_type = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		if (player.isInJail())
		{
			player.sendActionFailed();
			return;
		}

		switch (_type)
		{
			case 5: // Dimensional Merchant
			{
				DimensionalMerchantUtils.showLimitShopHtml(player, "premium_manager.htm", true);
				break;
			}
		}
	}
}
