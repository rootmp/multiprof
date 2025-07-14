package l2s.gameserver.network.l2.c2s;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.MultiSellHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExShowVariationCancelWindow;
import l2s.gameserver.network.l2.s2c.ExShowVariationMakeWindow;
import l2s.gameserver.network.l2.s2c.PackageToListPacket;
import l2s.gameserver.utils.BypassStorage.ValidBypass;
import l2s.gameserver.utils.DimensionalMerchantUtils;
import l2s.gameserver.utils.WarehouseFunctions;

public class RequestExPremiumManagerPassCmdToServer implements IClientIncomingPacket
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestExPremiumManagerPassCmdToServer.class);

	private String _bypass = null;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_bypass = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if (_bypass.isEmpty())
			return;

		Player player = client.getActiveChar();
		if (player == null)
			return;

		if (player.isInJail())
		{
			player.sendActionFailed();
			return;
		}

		ValidBypass bp = player.getBypassStorage().validate(_bypass);
		if (bp == null)
		{
			LOGGER.debug("RequestExPremiumManagerPassCmdToServer: Unexpected bypass : " + _bypass + " client : " + client + "!");
			return;
		}

		try
		{
			if (player.isGM())
				player.sendMessage("premium bypass:" + bp.bypass);

			if (bp.bypass.startsWith("menu_select_dimensional?"))
			{
				String params = bp.bypass.substring(bp.bypass.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int ask = Integer.parseInt(st.nextToken().split("=")[1]);
				long reply = st.hasMoreTokens() ? Long.parseLong(st.nextToken().split("=")[1]) : 0L;
				int state = st.hasMoreTokens() ? Integer.parseInt(st.nextToken().split("=")[1]) : 0;
				DimensionalMerchantUtils.onMenuSelect(null, player, ask, reply, state);
			}
			else if (bp.bypass.equals("package_deposit"))
			{
				player.sendPacket(new PackageToListPacket(player));
			}
			else if (bp.bypass.equals("package_withdraw"))
			{
				WarehouseFunctions.showFreightWindow(player);
			}
			else if (bp.bypass.startsWith("multisell "))
			{
				int multisellId = Integer.parseInt(bp.bypass.substring(10).trim());
				MultiSellHolder.getInstance().SeparateAndSend(multisellId, player, 0);
			}
			else if (bp.bypass.startsWith("Augment "))
			{
				if (Config.ALLOW_AUGMENTATION)
				{
					int cmdChoice = Integer.parseInt(bp.bypass.substring(8).trim());
					if (cmdChoice == 1)
						player.sendPacket(SystemMsg.SELECT_THE_ITEM_TO_BE_AUGMENTED, ExShowVariationMakeWindow.STATIC);
					else if (cmdChoice == 2)
						player.sendPacket(SystemMsg.SELECT_THE_ITEM_FROM_WHICH_YOU_WISH_TO_REMOVE_AUGMENTATION, ExShowVariationCancelWindow.STATIC);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error while handling bypass: " + bp.bypass, e);
		}
	}
}