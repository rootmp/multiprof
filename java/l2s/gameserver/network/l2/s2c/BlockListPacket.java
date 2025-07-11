package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Block;

/**
 * @author Bonux
 */
public class BlockListPacket implements IClientOutgoingPacket
{
	private Block[] _blockList;

	public BlockListPacket(Player player)
	{
		_blockList = player.getBlockList().values();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_blockList.length);
		for (Block b : _blockList)
		{
			packetWriter.writeS(b.getName());
			packetWriter.writeS(b.getMemo());
		}
		return true;
	}
}