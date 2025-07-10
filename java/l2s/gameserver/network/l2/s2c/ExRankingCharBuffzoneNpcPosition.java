package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExRankingCharBuffzoneNpcPosition implements IClientOutgoingPacket
{
	private byte _active;
	private int _locX;
	private int _locY;
	private int _locZ;

	public ExRankingCharBuffzoneNpcPosition(byte isActive, int x, int y, int z)
	{
		_active = isActive;
		_locX = x;
		_locY = y;
		_locZ = z;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_active); // is active
		packetWriter.writeD(_locX); // x
		packetWriter.writeD(_locY); // y
		packetWriter.writeD(_locZ); // z
	}
}
