package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class ExChatBackgroundSettingNoti implements IClientOutgoingPacket
{
	private int nCurrentChatBackground;
	private boolean bEnable;

	public ExChatBackgroundSettingNoti(Player player)
	{
		nCurrentChatBackground = player.getChatBg();
		bEnable = player.isUseChatBg();
	}

	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nCurrentChatBackground);
		packetWriter.writeC(bEnable);
		return true;
	}
}