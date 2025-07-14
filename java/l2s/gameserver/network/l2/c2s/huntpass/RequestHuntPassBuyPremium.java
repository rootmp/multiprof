package l2s.gameserver.network.l2.c2s.huntpass;

import java.util.Calendar;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassInfo;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassSayhasSupportInfo;

public class RequestHuntPassBuyPremium implements IClientIncomingPacket
{
	private int _huntPassType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_huntPassType = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		final Calendar calendar = Calendar.getInstance();
		if((calendar.get(Calendar.DAY_OF_MONTH) == Config.HUNT_PASS_PERIOD) && (calendar.get(Calendar.HOUR_OF_DAY) == 6)
				&& (calendar.get(Calendar.MINUTE) < 30))
		{
			player.sendPacket(new SystemMessage(SystemMsg.CURRENTLY_UNAVAILABLE_FOR_PURCHASE_YOU_CAN_BUY_THE_SEASON_PASS_ADDITIONAL_REWARDS_ONLY_UNTIL_6_30_A_M_OF_THE_SEASON_S_LAST_DAY));
			return;
		}

		if(!player.getInventory().destroyItemByItemId(Config.HUNT_PASS_PREMIUM_ITEM_ID, Config.HUNT_PASS_PREMIUM_COST))
		{
			player.sendPacket(SystemMsg.NOT_ENOUGH_MONEY_TO_USE_THE_FUNCTION);
			return;
		}

		player.getHuntPass().setPremium(true);
		player.sendPacket(new HuntPassSayhasSupportInfo(player));
		player.sendPacket(new HuntPassInfo(player, _huntPassType));
	}
}