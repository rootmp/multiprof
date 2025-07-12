package l2s.gameserver.network.l2.c2s.huntpass;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.HuntPass;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassSayhasSupportInfo;

public class HuntpassSayhasToggle implements IClientIncomingPacket
{
	private boolean _sayhaToggle;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_sayhaToggle = packet.readC() != 0;
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		final HuntPass huntPass = player.getHuntPass();
		if (huntPass == null)
		{
			return;
		}
		
		int timeEarned = huntPass.getAvailableSayhaTime();
		int timeUsed = huntPass.getUsedSayhaTime();
		if (player.getSayhasGrace() < 35000)
		{
			player.sendPacket(new SystemMessagePacket(SystemMsg.UNABLE_TO_ACTIVATE_YOU_CAN_USE_SAYHA_S_GRACE_SUSTENTION_EFFECT_OF_THE_SEASON_PASS_ONLY_IF_YOU_HAVE_AT_LEAST_35_000_SAYHA_S_GRACE_POINTS));
			return;
		}
		
		if (_sayhaToggle && (timeEarned > 0) && (timeEarned > timeUsed))
		{
			huntPass.setSayhasSustention(true);
		}
		else
		{
			huntPass.setSayhasSustention(false);
		}
		player.sendPacket(new HuntPassSayhasSupportInfo(player));
	}
	
}
