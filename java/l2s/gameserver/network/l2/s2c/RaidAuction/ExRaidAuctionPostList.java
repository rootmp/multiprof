package l2s.gameserver.network.l2.s2c.RaidAuction;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.item.PkRaidAuctionPost;

public class ExRaidAuctionPostList implements IClientOutgoingPacket
{
	private List<PkRaidAuctionPost> _posts;

	public ExRaidAuctionPostList(List<PkRaidAuctionPost> posts)
	{
		_posts = posts;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_posts.size());
		for(PkRaidAuctionPost post : _posts)
		{
			packetWriter.writeD(post.nPostID);
			packetWriter.writeD(post.nType);
			packetWriter.writeD(post.nItemClassID);
			packetWriter.writeQ(post.nAmount);
			packetWriter.writeD(post.nRemainTime);
		}
		return true;
	}
}
