package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExChatBackgroundSettingNoti;

public class RequestExChatBackgroundSetting implements IClientIncomingPacket
{
	private int nCurrentChatBackground;
	private boolean bEnable;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nCurrentChatBackground = packet.readD();
		bEnable = packet.readC() == 1;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		player.setUseChatBg(bEnable);
		player.setChatBg(nCurrentChatBackground);
		player.sendPacket(SystemMsg.NAME_BACKGROUND_SETTINGS_HAVE_BEEN_CHANGED);
		player.sendPacket(new ExChatBackgroundSettingNoti(player));
	}

}
