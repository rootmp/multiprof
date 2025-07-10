package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.BookMark;

/**
 * dd d*[ddddSdS]
 */
public class ExGetBookMarkInfoPacket implements IClientOutgoingPacket
{
	private final int bookmarksCapacity;
	private final BookMark[] bookmarks;

	public ExGetBookMarkInfoPacket(Player player)
	{
		bookmarksCapacity = player.getBookMarkList().getCapacity();
		bookmarks = player.getBookMarkList().toArray();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00); // должно быть 0
		packetWriter.writeD(bookmarksCapacity);
		packetWriter.writeD(bookmarks.length);
		int slotId = 0;
		for (BookMark bookmark : bookmarks)
		{
			packetWriter.writeD(++slotId);
			packetWriter.writeD(bookmark.x);
			packetWriter.writeD(bookmark.y);
			packetWriter.writeD(bookmark.z);
			packetWriter.writeS(bookmark.getName());
			packetWriter.writeD(bookmark.getIcon());
			packetWriter.writeS(bookmark.getAcronym());
		}
	}
}