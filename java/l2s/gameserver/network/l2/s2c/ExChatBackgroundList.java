package l2s.gameserver.network.l2.s2c;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class ExChatBackgroundList implements IClientOutgoingPacket
{
	private List<Integer> chatBackgrounds;
	
	public ExChatBackgroundList(Player player)
	{
		chatBackgrounds = player.getVarIntegerList("chatBackgrounds");
	}

	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(chatBackgrounds.size());
		for(Integer bg : chatBackgrounds)
		{
			packetWriter.writeD(bg);
		}
		return true;
	}
}