package l2s.gameserver.network.l2.c2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.utils.BypassStorage.ValidBypass;
import l2s.gameserver.utils.DimensionalMerchantUtils;
import l2s.gameserver.utils.Util;

public class RequestExPremiumManagerLinkHtml implements IClientIncomingPacket
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestExPremiumManagerLinkHtml.class);

	private String _link;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_link = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;

		ValidBypass bp = player.getBypassStorage().validate(_link);
		if(bp == null)
		{
			LOGGER.warn("RequestExPremiumManagerLinkHtml: Unexpected link : " + _link + " from player " + player.getName() + " at location "
					+ player.getLoc() + "!");
			return;
		}

		String link = bp.bypass;
		int itemId = 0;

		String[] params = link.split(".htm#");
		if(params.length >= 2)
		{
			link = params[0] + ".htm";
			itemId = !Util.isDigit(params[1]) ? -1 : Integer.parseInt(params[1]);
		}

		if(link.contains("..") || !link.endsWith(".htm") || itemId == -1)
		{
			LOGGER.warn("RequestExPremiumManagerLinkHtml: hack? link contains prohibited characters: '" + link + "'!");
			return;
		}

		DimensionalMerchantUtils.showLimitShopHtml(player, link, true);
	}
}